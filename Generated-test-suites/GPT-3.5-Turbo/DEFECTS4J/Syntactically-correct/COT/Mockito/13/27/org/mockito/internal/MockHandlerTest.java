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
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class MockHandlerTest {

    @Test
    public void testHandle_whenInvocationContainerHasAnswersForStubbing_shouldSetMethodForStubbingAndReturnNull() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);

        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;

        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(mock(InvocationMatcher.class));

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        assertNull(result);
        verify(invocationContainerImpl).setMethodForStubbing(any(InvocationMatcher.class));
    }

    @Test
    public void testHandle_whenVerificationModeIsNotNullAndIsMockAwareVerificationMode_shouldVerifyInvocations() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        VerificationMode verificationMode = mock(MockAwareVerificationMode.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);

        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;

        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(invocation.getMock()).thenReturn(mock(Object.class));

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        assertNull(result);
        verify(invocationContainerImpl, never()).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(verificationMode).verify(any(VerificationDataImpl.class));
    }

    @Test
    public void testHandle_whenVerificationModeIsNotNullAndIsNotMockAwareVerificationMode_shouldNotVerifyInvocations() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);

        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;

        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        assertNull(result);
        verify(verificationMode, never()).verify(any(VerificationDataImpl.class));
    }

    @Test
    public void testHandle_whenInvocationIsNotStubbed_shouldUseDefaultAnswer() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        Answer<Object> defaultAnswer = mock(Answer.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);

        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
        mockHandler.mockSettings = mockSettings;

        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        when(defaultAnswer.answer(invocation)).thenReturn(new Object());

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        assertNotNull(result);
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(any(InvocationMatcher.class));
    }

    @Test
    public void testVoidMethodStubbable_shouldReturnVoidMethodStubbableImpl() {
        // Arrange
        Object mock = mock(Object.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);

        MockHandler<Object> mockHandler = new MockHandler<>();
        mockHandler.invocationContainerImpl = invocationContainerImpl;

        // Act
        VoidMethodStubbable<?> stubbable = mockHandler.voidMethodStubbable(mock);

        // Assert
        assertTrue(stubbable instanceof VoidMethodStubbableImpl);
        assertSame(mock, ((VoidMethodStubbableImpl<?>) stubbable).getMock());
        assertSame(invocationContainerImpl, ((VoidMethodStubbableImpl<?>) stubbable).getInvocationContainer());
    }
}