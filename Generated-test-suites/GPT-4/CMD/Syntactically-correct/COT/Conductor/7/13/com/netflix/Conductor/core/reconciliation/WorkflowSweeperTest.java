package com.netflix.conductor.core.reconciliation;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import static org.junit.Assert.assertTrue;
import org.mockito.MockitoAnnotations;

public class WorkflowSweeperTest {

    @Mock
    WorkflowSweeper WorkflowSweeperMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSweepAsync() throws ExecutionException, InterruptedException {
        Future<Void> futureMock = mock(Future.class);
        when(WorkflowSweeperMock.sweepAsync("testID")).thenReturn((CompletableFuture<Void>) futureMock);
        Future<Void> result = WorkflowSweeperMock.sweepAsync("testID");
        assertEquals(result, futureMock);
    }
    
    @Test
    public void testSweep() {
        WorkflowSweeperMock.sweep("testID");
        verify(WorkflowSweeperMock, times(1)).sweep("testID");
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long resultOffset = WorkflowSweeperMock.workflowOffsetWithJitter(15);
        assertTrue(resultOffset >= 10 && resultOffset <= 20);
    }
 
    @Test(expected = NotFoundException.class)
    public void testSweepWithNotFoundException() {
        when(WorkflowSweeperMock.sweep("invalidID")).thenThrow(NotFoundException.class);
        WorkflowSweeperMock.sweep("invalidID");
    }
    
    @Test(expected = Exception.class)
    public void testSweepWithGeneralException() {
       when(WorkflowSweeperMock.sweep("invalidID")).thenThrow(Exception.class);
       WorkflowSweeperMock.sweep("invalidID");
    }
}
