
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

@RunWith(MockitoJUnitRunner.class)
public class AsyncSystemTaskExecutorTest {

	@Mock
	ExecutionDAOFacade executionDAOFacade;

	@Mock
	QueueDAO queueDAO;

	@Mock
	MetadataDAO metadataDAO;
	
	@Mock
	ConductorProperties conductorProperties;
	
	@Mock
	WorkflowExecutor workflowExecutor;

	@InjectMocks
	AsyncSystemTaskExecutor asyncSystemTaskExecutor;

	@Test
	public void testExecute() {
		WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
		String taskId = "task1";
		TaskModel task = new TaskModel();
		task.setTaskId(taskId);
		when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
		asyncSystemTaskExecutor.execute(systemTask, taskId);
		verify(executionDAOFacade).getTaskModel(taskId);
	}

	@Test
	public void testPostponeQuietly() {
		String queueName = "queue1";
		TaskModel task = new TaskModel();
		task.setTaskId("task1");
		asyncSystemTaskExecutor.postponeQuietly(queueName, task);
		verify(queueDAO).postpone(any(), any(), anyInt(), anyLong());
	}

	@Test
	public void testLoadTaskQuietly() {
		String taskId = "task1";
		TaskModel task = new TaskModel();
		task.setTaskId(taskId);
		when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
		asyncSystemTaskExecutor.loadTaskQuietly(taskId);
		verify(executionDAOFacade).getTaskModel(taskId);
	}
}
