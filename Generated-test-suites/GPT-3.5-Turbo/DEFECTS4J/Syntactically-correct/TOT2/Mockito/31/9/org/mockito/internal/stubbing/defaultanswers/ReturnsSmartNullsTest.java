import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_returnsSmartNulls() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = returnsSmartNulls.answer(invocation);
        assertNotNull(result);
        assertTrue(result instanceof Object);
    }

    @Test
    public void testAnswer_returnsSmartNulls_forImposterizableType() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        ClassImposterizer imposterizer = mock(ClassImposterizer.class);
        when(imposterizer.canImposterise(Object.class)).thenReturn(true);
        Object result = returnsSmartNulls.answer(invocation);
        assertNotNull(result);
        assertTrue(result instanceof MethodInterceptor);
    }

    @Test
    public void testAnswer_returnsSmartNulls_forNullDefaultReturnValue() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> delegate = mock(Answer.class);
        when(delegate.answer(invocation)).thenReturn(null);
        ReturnsSmartNulls returnsSmartNullsSpy = spy(returnsSmartNulls);
        doReturn(delegate).when(returnsSmartNullsSpy).getDelegate();
        Object result = returnsSmartNullsSpy.answer(invocation);
        assertNull(result);
    }

    @Test(expected = MockitoException.class)
    public void testAnswer_throwsException_forImposterizationFailure() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        ClassImposterizer imposterizer = mock(ClassImposterizer.class);
        when(imposterizer.canImposterise(Object.class)).thenReturn(false);
        returnsSmartNulls.answer(invocation);
    }

    @Test
    public void testThrowingInterceptor_interceptsMethodCall() throws Throwable {
        ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = new ReturnsSmartNulls().new ThrowingInterceptor(mock(InvocationOnMock.class));
        Object result = throwingInterceptor.intercept(null, Object.class.getMethod("toString"), null, null);
        assertNotNull(result);
        assertTrue(result instanceof String);
    }

    @Test
    public void testThrowingInterceptor_throwsSmartNullPointerException() {
        ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = new ReturnsSmartNulls().new ThrowingInterceptor(mock(InvocationOnMock.class));
        try {
            throwingInterceptor.intercept(null, Object.class.getMethod("hashCode"), null, null);
            fail("Expected an exception");
        } catch (Throwable e) {
            assertTrue(e instanceof MockitoException);
        }
    }
}