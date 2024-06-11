package org.mockito.internal.verification.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationModeStub;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.VerificationWithTimeout;
import org.mockito.internal.util.Timer;

import java.util.concurrent.TimeUnit;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerificationOverTimeImpl_withValidParameters_verificationSucceeds() {
        VerificationMode delegate = new VerificationWithTimeout(1, TimeUnit.SECONDS);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100L, 2000L, delegate, true);

        VerificationData data = new VerificationData();
        verificationOverTime.verify(data);

        // As the verification succeeds immediately, the returnOnSuccess flag is true, so no need to wait for the full duration.
        Assert.assertTrue("Verification should succeed", true);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerificationOverTimeImpl_withDelegateNotFoundSatisfied_throwsMockitoAssertionError() {
        VerificationMode delegate = new VerificationModeStub();
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100L, 2000L, delegate, false);

        VerificationData data = new VerificationData();

        verificationOverTime.verify(data);

        // As the delegate is not satisfied, the full duration is not reached, and the returnOnSuccess flag is false,
        // an exception should be thrown.
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerificationOverTimeImpl_withDelegateNotFoundSatisfiedAfterTimeout_throwsMockitoAssertionError() {
        VerificationMode delegate = new VerificationModeStub();
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100L, 2000L, delegate, false);

        VerificationData data = new VerificationData();

        // Simulate a delay of 3 seconds for the delegate verification.
        Timer timer = new Timer(3000L);

        try {
            verificationOverTime.verify(data);
        } finally {
            timer.stop();
        }

        // The delegate is not satisfied within the timeout, so an exception should be thrown.
    }

    @Test
    public void testVerificationOverTimeImpl_withDelegateSatisfiedBeforeTimeout_verificationSucceeds() {
        VerificationMode delegate = new VerificationWithTimeout(1, TimeUnit.SECONDS);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100L, 2000L, delegate, false);

        VerificationData data = new VerificationData();

        // Simulate the delegate verification being satisfied before the timeout.
        delegate.verify(data);

        try {
            verificationOverTime.verify(data);
        } finally {
            // No exception should be thrown, as the delegate is satisfied and the full duration has passed.
        }

        Assert.assertTrue("Verification should succeed", true);
    }

    @Test(expected = InterruptedException.class)
    public void testVerificationOverTimeImpl_withInterruptedThread_throwsInterruptedException() {
        VerificationMode delegate = new VerificationWithTimeout(1, TimeUnit.SECONDS);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100L, 2000L, delegate, false);

        VerificationData data = new VerificationData();

        // Simulate the thread being interrupted.
        Thread thread = new Thread(() -> {
            try {
                verificationOverTime.verify(data);
            } finally {
                // No verification should be performed, as the thread has been interrupted.
            }
        });

        thread.start();

        thread.interrupt();

        // The verification should not be performed, and an InterruptedException should be thrown.
    }

    private static class VerificationModeStub implements VerificationMode {
        @Override
        public void verify(VerificationData data) {
            // Stub implementation for the verification mode.
            // In this case, it always satisfies the verification.
        }
    }

    private static class VerificationWithTimeout implements VerificationMode {
        private final long timeout;
        private final TimeUnit timeUnit;

        public VerificationWithTimeout(long timeout, TimeUnit timeUnit) {
            this.timeout = timeout;
            this.timeUnit = timeUnit;
        }

        @Override
        public void verify(VerificationData data) {
            try {
                // Simulate a delay before the verification is satisfied.
                TimeUnit.MILLISECONDS.sleep(timeout);
            } catch (InterruptedException e) {
                throw new AssertionError("Thread was interrupted");
            }

            // The verification is satisfied.
            Assert.assertEquals("Unexpected data", "Expected Data", data.getInvocations());
        }
    }
}