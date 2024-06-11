package org.mockito.internal.verification;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.VerificationWithTimeoutImpl;
import org.mockito.verification.VerificationModeFactory;

public class VerificationOverTimeImplTest {

    private VerificationMode delegate;
    private VerificationMode verificationOverTime;
    private VerificationData data;
    private Timer timer;

    @Before
    public void setUp() {
        delegate = mock(VerificationMode.class);
        data = mock(VerificationData.class);
        timer = mock(Timer.class);
        verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, true, timer);
    }

    @Test
    public void testVerifySucceedsImmediately() {
        verify(delegate).verify(data);
        verificationOverTime.verify(data);
        assertTrue(verificationOverTime.isSatisfied());
    }

    @Test
    public void testVerifySucceedsWithinTimeout() {
        verifyZeroInteractions(delegate);
        verificationOverTime.verify(data);
        verify(delegate, times(1)).verify(data);
        assertTrue(verificationOverTime.isSatisfied());
    }

    @Test
    public void testVerifyThrowsExceptionImmediatelyOnFailure() {
        verifyZeroInteractions(delegate);
        doThrow(new MockitoAssertionError("Expected exception message"))
                .when(delegate).verify(data);
        try {
            verificationOverTime.verify(data);
        } catch (AssertionError e) {
            assertEquals("Expected exception message", e.getMessage());
            return;
        }
        fail("Expected exception to be thrown");
    }

    @Test
    public void testVerifyThrowsExceptionAfterTimeout() {
        verifyZeroInteractions(delegate);
        doThrow(new MockitoAssertionError("Expected exception message"))
                .when(delegate).verify(data);
        long timeout = 10L;
        timer.setTimeout(timeout);
        verificationOverTime.verify(data);
        assertEquals("Expected exception message", verificationOverTime.takeValue());
    }

    @Test
    public void testVerifyDoesNotSatisfyImmediatelyIfReturnOnSuccessIsFalse() {
        verifyZeroInteractions(delegate);
        verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, false, timer);
        verificationOverTime.verify(data);
        assertFalse(verificationOverTime.isSatisfied());
    }

    @Test
    public void testVerifySatisfiesAfterTimeoutIfReturnOnSuccessIsFalse() {
        verifyZeroInteractions(delegate);
        verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, false, timer);
        timer.setTimeout(1001);
        verificationOverTime.verify(data);
        verify(delegate, times(1)).verify(data);
        assertTrue(verificationOverTime.isSatisfied());
    }

    @Test
    public void testVerifyThrowsExceptionWithTimeoutIfDelegateThrowsExceptionAndCanRecover() {
        doThrow(new MockitoAssertionError("Expected exception message"))
                .when(delegate).verify(data);
        verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, true, timer);
        try {
            verificationOverTime.verify(data);
        } catch (AssertionError e) {
            assertNotNull(e);
            assertNull(verificationOverTime.takeValue());
            return;
        }
        fail("Expected exception to be thrown");
    }

    @Test
    public void testVerifyThrowsExceptionWithoutTimeoutIfDelegateThrowsExceptionAndCantRecover() {
        doThrow(new VerificationModeFailedException("Expected failure message"))
                .when(delegate).verify(data);
        verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, true, timer);
        try {
            verificationOverTime.verify(data);
        } catch (MockitoAssertionError e) {
            assertEquals("Expected failure message", e.getMessage());
            return;
        }
        fail("Expected exception to be thrown");
    }

    @Test
    public void testVerifyWaitsForTimeoutIfDelegateDoesNotSatisfyImmediately() {
        verifyZeroInteractions(delegate);
        verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, true, timer);
        timer.setTimeout(1001);
        verificationOverTime.verify(data);
        verifyZeroInteractions(delegate);
        try {
            verificationOverTime.takeValue();
            fail("Expected exception to be thrown");
        } catch (MockitoAssertionError e) {
            // expected
            return;
        }
    }

    @Test
    public void testVerifyReturnsResultIfDelegateReturnsResult() {
        VerificationWithTimeoutImpl verificationWithTimeout = mock(VerificationWithTimeoutImpl.class);
        verificationWithTimeout.setExpectedNumberOfCalls(1);
        verificationOverTime = new VerificationOverTimeImpl(100, 1000, verificationWithTimeout, true, timer);
        verificationOverTime.verify(data);
        assertEquals(1, verificationWithTimeout.getInvocationCount());
    }

    @Test
    public void testGetPollingPeriod() {
        assertEquals(100, verificationOverTime.getPollingPeriod());
    }

    @Test
    public void testGetDuration() {
        assertEquals(1000, verificationOverTime.getDuration());
    }

    @Test
    public void testGetDelegate() {
        assertEquals(delegate, verificationOverTime.getDelegate());
    }
}

class VerificationModeFailedException extends MockitoAssertionError {

    public VerificationModeFailedException(String message) {
        super(message);
    }
}