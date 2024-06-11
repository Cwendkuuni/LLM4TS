import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class MockHandlerTest {

    private MockHandler mockHandler;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockHandler = new MockHandler();
    }

    @Test
    public void testHandle_InvocationsForStubbing_ReturnsNull() throws Throwable {
        // Arrange
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        mockHandler.invocationContainerImpl = invocationContainer;
        Invocation invocation = mock(Invocation.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        mockHandler.matchersBinder = matchersBinder;
        MockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        mockHandler.mockingProgress = mockingProgress;

        when(invocationContainer.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(mock(InvocationMatcher.class));

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        verify(invocationContainer).setMethodForStubbing(any(InvocationMatcher.class));
        verifyNoMoreInteractions(invocationContainer);
        assertNull(result);
    }

    @Test
    public void testHandle_VerificationModeNotNull_CallsVerify() throws Throwable {
        // Arrange
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        mockHandler.invocationContainerImpl = invocationContainer;
        Invocation invocation = mock(Invocation.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        mockHandler.matchersBinder = matchersBinder;
        MockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        mockHandler.mockingProgress = mockingProgress;
        VerificationMode verificationMode = mock(VerificationMode.class);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);

        when(matchersBinder.bindMatchers(any(), any())).thenReturn(mock(InvocationMatcher.class));

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        verify(mockingProgress).validateState();
        verifyNoMoreInteractions(mockingProgress);
        verify(verificationMode).verify(any(VerificationDataImpl.class));
        verifyNoMoreInteractions(verificationMode);
        assertNull(result);
    }

    @Test
    public void testHandle_InvocationNotStubbed_ReturnsDefaultAnswer() throws Throwable {
        // Arrange
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        mockHandler.invocationContainerImpl = invocationContainer;
        Invocation invocation = mock(Invocation.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        mockHandler.matchersBinder = matchersBinder;
        MockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        mockHandler.mockingProgress = mockingProgress;
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        Answer defaultAnswer = mock(Answer.class);
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);
        mockHandler.mockSettings = mockSettings;

        when(matchersBinder.bindMatchers(any(), any())).thenReturn(mock(InvocationMatcher.class));

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        verify(invocationContainer).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verifyNoMoreInteractions(invocationContainer);
        verify(invocationContainer).resetInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verifyNoMoreInteractions(invocationContainer);
        assertEquals(defaultAnswer, result);
    }

    @Test
    public void testVoidMethodStubbable_ReturnsVoidMethodStubbableImpl() {
        // Arrange
        Object mock = new Object();
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        mockHandler.invocationContainerImpl = invocationContainer;

        // Act
        VoidMethodStubbable result = mockHandler.voidMethodStubbable(mock);

        // Assert
        assertNotNull(result);
        assertEquals(mock, result.mock);
        assertEquals(invocationContainer, result.invocationContainer);
    }

    @Test
    public void testGetMockSettings_ReturnsMockSettingsImpl() {
        // Arrange
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockHandler.mockSettings = mockSettings;

        // Act
        MockSettingsImpl result = mockHandler.getMockSettings();

        // Assert
        assertEquals(mockSettings, result);
    }

    @Test
    public void testSetAnswersForStubbing_ProvidesAnswersToInvocationContainer() {
        // Arrange
        List<Answer> answers = new ArrayList<>();
        mockHandler.invocationContainerImpl = mock(InvocationContainerImpl.class);

        // Act
        mockHandler.setAnswersForStubbing(answers);

        // Assert
        verify(mockHandler.invocationContainerImpl).setAnswersForStubbing(answers);
        verifyNoMoreInteractions(mockHandler.invocationContainerImpl);
    }

    @Test
    public void testGetInvocationContainer_ReturnsInvocationContainerImpl() {
        // Arrange
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        mockHandler.invocationContainerImpl = invocationContainer;

        // Act
        InvocationContainerImpl result = mockHandler.getInvocationContainer();

        // Assert
        assertEquals(invocationContainer, result);
    }
}