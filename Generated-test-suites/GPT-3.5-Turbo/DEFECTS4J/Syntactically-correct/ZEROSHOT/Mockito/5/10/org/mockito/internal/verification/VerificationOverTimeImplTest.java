import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerify_DelegateSatisfiedBeforeTimeout_ReturnOnSuccess() {
        // Arrange
        VerificationMode delegateMode = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        Timer timer = mock(Timer.class);
        VerificationOverTimeImpl verificationProtocol = new VerificationOverTimeImpl(1000, 5000, delegateMode, true, timer);

        // Act
        verificationProtocol.verify(data);

        // Assert
        verify(delegateMode).verify(data);
        verifyNoMoreInteractions(delegateMode);
        verify(timer).start();
        verify(timer, times(6)).isCounting();
    }

    @Test
    public void testVerify_DelegateSatisfiedAfterTimeout_ReturnOnSuccess() {
        // Arrange
        VerificationMode delegateMode = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        Timer timer = mock(Timer.class);
        VerificationOverTimeImpl verificationProtocol = new VerificationOverTimeImpl(1000, 5000, delegateMode, true, timer);

        // Act
        verificationProtocol.verify(data);

        // Assert
        verify(delegateMode, timeout(5000)).verify(data);
        verifyNoMoreInteractions(delegateMode);
        verify(timer).start();
        verify(timer, times(6)).isCounting();
    }

    @Test
    public void testVerify_DelegateSatisfiedBeforeTimeout_ReturnOnTimeout() {
        // Arrange
        VerificationMode delegateMode = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        Timer timer = mock(Timer.class);
        VerificationOverTimeImpl verificationProtocol = new VerificationOverTimeImpl(1000, 5000, delegateMode, false, timer);

        // Act
        verificationProtocol.verify(data);

        // Assert
        verify(delegateMode).verify(data);
        verifyNoMoreInteractions(delegateMode);
        verify(timer).start();
        verify(timer, times(6)).isCounting();
    }

    @Test
    public void testVerify_DelegateSatisfiedAfterTimeout_ReturnOnTimeout() {
        // Arrange
        VerificationMode delegateMode = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        Timer timer = mock(Timer.class);
        VerificationOverTimeImpl verificationProtocol = new VerificationOverTimeImpl(1000, 5000, delegateMode, false, timer);

        // Act
        verificationProtocol.verify(data);

        // Assert
        verify(delegateMode, timeout(5000)).verify(data);
        verifyNoMoreInteractions(delegateMode);
        verify(timer).start();
        verify(timer, times(6)).isCounting();
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_DelegateNotSatisfiedBeforeTimeout_ReturnOnSuccess() {
        // Arrange
        VerificationMode delegateMode = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        Timer timer = mock(Timer.class);
        VerificationOverTimeImpl verificationProtocol = new VerificationOverTimeImpl(1000, 5000, delegateMode, true, timer);
        doThrow(new MockitoAssertionError()).when(delegateMode).verify(data);

        // Act
        verificationProtocol.verify(data);

        // Assert
        verify(delegateMode).verify(data);
        verifyNoMoreInteractions(delegateMode);
        verify(timer).start();
        verify(timer, times(6)).isCounting();
    }

    @Test(expected = ArgumentsAreDifferent.class)
    public void testVerify_DelegateNotSatisfiedBeforeTimeout_ExceptionThrown() {
        // Arrange
        VerificationMode delegateMode = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        Timer timer = mock(Timer.class);
        VerificationOverTimeImpl verificationProtocol = new VerificationOverTimeImpl(1000, 5000, delegateMode, true, timer);
        doThrow(new ArgumentsAreDifferent("Arguments are different")).when(delegateMode).verify(data);

        // Act
        verificationProtocol.verify(data);

        // Assert
        verify(delegateMode).verify(data);
        verifyNoMoreInteractions(delegateMode);
        verify(timer).start();
        verify(timer, times(6)).isCounting();
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_DelegateNotSatisfiedAfterTimeout_ReturnOnSuccess() {
        // Arrange
        VerificationMode delegateMode = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        Timer timer = mock(Timer.class);
        VerificationOverTimeImpl verificationProtocol = new VerificationOverTimeImpl(1000, 5000, delegateMode, true, timer);
        doThrow(new MockitoAssertionError()).when(delegateMode).verify(data);

        // Act
        verificationProtocol.verify(data);

        // Assert
        verify(delegateMode, timeout(5000)).verify(data);
        verifyNoMoreInteractions(delegateMode);
        verify(timer).start();
        verify(timer, times(6)).isCounting();
    }

    @Test(expected = ArgumentsAreDifferent.class)
    public void testVerify_DelegateNotSatisfiedAfterTimeout_ExceptionThrown() {
        // Arrange
        VerificationMode delegateMode = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        Timer timer = mock(Timer.class);
        VerificationOverTimeImpl verificationProtocol = new VerificationOverTimeImpl(1000, 5000, delegateMode, true, timer);
        doThrow(new ArgumentsAreDifferent("Arguments are different")).when(delegateMode).verify(data);

        // Act
        verificationProtocol.verify(data);

        // Assert
        verify(delegateMode, timeout(5000)).verify(data);
        verifyNoMoreInteractions(delegateMode);
        verify(timer).start();
        verify(timer, times(6)).isCounting();
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_DelegateNotSatisfiedBeforeTimeout_ReturnOnTimeout() {
        // Arrange
        VerificationMode delegateMode = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        Timer timer = mock(Timer.class);
        VerificationOverTimeImpl verificationProtocol = new VerificationOverTimeImpl(1000, 5000, delegateMode, false, timer);
        doThrow(new MockitoAssertionError()).when(delegateMode).verify(data);

        // Act
        verificationProtocol.verify(data);

        // Assert
        verify(delegateMode).verify(data);
        verifyNoMoreInteractions(delegateMode);
        verify(timer).start();
        verify(timer, times(6)).isCounting();
    }

    @Test(expected = ArgumentsAreDifferent.class)
    public void testVerify_DelegateNotSatisfiedBeforeTimeout_ExceptionThrown_ReturnOnTimeout() {
        // Arrange
        VerificationMode delegateMode = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        Timer timer = mock(Timer.class);
        VerificationOverTimeImpl verificationProtocol = new VerificationOverTimeImpl(1000, 5000, delegateMode, false, timer);
        doThrow(new ArgumentsAreDifferent("Arguments are different")).when(delegateMode).verify(data);

        // Act
        verificationProtocol.verify(data);

        // Assert
        verify(delegateMode).verify(data);
        verifyNoMoreInteractions(delegateMode);
        verify(timer).start();
        verify(timer, times(6)).isCounting();
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_DelegateNotSatisfiedAfterTimeout_ReturnOnTimeout() {
        // Arrange
        VerificationMode delegateMode = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        Timer timer = mock(Timer.class);
        VerificationOverTimeImpl verificationProtocol = new VerificationOverTimeImpl(1000, 5000, delegateMode, false, timer);
        doThrow(new MockitoAssertionError()).when(delegateMode).verify(data);

        // Act
        verificationProtocol.verify(data);

        // Assert
        verify(delegateMode, timeout(5000)).verify(data);
        verifyNoMoreInteractions(delegateMode);
        verify(timer).start();
        verify(timer, times(6)).isCounting();
    }

    @Test(expected = ArgumentsAreDifferent.class)
    public void testVerify_DelegateNotSatisfiedAfterTimeout_ExceptionThrown_ReturnOnTimeout() {
        // Arrange
        VerificationMode delegateMode = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        Timer timer = mock(Timer.class);
        VerificationOverTimeImpl verificationProtocol = new VerificationOverTimeImpl(1000, 5000, delegateMode, false, timer);
        doThrow(new ArgumentsAreDifferent("Arguments are different")).when(delegateMode).verify(data);

        // Act
        verificationProtocol.verify(data);

        // Assert
        verify(delegateMode, timeout(5000)).verify(data);
        verifyNoMoreInteractions(delegateMode);
        verify(timer).start();
        verify(timer, times(6)).isCounting();
    }
}