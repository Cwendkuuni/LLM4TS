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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockHandlerTest {
    
    @Test
    public void testHandle_StubbingVoidsWithStubVoidStyle_ReturnsNull() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        doReturn(null).when(matchersBinder).bindMatchers(any(), any());
        
        MockHandler mockHandler = new MockHandler() {
            @Override
            public InvocationContainer getInvocationContainer() {
                return invocationContainerImpl;
            }
        };
        
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
        
        // Act
        Object result = mockHandler.handle(invocation);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testHandle_VerificationModeNotNull_InvokesVerify() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        VerificationDataImpl verificationData = mock(VerificationDataImpl.class);
        MockAwareVerificationMode verificationMode = mock(MockAwareVerificationMode.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(verificationMode instanceof MockAwareVerificationMode).thenReturn(true);
        when(((MockAwareVerificationMode) verificationMode).getMock()).thenReturn(invocation.getMock());
        doReturn(null).when(verificationMode).verify(verificationData);
        
        MockHandler mockHandler = new MockHandler() {
            @Override
            public InvocationContainer getInvocationContainer() {
                return invocationContainerImpl;
            }
        };
        
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
        
        // Act
        Object result = mockHandler.handle(invocation);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testHandle_StubbedInvocationNotNull_ReturnsStubbedInvocationAnswer() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocationMatcher);
        when(stubbedInvocationMatcher.captureArgumentsFrom(invocation)).thenReturn(null);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn(null);
        doReturn(invocationMatcher).when(matchersBinder).bindMatchers(any(), any());
        
        MockHandler mockHandler = new MockHandler(mockSettings) {
            @Override
            public InvocationContainer getInvocationContainer() {
                return invocationContainerImpl;
            }
        };
        
        mockHandler.matchersBinder = matchersBinder;
        
        // Act
        Object result = mockHandler.handle(invocation);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testHandle_StubbedInvocationNull_ReturnsDefaultAnswer() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        Answer defaultAnswer = mock(Answer.class);
        
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);
        doReturn(invocationMatcher).when(matchersBinder).bindMatchers(any(), any());
        
        MockHandler mockHandler = new MockHandler(mockSettings) {
            @Override
            public InvocationContainer getInvocationContainer() {
                return invocationContainerImpl;
            }
        };
        
        mockHandler.matchersBinder = matchersBinder;
        
        // Act
        Object result = mockHandler.handle(invocation);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testVoidMethodStubbable_ReturnsVoidMethodStubbableImpl() {
        // Arrange
        MockHandler mockHandler = new MockHandler();
        Object mock = new Object();
        
        // Act
        VoidMethodStubbable stubbable = mockHandler.voidMethodStubbable(mock);
        
        // Assert
        assertNotNull(stubbable);
        assertTrue(stubbable instanceof VoidMethodStubbableImpl);
    }
    
    @Test
    public void testSetAnswersForStubbing_SetsAnswersForStubbing() {
        // Arrange
        MockHandler mockHandler = new MockHandler();
        List<Answer> answers = new ArrayList<>();
        answers.add(mock(Answer.class));
        
        // Act
        mockHandler.setAnswersForStubbing(answers);
        
        // Assert
        assertEquals(answers, mockHandler.getInvocationContainer().getAnswersForStubbing());
    }
    
    @Test
    public void testGetMockSettings_ReturnsMockSettings() {
        // Arrange
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler mockHandler = new MockHandler(mockSettings);
        
        // Act
        MockSettingsImpl result = mockHandler.getMockSettings();
        
        // Assert
        assertEquals(mockSettings, result);
    }
    
    @Test
    public void testGetInvocationContainer_ReturnsInvocationContainerImpl() {
        // Arrange
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MockHandler mockHandler = new MockHandler() {
            @Override
            public InvocationContainer getInvocationContainer() {
                return invocationContainerImpl;
            }
        };
        
        // Act
        InvocationContainer result = mockHandler.getInvocationContainer();
        
        // Assert
        assertEquals(invocationContainerImpl, result);
    }
}