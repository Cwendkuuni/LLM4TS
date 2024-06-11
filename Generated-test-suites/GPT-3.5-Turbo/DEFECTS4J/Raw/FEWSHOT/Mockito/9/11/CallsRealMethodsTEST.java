package org.mockito.internal.stubbing.answers;

import static org.mockito.Mockito.*;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class CallsRealMethodsTest {
    @Test
    public void testAnswer() throws Throwable {
        // Create a mock object
        CallsRealMethods mock = mock(CallsRealMethods.class);

        // Create an invocation on the mock object
        InvocationOnMock invocation = (InvocationOnMock) when(mock.answer(any())).thenAnswer(new CallsRealMethods()).getMock();

        // Test the answer method
        Object result = mock.answer(invocation);

        // Verify the result
        assertNotNull(result);
    }
}