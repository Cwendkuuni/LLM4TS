import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {
    @Test
    public void testAnswer() throws Throwable {
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        // Test case 1: defaultReturnValue is not null
        when(delegate.answer(invocation)).thenReturn("abc");
        Object result1 = returnsSmartNulls.answer(invocation);
        assertEquals("abc", result1);

        // Test case 2: defaultReturnValue is null, type can be imposterise
        when(delegate.answer(invocation)).thenReturn(null);
        when(invocation.getMethod().getReturnType()).thenReturn(String.class);
        Object result2 = returnsSmartNulls.answer(invocation);
        // Add assertion for imposterise

        // Test case 3: defaultReturnValue is null, type cannot be imposterise
        when(delegate.answer(invocation)).thenReturn(null);
        when(invocation.getMethod().getReturnType()).thenReturn(Integer.class);
        Object result3 = returnsSmartNulls.answer(invocation);
        assertNull(result3);
    }
}