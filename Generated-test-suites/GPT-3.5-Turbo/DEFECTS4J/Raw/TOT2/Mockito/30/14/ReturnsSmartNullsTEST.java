import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_WhenDefaultReturnIsNull_ReturnsSmartNull() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        Object smartNull = returnsSmartNulls.answer(invocation);
        assertNotNull(smartNull);
    }

    @Test
    public void testAnswer_WhenDefaultReturnIsNotNull_ReturnsDefaultReturn() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object returnValue = new Object();
        when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        when(returnsSmartNulls.delegate.answer(invocation)).thenReturn(returnValue);
        Object result = returnsSmartNulls.answer(invocation);
        assertEquals(returnValue, result);
    }

    @Test
    public void testAnswer_WhenReturnTypeCannotBeImposterized_ReturnsNull() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Void.class);
        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }

    @Test
    public void testAnswer_WhenMethodIsToString_ReturnsSmartNullMessage() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getName()).thenReturn("toString");
        when(invocation.getArguments()).thenReturn(new Object[] {});
        Object result = returnsSmartNulls.answer(invocation);
        assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
    }

}