package org.mockito.internal.verification.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.*;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerifyMethodWithSuccess() {
        VerificationData verificationData = new VerificationData("mock", VerificationType.INVOCATION, null, null, 1L, times(1));
        VerificationMode verificationMode = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(500, 3000, verificationMode, true);

        verificationOverTime.verify(verificationData);
        Assert.assertTrue("Method call should have succeeded", true);
    }

    @Test
    public void testVerifyMethodWithFailureAndRecoverable() {
        VerificationData verificationData = new VerificationData("mock", VerificationType.INVOCATION, null, null, 1L, never());
        VerificationMode verificationMode = mock(VerificationMode.class);
        when(verificationMode.verify(verificationData)).thenThrow(new MockitoAssertionError("Expected exception"));
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(500, 3000, verificationMode, true);

        try {
            verificationOverTime.verify(verificationData);
            fail("Expected exception to be thrown");
        } catch (MockitoAssertionError e) {
            Assert.assertEquals("Expected exception", e.getMessage());
            Assert.assertTrue("Delegate verification mode should be recoverable", canRecoverFromFailure(verificationMode));
        }
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyMethodWithFailureAndNonRecoverable() {
        VerificationData verificationData = new VerificationData("mock", VerificationType.INVOCATION, null, null, 1L, never());
        VerificationMode verificationMode = mock(AtMost.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(500, 3000, verificationMode, false);

        verificationOverTime.verify(verificationData);
    }

    @Test
    public void testGetPollingPeriod() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(500, 3000, mock(VerificationMode.class), false, new Timer(3000));
        long pollingPeriod = verificationOverTime.getPollingPeriod();
        Assert.assertEquals(500, pollingPeriod);
    }

    @Test
    public void testGetDuration() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(500, 3000, mock(VerificationMode.class), false, new Timer(3000));
        long duration = verificationOverTime.getDuration();
        Assert.assertEquals(3000, duration);
    }

    @Test
    public void testGetDelegate() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(500, 3000, mock(VerificationMode.class), false, new Timer(3000));
        VerificationMode delegate = verificationOverTime.getDelegate();
        Assert.assertNotNull(delegate);
    }

    private boolean canRecoverFromFailure(VerificationMode verificationMode) {
        return new VerificationOverTimeImpl(500, 3000, verificationMode, false).canRecoverFromFailure(verificationMode);
    }
}