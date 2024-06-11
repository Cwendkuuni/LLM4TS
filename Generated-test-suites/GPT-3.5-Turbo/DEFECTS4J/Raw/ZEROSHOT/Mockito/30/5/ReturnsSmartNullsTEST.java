import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswerReturnsSmartNull() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        Object result = returnsSmartNulls.answer(mock(InvocationOnMock.class));

        assertEquals("SmartNull returned by unstubbed method on mock", result);
    }

    @Test
    public void testAnswerReturnsNullForNonMockableType() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = ReturnsSmartNulls.class.getDeclaredMethod("formatMethodCall");
        when(invocation.getMethod()).thenReturn(method);

        Object result = returnsSmartNulls.answer(invocation);

        assertEquals(null, result);
    }
}