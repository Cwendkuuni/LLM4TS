package org.mockito.internal.stubbing.answers.test;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CallsRealMethodsTest {

    @Test
    public void testAnswerRealMethod() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        Object target = mock(Answer.class, callRealMethods);

        Object result = target.answer(Mockito.mock(InvocationOnMock.class));
        verify(target).callRealMethod();
        assertNotNull(result);
    }

    @Test(expected = Throwable.class)
    public void testAnswerRealMethodWithException() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        Object target = mock(Answer.class, callRealMethods);

        // Assuming callRealMethod() method throws an exception
        Mockito.doThrow(new RuntimeException("Test Exception")).when(target).callRealMethod();
        Object result = target.answer(Mockito.mock(InvocationOnMock.class));
    }

    @Test
    public void testNullArgument() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        Object target = mock(Answer.class, callRealMethods);

        ArgumentCaptor<InvocationOnMock> invocationCaptor = ArgumentCaptor.forClass(InvocationOnMock.class);
        Mockito.verify(target).callRealMethod(invocationCaptor.capture());
        InvocationOnMock invocation = invocationCaptor.getValue();
        assertNull(invocation.getArgument(0));
    }

    @Test
    public void testEmptyArrayArgument() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        Object target = mock(Answer.class, callRealMethods);

        ArgumentCaptor<InvocationOnMock> invocationCaptor = ArgumentCaptor.forClass(InvocationOnMock.class);
        Mockito.verify(target).callRealMethod(invocationCaptor.capture());
        InvocationOnMock invocation = invocationCaptor.getValue();
        Object[] emptyArray = invocation.getArgument(0);
        assertArrayEquals(new Object[0], emptyArray);
    }

    @Test
    public void testArrayWithOneElementArgument() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        Object target = mock(Answer.class, callRealMethods);

        ArgumentCaptor<InvocationOnMock> invocationCaptor = ArgumentCaptor.forClass(InvocationOnMock.class);
        Object[] arrayWithOneElement = {new Object()};
        Mockito.verify(target).callRealMethod(invocationCaptor.capture());
        InvocationOnMock invocation = invocationCaptor.getValue();
        Object[] arguments = invocation.getArgument(0);
        assertEquals(arrayWithOneElement.length, arguments.length);
        assertEquals(arrayWithOneElement[0], arguments[0]);
    }

    @Test
    public void testNullReturnType() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        Object target = mock(Answer.class, callRealMethods);

        ArgumentCaptor<InvocationOnMock> invocationCaptor = ArgumentCaptor.forClass(InvocationOnMock.class);
        Mockito.verify(target).callRealMethod(invocationCaptor.capture());
        InvocationOnMock invocation = invocationCaptor.getValue();
        assertNull(invocation.getReturnValue());
    }

    @Test
    public void testPrimitiveArrayReturnType() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        Object target = mock(Answer.class, callRealMethods);

        ArgumentCaptor<InvocationOnMock> invocationCaptor = ArgumentCaptor.forClass(InvocationOnMock.class);
        int[] primitiveArray = {1, 2, 3};
        Mockito.verify(target).callRealMethod(invocationCaptor.capture());
        InvocationOnMock invocation = invocationCaptor.getValue();
        int[] arguments = (int[]) invocation.getArgument(0);
        int[] expected = {1, 2, 3};
        assertArrayEquals(expected, arguments);
        int result = ((int[]) invocation.getReturnValues()[0])[0];
        assertEquals(expected[0], result);
    }

    @Test
    public void testCustomObjectReturnType() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        Object target = mock(Answer.class, callRealMethods);

        ArgumentCaptor<InvocationOnMock> invocationCaptor = ArgumentCaptor.forClass(InvocationOnMock.class);
        MyCustomObject customObject = new MyCustomObject();
        Mockito.verify(target).callRealMethod(invocationCaptor.capture());
        InvocationOnMock invocation = invocationCaptor.getValue();
        MyCustomObject arguments = (MyCustomObject) invocation.getArgument(0);
        MyCustomObject expected = customObject;
        assertEquals(expected, arguments);
        MyCustomObject result = (MyCustomObject) invocation.getReturnValues()[0];
        assertEquals(expected, result);
    }

    @Test
    public void testNullReturnValues() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        Object target = mock(Answer.class, callRealMethods);

        ArgumentCaptor<InvocationOnMock> invocationCaptor = ArgumentCaptor.forClass(InvocationOnMock.class);
        Mockito.verify(target).callRealMethod(invocationCaptor.capture());
        InvocationOnMock invocation = invocationCaptor.getValue();
        assertArrayEquals(new Object[0], invocation.getReturnValues());
    }

    @Test
    public void testMultipleReturnValues() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        Object target = mock(Answer.class, callRealMethods);

        ArgumentCaptor<InvocationOnMock> invocationCaptor = ArgumentCaptor.forClass(InvocationOnMock.class);
        Object[] arguments = {new Object()};
        Object[] expectedReturnValues = {"Expected Value 1", "Expected Value 2"};
        Mockito.verify(target).callRealMethod(invocationCaptor.capture());
        InvocationOnMock invocation = invocationCaptor.getValue();
        Object[] argumentsCaptured = (Object[]) invocation.getArgument(0);
        Object[] returnValues = invocation.getReturnValues();
        assertArrayEquals(arguments, argumentsCaptured);
        assertArrayEquals(expectedReturnValues, returnValues);
    }
}