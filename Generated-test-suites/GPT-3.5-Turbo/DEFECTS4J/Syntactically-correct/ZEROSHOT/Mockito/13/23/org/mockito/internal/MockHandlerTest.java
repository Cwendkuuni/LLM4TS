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
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockHandlerTest {

    private MockHandler mockHandler;
    private MockSettingsImpl mockSettings;
    private InvocationContainerImpl invocationContainer;
    private MatchersBinder matchersBinder;
    private MockingProgress mockingProgress;

    @Before
    public void setUp() {
        mockSettings = mock(MockSettingsImpl.class);
        invocationContainer = mock(InvocationContainerImpl.class);
        matchersBinder = mock(MatchersBinder.class);
        mockingProgress = mock(ThreadSafeMockingProgress.class);

        mockHandler = new MockHandler(mockSettings);
        mockHandler.invocationContainerImpl = invocationContainer;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
    }

    @Test
    public void testHandle_withInvocationContainerWithAnswersForStubbing_shouldReturnNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);

        when(invocationContainer.hasAnswersForStubbing()).thenReturn(true);

        Object result = mockHandler.handle(invocation);

        assertNull(result);
        verify(mockingProgress).getArgumentMatcherStorage();
        verify(matchersBinder).bindMatchers(any(), eq(invocation));
        verify(invocationContainer).setMethodForStubbing(any());
    }

    @Test
    public void testHandle_withVerificationModeAndCorrectMock_shouldVerify() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        VerificationMode verificationMode = mock(MockAwareVerificationMode.class);
        
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(((MockAwareVerificationMode) verificationMode).getMock()).thenReturn(invocation.getMock());

        Object result = mockHandler.handle(invocation);

        assertNull(result);
        verify(mockingProgress).getArgumentMatcherStorage();
        verify(matchersBinder).bindMatchers(any(), eq(invocation));
        verify(mockingProgress).validateState();
        verify(verificationMode).verify(any());
    }

    @Test
    public void testHandle_withStubbedInvocation_shouldReturnStubbedAnswer() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        Answer answer = mock(Answer.class);

        when(invocationContainer.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(stubbedInvocation.answer(invocation)).thenReturn(answer);

        Object result = mockHandler.handle(invocation);

        assertSame(answer, result);
        verify(mockingProgress).getArgumentMatcherStorage();
        verify(invocationContainer).setInvocationForPotentialStubbing(any());
    }

    @Test
    public void testHandle_withNoStubbedInvocation_shouldReturnDefaultAnswer() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        Answer answer = mock(Answer.class);

        when(invocationContainer.findAnswerFor(invocation)).thenReturn(null);
        when(mockSettings.getDefaultAnswer()).thenReturn(answer);

        Object result = mockHandler.handle(invocation);

        assertSame(answer, result);
        verify(mockingProgress).getArgumentMatcherStorage();
        verify(mockSettings).getDefaultAnswer();
        verify(invocationContainer).resetInvocationForPotentialStubbing(any());
    }

    @Test
    public void testVoidMethodStubbable_shouldCreateVoidMethodStubbableImpl() {
        String mock = "mock";
        VoidMethodStubbableImpl stubbable = mock(VoidMethodStubbableImpl.class);

        when(invocationContainer.voidMethodStubbable(mock)).thenReturn(stubbable);

        VoidMethodStubbableImpl result = mockHandler.voidMethodStubbable(mock);

        assertSame(stubbable, result);
        verify(invocationContainer).voidMethodStubbable(mock);
    }

    @Test
    public void testGetMockSettings_shouldReturnMockSettings() {
        MockSettingsImpl result = mockHandler.getMockSettings();

        assertSame(mockSettings, result);
    }

    @Test
    public void testSetAnswersForStubbing_shouldSetAnswersForStubbing() {
        List<Answer> answers = mock(List.class);

        mockHandler.setAnswersForStubbing(answers);

        verify(invocationContainer).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer_shouldReturnInvocationContainer() {
        InvocationContainer result = mockHandler.getInvocationContainer();

        assertSame(invocationContainer, result);
    }
}