import org.junit.Assert;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class MockHandlerTest {

    @Test
    public void handle_shouldSetMethodForStubbingWhenHasAnswersForStubbing() throws Throwable {
        // Arrange
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<MockSettingsImpl> mockHandler = new MockHandler<>(mockSettings);
        Invocation invocation = mock(Invocation.class);

        // Act
        mockHandler.handle(invocation);

        // Assert
        verify(mockSettings, times(1)).getDefaultAnswer();
        verifyNoMoreInteractions(mockSettings);

        verifyZeroInteractions(invocation);
    }

    @Test
    public void handle_shouldVerifyDataWhenVerificationModeIsNotNull() throws Throwable {
        // Arrange
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<MockSettingsImpl> mockHandler = new MockHandler<>(mockSettings);
        Invocation invocation = mock(Invocation.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        mockHandler.mockingProgress.stubbingStarted();
        mockHandler.mockingProgress.reportOngoingStubbing(mock(mockingProgress.class));

        // Act
        mockHandler.handle(invocation);

        // Assert
        verifyZeroInteractions(invocation);
        verify(mockSettings, times(1)).getDefaultAnswer();
        verifyNoMoreInteractions(mockSettings);
        verifyZeroInteractions(verificationMode);
    }

    @Test
    public void handle_shouldSetInvocationForPotentialStubbingWhenVerificationModeIsNull() throws Throwable {
        // Arrange
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<MockSettingsImpl> mockHandler = new MockHandler<>(mockSettings);
        Invocation invocation = mock(Invocation.class);
        mockHandler.mockingProgress.stubbingStarted();
        mockHandler.mockingProgress.reportOngoingStubbing(mock(mockingProgress.class));
        VerificationMode verificationMode = null;

        // Act
        mockHandler.handle(invocation);

        // Assert
        verifyZeroInteractions(invocation);
        verify(mockSettings, times(1)).getDefaultAnswer();
        verifyNoMoreInteractions(mockSettings);

        verifyZeroInteractions(verificationMode);
    }

    @Test
    public void handle_shouldReturnStubbedInvocationAnswerWhenStubbedInvocationNotNull() throws Throwable {
        // Arrange
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<MockSettingsImpl> mockHandler = new MockHandler<>(mockSettings);
        Invocation invocation = mock(Invocation.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        verifyZeroInteractions(invocation);
        verify(mockSettings, times(1)).getDefaultAnswer();
        verifyNoMoreInteractions(mockSettings);
        verify(mockHandler.invocationContainerImpl, times(1)).findAnswerFor(invocation);
        verify(stubbedInvocation, times(1)).captureArgumentsFrom(invocation);
        verify(stubbedInvocation, times(1)).answer(invocation);

        Assert.assertNull(result);
    }

    @Test
    public void handle_shouldReturnDefaultAnswerWhenStubbedInvocationIsNull() throws Throwable {
        // Arrange
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<MockSettingsImpl> mockHandler = new MockHandler<>(mockSettings);
        Invocation invocation = mock(Invocation.class);
        when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        Answer<Object> defaultAnswer = mock(Answer.class);
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        verifyZeroInteractions(invocation);
        verify(mockSettings, times(1)).getDefaultAnswer();
        verifyNoMoreInteractions(mockSettings);
        verify(mockHandler.invocationContainerImpl, times(1)).findAnswerFor(invocation);
        verify(defaultAnswer, times(1)).answer(invocation);
        verify(mockHandler.invocationContainerImpl, times(1)).resetInvocationForPotentialStubbing(any());

        Assert.assertNull(result);
    }

    @Test
    public void voidMethodStubbable_shouldReturnVoidMethodStubbable() {
        // Arrange
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<MockSettingsImpl> mockHandler = new MockHandler<>(mockSettings);
        Object mock = new Object();

        // Act
        VoidMethodStubbable<Object> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);

        // Assert
        Assert.assertNotNull(voidMethodStubbable);
    }

    @Test
    public void getMockSettings_shouldReturnMockSettings() {
        // Arrange
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<MockSettingsImpl> mockHandler = new MockHandler<>(mockSettings);

        // Act
        MockSettingsImpl result = mockHandler.getMockSettings();

        // Assert
        Assert.assertEquals(mockSettings, result);
    }

    @Test
    public void setAnswersForStubbing_shouldSetAnswersForStubbing() {
        // Arrange
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<MockSettingsImpl> mockHandler = new MockHandler<>(mockSettings);
        List<Answer> answers = Arrays.asList(mock(Answer.class));

        // Act
        mockHandler.setAnswersForStubbing(answers);

        // Assert
        verify(mockHandler.invocationContainerImpl, times(1)).setAnswersForStubbing(answers);
    }

    @Test
    public void getInvocationContainer_shouldReturnInvocationContainer() {
        // Arrange
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<MockSettingsImpl> mockHandler = new MockHandler<>(mockSettings);

        // Act
        InvocationContainer result = mockHandler.getInvocationContainer();

        // Assert
        Assert.assertEquals(mockHandler.invocationContainerImpl, result);
    }
}