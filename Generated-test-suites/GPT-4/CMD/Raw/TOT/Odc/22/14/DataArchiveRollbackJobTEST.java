
package com.oceanbase.odc.service.schedule.job;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DataArchiveRollbackJobTest {

    @Mock
    private JobExecutionContext context;
    
    @Mock
    private ScheduleTaskEntity taskEntity;
    
    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @BeforeAll
    static void setup() {
        // setup the Mocking data here
    }

    @Test
    public void executeTestWithNonExistingDataArchiveTask() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("{\"dataArchiveTaskId\": \"non_existing_task\"}");

        doNothing().when(scheduleTaskRepository).updateStatusById(anyTaskId, any(TaskStatus.class));

        dataArchiveRollbackJob.execute(context);

        ArgumentCaptor<String> taskIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<TaskStatus> taskStatusCaptor = ArgumentCaptor.forClass(TaskStatus.class);

        verify(scheduleTaskRepository, times(1)).updateStatusById(taskIdCaptor.capture(), taskStatusCaptor.capture());

        TaskStatus status = taskStatusCaptor.getValue();
        String taskId = taskIdCaptor.getValue();

        assertEquals(TaskStatus.FAILED, status);
        assertEquals("non_existing_task", taskId);
    }
}
