package org.mockito.internal.stubbing.answers;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class CallsRealMethodsTest {

    // Test case for answer method
    @Test
    public void testAnswerMethod() throws Throwable {
        // Create a mock InvocationOnMock object
        InvocationOnMock mockInvocation = mock(InvocationOnMock.class);
        
        // Create an object of CallsRealMethods
        CallsRealMethods callsRealMethods = new CallsRealMethods();
        
        // Mock the invocation to return a value
        when(mockInvocation.callRealMethod()).thenReturn("Test value");
        
        // Assert that the answer method returns the expected value
        assertEquals("Test value", callsRealMethods.answer(mockInvocation));
        
        // Verify that the callRealMethod method was called
        verify(mockInvocation).callRealMethod();
    }
}