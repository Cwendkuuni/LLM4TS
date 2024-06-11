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
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {

    private MockHandler mockHandler;

    @Before
    public void setUp() {
        mockHandler = mock(MockHandler.class, CALLS_REAL_METHODS);
    }

    @Test
    public void testHandle_withStubbingVoidMethod_shouldSetMethodForStubbingAndReturnNull() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(mockHandler.hasAnswersForStubbing()).thenReturn(true);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        verify(mockingProgress, times(1)).getArgumentMatcherStorage();
        verify(matchersBinder, times(1)).bindMatchers(any(), any());
        verify(invocationContainerImpl, times(1)).setMethodForStubbing(any());
        assertNull(result);
    }

    @Test
    public void testHandle_withVerificationMode_shouldVerifyInvocationsAndReturnNull() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        verify(mockingProgress, times(1)).pullVerificationMode();
        verify(matchersBinder, times(1)).bindMatchers(any(), any());
        verify(invocationContainerImpl, times(1)).getInvocations();
        verify(verificationMode, times(1)).verify(any(VerificationDataImpl.class));
        assertNull(result);
    }

    @Test
    public void testHandle_withStubbedInvocation_shouldCaptureArgumentsAndReturnStubbedAnswer() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocationMatcher);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        verify(matchersBinder, times(1)).bindMatchers(any(), any());
        verify(stubbedInvocationMatcher, times(1)).captureArgumentsFrom(any());
        verify(stubbedInvocationMatcher, times(1)).answer(any());
        assertEquals(expectedAnswer, result);
    }

    @Test
    public void testHandle_withUnstubbedInvocation_shouldSetDefaultAnswerAndReturnAnswer() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(mockSettings.getDefaultAnswer().answer(invocation)).thenReturn(expectedAnswer);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        verify(matchersBinder, times(1)).bindMatchers(any(), any());
        verify(invocationContainerImpl, times(1)).resetInvocationForPotentialStubbing(any());
        assertEquals(expectedAnswer, result);
    }

    @Test
    public void testVoidMethodStubbable_shouldReturnVoidMethodStubbable() {
        // Arrange
        Object mock = new Object();

        // Act
        VoidMethodStubbable voidMethodStubbable = mockHandler.voidMethodStubbable(mock);

        // Assert
        assertNotNull(voidMethodStubbable);
    }

    @Test
    public void testSetAnswersForStubbing_shouldSetAnswersForStubbing() {
        // Arrange
        List<Answer> answers = new ArrayList<>();

        // Act
        mockHandler.setAnswersForStubbing(answers);

        // Assert
        verify(invocationContainerImpl, times(1)).setAnswersForStubbing(any());
    }

    @Test
    public void testGetInvocationContainer_shouldReturnInvocationContainer() {
        // Act
        InvocationContainer invocationContainer = mockHandler.getInvocationContainer();

        // Assert
        assertEquals(invocationContainerImpl, invocationContainer);
    }
}