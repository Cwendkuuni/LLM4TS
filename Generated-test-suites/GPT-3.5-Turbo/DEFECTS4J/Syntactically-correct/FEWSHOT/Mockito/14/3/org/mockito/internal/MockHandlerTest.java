package org.mockito.internal;

import static org.mockito.Mockito.*;
import org.mockito.invocation.Invocation;
import org.mockito.verification.VerificationDataImpl;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbingImpl;
import java.util.List;
import org.mockito.verification.VerificationMode;
import org.mockito.stubbing.Answer;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.invocation.InvocationContainerImpl;

public class MockHandlerTest {
    
    @Test
    public void testHandle_answerNotNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        VerificationMode verificationMode = mock(VerificationMode.class);        
        ThreadSafeMockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        Answer<Object> defaultAnswer = mock(Answer.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        OngoingStubbingImpl<MockHandlerInterface> ongoingStubbing = mock(OngoingStubbingImpl.class);        
        List<Answer> answers = mock(List.class);
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        
        MockHandler<MockHandlerInterface> mockHandler = new MockHandler<>(new MockHandlerInterface<Test>());
        mockHandler.invocationContainerImpl = invocationContainer;
        mockHandler.mockSettings.defaultAnswer(defaultAnswer);
        
        when(invocationContainer.hasAnswersForStubbing()).thenReturn(true);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(invocationMatcher.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(answers.get(anyInt())).thenReturn(null);
        when(invocationContainer.findAnswerFor(invocation)).thenReturn(null);
        when(mockHandler.voidMethodStubbable(any())).thenReturn(null);
        
        mockHandler.handle(invocation);
        
        verify(invocationContainer).setMethodForStubbing(invocationMatcher);
    }

    @Test
    public void testHandle_verificationModeNotNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        VerificationMode verificationMode = mock(VerificationMode.class);        
        ThreadSafeMockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        Answer<Object> defaultAnswer = mock(Answer.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        OngoingStubbingImpl<MockHandlerInterface> ongoingStubbing = mock(OngoingStubbingImpl.class);        
        List<Answer> answers = mock(List.class);
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        
        MockHandler<MockHandlerInterface> mockHandler = new MockHandler<>(new MockHandlerInterface<Test>());
        mockHandler.invocationContainerImpl = invocationContainer;
        mockHandler.mockSettings.defaultAnswer(defaultAnswer);
        
        when(invocationContainer.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(invocationMatcher.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(answers.get(anyInt())).thenReturn(null);
        when(invocationContainer.findAnswerFor(invocation)).thenReturn(null);
        when(mockHandler.voidMethodStubbable(any())).thenReturn(null);
        
        mockHandler.handle(invocation);
        
        verify(verificationMode).verify(any());
    }
    
    @Test
    public void testHandle_stubbedInvocationNotNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        VerificationMode verificationMode = mock(VerificationMode.class);        
        ThreadSafeMockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        Answer<Object> defaultAnswer = mock(Answer.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        OngoingStubbingImpl<MockHandlerInterface> ongoingStubbing = mock(OngoingStubbingImpl.class);        
        List<Answer> answers = mock(List.class);
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        
        MockHandler<MockHandlerInterface> mockHandler = new MockHandler<>(new MockHandlerInterface<Test>());
        mockHandler.invocationContainerImpl = invocationContainer;
        mockHandler.mockSettings.defaultAnswer(defaultAnswer);
        
        when(invocationContainer.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(invocationMatcher.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(answers.get(anyInt())).thenReturn(null);
        when(invocationContainer.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(stubbedInvocation.answer(invocation)).thenReturn(null);
        
        mockHandler.handle(invocation);
        
        verify(stubbedInvocation).captureArgumentsFrom(invocation);
    }
    
    @Test
    public void testHandle_stubbedInvocationNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        VerificationMode verificationMode = mock(VerificationMode.class);        
        ThreadSafeMockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        Answer<Object> defaultAnswer = mock(Answer.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        OngoingStubbingImpl<MockHandlerInterface> ongoingStubbing = mock(OngoingStubbingImpl.class);        
        List<Answer> answers = mock(List.class);
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        
        MockHandler<MockHandlerInterface> mockHandler = new MockHandler<>(new MockHandlerInterface<Test>());
        mockHandler.invocationContainerImpl = invocationContainer;
        mockHandler.mockSettings.defaultAnswer(defaultAnswer);
        
        when(invocationContainer.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(invocationMatcher.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(answers.get(anyInt())).thenReturn(null);
        when(invocationContainer.findAnswerFor(invocation)).thenReturn(null);
        when(defaultAnswer.answer(invocation)).thenReturn(null);
        
        mockHandler.handle(invocation);
        
        verify(invocationContainer).resetInvocationForPotentialStubbing(invocationMatcher);
    }
    
    @Test
    public void testVoidMethodStubbable() {
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        
        MockHandler<MockHandlerInterface> mockHandler = new MockHandler<>(new MockHandlerInterface<Test>());
        mockHandler.invocationContainerImpl = invocationContainer;
        
        mockHandler.voidMethodStubbable(new MockHandlerInterface<Test>());
        
        verify(invocationContainer).resetInvocationForPotentialStubbing(any());
    }
    
    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = mock(List.class);
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        
        MockHandler<MockHandlerInterface> mockHandler = new MockHandler<>(new MockHandlerInterface<Test>());
        mockHandler.invocationContainerImpl = invocationContainer;
        
        mockHandler.setAnswersForStubbing(answers);
        
        verify(invocationContainer).setAnswersForStubbing(answers);
    }
    
    @Test
    public void testGetInvocationContainer() {
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        
        MockHandler<MockHandlerInterface> mockHandler = new MockHandler<>(new MockHandlerInterface<Test>());
        mockHandler.invocationContainerImpl = invocationContainer;
        
        mockHandler.getInvocationContainer();
        
        verify(invocationContainer).setAnswersForStubbing(any());
    }
}