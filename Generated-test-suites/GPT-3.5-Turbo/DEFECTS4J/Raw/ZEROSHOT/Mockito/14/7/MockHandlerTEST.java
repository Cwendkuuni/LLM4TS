import org.junit.Test;
import java.util.List;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainer;
import static org.mockito.Mockito.*;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.stubbing.VoidMethodStubbableImpl;
import org.mockito.internal.verification.VerificationDataImpl;

public class MockHandlerTest {
    @Test
    public void testHandle() throws Throwable {
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);

        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(Boolean.TRUE);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(null);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(matchingContainerImpl.setMethodForStubbing(any(InvocationMatcher.class))).thenReturn(null);
        when(mockSettings.getDefaultAnswer()).thenReturn(null);
        when(invocationContainerImpl.findAnswerFor(any(Invocation.class))).thenReturn(stubbedInvocation);
        when(stubbedInvocation.captureArgumentsFrom(any(Invocation.class))).thenReturn(null);
        when(stubbedInvocation.answer(any(Invocation.class))).thenReturn(null);

        MockHandler mockHandler = new MockHandler(mockSettings);
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;

        Object result = mockHandler.handle(invocation);
        mockSettings.getDefaultAnswer();
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(any(InvocationMatcher.class));
    }

    @Test
    public void testVoidMethodStubbable() {
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        Object mock = new Object();
        VoidMethodStubbableImpl voidMethodStubbableImpl = mock(VoidMethodStubbableImpl.class);

        when(voidMethodStubbableImpl(voidMethodStubbableImpl)).thenReturn(voidMethodStubbableImpl);

        MockHandler mockHandler = new MockHandler();
        mockHandler.invocationContainerImpl = invocationContainerImpl;

        VoidMethodStubbable result = mockHandler.voidMethodStubbable(mock);
        assertEquals(voidMethodStubbableImpl, result);
    }

    @Test
    public void testGetMockSettings() {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);

        MockHandler mockHandler = new MockHandler(mockSettings);

        MockSettingsImpl result = mockHandler.getMockSettings();
        assertEquals(mockSettings, result);
    }

    @Test
    public void testSetAnswersForStubbing() {
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        List answers = mock(List.class);

        MockHandler mockHandler = new MockHandler();
        mockHandler.invocationContainerImpl = invocationContainerImpl;

        mockHandler.setAnswersForStubbing(answers);
        verify(invocationContainerImpl).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer() {
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);

        MockHandler mockHandler = new MockHandler();
        mockHandler.invocationContainerImpl = invocationContainerImpl;

        InvocationContainer result = mockHandler.getInvocationContainer();
        assertEquals(invocationContainerImpl, result);
    }
}