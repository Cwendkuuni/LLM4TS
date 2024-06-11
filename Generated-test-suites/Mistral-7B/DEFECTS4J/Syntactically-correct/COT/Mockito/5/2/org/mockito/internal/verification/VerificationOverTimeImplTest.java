package org.mockito.internal.verification.test;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.VerificationWithTimeout;
import org.mockito.internal.verification.VerificationAfterDelay;
import org.mockito.internal.verification.VerificationModeImpl;
import org.mockito.internal.util.Timer;

import java.util.concurrent.TimeUnit;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerificationOverTimeImplConstructionWithVerificationWithTimeout() {
        long pollingPeriodMillis = 100L;
        long durationMillis = 500L;
        VerificationMode delegate = new VerificationWithTimeout(1);
        boolean returnOnSuccess = true;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);

        Assert.assertEquals(pollingPeriodMillis, verificationOverTime.getPollingPeriod());
        Assert.assertEquals(durationMillis, verificationOverTime.getDuration());
        Assert.assertSame(delegate, verificationOverTime.getDelegate());
        Assert.assertTrue(returnOnSuccess);
    }

    @Test
    public void testVerificationOverTimeImplConstructionWithVerificationAfterDelay() {
        long pollingPeriodMillis = 100L;
        long durationMillis = 500L;
        VerificationMode delegate = new VerificationAfterDelay(1, 100L);
        boolean returnOnSuccess = false;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);

        Assert.assertEquals(pollingPeriodMillis, verificationOverTime.getPollingPeriod());
        Assert.assertEquals(durationMillis, verificationOverTime.getDuration());
        Assert.assertSame(delegate, verificationOverTime.getDelegate());
        Assert.assertFalse(returnOnSuccess);
    }

    @Test
    public void testVerify_DelegateIsSatisfiedBeforeTimeout() {
        long pollingPeriodMillis = 50L;
        long durationMillis = 250L;
        VerificationMode delegate = new VerificationWithTimeout(1);
        boolean returnOnSuccess = true;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);
        VerificationData data = new VerificationData(new Object(), new Object()[0]);

        verificationOverTime.verify(data);

        Assert.assertTrue(verificationOverTime.isSatisfied());
    }

    @Test
    public void testVerify_DelegateIsSatisfiedAfterTimeout() {
        long pollingPeriodMillis = 50L;
        long durationMillis = 100L;
        VerificationMode delegate = new VerificationWithTimeout(1);
        boolean returnOnSuccess = false;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);
        VerificationData data = new VerificationData(new Object(), new Object()[0]);

        Assert.assertFalse(verificationOverTime.isSatisfied());

        verificationOverTime.verify(data);

        try {
            verificationOverTime.verify(data);
            Assert.fail();
        } catch (MockitoAssertionError e) {
            // expected
        }

        Assert.assertFalse(verificationOverTime.isSatisfied());
    }

    @Test
    public void testVerify_DelegateIsSatisfiedAfterTimeoutAndReturnOnSuccess() {
        long pollingPeriodMillis = 50L;
        long durationMillis = 100L;
        VerificationMode delegate = new VerificationWithTimeout(1);
        boolean returnOnSuccess = true;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);
        VerificationData data = new VerificationData(new Object(), new Object()[0]);

        Assert.assertFalse(verificationOverTime.isSatisfied());

        verificationOverTime.verify(data);

        Assert.assertTrue(verificationOverTime.isSatisfied());
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_DelegateIsNotSatisfiedBeforeOrAfterTimeout() {
        long pollingPeriodMillis = 50L;
        long durationMillis = 100L;
        VerificationMode delegate = new NoMoreInteractions();
        boolean returnOnSuccess = false;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);
        VerificationData data = new VerificationData(new Object(), new Object()[0]);

        Assert.assertFalse(verificationOverTime.isSatisfied());

        verificationOverTime.verify(data);

        Assert.assertFalse(verificationOverTime.isSatisfied());
    }

    @Test
    public void testCanRecoverFromFailure() {
        VerificationMode delegate = new VerificationWithTimeout(1);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100L, 200L, delegate, false);

        Assert.assertTrue(verificationOverTime.canRecoverFromFailure(delegate));
    }

    @Test
    public void testHandleVerifyException_DelegateCanRecoverFromFailure() {
        VerificationMode delegate = new VerificationWithTimeout(1);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100L, 200L, delegate, false);
        AssertionError error = new AssertionError("Test error");

        Assert.assertNull(verificationOverTime.handleVerifyException(error));
        Assert.assertEquals("Test error", verificationOverTime.getLastError());

        sleep(verificationOverTime.getPollingPeriod());

        Assert.assertNull(verificationOverTime.getLastError());
    }

    @Test(expected = AssertionError.class)
    public void testHandleVerifyException_DelegateCannotRecoverFromFailure() {
        VerificationMode delegate = new AtLeastOnce();
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100L, 200L, delegate, false);
        AssertionError error = new AssertionError("Test error");

        Assert.assertNull(verificationOverTime.handleVerifyException(error));
        Assert.assertEquals("Test error", verificationOverTime.getLastError());

        sleep(verificationOverTime.getPollingPeriod());

        Assert.assertNotNull(verificationOverTime.getLastError());
    }

    @Test
    public void testSleep() {
        long sleep = 100L;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100L, 200L, null, false);

        verificationOverTime.sleep(sleep);

        long elapsedTime = System.nanoTime() - verificationOverTime.getStartTimeNanos();
        Assert.assertTrue(elapsedTime >= TimeUnit.NANOSECONDS.convert(sleep, TimeUnit.MILLISECONDS));
    }

    private void sleep(long sleep) {
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            // ignore
        }
    }
}