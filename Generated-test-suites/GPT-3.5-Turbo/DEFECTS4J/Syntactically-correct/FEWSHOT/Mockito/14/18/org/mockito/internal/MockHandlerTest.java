
/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;
    private MockSettingsImpl mockSettings;
    private InvocationContainerImpl invocationContainerImpl;
    private MatchersBinder matchersBinder;
    private MockingProgress mockingProgress;

    @Before
    public void setup() {
        mockSettings = new MockSettingsImpl();
        invocationContainerImpl = new InvocationContainerImpl(mockingProgress);
        matchersBinder = new MatchersBinder();
        mockingProgress = new ThreadSafeMockingProgress();

        mockHandler = new MockHandler<Object>(mockSettings);
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
    }

    @Test
    public void testHandleWithStubbedInvocation() throws Throwable {
        Invocation invocation = mockInvocation();

        invocationContainerImpl.addAnswerForStubbing(stubbedInvocation(invocation));

        assertNull(mockHandler.handle(invocation));

        verify(mockingProgress).reportOngoingStubbing(invocationContainerImpl);
    }

    private Invocation mockInvocation() {
        return new Invocation() {
            @Override
            public Object getMock() {
                return null;
            }

            @Override
            public Object[] getArguments() {
                return null;
            }

            @Override
            public void markVerified() {

            }

            @Override
            public InvocationMatcher getInvocationMatcher() {
                return null;
            }
        };
    }

    private StubbedInvocationMatcher stubbedInvocation(Invocation invocation) {
        return new StubbedInvocationMatcher(invocation, null);
    }

    @Test
    public void testHandleWithUnstubbedInvocation() throws Throwable {
        Invocation invocation = mockInvocation();

        assertEquals(mockSettings.getDefaultAnswer().answer(invocation), mockHandler.handle(invocation));

        verify(mockingProgress).reportOngoingStubbing(invocationContainerImpl);
    }

    @Test
    public void testHandleWithVoidMethodStubbable() throws Throwable {
        Invocation invocation = mockInvocation();

        VoidMethodStubbable voidMethodStubbable = new VoidMethodStubbableImpl<Object>(null, invocationContainerImpl);
        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(voidMethodStubbable).then();

        assertNull(mockHandler.handle(invocation));

        verify(mockingProgress).reportOngoingStubbing(invocationContainerImpl);
    }

    @Test
    public void testHandleWithVerificationMode() throws Throwable {
        Invocation invocation = mockInvocation();

        VerificationMode verificationMode = mockingProgress.pullVerificationMode();
        mockingProgress.validateState();

        assertNull(mockHandler.handle(invocation));

        verify(mockingProgress).pullVerificationMode();
        verify(mockingProgress).validateState();

        verify(verificationMode).verify(new VerificationDataImpl(invocationContainerImpl.getInvocations(), null));
    }
}
