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

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import java.util.ArrayList;
import java.util.List;

public class DataArchiveDeleteJobTest {

    @Mock
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Mock
    private JobExecutionContext context;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecuteDataArchiveDeleteJob() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setResult(context.getResult());
        taskEntity.setParametersJson(null);
        taskEntity.setId(1);
        taskEntity.setJobName("test");
        taskEntity.setJobGroup("group");
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(taskEntity));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.DONE);
        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId("test_id");
        dlmTask.setStatus(TaskStatus.PREPARING);
        dlmTasks.add(dlmTask);

        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(dataArchiveTask));
        doNothing().when(dataArchiveDeleteJob).executeTask(any(), any());
        when(dataArchiveDeleteJob.getTaskStatus(dlmTasks)).thenReturn(TaskStatus.DONE);
        doNothing().when(scheduleTaskRepository).updateStatusById(any(), any());

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository, times(1)).findById(any());
        verify(scheduleTaskRepository, times(1)).updateStatusById(any(), any());
        verify(dataArchiveDeleteJob, times(1)).executeTask(any(), any());
    }
}