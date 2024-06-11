package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswerReturnsDefaultValue() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        Object returnValue = returnsSmartNulls.answer(createInvocationOnMock("methodThatReturnsDefaultValue", null));
        assertNull(returnValue);
    }

    @Test
    public void testAnswerReturnsSmartNullForString() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        String result = (String) returnsSmartNulls.answer(createInvocationOnMock("toStringMethod", null));
        assertEquals("SmartNull returned by unstubbed toString method on mock", result);
    }

    @Test
    public void testAnswerReturnsSmartNullForPrimitiveTypes() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        byte result = (Byte) returnsSmartNulls.answer(createInvocationOnMock("methodThatReturnsByte", null));
        assertEquals("SmartNull returned by unstubbed methodThatReturnsByte method on mock", result);
    }

    @Test
    public void testAnswerReturnsSmartNullForObject() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        Object result = returnsSmartNulls.answer(createInvocationOnMock("methodThatReturnsObject", new Object()));
        assertEquals("SmartNull returned by unstubbed methodThatReturnsObject method on mock", result.getClass().getName());
    }

    @Test
    public void testAnswerReturnsSmartNullForArrayList() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        Object result = returnsSmartNulls.answer(createInvocationOnMock("methodThatReturnsArrayList", Arrays.asList(new Object[]{})));
        assertEquals("SmartNull returned by unstubbed methodThatReturnsArrayList method on mock", result.getClass().getName());
    }

    @Test
    public void testAnswerReturnsSmartNullForStringArray() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        String[] result = (String[]) returnsSmartNulls.answer(createInvocationOnMock("methodThatReturnsStringArray", new String[0]));
        assertEquals("SmartNull returned by unstubbed methodThatReturnsStringArray method on mock", result.getClass().getName());
    }

    @Test
    public void testAnswerReturnsSmartNullForMethodWithoutReturnType() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        Method method = new Object() {}.getClass().getDeclaredMethod("methodWithoutReturnType", new Class[0]);
        InvocationOnMock invocation = createInvocationOnMock(method, null);
        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }

    @Test
    public void testAnswerReturnsNullForNonMockableType() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        Class<?> nonMockableType = Long.TYPE;
        InvocationOnMock invocation = createInvocationOnMock(new Object() {}.getClass().getMethod("methodThatReturnsLong", new Class[0]), nonMockableType);
        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }

    @Test
    public void testThrowingInterceptorThrowsNpeWhenToStringIsCalled() {
        ReturnsSmartNulls returnsSmartNulls = spy(new ReturnsSmartNulls());
        InvocationOnMock invocation = createInvocationOnMock("toStringMethod", null);
        try {
            returnsSmartNulls.answer(invocation);
            fail("Expected NPE but got: " + returnsSmartNulls.answer(invocation));
        } catch (NullPointerException e) {
            assertEquals("unexpected method call to toString()", e.getMessage());
        }
    }

    @Test
    public void testAnswerReturnsDelegateValueWhenAvailable() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        Object delegateValue = new Object();
        Answer<Object> delegate = mock(Answer.class);
        when(delegate.answer(any(InvocationOnMock.class))).thenReturn(delegateValue);
        when(returnsSmartNulls.delegate).thenReturn(delegate);
        Object result = returnsSmartNulls.answer(createInvocationOnMock("methodThatReturnsDelegateValue", null));
        assertEquals(delegateValue, result);
    }

    private InvocationOnMock createInvocationOnMock(Method method, Object argument) {
        Object mock = mock(method.getDeclaringClass());
        return new InvocationOnMock(mock, method, argument);
    }

    private InvocationOnMock createInvocationOnMock(String methodName, Object argument) {
        Method method = new Object() {}.getClass().getMethod(methodName, new Class[0]);
        return createInvocationOnMock(method, argument);
    }

}