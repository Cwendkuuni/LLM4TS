package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswerNull() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(Object.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setMethod(new Method(String.class.getName(), new Class[0]));

        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }

    @Test
    public void testAnswerInstanceOfMock() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(String.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setMethod(new Method(String.class.getName(), new Class[0]));
        invocation.setTarget(mock);

        Object result = returnsSmartNulls.answer(invocation);
        assertSame(mock, result);
    }

    @Test
    public void testAnswerInstanceOfMockWithReturnType() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(String.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setMethod(new Method("toString", new Class[0]));
        invocation.setTarget(mock);

        Object result = returnsSmartNulls.answer(invocation);
        assertEquals("SmartNull returned by unstubbed toString() method on mock", result.toString());
    }

    @Test
    public void testAnswerInstanceOfNonMockableClass() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(String.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setMethod(new Method(int.class.getName(), new Class[0]));
        invocation.setTarget(mock);

        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }

    @Test
    public void testAnswerInstanceOfNonMockableFinalClass() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(String.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setMethod(new Method("finalize", new Class[0]));
        invocation.setTarget(mock);

        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }

    @Test
    public void testAnswerInstanceOfClassWithMethodThatIsNotToString() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(String.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setMethod(new Method("hashCode", new Class[0]));
        invocation.setTarget(mock);

        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }

    @Test
    public void testAnswerInstanceOfClassWithThrowingMethod() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(String.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setMethod(new Method("throwRuntimeException", new Class[0]));
        invocation.setTarget(mock);

        try {
            returnsSmartNulls.answer(invocation);
            fail();
        } catch (RuntimeException e) {
            // expected
        }

        verify(mock, ArgumentMatchers.atLeastOnce()).toString();
    }
}