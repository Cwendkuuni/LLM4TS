package org.mockito.internal.verification.test;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.verification.VerificationModeFactory;
import org.mockito.verification.VerificationWithTimeout;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerifyWithSuccess() {
        VerificationMode delegate = mock(VerificationWithTimeout.class);
        VerificationMode verificationOverTime = new VerificationOverTimeImpl(100L, 500L, delegate, true);
        VerificationData data = new VerificationData(mock(Object.class), "methodName", new Object[0]);

        verificationOverTime.verify(data);
        verify(delegate).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyWithTimeoutAndExceptionalDelegate() {
        VerificationMode delegate = mock(AtMost.class);
        VerificationMode verificationOverTime = new VerificationOverTimeImpl(100L, 500L, delegate, false);
        VerificationData data = new VerificationData(mock(Object.class), "methodName", new Object[0]);

        verificationOverTime.verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyWithTimeoutAndNonRecoverableDelegate() {
        VerificationMode delegate = mock(NoMoreInteractions.class);
        VerificationMode verificationOverTime = new VerificationOverTimeImpl(100L, 500L, delegate, false);
        VerificationData data = new VerificationData(mock(Object.class), "methodName", new Object[0]);

        verificationOverTime.verify(data);
    }

    @Test
    public void testVerifyWithSuccessAndSleep() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationMode verificationOverTime = new VerificationOverTimeImpl(100L, 500L, delegate, true);
        VerificationData data = new VerificationData(mock(Object.class), "methodName", new Object[0]);

        verificationOverTime.verify(data);
        verify(delegate, times(1)).verify(data);
    }

    @Test
    public void testVerifyWithTimeoutAndSleep() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationMode verificationOverTime = new VerificationOverTimeImpl(100L, 500L, delegate, false);
        VerificationData data = new VerificationData(mock(Object.class), "methodName", new Object[0]);

        verificationOverTime.verify(data);
        verifyNoInteractions(delegate);
    }

    @Test
    public void testGetPollingPeriod() {
        VerificationMode verificationOverTime = new VerificationOverTimeImpl(100L, 500L, mock(VerificationMode.class), false, mock(Timer.class));

        long pollingPeriod = verificationOverTime.getPollingPeriod();
        assertEquals(100L, pollingPeriod);
    }

    @Test
    public void testGetDuration() {
        VerificationMode verificationOverTime = new VerificationOverTimeImpl(100L, 500L, mock(VerificationMode.class), false, mock(Timer.class));

        long duration = verificationOverTime.getDuration();
        assertEquals(500L, duration);
    }

    @Test
    public void testGetDelegate() {
        VerificationMode verificationOverTime = new VerificationOverTimeImpl(100L, 500L, mock(VerificationMode.class), false, mock(Timer.class));

        VerificationMode delegate = verificationOverTime.getDelegate();
        assertNotNull(delegate);
    }
}