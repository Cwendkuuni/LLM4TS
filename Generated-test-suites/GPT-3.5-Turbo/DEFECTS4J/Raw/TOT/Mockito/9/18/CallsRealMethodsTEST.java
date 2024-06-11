import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CallsRealMethodsTest {

    private CallsRealMethods callsRealMethods;

    @Before
    public void setUp() {
        callsRealMethods = new CallsRealMethods();
    }

    @Test
    public void answer_invocation_callsRealMethod() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Set up the expectations
        when(invocation.callRealMethod()).thenReturn("result");

        // Call the method under test
        Object result = callsRealMethods.answer(invocation);

        // Verify the result
        assertEquals("result", result);
    }

    @Test
    public void answer_invocation_callsRealMethod_multipleTimes() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Set up the expectations
        when(invocation.callRealMethod()).thenReturn("result1").thenReturn("result2");

        // Call the method under test
        Object result1 = callsRealMethods.answer(invocation);
        Object result2 = callsRealMethods.answer(invocation);

        // Verify the results
        assertEquals("result1", result1);
        assertEquals("result2", result2);
    }

    @Test(expected = Throwable.class)
    public void answer_invocation_throwsException() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Set up the expectations
        when(invocation.callRealMethod()).thenThrow(new Throwable("test exception"));

        // Call the method under test
        callsRealMethods.answer(invocation);
    }

    @Test
    public void answer_invocation_callsRealMethod_withDifferentArguments() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Set up the expectations
        when(invocation.callRealMethod("arg1", 2)).thenReturn("result");

        // Call the method under test
        Object result = callsRealMethods.answer(invocation, "arg1", 2);

        // Verify the result
        assertEquals("result", result);
    }
}