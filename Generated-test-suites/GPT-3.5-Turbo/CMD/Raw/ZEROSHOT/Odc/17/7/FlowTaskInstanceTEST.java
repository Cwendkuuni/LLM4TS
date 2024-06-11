import static org.mockito.Mockito.verify;

import static org.mockito.Mockito.when;

import org.flowable.task.api.Task;
import org.flowable.task.api.TaskService;

import org.junit.Before;

import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;

    @Mock

    private TaskService taskService;

    @Before

    public void setup() {

        flowTaskInstance = new FlowTaskInstance(taskService);

    }

    

    @Test

    public void bindServiceTaskTest() {

        Task task = new Task();

        flowTaskInstance.bindServiceTask(task);

        assert flowTaskInstance.getTargetTaskHandle() == task;

    }

    

    @Test

    public void confirmExecuteTest() {

        flowTaskInstance.confirmExecute();

        verify(flowTaskInstance).complete(false);

    }

    

    @Test

    public void abortTest() {

        flowTaskInstance.abort();

        verify(flowTaskInstance).complete(true);

    }

    

    @Test

    public void cancelTest() {

        flowTaskInstance.cancel(true);

        verify(flowTaskInstance.getTargetTaskHandle()).cancel(true);

    }

    

    @Test

    public void isDoneTest() {

        flowTaskInstance.isDone();

        verify(flowTaskInstance.getTargetTaskHandle()).isDone();

    }

    

    @Test

    public void getResultTest() {

        flowTaskInstance.getResult();

        verify(flowTaskInstance.getTargetTaskHandle()).get();

    }

    

    @Test

    public void getResultWithTimeoutTest() {

        long timeout = 10;

        TimeUnit unit = TimeUnit.SECONDS;

        flowTaskInstance.getResult(timeout, unit);

        verify(flowTaskInstance.getTargetTaskHandle()).get(timeout, unit);

    }

}