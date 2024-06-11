import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
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
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {
    
    private MockHandler<Object> mockHandler;
    private InvocationContainer mockInvocationContainer;
    private MatchersBinder mockMatchersBinder;
    private MockingProgress mockMockingProgress;
    private MockSettingsImpl mockMockSettings;
    
    @Before
    public void setUp() {
        mockInvocationContainer = mock(InvocationContainer.class);
        mockMatchersBinder = mock(MatchersBinder.class);
        mockMockingProgress = mock(ThreadSafeMockingProgress.class);
        mockMockSettings = mock(MockSettingsImpl.class);
        
        mockHandler = new MockHandler<>(mockMockSettings);
        mockHandler.invocationContainerImpl = mockInvocationContainer;
        mockHandler.matchersBinder = mockMatchersBinder;
        mockHandler.mockingProgress = mockMockingProgress;
    }
    
    @Test
    public void testHandle_withStubbingAnswersForStubbing_shouldSetMethodForStubbingAndReturnNull() throws Throwable {
        Invocation mockInvocation = mock(Invocation.class);
        InvocationMatcher mockInvocationMatcher = mock(InvocationMatcher.class);
        
        when(mockInvocationContainer.hasAnswersForStubbing()).thenReturn(true);
        when(mockMatchersBinder.bindMatchers(any(), eq(mockInvocation)))
            .thenReturn(mockInvocationMatcher);
        
        Object result = mockHandler.handle(mockInvocation);
        
        verify(mockInvocationContainer).setMethodForStubbing(mockInvocationMatcher);
        assertNull(result);
    }
    
    @Test
    public void testHandle_withVerificationModeNonNull_shouldVerifyInvocation() throws Throwable {
        Invocation mockInvocation = mock(Invocation.class);
        InvocationMatcher mockInvocationMatcher = mock(InvocationMatcher.class);
        VerificationMode mockVerificationMode = mock(VerificationMode.class);
        OngoingStubbingImpl<Object> mockOngoingStubbing = mock(OngoingStubbingImpl.class);
        StubbedInvocationMatcher mockStubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        
        when(mockInvocationContainer.hasAnswersForStubbing()).thenReturn(false);
        when(mockMockingProgress.pullVerificationMode()).thenReturn(mockVerificationMode);
        when(mockMatchersBinder.bindMatchers(any(), eq(mockInvocation)))
            .thenReturn(mockInvocationMatcher);
        when(mockInvocationContainer.getInvocations()).thenReturn(new ArrayList<>());
        when(mockInvocationContainer.findAnswerFor(mockInvocation))
            .thenReturn(mockStubbedInvocationMatcher);
        when(mockInvocationContainer.setInvocationForPotentialStubbing(mockInvocationMatcher))
            .thenReturn(mockOngoingStubbing);
        
        mockHandler.handle(mockInvocation);
        
        verify(mockMockingProgress).pullVerificationMode();
        verify(mockInvocationContainer).setInvocationForPotentialStubbing(mockInvocationMatcher);
        verify(mockVerificationMode).verify(any(VerificationDataImpl.class));
    }
    
    @Test
    public void testHandle_withStubbedInvocationNotNull_shouldCaptureArgumentsAndReturnAnswer() throws Throwable {
        Invocation mockInvocation = mock(Invocation.class);
        InvocationMatcher mockInvocationMatcher = mock(InvocationMatcher.class);
        StubbedInvocationMatcher mockStubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        
        when(mockInvocationContainer.hasAnswersForStubbing()).thenReturn(false);
        when(mockMockingProgress.pullVerificationMode()).thenReturn(null);
        when(mockMatchersBinder.bindMatchers(any(),eq(mockInvocation)))
            .thenReturn(mockInvocationMatcher);
        when(mockInvocationContainer.findAnswerFor(mockInvocation))
            .thenReturn(mockStubbedInvocationMatcher);
        when(mockStubbedInvocationMatcher.answer(mockInvocation)).thenReturn("Mocked Result");
        
        Object result = mockHandler.handle(mockInvocation);
        
        verify(mockInvocationContainer).setInvocationForPotentialStubbing(mockInvocationMatcher);
        verify(mockStubbedInvocationMatcher).captureArgumentsFrom(mockInvocation);
        assertEquals("Mocked Result", result);
    }
    
    @Test
    public void testHandle_withStubbedInvocationIsNull_shouldReturnDefaultAnswerResult() throws Throwable {
        Invocation mockInvocation = mock(Invocation.class);
        InvocationMatcher mockInvocationMatcher = mock(InvocationMatcher.class);
        OngoingStubbingImpl<Object> mockOngoingStubbing = mock(OngoingStubbingImpl.class);
        Answer<Object> mockDefaultAnswer = mock(Answer.class);
        
        when(mockInvocationContainer.hasAnswersForStubbing()).thenReturn(false);
        when(mockMockingProgress.pullVerificationMode()).thenReturn(null);
        when(mockMatchersBinder.bindMatchers(any(), eq(mockInvocation)))
            .thenReturn(mockInvocationMatcher);
        when(mockInvocationContainer.findAnswerFor(mockInvocation)).thenReturn(null);
        when(mockMockSettings.getDefaultAnswer()).thenReturn(mockDefaultAnswer);
        when(mockDefaultAnswer.answer(mockInvocation)).thenReturn("Default Result");
        when(mockInvocationContainer.resetInvocationForPotentialStubbing(mockInvocationMatcher))
            .thenReturn(mockInvocation);
        
        Object result = mockHandler.handle(mockInvocation);
        
        verify(mockInvocationContainer).setInvocationForPotentialStubbing(mockInvocationMatcher);
        verify(mockInvocationContainer).resetInvocationForPotentialStubbing(mockInvocationMatcher);
        assertEquals("Default Result", result);
    }
    
    @Test
    public void testVoidMethodStubbable_shouldCreateVoidMethodStubbable() {
        Object mock = new Object();
        
        VoidMethodStubbable mockVoidMethodStubbable = mockHandler.voidMethodStubbable(mock);
        
        assertNotNull(mockVoidMethodStubbable);
        assertTrue(mockVoidMethodStubbable instanceof VoidMethodStubbable);
    }
    
    @Test
    public void testGetMockSettings_shouldReturnMockSettings() {
        MockSettingsImpl result = mockHandler.getMockSettings();
        
        assertEquals(mockMockSettings, result);
    }
    
    @Test
    public void testSetAnswersForStubbing_shouldSetAnswersForStubbing() {
        List<Answer> answers = new ArrayList<>();
        
        mockHandler.setAnswersForStubbing(answers);
        
        verify(mockInvocationContainer).setAnswersForStubbing(answers);
    }
    
    @Test
    public void testGetInvocationContainer_shouldReturnInvocationContainer() {
        InvocationContainer result = mockHandler.getInvocationContainer();
        
        assertEquals(mockInvocationContainer, result);
    }
}