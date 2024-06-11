import static org.junit.Assert.*;
import org.junit.Test;

public class ReturnsSmartNullsTest {
  
  @Test
  public void testAnswer() {
    ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
    
    // Test mocking method invocation that returns null
    InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
    assertNull(returnsSmartNulls.answer(invocation1));
    
    // Test mocking method invocation that returns non-null value
    InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
    Mockito.when(invocation2.getMethod().getReturnType()).thenReturn(String.class);
    assertEquals("", returnsSmartNulls.answer(invocation2));
    
    // Test mocking method invocation that returns a mockable object
    InvocationOnMock invocation3 = Mockito.mock(InvocationOnMock.class);
    Mockito.when(invocation3.getMethod().getReturnType()).thenReturn(ReturnsSmartNulls.class);
    assertNotNull(returnsSmartNulls.answer(invocation3));
    
    // Test mocking method invocation that returns a non-mockable object
    InvocationOnMock invocation4 = Mockito.mock(InvocationOnMock.class);
    Mockito.when(invocation4.getMethod().getReturnType()).thenReturn(Long.class);
    assertNull(returnsSmartNulls.answer(invocation4));
  }
  
  @Test
  public void testThrowingInterceptor() throws Throwable {
    ReturnsSmartNulls.ThrowsInterceptor interceptor = new ReturnsSmartNulls.ThrowsInterceptor(null);
    
    // Test intercepting toString method and returning smart null message
    Method toStringMethod = Object.class.getDeclaredMethod("toString");
    Object result1 = interceptor.intercept(null, toStringMethod, new Object[]{}, null);
    assertEquals("SmartNull returned by unstubbed toString method on mock", result1);
    
    // Test intercepting other method and throwing smart null exception
    Method otherMethod = Object.class.getDeclaredMethod("hashCode");
    try {
      interceptor.intercept(null, otherMethod, new Object[]{}, null);
      fail("Expected smartNullPointerException");
    } catch (NullPointerException e) {
      assertEquals("Smart NullPointerException", e.getMessage());
    }
  }
  
  @Test
  public void testFormatMethodCall() {
    ReturnsSmartNulls.ThrowsInterceptor interceptor = new ReturnsSmartNulls.ThrowsInterceptor(null);
    
    // Test formatting method call with arguments
    InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
    Mockito.when(invocation1.getArguments()).thenReturn(new Object[]{1, 2, 3});
    Mockito.when(invocation1.getMethod()).thenReturn(ReturnsSmartNulls.class.getDeclaredMethod("method", int.class, int.class, int.class));
    String result1 = interceptor.formatMethodCall(invocation1);
    assertEquals("method(1, 2, 3)", result1);
    
    // Test formatting method call without arguments
    InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
    Mockito.when(invocation2.getArguments()).thenReturn(new Object[]{});
    Mockito.when(invocation2.getMethod()).thenReturn(ReturnsSmartNulls.class.getDeclaredMethod("method"));
    String result2 = interceptor.formatMethodCall(invocation2);
    assertEquals("method()", result2);
  }
}