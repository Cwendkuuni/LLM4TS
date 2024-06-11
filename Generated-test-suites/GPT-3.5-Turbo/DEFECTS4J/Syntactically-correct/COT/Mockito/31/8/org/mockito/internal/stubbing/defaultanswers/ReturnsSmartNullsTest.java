
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

    private ReturnsSmartNulls returnsSmartNulls;

    @Before
    public void setup() {
        returnsSmartNulls = new ReturnsSmartNulls();
    }

    @Test
    public void testAnswer_returnsNonNull() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(String.class);

        Object result = returnsSmartNulls.answer(invocation);

        assertNotNull(result);
    }

    @Test
    public void testAnswer_returnsSmartNull() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Integer.class);

        Object result = returnsSmartNulls.answer(invocation);

        assertNotNull(result);
        assertTrue(result.toString().startsWith("SmartNull returned by unstubbed"));
    }

    @Test
    public void testAnswer_returnsNull() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(TestClass.class);

        Object result = returnsSmartNulls.answer(invocation);

        assertNull(result);
    }
}

class TestClass {

}
