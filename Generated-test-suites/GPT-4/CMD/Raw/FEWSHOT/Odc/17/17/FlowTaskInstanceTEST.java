package com.oceanbase.odc.service.flow.instance;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableAdaptor;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.flowable.engine.TaskService;

public class FlowTaskInstanceTest {

    @Mock
    private ServiceTaskInstanceRepository mockRepository;

    @Mock
    private TaskService mockService;

    @Mock
    private EventPublisher mockEventPublisher;

    @Mock
    private RuntimeDelegateMapper mockMapper;

    @Mock
    private FlowableAdaptor mockAdaptor;

    @Mock
    private NodeInstanceEntityRepository mockNodeRepository;

    @Mock
    private SequenceInstanceRepository mockSequenceRepository;

    private FlowTaskInstance testInstance;

    @Before 
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ExecutionStrategyConfig config = new ExecutionStrategyConfig();
        testInstance = new FlowTaskInstance(
            TaskType.DUMMY, 
            1L, // OrganizationID
            1L, // FlowInstanceID
            config, 
            true, // startEndpoint
            true, // endEndpoint
            mockMapper,
            mockAdaptor,
            mockEventPublisher,
            mockService,
            mockNodeRepository,
            mockSequenceRepository,
            mockRepository
        );
    }

    @Test
    public void testCreate() {
        testInstance.create();
        verify(mockRepository, times(1)).save(any());
    }

    @Test
    public void testUpdate() {
        testInstance.update();
        verify(mockRepository, times(1)).update(any());
    }

    @Test
    public void testDelete() {
        testInstance.doDelete(1L);
        verify(mockRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testCoreFlowableElementType() {
        assertTrue(testInstance.getCoreFlowableElementType() == FlowableElementType.SERVICE_TASK);
    }

    @Test
    public void testResourceType() {
        assertTrue(testInstance.resourceType().equals(ResourceType.ODC_FLOW_TASK_INSTANCE.name()));
    }

    @Test
    public void testIsPresentOnThisMachine() {
        boolean present = testInstance.isPresentOnThisMachine();
        assertTrue(present == false);
    }

    // additional test methods to cover all methods in FlowTaskInstance...
}