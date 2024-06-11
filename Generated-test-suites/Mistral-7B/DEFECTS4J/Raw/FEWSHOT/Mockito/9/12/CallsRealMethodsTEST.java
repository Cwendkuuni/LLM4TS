package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class CallsRealMethodsTest {

    private Object mockedObject;

    @Before
    public void setup() {
        mockedObject = mock(Object.class);
    }

    @Test
    public void testAnswerReturnsRealMethodInvocation() {
        CallsRealMethods answer = new CallsRealMethods();
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        Object result = answer.answer(invocation);
        assertNotNull(result);
        verify(mockedObject).callRealMethod();
    }

    @Test
    public void testAnswerReturnsRealMethodInvocationWithArgument() {
        CallsRealMethods answer = new CallsRealMethods();
        Object[] arguments = new Object[1];
        arguments[0] = "test";
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.getArguments()[0] = arguments[0];

        Object result = answer.answer(invocation);
        assertNotNull(result);
        verify(mockedObject).callRealMethod(arguments[0]);
    }

    @Test
    public void testAnswerReturnsRealMethodInvocationWithMultipleArguments() {
        CallsRealMethods answer = new CallsRealMethods();
        Object[] arguments = new Object[2];
        arguments[0] = "test1";
        arguments[1] = 1;
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.getArguments()[0] = arguments[0];
        invocation.getArguments()[1] = arguments[1];

        Object result = answer.answer(invocation);
        assertNotNull(result);
        verify(mockedObject).callRealMethod(arguments[0], arguments[1]);
    }

    @Test
    public void testAnswerReturnsRealMethodInvocationWithNoArguments() {
        CallsRealMethods answer = new CallsRealMethods();
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        Object result = answer.answer(invocation);
        assertNotNull(result);
        verify(mockedObject).callRealMethod();
        verify(mockedObject).callRealMethod(new Object[0]);
    }
}