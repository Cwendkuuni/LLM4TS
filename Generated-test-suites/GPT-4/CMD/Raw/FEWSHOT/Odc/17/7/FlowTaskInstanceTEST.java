
package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.task.api.TaskInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FlowTaskInstanceTest {

    @Test
    public void testBatchCreate() {
        FlowTaskInstance firstInstance = getMockedTaskInstance();
        FlowTaskInstance secondInstance = getMockedTaskInstance();

        List<FlowTaskInstance> instances = Arrays.asList(firstInstance, secondInstance);
        List<FlowTaskInstance> createdInstances = FlowTaskInstance.batchCreate(instances, serviceTaskInstanceRepository);

        assertTrue(CollectionUtils.isEqualCollection(instances, createdInstances));
    }

    @Test
    public void testCreate() {
        FlowTaskInstance instance = getMockedTaskInstance();
        instance.create();
        assertNotNull(instance.getId());
    }

    @Test
    public void testUpdate() {
        FlowTaskInstance instance = getMockedTaskInstance();
        instance.create();
        instance.setStatus(FlowNodeStatus.COMPLETED);
        instance.update();
        assertEquals(FlowNodeStatus.COMPLETED, instance.getStatus());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancel() throws Exception {
        FlowTaskInstance instance = getMockedTaskInstance();
        instance.create();
        instance.cancel(true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsDone() throws Exception {
        FlowTaskInstance instance = getMockedTaskInstance();
        instance.create();
        instance.isDone();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResult() throws InterruptedException, ExecutionException {
        FlowTaskInstance instance = getMockedTaskInstance();
        instance.create();
        instance.getResult();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        FlowTaskInstance instance = getMockedTaskInstance();
        instance.create();
        instance.getResult(200L, TimeUnit.MILLISECONDS);
    }

    private FlowTaskInstance getMockedTaskInstance(){
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
        RuntimeDelegateMapper delegateConvertor = mock(RuntimeDelegateMapper.class);
        EventPublisher eventPublisher = mock(EventPublisher.class);
        TaskService taskService = mock(TaskService.class);
        NodeInstanceEntityRepository nodeRepository = mock(NodeInstanceEntityRepository.class);
        SequenceInstanceRepository sequenceRepository = mock(SequenceInstanceRepository.class);
        ServiceTaskInstanceRepository serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);
        TaskType taskType = mock(TaskType.class);
        ExecutionStrategyConfig strategyConfig = mock(ExecutionStrategyConfig.class);

        FlowTaskInstance instance = new FlowTaskInstance(taskType, 1L, 1L, strategyConfig, true, true,
                delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
                serviceTaskRepository);
       return instance;
    }
}
