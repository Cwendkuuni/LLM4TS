package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
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
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {
    private MockHandler<MockHandlerInterface<String>> handler;

    @Before
    public void setup() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        handler = new MockHandler<>(mockSettings);
    }

    @Test
    public void testHandle_withStubbingVoids() throws Throwable {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = Mockito.mock(InvocationMatcher.class);
        MatchersBinder matchersBinder = Mockito.mock(MatchersBinder.class);
        MockingProgress mockingProgress = Mockito.mock(MockingProgress.class);
        handler.invocationContainerImpl = Mockito.mock(InvocationContainerImpl.class);
        handler.invocationContainerImpl.stubbingAnswers = new Answer[]{};
        handler.mockingProgress = mockingProgress;

        when(handler.matchersBinder.bindMatchers(Mockito.any(), Mockito.any())).thenReturn(invocationMatcher);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(any());

        assertNull(handler.handle(invocation));

        verify(handler.invocationContainerImpl).setMethodForStubbing(invocationMatcher);
    }

    @Test
    public void testHandle_withVerificationMode() throws Throwable {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = Mockito.mock(InvocationMatcher.class);
        MatchersBinder matchersBinder = Mockito.mock(MatchersBinder.class);
        MockingProgress mockingProgress = Mockito.mock(MockingProgress.class);
        VerificationMode verificationMode = Mockito.mock(VerificationMode.class);
        MockAwareVerificationMode mockAwareVerificationMode = Mockito.mock(MockAwareVerificationMode.class);
        handler.invocationContainerImpl = Mockito.mock(InvocationContainerImpl.class);
        handler.invocationContainerImpl.stubbingAnswers = new Answer[]{};
        handler.mockingProgress = mockingProgress;

        when(handler.matchersBinder.bindMatchers(Mockito.any(), Mockito.any())).thenReturn(invocationMatcher);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(verificationMode instanceof MockAwareVerificationMode).thenReturn(true);
        when(((MockAwareVerificationMode) verificationMode).getMock()).thenReturn(invocation.getMock());

        assertNull(handler.handle(invocation));

        verify(handler.invocationContainerImpl, never()).setMethodForStubbing(invocationMatcher);
        verify(mockingProgress, never()).validateState();
        verify(verificationMode).verify(any(VerificationDataImpl.class));
    }

    @Test
    public void testHandle_withStubbedInvocation() throws Throwable {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = Mockito.mock(InvocationMatcher.class);
        MatchersBinder matchersBinder = Mockito.mock(MatchersBinder.class);
        MockingProgress mockingProgress = Mockito.mock(MockingProgress.class);
        VerificationMode verificationMode = Mockito.mock(VerificationMode.class);
        MockAwareVerificationMode mockAwareVerificationMode = Mockito.mock(MockAwareVerificationMode.class);
        StubbedInvocationMatcher stubbedInvocation = Mockito.mock(StubbedInvocationMatcher.class);
        handler.invocationContainerImpl = Mockito.mock(InvocationContainerImpl.class);
        handler.invocationContainerImpl.stubbingAnswers = new Answer[]{};
        handler.mockSettings = Mockito.mock(MockSettingsImpl.class);
        handler.mockingProgress = mockingProgress;
        handler.matchersBinder = matchersBinder;

        when(handler.matchersBinder.bindMatchers(Mockito.any(), Mockito.any())).thenReturn(invocationMatcher);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(handler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(stubbedInvocation.captureArgumentsFrom(invocation)).thenReturn(any());
        when(stubbedInvocation.answer(invocation)).thenReturn(any());

        assertNull(handler.handle(invocation));

        verify(mockingProgress).reportOngoingStubbing(any(OngoingStubbingImpl.class));
        verify(handler.invocationContainerImpl, never()).setMethodForStubbing(invocationMatcher);
        verify(handler.mockSettings).getDefaultAnswer();
        verify(handler.invocationContainerImpl).resetInvocationForPotentialStubbing(invocationMatcher);
    }

    @Test
    public void testVoidMethodStubbable() {
        String mock = Mockito.mock(String.class);
        handler.invocationContainerImpl = Mockito.mock(InvocationContainerImpl.class);

        assertNotNull(handler.voidMethodStubbable(mock));
        verify(handler.invocationContainerImpl).resetUnstubbedInvocations();
    }

    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = Mockito.mock(List.class);
        handler.invocationContainerImpl = Mockito.mock(InvocationContainerImpl.class);

        handler.setAnswersForStubbing(answers);

        verify(handler.invocationContainerImpl).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer() {
        InvocationContainer container = handler.getInvocationContainer();

        assertNotNull(container);
        assertEquals(handler.invocationContainerImpl, container);
    }
}