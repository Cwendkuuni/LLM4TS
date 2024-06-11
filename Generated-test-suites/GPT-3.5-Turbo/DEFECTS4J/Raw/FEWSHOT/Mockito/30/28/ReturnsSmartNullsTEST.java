
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {
    private ReturnsSmartNulls returnsSmartNulls;
    private InvocationOnMock invocation;
    
    @Before
    public void setup() {
        returnsSmartNulls = new ReturnsSmartNulls();
        invocation = mock(InvocationOnMock.class);
    }
    
    @Test
    public void testAnswer_withDefaultReturnValue() throws Throwable {
        when(invocation.getMethod().getReturnType()).thenReturn(String.class);
        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }
    
    @Test
    public void testAnswer_withMockableReturnType() throws Throwable {
        when(invocation.getMethod().getReturnType()).thenReturn(Foo.class);
        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }
    
    @Test
    public void testAnswer_withNonMockableReturnType() throws Throwable {
        when(invocation.getMethod().getReturnType()).thenReturn(Integer.class);
        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }
    
    @Test
    public void testAnswer_interceptToString() throws Throwable {
        when(invocation.getMethod().getReturnType()).thenReturn(String.class);
        when(invocation.getMethod().getName()).thenReturn("toString");
        when(invocation.getArguments()).thenReturn(new Object[] {});
        Object result = returnsSmartNulls.answer(invocation);
        assertEquals("SmartNull returned by unstubbed toString method on mock", result);
    }
}
