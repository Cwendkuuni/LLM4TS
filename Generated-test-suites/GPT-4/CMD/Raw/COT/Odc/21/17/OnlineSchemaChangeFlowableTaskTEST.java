import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Pageable;

public class OnlineSchemaChangeFlowableTaskTest {
    OnlineSchemaChangeFlowableTask taskUnderTest = new OnlineSchemaChangeFlowableTask();

    @Test
    public void testIsCancelled() {
        assertFalse(taskUnderTest.isCancelled());
    }

    @Test(expected = Exception.class)
    public void testStart_withInvalidTaskId() throws Exception {
        Long taskId = null;
        TaskService taskService = Mockito.mock(TaskService.class);
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        taskUnderTest.start(taskId, taskService, execution);
    }

    @Test
    public void testIsSuccessful() {
        assertFalse(taskUnderTest.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        assertFalse(taskUnderTest.isFailure());
    }

    @Test
    public void testOnFailure() {
        Long taskId = 1L;
        TaskService taskService = Mockito.mock(TaskService.class);
        taskUnderTest.onFailure(taskId, taskService);
        assertTrue(taskUnderTest.isFailure());
    }

    @Test
    public void testSuccessful() {
        Long taskId = 1L;
        TaskService taskService = Mockito.mock(TaskService.class);
        taskUnderTest.onSuccessful(taskId, taskService);
        assertTrue(taskUnderTest.isSuccessful());
    }

    @Test
    public void testOnProgressUpdate() {
        Long taskId = 1L;
        TaskService taskService = Mockito.mock(TaskService.class);
        try {
            taskUnderTest.onProgressUpdate(taskId, taskService);
        } catch (Exception e) {
            fail("Exception should not have occurred");
        }
    }

    @Test(expected = Exception.class)
    public void testCancel_taskIdNull() throws Exception {
        Long taskId = null;
        TaskService taskService = Mockito.mock(TaskService.class);
        taskUnderTest.cancel(true, taskId, taskService);
    }
}