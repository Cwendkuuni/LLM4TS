package org.mockito.internal.verification.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.verification.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.api.VerificationModeFactory;
import org.mockito.internal.verification.VerificationWithTimeout;
import org.mockito.internal.verification.VerificationAfterDelay;
import org.mockito.exceptions.base.MockitoAssertionError;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerificationOverTimeWithTimeout() {
        long pollingPeriodMillis = 100;
        long durationMillis = 500;
        VerificationMode delegate = VerificationModeFactory.create(new VerificationWithTimeout(3, times(2)));
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, true);

        VerificationData data = new VerificationData(mock(Object.class), "methodName", "anyArg");

        verificationMode.verify(data);

        // Check if the delegate method is called the required number of times before the timeout
        verify(delegate.getObject(), times(2)).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerificationOverTimeWithTimeoutAndDelegateFailed() {
        long pollingPeriodMillis = 100;
        long durationMillis = 500;
        VerificationMode delegate = VerificationModeFactory.create(new AtLeastOnce());
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, true);

        VerificationData data = new VerificationData(mock(Object.class), "methodName", "anyArg");

        verificationMode.verify(data);

        // Check if the delegate method is called at least once before the timeout is reached
        verify(delegate.getObject(), atLeastOnce()).verify(data);

        // Check if the verification throws an AssertionError if the delegate method is not satisfied before the timeout
        Assert.assertThat(verifyException(verificationMode, data), is(instanceOf(MockitoAssertionError.class)));
    }

    @Test
    public void testVerificationOverTimeWithAfterDelay() {
        long pollingPeriodMillis = 100;
        long durationMillis = 500;
        VerificationMode delegate = VerificationModeFactory.create(new VerificationAfterDelay(3, 100));
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);

        VerificationData data = new VerificationData(mock(Object.class), "methodName", "anyArg");

        verificationMode.verify(data);

        // Wait for the delegate method to be called the required number of times after the delay
        try {
            TimeUnit.MILLISECONDS.sleep(durationMillis + 100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Check if the delegate method is called the required number of times after the delay
        verify(delegate.getObject(), times(3)).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerificationOverTimeWithAfterDelayAndDelegateFailed() {
        long pollingPeriodMillis = 100;
        long durationMillis = 500;
        VerificationMode delegate = VerificationModeFactory.create(new AtMost(3));
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);

        VerificationData data = new VerificationData(mock(Object.class), "methodName", "anyArg");

        verificationMode.verify(data);

        // Wait for the delegate method to fail before the timeout is reached
        try {
            TimeUnit.MILLISECONDS.sleep(durationMillis / 2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Check if the verification throws an AssertionError if the delegate method is not satisfied before the timeout
        Assert.assertThat(verifyException(verificationMode, data), is(instanceOf(MockitoAssertionError.class)));
    }

    private AssertionError verifyException(VerificationOverTimeImpl verificationMode, VerificationData data) {
        try {
            verificationMode.verify(data);
            return null;
        } catch (AssertionError e) {
            return e;
        }
    }
}