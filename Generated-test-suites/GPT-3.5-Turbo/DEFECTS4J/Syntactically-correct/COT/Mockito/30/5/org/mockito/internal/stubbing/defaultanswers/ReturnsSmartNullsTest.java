import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.mockito.internal.creation.jmock.ClassImposterizer.INSTANCE;
import static org.mockito.internal.util.ObjectMethodsGuru.isToString;
import static org.mockito.internal.util.ObjectMethodsGuru.smartNullPointerException;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_defaultReturnValue() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object result = returnsSmartNulls.answer(invocation);
        assert result == null;
    }

    @Test
    public void testAnswer_returnMockable() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = this.getClass().getMethod("testAnswer_returnMockable");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(INSTANCE.canImposterise(method.getReturnType())).thenReturn(true);
        Object result = returnsSmartNulls.answer(invocation);
        assert result instanceof ThrowingInterceptor;
    }

    @Test
    public void testAnswer_returnNonMockable() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = this.getClass().getMethod("testAnswer_returnNonMockable");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(INSTANCE.canImposterise(method.getReturnType())).thenReturn(false);
        Object result = returnsSmartNulls.answer(invocation);
        assert result == null;
    }

    @Test
    public void testThrowingInterceptor_intercept_toString() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("toString");
        Mockito.when(new ObjectMethodsGuru().isToString(method)).thenReturn(true);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        returnsSmartNulls.new ThrowingInterceptor(invocation).intercept(null, method, null, null);
        Mockito.verify(new ObjectMethodsGuru()).isToString(method);
    }

    @Test
    public void testThrowingInterceptor_intercept_nonToString() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = this.getClass().getMethod("testThrowingInterceptor_intercept_nonToString");
        Mockito.when(new ObjectMethodsGuru().isToString(method)).thenReturn(false);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(new Reporter().smartNullPointerException(invocation.getMethod())).thenReturn(null);
        returnsSmartNulls.new ThrowingInterceptor(invocation).intercept(null, method, null, null);
        Mockito.verify(new Reporter()).smartNullPointerException(invocation.getMethod());
    }

    @Test
    public void testThrowingInterceptor_formatMethodCall() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = this.getClass().getMethod("testThrowingInterceptor_formatMethodCall");
        Mockito.when(invocation.getArguments()).thenReturn(new Object[]{1, "test", true});
        Mockito.when(invocation.getMethod()).thenReturn(method);
        String result = returnsSmartNulls.new ThrowingInterceptor(invocation).formatMethodCall();
        assert result.equals("testThrowingInterceptor_formatMethodCall(1, test, true)");
    }

}