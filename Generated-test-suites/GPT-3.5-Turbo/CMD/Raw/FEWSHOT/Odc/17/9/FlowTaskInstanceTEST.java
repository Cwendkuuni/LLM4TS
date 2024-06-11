package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;
    private ServiceTaskInstanceEntity entityMock = mock(ServiceTaskInstanceEntity.class);
    private RuntimeDelegateMapper runtimeDelegateMapperMock = mock(RuntimeDelegateMapper.class);
    private FlowableAdaptor flowableAdaptorMock = mock(FlowableAdaptor.class);
    private EventPublisher eventPublisherMock = mock(EventPublisher.class);
    private TaskService taskServiceMock = mock(TaskService.class);
    private NodeInstanceEntityRepository nodeRepositoryMock = mock(NodeInstanceEntityRepository.class);
    private SequenceInstanceRepository sequenceRepositoryMock = mock(SequenceInstanceRepository.class);
    private ServiceTaskInstanceRepository serviceTaskRepositoryMock = mock(ServiceTaskInstanceRepository.class);

    @Before
    public void setUp() {
        flowTaskInstance = new FlowTaskInstance(entityMock, runtimeDelegateMapperMock, flowableAdaptorMock,
            eventPublisherMock, taskServiceMock, nodeRepositoryMock, sequenceRepositoryMock, serviceTaskRepositoryMock);
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowNodeType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
        when(CollectionUtils.isEmpty(instances)).thenReturn(false);
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setId(1L);
        when(entityMock.getId()).thenReturn(1L);
        when(entityMock.getCreateTime()).thenReturn(1L);
        when(entityMock.getUpdateTime()).thenReturn(1L);
        when(serviceTaskRepositoryMock.batchCreate(Collections.singletonList(entityMock)))
            .thenReturn(Collections.singletonList(entity));
        List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskRepositoryMock);
        assertTrue(result.size() == 1);
        assertEquals(1L, result.get(0).getId());
        assertEquals(1L, result.get(0).getCreateTime());
        assertEquals(1L, result.get(0).getUpdateTime());
    }

    @Test
    public void testCreate() {
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        when(entityMock.getId()).thenReturn(1L);
        when(entityMock.getCreateTime()).thenReturn(1L);
        when(entityMock.getUpdateTime()).thenReturn(1L);
        when(serviceTaskRepositoryMock.save(entityMock)).thenReturn(entity);
        flowTaskInstance.create();
        assertEquals(1L, flowTaskInstance.getId());
        assertEquals(1L, flowTaskInstance.getCreateTime());
        assertEquals(1L, flowTaskInstance.getUpdateTime());
    }

    @Test
    public void testUpdate() {
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setId(1L);
        entity.setStatus("status");
        entity.setTargetTaskId(2L);
        entity.setTaskType(TaskType.TYPE1);
        when(entityMock.getId()).thenReturn(1L);
        when(serviceTaskRepositoryMock.update(entityMock)).thenReturn(1);
        flowTaskInstance.update();
        assertEquals(1, flowTaskInstance.getId());
        assertEquals("status", flowTaskInstance.getStatus());
        assertEquals(2L, flowTaskInstance.getTargetTaskId());
        assertEquals(TaskType.TYPE1, flowTaskInstance.getTaskType());
    }

    @Test
    public void testComplete() {
        ServiceTaskStartedListener listenerMock = mock(ServiceTaskStartedListener.class);
        Task taskMock = mock(Task.class);
        when(flowTaskInstance.getStrategyConfig()).thenReturn(new ExecutionStrategyConfig());
        when(entityMock.getId()).thenReturn(1L);
        when(entityMock.getFlowInstanceId()).thenReturn(1L);
        when(taskServiceMock.createTaskQuery().processInstanceId("1").list())
            .thenReturn(Collections.singletonList(taskMock));
        flowTaskInstance.alloc();
        flowTaskInstance.bindServiceTask(null);
        flowTaskInstance.confirmExecute();
        flowTaskInstance.abort();
        flowTaskInstance.cancel(false);
        flowTaskInstance.isDone();
        flowTaskInstance.getResult();
        try {
            flowTaskInstance.getResult(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotNull(flowTaskInstance.resourceType());
        assertNotNull(flowTaskInstance.getTargetDelegateClass());
    }
}