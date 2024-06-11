package org.mockito.internal.stubbing.defaultanswers.test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    @Test
    public void testReturnsSmartNullsInstance() {
        Assert.assertNotNull(new ReturnsSmartNulls());
    }

    @Test
    public void testAnswerMethod() throws Throwable {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(Object.class);
        Object result = answer.answer(invocation);
        Assert.assertNull(result);
    }

    @Test
    public void testIsToString() {
        Assert.assertTrue(new ReturnsSmartNulls().isToString(new Method(Object.class.getDeclaredMethod("toString"))));
    }

    @Test
    public void testFormatMethodCall() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Assert.assertEquals("answer()", returnsSmartNulls.formatMethodCall());
    }

    @Test
    public void testAnswerInterface() throws Throwable {
        InvocationOnMock mock = mock(Object.class);
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        when(mock.getInterfaceMethod()).thenCallRealMethod();
        when(mock.getInterfaceMethod()).thenAnswer(answer);
        Assert.assertEquals("SmartNull returned by unstubbed getInterfaceMethod() method on mock", answer.answer(argThat(i -> i.getClass().isInterface())));
    }

    @Test
    public void testAnswerArray() throws Throwable {
        InvocationOnMock mock = mock(Object[].class);
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        when(mock.getArrayMethod()).thenCallRealMethod();
        when(mock.getArrayMethod()).thenAnswer(answer);
        Assert.assertArrayEquals(new Object[0], answer.answer(argThat(a -> a.getClass().isArray())));
    }

    @Test(expected = Throwable.class)
    public void testAnswerCheckedException() throws Throwable {
        InvocationOnMock mock = mock(Throwable.class);
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        when(mock.getCheckedExceptionMethod()).thenCallRealMethod();
        when(mock.getCheckedExceptionMethod()).thenAnswer(answer);
        answer.answer(argThat(i -> i.getClass() == Throwable.class));
    }

    @Test
    public void testAnswerPrimitiveTypes() throws Throwable {
        InvocationOnMock mock = mock(int[].class);
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        when(mock.getPrimitiveTypeMethod()).thenCallRealMethod();
        when(mock.getPrimitiveTypeMethod()).thenAnswer(answer);
        Assert.assertNull(answer.answer(argThat(i -> i.getClass().isPrimitive())));
    }
}