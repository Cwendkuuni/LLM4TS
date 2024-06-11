package org.mockito.internal.stubbing.answers.test;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.io.Serializable;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CallsRealMethodsTest {

    @Test
    public void testAnswer_returnsInvokedMethodResult() throws Throwable {
        // Given
        CallsRealMethods answer = new CallsRealMethods();
        Object target = mock(Answer.class);
        Object expectedResult = new Object() {};
        Object input = new Object() {};
        InvocationOnMock invocation = Mockito.mockInvocationOnMock(target, new Object[] { input });

        // When
        Object result = answer.answer(invocation);

        // Then
        verify(target).callRealMethod();
        assertEquals(expectedResult, result);
    }

    @Test(expected = Throwable.class)
    public void testAnswer_throwsThrownException() throws Throwable {
        // Given
        CallsRealMethods answer = new CallsRealMethods();
        Object target = mock(Answer.class);
        Object input = new Object() {};
        InvocationOnMock invocation = Mockito.mockInvocationOnMock(target, new Object[] { input });
        Throwable expectedException = new Throwable();
        doThrow(expectedException).when(target).callRealMethod();

        // When
        answer.answer(invocation);

        // Then
        verify(target).callRealMethod();
        throw expectedException;
    }

    @Test
    public void testAnswer_delegatesToRealMethod_whenInvokedWithNullArgument() throws Throwable {
        // Given
        CallsRealMethods answer = new CallsRealMethods();
        Object target = mock(Answer.class);
        Object input = null;
        InvocationOnMock invocation = Mockito.mockInvocationOnMock(target, new Object[] { input });

        // When
        Object result = answer.answer(invocation);

        // Then
        verify(target).callRealMethod();
        assertEquals(null, result);
    }

    @Test
    public void testAnswer_delegatesToRealMethod_whenInvokedWithEmptyArrayArgument() throws Throwable {
        // Given
        CallsRealMethods answer = new CallsRealMethods();
        Object target = mock(Answer.class);
        Object[] emptyArray = new Object[0];
        InvocationOnMock invocation = Mockito.mockInvocationOnMock(target, new Object[] { emptyArray });

        // When
        Object result = answer.answer(invocation);

        // Then
        verify(target).callRealMethod();
        assertEquals(null, result);
    }

    @Test
    public void testAnswer_delegatesToRealMethod_whenInvokedWithMultipleArguments() throws Throwable {
        // Given
        CallsRealMethods answer = new CallsRealMethods();
        Object target = mock(Answer.class);
        Object arg1 = new Object() {};
        Object arg2 = new Object() {};
        Object[] arguments = new Object[] { arg1, arg2 };
        InvocationOnMock invocation = Mockito.mockInvocationOnMock(target, arguments);

        // When
        Object result = answer.answer(invocation);

        // Then
        ArgumentCaptor<?> argCaptor = ArgumentCaptor.forClass(Object.class);
        verify(target).callRealMethod(argCaptor.capture());
        assertEquals(arg1, argCaptor.getValue());
        assertEquals(arg2, invocation.getArguments()[1]);
        assertEquals(result, invocation.getArguments()[0]);
    }

    @Test
    public void testAnswer_delegatesToRealMethod_whenInvokedWithVarArgs() throws Throwable {
        // Given
        CallsRealMethods answer = new CallsRealMethods();
        Object target = mock(Answer.class);
        Object arg1 = new Object() {};
        Object arg2 = new Object() {};
        Object[] args1 = new Object[] { arg1, arg2 };
        Object[] args2 = new Object[] { arg1 };
        InvocationOnMock invocation1 = Mockito.mockInvocationOnMock(target, args1);
        InvocationOnMock invocation2 = Mockito.mockInvocationOnMock(target, args2);

        // When
        Object result1 = answer.answer(invocation1);
        Object result2 = answer.answer(invocation2);

        // Then
        verify(target).callRealMethod(arg1, arg2);
        verify(target, times(1)).callRealMethod(arg1);
        assertEquals(result1, invocation1.getArguments()[0]);
        assertEquals(result2, invocation2.getArguments()[0]);
    }

}