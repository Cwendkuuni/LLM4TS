
package com.oceanbase.odc.service.flow.instance;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class FlowTaskInstanceTest {
    @Mock TaskService mockTaskService;
    @Mock EventPublisher mockEventPublisher;
    @Mock RuntimeDelegateMapper mockDelegateConvertor;
    @Mock FlowableAdaptor mockFlowableAdaptor;
    @Mock ServiceTaskInstanceRepository mockServiceTaskRepository;
    @Mock SequenceInstanceRepository mockSequenceRepository;
    @Mock NodeInstanceEntityRepository mockNodeRepository;

    private ServiceTaskInstanceEntity entity;
    private ExecutionStrategyConfig strategyConfig;
    private FlowTaskInstance taskInstance;
    private Field nodeTypeField;

    @Before
    public void init() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.initMocks(this);

        entity = new ServiceTaskInstanceEntity();
        strategyConfig = ExecutionStrategyConfig.from(entity);
        
        taskInstance = new FlowTaskInstance(entity, mockDelegateConvertor, mockFlowableAdaptor, mockEventPublisher, 
            mockTaskService, mockNodeRepository, mockSequenceRepository, mockServiceTaskRepository);
        
        nodeTypeField = BaseFlowNodeInstance.class.getDeclaredField("nodeType");
        nodeTypeField.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(nodeTypeField, nodeTypeField.getModifiers() & ~Modifier.FINAL);
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(taskInstance.getCoreFlowableElementType(), FlowableElementType.SERVICE_TASK);
    }

    @Test
    public void testCreate() {
        taskInstance.create();
        verify(mockServiceTaskRepository, times(1)).save(entity);
    }

    @Test
    public void testUpdate() {
        taskInstance.update();
        verify(mockServiceTaskRepository, times(1)).update(entity);
    }

    @Test
    public void testDoDelete() {
        taskInstance.doDelete(entity.getId());
        verify(mockServiceTaskRepository, times(1)).deleteById(entity.getId());
    }

    @Test
    public void testIsPresentOnThisMachine() {
        assertFalse(taskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> mockTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
        when(mockTaskHandle.get()).thenReturn(new Object());
        taskInstance.bindServiceTask(mockTaskHandle);
        assertNotNull(taskInstance.getResult());
    }

    @Test
    public void testConfirmExecute() {
        taskInstance.confirmExecute();
        verify(mockFlowableAdaptor, times(1)).getProcessInstanceIdByFlowInstanceId(anyLong());
    }

    @Test
    public void testAbort() {
        taskInstance.abort();
        verify(mockFlowableAdaptor, times(1)).getProcessInstanceIdByFlowInstanceId(anyLong());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancel() {
        taskInstance.cancel(true);
    }

    @Test
    public void testGetTargetDelegateClass() {
        assertNotNull(taskInstance.getTargetDelegateClass());
    }
}
