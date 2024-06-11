import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.exceptions.Reporter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {
  
  @Test
  public void testAnswer() throws Throwable {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
    when(delegate.answer(invocation)).thenReturn(null);
    
    ReturnsSmartNulls rs = new ReturnsSmartNulls();
    Object result = rs.answer(invocation);
    assertNull(result);
    verify(delegate).answer(invocation);
  }
  
  @Test
  public void testToStringMethod() throws Throwable {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));

    ThrowingInterceptor interceptor = new ThrowingInterceptor(invocation);
    Object result = interceptor.intercept(null, null, null, null);
    assertNotNull(result);
    assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
  }
  
  @Test
  public void testImposteriseMethod() throws Throwable {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod()).thenReturn(Object.class.getMethod("hashCode"));
    when(ClassImposterizer.INSTANCE.canImposterise(any(Class.class))).thenReturn(true);
    
    ThrowingInterceptor interceptor = new ThrowingInterceptor(invocation);
    Object result = interceptor.intercept(null, null, null, null);
    assertNotNull(result);
    assertTrue(result instanceof ThrowingInterceptor);
    assertNotNull(((ThrowingInterceptor) result).location);
    verify(ClassImposterizer.INSTANCE).canImposterise(any(Class.class));
  }
  
  @Test
  public void testImposteriseMethodCannotImposterise() throws Throwable {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod()).thenReturn(Object.class.getMethod("hashCode"));
    when(ClassImposterizer.INSTANCE.canImposterise(any(Class.class))).thenReturn(false);
    
    ThrowingInterceptor interceptor = new ThrowingInterceptor(invocation);
    Object result = interceptor.intercept(null, null, null, null);
    assertNull(result);
    verify(ClassImposterizer.INSTANCE).canImposterise(any(Class.class));
  }
  
  @Test(expected = NullPointerException.class)
  public void testSmartNullPointerException() throws Throwable {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    ThrowingInterceptor interceptor = new ThrowingInterceptor(invocation);
    interceptor.intercept(null, null, null, null);
  }
}