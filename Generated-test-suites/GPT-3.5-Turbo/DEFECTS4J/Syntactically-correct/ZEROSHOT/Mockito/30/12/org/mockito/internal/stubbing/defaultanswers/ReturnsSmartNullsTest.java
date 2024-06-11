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

import java.lang.reflect.Method;
import java.util.Arrays;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(delegate.answer(invocation)).thenReturn(null);

        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        Object result = answer.answer(invocation);

        assertNull(result);
        verify(delegate).answer(invocation);
    }

    @Test
    public void testThrowingInterceptor() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        MethodProxy proxy = mock(MethodProxy.class);
        ThrowingInterceptor interceptor = new ReturnsSmartNulls().new ThrowingInterceptor(invocation);
        Method method = getClass().getDeclaredMethod("testThrowingInterceptor");
        when(invocation.getMethod()).thenReturn(method);
        when(proxy.invokeSuper(any(), any())).thenThrow(new Throwable("test exception"));

        Object result = interceptor.intercept(this, method, null, proxy);

        assertNull(result);
        verify(invocation).getMethod();
        verify(invocation).getArguments();
        verify(proxy).invokeSuper(this, null);
    }

    @Test
    public void testAnswerWhenDefaultReturnValueNotNull() throws Throwable {
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(delegate.answer(invocation)).thenReturn("default");

        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        Object result = answer.answer(invocation);

        assertEquals("default", result);
        verify(delegate).answer(invocation);
    }

    @Test
    public void testAnswerWhenImposterizableReturnType() throws Throwable {
        ClassImposterizer imposterizer = mock(ClassImposterizer.class);
        MethodProxy proxy = mock(MethodProxy.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(getClass().getDeclaredMethod("testAnswerWhenImposterizableReturnType"));
        when(ClassImposterizer.INSTANCE.canImposterise(any(Class.class))).thenReturn(true);
        when(ClassImposterizer.INSTANCE.imposterise(any(MethodInterceptor.class), any(Class.class))).thenReturn(this);
        when(invocation.getArguments()).thenReturn(new Object[]{});

        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        Object result = answer.answer(invocation);

        assertSame(this, result);
        verify(ClassImposterizer.INSTANCE).canImposterise(getClass());
        verify(ClassImposterizer.INSTANCE).imposterise(any(MethodInterceptor.class), any(Class.class));
        verify(invocation).getMethod();
        verify(invocation).getArguments();
    }
}