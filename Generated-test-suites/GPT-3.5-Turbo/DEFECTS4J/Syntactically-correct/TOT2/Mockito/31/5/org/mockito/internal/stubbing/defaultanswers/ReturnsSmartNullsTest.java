import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(String.class);

        Object result = returnsSmartNulls.answer(invocation);

        assertNotNull(result);
    }

    @Test
    public void testAnswerWithNullReturnType() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(null);

        Object result = returnsSmartNulls.answer(invocation);

        assertNull(result);
    }

    @Test
    public void testAnswerWithFinalType() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(ThrowsException.class);

        Object result = returnsSmartNulls.answer(invocation);

        assertNull(result);
    }
}