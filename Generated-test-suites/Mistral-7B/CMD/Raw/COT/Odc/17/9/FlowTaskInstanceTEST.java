import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FlowTaskInstanceTest {

 @InjectMocks
 private FlowTaskInstance flowTaskInstance;

 @Mock
 private RuntimeDelegateMapper delegateConvertor;

 @Mock
 private EventPublisher eventPublisher;

 @Mock
 private TaskService taskService;

 @Mock
 private FlowableAdaptor flowableAdaptor;

 @Mock
 private NodeInstanceEntityRepository nodeRepository;

 @Mock
 private SequenceInstanceRepository sequenceRepository;

 @Mock
 private ServiceTaskInstanceRepository serviceTaskRepository;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 flowTaskInstance = new FlowTaskInstance(null, delegateConvertor, flowableAdaptor, eventPublisher, taskService,
 nodeRepository, sequenceRepository, serviceTaskRepository);
 }

 @Test
 public void testGetCoreFlowableElementType() {
 Assert.assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
 }

 @Test
 public void testBatchCreate() throws Exception {
 List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
 ServiceTaskInstanceRepository mockServiceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
 when(serviceTaskRepository).thenReturn(mockServiceTaskInstanceRepository);
 List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
 Assert.assertEquals(instances, result);
 }

 @Test
 public void testCreate() throws Exception {
 ServiceTaskInstanceRepository mockServiceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
 doAnswer(new Answer<ServiceTaskInstanceEntity>() {
 @Override
 public ServiceTaskInstanceEntity answer(ServiceTaskInstanceEntity entity) {
 entity.setId(1L);
 entity.setCreateTime(System.currentTimeMillis());
 entity.setUpdateTime(System.currentTimeMillis());
 return entity;
 }
 }).when(mockServiceTaskInstanceRepository).save(any(ServiceTaskInstanceEntity.class));
 when(serviceTaskRepository).thenReturn(mockServiceTaskInstanceRepository);
 flowTaskInstance.create();
 Assert.assertNotNull(flowTaskInstance.getId());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCreateWithExistsInstance() {
 ServiceTaskInstanceEntity mockEntity = mock(ServiceTaskInstanceEntity.class);
 when(serviceTaskRepository.findById(anyLong())).thenReturn(Optional.of(mockEntity));
 flowTaskInstance = new FlowTaskInstance(null, delegateConvertor, flowableAdaptor, eventPublisher, taskService,
 nodeRepository, sequenceRepository, serviceTaskRepository);
 flowTaskInstance.create();
 }

 @Test
 public void testUpdate() throws Exception {
 ServiceTaskInstanceEntity mockEntity = mock(ServiceTaskInstanceEntity.class);
 when(serviceTaskRepository.findById(anyLong())).thenReturn(Optional.of(mockEntity));
 when(serviceTaskRepository.update(any(ServiceTaskInstanceEntity.class))).thenAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(Integer ints) {
 return 1;
 }
 });
 flowTaskInstance.update();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUpdateWithNotExistsInstance() {
 flowTaskInstance = new FlowTaskInstance(null, delegateConvertor, flowableAdaptor, eventPublisher, taskService,
 nodeRepository, sequenceRepository, serviceTaskRepository);
 flowTaskInstance.update();
 }

 @Test
 public void testDelete() throws Exception {
 ServiceTaskInstanceRepository mockServiceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
 doAnswer(new Answer<Void>() {
 @Override
 public Void answer(Void aVoid) {
 return null;
 }
 }).when(mockServiceTaskInstanceRepository).deleteById(anyLong());
 when(serviceTaskRepository).thenReturn(mockServiceTaskInstanceRepository);
 flowTaskInstance.doDelete(1L);
 }

 @Test
 public void testBindServiceTask() {
 BaseRuntimeFlowableDelegate<?> mockDelegate = mock(BaseRuntimeFlowableDelegate.class);
 flowTaskInstance.bindServiceTask(mockDelegate);
 Assert.assertNotNull(flowTaskInstance.getTargetTaskHandle());
 }

 @Test
 public void testIsPresentOnThisMachine() {
 BaseRuntimeFlowableDelegate<?> mockDelegate = mock(BaseRuntimeFlowableDelegate.class);
 flowTaskInstance.bindServiceTask(mockDelegate);
 Assert.assertTrue(flowTaskInstance.isPresentOnThisMachine());
 }

 @Test
 public void testDealloc() {
 eventPublisher = mock(EventPublisher.class);
 ServiceTaskStartedListener mockListener = mock(ServiceTaskStartedListener.class);
 doAnswer(new Answer<Void>() {
 @Override
 public Void answer(Void aVoid) {
 return null;
 }
 }).when(eventPublisher).removeEventListener(mockListener);
 flowTaskInstance.dealloc();
 Assert.assertNull(flowTaskInstance.getListener());
 }

 @Test
 public void testAlloc() {
 eventPublisher = mock(EventPublisher.class);
 ServiceTaskStartedListener mockListener = mock(ServiceTaskStartedListener.class);
 doAnswer(new Answer<Void>() {
 @Override
 public Void answer(Void aVoid) {
 return null;
 }
 }).when(eventPublisher).addEventListener(mockListener);
 flowTaskInstance.alloc();
 Assert.assertNotNull(flowTaskInstance.getListener());
 }

 @Test
 public void testConfirmExecute() throws InterruptedException, ExecutionException {
 // test confirmExecute logic here
 }

 @Test
 public void testAbort() throws InterruptedException, ExecutionException {
 // test abort logic here
 }

 @Test
 public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
 // test cancel logic here
 }

 @Test
 public void testIsDone() throws InterruptedException, ExecutionException {
 // test isDone logic here
 }

 @Test
 public void testGetResult() throws InterruptedException, ExecutionException {
 // test getResult logic here
 }

 @Test
 public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
 // test getResult with timeout logic here
 }

 @Test
 public void testGetTargetDelegateClass() {
 Class<? extends BaseRuntimeFlowableDelegate<?>> result = flowTaskInstance.getTargetDelegateClass();
 Assert.assertEquals(BaseRuntimeFlowableDelegate.class, result);
 }

 @Test
 public void testComplete() throws InterruptedException, ExecutionException {
 // test complete logic here
 }

 @Test
 public void testMapToTaskEntity() {
 ServiceTaskInstanceEntity mockEntity = mock(ServiceTaskInstanceEntity.class);
 FlowTaskInstance instance = new FlowTaskInstance(null, null, null, null, null, null, null, null, null, null, null, null);
 FlowTaskInstance result = new FlowTaskInstance(instance.getId(), null, null, null, null, null, delegateConvertor, null, null, null, null, null);
 Assert.assertEquals(result.mapToTaskEntity(instance), mockEntity);
 }

}