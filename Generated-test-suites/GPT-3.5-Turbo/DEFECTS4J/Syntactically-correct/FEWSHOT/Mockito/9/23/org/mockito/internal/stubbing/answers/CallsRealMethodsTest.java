package org.mockito.internal.stubbing.answers;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

public class CallsRealMethodsTest {
    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Test call to real method
        Object result = new CallsRealMethods().answer(invocation);
        Assert.assertNull(result); // Assert that result is null
    }
}