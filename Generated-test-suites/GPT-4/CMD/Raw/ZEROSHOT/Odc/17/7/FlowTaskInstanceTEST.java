
import org.junit.Before;
import org.junit.Test;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FlowTaskInstanceTest {
    
    @MockBean private FlowTaskInstance flowTaskInstance;
    @MockBean private TaskService taskService;
    @MockBean private Task task;
    
    @Before
    public void setUp(){
        taskService = Mockito.mock(TaskService.class);
        task = Mockito.mock(Task.class);
    }
    
    @Test
    public void testFlowTaskInstance() {
        assertNotNull(flowTaskInstance);
    }

    @Test
    public void testIsPresentOnThisMachine() {
        assertFalse(flowTaskInstance.isPresentOnThisMachine());
        flowTaskInstance.bindServiceTask(new BaseRuntimeFlowableDelegate<Object>() {});
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }
    
    @Test
    public void testCreate() {
        flowTaskInstance.create();
        assertNotNull(flowTaskInstance.getId());
    }

    @Test
    public void testUpdate() {
        flowTaskInstance.update();
        assertNotNull(flowTaskInstance.getStatus());
    }
    
    @Test
    public void testComplete() {
        flowTaskInstance.complete(true);
        assertEquals(Optional.of(TaskType.COMPLETE), flowTaskInstance.getTaskType());
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        assertEquals(Optional.of(TaskType.ABORT), flowTaskInstance.getTaskType());
    }

    @Test
    public void testIsDone() {
        assertFalse(flowTaskInstance.isDone());
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        assertEquals(Optional.of(TaskType.CONFIRM_EXECUTE), flowTaskInstance.getTaskType());
    }

    @Test
    public void testGetResult() {

        when(taskService.createTaskQuery().list()).thenReturn(tasks);
        Object result = flowTaskInstance.getResult();
        assertNotNull(result);
    }

    @Test
    public void testGetResultWithTimeout() {
        Object result = flowTaskInstance.getResult(10, TimeUnit.SECONDS);
        assertNotNull(result);
    }
}
