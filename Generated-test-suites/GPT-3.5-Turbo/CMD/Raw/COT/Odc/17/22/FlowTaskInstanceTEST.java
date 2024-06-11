
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;

public class FlowTaskInstanceTest {
  
  @Test
  public void testBindServiceTask() {
    FlowTaskInstance flowTaskInstance = Mockito.mock(FlowTaskInstance.class);
    BaseRuntimeFlowableDelegate<?> target = new BaseRuntimeFlowableDelegate<>();
    
    flowTaskInstance.bindServiceTask(target);
    
    assertEquals(target, flowTaskInstance.getTargetTaskHandle());
    assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
  }
  
  @Test
  public void testCancel() {
    FlowTaskInstance flowTaskInstance = Mockito.mock(FlowTaskInstance.class);
    BaseRuntimeFlowableDelegate<?> target = new BaseRuntimeFlowableDelegate<>();
    Mockito.when(flowTaskInstance.isPresentOnThisMachine()).thenReturn(true);
    Mockito.when(flowTaskInstance.getTargetTaskHandle()).thenReturn(target);
    
    flowTaskInstance.cancel(true);
    
    Mockito.verify(target).cancel(true);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testCancelTaskNotOnMachine() {
    FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
    
    flowTaskInstance.cancel(true);
  }
  
  @Test
  public void testIsDone() {
    FlowTaskInstance flowTaskInstance = Mockito.mock(FlowTaskInstance.class);
    BaseRuntimeFlowableDelegate<?> target = new BaseRuntimeFlowableDelegate<>();
    Mockito.when(flowTaskInstance.isPresentOnThisMachine()).thenReturn(true);
    Mockito.when(flowTaskInstance.getTargetTaskHandle()).thenReturn(target);
    Mockito.when(target.isDone()).thenReturn(true);
    
    assertTrue(flowTaskInstance.isDone());
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testIsDoneTaskNotOnMachine() {
    FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
    
    flowTaskInstance.isDone();
  }
  
  @Test
  public void testGetResult() throws Exception {
    FlowTaskInstance flowTaskInstance = Mockito.mock(FlowTaskInstance.class);
    BaseRuntimeFlowableDelegate<?> target = new BaseRuntimeFlowableDelegate<>();
    Object expectedResult = new Object();
    Mockito.when(flowTaskInstance.isPresentOnThisMachine()).thenReturn(true);
    Mockito.when(flowTaskInstance.getTargetTaskHandle()).thenReturn(target);
    Mockito.when(target.get()).thenReturn(expectedResult);
    
    Object result = flowTaskInstance.getResult();
    
    assertEquals(expectedResult, result);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetResultTaskNotOnMachine() throws Exception {
    FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
    
    flowTaskInstance.getResult();
  }
  
  @Test
  public void testGetResultWithTimeout() throws Exception {
    FlowTaskInstance flowTaskInstance = Mockito.mock(FlowTaskInstance.class);
    BaseRuntimeFlowableDelegate<?> target = new BaseRuntimeFlowableDelegate<>();
    Object expectedResult = new Object();
    long timeout = 1;
    TimeUnit unit = TimeUnit.SECONDS;
    Mockito.when(flowTaskInstance.isPresentOnThisMachine()).thenReturn(true);
    Mockito.when(flowTaskInstance.getTargetTaskHandle()).thenReturn(target);
    Mockito.when(target.get(timeout, unit)).thenReturn(expectedResult);
    
    Object result = flowTaskInstance.getResult(timeout, unit);
    
    assertEquals(expectedResult, result);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetResultWithTimeoutTaskNotOnMachine() throws Exception {
    FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
    
    flowTaskInstance.getResult(1, TimeUnit.SECONDS);
  }
}
