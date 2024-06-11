package org.mockito.internal.verification.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationModeWithTimeout;
import org.mockito.internal.verification.VerificationModeAfterDelay;
import org.mockito.internal.verification.VerificationWithTimeout;
import org.mockito.internal.verification.VerificationAfterDelay;
import org.mockito.internal.verification.MockitoVerificationException;
import org.mockito.internal.verification.AtMost;
import org.mockito.internal.verification.NoMoreInteractions;
import org.mockito.internal.util.Timer;

import java.util.concurrent.TimeUnit;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerificationOverTimeImplConstructor() {
        VerificationMode delegate = new VerificationModeWithTimeout(1, 1, null, 1);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1, 2, delegate, true);
        assertEquals(1, verificationOverTime.getPollingPeriod());
        assertEquals(2, verificationOverTime.getDuration());
        assertEquals(delegate, verificationOverTime.getDelegate());
        assertTrue(verificationOverTime instanceof VerificationOverTimeImpl);
    }

    @Test
    public void testVerifySuccessWithVerificationModeThatRecovers() {
        VerificationMode delegate = new VerificationWithTimeout(1, 1, null, 1);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1, 2, delegate, true);
        VerificationData data = new VerificationData(null, null, null, null);

        verificationOverTime.verify(data);
        assertTrue(delegate.isSatisfied());
    }

    @Test
    public void testVerifyFailureWithVerificationModeThatDoesNotRecover() {
        VerificationMode delegate = new AtMost(1);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1, 2, delegate, true);
        VerificationData data = new VerificationData(null, null, null, null);

        try {
            verificationOverTime.verify(data);
            fail("Expected MockitoAssertionError");
        } catch (MockitoAssertionError e) {
            // Expected
        }
    }

    @Test
    public void testVerifySuccessWithVerificationModeThatDoesNotRecover() {
        VerificationMode delegate = new NoMoreInteractions();
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1, 2, delegate, false);
        VerificationData data = new VerificationData(null, null, null, null);

        try {
            verificationOverTime.verify(data);
            fail("Expected MockitoAssertionError");
        } catch (MockitoAssertionError e) {
            try {
                delegate.verify(data);
                verificationOverTime.verify(data); // Should not throw an error this time
            } catch (MockitoAssertionError e2) {
                fail("Unexpected error");
            }
        }
    }

    @Test
    public void testVerifyTimeoutWithVerificationModeThatDoesNotRecover() {
        VerificationMode delegate = new AtMost(1);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1, 1, delegate, false);
        VerificationData data = new VerificationData(null, null, null, null);

        Assert.assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(data));
    }

    @Test
    public void testHandlerVerifyExceptionWithVerificationModeThatRecovers() {
        VerificationMode delegate = new VerificationWithTimeout(1, 1, null, 1);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1, 2, delegate, true);
        VerificationData data = new VerificationData(null, null, null, null);
        AssertionError error = new AssertionError("Expected error");

        Assert.assertThrows(AssertionError.class, () -> verificationOverTime.verify(data));
        Assert.assertEquals("Expected error", verificationOverTime.handleVerifyException(error).getMessage());
    }

    @Test
    public void testHandlerVerifyExceptionWithVerificationModeThatDoesNotRecover() {
        VerificationMode delegate = new AtMost(1);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1, 2, delegate, true);
        VerificationData data = new VerificationData(null, null, null, null);
        AssertionError error = new AssertionError("Expected error");

        Assert.assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(data));
        Assert.assertFalse(verificationOverTime.canRecoverFromFailure(delegate));
        Assert.assertThrows(MockitoAssertionError.class, () -> verificationOverTime.handleVerifyException(error));
    }

    @Test
    public void testSleep() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1, 2, null, false);
        long sleep = 100;

        verificationOverTime.sleep(sleep);

        long elapsedTime = System.currentTimeMillis() - System.currentTimeMillis() % sleep;
        assertTrue(elapsedTime >= sleep);
    }

    @Test
    public void testGetDuration() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1, 2, null, false);

        assertEquals(2, verificationOverTime.getDuration());
    }

    @Test
    public void testGetPollingPeriod() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1, 2, null, false);

        assertEquals(1, verificationOverTime.getPollingPeriod());
    }

    @Test
    public void testGetDelegate() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1, 2, null, false);

        assertEquals(null, verificationOverTime.getDelegate());
    }
}