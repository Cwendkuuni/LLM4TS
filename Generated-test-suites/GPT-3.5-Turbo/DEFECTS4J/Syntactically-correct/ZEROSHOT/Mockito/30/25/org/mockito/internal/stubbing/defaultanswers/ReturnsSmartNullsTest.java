import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.stubbing.defaultanswers.ReturnsMoreEmptyValues;
import org.mockito.internal.util.ObjectMethodsGuru;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = returnsSmartNulls.answer(invocation);
        assertEquals(null, result);
    }

    @Test
    public void testThrowingInterceptorIntercept() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Location location = new Location();
        ThrowingInterceptor throwingInterceptor = returnsSmartNulls.new ThrowingInterceptor(invocation);
        Method method = Object.class.getMethod("hashCode");
        Object[] args = {};
        Object result = throwingInterceptor.intercept(null, method, args, null);
        assertEquals("SmartNull returned by unstubbed hashCode() method on mock", result);
    }

    @Test
    public void testThrowingInterceptorInterceptWithToStringMethod() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Location location = new Location();
        ThrowingInterceptor throwingInterceptor = returnsSmartNulls.new ThrowingInterceptor(invocation);
        Method method = Object.class.getMethod("toString");
        Object[] args = {};
        Object result = throwingInterceptor.intercept(null, method, args, null);
        assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
    }

    @Test
    public void testFormatMethodCall() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Location location = new Location();
        ThrowingInterceptor throwingInterceptor = returnsSmartNulls.new ThrowingInterceptor(invocation);
        Method method = Object.class.getMethod("hashCode");
        Object[] args = {1, "test"};
        String result = throwingInterceptor.formatMethodCall();
        assertEquals("hashCode(1, \"test\")", result);
    }

    @Test
    public void testAnswerWithDefaultReturnValue() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        Mockito.when(delegate.answer(Mockito.any(InvocationOnMock.class))).thenReturn("default");
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = returnsSmartNulls.answer(invocation);
        assertEquals("default", result);
    }

    @Test
    public void testAnswerWithClassImposterizer() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        Mockito.when(delegate.answer(Mockito.any(InvocationOnMock.class))).thenReturn(null);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("hashCode");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(method.getReturnType()).thenReturn(Object.class);
        ClassImposterizer classImposterizer = mock(ClassImposterizer.class);
        Mockito.when(classImposterizer.canImposterise(Mockito.eq(Object.class))).thenReturn(true);
        Mockito.when(classImposterizer.imposterise(Mockito.any(ReturnsSmartNulls.ThrowingInterceptor.class), Mockito.eq(Object.class))).thenReturn("mocked");
        Object result = returnsSmartNulls.answer(invocation);
        assertEquals("mocked", result);
    }

    @Test
    public void testAnswerWithClassImposterizerNotMockable() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        Mockito.when(delegate.answer(Mockito.any(InvocationOnMock.class))).thenReturn(null);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("hashCode");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(method.getReturnType()).thenReturn(Object.class);
        ClassImposterizer classImposterizer = mock(ClassImposterizer.class);
        Mockito.when(classImposterizer.canImposterise(Mockito.eq(Object.class))).thenReturn(false);
        Object result = returnsSmartNulls.answer(invocation);
        assertEquals(null, result);
    }
}