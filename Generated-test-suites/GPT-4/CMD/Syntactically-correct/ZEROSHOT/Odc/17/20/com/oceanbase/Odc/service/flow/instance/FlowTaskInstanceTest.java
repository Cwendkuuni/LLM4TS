
package com.oceanbase.odc.service.flow.instance;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.BaseFlowNodeInstanceTest;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

@RunWith(MockitoJUnitRunner.class)
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
    private NodeInstanceEntityRepository nodeRepository;
    @Mock
    private SequenceInstanceRepository sequenceRepository;
    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepository;
    @Mock
    private BaseRuntimeFlowableDelegate runtimeDelegate;

    private FlowTaskInstance instance;

    @Before
    public void setUp() {
        instance = new FlowTaskInstance(entity, convertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, instance.getCoreFlowableElementType());
    }

    @Test
    public void testIsPresentOnThisMachine() {
        assertFalse(instance.isPresentOnThisMachine());
        instance.bindServiceTask(runtimeDelegate);
        assertTrue(instance.isPresentOnThisMachine());
    }
    
    @Test
    public void testAbort() {
        instance.abort();
        assertNotEquals(FlowNodeStatus.PENDING, instance.getStatus());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testInvalidIsDone() {
        instance.isDone();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testInvalidGetResult() throws Exception {
        instance.getResult();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testInvalidGetResultWithTimeout() throws Exception {
        instance.getResult(10, TimeUnit.SECONDS);
    }
   
    @Test
    public void testComplete() {
        instance.complete(false);
        assertNotEquals(FlowNodeStatus.PENDING, instance.getStatus());
    }
}
