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

import java.util.ArrayList;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleTaskEntity taskEntity;

    @Mock
    private ScheduleTaskEntity dataArchiveTask;

    @Mock
    private DlmTask dlmTask;

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private DataArchiveRollbackParameters rollbackParameters;

    @Before
    public void setup(){
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson())
                .thenReturn(rollbackParameters);
        when(taskEntity.getId()).thenReturn(1L);
    }

    @Test
    public void testExecuteTaskWithNoDataArchiveTaskPresent(){
        when(taskEntity.getStatus()).thenReturn(TaskStatus.FAILED);
        dataArchiveRollbackJob.execute(context);
        verify(taskEntity, times(1)).getStatus();
    }

    @Test
    public void testExecuteTaskWithDataArchiveTaskPresent(){
        when(taskEntity.getStatus()).thenReturn(TaskStatus.PREPARING);
        when(dataArchiveTask.getResultJson()).thenReturn(new ArrayList<>());
        when(dlmTask.getStatus()).thenReturn(TaskStatus.DONE);
        dataArchiveRollbackJob.execute(context);
        verify(dlmTask, times(1)).getStatus();
    }
}