package org.mockito.internal;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationContainer;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.AnswerStub;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {

    @Test
    public void testHandle_NoAnswersForStubbing() throws Throwable {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);
        Invocation invocation = mock(Invocation.class);
        Object returnValue = "return value";

        when(mockSettings.getDefaultAnswer()).thenReturn(new AnswerStub("test") {
            @Override
            public Object answer(Invocation invocation) throws Throwable {
                return returnValue;
            }
        });

        Object result = mockHandler.handle(invocation);
        assertSame(returnValue, result);
    }

    @Test
    public void testHandle_AnswersForStubbing() throws Throwable {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);
        Invocation invocation = mock(Invocation.class);
        Object[] arguments = new Object[]{"argument1", "argument2"};

        List<Answer> answers = Arrays.asList(
                new AnswerStub("test1") {
                    @Override
                    public Object answer(Invocation invocation) throws Throwable {
                        return "answer1";
                    }
                },
                new AnswerStub("test2") {
                    @Override
                    public Object answer(Invocation invocation) throws Throwable {
                        return "answer2";
                    }
                }
        );

        mockHandler.setAnswersForStubbing(answers);

        VerificationMode verificationMode = mock(VerificationMode.class);
        mockingProgress.pushVerificationMode(verificationMode);

        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(invocationMatcher.matches(any(Invocation.class))).thenReturn(true);

        mockHandler.invocationContainerImpl.setInvocationForPotentialStubbing(invocationMatcher);

        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        when(stubbedInvocation.answer(eq(invocation))).thenReturn("answer3");

        mockHandler.invocationContainerImpl.setAnswerFor(stubbedInvocation);

        Object result = mockHandler.handle(invocation);
        assertSame("answer3", result);

        verify(verificationMode).verify(eq(invocation));
    }

    @Test
    public void testHandle_NoVerificationMode() {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);
        Invocation invocation = mock(Invocation.class);

        mockHandler.mockingProgress.pushVerificationMode(null);

        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(invocationMatcher.matches(any(Invocation.class))).thenReturn(true);

        mockHandler.invocationContainerImpl.setInvocationForPotentialStubbing(invocationMatcher);

        Object result = mockHandler.handle(invocation);

        verifyNoMoreInteractions(invocationMatcher);
        verifyZeroInteractions(mockHandler.mockingProgress);
    }

    @Test
    public void testHandle_VerificationModeMismatch() {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);

        mockHandler.mockingProgress.pushVerificationMode(mock(VerificationMode.class));

        when(invocationMatcher.matches(any(Invocation.class))).thenReturn(true);

        mockHandler.invocationContainerImpl.setInvocationForPotentialStubbing(invocationMatcher);

        Object otherMock = mock(Object.class);
        VerificationMode otherVerificationMode = mock(VerificationMode.class);

        when(invocation.getMock()).thenReturn(otherMock);
        when(otherVerificationMode.verify(any(VerificationDataImpl.class))).thenThrow(new IllegalStateException());

        mockHandler.mockingProgress.pushVerificationMode(otherVerificationMode);

        Object result = mockHandler.handle(invocation);

        verifyNoMoreInteractions(invocationMatcher);
        verifyZeroInteractions(mockHandler.mockingProgress);
    }

    @Test
    public void testHandle_VerificationModeMatch() {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        VerificationMode verificationMode = mock(VerificationMode.class);

        mockHandler.mockingProgress.pushVerificationMode(verificationMode);

        when(invocationMatcher.matches(any(Invocation.class))).thenReturn(true);
        when(invocation.getMock()).thenReturn(invocation);

        mockHandler.invocationContainerImpl.setInvocationForPotentialStubbing(invocationMatcher);

        VerificationDataImpl data = mock(VerificationDataImpl.class);
        when(data.getInvocation()).thenReturn(invocation);

        verificationMode.verify(data);

        verifyNoMoreInteractions(verificationMode);
        verifyNoMoreInteractions(invocationMatcher);
        verifyZeroInteractions(mockHandler.mockingProgress);
    }

    @Test
    public void testVoidMethodStubbable() {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);
        Object mock = mock(Object.class);

        VoidMethodStubbable<Object> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);

        assertSame(mock, voidMethodStubbable.getMock());
    }
}