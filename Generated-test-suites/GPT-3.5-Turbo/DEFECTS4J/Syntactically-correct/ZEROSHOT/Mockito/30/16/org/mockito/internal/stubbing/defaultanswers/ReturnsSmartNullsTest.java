import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class ReturnsSmartNullsTest {
  
  @Test
  public void testAnswer() throws Throwable {
    ReturnsMoreEmptyValues delegate = new ReturnsMoreEmptyValues();
    ReturnsSmartNulls answer = new ReturnsSmartNulls();
    
    InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
    Mockito.when(delegate.answer(invocation1)).thenReturn("Mockito");
    Object result1 = answer.answer(invocation1);
    Assert.assertEquals("Mockito", result1);
    
    InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
    Mockito.when(delegate.answer(invocation2)).thenReturn(null);
    Mockito.when(invocation2.getMethod()).thenReturn(getClass().getMethod("testAnswer"));
    Object result2 = answer.answer(invocation2);
    Assert.assertNull(result2);
    
    InvocationOnMock invocation3 = Mockito.mock(InvocationOnMock.class);
    Mockito.when(delegate.answer(invocation3)).thenReturn("SmartNull returned by unstubbed testAnswer method on mock");
    Object result3 = answer.answer(invocation3);
    Assert.assertEquals("SmartNull returned by unstubbed testAnswer method on mock", result3);
    
    InvocationOnMock invocation4 = Mockito.mock(InvocationOnMock.class);
    Mockito.when(delegate.answer(invocation4)).thenReturn(null);
    Mockito.when(invocation4.getMethod()).thenReturn(getClass().getMethod("toString"));
    Object result4 = answer.answer(invocation4);
    Assert.assertEquals("SmartNull returned by unstubbed toString method on mock", result4);
    
    InvocationOnMock invocation5 = Mockito.mock(InvocationOnMock.class);
    Mockito.when(delegate.answer(invocation5)).thenReturn(null);
    Mockito.when(invocation5.getMethod()).thenReturn(getClass().getMethod("intercept", Object.class, Method.class, Object[].class, MethodProxy.class));
    Object result5 = answer.answer(invocation5);
    Assert.assertNull(result5);
    
    InvocationOnMock invocation6 = Mockito.mock(InvocationOnMock.class);
    Mockito.when(delegate.answer(invocation6)).thenReturn(null);
    Mockito.when(invocation6.getMethod()).thenReturn(getClass().getMethod("formatMethodCall"));
    Object result6 = answer.answer(invocation6);
    Assert.assertEquals("SmartNull returned by unstubbed formatMethodCall method on mock", result6);
  }
  
}