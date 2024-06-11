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
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockHandlerTest {
    @Test
    public void testHandle() throws Throwable {
        // Create mock objects
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        
        // Create the MockHandler object to be tested
        MockHandler mockHandler = new MockHandler(mockSettings);
        
        // Set the mock objects in the MockHandler object
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        
        // Create the Invocation object
        Invocation invocation = mock(Invocation.class);
        
        // Create the InvocationMatcher object
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        
        // Create the VerificationMode object
        VerificationMode verificationMode = mock(MockAwareVerificationMode.class);
        
        // Create the StubbedInvocationMatcher object
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        
        // Create the List<Answer> object
        List<Answer> answers = mock(List.class);
        
        // Create the InvocationDataImpl object
        VerificationDataImpl data = mock(VerificationDataImpl.class);
        
        // Perform the method call to be tested
        Object result = mockHandler.handle(invocation);
        
        // Verify the results
        assertNull(result);
        verify(invocationContainerImpl).hasAnswersForStubbing();
        verify(matchersBinder).bindMatchers(any(), any());
        verify(invocationContainerImpl).setMethodForStubbing(invocationMatcher);
        verify(mockHandler).getMockSettings();
        verify(mockingProgress).pullVerificationMode();
        verify(mockHandler).handle(invocation);
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockingProgress).reportOngoingStubbing(any());
        verify(invocationContainerImpl).findAnswerFor(invocation);
        verify(stubbedInvocation).captureArgumentsFrom(invocation);
        verify(stubbedInvocation).answer(invocation);
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(invocationMatcher);
        verify(mockSettings).getDefaultAnswer();
        verify(mockHandler).handle(invocation);
        verifyZeroInteractions(invocation, invocationMatcher, verificationMode, answers, data);
    }
    
    @Test
    public void testVoidMethodStubbable() {
        // Create mock objects
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        
        // Create the MockHandler object to be tested
        MockHandler mockHandler = new MockHandler();
        
        // Set the mock objects in the MockHandler object
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        
        // Create the mock object
        Object mock = mock(Object.class);
        
        // Perform the method call to be tested
        VoidMethodStubbable result = mockHandler.voidMethodStubbable(mock);
        
        // Verify the results
        assertNotNull(result);
        verifyZeroInteractions(invocationContainerImpl, mock);
    }
    
    @Test
    public void testSetAnswersForStubbing() {
        // Create mock objects
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        
        // Create the MockHandler object to be tested
        MockHandler mockHandler = new MockHandler();
        
        // Set the mock objects in the MockHandler object
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        
        // Create the List<Answer> object
        List<Answer> answers = mock(List.class);
        
        // Perform the method call to be tested
        mockHandler.setAnswersForStubbing(answers);
        
        // Verify the results
        verify(invocationContainerImpl).setAnswersForStubbing(answers);
        verifyZeroInteractions(answers);
    }
    
    @Test
    public void testGetInvocationContainer() {
        // Create mock objects
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        
        // Create the MockHandler object to be tested
        MockHandler mockHandler = new MockHandler();
        
        // Set the mock objects in the MockHandler object
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        
        // Perform the method call to be tested
        InvocationContainer result = mockHandler.getInvocationContainer();
        
        // Verify the results
        assertNotNull(result);
        verifyZeroInteractions(invocationContainerImpl);
    }
}