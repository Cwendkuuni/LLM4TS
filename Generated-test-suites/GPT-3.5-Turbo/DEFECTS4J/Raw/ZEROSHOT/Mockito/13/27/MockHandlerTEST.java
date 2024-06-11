import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

import java.util.List;

import static org.mockito.Mockito.*;

public class MockHandlerTest {
    
    private MockHandler<Object> mockHandler;
    private InvocationContainerImpl invocationContainer;
    private MatchersBinder matchersBinder;
    private MockingProgress mockingProgress;
    
    @Before
    public void setup() {
        mockHandler = new MockHandler<>();
        invocationContainer = mock(InvocationContainerImpl.class);
        matchersBinder = mock(MatchersBinder.class);
        mockingProgress = mock(ThreadSafeMockingProgress.class);
        
        mockHandler.invocationContainerImpl = invocationContainer;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
    }
    
    @Test
    public void handle_shouldCallBindMatchersAndSetMethodForStubbingWhenContainerHasAnswersForStubbing() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        
        when(invocationContainer.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        
        mockHandler.handle(invocation);
        
        verify(matchersBinder, times(1)).bindMatchers(any(), any());
        verify(invocationContainer, times(1)).setMethodForStubbing(invocationMatcher);
    }
    
    @Test
    public void handle_shouldCallPullVerificationModeAndBindMatchersWhenContainerDoesNotHaveAnswersForStubbingAndVerificationModeIsNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        
        when(invocationContainer.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        
        mockHandler.handle(invocation);
        
        verify(mockingProgress, times(1)).pullVerificationMode();
        verify(matchersBinder, times(1)).bindMatchers(any(), any());
    }
    
    @Test
    public void handle_shouldCallHandleVerificationModeWhenContainerDoesNotHaveAnswersForStubbingAndVerificationModeIsNotNullAndMockIsTheSame() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        MockAwareVerificationMode mockAwareVerificationMode = mock(MockAwareVerificationMode.class);
        
        when(invocationContainer.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(mockAwareVerificationMode);
        when(mockAwareVerificationMode.getMock()).thenReturn(invocation.getMock());
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        
        mockHandler.handle(invocation);
        
        verify(mockingProgress, times(1)).pullVerificationMode();
        verify(mockAwareVerificationMode, times(1)).getMock();
        verifyNoMoreInteractions(invocationContainer);
    }
    
    @Test
    public void handle_shouldCallSetInvocationForPotentialStubbingAndReportOngoingStubbingWhenContainerDoesNotHaveAnswersForStubbingAndVerificationModeIsNotNullAndMockIsDifferent() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        MockAwareVerificationMode mockAwareVerificationMode = mock(MockAwareVerificationMode.class);
        InvocationMatcher otherInvocationMatcher = mock(InvocationMatcher.class);
        
        when(invocationContainer.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(mockAwareVerificationMode);
        when(mockAwareVerificationMode.getMock()).thenReturn(mock(Invocation.class).getMock());
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(otherInvocationMatcher);
        
        mockHandler.handle(invocation);
        
        verify(mockingProgress, times(1)).pullVerificationMode();
        verify(mockAwareVerificationMode, times(1)).getMock();
        verify(invocationContainer, times(1)).setInvocationForPotentialStubbing(otherInvocationMatcher);
        verify(mockingProgress, times(1)).reportOngoingStubbing(any());
    }
    
    @Test
    public void handle_shouldCallFindAnswerForAndCaptureArgumentsFromWhenStubbedInvocationIsNotNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        
        when(invocationContainer.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(invocationContainer.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        
        mockHandler.handle(invocation);
        
        verify(invocationContainer, times(1)).findAnswerFor(invocation);
        verify(stubbedInvocation, times(1)).captureArgumentsFrom(invocation);
    }
    
    @Test
    public void handle_shouldCallAnswerWhenStubbedInvocationIsNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        Answer<Object> defaultAnswer = mock(Answer.class);
        
        when(invocationContainer.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(invocationContainer.findAnswerFor(invocation)).thenReturn(null);
        when(mockHandler.getMockSettings().getDefaultAnswer()).thenReturn(defaultAnswer);
        
        mockHandler.handle(invocation);
        
        verify(invocationContainer, times(1)).findAnswerFor(invocation);
        verify(defaultAnswer, times(1)).answer(invocation);
    }
    
    @Test
    public void voidMethodStubbable_shouldReturnInstanceOfVoidMethodStubbableImpl() {
        Object mock = mock(Object.class);
        VoidMethodStubbableImpl<Object> expected = new VoidMethodStubbableImpl<>(mock, invocationContainer);
        
        VoidMethodStubbable<Object> actual = mockHandler.voidMethodStubbable(mock);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void getMockSettings_shouldReturnMockSettingsImpl() {
        MockSettingsImpl expected = mockHandler.getMockSettings();
        
        MockSettingsImpl actual = mockHandler.getMockSettings();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void setAnswersForStubbing_shouldCallSetAnswersForStubbingOnInvocationContainer() {
        List<Answer<Object>> answers = mock(List.class);
        
        mockHandler.setAnswersForStubbing(answers);
        
        verify(invocationContainer, times(1)).setAnswersForStubbing(answers);
    }
    
    @Test
    public void getInvocationContainer_shouldReturnInvocationContainerImpl() {
        InvocationContainerImpl expected = invocationContainer;
        
        InvocationContainerImpl actual = mockHandler.getInvocationContainer();
        
        assertEquals(expected, actual);
    }
}