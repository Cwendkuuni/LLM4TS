import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationContainer;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {
    
    private MockHandler<Object> mockHandler;
    
    @Before
    public void setUp() {
        mockHandler = new MockHandler<>();
    }
    
    @Test
    public void testHandle_withStubbing() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        List<Answer<?>> answers = new ArrayList<>();
        Answer<Object> answer = mock(Answer.class);
        answers.add(answer);
        
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(mock(InvocationMatcher.class));
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.setAnswersForStubbing(answers);
        
        assertNull(mockHandler.handle(invocation));
        verify(invocationContainerImpl).setMethodForStubbing(any(InvocationMatcher.class));
    }
    
    @Test
    public void testHandle_withVerificationMode() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        List<Answer<?>> answers = new ArrayList<>();
        Answer<Object> answer = mock(Answer.class);
        answers.add(answer);
        VerificationMode verificationMode = mock(VerificationMode.class);
        
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(mock(InvocationMatcher.class));
        when(invocationContainerImpl.findAnswerFor(any())).thenReturn(mock(StubbedInvocationMatcher.class));
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.setAnswersForStubbing(answers);
        mockHandler.mockingProgress.reportOngoingStubbing(mock(OngoingStubbingImpl.class));
        mockHandler.mockingProgress.pullVerificationMode();
        mockHandler.mockingProgress.validateState();
        mockHandler.mockingProgress.reportOngoingStubbing(mock(OngoingStubbingImpl.class));
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        
        assertNull(mockHandler.handle(invocation));
        verify(mockingProgress).pullVerificationMode();
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(invocationContainerImpl).findAnswerFor(any());
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(verificationMode).verify(any(VerificationDataImpl.class));
    }
    
    @Test
    public void testHandle_withoutStubbing() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        List<Answer<?>> answers = new ArrayList<>();
        Answer<Object> answer = mock(Answer.class);
        answers.add(answer);
        
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(mock(InvocationMatcher.class));
        when(invocationContainerImpl.findAnswerFor(any())).thenReturn(null);
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.setAnswersForStubbing(answers);
        mockHandler.mockingProgress.reportOngoingStubbing(mock(OngoingStubbingImpl.class));
        mockHandler.mockingProgress.pullVerificationMode();
        
        assertNull(mockHandler.handle(invocation));
        verify(mockingProgress).pullVerificationMode();
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(invocationContainerImpl).findAnswerFor(any());
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(any(InvocationMatcher.class));
    }
    
    @Test
    public void testVoidMethodStubbable() {
        Object mock = new Object();
        VoidMethodStubbable<Object> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);
        
        assertNotNull(voidMethodStubbable);
        assertTrue(voidMethodStubbable instanceof VoidMethodStubbableImpl);
    }
    
    @Test
    public void testGetMockSettings() {
        MockSettingsImpl mockSettings = mockHandler.getMockSettings();
        
        assertNotNull(mockSettings);
        assertTrue(mockSettings instanceof MockSettingsImpl);
    }
    
    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = new ArrayList<>();
        mockHandler.setAnswersForStubbing(answers);
        
        InvocationContainer invocationContainer = mockHandler.getInvocationContainer();
        assertNotNull(invocationContainer);
        assertTrue(invocationContainer instanceof InvocationContainerImpl);
    }
}