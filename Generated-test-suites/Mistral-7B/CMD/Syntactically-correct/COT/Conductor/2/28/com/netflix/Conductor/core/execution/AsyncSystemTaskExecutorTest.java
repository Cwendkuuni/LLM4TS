package com.netflix.conductor.core.execution.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.execution.WorkflowExecutorImpl;
import com.netflix.conductor.execution.tasks.MockWorkflowSystemTask;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.metrics.NoOpMonitor;

public class AsyncSystemTaskExecutorTest {

 private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
 private ExecutionDAOFacade executionDAOFacadeMock;
 private QueueDAO queueDAOMock;
 private MetadataDAO metadataDAOMock;
 private ConductorProperties conductorPropertiesMock;
 private WorkflowExecutor workflowExecutorMock;
 private QueueDAO queueDAO;
 private Monitors monitorsMock;

 @Before
 public void setup() {
 executionDAOFacadeMock = mock(ExecutionDAOFacade.class);
 queueDAOMock = mock(QueueDAO.class);
 metadataDAOMock = mock(MetadataDAO.class);
 conductorPropertiesMock = mock(ConductorProperties.class);
 workflowExecutorMock = mock(WorkflowExecutor.class);
 queueDAO = new QueueDAOImpl();
 monitorsMock = new NoOpMonitor();

 asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
 executionDAOFacadeMock,
 queueDAOMock,
 metadataDAOMock,
 conductorPropertiesMock,
 new WorkflowExecutorImpl(workflowExecutorMock, monitorsMock, queueDAO));
 }

 @Test
 public void testExecute_taskNotFound() {
 String taskId = "taskId";
 WorkflowSystemTask systemTask = new MockWorkflowSystemTask();

 when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(null);

 asyncSystemTaskExecutor.execute(systemTask, taskId);

 verify(queueDAOMock, times(1)).remove(anyString(), eq(taskId));
 }

 @Test
 public void testExecute_taskCompleted() {
 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setStatus(ExecutionStatus.COMPLETE);
 String taskId = "taskId";
 TaskModel task = new TaskModel();
 task.setId(taskId);
 task.setStatus(TaskModel.Status.SCHEDULED);
 task.setWorkflowInstanceId(workflowModel.getId());
 WorkflowSystemTask systemTask = new MockWorkflowSystemTask();

 when(executionDAOFacadeMock.getWorkflowModel(eq(workflowModel.getId()), anyBoolean()))
 .thenReturn(workflowModel);

 when(executionDAOFacadeMock.getTaskModel(taskId)).thenReturn(task);

 asyncSystemTaskExecutor.execute(systemTask, taskId);

 verify(queueDAOMock, times(1)).remove(anyString(), eq(taskId));
 }

 // Add more test cases for each method and edge cases here

}