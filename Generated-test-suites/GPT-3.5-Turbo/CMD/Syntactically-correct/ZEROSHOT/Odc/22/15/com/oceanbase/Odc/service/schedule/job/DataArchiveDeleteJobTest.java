package com.oceanbase.odc.service.schedule.job;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @Test
    public void testExecute() {
        // Mocking data for testing
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setJobName("TestJob");
        taskEntity.setJobGroup("TestGroup");
        
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));
        
        List<DlmTask> taskUnits = Arrays.asList(new DlmTask());
        when(JsonUtils.fromJson(taskEntity.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);

        // Test the execute method
        dataArchiveDeleteJob.execute(jobExecutionContext);

        // Validate the status after execution
        assertEquals(TaskStatus.PREPARING, taskUnits.get(0).getStatus());
        assertEquals(TaskStatus.PREPARING, taskEntity.getStatus());
    }
}