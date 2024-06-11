package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveRollbackJobTest {

    @Test
    public void testExecute() {
        DataArchiveRollbackJob job = new DataArchiveRollbackJob();
        
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setJobName("TestJob");
        taskEntity.setJobGroup("TestGroup");
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 1}");
        taskEntity.setResultJson("[{\"id\": 1, \"sourceDatabaseId\": 1, \"targetDatabaseId\": 2,"
                + "\"status\": \"PREPARING\"}]");
        
        ScheduleTaskRepository mockRepository = Mockito.mock(ScheduleTaskRepository.class);
        Mockito.when(mockRepository.findById(1L)).thenReturn(Optional.of(taskEntity));
        
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        Mockito.when(context.getResult()).thenReturn(taskEntity);
        
        job.setScheduleTaskRepository(mockRepository);
        
        job.execute(context);
        
        List<DlmTask> taskUnits = JsonUtils.fromJson(taskEntity.getResultJson(), new TypeReference<List<DlmTask>>() {});
        
        assertNotNull(taskUnits);
        assertEquals(1, taskUnits.size());
        
        DlmTask taskUnit = taskUnits.get(0);
        assertEquals("Rollback", taskUnit.getJobType());
        assertEquals(TaskStatus.DONE, taskUnit.getStatus());
        // Add more asserts based on the logic in the execute method
        
        // Test case for taskEntity not present
        ScheduleTaskEntity invalidTaskEntity = new ScheduleTaskEntity();
        invalidTaskEntity.setId(2L);
        invalidTaskEntity.setJobName("InvalidJob");
        invalidTaskEntity.setJobGroup("InvalidGroup");
        invalidTaskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
        
        Mockito.when(mockRepository.findById(2L)).thenReturn(Optional.empty());
        
        context = Mockito.mock(JobExecutionContext.class);
        Mockito.when(context.getResult()).thenReturn(invalidTaskEntity);
        
        job.execute(context);
        
        // Add asserts for the case when data archive task is not found
        
        // Add more test cases to cover different scenarios in the execute method
    }
}