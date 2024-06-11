package com.oceanbase.odc.service.flow.instance;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest {

    @Mock
    private ServiceTaskInstanceEntity serviceTaskInstanceEntity;

    @Mock
    private RuntimeDelegateMapper runtimeDelegateMapper;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private TaskService taskService;

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    private SequenceInstanceRepository sequenceInstanceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        flowTaskInstance = new FlowTaskInstance(serviceTaskInstanceEntity, runtimeDelegateMapper, flowableAdaptor, eventPublisher, taskService, nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);
    }

    @Test
    public void testGetCoreFlowableElementType() {
        Assert.assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate baseRuntimeFlowableDelegate = mock(BaseRuntimeFlowableDelegate.class);
        when(baseRuntimeFlowableDelegate.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
        flowTaskInstance.bindServiceTask(baseRuntimeFlowableDelegate);
        Assert.assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testIsPresentOnThisMachine() {
        Assert.assertFalse(flowTaskInstance.isPresentOnThisMachine());
        BaseRuntimeFlowableDelegate baseRuntimeFlowableDelegate = mock(BaseRuntimeFlowableDelegate.class);
        when(baseRuntimeFlowableDelegate.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
        flowTaskInstance.bindServiceTask(baseRuntimeFlowableDelegate);
        Assert.assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancelWithoutBind() {
        flowTaskInstance.cancel(true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsDoneWithoutBind() {
        flowTaskInstance.isDone();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResultWithoutBind() throws Exception {
        flowTaskInstance.getResult();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResultWithTimeoutWithoutBind() throws Exception {
        flowTaskInstance.getResult(1, TimeUnit.SECONDS);
    }

    @Test
    public void testResourceType() {
        Assert.assertEquals("ODC_FLOW_TASK_INSTANCE", flowTaskInstance.resourceType());
    }

    @Test
    public void testGetTargetDelegateClass() {
        when(runtimeDelegateMapper.map(any())).thenReturn(BaseRuntimeFlowableDelegate.class);
        Assert.assertEquals(BaseRuntimeFlowableDelegate.class, flowTaskInstance.getTargetDelegateClass());
    }
}
