import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.stubbing.defaultanswers.ReturnsMoreEmptyValues;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocationOnMock = Mockito.mock(InvocationOnMock.class);

        Object result = returnsSmartNulls.answer(invocationOnMock);

        Assert.assertNull(result);
    }

    @Test
    public void testThrowingInterceptorInterceptWithToStringMethod() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocationOnMock = Mockito.mock(InvocationOnMock.class);
        Method method = String.class.getMethod("toString", new Class[]{});

        ThrowingInterceptor throwingInterceptor = returnsSmartNulls.new ThrowingInterceptor(invocationOnMock);
        Object result = throwingInterceptor.intercept(null, method, null, null);

        Assert.assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
    }

    @Test
    public void testThrowingInterceptorInterceptWithNonToStringMethod() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocationOnMock = Mockito.mock(InvocationOnMock.class);
        Method method = String.class.getMethod("length", new Class[]{});

        ThrowingInterceptor throwingInterceptor = returnsSmartNulls.new ThrowingInterceptor(invocationOnMock);
        Object result = throwingInterceptor.intercept(null, method, null, null);

        Assert.assertNull(result);
        Mockito.verify(new Reporter()).smartNullPointerException(null);
    }

    @Test
    public void testAnswerWithDefaultReturnValueNotNull() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocationOnMock = Mockito.mock(InvocationOnMock.class);
        ReturnsMoreEmptyValues delegate = Mockito.mock(ReturnsMoreEmptyValues.class);
        Mockito.when(delegate.answer(invocationOnMock)).thenReturn("test");

        Object result = returnsSmartNulls.answer(invocationOnMock);

        Assert.assertEquals("test", result);
    }

    @Test
    public void testAnswerWithDefaultReturnValueNull() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocationOnMock = Mockito.mock(InvocationOnMock.class);
        ReturnsMoreEmptyValues delegate = Mockito.mock(ReturnsMoreEmptyValues.class);
        Mockito.when(delegate.answer(invocationOnMock)).thenReturn(null);

        Object result = returnsSmartNulls.answer(invocationOnMock);

        Assert.assertNull(result);
    }

    @Test
    public void testAnswerWithNonMockableReturnType() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocationOnMock = Mockito.mock(InvocationOnMock.class);
        ReturnsMoreEmptyValues delegate = Mockito.mock(ReturnsMoreEmptyValues.class);
        Mockito.when(delegate.answer(invocationOnMock)).thenReturn(null);

        ClassImposterizer imposterizer = Mockito.mock(ClassImposterizer.class);
        Mockito.when(imposterizer.canImposterise(Object.class)).thenReturn(false);
        
        Object result = returnsSmartNulls.answer(invocationOnMock);

        Assert.assertNull(result);
    }

}