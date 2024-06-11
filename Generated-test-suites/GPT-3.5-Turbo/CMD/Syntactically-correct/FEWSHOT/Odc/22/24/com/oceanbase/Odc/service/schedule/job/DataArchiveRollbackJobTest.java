package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DataArchiveRollbackJobTest {

    @Test
    public void testExecute() {
        // Test case for valid taskEntity and rollbackParameters
        DataArchiveRollbackJob job = new DataArchiveRollbackJob();
        JobExecutionContext context = new JobExecutionContext();
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1);
        taskEntity.setParametersJson("{ \"dataArchiveTaskId\": 123 }");
        context.setResult(taskEntity);
        
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(123);
        dataArchiveTask.setResultJson("[{\"id\": 1, \"sourceDatabaseId\": 1, \"targetDatabaseId\": 2, \"status\": \"PREPARING\"}]");
        
        ScheduleTaskRepository scheduleTaskRepository = new ScheduleTaskRepository();
        scheduleTaskRepository.save(dataArchiveTask);
        
        job.execute(context);
        
        assertNotNull(dataArchiveTask.getResultJson());
        
        // Test case for when data archive task not found
        taskEntity.setParametersJson("{ \"dataArchiveTaskId\": 456 }");
        
        job.execute(context);
        
        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
        
        // Test case for invalid parametersJson format
        taskEntity.setParametersJson("invalidJson");
        
        job.execute(context);
        
        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }
}