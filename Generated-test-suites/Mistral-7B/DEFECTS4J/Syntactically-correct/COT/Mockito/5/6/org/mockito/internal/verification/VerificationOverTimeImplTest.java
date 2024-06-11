package org.mockito.internal.verification.test;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.util.Timer;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for VerificationOverTimeImpl
 */
public class VerificationOverTimeImplTest {

    private VerificationMode delegate;
    private VerificationOverTimeImpl verificationMode;
    private Timer timer;

    @Before
    public void setUp() {
        delegate = mock(VerificationMode.class);
        timer = spy(new Timer(1000));
        verificationMode = new VerificationOverTimeImpl(500, 2000, delegate, false, timer);
    }

    /**
     * Test for constructor with pollingPeriodMillis, durationMillis, delegate, returnOnSuccess
     */
    @Test
    public void testConstructor() {
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(500, 2000, delegate, false, timer);
        assert(verificationMode != null);
    }

    /**
     * Test for getter methods
     */
    @Test
    public void testGetters() {
        assert(verificationMode.getPollingPeriod() == 500);
        assert(verificationMode.getDuration() == 2000);
        assert(verificationMode.getDelegate() == delegate);
    }

    /**
     * Test for basic functionality of verify method
     */
    @Test
    public void testVerifyBasicFunctionality() {
        VerificationData data = mock(VerificationData.class);
        verificationMode.verify(data);
        verify(delegate).verify(data);
    }

    /**
     * Test for handling exceptions thrown by delegate
     */
    @Test(expected = MockitoAssertionError.class)
    public void testHandleVerifyException() {
        doThrow(new MockitoAssertionError("Exception message"))
                .when(delegate)
                .verify(mock(VerificationData.class));

        verificationMode.verify(mock(VerificationData.class));
    }

    /**
     * Test for handling "recoverable" exceptions
     */
    @Test
    public void testCanRecoverFromFailure() {
        VerificationMode notRecoverable = mock(AtMost.class);
        VerificationMode recoverable = mock(Times.class);

        verificationMode = new VerificationOverTimeImpl(500, 2000, notRecoverable, false, timer);
        assertFalse(verificationMode.canRecoverFromFailure(notRecoverable));

        verificationMode = new VerificationOverTimeImpl(500, 2000, recoverable, false, timer);
        assertTrue(verificationMode.canRecoverFromFailure(recoverable));
    }

    /**
     * Test for sleep method
     */
    @Test
    public void testSleep() {
        verificationMode.sleep(1000);
        long elapsedTime = timer.getElapsedTime();
        assertTrue(elapsedTime >= 1000);
    }

    /**
     * Test for edge case: timeout is reached before the delegate is satisfied
     */
    @Test(expected = MockitoAssertionError.class)
    public void testTimeoutBeforeDelegateSatisfied() {
        doThrow(new MockitoAssertionError("Exception message"))
                .when(delegate)
                .verify(mock(VerificationData.class));

        verificationMode.verify(mock(VerificationData.class));
    }

    /**
     * Test for edge case: pollingPeriodMillis is less than 0
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativePollingPeriodMillis() {
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(-1, 2000, delegate, false, timer);
    }

    /**
     * Test for edge case: durationMillis is less than 0
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDurationMillis() {
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(500, -1, delegate, false, timer);
    }

    /**
     * Test for edge case: delegate is null
     */
    @Test(expected = NullPointerException.class)
    public void testNullDelegate() {
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(500, 2000, null, false, timer);
    }
}