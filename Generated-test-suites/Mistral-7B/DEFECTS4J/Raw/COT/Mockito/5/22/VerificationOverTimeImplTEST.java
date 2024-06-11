package org.mockito.internal.verification.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationModeWithTimeout;
import org.mockito.internal.verification.VerificationWithTimeout;
import org.mockito.internal.verification.test.testdoubles.DummyVerificationMode;
import org.mockito.internal.verification.test.testdoubles.DummyVerificationModeWithTimeout;
import org.mockito.internal.verification.test.testdoubles.DummyTimer;

import java.util.concurrent.TimeUnit;

/**
 * Test class for VerificationOverTimeImpl
 */
public class VerificationOverTimeImplTest {

    @Test
    public void testConstructor() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 500, new VerificationWithTimeout(50), true);
        Assert.assertNotNull(verificationOverTime);
    }

    @Test
    public void testVerify_DelegateSatisfiedBeforeTimeout() {
        VerificationMode delegate = new DummyVerificationMode();
        VerificationMode verificationMode = new VerificationOverTimeImpl(100, 500, delegate, true);
        VerificationData data = new VerificationData(delegate, new Object()[0]);

        verificationMode.verify(data);
        Assert.assertTrue(delegate.isSatisfied());
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_DelegateNotSatisfiedBeforeTimeout() {
        VerificationMode delegate = new VerificationWithTimeout(1);
        VerificationMode verificationMode = new VerificationOverTimeImpl(100, 500, delegate, false);
        VerificationData data = new VerificationData(delegate, new Object()[0]);

        verificationMode.verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_DelegateNotSatisfiedAndCannotRecover() {
        VerificationMode delegate = new AtMost(1);
        VerificationMode verificationMode = new VerificationOverTimeImpl(100, 500, delegate, false);
        VerificationData data = new VerificationData(delegate, new Object()[0]);

        verificationMode.verify(data);
    }

    @Test
    public void testVerify_DelegateSatisfiedAfterTimeout() {
        VerificationMode delegate = new DummyVerificationModeWithTimeout(500);
        VerificationMode verificationMode = new VerificationOverTimeImpl(100, 501, delegate, false);
        VerificationData data = new VerificationData(delegate, new Object()[0]);

        verificationMode.verify(data);
        Assert.assertFalse(delegate.isSatisfied());
    }

    @Test
    public void testGetPollingPeriod() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 500, new VerificationWithTimeout(50), true);
        long pollingPeriod = verificationOverTime.getPollingPeriod();
        Assert.assertEquals(100, pollingPeriod);
    }

    @Test
    public void testGetDuration() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 500, new VerificationWithTimeout(50), true);
        long duration = verificationOverTime.getDuration();
        Assert.assertEquals(500, duration);
    }

    @Test
    public void testGetDelegate() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 500, new VerificationWithTimeout(50), true);
        VerificationMode delegate = verificationOverTime.getDelegate();
        Assert.assertSame(new VerificationWithTimeout(50), delegate);
    }

    /**
     * Test edge case for InterruptedException when sleeping
     */
    @Test(expected = InterruptedException.class)
    public void testSleep_InterruptedException() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 500, new DummyVerificationMode(), false);
        verificationOverTime.sleep(1000);
        Thread thread = new Thread();
        thread.interrupt();
    }

}