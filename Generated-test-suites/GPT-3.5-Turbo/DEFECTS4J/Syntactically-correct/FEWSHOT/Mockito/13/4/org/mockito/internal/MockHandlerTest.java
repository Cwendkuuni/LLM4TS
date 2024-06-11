
package org.mockito.internal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

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
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {

    private MockSettingsImpl mockSettings;
    private MockHandler<ExampleClass> mockHandler;

    @Before
    public void setUp() {
        mockSettings = new MockSettingsImpl();
        mockHandler = new MockHandler<>(mockSettings);
    }

    @Test
    public void testHandle_WhenInvocationContainerHasAnswersForStubbing_ShouldSetMethodForStubbing() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        Answer<Void> answer = mock(Answer.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(invocationContainerImpl.setMethodForStubbing(invocationMatcher)).thenReturn(null);
        
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
        
        // Act
        Object result = mockHandler.handle(invocation);
        
        // Assert
        verify(invocationContainerImpl).hasAnswersForStubbing();
        verify(matchersBinder).bindMatchers(any(), any());
        verify(invocationContainerImpl).setMethodForStubbing(invocationMatcher);
        assertNull(result);
    }
    
    @Test
    public void testHandle_WhenVerificationModeIsNotNullAndMockAware_ShouldVerifyInvocationData() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        Answer<Void> answer = mock(Answer.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        VerificationMode verificationMode = mock(MockAwareVerificationMode.class);

        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(invocation.getMock()).thenReturn(mock(ExampleClass.class));
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        when(invocationContainerImpl.setInvocationForPotentialStubbing(invocationMatcher)).thenReturn(null);
        
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
        
        // Act
        Object result = mockHandler.handle(invocation);
        
        // Assert
        verify(mockingProgress).pullVerificationMode();
        verify(invocationContainerImpl).findAnswerFor(invocation);
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
        verifyZeroInteractions(matchersBinder);
        verifyZeroInteractions(invocationContainerImpl);
        verifyZeroInteractions(verificationMode);
        verifyZeroInteractions(answer);
        assertNull(result);
    }
    
    @Test
    public void testHandle_WhenStubbedInvocationNotNull_ShouldReturnAnsweredValue() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        Answer<Void> answer = mock(Answer.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);

        when(mockSettings.getDefaultAnswer()).thenReturn(answer);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocationMatcher);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn("AnsweredValue");

        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
        
        // Act
        Object result = mockHandler.handle(invocation);
        
        // Assert
        verify(mockSettings).getDefaultAnswer();
        verify(invocationContainerImpl).findAnswerFor(invocation);
        verify(stubbedInvocationMatcher).captureArgumentsFrom(invocation);
        verify(stubbedInvocationMatcher).answer(invocation);
        verifyZeroInteractions(matchersBinder);
        verifyZeroInteractions(invocationContainerImpl);
        verifyZeroInteractions(mockingProgress);
        verifyZeroInteractions(answer);
        assertEquals("AnsweredValue", result);
    }
    
    @Test
    public void testHandle_WhenStubbedInvocationIsNull_ShouldReturnDefaultAnswer() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        Answer<Void> answer = mock(Answer.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);

        when(mockSettings.getDefaultAnswer()).thenReturn(answer);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        when(answer.answer(invocation)).thenReturn("DefaultAnswer");

        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
        
        // Act
        Object result = mockHandler.handle(invocation);
        
        // Assert
        verify(mockSettings).getDefaultAnswer();
        verify(invocationContainerImpl).findAnswerFor(invocation);
        verifyZeroInteractions(stubbedInvocationMatcher);
        verifyZeroInteractions(matchersBinder);
        verifyZeroInteractions(invocationContainerImpl);
        verifyZeroInteractions(mockingProgress);
        assertEquals("DefaultAnswer", result);
    }
    
    @Test
    public void testVoidMethodStubbable_ShouldInitializeAndReturnVoidMethodStubbable() {
        // Arrange
        ExampleClass mock = mock(ExampleClass.class);
        VoidMethodStubbableImpl<VoidMethodStubbable> expected = mock(VoidMethodStubbableImpl.class);
        
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        
        when(invocationContainerImpl.voidMethodStubbable(mock)).thenReturn(expected);
        
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        
        // Act
        VoidMethodStubbable<VoidMethodStubbable> result = mockHandler.voidMethodStubbable(mock);
        
        // Assert
        verify(invocationContainerImpl).voidMethodStubbable(mock);
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetMockSettings_ShouldReturnMockSettings() {
        // Arrange & Act
        MockSettingsImpl result = mockHandler.getMockSettings();
        
        // Assert
        assertEquals(mockSettings, result);
    }
    
    @Test
    public void testSetAnswersForStubbing_ShouldSetAnswersForStubbing() {
        // Arrange
        List<Answer> answers = mock(List.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        
        // Act
        mockHandler.setAnswersForStubbing(answers);
        
        // Assert
        verify(invocationContainerImpl).setAnswersForStubbing(answers);
    }
    
    @Test
    public void testGetInvocationContainer_ShouldReturnInvocationContainer() {
        // Arrange
        InvocationContainerImpl expected = mock(InvocationContainerImpl.class);
        
        mockHandler.invocationContainerImpl = expected;
        
        // Act
        InvocationContainer result = mockHandler.getInvocationContainer();
        
        // Assert
        assertEquals(expected, result);
    }
}
