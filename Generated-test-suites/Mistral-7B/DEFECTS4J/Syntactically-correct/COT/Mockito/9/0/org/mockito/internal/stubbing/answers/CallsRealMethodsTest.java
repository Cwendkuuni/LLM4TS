package org.mockito.internal.stubbing.answers.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answer;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.AnswerCalledWith;
import org.mockito.stubbing.AnswerReturns;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.quality.DontUsePartialMocks;
import org.mockito.quality.DontVerifyMockMethods;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@DontUsePartialMocks
@DontVerifyMockMethods
public class CallsRealMethodsTest {

    @Test
    public void testAnswerMethodWithNoArguments() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        Object mock = mock(SomeInterface.class);

        when(mock.someMethod()).thenAnswer(callRealMethods);

        mock.someMethod();

        verify(mock).someMethod();
    }

    @Test
    public void testAnswerMethodWithOneArgumentIntegerType(Exception e) {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        SomeInterface mock = mock(SomeInterface.class);

        when(mock.someMethod(1)).thenAnswer(new AnswerCalledWith(1) {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return callRealMethods.answer(invocation);
            }
        });

        mock.someMethod(1);

        verify(mock).someMethod(1);
    }

    @Test
    public void testAnswerMethodWithOneArgumentStringType() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        SomeInterface mock = mock(SomeInterface.class);

        when(mock.someMethod("test")).thenAnswer(callRealMethods);

        mock.someMethod("test");

        verify(mock).someMethod("test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAnswerMethodWithTwoArgumentsThrowsIllegalArgumentException() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        SomeInterface mock = mock(SomeInterface.class);

        when(mock.someMethod(1, "test")).thenAnswer(callRealMethods);

        mock.someMethod(1, "test"); // This should call the real method

        verify(mock).someMethod(1, "test");

        // This test case should fail with an IllegalArgumentException
        mock.someMethod(2, "test");
    }

    @Test(expected = IllegalStateException.class)
    public void testAnswerMethodWithUnsupportedTypeThrowsIllegalStateException() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        SomeInterface mock = mock(SomeInterface.class);

        when(mock.someMethod((Object) new byte[0])).thenAnswer(callRealMethods);

        // This should call the real method
        mock.someMethod((Object) new byte[0]);

        // This test case should fail with an IllegalStateException
        mock.someMethod(new byte[0]);
    }

    @Test(expected = NullPointerException.class)
    public void testAnswerMethodWithNullArgumentThrowsNullPointerException() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        SomeInterface mock = mock(SomeInterface.class);

        when(mock.someMethod(null)).thenAnswer(callRealMethods);

        // This should call the real method
        mock.someMethod(null);

        // This test case should fail with a NullPointerException
        mock.someMethod(null);
    }

    @Test(expected = RuntimeException.class)
    public void testAnswerMethodWithThrowingMethodThrowsException() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        SomeInterface mock = mock(SomeInterface.class);

        when(mock.someMethod()).thenAnswer(new AnswerReturns(new RuntimeException("Test Exception")) {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                throw new RuntimeException("Test Exception");
            }
        });

        try {
            mock.someMethod();
            fail("This method should have thrown an exception");
        } catch (RuntimeException e) {
            // Expected
        }
    }

    @Test
    public void testAnswerMethodWithEmptyArray() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        SomeInterface mock = mock(SomeInterface.class);

        when(mock.someMethod((Object[]) new Object[0])).thenAnswer(callRealMethods);

        mock.someMethod((Object[]) new Object[0]);

        verify(mock).someMethod((Object[]) new Object[0]);
    }

    interface SomeInterface {
        void someMethod();
        void someMethod(int arg1);
        void someMethod(String arg1);
        void someMethod(int arg1, String arg2);
        void someMethod(Object arg1);
    }
}