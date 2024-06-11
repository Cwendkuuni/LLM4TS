package org.mockito.internal.verification;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.verification.VerificationModeFactory;

import java.util.concurrent.TimeUnit;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerifySuccessWithinDuration() throws Exception {
        long pollingPeriodMillis = 10;
        long durationMillis = 100;
        VerificationMode delegate = VerificationModeFactory.times(1);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, true);

        VerificationData data = new VerificationData(delegate, null, null, null);
        verificationOverTime.verify(data);
    }

    @Test
    public void testVerifySuccessWithPollingPeriod() throws Exception {
        long pollingPeriodMillis = 10;
        long durationMillis = 150;
        VerificationMode delegate = VerificationModeFactory.times(2);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, true);

        VerificationData data = new VerificationData(delegate, null, null, null);
        verificationOverTime.verify(data);
        verificationOverTime.verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyFailureBeforeDuration() throws Exception {
        long pollingPeriodMillis = 10;
        long durationMillis = 10;
        VerificationMode delegate = VerificationModeFactory.never();
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, true);

        VerificationData data = new VerificationData(delegate, null, null, null);
        verificationOverTime.verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyFailureAfterDuration() throws Exception {
        long pollingPeriodMillis = 10;
        long durationMillis = 150;
        VerificationMode delegate = VerificationModeFactory.times(1);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);

        VerificationData data = new VerificationData(delegate, null, null, null);
        verificationOverTime.verify(data);

        Thread.sleep(durationMillis + pollingPeriodMillis);

        verificationOverTime.verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyFailureAfterDurationWithCanRecoverFromFailureReturnFalse() throws Exception {
        long pollingPeriodMillis = 10;
        long durationMillis = 150;
        VerificationMode delegate = VerificationModeFactory.atLeastOnce();
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);

        VerificationData data = new VerificationData(delegate, null, null, null);
        verificationOverTime.verify(data);

        Thread.sleep(durationMillis + pollingPeriodMillis);

        Assert.assertFalse(verificationOverTime.canRecoverFromFailure(delegate));
        verificationOverTime.verify(data);
    }
}