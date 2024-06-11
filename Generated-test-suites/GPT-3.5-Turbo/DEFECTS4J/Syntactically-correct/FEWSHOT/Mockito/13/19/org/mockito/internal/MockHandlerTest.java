
//Corresponding JUnit 4 Test Cases:
package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;
    private InvocationContainerImpl invocationContainerImpl;
    private MatchersBinder matchersBinder;
    private MockingProgress mockingProgress;

    @Before
    public void setup() {
        mockHandler = new MockHandler<Object>();
        invocationContainerImpl = mock(InvocationContainerImpl.class);
        when(invocationContainerImpl.findAnswerFor(any())).thenCallRealMethod();
        matchersBinder = mock(MatchersBinder.class);
        mockingProgress = mock(MockingProgress.class);
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
    }

    @Test
    public void testHandle_WithStubbing() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        mockHandler.handle(invocation);
        verify(matchersBinder).bindMatchers(any(), any());
        verify(invocationContainerImpl).setMethodForStubbing(eq(invocationMatcher));
    }

    @Test
    public void testHandle_WithVerificationAndMatchingMock() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        MockAwareVerificationMode mockAwareVerificationMode = mock(MockAwareVerificationMode.class);
        when(matchingMockAwareVerificationMode.getMock()).thenReturn(invocation.getMock());
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(invocationContainerImpl.findAnswerFor(any())).thenReturn(null);
        mockHandler.handle(invocation);
        verify(matchingMockAwareVerificationMode).getMock();
        verify(matchersBinder).bindMatchers(any(), any());
        verify(mockingProgress).validateState();
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(eq(invocationMatcher));
        verify(mockingProgress).reportOngoingStubbing(any());
    }

    @Test
    public void testHandle_WithVerificationAndNotMatchingMock() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        MockAwareVerificationMode mockAwareVerificationMode = mock(MockAwareVerificationMode.class);
        when(matchingMockAwareVerificationMode.getMock()).thenReturn(mock(Object.class));
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(invocationContainerImpl.findAnswerFor(any())).thenReturn(null);
        mockHandler.handle(invocation);
        verify(matchingMockAwareVerificationMode).getMock();
        verify(matchersBinder).bindMatchers(any(), any());
        verify(mockingProgress).validateState();
    }

    @Test
    public void testHandle_WithoutStubbedInvocation() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(invocationContainerImpl.findAnswerFor(any())).thenReturn(null);
        when(mockSettings.getDefaultAnswer().answer(any())).thenReturn(null);
        mockHandler.handle(invocation);
        verify(matchersBinder).bindMatchers(any(), any());
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(eq(invocationMatcher));
        verify(mockingProgress).reportOngoingStubbing(any());
        verify(mockSettings).getDefaultAnswer();
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(eq(invocationMatcher));
    }

    @Test
    public void testVoidMethodStubbable() {
        Object mock = new Object();
        VoidMethodStubbableImpl<Object> expectedVoidMethodStubbable = mock(VoidMethodStubbableImpl.class);
        whenNew(VoidMethodStubbableImpl.class).withArguments(eq(mock), eq(invocationContainerImpl)).thenReturn(expectedVoidMethodStubbable);
        VoidMethodStubbable<Object> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);
        assertEquals(expectedVoidMethodStubbable, voidMethodStubbable);
    }

    @Test
    public void testGetMockSettings() {
        MockSettingsImpl expectedMockSettings = mock(MockSettingsImpl.class);
        when(mockHandler.getMockSettings()).thenReturn(expectedMockSettings);
        MockSettingsImpl mockSettings = mockHandler.getMockSettings();
        assertEquals(expectedMockSettings, mockSettings);
    }

    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = mock(List.class);
        mockHandler.setAnswersForStubbing(answers);
        verify(invocationContainerImpl).setAnswersForStubbing(eq(answers));
    }

    @Test
    public void testGetInvocationContainer() {
        InvocationContainer expectedInvocationContainer = mock(InvocationContainerImpl.class);
        when(mockHandler.getInvocationContainer()).thenReturn(expectedInvocationContainer);
        InvocationContainer invocationContainer = mockHandler.getInvocationContainer();
        assertEquals(expectedInvocationContainer, invocationContainer);
    }
}
