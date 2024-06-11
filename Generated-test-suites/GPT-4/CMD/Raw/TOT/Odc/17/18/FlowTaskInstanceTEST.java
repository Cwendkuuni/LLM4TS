
import org.junit.Before;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.core.shared.constant.TaskType;

public class FlowTaskInstanceTest {

    @Mock
    private RuntimeDelegateMapper mockMapper;

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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreate() {
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        Mockito.when(mockServiceTaskRepository.save(Mockito.any())).thenReturn(entity);

        FlowTaskInstance instance = new FlowTaskInstance(TaskType.SUB_TASK, 1L, 1L,
                ExecutionStrategyConfig.defaultConfig(), false ,false, mockMapper, mockFlowableAdaptor,
                mockEventPublisher, mockTaskService, mockNodeRepository, mockSequenceRepository,
                mockServiceTaskRepository);

        instance.create();
        Assert.assertNotNull("Id should not be null", instance.getId());
    }

    @Test
    public void testUpdate() {
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        Mockito.when(mockServiceTaskRepository.update(Mockito.any())).thenReturn(1);

        FlowTaskInstance instance = new FlowTaskInstance(entity, mockMapper, mockFlowableAdaptor,
                mockEventPublisher, mockTaskService, mockNodeRepository, mockSequenceRepository,
                mockServiceTaskRepository);

        instance.update();
        Assert.assertEquals("Status should be updated", FlowNodeStatus.PENDING, instance.getStatus());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testAllocWhenListenerExist() {
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        FlowTaskInstance instance = new FlowTaskInstance(entity, mockMapper, mockFlowableAdaptor,
                mockEventPublisher, mockTaskService, mockNodeRepository, mockSequenceRepository,
                mockServiceTaskRepository);

        instance.alloc(); //First alloc
        instance.alloc(); //Second alloc which is expected to throw UnsupportedOperationException
    }

    @Test
    public void testBatchCreateWhenInstancesIsEmpty() {
        Assert.assertEquals("Result should be empty", Collections.emptyList(), 
            FlowTaskInstance.batchCreate(Collections.emptyList(), mockServiceTaskRepository));
    }
}
