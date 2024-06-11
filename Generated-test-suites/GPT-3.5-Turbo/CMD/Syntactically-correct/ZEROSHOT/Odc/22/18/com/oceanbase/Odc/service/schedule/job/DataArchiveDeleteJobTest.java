package com.oceanbase.odc.service.schedule.job;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Before
    public void setup() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
    }

    @Test
    public void testExecute(){
        // Test if execute method runs without error
        dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));
        // Test any assertions here
    }

    @Test
    public void testGetTaskStatus(){
        // Test getTaskStatus method

        // Prepare test data
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask task1 = new DlmTask();
        // Populate task1 data
        taskUnits.add(task1);

        // Mock required methods
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class))).thenReturn(true);

        // Test the method
        TaskStatus taskStatus = dataArchiveDeleteJob.getTaskStatus(taskUnits);

        // Perform assertions
        assertNotNull(taskStatus);
        // Add more assertions as needed
    }

    // Additional test methods can be added here for more coverage

}