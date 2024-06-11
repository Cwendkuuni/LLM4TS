package org.mockito.internal.verification.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.VerificationData;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.VerificationWithTimeout;
import org.mockito.verification.VerificationModeFactory;

import java.util.concurrent.TimeUnit;

/**
 * Test class for VerificationOverTimeImpl
 */
public class VerificationOverTimeImplTest {

    @Test
    public void testVerifyWithVerificationWithTimeoutDelegate() {
        long pollingPeriodMillis = 10L;
        long durationMillis = 150L;
        VerificationMode verificationMode = new VerificationWithTimeout(pollingPeriodMillis, durationMillis, ArgumentMatchers.any());
        VerificationMode delegate = verificationMode;

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, true);

        VerificationData data = new VerificationData(ArgumentMatchers.any(), ArgumentMatchers.any());

        verificationOverTime.verify(data);

        // Verify that the delegate verification mode was called at least once
        verificationMode.verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyWithVerificationWithTimeoutDelegateWhenTimeoutReached() {
        long pollingPeriodMillis = 10L;
        long durationMillis = 50L;
        VerificationMode verificationMode = new VerificationWithTimeout(pollingPeriodMillis, durationMillis, ArgumentMatchers.any());
        VerificationMode delegate = verificationMode;

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);

        VerificationData data = new VerificationData(ArgumentMatchers.any(), ArgumentMatchers.any());

        // Simulate the delegate verification mode not being satisfied within the timeout
        Assert.assertFalse(verificationMode.matches(data));

        verificationOverTime.verify(data);

        // Verify that the MockitoAssertionError was thrown
    }

    @Test
    public void testVerifyWithNoMoreInteractionsDelegate() {
        long pollingPeriodMillis = 10L;
        long durationMillis = 150L;
        VerificationMode verificationMode = new NoMoreInteractions();
        VerificationMode delegate = verificationMode;

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, true);

        VerificationData data = new VerificationData(ArgumentMatchers.any(), ArgumentMatchers.any());

        // Verify that the delegate verification mode was not called after the first interaction
        verificationMode.verifyNoMoreInteractions();

        verificationOverTime.verify(data);

        // Verify that the test did not throw an exception
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyWithNoMoreInteractionsDelegateWhenInteractionOccurs() {
        long pollingPeriodMillis = 10L;
        long durationMillis = 50L;
        VerificationMode verificationMode = new NoMoreInteractions();
        VerificationMode delegate = verificationMode;

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);

        VerificationData data = new VerificationData(ArgumentMatchers.any(), ArgumentMatchers.any());

        // Verify that the delegate verification mode was not called before the interaction
        verificationMode.verifyNoMoreInteractions();

        // Simulate the first interaction
        delegate.verify(data);

        // Verify that the MockitoAssertionError was thrown
        verificationOverTime.verify(data);
    }

    @Test
    public void testGetPollingPeriod() {
        long pollingPeriodMillis = 10L;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, 50L, new VerificationWithTimeout(pollingPeriodMillis, 50L, ArgumentMatchers.any()), true);

        long actualPollingPeriod = verificationOverTime.getPollingPeriod();

        Assert.assertEquals(pollingPeriodMillis, actualPollingPeriod);
    }

    @Test
    public void testGetDuration() {
        long durationMillis = 150L;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(10L, durationMillis, new VerificationWithTimeout(10L, durationMillis, ArgumentMatchers.any()), true);

        long actualDuration = verificationOverTime.getDuration();

        Assert.assertEquals(durationMillis, actualDuration);
    }

    @Test
    public void testGetDelegate() {
        VerificationMode delegate = new VerificationWithTimeout(10L, 50L, ArgumentMatchers.any());
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(10L, 50L, delegate, true);

        VerificationMode actualDelegate = verificationOverTime.getDelegate();

        Assert.assertEquals(delegate, actualDelegate);
    }
}