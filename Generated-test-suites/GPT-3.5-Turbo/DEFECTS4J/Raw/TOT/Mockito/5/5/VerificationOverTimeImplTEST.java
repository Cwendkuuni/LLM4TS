import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerify_WhenDelegateIsSatisfiedWithinDuration_ReturnSuccess() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, delegate, true);

        verification.verify(mock(VerificationData.class));

        verify(delegate).verify(any(VerificationData.class));
    }

    @Test
    public void testVerify_WhenDelegateIsNotSatisfiedWithinDuration_ThrowAssertionError() {
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate instanceof MockitoAssertionError).thenReturn(true);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, delegate, false);

        try {
            verification.verify(mock(VerificationData.class));
        } catch (MockitoAssertionError e) {
            // expected
        }

        verify(delegate, atLeastOnce()).verify(any(VerificationData.class));
    }

    @Test
    public void testVerify_WhenDelegateThrowsUnexpectedException_ThrowException() {
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate.verify(any(VerificationData.class))).thenThrow(new NullPointerException());
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, delegate, true);

        try {
            verification.verify(mock(VerificationData.class));
        } catch (RuntimeException e) {
            // expected
        }

        verify(delegate).verify(any(VerificationData.class));
    }

    @Test
    public void testVerify_WhenDelegateCannotRecoverFromFailure_ThrowAssertionError() {
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate instanceof MockitoAssertionError).thenReturn(true);
        when(delegate instanceof AtMost).thenReturn(true);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, delegate, true);

        try {
            verification.verify(mock(VerificationData.class));
        } catch (MockitoAssertionError e) {
            // expected
        }

        verify(delegate, never()).verify(any(VerificationData.class));
    }

    @Test(expected = InterruptedException.class)
    public void testSleep_WhenThreadSleepIsInterrupted_ThrowInterruptedException() throws InterruptedException {
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, mock(VerificationMode.class), true);

        Thread.currentThread().interrupt();
        verification.sleep(1000);
    }

    @Test
    public void testGetPollingPeriod_ReturnPollingPeriodMillis() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, delegate, true);

        long pollingPeriod = verification.getPollingPeriod();

        assertEquals(100, pollingPeriod);
    }

    @Test
    public void testGetDuration_ReturnDurationMillis() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, delegate, true);

        long duration = verification.getDuration();

        assertEquals(1000, duration);
    }

    @Test
    public void testGetDelegate_ReturnDelegateVerificationMode() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, delegate, true);

        VerificationMode actualDelegate = verification.getDelegate();

        assertEquals(delegate, actualDelegate);
    }
}