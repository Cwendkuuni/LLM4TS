package org.mockito.internal.verification;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.VerificationWithTimeout;
import org.mockito.internal.verification.VerificationWithTimeout.TimeoutStrategy;

public class VerificationOverTimeImplTest {

    private VerificationMode delegate;
    private Timer timer;

    @Before
    public void setup() {
        this.delegate = mock(VerificationMode.class);
        this.timer = mock(Timer.class);
    }

    @Test
    public void testVerificationOverTimeConstructor() {
        long pollingPeriodMillis = 100;
        long durationMillis = 500;
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false, this.timer);

        assertEquals(pollingPeriodMillis, verificationOverTime.getPollingPeriod());
        assertEquals(durationMillis, verificationOverTime.getDuration());
        assertEquals(delegate, verificationOverTime.getDelegate());
    }

    @Test
    public void testVerificationOverTimeVerifySuccess() {
        long pollingPeriodMillis = 100;
        long durationMillis = 500;
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, true, this.timer);

        VerificationData verificationData = mock(VerificationData.class);
        verificationOverTime.verify(verificationData);

        verify(delegate, times(1)).verify(verificationData);
    }

    @Test
    public void testVerificationOverTimeVerifyFailure() {
        long pollingPeriodMillis = 100;
        long durationMillis = 500;
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false, this.timer);

        VerificationData verificationData = mock(VerificationData.class);
        verifyNoMoreInteractions(delegate);
        verifyZeroInteractions(delegate);

        assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(verificationData));
    }

    @Test
    public void testVerificationOverTimeVerifyTimeoutWithRecoverableFailureMode() {
        long pollingPeriodMillis = 100;
        long durationMillis = 500;
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, true, this.timer);

        VerificationData verificationData = mock(VerificationData.class);
        doThrow(new MockitoAssertionError("test error")).when(delegate).verify(verificationData);

        AssertionError error = null;
        try {
            verificationOverTime.verify(verificationData);
        } catch (AssertionError e) {
            error = e;
        }

        assertNotNull(error);
        verify(delegate, times(1)).verify(verificationData);
    }

    @Test
    public void testVerificationOverTimeVerifyTimeoutWithNonRecoverableFailureMode() {
        long pollingPeriodMillis = 100;
        long durationMillis = 500;
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false, this.timer);

        VerificationData verificationData = mock(VerificationData.class);
        doThrow(new AssertionError()).when(delegate).verify(verificationData);

        AssertionError error = null;
        try {
            verificationOverTime.verify(verificationData);
        } catch (AssertionError e) {
            error = e;
        }

        assertNotNull(error);
        verify(delegate, times(1)).verify(verificationData);
    }

    @Test
    public void testVerificationOverTimeVerifyTimeoutWithTimeoutStrategy() {
        long pollingPeriodMillis = 100;
        long durationMillis = 500;
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false, this.timer);

        VerificationData verificationData = mock(VerificationData.class);
        doThrow(new MockitoAssertionError("test error")).when(delegate).verify(verificationData);

        VerificationWithTimeout verificationWithTimeout = new VerificationWithTimeout(pollingPeriodMillis, new TimeoutStrategy() {
            @Override
            public void timeout() {
                throw new RuntimeException("timeout");
            }
        });

        VerificationMode verificationModeWithTimeout = verificationWithTimeout.wrap(delegate);

        VerificationOverTimeImpl verificationOverTimeWithTimeout = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, verificationModeWithTimeout, false, this.timer);

        AssertionError error = null;
        try {
            verificationOverTimeWithTimeout.verify(verificationData);
        } catch (AssertionError e) {
            error = e;
        }

        assertNotNull(error);
        verify(delegate, never()).verify(verificationData);
        verify(this.timer).stop();
    }

    @Test
    public void testVerificationOverTimeVerifyTimeoutWithNoMoreInteractions() {
        long pollingPeriodMillis = 100;
        long durationMillis = 500;
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false, this.timer);

        VerificationData verificationData = mock(VerificationData.class);
        doThrow(new MockitoAssertionError("test error")).when(delegate).verify(verificationData);

        VerificationMode noMoreInteractionsVerificationMode = mock(VerificationMode.class);
        doThrow(new MockitoAssertionError("test error")).when(noMoreInteractionsVerificationMode).verify(verificationData);
        doNothing().when(noMoreInteractionsVerificationMode).verifyNoMoreInteractions(any());

        VerificationWithTimeout verificationWithTimeout = new VerificationWithTimeout(pollingPeriodMillis, new TimeoutStrategy() {
            @Override
            public void timeout() {
                verifyNoMoreInteractions(delegate);
            }
        });

        VerificationMode verificationModeWithTimeout = verificationWithTimeout.wrap(noMoreInteractionsVerificationMode);

        VerificationOverTimeImpl verificationOverTimeWithTimeout = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, verificationModeWithTimeout, false, this.timer);

        AssertionError error = null;
        try {
            verificationOverTimeWithTimeout.verify(verificationData);
        } catch (AssertionError e) {
            error = e;
        }

        assertNotNull(error);
        verify(delegate, never()).verify(verificationData);
        verify(this.timer).stop();
    }

    @Test
    public void testVerificationOverTimeVerifySuccessWithSleep() {
        long pollingPeriodMillis = 100;
        long durationMillis = 500;
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, true, this.timer);

        VerificationData verificationData = mock(VerificationData.class);

        try {
            verificationOverTime.verify(verificationData);
            Thread.sleep(pollingPeriodMillis);
            verificationOverTime.verify(verificationData);
            Thread.sleep(pollingPeriodMillis);
            verificationOverTime.verify(verificationData);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        verify(delegate, times(3)).verify(verificationData);
    }

    @Test
    public void testVerificationOverTimeVerifyFailureWithSleep() {
        long pollingPeriodMillis = 100;
        long durationMillis = 500;
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, true, this.timer);

        VerificationData verificationData = mock(VerificationData.class);
        doThrow(new MockitoAssertionError("test error")).when(delegate).verify(verificationData);

        try {
            verificationOverTime.verify(verificationData);
            Thread.sleep(pollingPeriodMillis);
            verificationOverTime.verify(verificationData);
            Thread.sleep(pollingPeriodMillis);
            verificationOverTime.verify(verificationData);
        } catch (AssertionError e) {
            //expected
        }

        verify(delegate, never()).verify(verificationData);
        verify(this.timer, times(3)).isCounting();
        verify(this.timer, times(3)).stop();
    }
}