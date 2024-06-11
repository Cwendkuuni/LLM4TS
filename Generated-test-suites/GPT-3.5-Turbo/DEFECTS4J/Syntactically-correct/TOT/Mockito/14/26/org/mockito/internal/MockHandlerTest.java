import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationData;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;

    @Before
    public void setUp() {
        mockHandler = new MockHandler<>(new MockSettingsImpl());
        mockHandler.mockingProgress = mock(MockingProgress.class);
        mockHandler.invocationContainerImpl = mock(InvocationContainerImpl.class);
    }

    @Test
    public void handle_WhenAnswersForStubbingExist_ShouldSetMethodForStubbing() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(mockHandler.invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(mockHandler.matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        assertNull(result);
        verify(mockHandler.invocationContainerImpl).setMethodForStubbing(invocationMatcher);
    }

    @Test
    public void handle_WhenVerificationModeNotNull_ShouldVerifyCorrectMock() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        VerificationData verificationData = mock(VerificationData.class);
        when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(mock(VerificationMode.class));
        when(mockHandler.matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(mockHandler.invocationContainerImpl.getInvocations()).thenReturn(new ArrayList<>());

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        assertNull(result);
        verify(verificationData).verify();
    }

    @Test
    public void handle_WhenStubbedInvocationExists_ShouldAnswerWithStubbedInvocation() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        when(mockHandler.mockSettings.getDefaultAnswer()).thenReturn(mock(Answer.class));
        when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        assertNotNull(result);
        verify(stubbedInvocation).captureArgumentsFrom(invocation);
        verify(stubbedInvocation).answer(invocation);
    }

    @Test
    public void handle_WhenStubbedInvocationNull_ShouldAnswerWithDefaultAnswer() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(mockHandler.mockSettings.getDefaultAnswer()).thenReturn(mock(Answer.class));
        when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        assertNotNull(result);
        verify(mockHandler.invocationContainerImpl).resetInvocationForPotentialStubbing(any());
    }

    @Test
    public void voidMethodStubbable_WithMock_ShouldReturnVoidMethodStubbable() {
        // Arrange
        Object mock = new Object();

        // Act
        VoidMethodStubbable<?> result = mockHandler.voidMethodStubbable(mock);

        // Assert
        assertNotNull(result);
        assertEquals(mock, result.getMock());
        assertEquals(mockHandler.invocationContainerImpl, result.getInvocationContainer());
    }

    @Test
    public void setAnswersForStubbing_WithAnswers_ShouldSetAnswersForStubbing() {
        // Arrange
        List<Answer> answers = new ArrayList<>();

        // Act
        mockHandler.setAnswersForStubbing(answers);

        // Assert
        verify(mockHandler.invocationContainerImpl).setAnswersForStubbing(answers);
    }

    @Test
    public void getInvocationContainer_ShouldReturnInvocationContainer() {
        // Act
        InvocationContainer result = mockHandler.getInvocationContainer();

        // Assert
        assertNotNull(result);
        assertEquals(mockHandler.invocationContainerImpl, result);
    }
}