import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private ScheduleService scheduleService;
    @Mock
    private ScheduleTaskService scheduleTaskService;
    @Mock
    private QuartzJobService quartzJobService;
    @Mock
    private OnlineSchemaChangeTaskHandler taskHandler;
    @Mock
    private OscTaskCompleteHandler completeHandler;
    @Mock
    private TaskService taskService;
    
    @Autowired
    OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    
    @Before
    public void setUp() {
        onlineSchemaChangeFlowableTask = newOnlineSchemaChangeFlowableTask();
    }

   @Test
   public void testStart() {
       // Test start method with valid input
       Long taskId = 1L;

       when(taskService.start(taskId)).thenReturn(true);

       onlineSchemaChangeFlowableTask.start(taskId, taskService, mock(DelegateExecution.class));

       verify(taskService).start(taskId);
   }

   @Test
   public void testIsSuccessful() {
       // Test isSuccessful method when status is DONE
       onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);
       assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());

       // Test isSuccessful method when status is not DONE
       onlineSchemaChangeFlowableTask.setStatus(TaskStatus.RUNNING);
       assertFalse(onlineSchemaChangeFlowableTask.isSuccessful());
   }

   @Test
   public void testIsFailure() {
       // Test isFailure method when status is FAILED
       onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);
       assertTrue(onlineSchemaChangeFlowableTask.isFailure());

       // Test isFailure method when status is not FAILED
       onlineSchemaChangeFlowableTask.setStatus(TaskStatus.RUNNING);
       assertFalse(onlineSchemaChangeFlowableTask.isFailure());
   }

   @Test
    public void testCancel() {
       Long taskId = 1L;

       when(scheduleTaskService.listTask(Pageable.unpaged(), scheduleId)).thenReturn(new PageImpl<>(new ArrayList<>()));

       onlineSchemaChangeFlowableTask.cancel(false, taskId, taskService);

       verify(scheduleTaskService).listTask(Pageable.unpaged(), scheduleId);
       
       assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
   }

}