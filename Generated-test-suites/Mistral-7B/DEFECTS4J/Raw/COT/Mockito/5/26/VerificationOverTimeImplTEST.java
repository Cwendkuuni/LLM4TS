package org.mockito.internal.verification.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationModeStub;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.VerificationWithTimeout;
import org.mockito.internal.verification.VerificationWithTimeoutStub;
import org.mockito.internal.util.Timer;

import java.util.concurrent.TimeUnit;

public class VerificationOverTimeImplTest {

    @Test
    public void testConstructor_ValidArguments() {
        long pollingPeriodMillis = 500;
        long durationMillis = 2000;
        VerificationMode delegate = new VerificationWithTimeoutStub();
        boolean returnOnSuccess = true;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);

        Assert.assertEquals(pollingPeriodMillis, verificationOverTime.getPollingPeriod());
        Assert.assertEquals(durationMillis, verificationOverTime.getDuration());
        Assert.assertEquals(delegate, verificationOverTime.getDelegate());
        Assert.assertTrue(returnOnSuccess);
    }

    @Test
    public void testVerify_DelegateSatisfiedImmediatelyWithReturnOnSuccess() {
        long pollingPeriodMillis = 500;
        long durationMillis = 1000;
        VerificationMode delegate = new VerificationWithTimeoutStub();
        boolean returnOnSuccess = true;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);
        VerificationData data = new VerificationData(MockitoTestUtils.MOCK_VERIFIABLE, MockitoTestUtils.MOCK_ARGUMENTS);

        verificationOverTime.verify(data);

        Assert.assertTrue(verificationOverTime.isSatisfied());
    }

    @Test
    public void testVerify_DelegateNotSatisfiedImmediatelyWithReturnOnSuccess() {
        long pollingPeriodMillis = 500;
        long durationMillis = 1000;
        VerificationMode delegate = new VerificationWithTimeoutStub();
        boolean returnOnSuccess = true;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);
        VerificationData data = new VerificationData(MockitoTestUtils.MOCK_VERIFIABLE, MockitoTestUtils.MOCK_ARGUMENTS);

        // Verify that the delegate is not initially satisfied
        Assert.assertFalse(delegate.matches(data));

        // Verify that the verificationOverTime is not initially satisfied
        Assert.assertFalse(verificationOverTime.isSatisfied());

        // Start the verification process
        verificationOverTime.verify(data);

        // The verification process should block
        Assert.assertTrue(verificationOverTime.isBlocked());

        // The delegate should not be satisfied yet
        Assert.assertFalse(delegate.matches(data));

        // Sleep for a while to give the verification process some time
        try {
            TimeUnit.MILLISECONDS.sleep(durationMillis + pollingPeriodMillis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // The verification process should have been completed by now
        Assert.assertFalse(verificationOverTime.isBlocked());

        // The delegate should be satisfied by now
        Assert.assertTrue(delegate.matches(data));

        // The verificationOverTime should also be satisfied by now (since returnOnSuccess is true)
        Assert.assertTrue(verificationOverTime.isSatisfied());
    }

    @Test
    public void testVerify_DelegateNotSatisfiedWithTimeout() {
        long pollingPeriodMillis = 500;
        long durationMillis = 1000;
        VerificationMode delegate = new VerificationWithTimeoutStub();
        boolean returnOnSuccess = false;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);
        VerificationData data = new VerificationData(MockitoTestUtils.MOCK_VERIFIABLE, MockitoTestUtils.MOCK_ARGUMENTS);

        // Verify that the delegate is not initially satisfied
        Assert.assertFalse(delegate.matches(data));

        // Verify that the verificationOverTime is not initially blocked
        Assert.assertFalse(verificationOverTime.isBlocked());

        // Start the verification process
        verificationOverTime.verify(data);

        // The verification process should block
        Assert.assertTrue(verificationOverTime.isBlocked());

        // Sleep for a while to give the verification process some time
        try {
            TimeUnit.MILLISECONDS.sleep(durationMillis + pollingPeriodMillis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // The verification process should have been completed by now
        Assert.assertFalse(verificationOverTime.isBlocked());

        // The delegate should still not be satisfied
        Assert.assertFalse(delegate.matches(data));

        // The verificationOverTime should not be satisfied yet (since returnOnSuccess is false)
        Assert.assertFalse(verificationOverTime.isSatisfied());

        // Throws a MockitoAssertionError since the delegate is never satisfied within the given timeout
        try {
            verificationOverTime.verify(data);
        } catch (MockitoAssertionError e) {
            // Expected
        }
    }

    @Test
    public void testVerify_DelegateSatisfiedWithTimeout() {
        long pollingPeriodMillis = 500;
        long durationMillis = 1000;
        VerificationMode delegate = new VerificationWithTimeoutStub();
        boolean returnOnSuccess = false;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);
        VerificationData data = new VerificationData(MockitoTestUtils.MOCK_VERIFIABLE, MockitoTestUtils.MOCK_ARGUMENTS);

        // Start the verification process
        Assert.assertTrue(verificationOverTime.isBlocked());

        // The delegate is not initially satisfied, but will be satisfied after a delay
        Timer timer = new Timer(durationMillis);
        try {
            timer.sleep();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // The delegate should now be satisfied
        Assert.assertTrue(delegate.matches(data));

        // The verificationOverTime should also be satisfied by now (since the delegate is satisfied and returnOnSuccess is false)
        Assert.assertTrue(verificationOverTime.isSatisfied());

        // Verify that the verificationOverTime is no longer blocked
        Assert.assertFalse(verificationOverTime.isBlocked());
    }

    @Test
    public void testVerify_DelegateSatisfiedAfterThrowingException() {
        long pollingPeriodMillis = 500;
        long durationMillis = 1000;
        VerificationMode delegate = new VerificationWithExceptionStub();
        boolean returnOnSuccess = false;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);
        VerificationData data = new VerificationData(MockitoTestUtils.MOCK_VERIFIABLE, MockitoTestUtils.MOCK_ARGUMENTS);

        // The delegate should initially throw an exception
        try {
            delegate.verify(data);
        } catch (MockitoAssertionError e) {
            // Expected
        }

        // Start the verification process
        Assert.assertTrue(verificationOverTime.isBlocked());

        // The delegate is not initially satisfied, but will be satisfied after a delay
        Timer timer = new Timer(durationMillis - pollingPeriodMillis);
        try {
            timer.sleep();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // The delegate should now be satisfied
        Assert.assertTrue(delegate.matches(data));

        // The verificationOverTime should also be satisfied by now
        Assert.assertTrue(verificationOverTime.isSatisfied());

        // Verify that the verificationOverTime is no longer blocked
        Assert.assertFalse(verificationOverTime.isBlocked());
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_DelegateNeverSatisfied() {
        long pollingPeriodMillis = 500;
        long durationMillis = 1000;
        VerificationMode delegate = new VerificationNeverSatisfiedStub();
        boolean returnOnSuccess = false;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);
        VerificationData data = new VerificationData(MockitoTestUtils.MOCK_VERIFIABLE, MockitoTestUtils.MOCK_ARGUMENTS);

        // Start the verification process
        Assert.assertTrue(verificationOverTime.isBlocked());

        // The delegate is never satisfied, so the verification should eventually throw an exception
        Timer timer = new Timer(durationMillis * 2);
        try {
            timer.sleep();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // The verificationOverTime should still be blocked
        Assert.assertTrue(verificationOverTime.isBlocked());

        // Throws a MockitoAssertionError since the delegate is never satisfied within the given timeout
        verificationOverTime.verify(data);
    }

    @Test(expected = InterruptedException.class)
    public void testSleep() {
        long sleep = 1000;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(0, 0, null, false);
        verificationOverTime.sleep(sleep);
    }
}

class MockitoTestUtils {
    public static final Object MOCK_VERIFIABLE = new Object();
    public static final Object[] MOCK_ARGUMENTS = new Object[]{MOCK_VERIFIABLE};
}

class VerificationWithTimeoutStub implements VerificationMode {
    @Override
    public void verify(VerificationData data) {
        // Deliberately slow down the verification process to simulate a timeout scenario
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Simulate a successful verification
        Assert.assertEquals(MOCK_ARGUMENTS[0], data.getArguments()[0]);
    }

    @Override
    public String description() {
        return "VerificationWithTimeoutStub";
    }
}

class VerificationWithExceptionStub implements VerificationMode {
    @Override
    public void verify(VerificationData data) {
        throw new MockitoAssertionError("Expected exception");
    }

    @Override
    public String description() {
        return "VerificationWithExceptionStub";
    }
}

class VerificationNeverSatisfiedStub implements VerificationMode {
    @Override
    public void verify(VerificationData data) {
        // Do nothing
    }

    @Override
    public String description() {
        return "VerificationNeverSatisfiedStub";
    }
}