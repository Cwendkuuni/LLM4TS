import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationDataImpl;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;
    private InvocationContainerImpl invocationContainer;
    private MatchersBinder matchersBinder;
    private MockingProgress mockingProgress;
    private MockSettingsImpl mockSettings;

    @Before
    public void setup() {
        mockSettings = mock(MockSettingsImpl.class);
        invocationContainer = mock(InvocationContainerImpl.class);
        matchersBinder = mock(MatchersBinder.class);
        mockingProgress = mock(ThreadSafeMockingProgress.class);
        mockHandler = new MockHandler<>(mockSettings);
        mockHandler.invocationContainerImpl = invocationContainer;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
    }

    @Test
    public void testHandle_WithAnswersForStubbing() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(invocationContainer.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        verify(invocationContainer).setMethodForStubbing(invocationMatcher);
        verifyZeroInteractions(invocationContainer.getInvocations());
        verifyZeroInteractions(invocationContainer.findAnswerFor(any(Invocation.class)));
        verifyZeroInteractions(mockSettings.getDefaultAnswer());
        verifyZeroInteractions(invocationContainer.resetInvocationForPotentialStubbing(any(InvocationMatcher.class)));
        verify(mockingProgress).validateState();
        verify(mockingProgress, never()).pullVerificationMode();
        verify(mockingProgress).reportOngoingStubbing(any(OngoingStubbingImpl.class));
        verifyZeroInteractions(invocationMatcher);
        verifyZeroInteractions(invocationContainer.resetInvocationForPotentialStubbing(any(InvocationMatcher.class)));
        assertNull(result);
    }

    @Test
    public void testHandle_WithVerificationMode() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        VerificationDataImpl data = mock(VerificationDataImpl.class);
        when(invocationContainer.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(mock(VerificationMode.class));
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(data.getInvocationContainer()).thenReturn(invocationContainer);
        when(invocationContainer.getInvocations()).thenReturn(new ArrayList<>());
        when(invocationContainer.findAnswerFor(any(Invocation.class))).thenReturn(null);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        verify(queryContainer).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockingProgress).pullVerificationMode();
        verify(mockingProgress).validateState();
        verify(invocationMatcher).getInvocation();
        verify(invocationContainer).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockingProgress).pullVerificationMode();
        verify(mockingProgress).validateState();
        verify(invocationContainer).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockingProgress, never()).reportOngoingStubbing(any(OngoingStubbingImpl.class));
        verifyZeroInteractions(mockSettings.getDefaultAnswer());
        verifyZeroInteractions(invocationContainer.resetInvocationForPotentialStubbing(any(InvocationMatcher.class)));
        verifyZeroInteractions(invocationMatcher);
        verifyZeroInteractions(invocationContainer.resetInvocationForPotentialStubbing(any(InvocationMatcher.class)));
        assertNull(result);
    }

    @Test
    public void testHandle_WithStubbedInvocation() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        when(invocationContainer.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(invocationContainer.findAnswerFor(any(Invocation.class))).thenReturn(stubbedInvocation);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        verify(queryContainer).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockingProgress).pullVerificationMode();
        verify(mockingProgress).validateState();
        verify(invocationMatcher).getInvocation();
        verify(invocationContainer).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockingProgress, never()).pullVerificationMode();
        verify(mockingProgress, never()).validateState();
        verify(invocationMatcher, never()).getInvocation();
        verify(invocationContainer, never()).setInvocationForPotentialStubbing(invocationMatcher);
        verify(stubbedInvocation).captureArgumentsFrom(invocation);
        verify(stubbedInvocation).answer(invocation);
        assertNull(result);
    }

    @Test
    public void testHandle_WithoutStubbedInvocation() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(invocationContainer.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(invocationContainer.findAnswerFor(any(Invocation.class))).thenReturn(null);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        verify(queryContainer).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockingProgress).pullVerificationMode();
        verify(mockingProgress).validateState();
        verify(invocationMatcher).getInvocation();
        verify(invocationContainer).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockingProgress, never()).pullVerificationMode();
        verify(mockingProgress, never()).validateState();
        verifyZeroInteractions(invocationMatcher.captureArgumentsFrom(any(Invocation.class)));
        verify(mockSettings.getDefaultAnswer()).answer(invocation);
        verify(invocationContainer).resetInvocationForPotentialStubbing(invocationMatcher);
        assertNull(result);
    }

    @Test
    public void testVoidMethodStubbable() {
        // Arrange
        Object mock = new Object();

        // Act
        VoidMethodStubbable<Object> stubbable = mockHandler.voidMethodStubbable(mock);

        // Assert
        assertNotNull(stubbable);
    }

    @Test
    public void testSetAnswersForStubbing() {
        // Arrange
        List<Answer> answers = new ArrayList<>();

        // Act
        mockHandler.setAnswersForStubbing(answers);

        // Assert
        verify(invocationContainer).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer() {
        // Act
        InvocationContainer result = mockHandler.getInvocationContainer();

        // Assert
        assertNotNull(result);
        assertSame(invocationContainer, result);
    }
}