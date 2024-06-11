package com.netflix.conductor.core.execution;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.ExecutionStatus;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowInstanceStatus;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.WorkflowDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.utils.TaskDefs;

public class AsyncSystemTaskExecutorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutorTest.class);

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private WorkflowDAO workflowDAO;
    private Monitors monitors;

    @Before
    public void setup() {
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        workflowDAO = mock(WorkflowDAO.class);
        monitors = mock(Monitors.class);

        ConductorProperties conductorProperties = new ConductorProperties();
        conductorProperties.setSystemTaskWorkerCallbackDuration(
                new Duration(30000L, "milliseconds"));
        conductorProperties.setTaskExecutionPostponeDuration(
                new Duration(5000L, "milliseconds"));

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                mock(WorkflowExecutor.class));
    }

    @After
    public void tearDown() {
        Mockito.verifyNoMoreInteractions(executionDAOFacade);
        Mockito.verifyNoMoreInteractions(queueDAO);
        Mockito.verifyNoMoreInteractions(metadataDAO);
        Mockito.verifyNoMoreInteractions(workflowDAO);
        Mockito.verifyNoMoreInteractions(monitors);
    }

    @Test
    public void testExecute_TaskIsAlreadyCompleted_RemoveTaskFromQueue() {
        TaskModel taskModel = getTaskModel("TaskType1", "TaskId1");
        when(executionDAOFacade.getTaskModel(taskModel.getId())).thenReturn(taskModel);
        WorkflowModel workflowModel = getWorkflowModel("WorkflowId1");
        when(workflowDAO.getWorkflowModel(workflowModel.getId(), false)).thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(
                new WorkflowSystemTask() {
                    @Override
                    public void start(WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {
                        throw new RuntimeException("Task execution failed");
                    }

                    @Override
                    public void execute(
                            WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {
                        assertEquals(ExecutionStatus.COMPLETED, task.getStatus());
                    }

                    @Override
                    public boolean isAsyncComplete(TaskModel task) {
                        return true;
                    }
                },
                "TaskId1");

        Mockito.verify(queueDAO).remove("taskQueue", "TaskId1");
    }

    @Test
    public void testExecute_TaskIsTerminalState_RemoveTaskFromQueue() {
        TaskModel taskModel = getTaskModel("TaskType1", "TaskId1");
        when(executionDAOFacade.getTaskModel(taskModel.getId())).thenReturn(taskModel);
        WorkflowModel workflowModel = getWorkflowModel("WorkflowId1");
        when(workflowDAO.getWorkflowModel(workflowModel.getId(), false)).thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(
                new WorkflowSystemTask() {
                    @Override
                    public void start(
                            WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {
                        throw new RuntimeException("Task execution failed");
                    }

                    @Override
                    public void execute(
                            WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {
                        // No op
                    }

                    @Override
                    public boolean isAsyncComplete(TaskModel task) {
                        return false;
                    }
                },
                "TaskId1");

        WorkflowInstanceStatus workflowInstanceStatus = new WorkflowInstanceStatus();
        workflowInstanceStatus.setStatus(ExecutionStatus.FAILED);
        when(workflowModel.getStatus()).thenReturn(workflowInstanceStatus);

        asyncSystemTaskExecutor.execute(
                new WorkflowSystemTask() {
                    @Override
                    public void start(
                            WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {
                        throw new RuntimeException("Task execution failed");
                    }

                    @Override
                    public void execute(
                            WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {
                        // No op
                    }

                    @Override
                    public boolean isAsyncComplete(TaskModel task) {
                        return false;
                    }
                },
                "TaskId1");

        Mockito.verify(queueDAO).remove("taskQueue", "TaskId1");
    }

    @Test
    public void testExecute_TaskIsNotTerminalState_PostponeTaskInQueue() {
        TaskModel taskModel = getTaskModel("TaskType1", "TaskId1");
        when(executionDAOFacade.getTaskModel(taskModel.getId())).thenReturn(taskModel);
        WorkflowModel workflowModel = getWorkflowModel("WorkflowId1");
        when(workflowDAO.getWorkflowModel(workflowModel.getId(), false)).thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(
                new WorkflowSystemTask() {
                    @Override
                    public void start(
                            WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {
                        throw new RuntimeException("Task execution failed");
                    }

                    @Override
                    public void execute(
                            WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {
                        // No op
                    }

                    @Override
                    public boolean isAsyncComplete(TaskModel task) {
                        return false;
                    }
                },
                "TaskId1");

        Mockito.verify(queueDAO).postpone(
                "taskQueue",
                "TaskId1",
                QueueUtils.getQueuePriority(taskModel),
                30000L);
    }

    @Test
    public void testExecute_TaskIsInProgressState_ExecuteTaskAndUpdateTaskStatus() {
        TaskModel taskModel = getTaskModel("TaskType1", "TaskId1");
        when(executionDAOFacade.getTaskModel(taskModel.getId())).thenReturn(taskModel);
        WorkflowModel workflowModel = getWorkflowModel("WorkflowId1");
        when(workflowDAO.getWorkflowModel(workflowModel.getId(), false)).thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(
                new WorkflowSystemTask() {
                    @Override
                    public void start(
                            WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {
                        // No op
                    }

                    @Override
                    public void execute(
                            WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {
                        assertEquals(ExecutionStatus.IN_PROGRESS, task.getStatus());
                    }

                    @Override
                    public boolean isAsyncComplete(TaskModel task) {
                        return false;
                    }
                },
                "TaskId1");

        Mockito.verify(executionDAOFacade).updateTask(taskModel);
    }

    @Test
    public void testExecute_TaskIsScheduledState_ExecuteTaskAndUpdateTaskStatus() {
        TaskModel taskModel = getTaskModel("TaskType1", "TaskId1");
        when(executionDAOFacade.getTaskModel(taskModel.getId())).thenReturn(taskModel);
        WorkflowModel workflowModel = getWorkflowModel("WorkflowId1");
        when(workflowDAO.getWorkflowModel(workflowModel.getId(), false)).thenReturn(workflowModel);

        asyncSystemTaskExecutor.execute(
                new WorkflowSystemTask() {
                    @Override
                    public void start(
                            WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {
                        assertEquals(ExecutionStatus.SCHEDULED, task.getStatus());
                    }

                    @Override
                    public void execute(
                            WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {
                        // No op
                    }

                    @Override
                    public boolean isAsyncComplete(TaskModel task) {
                        return false;
                    }
                },
                "TaskId1");

        Mockito.verify(executionDAOFacade).updateTask(taskModel);
    }

    private TaskModel getTaskModel(String taskType, String taskId) {
        return new TaskModel() {
            @Override
            public String getTaskType() {
                return taskType;
            }

            @Override
            public String getId() {
                return taskId;
            }

            @Override
            public String getTaskDefName() {
                return TaskDefs.TASK_DEF_1;
            }

            @Override
            public ExecutionStatus getStatus() {
                return ExecutionStatus.SCHEDULED;
            }

            @Override
            public WorkflowInstanceStatus getWorkflowInstanceStatus() {
                return new WorkflowInstanceStatus();
            }

            @Override
            public long getPollCount() {
                return 0;
            }

            @Override
            public void incrementPollCount() {
                // No op
            }

            @Override
            public long getWorkflowPriority() {
                return new Random().nextLong();
            }

            @Override
            public long getStartTime() {
                return 0;
            }

            @Override
            public long getEndTime() {
                return 0;
            }

            @Override
            public void setStatus(ExecutionStatus status) {
                // No op
            }

            @Override
            public void setReasonForIncompletion(String reasonForIncompletion) {
                // No op
            }

            @Override
            public void setEndTime(long endTime) {
                // No op
            }
        };
    }

    private WorkflowModel getWorkflowModel(String workflowId) {
        return new WorkflowModel() {
            @Override
            public String getId() {
                return workflowId;
            }

            @Override
            public String getName() {
                return "WorkflowName";
            }

            @Override
            public ExecutionStatus getStatus() {
                return ExecutionStatus.RUNNING;
            }
        };
    }
}