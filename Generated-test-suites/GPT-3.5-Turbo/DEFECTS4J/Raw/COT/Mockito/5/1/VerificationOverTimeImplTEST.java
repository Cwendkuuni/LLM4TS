import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerify_WhenDelegateSatisfied_Returns() {
        // Given
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, delegate, true);
        doNothing().when(delegate).verify(data);

        // When
        verification.verify(data);

        // Then
        verify(delegate).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_WhenDelegateNotSatisfied_ThrowsError() {
        // Given
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, delegate, true);
        doThrow(new MockitoAssertionError("Delegate not satisfied")).when(delegate).verify(data);

        // When
        verification.verify(data);

        // Then expect MockitoAssertionError
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_WhenDelegateThrowsAssertionError_ThrowsError() {
        // Given
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, delegate, true);
        doThrow(new MockitoAssertionError("Delegate assertion error")).when(delegate).verify(data);

        // When
        verification.verify(data);

        // Then expect MockitoAssertionError
    }

    @Test
    public void testVerify_WhenDelegateNotSatisfiedAndCanRecover_SleepAndRetry() {
        // Given
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, delegate, true);
        doThrow(new MockitoAssertionError("Delegate not satisfied")).doNothing().when(delegate).verify(data);

        // When
        verification.verify(data);

        // Then
        verify(delegate, times(2)).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_WhenDelegateNotSatisfiedAndCannotRecover_ThrowsError() {
        // Given
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, delegate, true);
        doThrow(new MockitoAssertionError("Delegate not satisfied")).when(delegate).verify(data);
        when(verification.canRecoverFromFailure(delegate)).thenReturn(false);

        // When
        verification.verify(data);

        // Then expect MockitoAssertionError
    }

    @Test
    public void testGetPollingPeriod_ReturnsPollingPeriod() {
        // Given
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, delegate, true);

        // When
        long pollingPeriod = verification.getPollingPeriod();

        // Then
        assertEquals(100, pollingPeriod);
    }

    @Test
    public void testGetDuration_ReturnsDuration() {
        // Given
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, delegate, true);

        // When
        long duration = verification.getDuration();

        // Then
        assertEquals(1000, duration);
    }

    @Test
    public void testGetDelegate_ReturnsDelegate() {
        // Given
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, delegate, true);

        // When
        VerificationMode actualDelegate = verification.getDelegate();

        // Then
        assertEquals(delegate, actualDelegate);
    }
}