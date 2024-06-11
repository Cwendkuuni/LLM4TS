import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {

    private MockHandler<String> testMockHandler;
    private MockSettingsImpl mockSettings;
    private InvocationContainerImpl invocationContainer;
    private MatchersBinder matchersBinder;
    private MockingProgress mockingProgress;

    @Before
    public void setUp() {
        mockSettings = new MockSettingsImpl();
        invocationContainer = mock(InvocationContainerImpl.class);
        matchersBinder = mock(MatchersBinder.class);
        mockingProgress = mock(MockingProgress.class);
        testMockHandler = new MockHandler<>(mockSettings);
        testMockHandler.invocationContainerImpl = invocationContainer;
        testMockHandler.matchersBinder = matchersBinder;
        testMockHandler.mockingProgress = mockingProgress;
    }

    @Test
    public void testHandle_whenStubbingVoidsWithStubVoidOrDoAnswerStyle_thenSetMethodForStubbing() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(invocationContainer.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);

        Object result = testMockHandler.handle(invocation);

        assertNull(result);
        verify(invocationContainer).setMethodForStubbing(invocationMatcher);
    }

    @Test
    public void testHandle_whenVerificationModeIsNotNull_thenVerifyWithVerificationData() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        when(invocationContainer.getInvocations()).thenReturn(mock(List.class));

        Object result = testMockHandler.handle(invocation);

        assertNull(result);
        verify(mockingProgress).validateState();
        verify(verificationMode).verify(any(VerificationDataImpl.class));
    }

    @Test
    public void testHandle_whenStubbedInvocationIsNotNull_thenCaptureArgumentsAndAnswerInvocation() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        when(invocationContainer.findAnswerFor(invocation)).thenReturn(stubbedInvocation);

        Object result = testMockHandler.handle(invocation);

        assertNull(result);
        verify(stubbedInvocation).captureArgumentsFrom(invocation);
        verify(invocationContainer, never()).resetInvocationForPotentialStubbing(invocationMatcher);
        verify(stubbedInvocation).answer(invocation);
    }

    @Test
    public void testHandle_whenStubbedInvocationIsNull_thenAnswerWithDefaultAnswerAndResetInvocationForPotentialStubbing() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        Answer<?> defaultAnswer = mock(Answer.class);
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        when(invocationContainer.findAnswerFor(invocation)).thenReturn(null);

        Object result = testMockHandler.handle(invocation);

        assertNotNull(result);
        verify(defaultAnswer).answer(invocation);
        verify(invocationContainer).resetInvocationForPotentialStubbing(invocationMatcher);
    }

    @Test
    public void testVoidMethodStubbable_returnsNewVoidMethodStubbableImpl() {
        String mock = "mock";
        VoidMethodStubbableImpl<String> expectedVoidMethodStubbable = mock(VoidMethodStubbableImpl.class);
        when(invocationContainer.voidMethodStubbable(mock)).thenReturn(expectedVoidMethodStubbable);

        VoidMethodStubbable<String> result = testMockHandler.voidMethodStubbable(mock);

        assertEquals(expectedVoidMethodStubbable, result);
        verify(invocationContainer).voidMethodStubbable(mock);
    }

    @Test
    public void testGetMockSettings_returnsMockSettings() {
        MockSettingsImpl result = testMockHandler.getMockSettings();

        assertEquals(mockSettings, result);
    }

    @Test
    public void testSetAnswersForStubbing_setsAnswersForStubbingInInvocationContainer() {
        List<Answer> answers = mock(List.class);

        testMockHandler.setAnswersForStubbing(answers);

        verify(invocationContainer).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer_returnsInvocationContainer() {
        InvocationContainer result = testMockHandler.getInvocationContainer();

        assertEquals(invocationContainer, result);
    }
}