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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveRollbackJobTest {

    @Mock
    private DataArchiveRollbackJob job;
    @Mock
    private JobExecutionContext context;
    @Mock
    private ScheduleTaskEntity taskEntity;
    @Mock
    private ScheduleTaskEntity dataArchiveTask;
    @Mock
    private DlmTask taskUnit;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        job = new DataArchiveRollbackJob();
        taskEntity = mock(ScheduleTaskEntity.class);
        dataArchiveTask = mock(ScheduleTaskEntity.class);
    }

    @Test
    public void testExecute() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("{\"dataArchiveTaskId\":1}");
        when(scheduleTaskRepository.findById(1)).thenReturn(Optional.of(dataArchiveTask));
        when(taskUnit.getSourceDatabaseId()).thenReturn(1L);
        when(taskUnit.getTargetDatabaseId()).thenReturn(2L);
        when(taskUnit.getStatus()).thenReturn(TaskStatus.PREPARING);
        
        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(taskUnit);
        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {}))
                .thenReturn(taskUnits);

        job.execute(context);

        assertEquals(JobType.ROLLBACK, taskUnit.getJobType());
        assertEquals(TaskStatus.DONE, taskUnit.getStatus());
    }
}