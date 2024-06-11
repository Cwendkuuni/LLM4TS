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

import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;
import org.quartz.JobExecutionContext;
import org.mockito.Mockito;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;

public class DataArchiveDeleteJobTest {
  
    @Test
    public void testDataArchiveDeleteJobExecuteTask() {
        // Arrange
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters(); 

        Long id = 1L;
        dataArchiveClearParameters.setDataArchiveTaskId(id);
        taskEntity.setId(id);
        taskEntity.setParametersJson(dataArchiveClearParameters);

        Mockito.when(context.getResult()).thenReturn(taskEntity);

        // Act
        dataArchiveDeleteJob.execute(context);

        // Assert
        // Check whether the execute method of DataArchiveDeleteJob was called
        Mockito.verify(context, Mockito.times(1)).getResult();
    }
}