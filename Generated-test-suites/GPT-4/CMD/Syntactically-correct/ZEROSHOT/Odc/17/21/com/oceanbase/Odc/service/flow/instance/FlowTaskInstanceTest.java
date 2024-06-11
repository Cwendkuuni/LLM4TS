
package com.oceanbase.odc.service.flow.instance;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;


class FlowTaskInstanceTest {

    @Mock
    private RuntimeDelegateMapper mockDelegateConverter;
    @Mock
    private FlowableAdaptor mockFlowableAdaptor;
    @Mock
    private EventPublisher mockEventPublisher;
    @Mock
    private TaskService mockTaskService;
    @Mock
    private NodeInstanceEntityRepository mockNodeRepository;
    @Mock
    private SequenceInstanceRepository mockSequenceRepository;
    @Mock
    private ServiceTaskInstanceRepository mockServiceTaskRepository;

    private FlowTaskInstance testInstance;
    private ExecutionStrategyConfig strategyConfig;
    private Long organizationId = 1L;
    private Long flowInstanceId = 1L;
    private boolean startEndpoint = false;
    private boolean endEndPoint = false;
    @Mock
    private Task mockTask;
    @Mock
    private FlowableElement mockFlowableElement;
    @Mock
    private ServiceTaskInstanceEntity mockEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        strategyConfig = new ExecutionStrategyConfig(FlowTaskExecutionStrategy.AUTO, 1, 1);
        testInstance = new FlowTaskInstance(TaskType.DOCUMENT_RETRIEVE, organizationId,
                flowInstanceId, strategyConfig, startEndpoint, endEndPoint,
                mockDelegateConverter, mockFlowableAdaptor, mockEventPublisher,
                mockTaskService, mockNodeRepository, mockSequenceRepository, mockServiceTaskRepository);
    }

    @Test
    void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, testInstance.getCoreFlowableElementType());
    }

    @Test
    void testBatchCreate() {
        List<FlowTaskInstance> instances = new ArrayList<>();
        instances.add(testInstance);
        when(mockServiceTaskRepository.batchCreate(any())).thenReturn(Collections.emptyList());
        FlowTaskInstance.batchCreate(instances, mockServiceTaskRepository);
    }

    @Test
    void testCreate() {
        when(mockServiceTaskRepository.save(any())).thenReturn(mockEntity);
        when(mockEntity.getId()).thenReturn(1L);
        when(mockEntity.getCreateTime()).thenReturn(1L);
        when(mockEntity.getUpdateTime()).thenReturn(1L);
        testInstance.create();
        assertEquals(1L, testInstance.getId());
    }

    @Test
    void testUpdate() {
        when(mockServiceTaskRepository.update(any())).thenReturn(1);
        testInstance.update();
    }

    @Test
    void testDelete() {
        testInstance.delete();
        verify(mockServiceTaskRepository).deleteById(any());
    }

    @Test
    void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> stubDelegate = mock(BaseRuntimeFlowableDelegate.class);
        when(stubDelegate.getTargetTaskInstanceId()).thenReturn(1L);
        testInstance.bindServiceTask(stubDelegate);
        assertEquals(FlowNodeStatus.PENDING, testInstance.getStatus());  
    }

    @Test
    void testIsPresentOnThisMachine() {
        assertFalse(testInstance.isPresentOnThisMachine());
    }

    @Test
    void testDealloc() {
        testInstance.dealloc();
    }

    @Test
    void testAlloc() {
        testInstance.alloc();
    }

    @Test
    void testConfirmExecute() {
        testInstance.confirmExecute();
    }

    @Test
    void testAbort() {
        testInstance.abort();
    }

    @Test
    void testCancel() {
        assertThrows(UnsupportedOperationException.class, () -> testInstance.cancel(false));
    }

    @Test
    void testIsDone() {
        assertThrows(UnsupportedOperationException.class, () -> testInstance.isDone());
    }

    @Test
    void testGetResult() {
        assertThrows(UnsupportedOperationException.class, () -> testInstance.getResult());
    }

    @Test
    void testGetResultTimeout() {
        assertThrows(UnsupportedOperationException.class, () -> testInstance.getResult(1L, TimeUnit.SECONDS));
    }

    @Test
    void testResourceType() {
        assertEquals("ODC_FLOW_TASK_INSTANCE", testInstance.resourceType());
    }

    @Test
    void testGetTargetDelegateClass() {
        Class<? extends BaseRuntimeFlowableDelegate<?>> targetClass = testInstance.getTargetDelegateClass();
    }

    @Test
    void testMapToTaskEntity() {
        ServiceTaskInstanceEntity entity = FlowTaskInstance.mapToTaskEntity(testInstance);
        assertEquals(organizationId, entity.getOrganizationId());
        assertEquals(FlowNodeStatus.PENDING, entity.getStatus());
        assertEquals(flowInstanceId, entity.getFlowInstanceId());
        assertEquals(false, entity.isStartEndpoint());
        assertEquals(false, entity.isEndEndpoint());
    }
}
