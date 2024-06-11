import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class MockHandlerTest {

    private MockHandler<Integer> mockHandler;
    private MockHandlerInterface<Integer> oldMockHandler;
    private Invocation invocation;
    private InvocationContainerImpl invocationContainerImpl;
    private MatchersBinder matchersBinder;
    private MockingProgress mockingProgress;
    private MockSettingsImpl mockSettings;

    @Before
    public void setup() {
        mockSettings = mock(MockSettingsImpl.class);
        matchersBinder = mock(MatchersBinder.class);
        invocationContainerImpl = mock(InvocationContainerImpl.class);
        mockingProgress = mock(ThreadSafeMockingProgress.class);
        oldMockHandler = mock(MockHandlerInterface.class);
        mockHandler = new MockHandler<>(mockSettings);

        mockHandler.matchersBinder = matchersBinder;
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.mockingProgress = mockingProgress;

        invocation = mock(Invocation.class);
    }

    @Test
    public void shouldHandleInvocationWhenAnswersForStubbingExist() throws Throwable {
        List<Answer> answers = new ArrayList<>();
        answers.add(mock(Answer.class));
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(new ArgumentMatcherStorage());
        when(matchersBinder.bindMatchers(any(ArgumentMatcherStorage.class), any(Invocation.class))).thenReturn(mock(InvocationMatcher.class));
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(mockSettings.getDefaultAnswer()).thenReturn(mock(Answer.class));

        mockHandler.setAnswersForStubbing(answers);
        mockHandler.handle(invocation);

        verify(invocationContainerImpl).getMethodForStubbing(any(InvocationMatcher.class));
    }

    @Test
    public void shouldHandleInvocationWhenVerificationModeNotNull() throws Throwable {
        List<Answer> answers = new ArrayList<>();
        answers.add(mock(Answer.class));
        VerificationMode verificationMode = mock(VerificationMode.class);
        MockAwareVerificationMode mockAwareVerificationMode = mock(MockAwareVerificationMode.class);
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(new ArgumentMatcherStorage());
        when(matchersBinder.bindMatchers(any(ArgumentMatcherStorage.class), any(Invocation.class))).thenReturn(mock(InvocationMatcher.class));
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(verificationMode instanceof MockAwareVerificationMode).thenReturn(true);
        when(((MockAwareVerificationMode) verificationMode).getMock()).thenReturn(mock(Object.class));

        mockHandler.setAnswersForStubbing(answers);
        mockHandler.handle(invocation);

        verify(invocationContainerImpl, never()).getMethodForStubbing(any(InvocationMatcher.class));
        verify(mockingProgress, never()).reportOngoingStubbing(any());
        verify(invocationContainerImpl, never()).findAnswerFor(any());
        verify(mockSettings, never()).getDefaultAnswer();
        verify(invocationContainerImpl, never()).resetInvocationForPotentialStubbing(any());
    }

    @Test
    public void shouldHandleInvocationWhenStubbedInvocationIsNull() throws Throwable {
        List<Answer> answers = new ArrayList<>();
        answers.add(mock(Answer.class));
        Answer defaultAnswer = mock(Answer.class);
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(new ArgumentMatcherStorage());
        when(matchersBinder.bindMatchers(any(ArgumentMatcherStorage.class), any(Invocation.class))).thenReturn(mock(InvocationMatcher.class));
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);
        when(defaultAnswer.answer(any(Invocation.class))).thenReturn(mock(Object.class));

        mockHandler.setAnswersForStubbing(answers);
        mockHandler.handle(invocation);

        verify(invocationContainerImpl).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(invocationContainerImpl).findAnswerFor(any(Invocation.class));
        verify(invocationContainerImpl, never()).resetInvocationForPotentialStubbing(any());
        verify(defaultAnswer, never()).answer(any(Invocation.class));
    }

    @Test
    public void shouldHandleInvocationWhenStubbedInvocationIsNotNull() throws Throwable {
        List<Answer> answers = new ArrayList<>();
        answers.add(mock(Answer.class));
        Answer defaultAnswer = mock(Answer.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(new ArgumentMatcherStorage());
        when(matchersBinder.bindMatchers(any(ArgumentMatcherStorage.class), any(Invocation.class))).thenReturn(mock(InvocationMatcher.class));
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);
        when(invocationContainerImpl.findAnswerFor(any(Invocation.class))).thenReturn(stubbedInvocationMatcher);
        when(stubbedInvocationMatcher.answer(any(Invocation.class))).thenReturn(mock(Object.class));

        mockHandler.setAnswersForStubbing(answers);
        mockHandler.handle(invocation);

        verify(invocationContainerImpl).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(invocationContainerImpl).findAnswerFor(any(Invocation.class));
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(any());
        verify(defaultAnswer, never()).answer(any(Invocation.class));
    }

    @Test
    public void shouldHandleInvocationWhenDefaultAnswerReturnsNull() throws Throwable {
        List<Answer> answers = new ArrayList<>();
        answers.add(mock(Answer.class));
        Answer defaultAnswer = mock(Answer.class);
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(new ArgumentMatcherStorage());
        when(matchersBinder.bindMatchers(any(ArgumentMatcherStorage.class), any(Invocation.class))).thenReturn(mock(InvocationMatcher.class));
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);
        when(invocationContainerImpl.findAnswerFor(any(Invocation.class))).thenReturn(null);
        when(defaultAnswer.answer(any(Invocation.class))).thenReturn(null);

        mockHandler.setAnswersForStubbing(answers);
        mockHandler.handle(invocation);

        verify(invocationContainerImpl).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(invocationContainerImpl).findAnswerFor(any(Invocation.class));
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(any());
        verify(defaultAnswer).answer(any(Invocation.class));
    }

    @Test
    public void shouldCreateVoidMethodStubbable() {
        Integer mock = mock(Integer.class);
        VoidMethodStubbableImpl<Integer> stubbable = mockHandler.voidMethodStubbable(mock);

        verify(invocationContainerImpl).addVoidMethodStubbable(eq(stubbable));
    }

    @Test
    public void shouldReturnMockSettings() {
        MockSettingsImpl settings = mockHandler.getMockSettings();

        assertSame(mockSettings, settings);
    }

    @Test
    public void shouldSetAnswersForStubbing() {
        List<Answer> answers = new ArrayList<>();
        answers.add(mock(Answer.class));
        mockHandler.setAnswersForStubbing(answers);

        verify(invocationContainerImpl).setAnswersForStubbing(eq(answers));
    }

    @Test
    public void shouldGetInvocationContainer() {
        InvocationContainer container = mockHandler.getInvocationContainer();

        assertSame(invocationContainerImpl, container);
    }
}