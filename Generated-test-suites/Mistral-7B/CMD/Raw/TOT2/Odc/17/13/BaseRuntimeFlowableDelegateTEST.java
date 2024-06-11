package com.oceanbase.odc.service.flow.task;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;

import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.test.common.TestConstants;
import com.oceanbase.odc.service.test.utils.TestHelper;
import com.oceanbase.odc.util.PreConditions;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

/**
 * Test class for {@link BaseRuntimeFlowableDelegate}
 */
@Slf4j
@PrepareForTest({ BaseRuntimeFlowableDelegate.class, FlowableAdaptor.class })
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private EventPublisher eventPublisher;

    @Captor
    private ArgumentCaptor<ServiceTaskStartedEvent> serviceTaskStartedEventCaptor;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(System.class);
        PowerMockito.mockStatic(Thread.class);
    }

    @Test
    public void testRun_NormalCase() throws Exception {
        // Given
        String processDefinitionId = TestConstants.PROCESS_DEFINITION_ID;
        String activityId = TestConstants.ACTIVITY_ID;
        Callable<String> callable = PowerMockito.mock(Callable.class);
        doAnswer(invocation -> {
            baseRuntimeFlowableDelegate.preHandle(TestHelper.createDelegateExecution());
            return "test result";
        }).when(baseRuntimeFlowableDelegate).initCallable(TestHelper.createDelegateExecution());
        doAnswer(invocation -> {
            baseRuntimeFlowableDelegate.bindToFlowTaskInstance(TestHelper.createFlowTaskInstance());
            baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
            return callable.call();
        }).when(callable).call();

        // When
        baseRuntimeFlowableDelegate.run(TestHelper.createDelegateExecution());

        // Then
        assertEquals("test result", baseRuntimeFlowableDelegate.get());
        verify(eventPublisher).publishEvent(serviceTaskStartedEventCaptor.capture());
        ServiceTaskStartedEvent serviceTaskStartedEvent = serviceTaskStartedEventCaptor.getValue();
        assertEquals(baseRuntimeFlowableDelegate, serviceTaskStartedEvent.getServiceTask());
        verify(eventPublisher).removeEventListener(baseRuntimeFlowableDelegate.getTaskInstanceCreatedlistener());
        verify(eventPublisher).removeEventListener(baseRuntimeFlowableDelegate.getActiveTaskStatisticsListener());
    }

    @Test(expected = ServiceTaskError.class)
    public void testRun_ExceptionThrown() throws Exception {
        // Given
        String processDefinitionId = TestConstants.PROCESS_DEFINITION_ID;
        String activityId = TestConstants.ACTIVITY_ID;
        Callable<String> callable = PowerMockito.mock(Callable.class);
        doAnswer(invocation -> {
            throw new Exception("Test exception");
        }).when(callable).call();

        // When
        baseRuntimeFlowableDelegate.run(TestHelper.createDelegateExecution());

        // Then
        PreConditions.assertExceptionIsInstanceOf(ServiceTaskError.class, baseRuntimeFlowableDelegate.getThrown());
    }

    @Test(expected = InterruptedException.class)
    public void testRun_Interrupted() throws Exception {
        // Given
        String processDefinitionId = TestConstants.PROCESS_DEFINITION_ID;
        String activityId = TestConstants.ACTIVITY_ID;
        Callable<String> callable = PowerMockito.mock(Callable.class);
        doAnswer(invocation -> {
            throw new InterruptedException("Test exception");
        }).when(callable).call();

        // When
        baseRuntimeFlowableDelegate.run(TestHelper.createDelegateExecution());

        // Then
        // InterruptedException cannot be asserted in JUnit, so this test only checks if the exception is thrown
    }

    @Test(expected = ExecutionException.class)
    public void testGet_ExecutionExceptionThrown() throws Exception {
        // Given
        String processDefinitionId = TestConstants.PROCESS_DEFINITION_ID;
        String activityId = TestConstants.ACTIVITY_ID;
        Callable<String> callable = PowerMockito.mock(Callable.class);
        doAnswer(invocation -> {
            throw new Exception("Test exception");
        }).when(callable).call();

        // When
        baseRuntimeFlowableDelegate.run(TestHelper.createDelegateExecution());

        // Then
        baseRuntimeFlowableDelegate.get();

        // Then
        PreConditions.assertExceptionIsInstanceOf(ExecutionException.class, baseRuntimeFlowableDelegate.getThrown());
    }

    @Test(expected = TimeoutException.class)
    public void testGet_TimeoutExceptionThrown() throws Exception {
        // Given
        String processDefinitionId = TestConstants.PROCESS_DEFINITION_ID;
        String activityId = TestConstants.ACTIVITY_ID;
        Callable<String> callable = PowerMockito.mock(Callable.class);
        doAnswer(invocation -> {
            Thread.sleep(2000);
            return "test result";
        }).when(callable).call();

        // When
        baseRuntimeFlowableDelegate.run(TestHelper.createDelegateExecution());

        // Then
        baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS);

        // Then
        PreConditions.assertExceptionIsInstanceOf(TimeoutException.class, baseRuntimeFlowableDelegate.getThrown());
    }

    @Test
    public void testPreHandle() throws Exception {
        // Given
        DelegateExecution delegateExecution = TestHelper.createDelegateExecution();

        // When
        baseRuntimeFlowableDelegate.preHandle(delegateExecution);

        // Then
        // Empty test, as preHandle is abstract and not implemented in this test class
    }

    @Test
    public void testInitTargetTaskInstanceId() throws Exception {
        // Given
        String processDefinitionId = TestConstants.PROCESS_DEFINITION_ID;
        String activityId = TestConstants.ACTIVITY_ID;

        doAnswer(invocation -> flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId)).when(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        doAnswer(invocation -> flowableAdaptor.getTaskInstanceByActivityId(activityId, flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId))).when(flowableAdaptor).getTaskInstanceByActivityId(activityId, flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId));

        // When
        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(TestHelper.createDelegateExecution());

        // Then
        assertEquals(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId), baseRuntimeFlowableDelegate.getFlowInstanceId());
        assertEquals(flowableAdaptor.getTaskInstanceIdByActivityIdAndFlowInstanceId(activityId, flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId)), baseRuntimeFlowableDelegate.getTargetTaskInstanceId());
        assertEquals(TestConstants.TASK_TYPE, baseRuntimeFlowableDelegate.getTaskType());
        assertEquals(TestConstants.EXECUTION_STRATEGY_CONFIG, baseRuntimeFlowableDelegate.getStrategyConfig());
    }

    @Test
    public void testBindToFlowTaskInstance() throws Exception {
        // Given
        FlowTaskInstance flowTaskInstance = TestHelper.createFlowTaskInstance();

        // When
        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);

        // Then
        assertEquals(baseRuntimeFlowableDelegate, flowTaskInstance.getServiceTask());
        assertEquals(baseRuntimeFlowableDelegate.getTargetTaskInstanceId(), flowTaskInstance.getId());
    }

    @Test
    public void testUpdateFlowInstanceStatus() throws Exception {
        // Given
        FlowStatus flowStatus = FlowStatus.COMPLETED;

        // When
        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(flowStatus);

        // Then
        PowerMockito.verifyStatic(FlowInstanceRepository.class).updateStatusById(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(TestConstants.PROCESS_DEFINITION_ID), flowStatus);
    }

    @Test
    public void testInitCallable_NormalCase() throws Exception {
        // Given
        String processDefinitionId = TestConstants.PROCESS_DEFINITION_ID;
        String activityId = TestConstants.ACTIVITY_ID;
        Callable<String> callable = () -> "test result";

        doAnswer(invocation -> callable).when(baseRuntimeFlowableDelegate).initCallable(TestHelper.createDelegateExecution());

        // When
        Callable<String> resultCallable = baseRuntimeFlowableDelegate.initCallable(TestHelper.createDelegateExecution());

        // Then
        assertEquals(callable, resultCallable);
    }

    @Test(expected = Exception.class)
    public void testInitCallable_ExceptionThrown() throws Exception {
        // Given
        String processDefinitionId = TestConstants.PROCESS_DEFINITION_ID;
        String activityId = TestConstants.ACTIVITY_ID;
        Callable<String> callable = () -> { throw new Exception("Test exception"); };

        doAnswer(invocation -> callable).when(baseRuntimeFlowableDelegate).initCallable(TestHelper.createDelegateExecution());

        // When
        Callable<String> resultCallable = baseRuntimeFlowableDelegate.initCallable(TestHelper.createDelegateExecution());

        // Then
        PreConditions.assertExceptionIsInstanceOf(Exception.class, resultCallable);
    }
}