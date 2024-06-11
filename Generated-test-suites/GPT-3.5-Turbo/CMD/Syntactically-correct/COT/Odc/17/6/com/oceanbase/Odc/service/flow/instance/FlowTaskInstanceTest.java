
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

public class FlowTaskInstanceTest {
  
  private FlowTaskInstance flowTaskInstance;
  private ServiceTaskInstanceEntity entity;
  private RuntimeDelegateMapper convertor;
  private FlowableAdaptor flowableAdaptor;
  private EventPublisher eventPublisher;
  private TaskService taskService;
  private NodeInstanceEntityRepository nodeRepository;
  private SequenceInstanceRepository sequenceRepository;
  private ServiceTaskInstanceRepository serviceTaskRepository;

  @Before
  public void setUp() {
    entity = mock(ServiceTaskInstanceEntity.class);
    convertor = mock(RuntimeDelegateMapper.class);
    flowableAdaptor = mock(FlowableAdaptor.class);
    eventPublisher = mock(EventPublisher.class);
    taskService = mock(TaskService.class);
    nodeRepository = mock(NodeInstanceEntityRepository.class);
    sequenceRepository = mock(SequenceInstanceRepository.class);
    serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);

    flowTaskInstance = new FlowTaskInstance(entity, convertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
  }

  @Test
  public void testBindServiceTask() {
    BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
    flowTaskInstance.bindServiceTask(target);

    assertTrue(flowTaskInstance.isPresentOnThisMachine());
    assertEquals(target, flowTaskInstance.getTargetTaskHandle());
    assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
  }

  @Test
  public void testConfirmExecute() {
    flowTaskInstance.confirmExecute();
    // Add assertions here
  }

  @Test
  public void testAbort() {
    flowTaskInstance.abort();
    // Add assertions here
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testCancel_NotPresentOnThisMachine() {
    flowTaskInstance.cancel(true);
  }

  @Test
  public void testIsDone() {
    BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
    flowTaskInstance.bindServiceTask(target);

    assertTrue(flowTaskInstance.isDone());
  }

  @Test
  public void testGetResult() throws InterruptedException, ExecutionException {
    BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
    flowTaskInstance.bindServiceTask(target);

    Object result = flowTaskInstance.getResult();
    // Add assertions here
  }

  @Test
  public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
    BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
    flowTaskInstance.bindServiceTask(target);

    Object result = flowTaskInstance.getResult(10, TimeUnit.SECONDS);
    // Add assertions here
  }

  @Test
  public void testResourceType() {
    assertEquals("ODC_FLOW_TASK_INSTANCE", flowTaskInstance.resourceType());
  }

  @Test
  public void testGetTargetDelegateClass() {
    Class<? extends BaseRuntimeFlowableDelegate<?>> delegateClass = flowTaskInstance.getTargetDelegateClass();
    // Add assertions here
  }

}
