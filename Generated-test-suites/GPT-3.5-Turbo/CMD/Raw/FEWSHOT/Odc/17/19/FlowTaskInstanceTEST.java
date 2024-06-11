import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.*;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.apache.commons.collections4.CollectionUtils;

public class FlowTaskInstanceTest {

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("foo"));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("foo bar"));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
        assertEquals("foo and bar", Util.stripLeadingAndTrailingQuotes("foo and bar"));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
    }

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("foo", Util.stripLeadingHyphens("---foo"));
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = new ArrayList<>();
        ServiceTaskInstanceRepository serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
        when(CollectionUtils.isEmpty(instances)).thenReturn(true);
        assertEquals(Collections.emptyList(), FlowTaskInstance.batchCreate(instances, serviceTaskInstanceRepository));
    }

    @Test
    public void testCreate() {
        FlowTaskInstance instance = mock(FlowTaskInstance.class);
        instance.create();
        verify(instance, times(1)).validNotExists();
        verify(instance, times(1)).alloc();
        verify(instance, times(1)).eventPublisher.publishEvent(any());
    }

    @Test
    public void testUpdate() {
        FlowTaskInstance instance = mock(FlowTaskInstance.class);
        instance.update();
        verify(instance, times(1)).validExists();
    }

    @Test
    public void testDelete() {
        FlowTaskInstance instance = mock(FlowTaskInstance.class);
        instance.delete();
        verify(instance, times(1)).validExists();
        verify(instance.serviceTaskRepository, times(1)).deleteById(instance.getId());
    }

    @Test
    public void testIsPresentOnThisMachine() {
        FlowTaskInstance instance = new FlowTaskInstance();
        assertFalse(instance.isPresentOnThisMachine());
    }

    @Test
    public void testAlloc() {
        FlowTaskInstance instance = mock(FlowTaskInstance.class);
        instance.alloc();
        verify(instance, times(1)).eventPublisher.addEventListener(any());
    }

    @Test
    public void testDealloc() {
        FlowTaskInstance instance = mock(FlowTaskInstance.class);
        instance.dealloc();
        verify(instance, times(1)).eventPublisher.removeEventListener(any());
    }

    @Test
    public void testComplete() {
        FlowTaskInstance instance = mock(FlowTaskInstance.class);
        instance.complete(false);
        verify(instance, times(1)).alloc();
    }

    @Test
    public void testBindServiceTask() {
        FlowTaskInstance instance = mock(FlowTaskInstance.class);
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        instance.bindServiceTask(target);
        assertEquals(target, instance.targetTaskHandle);
        assertEquals(FlowNodeStatus.EXECUTING, instance.getStatus());
    }

}