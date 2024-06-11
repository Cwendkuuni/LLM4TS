import org.junit.Before;
import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CallsRealMethodsTest {

    private CallsRealMethods callsRealMethods;
    private Object realObject;

    @Before
    public void setup() {
        realObject = mock(Object.class);
        callsRealMethods = new CallsRealMethods();
    }

    @Test
    public void testCallsRealMethod() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(realObject);
        when(invocation.getMethod()).thenReturn(realObject.getClass().getMethod("methodWithReturnType"));

        Object result = callsRealMethods.answer(invocation);
        verify(realObject, Mockito.times(1)).methodWithReturnType();
        Assert.assertEquals(result, realObject.methodWithReturnType());
    }

    @Test
    public void testCallsRealMethodWithNullArgument() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(realObject);
        when(invocation.getMethod()).thenReturn(realObject.getClass().getMethod("methodWithNullArgument", String.class));

        try {
            callsRealMethods.answer(invocation);
            fail("Expected IllegalArgException not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
            Assert.assertEquals("null", e.getMessage());
        }

        verifyZeroInteractions(realObject);
    }

    @Test
    public void testCallsRealMethodWithException() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(realObject);
        when(invocation.getMethod()).thenReturn(realObject.getClass().getMethod("methodWithException", String.class));

        doThrow(RuntimeException.class).when(realObject).methodWithException("test");

        try {
            callsRealMethods.answer(invocation);
            fail("Expected exception not thrown");
        } catch (RuntimeException e) {
            // Expected exception
            Assert.assertEquals("test", e.getMessage());
        }

        verify(realObject, Mockito.times(1)).methodWithException("test");
    }
}