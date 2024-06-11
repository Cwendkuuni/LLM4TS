package org.mockito.internal.stubbing.answers.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
public class CallsRealMethodsTest {

    // Test Case for method: public Object answer(InvocationOnMock invocation) throws Throwable
    @Test
    public void testCallRealMethod() throws Throwable {
        Object realObject = new Object() {
            @Override
            public String toString() {
                return "Real Object";
            }
        };
        CallsRealMethods answer = new CallsRealMethods();
        Object mockObject = mock(Object.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setTarget(mockObject);
        invocation.setMethod(RealObject.class.getMethod("toString"));
        invocation.setArguments(new Object[]{});

        Object result = answer.answer(invocation);
        assertEquals("Real Object", result.toString());
    }

    // Test Case for edge case: method call with arguments
    @Test(expected = Throwable.class)
    public void testCallRealMethodWithArguments() throws Throwable {
        CallsRealMethods answer = new CallsRealMethods();
        Object mockObject = mock(Object.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setTarget(mockObject);
        invocation.setMethod(Object.class.getMethod("toString", String.class));
        invocation.setArguments(new Object[]{ArgumentMatchers.anyString()});

        answer.answer(invocation);
    }

    // Test Case for exception handling: method call with checked exception
    @Test(expected = IOException.class)
    public void testCallRealMethodWithCheckedException() throws Throwable {
        CallsRealMethods answer = new CallsRealMethods();
        Object mockObject = mock(Object.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setTarget(mockObject);
        invocation.setMethod(Object.class.getMethod("doSomething"));

        try {
            answer.answer(invocation);
        } catch (RuntimeException e) {
            throw new IOException("Unexpected exception", e);
        }
    }

    // Test Case for exception handling: method call with unchecked exception
    @Test(expected = NullPointerException.class)
    public void testCallRealMethodWithUncheckedException() throws Throwable {
        CallsRealMethods answer = new CallsRealMethods();
        Object mockObject = mock(Object.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setTarget(mockObject);
        invocation.setMethod(Object.class.getMethod("doSomethingElse"));

        answer.answer(invocation);
    }
}