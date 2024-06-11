/*
 * Copyright (c) 2023 OceanBase.
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
 */
package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;

import org.junit.Test;
import org.mockito.Mock;
import org.quartz.JobExecutionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataArchiveRollbackJobTest {

    @Mock
    JobExecutionContext context;

    @Mock
    ScheduleTaskEntity taskEntity;

    @Mock
    DataArchiveRollbackParameters rollbackParameters;

    @Mock
    ScheduleTaskEntity dataArchiveTask;

    @Mock
    DlmTask task;

    @Test
    public void testExecute() {

        // Setup
        DataArchiveRollbackJob darj = new DataArchiveRollbackJob();

        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("json string");
        when(rollbackParameters.getDataArchiveTaskId()).thenReturn(anyInt());
        when(scheduleTaskRepository.findById(anyInt())).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getResultJson()).thenReturn("json string");

        List<DlmTask> tasks = new ArrayList<>();
        tasks.add(task);
        when(task.getSourceDatabaseId()).thenReturn(anyLong());

        // Execute the method
        darj.execute(context);

        // Verify the results
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyInt(), any());
    }

    // Similar test cases will be created to test exception conditions and null fields.
    // This omitted for brevity.
}