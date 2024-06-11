import static org.mockito.Mockito.*;
import org.junit.Test;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {
  
  @Test
  public void testAnswer_OtherReturnValues_ReturnsReturnValue() throws Throwable {
    ReturnsMoreEmptyValues delegateMock = mock(ReturnsMoreEmptyValues.class);
    ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
    InvocationOnMock invocationMock = mock(InvocationOnMock.class);
    when(delegateMock.answer(invocationMock)).thenReturn("returnValue");
    
    Object result = returnsSmartNulls.answer(invocationMock);
    
    verify(delegateMock).answer(invocationMock);
    assertEquals("returnValue", result);
  }
  
  @Test
  public void testAnswer_NullReturnValues_ReturnsSmartNull() throws Throwable {
    ReturnsMoreEmptyValues delegateMock = mock(ReturnsMoreEmptyValues.class);
    ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
    InvocationOnMock invocationMock = mock(InvocationOnMock.class);
    when(delegateMock.answer(invocationMock)).thenReturn(null);
    Method methodMock = mock(Method.class);
    when(invocationMock.getMethod()).thenReturn(methodMock);
    when(methodMock.getReturnType()).thenReturn(Object.class);
    ClassImposterizer classImposterizerMock = mock(ClassImposterizer.class);
    when(ClassImposterizer.INSTANCE.canImposterise(Object.class)).thenReturn(true);
    when(ClassImposterizer.INSTANCE.imposterise(any(MethodInterceptor.class), eq(Object.class))).thenReturn("SmartNull");
    
    Object result = returnsSmartNulls.answer(invocationMock);
    
    verify(delegateMock).answer(invocationMock);
    verify(invocationMock).getMethod();
    verify(methodMock).getReturnType();
    verify(ClassImposterizer.INSTANCE).canImposterise(Object.class);
    verify(ClassImposterizer.INSTANCE).imposterise(any(MethodInterceptor.class), eq(Object.class));
    assertEquals("SmartNull", result);
  }
  
  @Test
  public void testAnswer_FinalClass_ReturnsNull() throws Throwable {
    ReturnsMoreEmptyValues delegateMock = mock(ReturnsMoreEmptyValues.class);
    ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
    InvocationOnMock invocationMock = mock(InvocationOnMock.class);
    when(delegateMock.answer(invocationMock)).thenReturn(null);
    Method methodMock = mock(Method.class);
    when(invocationMock.getMethod()).thenReturn(methodMock);
    when(methodMock.getReturnType()).thenReturn(Object.class);
    ClassImposterizer classImposterizerMock = mock(ClassImposterizer.class);
    when(ClassImposterizer.INSTANCE.canImposterise(Object.class)).thenReturn(false);
    
    Object result = returnsSmartNulls.answer(invocationMock);
    
    verify(delegateMock).answer(invocationMock);
    verify(invocationMock).getMethod();
    verify(methodMock).getReturnType();
    verify(ClassImposterizer.INSTANCE).canImposterise(Object.class);
    assertEquals(null, result);
  }
  
}