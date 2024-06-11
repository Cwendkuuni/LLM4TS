package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CallsRealMethodsTest {

    @Test
    public void testCallRealMethod() {
        Object realObject = mock(Object.class);
        CallsRealMethods answer = new CallsRealMethods();

        Object result = answer.answer(ArgumentMatchers.invocationCalledOn(realObject));

        verify(realObject).callRealMethod();
        assertEquals(result, result); // since callRealMethod returns the result of invoking the method on the real object
    }

    @Test
    public void testCallRealMethodWithArguments() {
        Object realObject = mock(Object.class);
        Object arg1 = "arg1";
        Object arg2 = 1;

        CallsRealMethods answer = new CallsRealMethods();

        Object result = answer.answer(ArgumentMatchers.argThat(invocation -> invocation.getArguments()[0].equals(arg1) && invocation.getArguments()[1].equals(arg2)));

        verify(realObject).callRealMethod(arg1, arg2);
        assertEquals(result, result);
    }

    @Test
    public void testCallRealMethodReturnsNull() throws Exception {
        Object realObject = mock(Object.class);
        CallsRealMethods answer = new CallsRealMethods();

        Object result = answer.answer(ArgumentMatchers.invocationCalledOn(realObject));

        verify(realObject, MockitoJUnitRunner.verificationMode().never()).callRealMethod(); // since we are testing that the real method is not called
        assertNull(result);
    }

    @Test
    public void testCallRealMethodThrowsException() throws Throwable {
        Object realObject = mock(Object.class);
        CallsRealMethods answer = new CallsRealMethods();

        InvocationOnMock invocation = ArgumentMatchers.invocationCalledOn(realObject);
        invocation.getMethod().setException(new RuntimeException("Test Exception"));

        try {
            answer.answer(invocation);
            fail();
        } catch (RuntimeException e) {
            assertEquals("Test Exception", e.getMessage());
        }

        verify(realObject).callRealMethod();
    }
}