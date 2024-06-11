package org.mockito.internal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationMatcher;
import org.mockito.invocation.MethodType;
import org.mockito.stubbing.Answer;

public class MockHandlerTest {

    private MockHandler<MockitoTestSubject> mockHandler;
    private MockSettingsImpl mockSettings;
    private InvocationContainer invocationContainer;
    private MatchersBinder matchersBinder;
    private ThreadSafeMockingProgress mockingProgress;

    @Before
    public void setUp() {
        mockSettings = new MockSettingsImpl();
        mockingProgress = new ThreadSafeMockingProgress();
        matchersBinder = new MatchersBinder();
        invocationContainer = new InvocationContainerImpl(mockingProgress);
        mockHandler = new MockHandler<>(mockSettings);
    }

    @Test
    public void testHandle_noAnswersOrVerificationMode_invokeRealMethod() throws Throwable {
        MockitoTestSubject mock = mock(MockitoTestSubject.class);
        Invocation invocation = Mockito.mock(Invocation.class);

        when(mock.doSomething()).thenReturn("mocked result");

        Object result = mockHandler.handle(invocation);
        assertEquals("mocked result", result);

        verify(mock).doSomething();
    }

    @Test
    public void testHandle_answersForStubbing_returnNull() throws Throwable {
        MockitoTestSubject mock = mock(MockitoTestSubject.class);
        Invocation invocation = Mockito.mock(Invocation.class);

        InvocationMatcher invocationMatcher = ArgumentMatchers.any();
        mockHandler.invocationContainerImpl.setAnswersForStubbing(Arrays.asList(new Answer() {
            @Override
            public Object answer(Invocation invocation) throws Throwable {
                return null;
            }
        }));

        mockHandler.handle(invocation);

        verify(mockingProgress).pushVerificationMode(null);
        verify(mockingProgress).pullVerificationMode();
        verify(mockingProgress).reportOngoingStubbing(Mockito.any());
        verify(mockingProgress).resetState();
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testHandle_answersForStubbing_returnAnswer() throws Throwable {
        MockitoTestSubject mock = mock(MockitoTestSubject.class);
        Invocation invocation = Mockito.mock(Invocation.class);

        InvocationMatcher invocationMatcher = ArgumentMatchers.any();
        mockHandler.invocationContainerImpl.setAnswersForStubbing(Arrays.asList(new Answer() {
            @Override
            public Object answer(Invocation invocation) throws Throwable {
                return "mocked answer";
            }
        }));

        Object result = mockHandler.handle(invocation);
        assertEquals("mocked answer", result);

        verify(mockingProgress).pushVerificationMode(null);
        verify(mockingProgress).pullVerificationMode();
        verify(mockingProgress).reportOngoingStubbing(Mockito.any());
        verify(mockingProgress).resetState();
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testHandle_verificationMode_verifyInvocation() throws Throwable {
        MockitoTestSubject mock = mock(MockitoTestSubject.class);
        Invocation invocation = Mockito.mock(Invocation.class);

        VerificationMode verificationMode = Mockito.verify(mock);
        InvocationMatcher invocationMatcher = ArgumentMatchers.any();

        mockHandler.handle(invocation);

        assertEquals(verificationMode, mockingProgress.popVerificationMode());
        assertEquals(verificationMode, mockingProgress.getVerificationMode());
        verify(mock).doSomething();
    }

    @Test
    public void testHandle_verificationMode_differentMock_ignoreVerification() throws Throwable {
        MockitoTestSubject mock1 = mock(MockitoTestSubject.class);
        MockitoTestSubject mock2 = mock(MockitoTestSubject.class);
        Invocation invocation = Mockito.mock(Invocation.class);

        VerificationMode verificationMode = Mockito.verify(mock1);

        mockHandler.handle(invocation);

        verify(mockingProgress).popVerificationMode();
        verifyZeroInteractions(mock2);
    }

    @Test
    public void testVoidMethodStubbable() throws Throwable {
        MockitoTestSubject mock = mock(MockitoTestSubject.class);

        VoidMethodStubbable<MockitoTestSubject> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);

        voidMethodStubbable.stubVoid();

        Invocation invocation = Mockito.mock(Invocation.class);
        mockHandler.handle(invocation);

        verify(mock).doSomething();
        verifyZeroInteractions(mock).doSomethingElse();
    }

    private static class MockitoTestSubject {
        public void doSomething() {
            // do something
        }

        public void doSomethingElse() {
            // do something else
        }
    }
}