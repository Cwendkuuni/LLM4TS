import org.junit.Before;
import org.junit.Test;
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
import org.mockito.internal.MockSettingsImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockHandlerTest {

    private MockHandler<SomeClass> mockHandler;

    @Before
    public void setUp() {
        mockHandler = new MockHandler<>(new MockSettingsImpl());
    }

    @Test
    public void testHandle_InvocationContainerImpl() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);

        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(mock(InvocationMatcher.class));
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(mock(StubbedInvocationMatcher.class));

        Object result = mockHandler.handle(invocation);

        assertNull(result);
        verify(mockHandler).handle(invocation);
        verify(invocationContainerImpl).hasAnswersForStubbing();
        verify(matchersBinder).bindMatchers(any(), any());
        verify(invocationContainerImpl).setMethodForStubbing(any(InvocationMatcher.class));
        verify(invocationContainerImpl, never()).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(invocationContainerImpl).findAnswerFor(invocation);
        verify(invocationContainerImpl, never()).resetInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(invocation, never()).getArguments();
    }

    @Test
    public void testHandle_VerificationModeNotNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        ThreadSafeMockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);

        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(mock(InvocationMatcher.class));

        Object result = mockHandler.handle(invocation);

        assertNull(result);
        verify(mockHandler).handle(invocation);
        verify(mockingProgress).pullVerificationMode();
        verify(invocationContainerImpl, never()).hasAnswersForStubbing();
        verify(matchersBinder).bindMatchers(any(), any());
        verify(mockingProgress).validateState();
        verify(mockingProgress, never()).reportOngoingStubbing(any());
        verify(invocationContainerImpl, never()).findAnswerFor(invocation);
        verify(verificationMode).verify(any());
        verify(invocation, never()).getArguments();
    }

    @Test
    public void testHandle_StubbedInvocationMatcherNotNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        ThreadSafeMockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        List<Answer> answers = mock(ArrayList.class);

        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(mock(StubbedInvocationMatcher.class));
        when(invocationContainerImpl.getInvocations()).thenReturn(new ArrayList<>());
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(mock(InvocationMatcher.class));
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(invocationContainerImpl.getArgumentMatcherStorage()).thenReturn(new ArrayList<>());
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(mock(StubbedInvocationMatcher.class));
        when(mockHandler.getMockSettings()).thenReturn(mock(MockSettingsImpl.class));
        when(mockHandler.getMockSettings().getDefaultAnswer()).thenReturn(mock(Answer.class));

        Object result = mockHandler.handle(invocation);

        assertNotNull(result);
        verify(mockHandler).handle(invocation);
        verify(mockingProgress).pullVerificationMode();
        verify(matchersBinder).bindMatchers(any(), any());
        verify(mockingProgress).validateState();
        verify(mockingProgress).reportOngoingStubbing(any());
        verify(mockingProgress, never()).validateState();
        verify(invocationContainerImpl).findAnswerFor(invocation);
        verify(mockHandler.getMockSettings()).getDefaultAnswer();
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(any());
        verify(invocation, never()).getArguments();
    }

    @Test
    public void testVoidMethodStubbable() {
        SomeClass mock = mock(SomeClass.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        List<Answer> answers = new ArrayList<>();

        when(invocationContainerImpl.getMethodForStubbing()).thenReturn(mock(InvocationMatcher.class));
        when(mockHandler.getInvocationContainer()).thenReturn(invocationContainerImpl);

        VoidMethodStubbable stubbable = mockHandler.voidMethodStubbable(mock);

        assertEquals(mock, stubbable.getMock());
        verify(mockHandler, times(2)).getInvocationContainer();        
    }

    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = mock(ArrayList.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);

        doNothing().when(invocationContainerImpl).setAnswersForStubbing(answers);
        when(mockHandler.getInvocationContainer()).thenReturn(invocationContainerImpl);

        mockHandler.setAnswersForStubbing(answers);

        verify(mockHandler).setAnswersForStubbing(answers);
        verify(mockHandler).getInvocationContainer();
        verify(invocationContainerImpl).setAnswersForStubbing(answers);
    }
}