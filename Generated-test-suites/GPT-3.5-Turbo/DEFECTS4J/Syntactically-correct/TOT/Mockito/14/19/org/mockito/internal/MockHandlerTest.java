package org.mockito.internal;

import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class MockHandlerTest {

    @Test
    public void testHandle_WhenStubbingVoidsWithStubVoidOrDoAnswerStyle_ShouldInvokeMethodAndReturnNull() throws Throwable {
        // Arrange
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        Answer defaultAnswer = mock(Answer.class);
        Answer invocationAnswer = mock(Answer.class);

        when(invocationContainer.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(invocationMatcher.getMethod()).thenReturn(null);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);
        when(invocationContainer.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(stubbedInvocation.answer(invocation)).thenReturn(null);
        when(mockSettings.getDefaultAnswer()).thenReturn(invocationAnswer);

        MockHandler mockHandler = new MockHandler(invocationContainer, matchersBinder, mockingProgress, mockSettings);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        verify(invocationContainer).setMethodForStubbing(invocationMatcher);
        verifyZeroInteractions(invocation);
        verify(mockingProgress).validateState();
        verify(invocationContainer).setInvocationForPotentialStubbing(invocationMatcher);
        verify(invocationContainer).resetInvocationForPotentialStubbing(invocationMatcher);

        verifyNoMoreInteractions(invocationContainer, matchersBinder, mockingProgress, invocationMatcher, mockSettings, stubbedInvocation, defaultAnswer, invocationAnswer);
    }

    @Test
    public void testHandle_WhenVerificationModeNotNullAndVerificationStartedOnCorrectMock_ShouldVerifyData() throws Throwable {
        // Arrange
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        VerificationDataImpl verificationData = mock(VerificationDataImpl.class);
        VerificationMode verificationMode = mock(VerificationMode.class);

        when(invocationContainer.getInvocations()).thenReturn(new ArrayList<>());
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(invocationMatcher.getMethod()).thenReturn(null);

        MockHandler mockHandler = new MockHandler(invocationContainer, matchersBinder, mockingProgress, mockSettings);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        verify(mockingProgress).pullVerificationMode();
        verify(invocationContainer).getInvocations();
        verify(matchersBinder).bindMatchers(any(), any());
        verify(verificationMode).verify(verificationData);

        verifyNoMoreInteractions(invocationContainer, matchersBinder, mockingProgress, invocation, invocationMatcher, verificationMode);
    }

    @Test
    public void testHandle_WhenStubbedInvocationNotNull_ShouldReturnAnswerResultAndCaptureArguments() throws Throwable {
        // Arrange
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        Object answerResult = mock(Object.class);

        when(invocationContainer.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(stubbedInvocation.answer(invocation)).thenReturn(answerResult);

        MockHandler mockHandler = new MockHandler(invocationContainer, matchersBinder, mockingProgress, mockSettings);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        verify(invocationContainer).findAnswerFor(invocation);
        verify(stubbedInvocation).answer(invocation);
        verify(stubbedInvocation).captureArgumentsFrom(invocation);

        verifyNoMoreInteractions(invocationContainer, matchersBinder, mockingProgress, invocation, invocationMatcher, stubbedInvocation);
    }

    @Test
    public void testHandle_WhenNoStubbedInvocationAndDefaultAnswerNotNull_ShouldReturnDefaultAnswerResult_AndResetInvocationForPotentialStubbing() throws Throwable {
        // Arrange
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        Object defaultAnswerResult = mock(Object.class);

        when(invocationContainer.findAnswerFor(invocation)).thenReturn(null);
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);
        when(defaultAnswer.answer(invocation)).thenReturn(defaultAnswerResult);

        MockHandler mockHandler = new MockHandler(invocationContainer, matchersBinder, mockingProgress, mockSettings);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        verify(invocationContainer).findAnswerFor(invocation);
        verify(mockSettings).getDefaultAnswer();
        verify(defaultAnswer).answer(invocation);
        verify(invocationContainer).resetInvocationForPotentialStubbing(invocationMatcher);

        verifyNoMoreInteractions(invocationContainer, matchersBinder, mockingProgress, invocation, invocationMatcher, stubbedInvocation);
    }

    @Test
    public void testVoidMethodStubbable_ShouldReturnNewVoidMethodStubbableImplObject() throws Throwable {
        // Arrange
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        MockHandler<T> mockHandler = new MockHandler<T>(invocationContainer);

        // Act
        VoidMethodStubbable<T> result = mockHandler.voidMethodStubbable(mock);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof VoidMethodStubbableImpl);
    }

    @Test
    public void testGetMockSettings_ShouldReturnMockSettingsObject() throws Throwable {
        // Arrange
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        MockHandler<T> mockHandler = new MockHandler<T>(invocationContainer);

        // Act
        MockSettingsImpl result = mockHandler.getMockSettings();

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof MockSettingsImpl);
    }

    @Test
    public void testSetAnswersForStubbing_ShouldSetAnswersForStubbingInInvocationContainerObject() throws Throwable {
        // Arrange
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        List<Answer> answers = new ArrayList<>();
        MockHandler<T> mockHandler = new MockHandler<T>(invocationContainer);

        // Act
        mockHandler.setAnswersForStubbing(answers);

        // Assert
        verify(invocationContainer).setAnswersForStubbing(answers);
        verifyNoMoreInteractions(invocationContainer);
    }

    @Test
    public void testGetInvocationContainer_ShouldReturnInvocationContainerObject() throws Throwable {
        // Arrange
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        MockHandler<T> mockHandler = new MockHandler<T>(invocationContainer);

        // Act
        InvocationContainer result = mockHandler.getInvocationContainer();

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof InvocationContainerImpl);
    }
}