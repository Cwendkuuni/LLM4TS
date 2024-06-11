import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {
  
  @Test
  public void testAnswer() throws Throwable {
    ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
    
    // Test default return values
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod().getReturnType()).thenReturn(String.class);
    Object defaultReturnValue = smartNulls.answer(invocation);
    assertNull(defaultReturnValue);
    
    // Test mockable return type
    invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod().getReturnType()).thenReturn(TestObject.class);
    TestObject mockObject = (TestObject)smartNulls.answer(invocation);
    assertNotNull(mockObject);
    
    // Test normal object method
    invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod().getReturnType()).thenReturn(TestObject.class);
    defaultReturnValue = smartNulls.answer(invocation);
    assertNull(defaultReturnValue);
    
    // Test toString method
    invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod().getReturnType()).thenReturn(String.class);
    defaultReturnValue = smartNulls.answer(invocation);
    assertNotNull(defaultReturnValue);
    assertEquals("SmartNull returned by unstubbed toString() method on mock", defaultReturnValue);
  }
  
  static class TestObject {}
}