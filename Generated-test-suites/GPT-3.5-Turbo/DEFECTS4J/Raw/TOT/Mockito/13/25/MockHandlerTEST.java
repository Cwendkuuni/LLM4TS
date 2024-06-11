import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockHandlerTest {

    @Test
    public void testHandle_WhenAnswersForStubbingExist_ReturnsNull() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);

        when(invocationContainer.hasAnswersForStubbing()).thenReturn(true);
        when(invocationContainer.getMethodForStubbing(any(InvocationMatcher.class))).thenReturn(null);

        MockHandler mockHandler = new MockHandler();
        mockHandler.invocationContainerImpl = invocationContainer;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        assertNull(result);
        verify(matchersBinder, times(1)).bindMatchers(any(), eq(invocation));
        verify(invocationContainer, times(1)).setMethodForStubbing(any(InvocationMatcher.class));
    }

    @Test
    public void testHandle_WhenVerificationModeIsNull_DoesNotVerifyData() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);

        when(invocationContainer.hasAnswersForStubbing()).thenReturn(false);
        when(invocationContainer.findAnswerFor(any(Invocation.class))).thenReturn(null);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);

        MockHandler mockHandler = new MockHandler();
        mockHandler.invocationContainerImpl = invocationContainer;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        assertNull(result);
        verify(matchersBinder, times(1)).bindMatchers(any(), eq(invocation));
        verify(invocationContainer, times(1)).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(mockingProgress, times(1)).validateState();
        verify(invocationContainer, times(1)).findAnswerFor(eq(invocation));
    }

    @Test
    public void testHandle_WhenVerificationModeIsMockAwareVerificationMode_VerifiesData() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        MockAwareVerificationMode verificationMode = mock(MockAwareVerificationMode.class);

        when(invocationContainer.hasAnswersForStubbing()).thenReturn(false);
        when(invocationContainer.findAnswerFor(any(Invocation.class))).thenReturn(null);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(verificationMode.getMock()).thenReturn(mockHandler);

        MockHandler mockHandler = new MockHandler();
        mockHandler.invocationContainerImpl = invocationContainer;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        assertNull(result);
        verify(matchersBinder, times(1)).bindMatchers(any(), eq(invocation));
        verify(invocationContainer, times(1)).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(mockingProgress, times(1)).validateState();
        verify(invocationContainer, times(1)).findAnswerFor(eq(invocation));
        verify(verificationMode, times(1)).verify(any(VerificationDataImpl.class));
    }

    @Test
    public void testHandle_WhenStubbedInvocationIsNull_DefaultAnswerIsCalled() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        Answer defaultAnswer = mock(Answer.class);

        when(invocationContainer.hasAnswersForStubbing()).thenReturn(false);
        when(invocationContainer.findAnswerFor(any(Invocation.class))).thenReturn(null);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(invocation.getMock()).thenReturn(mockHandler);
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);
        when(defaultAnswer.answer(any(Invocation.class))).thenReturn(null);

        MockHandler mockHandler = new MockHandler();
        mockHandler.invocationContainerImpl = invocationContainer;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
        mockHandler.mockSettings = mockSettings;

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        assertNull(result);
        verify(matchersBinder, times(1)).bindMatchers(any(), eq(invocation));
        verify(invocationContainer, times(1)).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(mockingProgress, times(1)).validateState();
        verify(invocationContainer, times(1)).findAnswerFor(eq(invocation));
        verify(defaultAnswer, times(1)).answer(eq(invocation));
    }

    @Test
    public void testVoidMethodStubbable_ReturnsInstanceOfVoidMethodStubbable() {
        // Arrange
        T mock = mock(T.class);
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);

        MockHandler mockHandler = new MockHandler();
        mockHandler.invocationContainerImpl = invocationContainer;

        // Act
        VoidMethodStubbable<T> result = mockHandler.voidMethodStubbable(mock);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof VoidMethodStubbableImpl);
    }

    @Test
    public void testSetAnswersForStubbing_SetsAnswersForStubbingInInvocationContainer() {
        // Arrange
        List<Answer> answers = Arrays.asList(mock(Answer.class));
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);

        MockHandler mockHandler = new MockHandler();
        mockHandler.invocationContainerImpl = invocationContainer;

        // Act
        mockHandler.setAnswersForStubbing(answers);

        // Assert
        verify(invocationContainer, times(1)).setAnswersForStubbing(eq(answers));
    }

    @Test
    public void testGetInvocationContainer_ReturnsInvocationContainerImpl() {
        // Arrange
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);

        MockHandler mockHandler = new MockHandler();
        mockHandler.invocationContainerImpl = invocationContainer;

        // Act
        InvocationContainer result = mockHandler.getInvocationContainer();

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof InvocationContainerImpl);
    }
}