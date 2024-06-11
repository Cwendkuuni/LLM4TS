import org.junit.Test;
import static org.mockito.Mockito.*;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        //Test null default return value
        when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        Object result = answer.answer(invocation);
        assertNull(result);

        //Test smartnull default return value
        when(invocation.getMethod().getReturnType()).thenReturn(String.class);
        result = answer.answer(invocation);
        assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
    }
}