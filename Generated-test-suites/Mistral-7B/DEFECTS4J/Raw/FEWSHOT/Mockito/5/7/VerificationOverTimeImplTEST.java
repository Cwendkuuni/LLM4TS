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
    public void testVerifySuccessWithinTime() {
        long pollingPeriodMillis = 10;
        long durationMillis = 50;
        VerificationMode delegate = VerificationModeFactory.times(1);
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, true);

        VerificationData data = new VerificationData(delegate, null);
        verificationMode.verify(data);

        Assert.assertTrue("Verification succeeded within the specified time.", true);
    }

    @Test
    public void testVerifySuccessAfterTime() {
        long pollingPeriodMillis = 10;
        long durationMillis = 50;
        VerificationMode delegate = VerificationModeFactory.times(2);
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);

        VerificationData data = new VerificationData(delegate, null);
        verificationMode.verify(data);

        verificationMode.verify(data);

        Assert.assertTrue("Verification succeeded after the specified time.", true);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyFailureBeforeTime() {
        long pollingPeriodMillis = 10;
        long durationMillis = 50;
        VerificationMode delegate = VerificationModeFactory.times(1);
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);

        VerificationData data = new VerificationData(delegate, null);

        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < durationMillis) {
            try {
                verificationMode.verify(data);
                break;
            } catch (MockitoAssertionError e) {
                // Expected
            }
        }

        verificationMode.verify(data);

        Assert.fail("Verification should have failed before the specified time.");
    }

    @Test
    public void testVerifyFailureAfterTime() {
        long pollingPeriodMillis = 10;
        long durationMillis = 50;
        VerificationMode delegate = VerificationModeFactory.times(1);
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);

        VerificationData data = new VerificationData(delegate, null);

        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < durationMillis && verificationMode.getDelegate().isSatisfied()) {
            verificationMode.verify(data);
            sleep(pollingPeriodMillis);
        }

        Assert.assertFalse("Verification failed after the specified time.", verificationMode.getDelegate().isSatisfied());
    }

    private void sleep(long sleep) {
        try {
            TimeUnit.MILLISECONDS.sleep(sleep);
        } catch (InterruptedException e) {
            // Ignore
        }
    }
}