package com.oceanbase.odc.service.flow.task;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegateTest;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.util.ReflectionUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Test class for BaseRuntimeFlowableDelegate.
 */
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<String> testObject;

    @Mock
    private DelegateExecution execution;

    private Method initCallableMethod;

    /**
     * Initialize test objects.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        initTestObjects();
    }

    /**
     * Initialize test objects.
     */
    private void initTestObjects() {
        this.initCallableMethod = ReflectionUtil.findMethod(BaseRuntimeFlowableDelegate.class, "initCallable",
                DelegateExecution.class);
    }

    /**
     * Test method 'initTargetTaskInstanceId'.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testInitTargetTaskInstanceId() throws Exception {
        // Given
        String processDefinitionId = "processDefinitionId";
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId))
                .thenReturn(1L);
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class)))
                .thenAnswer(new Answer<Boolean>() {
                    @Override
                    public Boolean answer(InvocationOnMock invocation) throws Throwable {
                        Object[] args = invocation.getArgs();
                        assertEquals(FlowStatus.STARTED, args[0]);
                        return true;
                    }
                });

        // When
        testObject.initTargetTaskInstanceId(execution);

        // Then
        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        verify(eventPublisher, times(1)).publishEvent(eq(new ServiceTaskStartedEvent(testObject, any())));
        verify(flowInstanceRepository, times(1)).updateStatusById(1L, eq(FlowStatus.STARTED));
    }

    /**
     * Test method 'preHandle'.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testPreHandle() throws Exception {
        // Given
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");

        // When
        testObject.preHandle(execution);

        // Then
        verify(testObject, times(1)).initTargetTaskInstanceId(execution);
        verifyNoMoreInteractions(execution);
    }

    /**
     * Test method 'run'.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testRun() throws Exception {
        // Given
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(execution.getId()).thenReturn(1L);

        Callable<String> callable = mock(Callable.class);
        when(testObject.initCallable(execution)).thenReturn(callable);

        CountDownLatch latch = new CountDownLatch(1);
        testObject.setLatch(latch);

        when(eventPublisher.addEventListener(any()))
                .thenAnswer(new Answer<Object>() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        Object listener = invocation.getArgumentAt(0, 0);
                        if (listener instanceof ActiveTaskStatisticsListener) {
                            when(listener.getName()).thenReturn("ActiveTaskStatisticsListener");
                        }
                        return null;
                    }
                });

        when(eventPublisher.publishEvent(any())).thenReturn(null);

        // When
        testObject.run(execution);

        // Then
        verify(testObject, times(1)).get();
        verify(callable, times(1)).call();
        verify(eventPublisher, times(1)).publishEvent(any());
        verify(latch, times(1)).await();
        verifyNoMoreInteractions(execution);
        verifyNoMoreInteractions(callable);
    }

    /**
     * Test method 'execute'.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testExecute() throws Exception {
        // Given
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");

        // When
        testObject.execute(execution);

        // Then
        verify(testObject, times(1)).preHandle(execution);
        verify(testObject, times(1)).updateFlowInstanceStatus(eq(FlowStatus.COMPLETED));
        verify(execution, times(1)).dealloc();
        verify(eventPublisher, times(2)).removeEventListener(eq(testObject.getTaskInstanceCreatedlistener()));
        verify(eventPublisher, times(2)).removeEventListener(eq(testObject.getActiveTaskStatisticsListener()));
    }

    /**
     * Test method 'get'.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testGet() throws Exception {
        // Given
        when(testObject.get()).thenReturn("result");

        // When & Then
        assertEquals("result", testObject.get());
    }

    /**
     * Test method 'get'.
     *
     * @throws Exception if an error occurs
     */
    @Test(expected = InterruptedException.class)
    public void testGetInterrupted() throws Exception {
        // Given
        CountDownLatch latch = new CountDownLatch(1);
        testObject.setLatch(latch);

        // When
        testObject.get();

        // Then - InterruptedException should be thrown
    }

    /**
     * Test method 'get'.
     *
     * @throws Exception if an error occurs
     */
    @Test(expected = ExecutionException.class)
    public void testGetExecutionException() throws Exception {
        // Given
        when(testObject.get()).thenThrow(new ExecutionException(new RuntimeException("test exception")));

        // When & Then - ExecutionException should be thrown
    }

    /**
     * Test method 'get'.
     *
     * @throws Exception if an error occurs
     */
    @Test(expected = TimeoutException.class)
    public void testGetTimeoutException() throws Exception {
        // Given
        CountDownLatch latch = new CountDownLatch(1);
        testObject.setLatch(latch);

        // When
        testObject.get(1, TimeUnit.MINUTES);

        // Then - TimeoutException should be thrown
    }
}