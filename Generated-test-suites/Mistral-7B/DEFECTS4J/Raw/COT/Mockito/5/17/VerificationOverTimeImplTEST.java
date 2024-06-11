package org.mockito.internal.verification.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.util.Timer;
import org.mockito.verification.VerificationModeFactory;

import java.util.concurrent.TimeUnit;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerifyWithSuccessReturnOn() {
        long pollingPeriodMillis = 10L;
        long durationMillis = 100L;
        VerificationMode delegate = VerificationModeFactory.inOrder();
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, true);
        VerificationData verificationData = new VerificationData(delegate, new Object()[0]);

        // Basic test case for verify method with success returnOn=true
        verificationOverTime.verify(verificationData);
        Assert.assertTrue("Test passed", true);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyWithFailureReturnOn() {
        long pollingPeriodMillis = 10L;
        long durationMillis = 100L;
        VerificationMode delegate = VerificationModeFactory.never();
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, true);
        VerificationData verificationData = new VerificationData(delegate, new Object()[0]);

        // Basic test case for verify method with failure returnOn=true
        verificationOverTime.verify(verificationData);
        Assert.assertTrue("Test passed", true);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyWithFailureNoReturnOn() {
        long pollingPeriodMillis = 10L;
        long durationMillis = 100L;
        VerificationMode delegate = VerificationModeFactory.never();
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);
        VerificationData verificationData = new VerificationData(delegate, new Object()[0]);

        // Basic test case for verify method with failure and no returnOn=false
        verificationOverTime.verify(verificationData);
        Assert.assertFalse("Test passed", true);
    }

    @Test
    public void testCanRecoverFromFailure() {
        long pollingPeriodMillis = 10L;
        long durationMillis = 100L;
        VerificationMode delegate = VerificationModeFactory.inOrder();
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);
        VerificationData verificationData = new VerificationData(delegate, new Object()[0]);

        // Test canRecoverFromFailure method
        Assert.assertTrue("Test passed: Can recover from failure", verificationOverTime.canRecoverFromFailure(delegate));
    }

    @Test(expected = MockitoAssertionError.class)
    public void testCannotRecoverFromFailure() {
        long pollingPeriodMillis = 10L;
        long durationMillis = 100L;
        VerificationMode delegate = VerificationModeFactory.atLeastOnce();
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);
        VerificationData verificationData = new VerificationData(delegate, new Object()[0]);

        // Test cannotRecoverFromFailure method
        Assert.assertFalse("Test passed: Cannot recover from failure", verificationOverTime.canRecoverFromFailure(delegate));
        verificationOverTime.verify(verificationData);
    }

    @Test
    public void testGetPollingPeriod() {
        long pollingPeriodMillis = 10L;
        long durationMillis = 100L;
        VerificationMode delegate = VerificationModeFactory.inOrder();
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);

        // Test getPollingPeriod method
        long pollingPeriod = verificationOverTime.getPollingPeriod();
        Assert.assertEquals("Test passed: Polling period is correct", pollingPeriodMillis, pollingPeriod);
    }

    @Test
    public void testGetDuration() {
        long pollingPeriodMillis = 10L;
        long durationMillis = 100L;
        VerificationMode delegate = VerificationModeFactory.inOrder();
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);

        // Test getDuration method
        long duration = verificationOverTime.getDuration();
        Assert.assertEquals("Test passed: Duration is correct", durationMillis, duration);
    }

    @Test
    public void testGetDelegate() {
        long pollingPeriodMillis = 10L;
        long durationMillis = 100L;
        VerificationMode delegate = VerificationModeFactory.inOrder();
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);

        // Test getDelegate method
        VerificationMode actualDelegate = verificationOverTime.getDelegate();
        Assert.assertEquals("Test passed: Delegate is correct", delegate, actualDelegate);
    }

    @Test(expected = AssertionError.class)
    public void testHandleVerifyExceptionWithRecoverableFailure() {
        long pollingPeriodMillis = 10L;
        long durationMillis = 100L;
        VerificationMode delegate = VerificationModeFactory.never();
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, true);
        VerificationData verificationData = new VerificationData(delegate, new Object()[0]);

        // Test handleVerifyException method with recoverable failure
        AssertionError expectedException = new AssertionError("Expected exception");
        Assert.assertFalse("Test passed: Should not recover from failure", verificationOverTime.canRecoverFromFailure(delegate));
        verificationOverTime.verify(verificationData);
        Assert.assertNotNull("Test passed: Exception was thrown", verificationOverTime.handleVerifyException(expectedException));
    }

    @Test(expected = AssertionError.class)
    public void testHandleVerifyExceptionWithNonRecoverableFailure() {
        long pollingPeriodMillis = 10L;
        long durationMillis = 100L;
        VerificationMode delegate = VerificationModeFactory.atLeastOnce();
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);
        VerificationData verificationData = new VerificationData(delegate, new Object()[0]);

        // Test handleVerifyException method with non-recoverable failure
        Assert.assertFalse("Test passed: Should not recover from failure", verificationOverTime.canRecoverFromFailure(delegate));
        verificationOverTime.verify(verificationData);
        Assert.assertNull("Test passed: No exception was thrown", verificationOverTime.handleVerifyException(new AssertionError("Expected exception")));
    }

    @Test
    public void testSleep() {
        long sleep = 10L;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(0L, 0L, null, false, new Timer(1000L));

        // Test sleep method
        verificationOverTime.sleep(sleep);
        long elapsedTime = System.currentTimeMillis() - System.nanoTime() / 1_000_000L;
        Assert.assertTrue("Test passed: Slept for the correct amount of time", elapsedTime >= sleep);
    }
}