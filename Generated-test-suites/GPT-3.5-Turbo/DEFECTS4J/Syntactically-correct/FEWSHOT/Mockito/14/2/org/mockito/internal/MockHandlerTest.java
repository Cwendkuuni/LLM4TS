package org.mockito.internal;

import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;
import java.util.List;

import static org.mockito.Mockito.*;

public class MockHandlerTest {


    @Test
    public void testHandle_whenInvocationContainerHasAnswersForStubbing_thenReturnNull() throws Throwable {
        //Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);

        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(mock(InvocationMatcher.class));
        doNothing().when(invocationContainerImpl).setMethodForStubbing(any());

        MockHandler mockHandler = new MockHandler(mockSettings);
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;

        //Act
        Object result = mockHandler.handle(invocation);

        //Assert
        assertNull(result);
        verify(invocationContainerImpl, times(1)).hasAnswersForStubbing();
        verify(matchersBinder, times(1)).bindMatchers(any(), any());
        verify(invocationContainerImpl, times(1)).setMethodForStubbing(any());
    }

    @Test
    public void testHandle_whenVerificationModeNotNull_thenVerifyInvocationData() throws Throwable {
        //Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        VerificationDataImpl verificationData = mock(VerificationDataImpl.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);

        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(mock(InvocationMatcher.class));
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(null);
        when(invocationContainerImpl.getInvocations()).thenReturn(null);
        when(verificationMode.verify(any())).thenReturn(null);

        MockHandler mockHandler = new MockHandler(mockSettings);
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;

        //Act
        Object result = mockHandler.handle(invocation);

        //Assert
        assertNull(result);
        verify(verificationData, times(1)).verify(any());
        verify(mockingProgress, times(1)).pullVerificationMode();
        verify(matchersBinder, times(1)).bindMatchers(any(), any());
    }

    @Test
    public void testHandle_whenStubbedInvocationNotNull_thenCaptureArgumentsAndAnswerInvocation() throws Throwable {
        //Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);

        when(invocationContainerImpl.findAnswerFor(any())).thenReturn(stubbedInvocationMatcher);
        when(stubbedInvocationMatcher.captureArgumentsFrom(any())).thenReturn(null);
        when(stubbedInvocationMatcher.answer(any())).thenReturn(null);

        MockHandler mockHandler = new MockHandler(mockSettings);
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;

        //Act
        Object result = mockHandler.handle(invocation);

        //Assert
        assertNull(result);
        verify(invocationContainerImpl, times(1)).findAnswerFor(any());
        verify(stubbedInvocationMatcher, times(1)).captureArgumentsFrom(any());
        verify(stubbedInvocationMatcher, times(1)).answer(any());
    }

    @Test
    public void testHandle_whenStubbedInvocationNull_thenReturnMockSettingsDefaultAnswerInvocation() throws Throwable {
        //Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        Answer answer = mock(Answer.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);

        when(invocationContainerImpl.findAnswerFor(any())).thenReturn(null);
        when(mockSettings.getDefaultAnswer()).thenReturn(answer);
        when(answer.answer(any())).thenReturn(null);
        doNothing().when(invocationContainerImpl).resetInvocationForPotentialStubbing(any());

        MockHandler mockHandler = new MockHandler(mockSettings);
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;

        //Act
        Object result = mockHandler.handle(invocation);

        //Assert
        assertNull(result);
        verify(invocationContainerImpl, times(1)).findAnswerFor(any());
        verify(mockSettings, times(1)).getDefaultAnswer();
        verify(answer, times(1)).answer(any());
        verify(invocationContainerImpl, times(1)).resetInvocationForPotentialStubbing(any());
    }

    @Test
    public void testVoidMethodStubbable() {
        //Arrange
        Object mock = mock(Object.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        VoidMethodStubbableImpl stubbable = new VoidMethodStubbableImpl(mock, invocationContainerImpl);//creating the instance not necessary 
        VoidMethodStubbableImpl mockStubbable = spy(stubbable);
        when(mockStubbable.getMethodName()).thenReturn("");
        doNothing().when(mockStubbable).validateForSetting();

        //Act
        VoidMethodStubbable result = mockStubbable.voidMethodStubbable(mock);

        //Assert
        assertNotNull(result);
        verify(mockStubbable, times(1)).validateForSetting();
        verify(mockStubbable, times(1)).reset();
        verify(mockStubbable, times(1)).setInvocationForPotentialStubbing(any());
    }

    @Test
    public void testSetAnswersForStubbing() {
        //Arrange
        List<Answer> answers = mock(List.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MockHandler mockHandler = new MockHandler();
        mockHandler.invocationContainerImpl = invocationContainerImpl;

        //Act
        mockHandler.setAnswersForStubbing(answers);
        List<Answer> result = invocationContainerImpl.getAnswersForStubbing();

        //Assert
        assertEquals(answers, result);
    }

}