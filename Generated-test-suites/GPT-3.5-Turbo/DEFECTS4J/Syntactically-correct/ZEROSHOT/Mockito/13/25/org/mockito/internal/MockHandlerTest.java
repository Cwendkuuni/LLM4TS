import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockHandlerTest {

    @Mock
    private MockSettingsImpl mockSettings;

    private MockHandler<String> mockHandler;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockHandler = new MockHandler<>(mockSettings);
    }

    @Test
    public void testHandle_withStubbingVoidMethod() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        ThreadSafeMockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);

        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation))
                .thenReturn(mock(InvocationMatcher.class));

        Object result = mockHandler.handle(invocation);

        assertNull(result);
        verify(invocationContainerImpl).setMethodForStubbing(any(InvocationMatcher.class));
    }

    @Test
    public void testHandle_withVerificationMode() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        ThreadSafeMockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        MockAwareVerificationMode mockAwareVerificationMode = mock(MockAwareVerificationMode.class);

        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation))
                .thenReturn(mock(InvocationMatcher.class));
        when(verificationMode instanceof MockAwareVerificationMode).thenReturn(true);
        when(mockAwareVerificationMode.getMock()).thenReturn(invocation.getMock());

        Object result = mockHandler.handle(invocation);

        assertNull(result);
        verify(invocationContainerImpl, times(0)).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(mockingProgress).validateState();
        verify(mockAwareVerificationMode).getMock();
    }

    @Test
    public void testHandle_withStubbedInvocation() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        ThreadSafeMockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);

        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation))
                .thenReturn(mock(InvocationMatcher.class));
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(stubbedInvocation.answer(invocation)).thenReturn("StubbedReturn");

        Object result = mockHandler.handle(invocation);

        assertEquals("StubbedReturn", result);
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(mockingProgress).reportOngoingStubbing(any(OngoingStubbingImpl.class));
        verify(stubbedInvocation).captureArgumentsFrom(invocation);
    }

    @Test
    public void testHandle_withDefaultAnswer() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        ThreadSafeMockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        Answer defaultAnswer = mock(Answer.class);

        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation))
                .thenReturn(mock(InvocationMatcher.class));
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);
        when(defaultAnswer.answer(invocation)).thenReturn("DefaultReturn");

        Object result = new MockHandler<>(mockSettings).handle(invocation);

        assertEquals("DefaultReturn", result);
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(mockingProgress).reportOngoingStubbing(any(OngoingStubbingImpl.class));
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(any(InvocationMatcher.class));
    }

    @Test
    public void testVoidMethodStubbable() {
        MockHandlerInterface<String> oldMockHandler = mock(MockHandlerInterface.class);

        VoidMethodStubbableImpl<String> result = (VoidMethodStubbableImpl<String>) mockHandler.voidMethodStubbable(oldMockHandler);

        assertNotNull(result);
        assertEquals(oldMockHandler, result.getMock());
        assertEquals(mockHandler.getInvocationContainer(), result.getInvocationContainer());
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

        verify(mockHandler.getInvocationContainer()).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer() {
        InvocationContainerImpl result = (InvocationContainerImpl) mockHandler.getInvocationContainer();

        assertNotNull(result);
        assertEquals(mockHandler.invocationContainerImpl, result);
    }
}