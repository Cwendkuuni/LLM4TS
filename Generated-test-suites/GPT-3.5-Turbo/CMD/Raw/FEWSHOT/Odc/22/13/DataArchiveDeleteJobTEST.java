package com.oceanbase.odc.service.schedule.job;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

    @Test
    public void testExecute() {

        // Mocking JobExecutionContext
        JobExecutionContext context = mock(JobExecutionContext.class);

        // Mocking dataArchiveClearParameters
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(123L);
        String parametersJson = "{\"dataArchiveTaskId\":123}";
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson(parametersJson);

        // Mocking dataArchiveTaskOption
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(123L);
        dataArchiveTask.setStatus(TaskStatus.DONE);

        // Mocking taskUnits
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setId("test-id");
        taskUnit.setStatus(TaskStatus.PENDING);
        taskUnits.add(taskUnit);

        // Setting up mock behaviors
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        when(JsonUtils.fromJson(anyString(), eq(new TypeReference<List<DlmTask>>() {}))).thenReturn(taskUnits);

        // Creating DataArchiveDeleteJob instance
        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.scheduleTaskRepository = mock(ScheduleTaskRepository.class);

        // Calling execute method
        dataArchiveDeleteJob.execute(context);

        // Verifying the expected behaviors
        verify(dataArchiveDeleteJob.scheduleTaskRepository, times(1)).updateStatusById(eq(123L), eq(TaskStatus.PREPARING));
        assertEquals(TaskStatus.PREPARING, taskUnit.getStatus());
        assertEquals("DELETE", taskUnit.getJobType().toString());
    }
}