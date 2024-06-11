import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_WhenDefaultReturnValueIsNull_ThenReturnSmartNull() throws Throwable {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        when(invocation.getMethod().getName()).thenReturn("testMethod");
        when(invocation.getArguments()).thenReturn(new Object[]{});

        Object answer = smartNulls.answer(invocation);

        assertNotNull(answer);
    }

    @Test(expected = NullPointerException.class)
    public void testAnswer_WhenDefaultReturnValueIsNotNull_ThenReturnDefaultReturnValue() throws Throwable {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Object.class);

        Object defaultReturnValue = new Object();
        when(smartNulls.answer(invocation)).thenReturn(defaultReturnValue);

        Object answer = smartNulls.answer(invocation);

        assertSame(defaultReturnValue, answer);
    }

    @Test
    public void testAnswer_WhenReturnTypeIsNotMockable_ThenReturnNull() throws Throwable {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Void.class);

        Object answer = smartNulls.answer(invocation);

        assertNull(answer);
    }

    @Test
    public void testAnswer_WhenCallingToStringMethod_ThenReturnFormattedSmartNullString() throws Throwable {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        when(invocation.getMethod().getName()).thenReturn("toString");
        when(invocation.getArguments()).thenReturn(new Object[]{});

        Object answer = smartNulls.answer(invocation);

        assertEquals("SmartNull returned by unstubbed toString() method on mock", answer);
    }

}