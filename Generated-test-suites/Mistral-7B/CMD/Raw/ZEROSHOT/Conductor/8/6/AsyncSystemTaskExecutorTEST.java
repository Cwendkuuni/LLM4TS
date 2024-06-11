package com.netflix.conductor.core.execution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.metadata.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.WorkflowInstanceStatus;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.dao.execution.ExecutionDAOFacadeImpl;
import com.netflix.conductor.dao.metadata.MetadataDAOImpl;
import com.netflix.conductor.metrics.monitor.MonitorRegistry;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModelImpl;
import com.netflix.conductor.utils.queue.WorkflowExecutor;

@Configuration
public class AsyncSystemTaskExecutorTest {

 private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
 private ExecutionDAOFacade executionDAOFacade;
 private QueueDAO queueDAO;
 private MetadataDAO metadataDAO;
 private WorkflowExecutor workflowExecutor;
 private ConductorProperties conductorProperties;
 private MonitorRegistry monitorRegistry;
 private WorkflowSystemTask workflowSystemTask;

 @Bean
 public ExecutionDAOFacade executionDAOFacade() {
 return new ExecutionDAOFacadeImpl();
 }

 @Bean
 public QueueDAO queueDAO() {
 return new QueueDAOImpl();
 }

 @Bean
 public MetadataDAO metadataDAO() {
 return new MetadataDAOImpl();
 }

 @Bean
 public WorkflowExecutor workflowExecutor() {
 return Mockito.mock(WorkflowExecutor.class);
 }

 @Bean
 public ConductorProperties conductorProperties() {
 ConductorProperties properties = Mockito.mock(ConductorProperties.class);
 when(properties.getSystemTaskWorkerCallbackDuration()).thenReturn(Mockito.mock(Duration.class));
 when(properties.getTaskExecutionPostponeDuration()).thenReturn(Mockito.mock(Duration.class));
 return properties;
 }

 @Bean
 public MonitorRegistry monitorRegistry() {
 return Mockito.mock(MonitorRegistry.class);
 }

 @Bean
 public AsyncSystemTaskExecutor asyncSystemTaskExecutor(
 ExecutionDAOFacade executionDAOFacade,
 QueueDAO queueDAO,
 MetadataDAO metadataDAO,
 ConductorProperties conductorProperties,
 WorkflowExecutor workflowExecutor) {
 return new AsyncSystemTaskExecutor(
 executionDAOFacade,
 queueDAO,
 metadataDAO,
 conductorProperties,
 workflowExecutor);
 }

 @Before
 public void setup() {
 workflowSystemTask = Mockito.mock(WorkflowSystemTask.class);
 executionDAOFacade = Mockito.spy(executionDAOFacade());
 queueDAO = mock(QueueDAO.class);
 metadataDAO = mock(MetadataDAO.class);
 conductorProperties = Mockito.mock(ConductorProperties.class);
 workflowExecutor = mock(WorkflowExecutor.class);
 asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
 executionDAOFacade,
 queueDAO,
 metadataDAO,
 conductorProperties,
 workflowExecutor);
 }

 @Test
 public void testExecute_taskIsAlreadyCompleted_queueIsRemoved() {
 WorkflowModel workflowModel = new WorkflowModelImpl();
 workflowModel.setStatus(WorkflowInstanceStatus.COMPLETED);
 when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
 .thenReturn(workflowModel);

 TaskModel taskModel = new TaskModel();
 taskModel.setStatus(TaskModel.Status.COMPLETED);
 when(executionDAOFacade.getTaskModel(anyString()))
 .thenReturn(Optional.of(taskModel));

 String queueName = QueueUtils.getQueueName(taskModel);
 when(queueDAO.remove(queueName, taskModel.getTaskId()))
 .thenReturn(true);

 asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());

 verify(queueDAO, times(1)).remove(queueName, taskModel.getTaskId());
 }

 @Test
 public void testExecute_taskIsAlreadyTerminal_queueIsRemoved() {
 WorkflowModel workflowModel = new WorkflowModelImpl();
 workflowModel.setStatus(WorkflowInstanceStatus.FAILED);
 when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
 .thenReturn(workflowModel);

 TaskModel taskModel = new TaskModel();
 taskModel.setStatus(TaskModel.Status.FAILED);
 when(executionDAOFacade.getTaskModel(anyString()))
 .thenReturn(Optional.of(taskModel));

 String queueName = QueueUtils.getQueueName(taskModel);
 when(queueDAO.remove(queueName, taskModel.getTaskId()))
 .thenReturn(true);

 asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());

 verify(queueDAO, times(1)).remove(queueName, taskModel.getTaskId());
 }

 @Test
 public void testExecute_taskIsScheduled_queueIsAdded() {
 WorkflowModel workflowModel = new WorkflowModelImpl();
 workflowModel.setStatus(WorkflowInstanceStatus.STARTED);
 when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
 .thenReturn(workflowModel);

 TaskModel taskModel = new TaskModel();
 taskModel.setStatus(TaskModel.Status.SCHEDULED);
 when(executionDAOFacade.getTaskModel(anyString()))
 .thenReturn(Optional.of(taskModel));

 String queueName = QueueUtils.getQueueName(taskModel);

 asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());

 verify(queueDAO, times(1)).postpone(
 queueName,
 eq(taskModel.getTaskId()),
 anyInt(),
 eq(1L));
 }

 @Test
 public void testExecute_taskIsInProgress_taskIsExecuted() {
 WorkflowModel workflowModel = new WorkflowModelImpl();
 workflowModel.setStatus(WorkflowInstanceStatus.STARTED);
 when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
 .thenReturn(workflowModel);

 TaskModel taskModel = new TaskModel();
 taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
 when(executionDAOFacade.getTaskModel(anyString()))
 .thenReturn(Optional.of(taskModel));

 when(workflowExecutor.decide(anyString()))
 .thenReturn(ExecutionStatus.CONTINUE);

 when(workflowExecutor.runWorkflow(any(), any()))
 .thenReturn(true);

 asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());

 verify(workflowExecutor, times(1)).runWorkflow(workflowModel, workflowSystemTask);
 }

 @Test
 public void testExecute_taskIsInProgress_queueIsPostponed() {
 WorkflowModel workflowModel = new WorkflowModelImpl();
 workflowModel.setStatus(WorkflowInstanceStatus.STARTED);
 when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
 .thenReturn(workflowModel);

 TaskModel taskModel = new TaskModel();
 taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
 when(executionDAOFacade.getTaskModel(anyString()))
 .thenReturn(Optional.of(taskModel));

 when(workflowExecutor.decide(anyString()))
 .thenReturn(ExecutionStatus.CONTINUE);

 when(workflowExecutor.runWorkflow(any(), any()))
 .thenThrow(new RuntimeException());

 asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());

 verify(queueDAO, times(1)).postpone(
 queueName,
 eq(taskModel.getTaskId()),
 anyInt(),
 eq(1L));
 }

 @Test
 public void testExecute_taskIsInProgress_queueIsRemoved() throws InterruptedException {
 WorkflowModel workflowModel = new WorkflowModelImpl();
 workflowModel.setStatus(WorkflowInstanceStatus.STARTED);
 when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
 .thenReturn(workflowModel);

 TaskModel taskModel = new TaskModel();
 taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
 when(executionDAOFacade.getTaskModel(anyString()))
 .thenReturn(Optional.of(taskModel));

 when(workflowExecutor.decide(anyString()))
 .thenReturn(ExecutionStatus.TERMINATE);

 when(workflowExecutor.runWorkflow(any(), any()))
 .thenReturn(true);

 asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());

 verify(queueDAO, times(1)).remove(queueName, taskModel.getTaskId());
 }

 @Test
 public void testExecute_taskIsTerminal_queueIsRemoved() {
 TaskModel taskModel = new TaskModel();
 taskModel.setStatus(ExecutionStatus.TERMINATED);
 taskModel.setReasonForIncompletion("Reason for termination");

 String queueName = QueueUtils.getQueueName(taskModel);
 when(queueDAO.remove(queueName, taskModel.getTaskId()))
 .thenReturn(true);

 asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());

 verify(queueDAO, times(1)).remove(queueName, taskModel.getTaskId());
 }

 @Test
 public void testExecute_taskHasExceededConcurrentLimit_queueIsPostponed() {
 WorkflowModel workflowModel = new WorkflowModelImpl();
 workflowModel.setStatus(WorkflowInstanceStatus.STARTED);
 when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
 .thenReturn(workflowModel);

 TaskModel taskModel = new TaskModel();
 taskModel.setStatus(TaskModel.Status.SCHEDULED);
 when(executionDAOFacade.getTaskModel(anyString()))
 .thenReturn(Optional.of(taskModel));

 when(queueDAO.exceedsConcurrentLimit(anyString(), anyBoolean()))
 .thenReturn(true);

 asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());

 verify(queueDAO, times(1)).postpone(
 queueName,
 eq(taskModel.getTaskId()),
 anyInt(),
 eq(1L));
 }

 @Test
 public void testExecute_taskHasExceededRateLimit_queueIsPostponed() {
 WorkflowModel workflowModel = new WorkflowModelImpl();
 workflowModel.setStatus(WorkflowInstanceStatus.STARTED);
 when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
 .thenReturn(workflowModel);

 TaskModel taskModel = new TaskModel();
 taskModel.setStatus(TaskModel.Status.SCHEDULED);
 when(executionDAOFacade.getTaskModel(anyString()))
 .thenReturn(Optional.of(taskModel));

 when(metadataDAO.getTaskDef(anyString()))
 .thenReturn(new Object());

 when(queueDAO.exceedsRateLimitPerFrequency(anyString(), anyString(), anyInt()))
 .thenReturn(true);

 asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());

 verify(queueDAO, times(1)).postpone(
 queueName,
 eq(taskModel.getTaskId()),
 anyInt(),
 eq(1L));
 }

 @Test
 public void testExecute_exceptionDuringExecution_queueIsPostponed() {
 WorkflowModel workflowModel = new WorkflowModelImpl();
 workflowModel.setStatus(WorkflowInstanceStatus.STARTED);
 when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean()))
 .thenReturn(workflowModel);

 TaskModel taskModel = new TaskModel();
 taskModel.setStatus(TaskModel.Status.SCHEDULED);
 when(executionDAOFacade.getTaskModel(anyString()))
 .thenReturn(Optional.of(taskModel));

 when(workflowExecutor.decide(anyString()))
 .thenReturn(ExecutionStatus.CONTINUE);

 when(workflowExecutor.runWorkflow(any(), any()))
 .thenThrow(new RuntimeException());

 asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());

 verify(queueDAO, times(1)).postpone(
 queueName,
 eq(taskModel.getTaskId()),
 anyInt(),
 eq(1L));
 }
}