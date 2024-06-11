import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.lang.reflect.Method;
import org.junit.Before;
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
  
  private ReturnsSmartNulls smartNulls;
  
  @Before
  public void setUp() {
    smartNulls = new ReturnsSmartNulls();
  }
  
  @Test
  public void testAnswer_NullDefaultReturnValue_ReturnsSmartNull() throws Throwable {
    // Arrange
    Method method = ReturnsSmartNulls.class.getDeclaredMethod("answer", InvocationOnMock.class);
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod()).thenReturn(method);
    
    // Act
    Object result = smartNulls.answer(invocation);
    
    // Assert
    assertNotNull(result);
    assertTrue(result instanceof String);
    assertEquals("SmartNull returned by unstubbed answer() method on mock", result);
  }
  
  @Test
  public void testAnswer_DefaultReturnValueNotNull_ReturnDefaultReturnValue() throws Throwable {
    // Arrange
    Method method = ReturnsSmartNulls.class.getDeclaredMethod("answer", InvocationOnMock.class);
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod()).thenReturn(method);
    
    Answer<Object> delegate = mock(Answer.class);
    when(delegate.answer(invocation)).thenReturn("DefaultReturnValue");
    smartNulls.delegate = delegate;
    
    // Act
    Object result = smartNulls.answer(invocation);
    
    // Assert
    assertNotNull(result);
    assertTrue(result instanceof String);
    assertEquals("DefaultReturnValue", result);
  }
  
  @Test
  public void testAnswer_DefaultReturnValueNull_ReturnImposteriseObject() throws Throwable {
    // Arrange
    Method method = ReturnsSmartNulls.class.getDeclaredMethod("answer", InvocationOnMock.class);
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod()).thenReturn(method);
    
    Answer<Object> delegate = mock(Answer.class);
    when(delegate.answer(invocation)).thenReturn(null);
    smartNulls.delegate = delegate;
    
    Class<?> type = Object.class;
    ClassImposterizer imposterizer = mock(ClassImposterizer.class);
    when(ClassImposterizer.INSTANCE.canImposterise(type)).thenReturn(true);
    when(imposterizer.imposterise(any(MethodInterceptor.class), eq(type))).thenReturn("ImposteriseObject");
    
    // Act
    Object result = smartNulls.answer(invocation);
    
    // Assert
    assertNotNull(result);
    assertEquals("ImposteriseObject", result);
  }
  
  @Test(expected = Throwable.class)
  public void testAnswer_ExceptionInMethodInvocation_ThrowsThrowable() throws Throwable {
    // Arrange
    Method method = ReturnsSmartNulls.class.getDeclaredMethod("answer", InvocationOnMock.class);
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod()).thenReturn(method);
    
    Answer<Object> delegate = mock(Answer.class);
    when(delegate.answer(invocation)).thenThrow(new Throwable());
    smartNulls.delegate = delegate;
    
    // Act
    Object result = smartNulls.answer(invocation);
  }
  
}