package com.oceanbase.odc.service.flow.instance;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.mockito.Mockito.when;

public class FlowTaskInstanceTest {
    @Mock
    private ServiceTaskInstanceEntity entity;
    @Mock
    private RuntimeDelegateMapper convertor;
    @Mock
    private FlowableAdaptor flowableAdaptor;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private TaskService taskService;
    @Mock
    private NodeInstanceEntityRepository nodeEntityRepo;
    @Mock
    private SequenceInstanceRepository seqRepo;
    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepo;
    @Mock
    private BaseRuntimeFlowableDelegate targetTaskHandle;
    @Mock
    private ExecutionStrategyConfig strategyConfig;

    private FlowTaskInstance instance;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(entity.getTaskType()).thenReturn(TaskType.UNKNOWN);
        when(entity.getOrganizationId()).thenReturn(1L);
        when(entity.getFlowInstanceId()).thenReturn(1L);
        when(entity.isStartEndpoint()).thenReturn(true);
        when(entity.isEndEndpoint()).thenReturn(false);
        instance = new FlowTaskInstance(entity, convertor, flowableAdaptor, eventPublisher, taskService, nodeEntityRepo, seqRepo, serviceTaskRepo);
    }

    @Test
    public void testBindServiceTask() {
        when(targetTaskHandle.getTargetTaskInstanceId()).thenReturn(1L);
        instance.bindServiceTask(targetTaskHandle);
        Assert.assertEquals(FlowNodeStatus.EXECUTING, instance.getStatus());
    }

    @Test
    public void testIsPresentOnThisMachine() {
        instance.bindServiceTask(targetTaskHandle);
        Assert.assertTrue(instance.isPresentOnThisMachine());
    }

    @Test
    public void testConfirmExecute() {
        instance.setStatus(FlowNodeStatus.PENDING);
        instance.confirmExecute();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancel() {
        instance.cancel(false);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsDone() throws InterruptedException, ExecutionException {
        instance.isDone();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResult() throws InterruptedException, ExecutionException {
        instance.getResult();
    }

    @Test
    public void testBatchCreate() {
        when(serviceTaskRepo.batchCreate(Mockito.anyList())).thenReturn(Arrays.asList(entity));
        Assert.assertEquals(1, FlowTaskInstance.batchCreate(Arrays.asList(instance), serviceTaskRepo).size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAbort() {
        instance.abort();
    }

    @Test
    public void testCreate() {
        instance.create();
    }

    @Test
    public void testUpdate() {
        instance.update();
    }

    @Test
    public void testResourceType() {
        Assert.assertEquals("ODC_FLOW_TASK_INSTANCE", instance.resourceType());
    }

    @Test
    public void testGetTargetDelegateClass() {
        Assert.assertEquals(BaseRuntimeFlowableDelegate.class, instance.getTargetDelegateClass());
    }
}
