import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;
    private MockHandlerInterface<Object> oldMockHandler;
    private Invocation invocation;
    private InvocationMatcher invocationMatcher;
    private VerificationMode verificationMode;

    @Before
    public void setup() {
        mockHandler = new MockHandler<>(new MockSettingsImpl());
        oldMockHandler = mock(MockHandlerInterface.class);
        invocation = mock(Invocation.class);
        invocationMatcher = mock(InvocationMatcher.class);
        verificationMode = mock(VerificationMode.class);
    }

    @Test
    public void testHandle_StubbingVoidsWithStubVoid() throws Throwable {
        when(mockHandler.invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(mockHandler.matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);

        mockHandler.handle(invocation);

        verify(mockHandler.invocationContainerImpl).setMethodForStubbing(invocationMatcher);
        verifyZeroInteractions(invocation);
    }

    @Test
    public void testHandle_StubbingVoidsWithDoAnswer() throws Throwable {
        when(mockHandler.invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(mockHandler.matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);

        mockHandler.handle(invocation);

        verify(mockHandler.invocationContainerImpl).setMethodForStubbing(invocationMatcher);
        verifyZeroInteractions(invocation);
    }

    @Test
    public void testHandle_VerificationModeIsNull() throws Throwable {
        when(mockHandler.invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockHandler.matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(null);

        mockHandler.handle(invocation);

        verify(mockHandler.invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockHandler.invocationContainerImpl).findAnswerFor(invocation);
        verify(mockHandler.invocationContainerImpl).resetInvocationForPotentialStubbing(invocationMatcher);
        verify(mockHandler.mockSettings.getDefaultAnswer()).answer(invocation);
        verifyZeroInteractions(invocation);
    }

    @Test
    public void testHandle_VerificationModeIsNotNull() throws Throwable {
        when(mockHandler.invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockHandler.matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(mockHandler.invocationContainerImpl.getInvocations()).thenReturn(someListOfInvocations);

        mockHandler.handle(invocation);

        verify(mockHandler.invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockHandler.invocationContainerImpl).findAnswerFor(invocation);
        verify(mockHandler.mockingProgress).validateState();
        verify(mockHandler.mockingProgress).pullVerificationMode();
        verify(mockHandler.invocationContainerImpl).getInvocations();
        verify(verificationMode).verify(any());
        verifyZeroInteractions(invocation);
    }

    @Test
    public void testHandle_AnswerIsNotNull() throws Throwable {
        Answer<Object> answer = mock(Answer.class);
        when(mockHandler.invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockHandler.matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(mockHandler.mockSettings.getDefaultAnswer()).thenReturn(answer);
        when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);

        mockHandler.handle(invocation);

        verify(mockHandler.invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockHandler.invocationContainerImpl).findAnswerFor(invocation);
        verify(mockHandler.invocationContainerImpl).resetInvocationForPotentialStubbing(invocationMatcher);
        verify(answer).answer(invocation);
        verifyZeroInteractions(invocation);
    }

    @Test
    public void testVoidMethodStubbable() {
        Object mock = new Object();
        VoidMethodStubbable stubbable = mockHandler.voidMethodStubbable(mock);

        assertNotNull(stubbable);
    }

    @Test
    public void testGetMockSettings() {
        MockSettingsImpl settings = mockHandler.getMockSettings();

        assertNotNull(settings);
    }

    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = mock(List.class);
        mockHandler.setAnswersForStubbing(answers);

        verify(mockHandler.invocationContainerImpl).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer() {
        InvocationContainer container = mockHandler.getInvocationContainer();

        assertNotNull(container);
        assertEquals(mockHandler.invocationContainerImpl, container);
    }
}