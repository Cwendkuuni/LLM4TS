import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {

    @Test
    public void verify_ReturnsImmediatelyWhenDelegateIsSatisfiedAndReturnOnSuccessIsTrue() {
        // Arrange
        long pollingPeriodMillis = 100;
        long durationMillis = 500;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        Timer timer = mock(Timer.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
        VerificationData data = mock(VerificationData.class);

        // Act
        verificationOverTime.verify(data);

        // Assert
        verify(delegate).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void verify_ThrowsExceptionWhenDelegateDoesNotSatisfyWithinTimeout() {
        // Arrange
        long pollingPeriodMillis = 100;
        long durationMillis = 500;
        VerificationMode delegate = mock(VerificationMode.class);
        doThrow(MockitoAssertionError.class).when(delegate).verify(any(VerificationData.class));
        boolean returnOnSuccess = false;
        Timer timer = mock(Timer.class);
        when(timer.isCounting()).thenReturn(true, true, true, true, false);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
        VerificationData data = mock(VerificationData.class);

        // Act
        verificationOverTime.verify(data);
    }

    @Test
    public void verify_WaitsUntilDelegateIsSatisfiedAndDurationHasPassedWhenReturnOnSuccessIsFalse() {
        // Arrange
        long pollingPeriodMillis = 100;
        long durationMillis = 500;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = false;
        Timer timer = mock(Timer.class);
        when(timer.isCounting()).thenReturn(true, true, true, true, false);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
        VerificationData data = mock(VerificationData.class);

        // Act
        verificationOverTime.verify(data);

        // Assert
        verify(delegate, times(5)).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void verify_ThrowsExceptionWhenDelegateCannotRecoverFromFailure() {
        // Arrange
        long pollingPeriodMillis = 100;
        long durationMillis = 500;
        VerificationMode atMostVerificationMode = mock(VerificationMode.class);
        when(atMostVerificationMode instanceof AtMost).thenReturn(true);
        VerificationMode delegate = atMostVerificationMode;
        doThrow(MockitoAssertionError.class).when(delegate).verify(any(VerificationData.class));
        boolean returnOnSuccess = true;
        Timer timer = mock(Timer.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
        VerificationData data = mock(VerificationData.class);

        // Act
        verificationOverTime.verify(data);
    }

    @Test
    public void handleVerifyException_ReturnsErrorWhenDelegateCanRecoverFromFailure() {
        // Arrange
        long pollingPeriodMillis = 100;
        long durationMillis = 500;
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);
        MockitoAssertionError error = mock(MockitoAssertionError.class);

        // Act
        AssertionError result = verificationOverTime.handleVerifyException(error);

        // Assert
        verify(delegate).verify(any(VerificationData.class));
        verify(verificationOverTime).sleep(pollingPeriodMillis);
        assertEquals(error, result);
    }
    

    @Test(expected = MockitoAssertionError.class)
    public void handleVerifyException_ThrowsExceptionWhenDelegateCannotRecoverFromFailure() {
        // Arrange
        long pollingPeriodMillis = 100;
        long durationMillis = 500;
        VerificationMode atMostVerificationMode = mock(VerificationMode.class);
        when(atMostVerificationMode instanceof AtMost).thenReturn(true);
        VerificationMode delegate = atMostVerificationMode;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, true);
        MockitoAssertionError error = mock(MockitoAssertionError.class);

        // Act
        verificationOverTime.handleVerifyException(error);
    }

    @Test
    public void sleep_WaitsForSpecifiedTime() {
        // Arrange
        long sleepTime = 100;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(0, 0, null, false);

        // Act
        verificationOverTime.sleep(sleepTime);

        // Assert
        // How can we test the sleep method?
    }
}