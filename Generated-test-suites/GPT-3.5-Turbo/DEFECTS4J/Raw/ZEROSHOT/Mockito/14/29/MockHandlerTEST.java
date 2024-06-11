import org.junit.Before;
import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationMatcher;
import org.mockito.progress.MockingProgress;
import org.mockito.progress.ThreadSafeMockingProgress;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbingImpl;
import org.mockito.verification.VerificationData;
import static org.mockito.Mockito.*;

public class MockHandlerTest {

    private MockHandler mockHandler;
    private MockSettingsImpl mockSettings;
    private MockingProgress mockingProgress;
    private MatchersBinder matchersBinder;
    private InvocationContainerImpl invocationContainerImpl;

    @Before
    public void setUp() {
        mockSettings = mock(MockSettingsImpl.class);
        mockingProgress = new ThreadSafeMockingProgress();
        matchersBinder = new MatchersBinder();
        invocationContainerImpl = new InvocationContainerImpl(mockingProgress);

        mockHandler = new MockHandler(mockSettings);
        mockHandler.mockingProgress = mockingProgress;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.invocationContainerImpl = invocationContainerImpl;
    }

    @Test
    public void testHandle_withValidInvocationForStubbing_shouldSetMethodForStubbing() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);

        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);

        Object result = mockHandler.handle(invocation);

        verify(invocationContainerImpl).setMethodForStubbing(invocationMatcher);
        assertNull(result);
    }

    @Test
    public void testHandle_withVerificationMode_shouldVerify() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        VerificationDataImpl verificationData = mock(VerificationDataImpl.class);
        VerificationMode verificationMode = mock(VerificationMode.class);

        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(invocationContainerImpl.getInvocations()).thenReturn(verificationData.getInvocations());
        when(verificationData.getInvocations()).thenReturn(invocationContainerImpl.getInvocations());

        Object result = mockHandler.handle(invocation);

        verify(verificationMode).verify(verificationData);
        assertNull(result);
    }

    @Test
    public void testHandle_withStubbedInvocation_shouldReturnStubbedAnswer() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);

        when(mockSettings.getDefaultAnswer).thenReturn(mock(Answer.class));
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(mock(InvocationMatcher.class));
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(stubbedInvocation.captureArgumentsFrom(invocation)).thenReturn(null);
        when(stubbedInvocation.answer(invocation)).thenReturn(mock(Object.class));

        Object result = mockHandler.handle(invocation);

        verify(invocationContainerImpl).setInvocationForPotentialStubbing(any());
        verify(stubbedInvocation).captureArgumentsFrom(invocation);
        verify(stubbedInvocation).answer(invocation);
        assertNotNull(result);
    }
    
    @Test
    public void testHandle_withNoStubbedInvocation_shouldReturnDefaultAnswer() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        Answer defaultAnswer = mock(Answer.class);

        when(mockSettings.getDefaultAnswer).thenReturn(defaultAnswer);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(mock(InvocationMatcher.class));
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        when(defaultAnswer.answer(invocation)).thenReturn(mock(Object.class));

        Object result = mockHandler.handle(invocation);

        verify(invocationContainerImpl).setInvocationForPotentialStubbing(any());
        verify(defaultAnswer).answer(invocation);
        assertNotNull(result);
    }

    @Test
    public void testVoidMethodStubbable_shouldReturnVoidMethodStubbableImpl() {
        Object mock = new Object();
        
        VoidMethodStubbable result = mockHandler.voidMethodStubbable(mock);
        
        assertTrue(result instanceof VoidMethodStubbableImpl);
    }

    @Test
    public void testGetMockSettings_shouldReturnMockSettings() {
        MockSettingsImpl result = mockHandler.getMockSettings;

        assertEquals(mockSettings, result);
    }

    @Test
    public void testSetAnswersForStubbing_shouldSetAnswersForStubbing() {
        List<Answer> answers = mock(List.class);

        mockHandler.setAnswersForStubbing(answers);

        verify(invocationContainerImpl).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer_shouldReturnInvocationContainerImpl() {
        InvocationContainerImpl result = mockHandler.getInvocationContainer();

        assertEquals(invocationContainerImpl, result);
    }
}