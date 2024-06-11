import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ReturnsSmartNullsTest {
  private ReturnsSmartNulls returnsSmartNulls;

  @Before
  public void setUp() {
    returnsSmartNulls = new ReturnsSmartNulls();
  }

  @Test
  public void testAnswerMethod() throws Throwable {
    InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
    Object result = returnsSmartNulls.answer(invocation);
    assertNull("Expected null result", result);
  }

  @Test
  public void testThrowingInterceptorMethods() throws Throwable {
    InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
    ReturnsSmartNulls.ThrowingInterceptor interceptor = returnsSmartNulls.new ThrowingInterceptor(invocation);
    Method method = interceptor.getClass().getMethod("intercept", Object.class, Method.class, Object[].class, MethodProxy.class);
    assertNotNull("Expected intercepted method", method);

    Object[] args = new Object[0];
    MethodProxy proxy = Mockito.mock(MethodProxy.class);

    Object result = interceptor.intercept(new Object(), method, args, proxy);
    assertNull("Expected null result", result);
  }

  @Test
  public void testThrowsInterceptorToStringMethod() throws Throwable {
    InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
    ReturnsSmartNulls.ThrowingInterceptor interceptor = returnsSmartNulls.new ThrowingInterceptor(invocation);
    Method method = interceptor.getClass().getMethod("intercept", Object.class, Method.class, Object[].class, MethodProxy.class);

    Object[] args = new Object[0];
    MethodProxy proxy = Mockito.mock(MethodProxy.class);

    ObjectMethodsGuru objectMethodsGuru = Mockito.mock(ObjectMethodsGuru.class);
    Mockito.when(objectMethodsGuru.isToString(method)).thenReturn(true);
    Report report = Mockito.mock(Report.class);
    Mockito.when(report.smartNullPointerException(Mockito.any(Location.class))).thenReturn(null);
    Reporter reporter = Mockito.mock(Reporter.class);
    Mockito.when(reporter.smartNullPointerException(Mockito.any(Location.class))).thenReturn(null);

    Object result = interceptor.intercept(new Object(), method, args, proxy);
    assertNotNull("Expected non-null result", result);
    assertEquals("Expected formatted method call", "SmartNull returned by unstubbed method on mock", result);
  }

  @Test
  public void testThrowsInterceptorFormatMethodCallMethod() throws Throwable {
    InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
    ReturnsSmartNulls.ThrowingInterceptor interceptor = returnsSmartNulls.new ThrowingInterceptor(invocation);
    Method method = interceptor.getClass().getMethod("formatMethodCall");
    assertNotNull("Expected formatMethodCall method", method);

    Object result = method.invoke(interceptor);
    assertNotNull("Expected non-null result", result);
    assertEquals("Expected formatted method call", "()" ,result);
  }

  @Test
  public void testAnswerDefaultReturnValue() throws Throwable {
    InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
    Answer<Object> delegate = Mockito.mock(Answer.class);
    ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls(delegate);
    Mockito.when(delegate.answer(invocation)).thenReturn(null);

    Object result = returnsSmartNulls.answer(invocation);
    assertNull("Expected null result", result);
  }

  @Test
  public void testAnswerMockableType() throws Throwable {
    InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
    InvocationOnMock methodInvocation = Mockito.mock(InvocationOnMock.class);
    Answer<Object> delegate = Mockito.mock(Answer.class);
    Method method = methodInvocation.getClass().getMethod("getMethod");
    Mockito.when(methodInvocation.getMethod()).thenReturn(method);
    Mockito.when(invocation.getMethod()).thenReturn(method);
    ClassImposterizer classImposterizer = Mockito.mock(ClassImposterizer.class);
    Mockito.when(classImposterizer.canImposterise(Mockito.any(Class.class))).thenReturn(true);
    ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls(delegate);

    Object result = returnsSmartNulls.answer(invocation);
    assertNotNull("Expected non-null result", result);
  }

  @Test
  public void testAnswerNonMockableType() throws Throwable {
    InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
    InvocationOnMock methodInvocation = Mockito.mock(InvocationOnMock.class);
    Answer<Object> delegate = Mockito.mock(Answer.class);
    Method method = methodInvocation.getClass().getMethod("getMethod");
    Mockito.when(methodInvocation.getMethod()).thenReturn(method);
    Mockito.when(invocation.getMethod()).thenReturn(method);
    ClassImposterizer classImposterizer = Mockito.mock(ClassImposterizer.class);
    Mockito.when(classImposterizer.canImposterise(Mockito.any(Class.class))).thenReturn(false);
    ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls(delegate);

    Object result = returnsSmartNulls.answer(invocation);
    assertNull("Expected null result", result);
  }
}