package org.mockito.internal.verification;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.api.VerificationModeFactory;
import org.mockito.internal.verification.api.VerificationModeWithTimeout;
import org.mockito.internal.verification.api.VerificationModeWithoutTimeout;
import org.mockito.internal.verification.api.VerificationWithDelay;

public class VerificationOverTimeImplTest {
    private VerificationMode delegate;
    private VerificationModeWithTimeout timeoutDelegate;
    private VerificationModeWithoutTimeout noTimeoutDelegate;
    private VerificationModeWithDelay delayDelegate;
    private VerificationOverTimeImpl verificationOverTime;

    @Before
    public void setup() {
        delegate = mock(VerificationMode.class);
        timeoutDelegate = mock(VerificationModeWithTimeout.class);
        noTimeoutDelegate = mock(VerificationModeWithoutTimeout.class);
        delayDelegate = mock(VerificationModeWithDelay.class);
    }

    @Test
    public void testVerifyWithTimeoutDelegateSatisfied() {
        when(timeoutDelegate.verify(VerificationData.any())).thenReturn();

        verificationOverTime = new VerificationOverTimeImpl(100, 500, timeoutDelegate, true);
        VerificationData verificationData = VerificationData.any();
        verificationOverTime.verify(verificationData);

        verify(timeoutDelegate).verify(verificationData);
    }

    @Test
    public void testVerifyWithTimeoutDelegateNotSatisfiedBeforeTimeout() {
        when(timeoutDelegate.verify(VerificationData.any())).thenThrow(new MockitoAssertionError("Verification failed."));

        verificationOverTime = new VerificationOverTimeImpl(100, 500, timeoutDelegate, false);
        VerificationData verificationData = VerificationData.any();
        assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(verificationData));
    }

    @Test
    public void testVerifyWithTimeoutDelegateNotSatisfiedAfterTimeout() {
        when(timeoutDelegate.verify(VerificationData.any())).thenThrow(new MockitoAssertionError("Verification failed."));

        verificationOverTime = new VerificationOverTimeImpl(100, 500, timeoutDelegate, false);
        VerificationData verificationData = VerificationData.any();
        Thread.sleep(600); // wait for the timeout
        assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(verificationData));
    }

    @Test
    public void testVerifyWithNoTimeoutDelegateSatisfied() {
        when(noTimeoutDelegate.verify(VerificationData.any())).thenReturn();

        verificationOverTime = new VerificationOverTimeImpl(100, -1, noTimeoutDelegate, true);
        VerificationData verificationData = VerificationData.any();
        verificationOverTime.verify(verificationData);

        verify(noTimeoutDelegate).verify(verificationData);
    }

    @Test
    public void testVerifyWithNoTimeoutDelegateNotSatisfied() {
        when(noTimeoutDelegate.verify(VerificationData.any())).thenThrow(new MockitoAssertionError("Verification failed."));

        verificationOverTime = new VerificationOverTimeImpl(100, -1, noTimeoutDelegate, false);
        VerificationData verificationData = VerificationData.any();
        assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(verificationData));
    }

    @Test
    public void testVerifyWithDelayDelegateSatisfied() {
        when(delayDelegate.verify(VerificationData.any()))
                .thenReturn()
                .thenReturn() // verify should be called twice to test if delegate is satisfied and timer is elapsed
                .thenThrow(new MockitoAssertionError("Verification failed.")); // should throw exception after 2nd call

        verificationOverTime = new VerificationOverTimeImpl(100, 1000, delayDelegate, true);
        VerificationData verificationData = VerificationData.any();
        verificationOverTime.verify(verificationData);

        verify(delayDelegate, twice()).verify(verificationData);
    }

    @Test
    public void testVerifyWithDelayDelegateNotSatisfiedBeforeTimeout() {
        when(delayDelegate.verify(VerificationData.any()))
                .thenReturn()
                .thenThrow(new MockitoAssertionError("Verification failed."));

        verificationOverTime = new VerificationOverTimeImpl(100, 1000, delayDelegate, false);
        VerificationData verificationData = VerificationData.any();
        assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(verificationData));
    }

    @Test
    public void testVerifyWithDelayDelegateNotSatisfiedAfterTimeout() {
        when(delayDelegate.verify(VerificationData.any()))
                .thenReturn()
                .thenReturn()
                .thenThrow(new MockitoAssertionError("Verification failed."));

        verificationOverTime = new VerificationOverTimeImpl(100, 1000, delayDelegate, false);
        VerificationData verificationData = VerificationData.any();
        Thread.sleep(2000); // wait for the timeout
        assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(verificationData));
    }

    @Test
    public void testCanRecoverFromFailure() {
        VerificationModeAtLeast once = mock(VerificationModeAtLeast.class);
        VerificationModeNever never = mock(VerificationModeNever.class);

        assertTrue(verificationOverTime.canRecoverFromFailure(once));
        assertFalse(verificationOverTime.canRecoverFromFailure(never));
    }
}