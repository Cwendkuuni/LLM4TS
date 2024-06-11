package org.mockito.internal;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import java.util.List;
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
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {
    
    @Test
    public void testHandle() throws Throwable {
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandlerInterface<T> oldMockHandler = mock(MockHandlerInterface.class);
        
        MockHandler mockHandler = new MockHandler(mockSettings);
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(null);
        when(mockingProgress.validateState()).thenReturn(null);
        when(invocationContainerImpl.setMethodForStubbing(invocationMatcher)).thenReturn(null);
        when(mockHandler.handle(invocation)).thenCallRealMethod();
        
        assertNull(mockHandler.handle(invocation));
        
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(mockSettings.getDefaultAnswer()).thenReturn(mock(Answer.class));
        when(invocationContainerImpl.resetInvocationForPotentialStubbing(invocationMatcher)).thenReturn(null);
        when(mockingProgress.reportOngoingStubbing(any())).thenReturn(null);
        when(mockHandler.handle(invocation)).thenCallRealMethod();
        
        assertNotNull(mockHandler.handle(invocation));
    }
    
    @Test
    public void testVoidMethodStubbable() {
      MockHandlerInterface<T> oldMockHandler = mock(MockHandlerInterface.class);
      InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
      
      MockHandler mockHandler = new MockHandler(oldMockHandler);
      
      assertNotNull(mockHandler.voidMethodStubbable(mock(T)));
      
    }
    
    @Test
    public void testGetMockSettings() {
      MockHandlerInterface<T> oldMockHandler = mock(MockHandlerInterface.class);
      
      MockHandler mockHandler = new MockHandler(oldMockHandler);
      
      assertNotNull(mockHandler.getMockSettings());
      
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testSetAnswersForStubbing() {
      List<Answer> answers = mock(List.class);
      InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
      MockitoInvocationHandler mockHandler = mock(MockitoInvocationHandler.class);
      
      when(mockHandler.setAnswersForStubbing(answers)).thenCallRealMethod();
      assertNotNull(mockHandler.setAnswersForStubbing(answers));
    }
    
    @Test
    public void testGetInvocationContainer() {
      InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
      MockitoInvocationHandler mockHandler = mock(MockitoInvocationHandler.class);
      
      when(mockHandler.getInvocationContainer()).thenCallRealMethod();
      assertNotNull(mockHandler.getInvocationContainer());
    }
}