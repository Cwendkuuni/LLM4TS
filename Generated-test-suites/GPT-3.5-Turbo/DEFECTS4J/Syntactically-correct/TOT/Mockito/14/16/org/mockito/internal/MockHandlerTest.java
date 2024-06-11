import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.verification.VerificationMode;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class MockHandlerTest {

    /* Test MockHandler.handle(Invocation) */
    @Test
    public void testHandle() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        MockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        OngoingStubbing stubbing = mock(OngoingStubbing.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        VerificationDataImpl verificationData = mock(VerificationDataImpl.class);
        
        MockHandler mockHandler = new MockHandler();
        mockHandler.mockingProgress = mockingProgress;
        mockHandler.matchersBinder = matchersBinder;
        
        // Test when invocationContainerImpl has answers for stubbing
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(mock(InvocationMatcher.class));
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(new ArrayList<>());
        when(stubbing.getInvocationContainer()).thenReturn(null);
        mockHandler.handle(invocation);
        verify(stubbing).getInvocationContainer();
        
        reset(stubbing);
        
        // Test when verificationMode is not null
        mockHandler.verificationMode = mock(VerificationMode.class);
        when(mockingProgress.pullVerificationMode()).thenReturn(mockHandler.verificationMode);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(mock(InvocationMatcher.class));
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(new ArrayList<>());
        mockHandler.handle(invocation);
        verify(mockHandler.verificationMode).verify(verificationData);
        
        reset(mockHandler.verificationMode);
        
        // Test when stubbedInvocation is not null
        mockHandler.stubbedInvocation = mock(StubbedInvocationMatcher.class);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(mock(InvocationMatcher.class));
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(new ArrayList<>());
        mockHandler.handle(invocation);
        verify(mockHandler.stubbedInvocation).captureArgumentsFrom(invocation);
        verify(mockHandler.stubbedInvocation).answer(invocation);
        
        reset(mockHandler.stubbedInvocation);
        
        // Test when stubbedInvocation is null
        when(mockHandler.mockSettings.getDefaultAnswer()).thenReturn(mock(Answer.class));
        mockHandler.handle(invocation);
        verify(mockHandler.mockSettings).getDefaultAnswer();
        verify(mockHandler.invocationContainerImpl).resetInvocationForPotentialStubbing(any(InvocationMatcher.class));
    }

    /* Test MockHandler.voidMethodStubbable(T) */
    @Test
    public void testVoidMethodStubbable() {
        Object mock = new Object();
        MockHandler handler = new MockHandler();
        handler.voidMethodStubbable(mock);
        verify(handler.invocationContainerImpl).resetInvocationForPotentialStubbing(any(InvocationMatcher.class));
    }

    /* Test MockHandler.setAnswersForStubbing(List) */
    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = mock(List.class);
        MockHandler handler = new MockHandler();
        handler.setAnswersForStubbing(answers);
        verify(handler.invocationContainerImpl).setAnswersForStubbing(answers);
    }

    /* Test MockHandler.getInvocationContainer() */
    @Test
    public void testGetInvocationContainer() {
        MockHandler handler = new MockHandler();
        InvocationContainer container = handler.getInvocationContainer();
        assertSame(handler.invocationContainerImpl, container);
    }
}