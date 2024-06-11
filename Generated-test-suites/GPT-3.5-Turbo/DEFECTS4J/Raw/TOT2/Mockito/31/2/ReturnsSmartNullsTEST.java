import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswerReturnsSmartNulls() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(String.class);
        Object result = returnsSmartNulls.answer(invocation);

        assertEquals("SmartNull returned by unstubbed method on mock", result);
    }
    
}