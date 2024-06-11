import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;


public class MockHandlerTest {
    
    private MockHandler<Object> mockHandler;
    
    @Mock
    private MockSettingsImpl mockSettings;
    
    @Mock
    private InvocationContainerImpl invocationContainerImpl;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockHandler = new MockHandler<>(mockSettings);
        this.mockHandler.invocationContainerImpl = invocationContainerImpl;
    }

    @Test
    public void testHandle_WhenAnswersForStubbingExist_ShouldBindInvocationMatcherAndReturnNull() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Answer<Object> answer = mock(Answer.class);
        List<Answer> answers = new ArrayList<>();
        answers.add(answer);
        mockHandler.setAnswersForStubbing(answers);
        
        // Act
        Object result = mockHandler.handle(invocation);
        
        // Assert
        verify(invocationContainerImpl).setMethodForStubbing(any());
        verifyNoMoreInteractions(invocationContainerImpl);
        assertNull(result);
    }
    
    @Test
    public void testHandle_WhenVerificationModeExists_ShouldVerifyVerificationDataAndReturnNull() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        mockHandler.mockingProgress.setVerificationMode(verificationMode);
        
        // Act
        Object result = mockHandler.handle(invocation);
        
        // Assert
        verifyZeroInteractions(invocationContainerImpl);
        verify(mockHandler.mockingProgress).pullVerificationMode();
        verify(mockHandler.matchersBinder).bindMatchers(any(), any());
        verify(mockHandler.mockingProgress).validateState();
        verify(mockHandler.mockingProgress).getArgumentMatcherStorage();
        verify(mockHandler.invocationContainerImpl).getInvocations();
        verify(mockHandler.invocationContainerImpl).findAnswerFor(any());
        verify(verificationMode).verify(any());
        assertNull(result);
    }
    
    @Test
    public void testHandle_WhenStubbedInvocationExists_ShouldCaptureArgumentsAndReturnAnswerResult() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Answer<Object> answer = mock(Answer.class);
        List<Answer> answers = new ArrayList<>();
        answers.add(answer);
        mockHandler.setAnswersForStubbing(answers);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(stubbedInvocation.answer(invocation)).thenReturn("AnswerResult");
        
        // Act
        Object result = mockHandler.handle(invocation);
        
        // Assert
        verify(invocationContainerImpl).setMethodForStubbing(any());
        verify(mockHandler.mockingProgress).reportOngoingStubbing(any());
        verify(mockHandler.invocationContainerImpl).findAnswerFor(invocation);
        verify(stubbedInvocation).answer(invocation);
        verify(stubbedInvocation).captureArgumentsFrom(invocation);
        assertNull(result);
    }
    
    @Test
    public void testHandle_WhenStubbedInvocationDoesNotExist_ShouldReturnDefaultAnswerResult() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Answer<Object> defaultAnswer = mock(Answer.class);
        mockSettings.setDefaultAnswer(defaultAnswer);
        when(defaultAnswer.answer(invocation)).thenReturn("DefaultAnswerResult");
        
        // Act
        Object result = mockHandler.handle(invocation);
        
        // Assert
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(any());
        verify(mockHandler.mockingProgress).pullVerificationMode();
        verify(mockHandler.matchersBinder).bindMatchers(any(), any());
        verify(mockHandler.mockingProgress).validateState();
        verify(mockHandler.invocationContainerImpl).resetInvocationForPotentialStubbing(any());
        assertNull(result);
    }
    
    @Test
    public void testVoidMethodStubbable_ShouldCreateAndReturnVoidMethodStubbable() {
        // Arrange
        Object mock = new Object();
        
        // Act
        VoidMethodStubbable<Object> result = mockHandler.voidMethodStubbable(mock);
        
        // Assert
        assertNotNull(result);
        assertEquals(mock, result.getMock());
        assertEquals(invocationContainerImpl, result.getInvocationContainer());
    }
    
    @Test
    public void testGetMockSettings_ShouldReturnMockSettings() {
        // Act
        MockSettingsImpl result = mockHandler.getMockSettings();
        
        // Assert
        assertEquals(mockSettings, result);
    }
    
    @Test
    public void testSetAnswersForStubbing_ShouldSetAnswersForStubbing() {
        // Arrange
        Answer<Object> answer = mock(Answer.class);
        List<Answer> answers = new ArrayList<>();
        answers.add(answer);
        
        // Act
        mockHandler.setAnswersForStubbing(answers);
        
        // Assert
        verify(invocationContainerImpl).setAnswersForStubbing(answers);
    }
    
    @Test
    public void testGetInvocationContainer_ShouldReturnInvocationContainer() {
        // Act
        InvocationContainer result = mockHandler.getInvocationContainer();
        
        // Assert
        assertEquals(invocationContainerImpl, result);
    }
}