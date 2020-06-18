/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.devbyteviewer.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.android.devbyteviewer.database.getDatabase
import com.example.android.devbyteviewer.repository.VideosRepository
import retrofit2.HttpException

//work manager class that does work on background thread
class RefreshDataWork(appContext: Context, params: WorkerParameters):
        CoroutineWorker(appContext, params){
    companion object{
        const val WORK_NAME = "RefreshDataWorker"
    }
    //doWork runs on background thread so it does not block the ui
    override suspend fun doWork(): Payload {

        //get the database
        val database = getDatabase(applicationContext)
        //get the repository
        val repository = VideosRepository(database)

        return try {
            //refresh the videos
            repository.refreshVideos()
            //tell work manager it succeeds
            Payload(Result.SUCCESS)

        } catch (exception: HttpException){
            //if the work fails, retry
            Payload(Result.RETRY)
        }

    }
}
