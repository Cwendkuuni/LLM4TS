
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
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;
    private MockSettingsImpl mockSettings;

    @Before
    public void setUp() {
        mockSettings = mock(MockSettingsImpl.class);
        mockHandler = new MockHandler<>(mockSettings);
    }

    @Test
    public void testHandle_withInvocationForStubbing() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(new ArgumentMatcherStorage());
        
        assertNull(mockHandler.handle(invocation));
        
        verify(invocationContainerImpl).setMethodForStubbing(invocationMatcher);
    }
    
    @Test
    public void testHandle_withVerificationMode() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        
        when(matchingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(mock(StubbedInvocationMatcher.class));
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(new ArgumentMatcherStorage());
        
        assertNull(mockHandler.handle(invocation));
        
        verify(verificationMode).verify(any(VerificationDataImpl.class));
    }
    
    @Test
    public void testHandle_withStubbedInvocation() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        OngoingStubbingImpl<Object> ongoingStubbing = mock(OngoingStubbingImpl.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        Answer<Object> defaultAnswer = mock(Answer.class);
        
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(ongoingStubbing.answer(invocation)).thenReturn(null);
        when(mockingProgress.reportOngoingStubbing(any(OngoingStubbingImpl.class))).thenReturn(null);
        
        assertNull(mockHandler.handle(invocation));
        
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
        verify(stubbedInvocation).captureArgumentsFrom(invocation);
        verify(stubbedInvocation).answer(invocation);
    }
    
    @Test
    public void testVoidMethodStubbable() {
        Object mock = new Object();
        
        VoidMethodStubbable<Void> result = mockHandler.voidMethodStubbable(mock);
        
        assertNotNull(result);
        assertTrue(result instanceof VoidMethodStubbableImpl);
    }
    
    @Test
    public void testGetMockSettings() {
        MockSettingsImpl result = mockHandler.getMockSettings();
        
        assertNotNull(result);
        assertEquals(mockSettings, result);
    }
    
    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = mock(List.class);
        
        mockHandler.setAnswersForStubbing(answers);
        
        verify(invocationContainerImpl).setAnswersForStubbing(answers);
    }
    
    @Test
    public void testGetInvocationContainer() {
        InvocationContainer result = mockHandler.getInvocationContainer();
        
        assertNotNull(result);
        assertTrue(result instanceof InvocationContainerImpl);
    }
}
