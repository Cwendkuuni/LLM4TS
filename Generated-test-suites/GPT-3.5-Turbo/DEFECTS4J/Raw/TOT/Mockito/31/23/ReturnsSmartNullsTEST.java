import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswerReturnsDefaultValues() {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(String.class);

        Object result = smartNulls.answer(invocation);

        assertNull(result);
    }

    @Test
    public void testAnswerReturnsSmartNulls() {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        when(new ObjectMethodsGuru().isToString(any())).thenReturn(true);

        Object result = smartNulls.answer(invocation);

        assertEquals("SmartNull returned by unstubbed method on mock", result);
    }

    @Test
    public void testAnswerReturnsNullForUnmockableTypes() {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Integer.class);
        when(ClassImposterizer.INSTANCE.canImposterise(Integer.class)).thenReturn(false);

        Object result = smartNulls.answer(invocation);

        assertNull(result);
    }
}