
package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
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
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;

    @Before
    public void setup() {
        mockHandler = new MockHandler<>();
    }

    @Test
    public void testHandle() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        OngoingStubbingImpl<Object> ongoingStubbing = mock(OngoingStubbingImpl.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        Answer<Object> defaultAnswer = mock(Answer.class);

        when(invocationContainer.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        when(invocationContainer.getDefaultAnswer()).thenReturn(defaultAnswer);
        when(defaultAnswer.answer(invocation)).thenReturn("foo");

        mockHandler.invocationContainerImpl = invocationContainer;
        mockHandler.matchersBinder = matchersBinder;

        Object result = mockHandler.handle(invocation);

        verify(invocationContainer).setInvocationForPotentialStubbing(invocationMatcher);
        verify(invocationContainer).resetInvocationForPotentialStubbing(invocationMatcher);
        verify(defaultAnswer).answer(invocation);

        assertEquals("foo", result);
    }

    @Test
    public void testHandle_withStubbedInvocation() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        OngoingStubbingImpl<Object> ongoingStubbing = mock(OngoingStubbingImpl.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        Answer<Object> defaultAnswer = mock(Answer.class);

        when(invocationContainer.findAnswerFor(invocation)).thenReturn(null);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        when(invocationContainer.getDefaultAnswer()).thenReturn(defaultAnswer);
        when(defaultAnswer.answer(invocation)).thenReturn("foo");

        mockHandler.invocationContainerImpl = invocationContainer;
        mockHandler.matchersBinder = matchersBinder;

        Object result = mockHandler.handle(invocation);

        verify(invocationContainer).setInvocationForPotentialStubbing(invocationMatcher);
        verify(invocationContainer).resetInvocationForPotentialStubbing(invocationMatcher);
        verify(defaultAnswer).answer(invocation);

        assertEquals("foo", result);
    }

    @Test
    public void testHandle_withMockAwareVerificationMode() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        OngoingStubbingImpl<Object> ongoingStubbing = mock(OngoingStubbingImpl.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        Answer<Object> defaultAnswer = mock(Answer.class);
        VerificationDataImpl verificationData = mock(VerificationDataImpl.class);
        MockAwareVerificationMode verificationMode = mock(MockAwareVerificationMode.class);

        when(invocationContainer.findAnswerFor(invocation)).thenReturn(null);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        when(invocationContainer.getDefaultAnswer()).thenReturn(defaultAnswer);
        when(defaultAnswer.answer(invocation)).thenReturn("foo");
        when(mockingProgress.pullVerificationMode()).thenReturn((VerificationMode) verificationMode);
        when(verificationMode instanceof MockAwareVerificationMode).thenReturn(true);
        when(verificationMode.getMock()).thenReturn(invocation.getMock());

        mockHandler.invocationContainerImpl = invocationContainer;
        mockHandler.matchersBinder = matchersBinder;

        Object result = mockHandler.handle(invocation);

        verify(invocationContainer).setInvocationForPotentialStubbing(invocationMatcher);
        verify(invocationContainer).resetInvocationForPotentialStubbing(invocationMatcher);
        verify(defaultAnswer).answer(invocation);

        assertEquals("foo", result);
    }

    @Test
    public void testHandle_withMockAwareVerificationMode_differentMock() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        OngoingStubbingImpl<Object> ongoingStubbing = mock(OngoingStubbingImpl.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        Answer<Object> defaultAnswer = mock(Answer.class);
        VerificationDataImpl verificationData = mock(VerificationDataImpl.class);
        MockAwareVerificationMode verificationMode = mock(MockAwareVerificationMode.class);

        when(invocationContainer.findAnswerFor(invocation)).thenReturn(null);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        when(invocationContainer.getDefaultAnswer()).thenReturn(defaultAnswer);
        when(defaultAnswer.answer(invocation)).thenReturn("foo");
        when(mockingProgress.pullVerificationMode()).thenReturn((VerificationMode) verificationMode);
        when(verificationMode instanceof MockAwareVerificationMode).thenReturn(true);
        when(verificationMode.getMock()).thenReturn(mock(Object.class));

        mockHandler.invocationContainerImpl = invocationContainer;
        mockHandler.matchersBinder = matchersBinder;

        Object result = mockHandler.handle(invocation);

        verify(invocationContainer).setInvocationForPotentialStubbing(invocationMatcher);
        verify(invocationContainer).resetInvocationForPotentialStubbing(invocationMatcher);
        verify(defaultAnswer).answer(invocation);

        assertEquals("foo", result);
    }

    @Test
    public void testVoidMethodStubbable() {
        Object mock = mock(Object.class);
        VoidMethodStubbableImpl<Object> voidMethodStubbable = mock(VoidMethodStubbableImpl.class);

        when(voidMethodStubbable.voidMethodStubbable(mock)).thenReturn(voidMethodStubbable);

        VoidMethodStubbable<T> result = mockHandler.voidMethodStubbable(mock);

        verify(voidMethodStubbable).voidMethodStubbable(mock);

        assertEquals(voidMethodStubbable, result);
    }

    @Test
    public void testGetMockSettings() {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        mockHandler.mockSettings = mockSettings;

        MockSettingsImpl result = mockHandler.getMockSettings();

        assertEquals(mockSettings, result);
    }

    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = mock(List.class);
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);

        mockHandler.invocationContainerImpl = invocationContainer;
        mockHandler.setAnswersForStubbing(answers);

        verify(invocationContainer).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer() {
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        mockHandler.invocationContainerImpl = invocationContainer;

        InvocationContainer result = mockHandler.getInvocationContainer();

        assertEquals(invocationContainer, result);
    }
}
