
import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_withNullDefaultReturnValue() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(null);

        Object result = returnsSmartNulls.answer(invocation);

        assertNull(result);
    }

    @Test
    public void testAnswer_withPrimitiveReturnType() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.getReturnType()).thenReturn(int.class);

        Object result = returnsSmartNulls.answer(invocation);

        assertEquals(0, result);
        assertTrue(result instanceof Integer);
    }

    @Test
    public void testAnswer_withFinalReturnType() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.getReturnType()).thenReturn(FinalClass.class);

        Object result = returnsSmartNulls.answer(invocation);

        assertNull(result);
    }

    @Test
    public void testAnswer_withToStringMethod() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn("toString");
        when(new ObjectMethodsGuru().isToString(method)).thenReturn(true);

        Object result = returnsSmartNulls.answer(invocation);

        assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
    }

    @Test(expected = NullPointerException.class)
    public void testAnswer_withUnmockableReturnTypeAndNoDefaultReturnValue() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.getReturnType()).thenReturn(UnmockableClass.class);
        when(ClassImposterizer.INSTANCE.canImposterise(UnmockableClass.class)).thenReturn(false);

        returnsSmartNulls.answer(invocation);
    }

    private static class FinalClass {
        // Final class
    }

    private static class UnmockableClass {
        // Unmockable class
    }
}

