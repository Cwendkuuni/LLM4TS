package org.mockito.internal;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

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
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;

public class MockHandlerTest {

    private MockHandler<?> handler;

    @Test
    public void testHandle() throws Throwable {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        this.handler = new MockHandler<Object>(mockSettings);

        List<Answer> answers = new ArrayList<>();
        handler.setAnswersForStubbing(answers);

        InvocationContainerImpl invocationContainerImpl = new InvocationContainerImpl(new ThreadSafeMockingProgress());
        handler.invocationContainerImpl = invocationContainerImpl;

        MatchersBinder matchersBinder = new MatchersBinder();
        handler.matchersBinder = matchersBinder;

        Invocation invocation = new Invocation(null, null, null, 0);

        assertNull(handler.handle(invocation));

        invocationContainerImpl.setInvocationForPotentialStubbing(new InvocationMatcher(null, null));
        OngoingStubbingImpl<?> ongoingStubbing = new OngoingStubbingImpl<>(invocationContainerImpl);
        ThreadSafeMockingProgress mockingProgress = new ThreadSafeMockingProgress();
        mockSettings.setDefaultAnswer(mockingProgress);
        mockingProgress.reportOngoingStubbing(ongoingStubbing);

        StubbedInvocationMatcher stubbedInvocation = invocationContainerImpl.findAnswerFor(invocation);

        assertNull(handler.handle(invocation));

        answers.add(mock(Answer.class));
        handler.setAnswersForStubbing(answers);

        assertNull(handler.handle(invocation));
        assertNotNull(handler.voidMethodStubbable(null));
        assertNotNull(handler.getMockSettings());
        assertNotNull(handler.getInvocationContainer());
    }

    @Test
    public void testMockHandler() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        this.handler = new MockHandler<Object>(mockSettings);
        this.handler = new MockHandler<>();
        MockHandlerInterface<Object> oldMockHandler = mock(MockHandlerInterface.class);
        this.handler = new MockHandler<>(oldMockHandler);
    }

    @Test
    public void testVoidMethodStubbable() {
        this.handler = new MockHandler<Object>(new MockSettingsImpl());
        assertNotNull(handler.voidMethodStubbable(null));
    }
}