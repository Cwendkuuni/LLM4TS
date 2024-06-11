import org.junit.Test;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockHandlerTest {

    @Test
    public void handle_stubbingVoid_invocationContainerImplSetMethodForStubbingCalled() throws Throwable {
        // Arrange
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        Invocation invocation = mock(Invocation.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(mock(InvocationMatcher.class));
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(mock(MatchersBinder.class));

        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.mockingProgress = mockingProgress;

        // Act
        mockHandler.handle(invocation);

        // Assert
        verify(invocationContainerImpl).setMethodForStubbing(any(InvocationMatcher.class));
    }

    @Test
    public void handle_verificationModeNotSet_nothingHappens() throws Throwable {
        // Arrange
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        Invocation invocation = mock(Invocation.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(mock(InvocationMatcher.class));
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(mock(MatchersBinder.class));

        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.mockingProgress = mockingProgress;

        // Act
        mockHandler.handle(invocation);

        // Assert
        verifyZeroInteractions(invocationContainerImpl);
    }

    @Test
    public void handle_verificationModeSet_dataPassedToVerificationMode() throws Throwable {
        // Arrange
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        Invocation invocation = mock(Invocation.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(mock(MatchersBinder.class));

        MockAwareVerificationMode verificationMode = mock(MockAwareVerificationMode.class);
        when(verificationMode.getMock()).thenReturn(invocation.getMock());

        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.mockingProgress = mockingProgress;
        mockingProgress.pullVerificationMode();
        mockingProgress.reportOngoingStubbing(mock(OngoingStubbingImpl.class));

        // Act
        mockHandler.handle(invocation);

        // Assert
        verify(verificationMode).verify(any(VerificationDataImpl.class));
    }

    @Test
    public void handle_stubbedInvocationFound_argumentsCapturedAndAnswerCalled() throws Throwable {
        // Arrange
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        Invocation invocation = mock(Invocation.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(mock(InvocationMatcher.class));
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(mock(MatchersBinder.class));

        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);

        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.mockingProgress = mockingProgress;
        mockingProgress.reportOngoingStubbing(mock(OngoingStubbingImpl.class));

        // Act
        mockHandler.handle(invocation);

        // Assert
        verify(stubbedInvocation).captureArgumentsFrom(invocation);
        verify(stubbedInvocation).answer(invocation);
    }

    @Test
    public void handle_stubbedInvocationNotFound_defaultAnswerCalledAndInvocationContainerReset() throws Throwable {
        // Arrange
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        Invocation invocation = mock(Invocation.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(mock(InvocationMatcher.class));
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(mock(MatchersBinder.class));

        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.mockingProgress = mockingProgress;
        mockingProgress.reportOngoingStubbing(mock(OngoingStubbingImpl.class));

        // Act
        mockHandler.handle(invocation);

        // Assert
        verify(mockSettings).getDefaultAnswer();
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(any(InvocationMatcher.class));
    }

    @Test
    public void voidMethodStubbable_returnInstanceOfVoidMethodStubbableImpl() {
        // Arrange
        MockHandler<Object> mockHandler = new MockHandler<>();

        // Act
        VoidMethodStubbable<Object> result = mockHandler.voidMethodStubbable(null);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof VoidMethodStubbableImpl);
    }

    @Test
    public void getMockSettings_returnMockSettings() {
        // Arrange
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        // Act
        MockSettingsImpl result = mockHandler.getMockSettings();

        // Assert
        assertEquals(mockSettings, result);
    }

    @Test
    public void setAnswersForStubbing_setAnswersForStubbingOnInvocationContainer() {
        // Arrange
        MockHandler<Object> mockHandler = new MockHandler<>();
        List<Answer> answers = new ArrayList<>();

        // Act
        mockHandler.setAnswersForStubbing(answers);

        // Assert
        assertEquals(answers, mockHandler.getInvocationContainer().getAnswersForStubbing());
    }
}