/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.Serializable;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CallsRealMethodsTest {
    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        CallsRealMethods answer = new CallsRealMethods();
        Object result = answer.answer(invocation);
        
        verify(invocation, times(1)).callRealMethod();
        // Add assertions for the result if needed
        // assertEquals(expectedResult, result);
    }
}