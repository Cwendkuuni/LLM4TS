import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerify_SuccessfulDelegateVerification_ReturnsImmediately() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, delegate, true);
        VerificationData data = mock(VerificationData.class);

        // Act
        verification.verify(data);

        // Assert
        verify(delegate).verify(data);
    }

    @Test
    public void testVerify_SuccessfulDelegateVerification_ReturnsAfterTimeout() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, delegate, false);
        verification.setTimer(new Timer(3000)); // Set a custom timer for testing
        VerificationData data = mock(VerificationData.class);

        // Act
        verification.verify(data);

        // Assert
        verify(delegate).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_DelegateVerificationFails_ThrowsAssertionError() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        doThrow(MockitoAssertionError.class).when(delegate).verify(any(VerificationData.class));

        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, delegate, false);
        verification.setTimer(new Timer(3000)); // Set a custom timer for testing
        VerificationData data = mock(VerificationData.class);

        // Act
        verification.verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_DelegateVerificationThrowsException_ThrowsAssertionError() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        doThrow(RuntimeException.class).when(delegate).verify(any(VerificationData.class));

        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, delegate, false);
        verification.setTimer(new Timer(3000)); // Set a custom timer for testing
        VerificationData data = mock(VerificationData.class);

        // Act
        verification.verify(data);
    }

    @Test
    public void testVerify_DelegateVerificationFailsButCanRecover_SleepsAndContinuesVerification() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        doThrow(MockitoAssertionError.class).when(delegate).verify(any(VerificationData.class));

        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, delegate, false);
        verification.setTimer(new Timer(3000)); // Set a custom timer for testing
        verification.setCanRecoverFromFailure(true);
        VerificationData data = mock(VerificationData.class);

        // Act
        verification.verify(data);

        // Assert
        verify(delegate, times(2)).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_DelegateVerificationFailsAndCannotRecover_ThrowsAssertionError() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        doThrow(MockitoAssertionError.class).when(delegate).verify(any(VerificationData.class));

        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, delegate, false);
        verification.setTimer(new Timer(3000)); // Set a custom timer for testing
        verification.setCanRecoverFromFailure(false);
        VerificationData data = mock(VerificationData.class);

        // Act
        verification.verify(data);
    }

    @Test
    public void testGetPollingPeriod_ReturnsPollingPeriodMillis() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, delegate, false);

        // Act
        long pollingPeriod = verification.getPollingPeriod();

        // Assert
        assertEquals(1000, pollingPeriod);
    }

    @Test
    public void testGetDuration_ReturnsDurationMillis() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, delegate, false);

        // Act
        long duration = verification.getDuration();

        // Assert
        assertEquals(5000, duration);
    }

    @Test
    public void testGetDelegate_ReturnsDelegateVerificationMode() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, delegate, false);

        // Act
        VerificationMode result = verification.getDelegate();

        // Assert
        assertEquals(delegate, result);
    }

}