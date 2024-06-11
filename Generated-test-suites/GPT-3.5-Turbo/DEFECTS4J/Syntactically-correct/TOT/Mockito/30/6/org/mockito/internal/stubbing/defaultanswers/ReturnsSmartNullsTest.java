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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

    @Test
    public void answer_ordinaryReturnValue() throws Throwable {
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        ReturnsSmartNulls answer = new ReturnsSmartNulls(delegate);

        Object returnValue = answer.answer(mock(InvocationOnMock.class));

        assertNotNull(returnValue);
    }

    @Test
    public void answer_nullReturnValue() throws Throwable {
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        when(delegate.answer(any(InvocationOnMock.class))).thenReturn(null);
        ReturnsSmartNulls answer = new ReturnsSmartNulls(delegate);

        Object returnValue = answer.answer(mock(InvocationOnMock.class));

        assertNotNull(returnValue);
    }

    @Test
    public void answer_impersonateReturnValue() throws Throwable {
        ClassImposterizer imposterizer = mock(ClassImposterizer.class);
        Object returnValue = new Object();
        when(imposterizer.canImposterise(any(Class.class))).thenReturn(true);
        when(imposterizer.imposterise(any(MethodInterceptor.class), any(Class.class))).thenReturn(returnValue);
        ReturnsSmartNulls answer = new ReturnsSmartNulls();

        Object result = answer.answer(mock(InvocationOnMock.class));

        assertEquals(returnValue, result);
    }

    @Test
    public void throwingInterceptor_intercept_toString() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        ReturnsSmartNulls.ThrowingInterceptor interceptor = new ReturnsSmartNulls().new ThrowingInterceptor(invocation);

        Object result = interceptor.intercept(mock(Object.class), Object.class.getMethod("toString"), new Object[]{}, mock(MethodProxy.class));

        assertEquals("SmartNull returned by unstubbed toString method on mock", result);
    }

    @Test(expected = Reporter.smartNullPointerException.class)
    public void throwingInterceptor_intercept_otherMethods() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("hashCode"));
        Location location = mock(Location.class);
        whenNew(Location.class).withNoArguments().thenReturn(location);
        ReturnsSmartNulls.ThrowingInterceptor interceptor = new ReturnsSmartNulls().new ThrowingInterceptor(invocation);

        interceptor.intercept(mock(Object.class), Object.class.getMethod("hashCode"), new Object[]{}, mock(MethodProxy.class));
    }

}