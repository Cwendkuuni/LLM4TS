import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.exceptions.Reporter;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswerOrdinaryReturnValue() throws Throwable {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(String.class);
        when(invocation.getMethod().isAnnotationPresent(Override.class)).thenReturn(true);

        Object result = smartNulls.answer(invocation);

        assertNotNull(result);
        assertEquals(String.class, result.getClass());
    }

    @Test
    public void testAnswerMockableReturnValue() throws Throwable {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Integer.class);
        when(invocation.getMethod().isAnnotationPresent(Override.class)).thenReturn(true);

        Object result = smartNulls.answer(invocation);

        assertNotNull(result);
        assertEquals(Integer.class, result.getClass());
    }

    @Test
    public void testAnswerFinalReturnValue() throws Throwable {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Void.class);
        when(invocation.getMethod().isAnnotationPresent(Override.class)).thenReturn(true);

        Object result = smartNulls.answer(invocation);

        assertNull(result);
    }

    @Test(expected = Reporter.class)
    public void testAnswerNonMockableReturnValue() throws Throwable {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Reporter.class);
        when(invocation.getMethod().isAnnotationPresent(Override.class)).thenReturn(true);

        smartNulls.answer(invocation);
    }
}