
package org.mockito.internal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
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
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;
import java.util.List;

public class MockHandlerTest {

    @Test
    public void testHandle_whenInvocationContainerHasAnswersForStubbing() throws Throwable {
        //Setup
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockitoInvocationHandler mockHandler = new MockHandler<>(mockSettings);
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        ThreadSafeMockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(mock(InvocationMatcher.class));
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;

        //Test
        Object result = mockHandler.handle(invocation);

        //Verify
        assertNull(result);
        verify(invocationContainerImpl).setMethodForStubbing(any(InvocationMatcher.class));
    }

    @Test
    public void testHandle_whenVerificationModeIsNotNullAndMockAwareVerificationModeMockIsSameAsInvocationMock() throws Throwable {
        //Setup
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockitoInvocationHandler mockHandler = new MockHandler<>(mockSettings);
        Invocation invocation = mock(Invocation.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        ThreadSafeMockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        MockAwareVerificationMode verificationMode = mock(MockAwareVerificationMode.class);
        invocation.getMock();
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(mock(InvocationMatcher.class));
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;

        //Test
        Object result = mockHandler.handle(invocation);

        //Verify
        assertNull(result);
    }

    @Test
    public void testHandle_whenVerificationModeIsNotNullAndMockAwareVerificationModeMockIsDifferentFromInvocationMock() throws Throwable {
        //Setup
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockitoInvocationHandler mockHandler = new MockHandler<>(mockSettings);
        Invocation invocation = mock(Invocation.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        ThreadSafeMockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        MockAwareVerificationMode mockAwareVerificationMode = mock(MockAwareVerificationMode.class);
        invocation.getMock();
        when(mockingProgress.pullVerificationMode()).thenReturn(mockAwareVerificationMode);
        when(mockAwareVerificationMode.getMock()).thenReturn(null);
        when(matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(mock(InvocationMatcher.class));
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;

        //Test
        Object result = mockHandler.handle(invocation);

        //Verify
        assertNull(result);
    }

    @Test
    public void testHandle_whenVerificationModeIsNull() throws Throwable {
        //Setup
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockitoInvocationHandler mockHandler = new MockHandler<>(mockSettings);
        Invocation invocation = mock(Invocation.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        ThreadSafeMockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        when(matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(mock(InvocationMatcher.class));
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
        mockHandler.invocationContainerImpl = mock(InvocationContainerImpl.class);
        when(mockHandler.invocationContainerImpl.setInvocationForPotentialStubbing(any(InvocationMatcher.class))).thenReturn(null);

        //Test
        Object result = mockHandler.handle(invocation);

        //Verify
        assertNull(result);
    }

    @Test
    public void testHandle_whenStubbedInvocationIsNull() throws Throwable {
        //Setup
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockitoInvocationHandler mockHandler = new MockHandler<>(mockSettings);
        Invocation invocation = mock(Invocation.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        ThreadSafeMockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        MockAwareVerificationMode mockAwareVerificationMode = mock(MockAwareVerificationMode.class);
        invocation.getMock();
        when(mockingProgress.pullVerificationMode()).thenReturn(mockAwareVerificationMode);
        when(mockAwareVerificationMode.getMock()).thenReturn(invocation.getMock());
        when(matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(mock(InvocationMatcher.class));
        stub(invocation.getMock().toString()).toReturn("stubbedMessage");
        stub(mockSettings.getDefaultAnswer().answer(invocation)).toReturn("returnValue");
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        InvocationContainerImpl invocationContainerImpl2 = mock(InvocationContainerImpl.class);
        when(invocationContainerImpl.findAnswerFor(mock(Invocation.class))).thenReturn(null);
        MockitoInvocationHandler invocationHandler = mock(MockitoInvocationHandler.class);
        when(mockAwareVerificationMode.getInvocationHandler()).thenReturn(invocationHandler);
        when(invocationHandler.handle(invocation)).thenReturn("handleReturnValue");
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        when(invocationContainerImpl2.resetInvocationForPotentialStubbing(any(InvocationMatcher.class))).thenReturn(null);

        //Test
        Object result = mockHandler.handle(invocation);

        //Verify
        assertEquals("handleReturnValue", result);
    }

    @Test
    public void testVoidMethodStubbable() {
        //Setup
        T mock = mock(T.class);
        MockHandler<T> mockHandler = mock(MockHandler.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        when(mockHandler.voidMethodStubbable(mock)).thenCallRealMethod();
        when(mockHandler.invocationContainerImpl).thenReturn(invocationContainerImpl);

        //Test
        VoidMethodStubbable<T> result = mockHandler.voidMethodStubbable(mock);

        //Verify
        assertEquals(invocationContainerImpl, result.invocationContainer);
        assertEquals(mock, result.mock);
    }

    @Test
    public void testGetMockSettings() {
        //Setup
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<T> mockHandler = new MockHandler<>(mockSettings);

        //Test
        MockSettingsImpl result = mockHandler.getMockSettings();

        //Verify
        assertSame(mockSettings, result);
    }

    @Test
    public void testSetAnswersForStubbing() {
        //Setup
        List<Answer> answers = mock(List.class);
        MockHandler<T> mockHandler = new MockHandler<>(mock(MockSettingsImpl.class));
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        when(mockHandler.invocationContainerImpl).thenReturn(invocationContainerImpl);

        //Test
        mockHandler.setAnswersForStubbing(answers);

        //Verify
        verify(invocationContainerImpl).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer() {
        //Setup
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MockHandler<T> mockHandler = new MockHandler<>(mock(MockSettingsImpl.class));
        when(mockHandler.invocationContainerImpl).thenReturn(invocationContainerImpl);

        //Test
        InvocationContainer result = mockHandler.getInvocationContainer();

        //Verify
        assertSame(invocationContainerImpl, result);
    }
}
