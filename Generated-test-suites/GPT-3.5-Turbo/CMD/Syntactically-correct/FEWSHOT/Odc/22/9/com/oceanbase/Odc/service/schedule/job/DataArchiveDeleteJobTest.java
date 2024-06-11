package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mock;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;

public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext context;

    @Test
    public void testExecute_Success() {
        DataArchiveDeleteJob job = new DataArchiveDeleteJob();
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        
        DataArchiveClearParameters parameters = new DataArchiveClearParameters();
        parameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(parameters.toString());

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit1 = new DlmTask();
        taskUnit1.setId(1L);
        taskUnit1.setStatus(TaskStatus.DONE);
        taskUnits.add(taskUnit1);

        taskEntity.setResultJson(taskUnits.toString());

        // Mock repository calls

        job.execute(context);

        // Asserts

        // Assert task entity status updated correctly
        assertNotNull(taskEntity);
        assertEquals(TaskStatus.DONE, taskEntity.getStatus());

        // Add more assertions as needed
    }

    @Test
    public void testExecute_TaskNotFound() {
        DataArchiveDeleteJob job = new DataArchiveDeleteJob();
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        
        DataArchiveClearParameters parameters = new DataArchiveClearParameters();
        parameters.setDataArchiveTaskId(999L);
        taskEntity.setParametersJson(parameters.toString());

        // Mock repository calls

        job.execute(context);

        // Asserts

        // Assert task entity status updated correctly
        assertNotNull(taskEntity);
        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());

        // Add more assertions as needed
    }

    // Add more test methods for edge cases and error handling as needed

}