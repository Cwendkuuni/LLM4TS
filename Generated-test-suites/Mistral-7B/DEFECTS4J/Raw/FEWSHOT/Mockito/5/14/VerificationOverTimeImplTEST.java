package org.mockito.internal.verification;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;

public class VerificationOverTimeImplTest {
    private VerificationData data;
    private Timer timer;
    private VerificationMode delegate;

    @Before
    public void setUp() {
        data = mock(VerificationData.class);
        timer = mock(Timer.class);
        delegate = mock(VerificationMode.class);
    }

    @Test
    public void testVerify_delegateSatisfiedBeforeTimeout() {
        when(delegate.verify(data)).thenReturn();

        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(100L, 500L, delegate, true, timer);
        verificationMode.verify(data);

        verify(delegate).verify(data);
        assertTrue(verificationMode.getDelegate().verifyCalled(data));
    }

    @Test
    public void testVerify_delegateNotSatisfiedBeforeTimeout() {
        when(delegate.verify(data)).thenThrow(new MockitoAssertionError("VerificationError"));

        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(100L, 500L, delegate, false, timer);

        assertThrows(MockitoAssertionError.class, () -> verificationMode.verify(data));
        verify(delegate).verify(data);
        verifyZeroInteractions(data);
    }

    @Test
    public void testVerify_delegateSatisfiedAfterTimeout() {
        when(delegate.verify(data)).thenReturn();

        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(100L, 500L, delegate, false, timer);

        timer.reset();
        verificationMode.verify(data);

        // verify is called repeatedly with a delay until it succeeds or until the timeout is reached
        for (int i = 0; i < 5; i++) {
            if (timer.isTimeoutReached()) {
                throw new AssertionError("Timeout reached before delegate was satisfied");
            }
            verify(delegate).verify(data);
            sleep(101); // sleep for a longer time than polling period to simulate the delay between calls
        }

        verify(delegate).verify(data);
        assertTrue(verificationMode.getDelegate().verifyCalled(data));
    }

    @Test
    public void testVerify_delegateNeverSatisfied() {
        when(delegate.verify(data)).thenThrow(new MockitoAssertionError("VerificationError"));

        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(100L, 500L, delegate, false, timer);

        assertThrows(MockitoAssertionError.class, () -> verificationMode.verify(data));
        verify(delegate, never()).verify(data);
        verifyZeroInteractions(data);
    }

    @Test
    public void testVerify_delegateRecoverableFailure() {
        when(delegate.verify(data)).thenThrow(new MockitoAssertionError("VerificationError"));

        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(100L, 500L, delegate, true, timer);

        assertThrows(MockitoAssertionError.class, () -> verificationMode.verify(data));
        verify(delegate).verify(data); // verify is called once before the exception is thrown
        sleep(100);
        verificationMode.verify(data); // verify is called again after the exception is thrown
        verify(delegate).verify(data);
        assertTrue(verificationMode.getDelegate().verifyCalled(data));
    }

    @Test
    public void testVerify_delegateNonRecoverableFailure() {
        when(delegate.verify(data)).thenThrow(new AssertionError("VerificationError"));

        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(100L, 500L, delegate, false, timer);

        assertThrows(AssertionError.class, () -> verificationMode.verify(data));
        verify(delegate, never()).verify(data);
        verifyZeroInteractions(data);
    }

    @Test
    public void testGetPollingPeriod() {
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(100L, 500L, delegate, false, timer);
        long pollingPeriod = verificationMode.getPollingPeriod();
        assertEquals(100, pollingPeriod);
    }

    @Test
    public void testGetDuration() {
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(100L, 500L, delegate, false, timer);
        long duration = verificationMode.getDuration();
        assertEquals(500, duration);
    }

    @Test
    public void testGetDelegate() {
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(100L, 500L, delegate, false, timer);
        VerificationMode actualDelegate = verificationMode.getDelegate();
        assertEquals(delegate, actualDelegate);
    }

    private void sleep(long sleep) {
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException ie) {
            // oups. not much luck.
        }
    }
}