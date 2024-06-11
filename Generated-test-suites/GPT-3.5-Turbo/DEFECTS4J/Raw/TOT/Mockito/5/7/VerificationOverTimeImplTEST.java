import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.verification.VerificationAfterDelay;
import org.mockito.verification.VerificationWithTimeout;
import org.mockito.exceptions.MockitoAssertionError;
import org.mockito.internal.util.Timer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {

    // Test case for constructor with default timer
    @Test
    public void testConstructor_DefaultTimer() {
        // Setup
        long pollingPeriodMillis = 1000L;
        long durationMillis = 5000L;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;

        // Execute
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);

        // Verify
        assertEquals(pollingPeriodMillis, verificationOverTime.getPollingPeriod());
        assertEquals(durationMillis, verificationOverTime.getDuration());
        assertSame(delegate, verificationOverTime.getDelegate());
    }

    // Test case for constructor with custom timer
    @Test
    public void testConstructor_CustomTimer() {
        // Setup
        long pollingPeriodMillis = 1000L;
        long durationMillis = 5000L;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        Timer timer = mock(Timer.class);

        // Execute
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);

        // Verify
        assertEquals(pollingPeriodMillis, verificationOverTime.getPollingPeriod());
        assertEquals(durationMillis, verificationOverTime.getDuration());
        assertSame(delegate, verificationOverTime.getDelegate());
    }

    // Test case for verify method when delegate verification succeeds within timeout
    @Test
    public void testVerify_DelegateSuccessWithinTimeout() {
        // Setup
        VerificationData data = mock(VerificationData.class);
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate instanceof AtMost).thenReturn(false);
        when(delegate instanceof NoMoreInteractions).thenReturn(false);
        when(delegate instanceof VerificationAfterDelay).thenReturn(false);
        when(delegate instanceof VerificationWithTimeout).thenReturn(false);
        when(delegate instanceof MockitoAssertionError).thenReturn(false);
        when(delegate.verify(data)).thenReturn(true);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1000L, 5000L, delegate, true);

        // Execute
        verificationOverTime.verify(data);

        // Verify
        verify(delegate).verify(data);
    }

    // Test case for verify method when delegate verification fails within timeout
    @Test(expected = MockitoAssertionError.class)
    public void testVerify_DelegateFailureWithinTimeout() {
        // Setup
        VerificationData data = mock(VerificationData.class);
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate instanceof AtMost).thenReturn(false);
        when(delegate instanceof NoMoreInteractions).thenReturn(false);
        when(delegate instanceof VerificationAfterDelay).thenReturn(false);
        when(delegate instanceof VerificationWithTimeout).thenReturn(false);
        when(delegate instanceof MockitoAssertionError).thenReturn(true);
        when(delegate.verify(data)).thenThrow(MockitoAssertionError.class);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1000L, 5000L, delegate, true);

        // Execute
        verificationOverTime.verify(data);
    }

    // Test case for verify method when delegate verification does not succeed within timeout
    @Test(expected = MockitoAssertionError.class)
    public void testVerify_DelegateNotSatisfiedBeforeTimeout() {
        // Setup
        VerificationData data = mock(VerificationData.class);
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate instanceof AtMost).thenReturn(false);
        when(delegate instanceof NoMoreInteractions).thenReturn(false);
        when(delegate instanceof VerificationAfterDelay).thenReturn(false);
        when(delegate instanceof VerificationWithTimeout).thenReturn(false);
        when(delegate instanceof MockitoAssertionError).thenReturn(false);
        when(delegate.verify(data)).thenAnswer(invocation -> {
            Thread.sleep(6000L); // Sleep longer than durationMillis
            return false;
        });

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1000L, 5000L, delegate, true);

        // Execute
        verificationOverTime.verify(data);
    }

    // Test case for verify method when delegate verification does not recover from failure
    @Test(expected = MockitoAssertionError.class)
    public void testVerify_DelegateDoesNotRecoverFromFailure() {
        // Setup
        VerificationData data = mock(VerificationData.class);
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate instanceof AtMost).thenReturn(true);
        when(delegate instanceof NoMoreInteractions).thenReturn(false);
        when(delegate instanceof VerificationAfterDelay).thenReturn(false);
        when(delegate instanceof VerificationWithTimeout).thenReturn(false);
        when(delegate instanceof MockitoAssertionError).thenReturn(false);
        when(delegate.verify(data)).thenThrow(MockitoAssertionError.class);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1000L, 5000L, delegate, true);

        // Execute
        verificationOverTime.verify(data);
    }

}