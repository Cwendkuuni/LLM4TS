package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveDeleteJobTest {

    @Test
    public void testExecute() {
        DataArchiveDeleteJob job = new DataArchiveDeleteJob();
        JobExecutionContext context = new TestJobExecutionContext();
        
        // Test dataArchiveTask not found
        context.setResult(null);
        job.execute(context);
        assertEquals(TaskStatus.FAILED, context.getResult().getStatus());
        
        // Test dataArchiveTask is not DONE
        ScheduleTaskEntity taskEntityNotDone = new ScheduleTaskEntity();
        taskEntityNotDone.setStatus(TaskStatus.IN_PROGRESS);
        context.setResult(taskEntityNotDone);
        job.execute(context);
        assertEquals(TaskStatus.FAILED, context.getResult().getStatus());
        
        // Test dataArchiveTask is DONE
        ScheduleTaskEntity taskEntityDone = new ScheduleTaskEntity();
        taskEntityDone.setStatus(TaskStatus.DONE);
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("1");
        taskEntityDone.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
        
        job.execute(context);
        assertEquals(TaskStatus.PREPARING, context.getResult().getStatus());
        
        // Additional tests here...
    }
    
    // Helper class for mock JobExecutionContext
    private static class TestJobExecutionContext implements JobExecutionContext {
        @Override
        public ScheduleTaskEntity getResult() {
            return new ScheduleTaskEntity();
        }
        
        @Override
        public void setResult(ScheduleTaskEntity entity) {
            // Do nothing for testing
        }
        
        // Implement other needed methods for testing
    }
}