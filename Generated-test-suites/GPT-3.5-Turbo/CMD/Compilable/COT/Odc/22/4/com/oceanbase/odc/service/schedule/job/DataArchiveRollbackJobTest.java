package com.oceanbase.odc.service.schedule.job;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;

public class DataArchiveRollbackJobTest {

    @Test
    public void testExecute() {
        // Prepare data
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        String parametersJson = JsonUtils.toJson(rollbackParameters);
        taskEntity.setParametersJson(parametersJson);

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getResult()).thenReturn(taskEntity);

        DataArchiveRollbackJob job = new DataArchiveRollbackJob();
        job.execute(context);

        // Validate the functionality of execute method
        // Add validation code here
    }

    // Add additional test cases for other public methods if needed
}
