/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.*;
import org.junit.Test;

public class CallsRealMethodsTest {
  
    @Test
    public void testAnswer() {
        CallsRealMethods answer = new CallsRealMethods();

        // Test case 1: Test with Integer return type
        InvocationOnMock invocation1 = createInvocationMock(Integer.class);
        Object result1 = answer.answer(invocation1);
        assertEquals(0, result1);

        // Test case 2: Test with String return type
        InvocationOnMock invocation2 = createInvocationMock(String.class);
        Object result2 = answer.answer(invocation2);
        assertNull(result2);

        // Test case 3: Test with custom object return type
        CustomObject expectedResult = new CustomObject();
        InvocationOnMock invocation3 = createInvocationMock(CustomObject.class);
        Object result3 = answer.answer(invocation3);
        assertEquals(expectedResult, result3);
    }

    private InvocationOnMock createInvocationMock(Class<?> returnType) {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(CustomObject.class.getMethod("customMethod"));
        when(invocation.getMock()).thenReturn(new CustomObject());
        when(invocation.callRealMethod()).thenCallRealMethod();
        when(invocation.getReturnType()).thenReturn(returnType);
        return invocation;
    }

    private class CustomObject {
        public int customMethod() {
            return 0;
        }
    }
}