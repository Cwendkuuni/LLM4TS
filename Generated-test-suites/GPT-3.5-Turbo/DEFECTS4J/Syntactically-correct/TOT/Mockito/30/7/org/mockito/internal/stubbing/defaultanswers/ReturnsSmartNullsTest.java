import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_withNonNullReturnValue() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(String.class);

        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object result = returnsSmartNulls.answer(invocation);

        assertNull("Expected null return value", result);
    }

    @Test
    public void testAnswer_withNullReturnValue() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Object.class);

        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object result = returnsSmartNulls.answer(invocation);

        assertNull("Expected null return value", result);
    }

    @Test
    public void testAnswer_withMockableReturnType() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(MyClass.class);

        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object result = returnsSmartNulls.answer(invocation);

        assertNotNull("Expected non-null return value", result);
        assertTrue("Expected instance of MyClass", result instanceof MyClass);
    }

    @Test
    public void testAnswer_withNonMockableReturnType() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(finalClass.class);

        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object result = returnsSmartNulls.answer(invocation);

        assertNull("Expected null return value", result);
    }

    @Test
    public void testAnswer_withToStringMethod() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(String.class);
        when(invocation.getMethod().getName()).thenReturn("toString");
        when(invocation.getArguments()).thenReturn(new Object[] {});

        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object result = returnsSmartNulls.answer(invocation);

        assertNotNull("Expected non-null return value", result);
        assertEquals("Expected 'SmartNull returned by unstubbed toString method on mock'", result.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testAnswer_withUnmockableMethod() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenThrow(new NullPointerException());

        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        returnsSmartNulls.answer(invocation);
    }

}