import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import java.util.List;

public class MockHandlerTest {

    private MockHandler<Integer> mockHandler;
    private MockSettingsImpl mockSettings;
    private InvocationContainerImpl invocationContainer;
    private MatchersBinder matchersBinder;
    private MockingProgress mockingProgress;

    @Before
    public void setup() {
        mockSettings = new MockSettingsImpl();
        mockingProgress = new ThreadSafeMockingProgress();
        matchersBinder = new MatchersBinder();
        invocationContainer = new InvocationContainerImpl(mockingProgress);
        mockHandler = new MockHandler<>(mockSettings);
        mockHandler.invocationContainerImpl = invocationContainer;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
    }

    @Test
    public void testHandle_withStubbingVoid() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        OngoingStubbingImpl<Integer> ongoingStubbing = mock(OngoingStubbingImpl.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);

        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(invocationContainer.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(stubbedInvocation.answer(invocation)).thenReturn(1);

        Object result = mockHandler.handle(invocation);

        verify(invocationContainer).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockingProgress).reportOngoingStubbing(ongoingStubbing);
        verify(stubbedInvocation).captureArgumentsFrom(invocation);
        verify(stubbedInvocation).answer(invocation);
        verify(invocationContainer, never()).resetInvocationForPotentialStubbing(invocationMatcher);
        assert(result instanceof Integer);
        assertEquals(1, result);
    }

    @Test
    public void testHandle_withoutStubbingVoid() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        OngoingStubbingImpl<Integer> ongoingStubbing = mock(OngoingStubbingImpl.class);

        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(invocationContainer.findAnswerFor(invocation)).thenReturn(null);
        when(mockSettings.getDefaultAnswer().answer(invocation)).thenReturn(2);

        Object result = mockHandler.handle(invocation);

        verify(invocationContainer).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockingProgress).reportOngoingStubbing(ongoingStubbing);
        verify(invocationContainer).resetInvocationForPotentialStubbing(invocationMatcher);
        assert(result instanceof Integer);
        assertEquals(2, result);
    }

    @Test
    public void testHandle_withVerificationMode() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        OngoingStubbingImpl<Integer> ongoingStubbing = mock(OngoingStubbingImpl.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        MockAwareVerificationMode verificationMode = mock(MockAwareVerificationMode.class);
        VerificationDataImpl verificationData = mock(VerificationDataImpl.class);

        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(verificationMode.getMock()).thenReturn(invocation.getMock());
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(invocationContainer.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(stubbedInvocation.answer(invocation)).thenReturn(3);
        when(verificationData.getVerificationMode()).thenReturn(verificationMode);

        Object result = mockHandler.handle(invocation);

        verify(invocationContainer, never()).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockingProgress, never()).reportOngoingStubbing(ongoingStubbing);
        verify(invocationContainer, never()).resetInvocationForPotentialStubbing(invocationMatcher);
        verify(verificationMode).verify(verificationData);
        assert(result instanceof Integer);
        assertEquals(3, result);
    }

    @Test
    public void testVoidMethodStubbable() {
        Integer mock = 4;
        VoidMethodStubbable stubbable = mockHandler.voidMethodStubbable(mock);
        assert(stubbable != null);
    }

    @Test
    public void testGetMockSettings() {
        MockSettingsImpl settings = mockHandler.getMockSettings();
        assert(settings != null);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = mock(List.class);
        mockHandler.setAnswersForStubbing(answers);
        verify(invocationContainerImpl).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer() {
        InvocationContainer container = mockHandler.getInvocationContainer();
        assert(container != null);
    }
}