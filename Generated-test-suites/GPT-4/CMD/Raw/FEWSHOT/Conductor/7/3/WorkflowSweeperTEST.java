
package com.netflix.conductor.core.reconciliation;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

    @Mock
    WorkflowExecutor workflowExecutor;
    @Mock
    WorkflowRepairService workflowRepairService;
    @Mock
    ConductorProperties conductorProperties;
    @Mock
    QueueDAO queueDAO;

    @Test
    public void testSweepAsync() {
        // Given
        String workflowId = "testWorkflowId";
        WorkflowSweeper workflowSweeper = new WorkflowSweeper(workflowExecutor,
                Optional.of(workflowRepairService), conductorProperties, queueDAO);

        // When
        workflowSweeper.sweepAsync(workflowId);

        // Then
        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        // Given
        long workflowOffsetTimeout = 20L;
        WorkflowSweeper workflowSweeper = new WorkflowSweeper(workflowExecutor,
                Optional.of(workflowRepairService), conductorProperties, queueDAO);

        // When
        long jitteredOffset = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        
        // Then
        long range = workflowOffsetTimeout / 3;
        assertTrue(jitteredOffset >= (workflowOffsetTimeout - range));
        assertTrue(jitteredOffset <= (workflowOffsetTimeout + range));
    }

    @Test
    public void testUnack() {
        // Test data and mocks needed here for complete test
        // This is just a sample structure for the unack() test method
    }

    @Test
    public void testSweep() {
        // Test data and mocks needed here for complete test
        // This is just a sample structure for the sweep() test method
    }
}
