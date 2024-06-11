import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.stubbing.Answer;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.verification.VerificationMode;

import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MockHandlerTest {

    private MockHandler mockHandler;
    private MockSettingsImpl mockSettings;
    private InvocationContainerImpl invocationContainerImpl;
    private MatchersBinder matchersBinder;
    private MockingProgress mockingProgress;

    @Before
    public void setUp() {
        mockSettings = mock(MockSettingsImpl.class);
        mockingProgress = mock(ThreadSafeMockingProgress.class);
        matchersBinder = mock(MatchersBinder.class);
        invocationContainerImpl = mock(InvocationContainerImpl.class);

        mockHandler = new MockHandler(mockSettings);
        mockHandler.mockSettings = mockSettings;
        mockHandler.mockingProgress = mockingProgress;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.invocationContainerImpl = invocationContainerImpl;
    }

    @Test
    public void testHandle() throws Throwable {
        Invocation invocation = mock(Invocation.class);

        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(mock(InvocationMatcher.class));
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(invocation.getMethod()).thenReturn(null);

        Object result = mockHandler.handle(invocation);

        assertNull(result);
        verify(invocationContainerImpl, times(1)).setMethodForStubbing(any(InvocationMatcher.class));
    }

    @Test
    public void testHandleWithVerificationMode() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        MockAwareVerificationMode mockAwareVerificationMode = mock(MockAwareVerificationMode.class);

        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(mock(InvocationMatcher.class));
        when(invocation.getMock()).thenReturn(null);
        when(verificationMode instanceof MockAwareVerificationMode).thenReturn(true);
        when(((MockAwareVerificationMode) verificationMode).getMock()).thenReturn(null);

        Object result = mockHandler.handle(invocation);

        assertNull(result);
        verify(mockingProgress, times(2)).pullVerificationMode();
        verify(mockingProgress, times(1)).validateState();
        verify(verificationMode, times(1)).verify(any(VerificationDataImpl.class));
    }

    @Test
    public void testHandleWithStubbedInvocation() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);

        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(stubbedInvocation.answer(invocation)).thenReturn(null);

        Object result = mockHandler.handle(invocation);

        assertNull(result);
        verify(invocationContainerImpl, times(1)).setInvocationForPotentialStubbing(invocationMatcher);
        verify(stubbedInvocation, times(1)).captureArgumentsFrom(invocation);
    }

    @Test
    public void testHandleWithDefaultAnswer() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        Answer defaultAnswer = mock(Answer.class);

        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(mock(InvocationMatcher.class));
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);
        when(invocation.getMethod()).thenReturn(null);

        Object result = mockHandler.handle(invocation);

        assertNull(result);
        verify(invocationContainerImpl, times(1)).resetInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(defaultAnswer, times(1)).answer(invocation);
    }

    @Test
    public void testVoidMethodStubbable() {
        String mock = "mock";
        mockHandler.invocationContainerImpl = mock(InvocationContainerImpl.class);

        VoidMethodStubbable result = mockHandler.voidMethodStubbable(mock);

        assertNotNull(result);
        verify(mockHandler.invocationContainerImpl, times(1)).resetInvocationForPotentialStubbing(any(InvocationMatcher.class));
    }

    @Test
    public void testGetMockSettings() {
        MockSettingsImpl result = mockHandler.getMockSettings();

        assertEquals(mockSettings, result);
    }

    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = new ArrayList<>();

        mockHandler.setAnswersForStubbing(answers);

        verify(invocationContainerImpl, times(1)).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer() {
        InvocationContainer result = mockHandler.getInvocationContainer();

        assertEquals(invocationContainerImpl, result);
    }

}