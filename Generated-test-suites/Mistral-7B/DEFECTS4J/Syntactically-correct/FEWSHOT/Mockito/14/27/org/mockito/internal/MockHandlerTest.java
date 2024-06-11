package org.mockito.internal.test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stub;
import org.mockito.verification.VerificationMode;

import org.mockito.internal.MockHandler;

public class MockHandlerTest {

    @Test
    public void testHandle_NoStubbingOrVerification() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);
        Object mock = mockHandler.getMockSettings().createMock(Object.class);

        Invocation invocation = MockitoInvocationHandler.createInvocation(Object.class, new Object[0]);

        Object result = mockHandler.handle(invocation);

        assertEquals(mockDefaultAnswer(invocation), result);
    }

    @Test
    public void testHandle_StubbingVoidMethod() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        Object mock = mockHandler.getMockSettings().createMock(Object.class);

        Invocation invocation = MockitoInvocationHandler.createInvocation(Void.TYPE, new Object[0]);
        when(mock).stubVoid("voidMethod");

        mockHandler.handle(invocation);
        assertNull(result);
    }

    @Test
    public void testHandle_StubbingMethodWithAnswer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        Object mock = mockHandler.getMockSettings().createMock(Object.class);

        Invocation invocation = MockitoInvocationHandler.createInvocation(Object.class, new Object[0]);

        when(mock).thenAnswer(i -> "answer");

        assertEquals("answer", mockHandler.handle(invocation));
    }

    @Test
    public void testHandle_StubbingMethodWithStub() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        Object mock = mockHandler.getMockSettings().createMock(Object.class);

        Invocation invocation = MockitoInvocationHandler.createInvocation(Object.class, new Object[0]);

        when(mock).thenStub(i -> "stub");

        assertEquals("stub", mockHandler.handle(invocation));
    }

    @Test
    public void testHandle_StubbingMethodWithAnswerAndStub() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        Object mock = mockHandler.getMockSettings().createMock(Object.class);

        Invocation invocation = MockitoInvocationHandler.createInvocation(Object.class, new Object[0]);

        when(mock).thenAnswer(i -> "answer");
        when(mock).thenStub(i -> "stub");

        Object result = mockHandler.handle(invocation);

        assertEquals("answer", result);
    }

    @Test
    public void testHandle_VerificationModeNull() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        Object mock = mockHandler.getMockSettings().createMock(Object.class);
        Invocation invocation = MockitoInvocationHandler.createInvocation(Object.class, new Object[0]);

        mockHandler.handle(invocation);
        verifyZeroInteractions(mock);
    }

    @Test
    public void testHandle_VerificationModeVerify() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        Object mock = mockHandler.getMockSettings().createMock(Object.class);

        Invocation invocation1 = MockitoInvocationHandler.createInvocation(Object.class, new Object[0]);
        Invocation invocation2 = MockitoInvocationHandler.createInvocation(Object.class, new Object[0]);

        when(mock).thenAnswer(i -> "answer1");
        when(mock).thenAnswer(i -> "answer2");

        mockHandler.handle(invocation1);
        mockHandler.handle(invocation2);

        verify(mock).answer(ArgumentMatchers.any());
        verify(mock, times(1)).answer(ArgumentMatchers.any());
        verify(mock).answer(invocation1);
        verify(mock).answer(invocation2);
        verify(mock).answer(invocation1);
        verify(mock).answer(invocation2);
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testHandle_VerificationModeVerifyOrder() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        Object mock = mockHandler.getMockSettings().createMock(Object.class);

        Invocation invocation1 = MockitoInvocationHandler.createInvocation(Object.class, new Object[0]);
        Invocation invocation2 = MockitoInvocationHandler.createInvocation(Object.class, new Object[0]);

        when(mock).thenAnswer(i -> "answer1");
        when(mock).thenAnswer(i -> "answer2");

        mockHandler.handle(invocation2);
        mockHandler.handle(invocation1);

        verify(mock).answer(invocation1);
        verify(mock).answer(invocation2);
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testHandle_VerificationModeWithMatcher() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        Object mock = mockHandler.getMockSettings().createMock(Object.class);

        Invocation invocation = MockitoInvocationHandler.createInvocation(Object.class, new Object[0]);

        when(mock).thenAnswer(i -> "answer");

        VerificationMode verificationMode = verify(mock).answer(ArgumentMatchers.isA(Long.class));

        mockHandler.handle(invocation);

        verificationMode.verify();
    }

    @Test
    public void testHandle_VerificationModeWithMatcherAndArgument() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        Object mock = mockHandler.getMockSettings().createMock(Object.class);

        Invocation invocation = MockitoInvocationHandler.createInvocation(Object.class, new Object[]{1L});

        when(mock).thenAnswer(i -> "answer");

        VerificationMode verificationMode = verify(mock).answer(ArgumentMatchers.eq(1L));

        mockHandler.handle(invocation);

        verificationMode.verify();
    }

    @Test
    public void testHandle_VerificationModeWithMatcherAndArgumentAndTimes() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        Object mock = mockHandler.getMockSettings().createMock(Object.class);

        Invocation invocation = MockitoInvocationHandler.createInvocation(Object.class, new Object[]{1L, 2L});

        when(mock).thenAnswer(i -> "answer");

        VerificationMode verificationMode = verify(mock, times(2)).answer(ArgumentMatchers.eq(1L));

        mockHandler.handle(invocation);
        mockHandler.handle(invocation);

        verificationMode.verify();
    }

    @Test
    public void testHandle_VerificationModeWithMatcherAndArgumentAndTimesAndInOrder() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        Object mock = mockHandler.getMockSettings().createMock(Object.class);

        Invocation invocation1 = MockitoInvocationHandler.createInvocation(Object.class, new Object[]{1L});
        Invocation invocation2 = MockitoInvocationHandler.createInvocation(Object.class, new Object[]{1L});

        when(mock).thenAnswer(i -> "answer");

        VerificationMode verificationMode = verify(mock).answer(ArgumentMatchers.eq(1L)).inOrder();

        mockHandler.handle(invocation1);
        mockHandler.handle(invocation2);

        verificationMode.verify();
    }

    @Test
    public void testHandle_VerificationModeWithMatcherAndArgumentAndTimesAndInOrder_PartialOrder() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        Object mock = mockHandler.getMockSettings().createMock(Object.class);

        Invocation invocation1 = MockitoInvocationHandler.createInvocation(Object.class, new Object[]{1L});
        Invocation invocation2 = MockitoInvocationHandler.createInvocation(Object.class, new Object[]{2L});

        when(mock).thenAnswer(i -> "answer");

        VerificationMode verificationMode = verify(mock).answer(ArgumentMatchers.eq(1L)).inOrder();

        mockHandler.handle(invocation2);
        mockHandler.handle(invocation1);

        verificationMode.verify();
    }

    @Test
    public void testHandle_VerificationModeWithMatcherAndArgumentAndTimesAndInOrder_NoInteractions() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        Object mock = mockHandler.getMockSettings().createMock(Object.class);

        Invocation invocation1 = MockitoInvocationHandler.createInvocation(Object.class, new Object[]{1L});
        Invocation invocation2 = MockitoInvocationHandler.createInvocation(Object.class, new Object[]{1L});

        when(mock).thenAnswer(i -> "answer");

        VerificationMode verificationMode = verify(mock).answer(ArgumentMatchers.eq(1L)).inOrder();

        mockHandler.handle(invocation1);
        mockHandler.handle(invocation2);

        verifyNoMoreInteractions(mock);
    }

    @Test(expected = IllegalStateException.class)
    public void testHandle_VerificationModeWithMatcherAndArgumentAndTimesAndInOrder_MismatchingOrder() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        Object mock = mockHandler.getMockSettings().createMock(Object.class);

        Invocation invocation1 = MockitoInvocationHandler.createInvocation(Object.class, new Object[]{1L});
        Invocation invocation2 = MockitoInvocationHandler.createInvocation(Object.class, new Object[]{2L});

        when(mock).thenAnswer(i -> "answer");

        VerificationMode verificationMode = verify(mock).answer(ArgumentMatchers.eq(1L)).inOrder();

        mockHandler.handle(invocation2);
        mockHandler.handle(invocation1);

        verificationMode.verify();
    }

    @Test
    public void testHandle_VerificationModeWithMatcherAndArgumentAndTimesAndInOrder_MultipleInvocations() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        Object mock = mockHandler.getMockSettings().createMock(Object.class);

        Invocation invocation = MockitoInvocationHandler.createInvocation(Object.class, new Object[0]);

        when(mock).thenAnswer(i -> "answer");

        VerificationMode verificationMode = verify(mock, times(3)).answer(ArgumentMatchers.any());

        mockHandler.handle(invocation);
        mockHandler.handle(invocation);
        mockHandler.handle(invocation);

        verificationMode.verify();
    }

    @Test
    public void testHandle_VerificationModeWithMatcherAndArgumentAndTimes_MultipleVerifications() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        Object mock = mockHandler.getMockSettings().createMock(Object.class);

        Invocation invocation1 = MockitoInvocationHandler.createInvocation(Object.class, new Object[]{1L});
        Invocation invocation2 = MockitoInvocationHandler.createInvocation(Object.class, new Object[]{2L});

        when(mock).thenAnswer(i -> "answer1");
        when(mock).thenAnswer(i -> "answer2");

        VerificationMode verificationMode1 = verify(mock).answer(ArgumentMatchers.eq(1L));
        VerificationMode verificationMode2 = verify(mock).answer(ArgumentMatchers.eq(2L));

        mockHandler.handle(invocation1);
        mockHandler.handle(invocation2);

        verificationMode1.verify();
        verificationMode2.verify();
    }

    @Test
    public void testHandle_VerificationModeWithMatcherAndArgumentAndTimes_MultipleVerificationsWithInOrder() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        Object mock = mockHandler.getMockSettings().createMock(Object.class);

        Invocation invocation1 = MockitoInvocationHandler.createInvocation(Object.class, new Object[]{1L});
        Invocation invocation2 = MockitoInvocationHandler.createInvocation(Object.class, new Object[]{2L});

        when(mock).thenAnswer(i -> "answer1");
        when(mock).thenAnswer(i -> "answer2");

        VerificationMode verificationMode1 = verify(mock).answer(ArgumentMatchers.eq(1L)).inOrder();
        VerificationMode verificationMode2 = verify(mock).answer(ArgumentMatchers.eq(2L)).inOrder();

        mockHandler.handle(invocation1);
        mockHandler.handle(invocation2);

        verificationMode1.verify();
        verificationMode2.verify();
    }

    @Test
    public void testHandle_VerificationModeWithMatcherAndArgumentAndTimes_MultipleVerificationsWithInOrder_MultipleInvocations() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        Object mock = mockHandler.getMockSettings().createMock(Object.class);

        Invocation invocation = MockitoInvocationHandler.createInvocation(Object.class, new Object[0]);

        when(mock).thenAnswer(i -> "answer");

        VerificationMode verificationMode1 = verify(mock, times(2)).answer(ArgumentMatchers.eq(1L));
        VerificationMode verificationMode2 = verify(mock, times(3)).answer(ArgumentMatchers.eq(1L)).inOrder();

        mockHandler.handle(invocation);
        mockHandler.handle(invocation);

        verificationMode1.verify();
        verificationMode2.verify();
    }

    private Object mockDefaultAnswer(Invocation invocation) {
        return Optional.ofNullable(invocation.getMethod().getDefaultValue())
                .map(Answer::answer).orElseGet(() -> ((VoidMethodStubbable<?>) mockHandler.voidMethodStubbable(mock)).answer(invocation));
    }
}