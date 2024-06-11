
package com.netflix.conductor.core.reconciliation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class WorkflowSweeperTest {
    
    @Test
    public void testWorkflowSweeper() {
        WorkflowExecutor workflowExecutor = Mockito.mock(WorkflowExecutor.class);
        WorkflowRepairService workflowRepairService = Mockito.mock(WorkflowRepairService.class);
        ConductorProperties properties = Mockito.mock(ConductorProperties.class);
        QueueDAO queueDAO = Mockito.mock(QueueDAO.class);
        WorkflowSweeper workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);
        future.join();
    }

    @Test
    public void testSweep() {
        String workflowId = "testWorkflowId";
        workflowSweeper.sweep(workflowId);
    }
    
    @Test
    public void testUnack(){
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        long workflowOffsetTimeout = 10000;
        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);
    }
    
    @Test
    public void testWorkflowOffsetWithJitter(){
        long workflowOffsetTimeout = 10000;
        long returnValue = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertNotNull(returnValue);
    }
}
