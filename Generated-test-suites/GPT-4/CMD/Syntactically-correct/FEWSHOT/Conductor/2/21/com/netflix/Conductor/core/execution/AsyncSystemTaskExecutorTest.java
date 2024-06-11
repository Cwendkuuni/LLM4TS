package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AsyncSystemTaskExecutorTest {

	@InjectMocks
	private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

	@Mock
	private ExecutionDAOFacade executionDAOFacade;

	@Mock
	private QueueDAO queueDAO;

	@Mock
	private MetadataDAO metadataDAO;

	@Mock
	private WorkflowSystemTask workflowSystemTask;

	@Mock
	private WorkflowExecutor workflowExecutor;

	@Mock
	private ConductorProperties conductorProperties;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testExecute() {
		String taskId = "1";
		TaskModel taskModel = new TaskModel();
		when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
		asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
		verify(queueDAO, atLeastOnce()).remove(anyString(), anyString());
		verify(executionDAOFacade, atLeastOnce()).updateTask(taskModel);
	}

	@Test
	public void testLoadTaskQuietly() {
		String taskId = "1";
		TaskModel taskModel = new TaskModel();
		when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
		TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
		assertEquals(taskModel, result);
	}

	@Test
	public void testPostponeQuietly() {
		TaskModel taskModel = new TaskModel();
		String queueName = "queue";
		doNothing().when(queueDAO).postpone(anyString(), anyString(), anyInt(), anyInt());
		asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);
		verify(queueDAO, atLeastOnce()).postpone(anyString(), anyString(), anyInt(), anyInt());
	}
}
