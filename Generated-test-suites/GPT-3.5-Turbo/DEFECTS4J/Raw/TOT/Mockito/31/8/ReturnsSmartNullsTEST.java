import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ReturnsSmartNullsTest {

    // Test answer method

    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        // Test when delegate answer returns non-null value
        Answer<Object> delegate = mock(Answer.class);
        Object returnValue = new Object();
        when(delegate.answer(invocation)).thenReturn(returnValue);

        Object result = answer.answer(invocation);

        assertEquals(returnValue, result);

        // Test when delegate answer returns null
        when(delegate.answer(invocation)).thenReturn(null);

        // Test when imposterize succeeds
        Class<?> mockReturnType = mock(Class.class);
        when(mockReturnType.getReturnType()).thenReturn(Object.class);
        when(ClassImposterizer.INSTANCE.canImposterise(mockReturnType)).thenReturn(true);

        result = answer.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof ReturnsSmartNulls.ThrowingInterceptor);

        // Test when imposterize fails
        when(ClassImposterizer.INSTANCE.canImposterise(mockReturnType)).thenReturn(false);

        result = answer.answer(invocation);

        assertNull(result);
    }

    // Test ThrowingInterceptor class

    @Test
    public void testThrowingInterceptor() throws Throwable {
        ReturnsSmartNulls.SmartNullThrowsException interceptor = new ReturnsSmartNulls.SmartNullThrowsException();
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        // Test when method is toString
        Method toStringMethod = Object.class.getDeclaredMethod("toString");
        Object result = interceptor.intercept(null, toStringMethod, null, null);

        assertEquals("SmartNull returned by unstubbed toString() method on mock", result);

        // Test when method is not toString
        Method otherMethod = Object.class.getDeclaredMethod("hashCode");
        try {
            interceptor.intercept(null, otherMethod, null, null);
            fail("Exception not thrown when intercepting non-toString method");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("smartNullPointerException"));
        }
    }

}