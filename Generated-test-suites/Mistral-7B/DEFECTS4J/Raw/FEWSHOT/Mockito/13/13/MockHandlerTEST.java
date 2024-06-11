package org.mockito.internal;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.ArgumentMatcher;
import org.mockito.Invocation;
import org.mockito.InvocationMatcher;
import org.mockito.Mockito;
import org.mockito.MockSettings;
import org.mockito.stubbing.AnswerAnswer;
import org.mockito.stubbing.AnswerReturnValue;
import org.mockito.stubbing.AnswerThrowingThrowable;
import org.mockito.stubbing.AnswerVoid;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;
    private Object mock;

    @Before
    public void setUp() {
        MockSettings mockSettings = new MockSettingsImpl();
        mockHandler = new MockHandler<Object>(mockSettings);
        mock = mock(Object.class);
    }

    @Test
    public void testHandleNullInvocation() {
        assertNull(mockHandler.handle(null));
    }

    @Test
    public void testHandleNoStubbing() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Object result = mockHandler.handle(invocation);
        assertNull(result);

        verifyNoInteractions(mock);
    }

    @Test
    public void testHandleVerificationMode() {
        Invocation invocation = Mockito.mock(Invocation.class);
        VerificationMode verificationMode = Mockito.mock(MockAwareVerificationMode.class);

        mockingProgress.pushVerificationMode(verificationMode);

        mockHandler.handle(invocation);

        verify(verificationMode).verify(Mockito.argThat(Mockito.isInstance(mock)));
    }

    @Test
    public void testHandleStubbing() {
        Invocation invocation = Mockito.mock(Invocation.class);
        String expected = "expected";
        ArgumentMatcher<?> argumentMatcher = Mockito.argThat(Mockito.isInstance(expected));

        when(mock).thenAnswer(new AnswerAnswer(new AnswerVoid(), expected));

        Object result = mockHandler.handle(invocation);
        assertNull(result);

        verify(mock).handle(argumentMatcher);
    }

    @Test
    public void testHandleStubbingWithCustomAnswer() {
        Invocation invocation = Mockito.mock(Invocation.class);
        String expected = "expected";
        ArgumentMatcher<?> argumentMatcher = Mockito.argThat(Mockito.isInstance(expected));

        when(mock).thenAnswer(new AnswerReturnValue(42));

        Object result = mockHandler.handle(invocation);
        assertEquals(42, result);

        verify(mock).handle(argumentMatcher);
    }

    @Test
    public void testHandleStubbingWithThrowingAnswer() {
        Invocation invocation = Mockito.mock(Invocation.class);
        RuntimeException expectedException = new RuntimeException("expected exception");
        ArgumentMatcher<?> argumentMatcher = Mockito.argThat(Mockito.isInstance(mock));

        when(mock).thenAnswer(new AnswerThrowingThrowable(expectedException));

        try {
            mockHandler.handle(invocation);
            fail("Expecting exception");
        } catch (RuntimeException e) {
            assertEquals(expectedException.getMessage(), e.getMessage());
        }

        verify(mock).handle(argumentMatcher);
    }

    @Test
    public void testHandleStubbingWithVoidMethod() {
        Invocation invocation = Mockito.mock(Invocation.class);
        ArgumentMatcher<?> argumentMatcher = Mockito.argThat(Mockito.isInstance(mock));

        doNothing().when(mock).voidMethod();

        mockHandler.handle(invocation);

        verify(mock).handle(argumentMatcher);
        verify(mock).voidMethod();
    }

    @Test
    public void testHandleStubbingWithMultipleStubbing() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        String expected = "expected";
        ArgumentMatcher<?> argumentMatcher = Mockito.argThat(Mockito.isInstance(expected));

        when(mock).thenAnswer(new AnswerAnswer(new AnswerVoid(), expected)).thenAnswer(new AnswerAnswer(new AnswerReturnValue("result"), expected));

        Object result1 = mockHandler.handle(invocation1);
        assertNull(result1);

        verify(mock).handle(argumentMatcher);

        Object result2 = mockHandler.handle(invocation2);
        assertEquals("result", result2);

        verify(mock).handle(argumentMatcher);
    }

    @Test
    public void testHandleStubbingWithPartialMock() {
        Object realObject = new Object() {
            public void realMethod() {
                System.out.println("realMethod");
            }
        };
        Invocation invocation = Mockito.mock(Invocation.class);

        when(mock).thenReturn(realObject);

        Object result = mockHandler.handle(invocation);
        assertSame(realObject, result);

        verify(realObject).handle(Mockito.any());
    }

    @Test
    public void testHandleStubbingWithAnswerVoid() {
        Invocation invocation = Mockito.mock(Invocation.class);

        mockHandler.handle(invocation);

        verify(mock, Mockito.never()).handle(Mockito.any());
    }

    @Test
    public void testHandleStubbingWithAnswerReturnValue() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Object expected = new Object();

        Answer<Object> answer = new AnswerReturnValue<>(expected);
        mockHandler.setAnswersForStubbing(Collections.singletonList(answer));

        Object result = mockHandler.handle(invocation);
        assertEquals(expected, result);

        verify(mock).handle(Mockito.any());
    }

    @Test
    public void testHandleStubbingWithAnswerThrowingThrowable() {
        Invocation invocation = Mockito.mock(Invocation.class);
        RuntimeException expectedException = new RuntimeException("expected exception");

        Answer<Object> answer = new AnswerThrowingThrowable<>(expectedException);
        mockHandler.setAnswersForStubbing(Collections.singletonList(answer));

        try {
            mockHandler.handle(invocation);
            fail("Expecting exception");
        } catch (RuntimeException e) {
            assertEquals(expectedException.getMessage(), e.getMessage());
        }

        verify(mock).handle(Mockito.any());
    }

    @Test
    public void testHandleStubbingWithAnswerVoidWithNoArguments() {
        Invocation invocation = Mockito.mock(Invocation.class);

        mockHandler.handle(invocation);

        verify(mock, Mockito.never()).handle(Mockito.any());
    }

    @Test
    public void testHandleStubbingWithAnswerVoidWithArguments() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Object[] arguments = new Object[]{"arg1", "arg2"};

        mockHandler.setAnswersForStubbing(Arrays.asList(new AnswerVoid()));

        mockHandler.handle(invocation);

        verify(mock).handle(Mockito.argThat(arg -> Arrays.equals(arg, arguments)));
    }

    @Test
    public void testHandleStubbingWithStubbingVoidMethod() {
        Invocation invocation = Mockito.mock(Invocation.class);

        VoidMethodStubbable<Object> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);
        voidMethodStubbable.stubVoid();

        mockHandler.handle(invocation);

        verifyNoInteractions(mock);
    }

    @Test
    public void testHandleStubbingWithStubbingVoidMethodWithArguments() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Object[] arguments = new Object[]{"arg1", "arg2"};

        VoidMethodStubbable<Object> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);
        voidMethodStubbable.stubVoid(Mockito.argThat(arg -> Arrays.equals(arg, arguments)));

        mockHandler.handle(invocation);

        verify(mock).handle(Mockito.argThat(arg -> Arrays.equals(arg, arguments)));
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testHandleStubbingWithStubbingVoidMethodWithException() {
        Invocation invocation = Mockito.mock(Invocation.class);
        RuntimeException expectedException = new RuntimeException("expected exception");

        VoidMethodStubbable<Object> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);
        voidMethodStubbable.stubVoidThrowing(expectedException);

        try {
            mockHandler.handle(invocation);
            fail("Expecting exception");
        } catch (RuntimeException e) {
            assertEquals(expectedException.getMessage(), e.getMessage());
        }

        verify(mock).handle(Mockito.any());
    }

    @Test
    public void testHandleStubbingWithStubbingReturningAnswer() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Object expected = new Object();

        VoidMethodStubbable<Object> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);
        voidMethodStubbable.stubReturning(expected);

        Object result = mockHandler.handle(invocation);
        assertEquals(expected, result);

        verify(mock).handle(Mockito.any());
    }

    @Test
    public void testHandleStubbingWithStubbingReturningAnswerWithArguments() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Object[] arguments = new Object[]{"arg1", "arg2"};

        VoidMethodStubbable<Object> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);
        voidMethodStubbable.stubReturning(arguments);

        Object[] result = (Object[]) mockHandler.handle(invocation);
        assertArrayEquals(arguments, result);

        verify(mock).handle(Mockito.any());
    }

    @Test
    public void testHandleStubbingWithStubbingReturningAnswerWithException() {
        Invocation invocation = Mockito.mock(Invocation.class);
        RuntimeException expectedException = new RuntimeException("expected exception");

        VoidMethodStubbable<Object> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);
        voidMethodStubbable.stubReturningThrowing(expectedException);

        try {
            mockHandler.handle(invocation);
            fail("Expecting exception");
        } catch (RuntimeException e) {
            assertEquals(expectedException.getMessage(), e.getMessage());
        }

        verify(mock).handle(Mockito.any());
    }
}