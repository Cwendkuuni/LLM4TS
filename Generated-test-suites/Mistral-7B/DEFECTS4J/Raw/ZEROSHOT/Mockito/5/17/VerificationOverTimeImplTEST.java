package org.mockito.internal.verification.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.util.concurrent.TimeUnit;

/**
 * Test file for VerificationOverTimeImpl class
 */
public class VerificationOverTimeImplTest {

    @Test
    public void testVerificationWithTimeoutAndImmediateReturnOnSuccess() {
        long pollingPeriodMillis = 100L;
        long durationMillis = 500L;
        VerificationMode delegate = mock(VerificationMode.class);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, true);

        VerificationData data = createVerificationData();

        // Delegate verification mode should be satisfied before timeout
        delegate.verify(data);

        verificationOverTime.verify(data);

        Assert.assertTrue(delegate.getDelegateVerified());
        Assert.assertTrue(verificationOverTime.getDelegate().getDelegateVerified());
        Assert.assertFalse(verificationOverTime.getTimer().isCounting());
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerificationWithTimeoutAndNoImmediateReturnOnSuccess() {
        long pollingPeriodMillis = 100L;
        long durationMillis = 500L;
        VerificationMode delegate = mock(VerificationMode.class);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);

        VerificationData data = createVerificationData();

        // Delegate verification mode should be satisfied before timeout
        delegate.verify(data);

        verificationOverTime.verify(data);

        // Verify that the verificationOverTime throws an exception because the delegate was satisfied, but the full duration hasn't passed
        Assert.assertFalse(delegate.getDelegateVerified());
        Assert.assertFalse(verificationOverTime.getDelegate().getDelegateVerified());
        Assert.assertTrue(verificationOverTime.getTimer().isCounting());
    }

    @Test
    public void testVerificationWithTimeoutAndDelegateUnrecoverableFailure() {
        long pollingPeriodMillis = 100L;
        long durationMillis = 500L;
        VerificationMode delegate = mock(VerificationMode.class, new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                throw new MockitoAssertionError("Delegate failure");
            }
        });

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);

        VerificationData data = createVerificationData();

        verificationOverTime.verify(data);

        Assert.assertFalse(delegate.getDelegateVerified());
        Assert.assertFalse(verificationOverTime.getDelegate().getDelegateVerified());
        Assert.assertTrue(verificationOverTime.getTimer().isCounting());
    }

    @Test
    public void testVerificationWithTimeoutAndDelegateRecoverableFailure() {
        long pollingPeriodMillis = 100L;
        long durationMillis = 1000L;
        VerificationMode delegate = mock(VerificationMode.class, new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                if (!getInvocations().isEmpty()) {
                    throw new MockitoAssertionError("Delegate failure");
                }
                return null;
            }
        });

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);

        VerificationData data = createVerificationData();

        verificationOverTime.verify(data);

        // Delegate should recover from failure
        delegate.verify(data);

        Assert.assertFalse(delegate.getDelegateVerified());
        Assert.assertFalse(verificationOverTime.getDelegate().getDelegateVerified());
        Assert.assertTrue(verificationOverTime.getTimer().isCounting());

        // Verify that the next call to the delegate's verify method succeeds
        delegate.verify(data);

        Assert.assertTrue(delegate.getDelegateVerified());
        Assert.assertTrue(verificationOverTime.getDelegate().getDelegateVerified());
        Assert.assertFalse(verificationOverTime.getTimer().isCounting());
    }

    @Test
    public void testVerificationAfterDelay() {
        long pollingPeriodMillis = 100L;
        long durationMillis = 1000L;
        VerificationMode delegate = mock(VerificationMode.class);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);

        VerificationData data = createVerificationData();

        // Delegate verification mode should not be satisfied until full duration has passed
        verificationOverTime.verify(data);

        Assert.assertFalse(delegate.getDelegateVerified());
        Assert.assertFalse(verificationOverTime.getDelegate().getDelegateVerified());
        Assert.assertTrue(verificationOverTime.getTimer().isCounting());

        // Allow some time for the verification to be satisfied
        try {
            TimeUnit.MILLISECONDS.sleep(durationMillis + pollingPeriodMillis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Assert.assertTrue(delegate.getDelegateVerified());
        Assert.assertTrue(verificationOverTime.getDelegate().getDelegateVerified());
        Assert.assertFalse(verificationOverTime.getTimer().isCounting());
    }

    private VerificationData createVerificationData() {
        return new VerificationData(mock(Object.class), ArgumentMatchers.anyLong(), ArgumentMatchers.any(), ArgumentMatchers.any());
    }
}