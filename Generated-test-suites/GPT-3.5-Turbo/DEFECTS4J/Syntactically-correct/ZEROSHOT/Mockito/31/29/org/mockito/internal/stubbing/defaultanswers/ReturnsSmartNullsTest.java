import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.*;
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
    public void testAnswer_WhenDefaultReturnValueIsNull_ThenReturnsNull() throws Throwable {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object defaultReturnValue = null;
        Object result = answer.answer(invocation);
        assertEquals(defaultReturnValue, result);
    }

    @Test
    public void testAnswer_WhenDefaultReturnValueIsNotNull_ThenReturnsDefaultReturnValue() throws Throwable {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object defaultReturnValue = new Object();
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(defaultReturnValue.getClass());
        Object result = answer.answer(invocation);
        assertEquals(defaultReturnValue, result);
    }

    @Test
    public void testAnswer_WhenReturnTypeIsMockable_ThenReturnsSmartNull() throws Throwable {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object defaultReturnValue = null;
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        Object result = answer.answer(invocation);
        assertNotNull(result);
    }

    @Test
    public void testAnswer_WhenReturnTypeIsNotMockable_ThenReturnsNull() throws Throwable {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object defaultReturnValue = null;
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(Void.class);
        Object result = answer.answer(invocation);
        assertNull(result);
    }

    @Test
    public void testAnswer_WhenToStringMethodCalled_ThenReturnsSmartNullWithMethodCallMessage() throws Throwable {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object defaultReturnValue = null;
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsSmartNulls.class.getMethod("toString"));
        Object result = answer.answer(invocation);
        assertNotNull(result);
        assertEquals("SmartNull returned by unstubbed toString() method on mock", result.toString());
    }

    @Test(expected = Reporter.SmartNullPointerException.class)
    public void testAnswer_WhenMethodCalled_ThenThrowsSmartNullPointerException() throws Throwable {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object defaultReturnValue = null;
        new ObjectMethodsGuru().isToString(ReturnsSmartNulls.class.getMethod("equals"));
        Object result = answer.answer(invocation);
    }

    @Test
    public void testThrowingInterceptorIntercept_WhenToStringMethodCalled_ThenReturnsSmartNullWithMethodCallMessage() throws Throwable {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        MethodProxy methodProxy = Mockito.mock(MethodProxy.class);
        Object[] args = new Object[0];
        Method method = ReturnsSmartNulls.class.getMethod("toString");
        Object result = answer.new ThrowingInterceptor(invocation).intercept(null, method, args, methodProxy);
        assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
    }

    @Test(expected = Reporter.SmartNullPointerException.class)
    public void testThrowingInterceptorIntercept_WhenMethodCalled_ThenThrowsSmartNullPointerException() throws Throwable {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        MethodProxy methodProxy = Mockito.mock(MethodProxy.class);
        Object[] args = new Object[0];
        Method method = ReturnsSmartNulls.class.getMethod("equals");
        Object result = answer.new ThrowingInterceptor(invocation).intercept(null, method, args, methodProxy);
    }

    @Test
    public void testFormatMethodCall_ThenReturnsFormattedMethodCallMessage() {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsSmartNulls.class.getMethod("equals"));
        String expected = "equals()";
        String result = answer.new ThrowingInterceptor(invocation).formatMethodCall();
        assertEquals(expected, result);
    }
}