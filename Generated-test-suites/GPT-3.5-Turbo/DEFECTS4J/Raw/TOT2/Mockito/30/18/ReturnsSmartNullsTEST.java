import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.stubbing.defaultanswers.ReturnsMoreEmptyValues;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_defaultReturnValueNotNull() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> delegate = mock(ReturnsMoreEmptyValues.class);
        when(delegate.answer(invocation)).thenReturn("Test Value");
        
        Object result = returnsSmartNulls.answer(invocation);
        
        assertEquals("Test Value", result);
    }

    @Test
    public void testAnswer_defaultReturnValueNull() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> delegate = mock(ReturnsMoreEmptyValues.class);
        when(delegate.answer(invocation)).thenReturn(null);
        when(invocation.getMethod()).thenReturn(ReturnsSmartNulls.class.getMethod("toString"));
        
        Object result = returnsSmartNulls.answer(invocation);
        
        assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
    }

    @Test
    public void testAnswer_imposteriseType() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = ReturnsSmartNulls.class.getMethod("toString");
        when(invocation.getMethod()).thenReturn(method);
        when(ClassImposterizer.INSTANCE.canImposterise(String.class)).thenReturn(true);

        Object result = returnsSmartNulls.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof MethodInterceptor);
    }

    @Test
    public void testIntercept_throwNullPointerException() throws Throwable {
        ReturnsSmartNulls.ThrowingInterceptor interceptor = new ReturnsSmartNulls().new ThrowingInterceptor(mock(InvocationOnMock.class));
        
        try {
            interceptor.intercept(null, null, null, null);
            fail("Expected exception was not thrown");
        } catch (Throwable e) {
            assertTrue(e instanceof NullPointerException);
        }
    }

}