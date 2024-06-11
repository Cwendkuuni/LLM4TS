package org.mockito.internal.verification.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationModeDelegate;
import org.mockito.internal.verification.VerificationModeDelegateSpy;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.VerificationWithTimeout;
import org.mockito.internal.util.Timer;

import java.util.concurrent.TimeUnit;

public class VerificationOverTimeImplTest {

    private VerificationMode verificationMode;
    private VerificationModeDelegateSpy verificationModeDelegateSpy;
    private VerificationOverTimeImpl verificationOverTimeImpl;

    @Before
    public void setup() {
        this.verificationModeDelegateSpy = new VerificationModeDelegateSpy();
        this.verificationMode = new VerificationWithTimeout(50, this.verificationModeDelegateSpy);
        this.verificationOverTimeImpl = new VerificationOverTimeImpl(10, 100, this.verificationMode, true);
    }

    @Test
    public void testVerify_DelegateSatisfiedWithinTimeout() {
        // Given: The delegate verification mode is satisfied within the timeout
        VerificationData data = new VerificationData("testMethod", new Object[0], null);
        this.verificationMode.verify(data);

        // Then: The VerificationOverTimeImpl should be satisfied
        this.verificationOverTimeImpl.verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_DelegateNotSatisfiedWithinTimeout() {
        // Given: The delegate verification mode is not satisfied within the timeout
        VerificationData data = new VerificationData("testMethod", new Object[0], null);
        this.verificationModeDelegateSpy.setThrowException(true);

        // Then: The VerificationOverTimeImpl should throw an exception
        this.verificationOverTimeImpl.verify(data);
    }

    @Test
    public void testVerify_DelegateSatisfiedAfterTimeout() {
        // Given: The delegate verification mode is satisfied after the timeout
        this.verificationModeDelegateSpy.setVerifySpyCallCounter(0);

        long startTime = System.currentTimeMillis();
        this.verificationOverTimeImpl.verify(new VerificationData("testMethod", new Object[0], null));
        long endTime = System.currentTimeMillis();

        // Then: The delegate verification mode should be called at least once (pollingPeriodMillis)
        this.verificationModeDelegateSpy.verify(new VerificationData("testMethod", new Object[0], null), MockitoVerificationMode.times(1));
        Assert.assertTrue("Delegate should be called at least once", this.verificationModeDelegateSpy.getVerifySpyCallCount() >= 1);

        // And: The method should wait for the full timeout to pass before throwing an exception
        Assert.assertTrue("Full timeout should pass before throwing an exception", (endTime - startTime) > this.verificationOverTimeImpl.getDuration());
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_DelegateNotSatisfiedAfterTimeout() {
        // Given: The delegate verification mode is not satisfied after the timeout
        this.verificationModeDelegateSpy.setVerifySpyCallCounter(0);

        // Then: The VerificationOverTimeImpl should throw an exception
        this.verificationOverTimeImpl.verify(new VerificationData("testMethod", new Object[0], null));
    }

    @Test
    public void testGetPollingPeriod() {
        Assert.assertEquals(10, this.verificationOverTimeImpl.getPollingPeriod());
    }

    @Test
    public void testGetDuration() {
        Assert.assertEquals(100, this.verificationOverTimeImpl.getDuration());
    }

    @Test
    public void testGetDelegate() {
        Assert.assertEquals(this.verificationMode, this.verificationOverTimeImpl.getDelegate());
    }
}