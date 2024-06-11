
/**
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

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import java.lang.reflect.Field;
import java.util.List;
import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {

    @Mock
    private JobExecutionContext context;
    @Mock
    private ScheduleTaskEntity taskEntity;

    private DataArchiveRollbackJob job;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        job = new DataArchiveRollbackJob();
        Field field = DataArchiveRollbackJob.class.getDeclaredField("context");
        field.setAccessible(true);
        field.set(job, context);
    }

    @Test
    public void executeTest() {

        when(context.getResult()).thenReturn(taskEntity);
        DataArchiveRollbackParameters parameters = new DataArchiveRollbackParameters();
        parameters.setDataArchiveTaskId("taskId");

        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(parameters));
        job.execute(context);

        verify(context, times(1)).getResult();
        verify(taskEntity, times(1)).getParametersJson();

        List<DlmTask> dlmTasks = job.getDalTasks();
        Assert.assertNotNull("Dlm task was not set.", dlmTasks);
        Assert.assertEquals("Dlm task size mismatch.", 1, dlmTasks.size());
        Assert.assertEquals("Task status incorrect.", TaskStatus.DONE, dlmTasks.get(0).getStatus());
        Assert.assertEquals("Job type incorrect.", JobType.ROLLBACK, dlmTasks.get(0).getJobType());
    }
}
