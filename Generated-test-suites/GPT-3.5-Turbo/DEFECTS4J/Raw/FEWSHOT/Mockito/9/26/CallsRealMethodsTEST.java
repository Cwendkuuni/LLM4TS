package org.mockito.internal.stubbing.answers;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CallsRealMethodsTest {

    @Test
    public void testAnswer() {
        CallsRealMethods callsRealMethods = new CallsRealMethods();

        // Test case 1: Test when invocation.callRealMethod() returns null
        InvocationOnMock invocation1 = createInvocationMock(null);
        Object result1 = callsRealMethods.answer(invocation1);
        assertEquals(null, result1);

        // Test case 2: Test when invocation.callRealMethod() returns a string
        String expectedResult = "mockito";
        InvocationOnMock invocation2 = createInvocationMock(expectedResult);
        Object result2 = callsRealMethods.answer(invocation2);
        assertEquals(expectedResult, result2);

        // Test case 3: Test when invocation.callRealMethod() returns an integer
        int expectedIntResult = 5;
        InvocationOnMock invocation3 = createInvocationMock(expectedIntResult);
        Object result3 = callsRealMethods.answer(invocation3);
        assertEquals(expectedIntResult, result3);

        // Additional Test cases can be added for other possible return types
    }

    private InvocationOnMock createInvocationMock(Object returnValue) {
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(invocationMock.callRealMethod()).thenReturn(returnValue);
        return invocationMock;
    }
}