
package com.netflix.conductor.core.execution;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncSystemTaskExecutorTest {

     private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

     private ExecutionDAOFacade executionDAOFacade;
     private QueueDAO queueDAO;
     private MetadataDAO metadataDAO;
     private ConductorProperties conductorProperties;
     private WorkflowExecutor workflowExecutor;

     @Before
     public void setup() {
         executionDAOFacade = mock(ExecutionDAOFacade.class);
         queueDAO = mock(QueueDAO.class);
         metadataDAO = mock(MetadataDAO.class);
         conductorProperties = mock(ConductorProperties.class);
         workflowExecutor = mock(WorkflowExecutor.class);
         
         asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
     }

     @Test
     public void testExecute() {
         TaskModel task = mock(TaskModel.class);
         WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
         when(executionDAOFacade.getTaskModel(any())).thenReturn(task);

         String taskId = "1234";
         asyncSystemTaskExecutor.execute(systemTask, taskId);

         verify(executionDAOFacade).getTaskModel(taskId);
     }

     @Test
     public void testPostponeQuietly() {
         TaskModel task = mock(TaskModel.class);
         String queueName = QueueUtils.getQueueName(task);
         asyncSystemTaskExecutor.postponeQuietly(queueName, task);

         verify(queueDAO).postpone(any(), any(), any(), any());
     }

     @Test
     public void testLoadTaskQuietly() {
         String taskId = "12345";
         when(executionDAOFacade.getTaskModel(any())).thenReturn(null);

         asyncSystemTaskExecutor.loadTaskQuietly(taskId);

         verify(executionDAOFacade).getTaskModel(taskId);
     }
}
