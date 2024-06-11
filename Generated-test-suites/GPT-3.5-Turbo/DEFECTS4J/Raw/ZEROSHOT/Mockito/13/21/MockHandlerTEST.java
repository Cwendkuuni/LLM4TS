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
import org.mockito.internal.creation.MockSettingsImpl;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit test for MockHandler class.
 */
public class MockHandlerTest {

    private MockHandler<String> mockHandler;
    private InvocationContainerImpl invocationContainer;
    private MatchersBinder matchersBinder;
    private MockingProgress mockingProgress;
    private MockSettingsImpl mockSettings;

    @Before
    public void setUp() {
        mockSettings = mock(MockSettingsImpl.class);
        invocationContainer = mock(InvocationContainerImpl.class);
        matchersBinder = mock(MatchersBinder.class);
        mockingProgress = mock(ThreadSafeMockingProgress.class);
        mockHandler = new MockHandler<>(mockSettings);
        mockHandler.invocationContainerImpl = invocationContainer;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
    }

    @Test
    public void testHandleWithStubbingVoidMethod() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(new ArrayList<>());
        when(matchersBinder.bindMatchers(anyList(), any(Invocation.class))).thenReturn(invocationMatcher);

        mockHandler.handle(invocation);

        verify(invocationContainer).setMethodForStubbing(invocationMatcher);
    }

    @Test
    public void testHandleWithVerificationMode() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        MockAwareVerificationMode mockAwareVerificationMode = mock(MockAwareVerificationMode.class);

        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(matchersBinder.bindMatchers(anyList(), any(Invocation.class))).thenReturn(mock(InvocationMatcher.class));
        when(mockAwareVerificationMode.getMock()).thenReturn(invocation.getMock());
        when(verificationMode instanceof MockAwareVerificationMode).thenReturn(true);
        when(verificationMode instanceof MockAwareVerificationMode && ((MockAwareVerificationMode) verificationMode).getMock() == invocation.getMock()).thenReturn(true);

        mockHandler.handle(invocation);

        verify(verificationMode).verify(any());
    }

    @Test
    public void testHandleWithoutStubbedInvocation() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        StubbedInvocationMatcher stubbedInvocation = null;
        when(mockSettings.getDefaultAnswer()).thenReturn(mock(Answer.class));
        when(matchersBinder.bindMatchers(anyList(), any(Invocation.class))).thenReturn(mock(InvocationMatcher.class));
        when(invocationContainer.findAnswerFor(invocation)).thenReturn(stubbedInvocation);

        mockHandler.handle(invocation);

        verify(mockSettings).getDefaultAnswer();
        verify(invocationContainer).resetInvocationForPotentialStubbing(any(InvocationMatcher.class));
    }

    @Test
    public void testVoidMethodStubbable() {
        String mock = "mock";
        VoidMethodStubbableImpl<String> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);

        assertEquals(mock, voidMethodStubbable.getMock());
        assertEquals(invocationContainer, voidMethodStubbable.invocationContainerImpl);
    }

    @Test
    public void testGetMockSettings() {
        assertEquals(mockSettings, mockHandler.getMockSettings());
    }

    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = new ArrayList<>();
        mockHandler.setAnswersForStubbing(answers);

        verify(invocationContainer).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer() {
        assertEquals(invocationContainer, mockHandler.getInvocationContainer());
    }
}