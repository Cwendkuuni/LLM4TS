import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
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
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

import java.util.List;

import static org.mockito.Mockito.*;

public class MockHandlerTest {

    private MockHandler<MockHandlerTest> mockHandler;
    private MockSettingsImpl mockSettings;
    private InvocationContainerImpl invocationContainerImpl;
    private MatchersBinder matchersBinder;
    private MockingProgress mockingProgress;

    @Before
    public void setup() {
        mockSettings = mock(MockSettingsImpl.class);
        invocationContainerImpl = mock(InvocationContainerImpl.class);
        matchersBinder = mock(MatchersBinder.class);
        mockingProgress = mock(ThreadSafeMockingProgress.class);
        mockHandler = new MockHandler<>(mockSettings);
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
    }

    @Test
    public void testHandle() throws Throwable {
        // Setup
        Invocation invocation = mock(Invocation.class);
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);

        // Run
        mockHandler.handle(invocation);

        // Verify
        verify(invocationContainerImpl).setMethodForStubbing(invocationMatcher);
    }

    @Test
    public void testHandle_WithVerificationMode() throws Throwable {
        // Setup
        Invocation invocation = mock(Invocation.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        List<Invocation> invocations = mock(List.class);
        when(invocationContainerImpl.getInvocations()).thenReturn(invocations);
        VerificationDataImpl verificationData = mock(VerificationDataImpl.class);
        whenNew(VerificationDataImpl.class).withArguments(invocations, invocationMatcher).thenReturn(verificationData);

        // Run
        mockHandler.handle(invocation);

        // Verify
        verify(verificationData).verify(verificationMode);
    }

    @Test
    public void testHandle_NoStubbedInvocation() throws Throwable {
        // Setup
        Invocation invocation = mock(Invocation.class);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        when(mockSettings.getDefaultAnswer()).thenReturn(mock(Answer.class));

        // Run
        mockHandler.handle(invocation);

        // Verify
        verify(mockSettings).getDefaultAnswer();
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(any());
    }

    @Test
    public void testHandle_WithStubbedInvocation() throws Throwable {
        // Setup
        Invocation invocation = mock(Invocation.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);

        // Run
        mockHandler.handle(invocation);

        // Verify
        verify(stubbedInvocation).captureArgumentsFrom(invocation);
        verify(stubbedInvocation).answer(invocation);
    }

    @Test
    public void testVoidMethodStubbable() {
        // Setup
        Object mock = new Object();

        // Run
        VoidMethodStubbable stubbable = mockHandler.voidMethodStubbable(mock);

        // Verify
        assertNotNull(stubbable);
        assertEquals(VoidMethodStubbableImpl.class, stubbable.getClass());
    }

    @Test
    public void testGetMockSettings() {
        // Run
        MockSettingsImpl settings = mockHandler.getMockSettings();

        // Verify
        assertNotNull(settings);
        assertEquals(mockSettings, settings);
    }

    @Test
    public void testSetAnswersForStubbing() {
        // Setup
        List<Answer> answers = mock(List.class);

        // Run
        mockHandler.setAnswersForStubbing(answers);

        // Verify
        verify(invocationContainerImpl).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer() {
        // Run
        InvocationContainer container = mockHandler.getInvocationContainer();

        // Verify
        assertNotNull(container);
        assertEquals(invocationContainerImpl, container);
    }
}