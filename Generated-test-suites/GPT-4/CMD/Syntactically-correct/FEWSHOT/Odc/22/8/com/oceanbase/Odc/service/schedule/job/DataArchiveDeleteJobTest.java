/*
 * Package for the test classes. 
 * Please replace with the package of the DataArchiveDeleteJob when using.
 */
package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;

import java.util.Optional;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private JobExecutionContext context;
    private ScheduleTaskEntity taskEntity;
    private DataArchiveClearParameters parameters;
    
    @Before
    public void setUp() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        context = mock(JobExecutionContext.class);
        taskEntity = mock(ScheduleTaskEntity.class);
        parameters = mock(DataArchiveClearParameters.class);
    }

    @Test
    public void testExecute_NoDataArchiveTaskFound() {
        when(context.getResult()).thenReturn(taskEntity);
        when(parameters.getDataArchiveTaskId()).thenReturn("1");
        when(taskEntity.getParametersJson()).thenReturn("{}");
        when(taskEntity.getId()).thenReturn("1");
        when(dataArchiveDeleteJob.scheduleTaskRepository.findById(anyString())).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(context);

        verify(dataArchiveDeleteJob.scheduleTaskRepository, times(1)).updateStatusById(anyString(), eq(TaskStatus.FAILED));
    }
    
    @Test
    public void testExecute_DataArchiveTaskNotDone() {
        when(context.getResult()).thenReturn(taskEntity);
        when(parameters.getDataArchiveTaskId()).thenReturn("2");
        when(taskEntity.getParametersJson()).thenReturn("{}");
        when(taskEntity.getId()).thenReturn("2");
        when(dataArchiveDeleteJob.scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(taskEntity)); 
        when(taskEntity.getStatus()).thenReturn(TaskStatus.PREPARING);

        dataArchiveDeleteJob.execute(context);

        verify(dataArchiveDeleteJob.scheduleTaskRepository, times(1)).updateStatusById(anyString(), eq(TaskStatus.FAILED));
    }
    
    @Test
    public void testExecute_TaskStatusUpdated() {
        /*
            Implement this unit test to test the rest of the conditions under the execute method.
            Note: In order to fully test all conditions in the DataArchiveDeleteJob class it may require breaks into multiple test cases.
        */
    }
}