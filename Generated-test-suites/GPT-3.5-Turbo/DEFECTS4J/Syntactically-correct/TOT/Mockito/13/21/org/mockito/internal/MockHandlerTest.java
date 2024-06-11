import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.stubbing.VoidMethodStubbableImpl;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.*;

public class MockHandlerTest {

    private MockHandler<SomeClassToMock> mockHandler;
    private SomeClassToMock mockObject;

    @Before
    public void setup() {
        mockObject = mock(SomeClassToMock.class);
        mockHandler = new MockHandler<>(mockObject);
    }
    
    @Test
    public void testHandle_WithStubbedInvocation_ReturnsStubbedAnswer() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        
        when(invocationContainer.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(stubbedInvocation.answer(invocation)).thenReturn("Stubbed Answer");
        
        mockHandler.invocationContainerImpl = invocationContainer;
        
        Object result = mockHandler.handle(invocation);
        
        verify(stubbedInvocation).captureArgumentsFrom(invocation);
        verify(stubbedInvocation).answer(invocation);
        verify(invocationContainer).findAnswerFor(invocation);
        
        assertEquals("Stubbed Answer", result);
    }
    
    @Test
    public void testHandle_WithoutStubbedInvocation_ReturnsDefaultAnswer() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        Answer defaultAnswer = mock(Answer.class);
        
        when(mockHandler.getMockSettings().getDefaultAnswer()).thenReturn(defaultAnswer);
        when(defaultAnswer.answer(invocation)).thenReturn("Default Answer");
        when(invocationContainer.findAnswerFor(invocation)).thenReturn(null);
        
        mockHandler.invocationContainerImpl = invocationContainer;
        
        Object result = mockHandler.handle(invocation);
        
        verify(mockHandler.getMockSettings()).getDefaultAnswer();
        verify(defaultAnswer).answer(invocation);
        verify(invocationContainer).findAnswerFor(invocation);
        verify(invocationContainer).resetInvocationForPotentialStubbing(invocationMatcher);
        
        assertEquals("Default Answer", result);
    }
    
    @Test
    public void testHandle_WithVerificationModeAndCorrectMock_VerifiesData() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        VerificationDataImpl verificationData = mock(VerificationDataImpl.class);
        MockAwareVerificationMode mockAwareVerificationMode = mock(MockAwareVerificationMode.class);
        
        mockHandler.mockingProgress = mockingProgress;
        
        when(invocation.getMock()).thenReturn(mockObject);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(verificationMode instanceof MockAwareVerificationMode).thenReturn(true);
        when(((MockAwareVerificationMode) verificationMode).getMock()).thenReturn(mockObject);
        when(invocationContainer.findAnswerFor(invocation)).thenReturn(null);
        
        mockHandler.invocationContainerImpl = invocationContainer;
        
        mockHandler.handle(invocation);
        
        verify(mockingProgress, times(1)).pullVerificationMode();
        verify(verificationMode).verify(any(VerificationDataImpl.class));
    }
    
    @Test
    public void testHandle_WithVerificationModeAndDifferentMock_VerificationModeNotExecuted() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        VerificationDataImpl verificationData = mock(VerificationDataImpl.class);
        MockAwareVerificationMode mockAwareVerificationMode = mock(MockAwareVerificationMode.class);
        SomeClassToMock differentMock = mock(SomeClassToMock.class);
        
        mockHandler.mockingProgress = mockingProgress;
        
        when(invocation.getMock()).thenReturn(differentMock);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(verificationMode instanceof MockAwareVerificationMode).thenReturn(true);
        when(((MockAwareVerificationMode) verificationMode).getMock()).thenReturn(mockObject);
        when(invocationContainer.findAnswerFor(invocation)).thenReturn(null);
        
        mockHandler.invocationContainerImpl = invocationContainer;
        
        mockHandler.handle(invocation);
        
        verify(mockingProgress, times(1)).pullVerificationMode();
        verify(verificationMode, never()).verify(any(VerificationDataImpl.class));
    }
    
    @Test
    public void testHandle_WithoutVerificationMode_CallsSetInvocationForPotentialStubbing() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        VerificationDataImpl verificationData = mock(VerificationDataImpl.class);
        MockAwareVerificationMode mockAwareVerificationMode = mock(MockAwareVerificationMode.class);
        
        mockHandler.mockingProgress = mockingProgress;
        
        when(invocation.getMock()).thenReturn(mockObject);
        when(invocationContainer.findAnswerFor(invocation)).thenReturn(null);
        
        mockHandler.invocationContainerImpl = invocationContainer;
        
        mockHandler.handle(invocation);
        
        verify(invocationContainer).setInvocationForPotentialStubbing(invocationMatcher);
    }
    
    @Test
    public void testHandle_WithAnswersForStubbing_CallsSetAnswersForStubbing() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        VerificationDataImpl verificationData = mock(VerificationDataImpl.class);
        MockAwareVerificationMode mockAwareVerificationMode = mock(MockAwareVerificationMode.class);
        List<Answer> answers = Arrays.asList(mock(Answer.class), mock(Answer.class));
        
        mockHandler.mockingProgress = mockingProgress;
        
        when(invocation.getMock()).thenReturn(mockObject);
        when(invocationContainer.findAnswerFor(invocation)).thenReturn(null);
        
        mockHandler.invocationContainerImpl = invocationContainer;
        
        mockHandler.setAnswersForStubbing(answers);
        
        verify(invocationContainer).setAnswersForStubbing(answers);
    }
    
    @Test
    public void testVoidMethodStubbable_ReturnsVoidMethodStubbableImpl() {
        VoidMethodStubbable stubbable = mockHandler.voidMethodStubbable(mockObject);
        
        assertTrue(stubbable instanceof VoidMethodStubbableImpl);
    }
    
    @Test
    public void testGetMockSettings_ReturnsMockSettings() {
        MockSettingsImpl settings = mockHandler.getMockSettings();
        
        assertNotNull(settings);
        assertTrue(settings instanceof MockSettingsImpl);
    }
    
    @Test
    public void testGetInvocationContainer_ReturnsInvocationContainer() {
        InvocationContainer container = mockHandler.getInvocationContainer();
        
        assertNotNull(container);
        assertTrue(container instanceof InvocationContainerImpl);
    }
    
    // Edge Cases
    
    @Test
    public void testHandle_WithNoInvocationContainerImpl_ThrowsException() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        
        mockHandler.invocationContainerImpl = null;
        
        assertThrows(NullPointerException.class, () -> {
            mockHandler.handle(invocation);
        });
    }
    
    @Test
    public void testHandle_WithNoDefaultAnswer_ThrowsException() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        
        mockHandler.mockSettings = null;
        
        assertThrows(NullPointerException.class, () -> {
            mockHandler.handle(invocation);
        });
    }
}