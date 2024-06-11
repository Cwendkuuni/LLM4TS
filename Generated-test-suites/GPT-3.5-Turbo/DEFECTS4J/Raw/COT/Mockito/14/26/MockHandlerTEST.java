
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationData;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class MockHandlerTest {
    private MockHandler<MockHandlerInterface> mockHandler;
    private MockHandlerInterface mockObject;

    @Before
    public void setup() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockHandler = new MockHandler<>(mockSettings);
        mockObject = mock(MockHandlerInterface.class, mockHandler);
    }

    @Test
    public void testHandle_InvokesMethodForStubbing() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(new ArgumentMatcherStorage());
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(mockHandler.handle(invocation)).thenCallRealMethod();
        when(mockHandler.getMockSettings()).thenReturn(new MockSettingsImpl());
        mockHandler.handle(invocation);
        verify(invocationContainerImpl).setMethodForStubbing(invocationMatcher);
    }

    @Test
    public void testHandle_PullsVerificationMode_VerifiesData() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        VerificationData verificationData = mock(VerificationData.class);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(new ArgumentMatcherStorage());
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(mockHandler.handle(invocation)).thenCallRealMethod();
        when(invocationContainerImpl.getInvocations()).thenReturn(new ArrayList<>());
        when(mockHandler.getMockSettings()).thenReturn(new MockSettingsImpl());
        when(verificationMode.verify(any())).thenReturn(verificationData);
        mockHandler.handle(invocation);
        verify(verificationData).verify();
    }

    @Test
    public void testHandle_NoVerificationMode_SetsInvocationForPotentialStubbing() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(new ArgumentMatcherStorage());
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(mockHandler.handle(invocation)).thenCallRealMethod();
        when(mockHandler.getMockSettings()).thenReturn(new MockSettingsImpl());
        mockHandler.handle(invocation);
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
    }

    @Test
    public void testHandle_FindsStubbedInvocation_ReturnsStubbedAnswer() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(new ArgumentMatcherStorage());
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(mockHandler.handle(invocation)).thenCallRealMethod();
        when(mockHandler.getMockSettings()).thenReturn(new MockSettingsImpl());
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(new StubbedInvocationMatcher());
        mockHandler.handle(invocation);
        verify(invocationContainerImpl).findAnswerFor(invocation);
    }

    @Test
    public void testHandle_NoStubbedInvocation_ReturnsDefaultAnswer() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(new ArgumentMatcherStorage());
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(mockHandler.handle(invocation)).thenCallRealMethod();
        when(mockHandler.getMockSettings()).thenReturn(new MockSettingsImpl());
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        when(mockSettings.getDefaultAnswer()).thenReturn(new DefaultAnswer());
        mockHandler.handle(invocation);
        verify(mockSettings, atLeastOnce()).getDefaultAnswer();
    }
}
