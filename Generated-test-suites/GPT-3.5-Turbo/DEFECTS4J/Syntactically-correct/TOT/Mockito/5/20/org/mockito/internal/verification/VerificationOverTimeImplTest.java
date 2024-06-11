import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.*;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

public class VerificationOverTimeImplTest {

    @Test
    public void verify_shouldReturnImmediatelyWhenDelegateSatisfiedAndReturnOnSuccessIsTrue() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, true);

        // Act
        verificationOverTime.verify(data);

        // Assert
        verify(delegate).verify(data);
    }

    @Test
    public void verify_shouldReturnAfterDurationPassedWhenDelegateSatisfiedAndReturnOnSuccessIsFalse() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, false);

        // Act
        verificationOverTime.verify(data);

        // Assert
        verify(delegate).verify(data);
    }

    @Test(expected=MockitoAssertionError.class)
    public void verify_shouldThrowExceptionWhenDelegateNotSatisfiedBeforeTimeout() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        doThrow(new MockitoAssertionError("Delegate not satisfied")).when(delegate).verify(data);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, true);

        // Act
        verificationOverTime.verify(data);

        // Assert
        // Exception is expected
    }

    @Test
    public void verify_shouldSleepAndRetryWhenCannotRecoverFromFailure() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        doThrow(new MockitoAssertionError("Delegate not satisfied")).when(delegate).verify(data);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, true);
        verificationOverTime = spy(verificationOverTime);
        doReturn(false).when(verificationOverTime).canRecoverFromFailure(delegate);

        // Act
        verificationOverTime.verify(data);

        // Assert
        verify(delegate).verify(data);
        verify(verificationOverTime, times(10)).sleep(100); // Retry 10 times with 100ms interval
    }

    @Test
    public void verify_shouldNotSleepWhenCanRecoverFromFailure() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        doThrow(new MockitoAssertionError("Delegate not satisfied")).when(delegate).verify(data);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, true);
        verificationOverTime = spy(verificationOverTime);
        doReturn(true).when(verificationOverTime).canRecoverFromFailure(delegate);

        // Act
        verificationOverTime.verify(data);

        // Assert
        verify(delegate).verify(data);
        verify(verificationOverTime, never()).sleep(anyLong());
    }
}