package org.mockito.internal.verification.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.VerificationData;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.VerificationWithTimeout;
import org.mockito.internal.util.Timer;

import java.util.concurrent.TimeUnit;

public class VerificationOverTimeImplTest {

    @Test
    public void testConstructor() {
        VerificationMode delegate = new VerificationWithTimeout(1, 100);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(10, 1000, delegate, true);
        Assert.assertEquals(delegate, verificationOverTime.getDelegate());
        Assert.assertTrue(verificationOverTime.returnOnSuccess);
        Assert.assertNotNull(verificationOverTime.timer);
    }

    @Test
    public void testVerifySuccess() {
        VerificationMode delegate = new VerificationWithTimeout(1, 100);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(10, 1000, delegate, true);
        VerificationData data = new VerificationData(null, null, null);

        verificationOverTime.verify(data);
        Assert.assertFalse(data.wasCalled());
        Assert.assertFalse(verificationOverTime.isSatisfied());
        Assert.assertTrue(verificationOverTime.isTimeoutReached());
        Assert.assertFalse(verificationOverTime.isSuccess());

        delegate.verify(data);
        Assert.assertTrue(data.wasCalled());
        Assert.assertTrue(verificationOverTime.isSuccess());
    }

    @Test
    public void testVerifyFailure() {
        VerificationMode delegate = new VerificationWithTimeout(1, 100);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(10, 1000, delegate, false);
        VerificationData data = new VerificationData(null, null, null);

        Assert.assertFalse(verificationOverTime.isSatisfied());
        Assert.assertFalse(verificationOverTime.isTimeoutReached());
        Assert.assertFalse(verificationOverTime.isSuccess());

        try {
            verificationOverTime.verify(data);
            Assert.fail("Expected exception");
        } catch (MockitoAssertionError e) {
            // expected exception
        }

        Assert.assertFalse(data.wasCalled());
        Assert.assertFalse(verificationOverTime.isSatisfied());
        Assert.assertFalse(verificationOverTime.isSuccess());
        Assert.assertFalse(verificationOverTime.isTimeoutReached());
    }

    @Test
    public void testVerifyWithTimeout() {
        VerificationMode delegate = new VerificationWithTimeout(1, 50);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(10, 1000, delegate, false);
        VerificationData data = new VerificationData(null, null, null);

        try {
            verificationOverTime.verify(data);
            Assert.fail("Expected exception");
        } catch (MockitoAssertionError e) {
            // expected exception
        }

        Assert.assertFalse(data.wasCalled());
        Assert.assertFalse(verificationOverTime.isSatisfied());
        Assert.assertFalse(verificationOverTime.isSuccess());
        Assert.assertTrue(verificationOverTime.isTimeoutReached());

        sleep(150);
        verificationOverTime.verify(data);
        Assert.assertTrue(data.wasCalled());
        Assert.assertTrue(verificationOverTime.isSatisfied());
        Assert.assertTrue(verificationOverTime.isSuccess());
        Assert.assertFalse(verificationOverTime.isTimeoutReached());
    }

    @Test
    public void testVerifyWithRecoverableFailure() {
        VerificationMode delegate = new VerificationWithTimeout(1, 50);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(10, 1000, delegate, true);
        VerificationData data = new VerificationData(null, null, null);

        try {
            verificationOverTime.verify(data);
            Assert.fail("Expected exception");
        } catch (MockitoAssertionError e) {
            // expected exception
        }

        Assert.assertFalse(data.wasCalled());
        Assert.assertFalse(verificationOverTime.isSatisfied());
        Assert.assertFalse(verificationOverTime.isSuccess());
        Assert.assertTrue(verificationOverTime.isTimeoutReached());

        sleep(51);
        try {
            verificationOverTime.verify(data);
            Assert.fail("Expected exception");
        } catch (MockitoAssertionError e) {
            // expected exception
        }

        Assert.assertFalse(data.wasCalled());
        Assert.assertFalse(verificationOverTime.isSatisfied());
        Assert.assertFalse(verificationOverTime.isSuccess());
        Assert.assertTrue(verificationOverTime.isTimeoutReached());

        sleep(100);
        verificationOverTime.verify(data);
        Assert.assertTrue(data.wasCalled());
        Assert.assertTrue(verificationOverTime.isSatisfied());
        Assert.assertTrue(verificationOverTime.isSuccess());
        Assert.assertFalse(verificationOverTime.isTimeoutReached());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testVerifyWithNonRecoverableFailure() {
        VerificationMode delegate = new AtLeast(1);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(10, 1000, delegate, false);
        VerificationData data = new VerificationData(null, null, null);

        verificationOverTime.verify(data);
        Assert.assertFalse(data.wasCalled());
        Assert.assertFalse(verificationOverTime.isSatisfied());
        Assert.assertFalse(verificationOverTime.isSuccess());
        Assert.assertFalse(verificationOverTime.isTimeoutReached());

        sleep(1001);
        verificationOverTime.verify(data);
        Assert.assertFalse(data.wasCalled());
        Assert.assertFalse(verificationOverTime.isSatisfied());
        Assert.assertFalse(verificationOverTime.isSuccess());
        Assert.assertTrue(verificationOverTime.isTimeoutReached());

        verificationOverTime.verify(data);
        // UnsupportedOperationException expected
    }

    @Test
    public void testGetters() {
        VerificationMode delegate = new VerificationWithTimeout(1, 100);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(10, 1000, delegate, true);

        Assert.assertEquals(10, verificationOverTime.getPollingPeriod());
        Assert.assertEquals(1000, verificationOverTime.getDuration());
        Assert.assertEquals(delegate, verificationOverTime.getDelegate());
        Assert.assertTrue(verificationOverTime.returnOnSuccess);
        Assert.assertNotNull(verificationOverTime.timer);
    }

    private void sleep(long sleep) {
        try {
            TimeUnit.MILLISECONDS.sleep(sleep);
        } catch (InterruptedException ie) {
            // ignore
        }
    }
}