import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.EventPublisher;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.Test;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class FlowTaskInstanceTest {

    @Test
    public void testCreate() {
        ServiceTaskInstanceRepository mockRepository = mock(ServiceTaskInstanceRepository.class);
        FlowTaskInstance instance = new FlowTaskInstance(null, null, null, null, null, null, null, null, null, null, mockRepository);
        instance.create();
        assertNotNull(instance.getId());
        assertNotNull(instance.getCreateTime());
        assertNotNull(instance.getUpdateTime());
    }

    @Test
    public void testUpdate() {
        ServiceTaskInstanceRepository mockRepository = mock(ServiceTaskInstanceRepository.class);
        FlowTaskInstance instance = new FlowTaskInstance(null, null, null, null, null, false, false, null, null, null, null, mockRepository);
        instance.update();
    }

    @Test
    public void testBatchCreate() {
        ServiceTaskInstanceRepository mockRepository = mock(ServiceTaskInstanceRepository.class);
        FlowTaskInstance instance = new FlowTaskInstance(null, null, null, null, null, false, false, null, null, null, null, mockRepository);
        FlowTaskInstance.batchCreate(List.of(instance), mockRepository);
        assertNotNull(instance.getId());
        assertNotNull(instance.getCreateTime());
        assertNotNull(instance.getUpdateTime());
    }

    @Test
    public void testBindServiceTask() {
        RuntimeDelegateMapper mockMapper = mock(RuntimeDelegateMapper.class);
        FlowTaskInstance instance = new FlowTaskInstance(null, null, null, null, null, false, false, null, null, null, null, null);
        instance.bindServiceTask(null);
        assertEquals(instance.getStatus(), FlowNodeStatus.EXECUTING);
    }

    @Test
    public void testConfirmExecute() {
        FlowTaskInstance instance = new FlowTaskInstance(null, null, null, null, null, false, false, null, null, null, null, null);
        instance.confirmExecute();
    }

    @Test
    public void testAbort() {
        FlowTaskInstance instance = new FlowTaskInstance(null, null, null, null, null, false, false, null, null, null, null, null);
        assertThrows(UnsupportedOperationException.class, instance::abort);
    }

    @Test
    public void testCancel() {
        FlowTaskInstance instance = new FlowTaskInstance(null, null, null, null, null, false, false, null, null, null, null, null);
        assertThrows(UnsupportedOperationException.class, () -> instance.cancel(false));
    }

    @Test
    public void testIsDone() {
        FlowTaskInstance instance = new FlowTaskInstance(null, null, null, null, null, false, false, null, null, null, null, null);
        assertThrows(UnsupportedOperationException.class, instance::isDone);
    }

    @Test
    public void testGetResult() {
        FlowTaskInstance instance = new FlowTaskInstance(null, null, null, null, null, false, false, null, null, null, null, null);
        assertThrows(UnsupportedOperationException.class, instance::getResult);
    }

    @Test
    public void testGetResultWithTime() {
        FlowTaskInstance instance = new FlowTaskInstance(null, null, null, null, null, false, false, null, null, null, null, null);
        assertThrows(UnsupportedOperationException.class, () -> instance.getResult(1, TimeUnit.SECONDS));
    }

    @Test
    public void testResourceType() {
        ServiceTaskInstanceRepository mockRepository = mock(ServiceTaskInstanceRepository.class);
        FlowTaskInstance instance = new FlowTaskInstance(null, null, null, null, null, false, false, null, null, null, null, mockRepository);
        assertEquals(instance.resourceType(), ResourceType.ODC_FLOW_TASK_INSTANCE.name());
    }

}