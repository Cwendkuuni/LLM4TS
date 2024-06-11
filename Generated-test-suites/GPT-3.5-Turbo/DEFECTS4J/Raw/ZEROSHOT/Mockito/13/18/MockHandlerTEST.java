import org.mockito.*;
import org.junit.*;
import java.util.List;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;
    private MockSettingsImpl mockSettings;
    private MockingProgress mockingProgress;
    private MatchersBinder matchersBinder;
    private InvocationContainerImpl invocationContainerImpl;

    @Before
    public void setUp() {
        mockSettings = Mockito.mock(MockSettingsImpl.class);
        mockingProgress = Mockito.mock(ThreadSafeMockingProgress.class);
        matchersBinder = Mockito.mock(MatchersBinder.class);
        invocationContainerImpl = Mockito.mock(InvocationContainerImpl.class);
        mockHandler = new MockHandler<>(mockSettings);
        mockHandler.mockingProgress = mockingProgress;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.invocationContainerImpl = invocationContainerImpl;
    }

    @Test
    public void testHandle_withAnswerForStubbing() throws Throwable {
        Invocation invocation = Mockito.mock(Invocation.class);
        Answer answer = Mockito.mock(Answer.class);
        InvocationMatcher invocationMatcher = Mockito.mock(InvocationMatcher.class);
        OngoingStubbingImpl ongoingStubbing = Mockito.mock(OngoingStubbingImpl.class);
        StubbedInvocationMatcher stubbedInvocation = Mockito.mock(StubbedInvocationMatcher.class);
        Object expected = new Object();

        Mockito.when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        Mockito.when(matchersBinder.bindMatchers(Mockito.any(), Mockito.any())).thenReturn(invocationMatcher);
        Mockito.when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        Mockito.when(stubbedInvocation.captureArgumentsFrom(invocation)).thenReturn(null);
        Mockito.when(stubbedInvocation.answer(invocation)).thenReturn(expected);

        Object result = mockHandler.handle(invocation);

        Mockito.verify(invocationContainerImpl).setMethodForStubbing(invocationMatcher);
        Assert.assertNull(result);
    }

    @Test
    public void testHandle_noAnswerForStubbing() throws Throwable {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = Mockito.mock(InvocationMatcher.class);
        VerificationMode verificationMode = Mockito.mock(VerificationMode.class);
        OngoingStubbingImpl ongoingStubbing = Mockito.mock(OngoingStubbingImpl.class);
        Object expected = new Object();

        Mockito.when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        Mockito.when(matchingProgress.pullVerificationMode()).thenReturn(verificationMode);
        Mockito.when(matchersBinder.bindMatchers(Mockito.any(), Mockito.any())).thenReturn(invocationMatcher);
        Mockito.when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        Mockito.when(mockSettings.getDefaultAnswer().answer(invocation)).thenReturn(expected);
        Mockito.when(invocationContainerImpl.resetInvocationForPotentialStubbing(invocationMatcher)).thenReturn(null);

        Object result = mockHandler.handle(invocation);

        Mockito.verify(invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
        Mockito.verify(mockingProgress).reportOngoingStubbing(ongoingStubbing);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testVoidMethodStubbable() {
        Object mock = new Object();
        VoidMethodStubbableImpl stubbable = Mockito.mock(VoidMethodStubbableImpl.class);
        
        Mockito.when(new VoidMethodStubbableImpl(mock, invocationContainerImpl)).thenReturn(stubbable);

        VoidMethodStubbable stub = mockHandler.voidMethodStubbable(mock);

        Mockito.verify(stubbable).equals(stub);
    }

    @Test
    public void testGetMockSettings() {
        MockSettingsImpl expected = Mockito.mock(MockSettingsImpl.class);

        Mockito.when(mockSettings.getDefaultAnswer()).thenReturn(expected);

        MockSettingsImpl result = mockHandler.getMockSettings();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = Mockito.mock(List.class);

        mockHandler.setAnswersForStubbing(answers);

        Mockito.verify(invocationContainerImpl).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer() {
        InvocationContainer expected = Mockito.mock(InvocationContainer.class);

        Mockito.when(mockHandler.getInvocationContainer()).thenReturn(expected);

        InvocationContainer result = mockHandler.getInvocationContainer();

        Assert.assertEquals(expected, result);
    }

}