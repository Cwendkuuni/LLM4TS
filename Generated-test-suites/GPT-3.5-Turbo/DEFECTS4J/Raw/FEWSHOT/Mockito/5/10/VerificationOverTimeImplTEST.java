
package org.mockito.internal.verification;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

public class VerificationOverTimeImplTest {

    @Test
    public void verify_shouldSucceedWithinDuration() {
        // Given
        long pollingPeriodMillis = 100;
        long durationMillis = 500;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);

        // When
        VerificationData data = mock(VerificationData.class);
        verificationOverTime.verify(data);

        // Then
        verify(delegate).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void verify_shouldThrowAssertionErrorIfDelegateDoesNotSatisfyWithinDuration() {
        // Given
        long pollingPeriodMillis = 100;
        long durationMillis = 500;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = false;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);

        // When
        VerificationData data = mock(VerificationData.class);
        when(delegate.verify(data)).thenThrow(new MockitoAssertionError("Delegate verification failed"));
        verificationOverTime.verify(data);

        // Then
        // Expected MockitoAssertionError to be thrown
    }

    @Test
    public void verify_shouldSleepBetweenPolling() throws InterruptedException {
        // Given
        long pollingPeriodMillis = 100;
        long durationMillis = 500;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = false;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);
        Timer timer = mock(Timer.class);
        when(timer.isCounting()).thenReturn(true, true, true, false);
        verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);

        // When
        VerificationData data = mock(VerificationData.class);
        when(delegate.verify(data)).thenThrow(new MockitoAssertionError("Delegate verification failed"));
        verificationOverTime.verify(data);

        // Then
        verify(delegate).verify(data);
        verify(timer).start();
        verify(timer, times(3)).isCounting();
        verify(verificationOverTime).sleep(pollingPeriodMillis);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void canRecoverFromFailure_shouldReturnFalseForAtMostMode() {
        // Given
        VerificationMode atMost = mock(AtMost.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 500, atMost, false);

        // When
        boolean recoverable = verificationOverTime.canRecoverFromFailure(atMost);

        // Then
        assertFalse(recoverable);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void canRecoverFromFailure_shouldReturnFalseForNoMoreInteractionsMode() {
        // Given
        VerificationMode noMoreInteractions = mock(NoMoreInteractions.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 500, noMoreInteractions, false);

        // When
        boolean recoverable = verificationOverTime.canRecoverFromFailure(noMoreInteractions);

        // Then
        assertFalse(recoverable);
    }

    @Test
    public void sleep_shouldSleepForSpecifiedTime() throws InterruptedException {
        // Given
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 500, mock(VerificationMode.class), false);

        // When
        long startTime = System.currentTimeMillis();
        verificationOverTime.sleep(200);
        long endTime = System.currentTimeMillis();

        // Then
        assertTrue(endTime - startTime >= 200);
    }
}
