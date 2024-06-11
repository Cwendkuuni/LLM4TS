import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_withUnstubbedMethodCall_returnsSmartNull() {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        InvocationOnMock.Method method = mock(InvocationOnMock.Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn("unstubbedMethod");
        
        Object result = answer.answer(invocation);

        assertNotNull(result);
        assertEquals("SmartNull returned by unstubbed unstubbedMethod() method on mock", result);
    }

    @Test
    public void testAnswer_withUnmockableReturnType_returnsNull() throws Throwable {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();

        InvocationOnMock invocation = mockInvocationOnMock();
        when(invocation.getMethod().getReturnType()).thenReturn(UnmockableClass.class);
        
        Object result = answer.answer(invocation);

        assertNull(result);
    }

    @Test
    public void testAnswer_withImposterizableReturnType_returnsImposterizedInstance() throws Throwable {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();

        InvocationOnMock invocation = mockInvocationOnMock();
        when(invocation.getMethod().getReturnType()).thenReturn(MockableClass.class);
        when(ClassImposterizer.INSTANCE.canImposterise(MockableClass.class)).thenReturn(true);
        
        Object result = answer.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof MockableClass);
    }

    private InvocationOnMock mockInvocationOnMock() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        InvocationOnMock.Method method = mock(InvocationOnMock.Method.class);
        when(invocation.getMethod()).thenReturn(method);

        return invocation;
    }

    private static class UnmockableClass {
        // implementation details not relevant for testing
    }

    private static class MockableClass {
        // implementation details not relevant for testing
    }

}