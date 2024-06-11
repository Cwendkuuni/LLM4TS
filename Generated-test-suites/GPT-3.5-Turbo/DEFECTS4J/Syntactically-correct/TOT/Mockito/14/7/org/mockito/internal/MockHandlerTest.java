import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class MockHandlerTest {

    private MockHandler<String> mockHandler;
    private MockSettingsImpl mockSettings;
    private MockingProgress mockingProgress;

    @Before
    public void setUp() {
        mockSettings = mock(MockSettingsImpl.class);
        mockingProgress = new ThreadSafeMockingProgress();
        mockHandler = new MockHandler<>(mockSettings);
        mockHandler.invocationContainerImpl = mock(InvocationContainerImpl.class);
        mockHandler.matchersBinder = mock(MatchersBinder.class);
        mockHandler.mockingProgress = mockingProgress;
    }

    @Test
    public void handle_WhenInvocationContainerHasAnswersForStubbing_ShouldSetMethodForStubbingAndReturnNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(mockHandler.invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(mockHandler.matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);

        Object result = mockHandler.handle(invocation);

        verify(mockHandler.invocationContainerImpl).setMethodForStubbing(invocationMatcher);
        verifyZeroInteractions(mockHandler.mockSettings);
        verifyZeroInteractions(mockHandler.invocationContainerImpl);
        verifyZeroInteractions(mockHandler.mockingProgress);
        verifyZeroInteractions(mockHandler.matchersBinder);
        assertNull(result);
    }

    @Test
    public void handle_WhenVerificationModeIsNotNullAndVerificationSucceeds_ShouldVerifyAndReturnNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        VerificationDataImpl verificationData = mock(VerificationDataImpl.class);
        when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(mockHandler.matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        when(mockHandler.invocationContainerImpl.getInvocations()).thenReturn(new ArrayList<>());
        when(mockHandler.mockingProgress.validateState()).thenReturn(true);
        when(mockHandler.mockSettings.getDefaultAnswer()).thenReturn(mock(Answer.class));
        when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        when(verificationData.wasVerificationPassed()).thenReturn(true);
        when(verificationMode.verify(any())).thenReturn(verificationData);

        Object result = mockHandler.handle(invocation);

        verify(mockHandler.matchersBinder).bindMatchers(any(), eq(invocation));
        verify(mockHandler.mockingProgress).pullVerificationMode();
        verify(mockHandler.invocationContainerImpl).getInvocations();
        verify(mockHandler.invocationContainerImpl).getInvocations();
        verify(mockHandler.invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockHandler.mockingProgress).reportOngoingStubbing(any());
        verify(mockHandler.invocationContainerImpl).resetInvocationForPotentialStubbing(invocationMatcher);
        verify(verificationMode).verify(verificationData);
        assertNull(result);
    }

    @Test
    public void handle_WhenVerificationModeIsNotNullAndVerificationFails_ShouldVerifyAndReturnNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        VerificationDataImpl verificationData = mock(VerificationDataImpl.class);
        when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(mockHandler.matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        when(mockHandler.invocationContainerImpl.getInvocations()).thenReturn(new ArrayList<>());
        when(mockHandler.mockingProgress.validateState()).thenReturn(true);
        when(mockHandler.mockSettings.getDefaultAnswer()).thenReturn(mock(Answer.class));
        when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        when(verificationData.wasVerificationPassed()).thenReturn(false);
        when(verificationMode.verify(any())).thenReturn(verificationData);

        Object result = mockHandler.handle(invocation);

        verify(mockHandler.matchersBinder).bindMatchers(any(), eq(invocation));
        verify(mockHandler.mockingProgress).pullVerificationMode();
        verify(mockHandler.invocationContainerImpl).getInvocations();
        verify(mockHandler.invocationContainerImpl).getInvocations();
        verify(mockHandler.invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockHandler.mockingProgress).reportOngoingStubbing(any());
        verify(mockHandler.invocationContainerImpl).resetInvocationForPotentialStubbing(invocationMatcher);
        verify(verificationMode).verify(verificationData);
        assertNull(result);
    }

    @Test
    public void handle_WhenStubbedInvocationNotNull_ShouldCaptureArgumentsAndReturnAnswer() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        Answer answer = mock(Answer.class);
        when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(null);
        when(mockHandler.matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        when(mockHandler.invocationContainerImpl.getInvocations()).thenReturn(new ArrayList<>());
        when(mockHandler.mockingProgress.validateState()).thenReturn(true);
        when(mockHandler.mockSettings.getDefaultAnswer()).thenReturn(answer);
        when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(stubbedInvocation.answer(invocation)).thenReturn("answer");

        Object result = mockHandler.handle(invocation);

        verify(mockHandler.matchersBinder).bindMatchers(any(), eq(invocation));
        verify(mockHandler.mockingProgress).pullVerificationMode();
        verify(mockHandler.invocationContainerImpl).getInvocations();
        verify(mockHandler.invocationContainerImpl).getInvocations();
        verify(mockHandler.invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockHandler.mockingProgress).reportOngoingStubbing(any());
        verify(stubbedInvocation).captureArgumentsFrom(invocation);
        verify(stubbedInvocation).answer(invocation);
        assertEquals("answer", result);
    }

    @Test
    public void handle_WhenStubbedInvocationIsNull_ShouldReturnDefaultAnswer() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        Answer answer = mock(Answer.class);
        when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(null);
        when(mockHandler.matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        when(mockHandler.invocationContainerImpl.getInvocations()).thenReturn(new ArrayList<>());
        when(mockHandler.mockingProgress.validateState()).thenReturn(true);
        when(mockHandler.mockSettings.getDefaultAnswer()).thenReturn(answer);
        when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        when(answer.answer(invocation)).thenReturn("defaultAnswer");

        Object result = mockHandler.handle(invocation);

        verify(mockHandler.matchersBinder).bindMatchers(any(), eq(invocation));
        verify(mockHandler.mockingProgress).pullVerificationMode();
        verify(mockHandler.invocationContainerImpl).getInvocations();
        verify(mockHandler.invocationContainerImpl).getInvocations();
        verify(mockHandler.invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockHandler.mockingProgress).reportOngoingStubbing(any());
        verify(mockHandler.invocationContainerImpl).resetInvocationForPotentialStubbing(invocationMatcher);
        verify(answer).answer(invocation);
        assertEquals("defaultAnswer", result);
    }

    @Test
    public void voidMethodStubbable_ShouldCreateVoidMethodStubbableImpl() {
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        String mock = "mock";
        mockHandler.invocationContainerImpl = invocationContainer;

        VoidMethodStubbable voidMethodStubbable = mockHandler.voidMethodStubbable(mock);

        assertNotNull(voidMethodStubbable);
        assertTrue(voidMethodStubbable instanceof VoidMethodStubbableImpl);
        assertNull(voidMethodStubbable.mock());
    }

    @Test
    public void getMockSettings_ShouldReturnMockSettingsImpl() {
        MockSettingsImpl result = mockHandler.getMockSettings();

        assertNotNull(result);
        assertTrue(result instanceof MockSettingsImpl);
    }

    @Test
    public void setAnswersForStubbing_ShouldSetAnswersForStubbingInInvocationContainer() {
        List<Answer> answers = new ArrayList<>();
        mockHandler.invocationContainerImpl = mock(InvocationContainerImpl.class);

        mockHandler.setAnswersForStubbing(answers);

        verify(mockHandler.invocationContainerImpl).setAnswersForStubbing(answers);
    }

    @Test
    public void getInvocationContainer_ShouldReturnInvocationContainerImpl() {
        InvocationContainerImpl result = mockHandler.getInvocationContainer();

        assertNotNull(result);
        assertTrue(result instanceof InvocationContainerImpl);
    }
}