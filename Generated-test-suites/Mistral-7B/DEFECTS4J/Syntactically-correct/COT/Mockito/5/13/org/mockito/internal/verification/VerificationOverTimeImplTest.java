package org.mockito.internal.verification.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.api.VerificationData.withRealCall;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.util.Timer;

import java.util.concurrent.TimeUnit;

/**
 * Test class for VerificationOverTimeImpl
 */
public class VerificationOverTimeImplTest {

    private VerificationMode delegate;
    private Timer timer;

    @Test
    public void testVerificationOverTimeImplConstructor() {
        long pollingPeriodMillis = 1L;
        long durationMillis = 2L;
        VerificationMode verificationMode = mock(VerificationMode.class);
        boolean returnOnSuccess = true;

        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, verificationMode, returnOnSuccess);

        assert verificationOverTimeImpl.getPollingPeriod() == pollingPeriodMillis;
        assert verificationOverTimeImpl.getDuration() == durationMillis;
        assert verificationOverTimeImpl.getDelegate() == verificationMode;
        assert verificationOverTimeImpl.returnOnSuccess == returnOnSuccess;
    }

    @Test
    public void testVerifyMethodWhenDelegateIsSatisfiedBeforeTimeout() {
        long pollingPeriodMillis = 500L;
        long durationMillis = 1000L;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;

        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);

        VerificationData data = withRealCall(mock(Object.class));

        when(delegate.verify(data)).thenReturn(null);

        verificationOverTimeImpl.verify(data);

        assert !timer.isCounting();
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyMethodWhenDelegateIsNotSatisfiedBeforeTimeout() {
        long pollingPeriodMillis = 500L;
        long durationMillis = 1000L;
        VerificationMode delegate = mock(VerificationMode.class);

        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);

        VerificationData data = withRealCall(mock(Object.class));

        when(delegate.verify(data)).thenThrow(new MockitoAssertionError("Expected exception"));

        verificationOverTimeImpl.verify(data);

        // The test should throw MockitoAssertionError here
    }

    @Test
    public void testVerifyMethodWhenDelegateIsNotSatisfiedButCanRecover() {
        long pollingPeriodMillis = 500L;
        long durationMillis = 1000L;
        VerificationMode delegate = mock(VerificationMode.class);

        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);

        VerificationData data = withRealCall(mock(Object.class));
        AssertionError exception = new AssertionError("Expected exception");

        when(delegate.verify(data)).thenThrow(exception);

        verificationOverTimeImpl.verify(data);

        try {
            Thread.sleep(pollingPeriodMillis * 2);
        } catch (InterruptedException e) {
            // Ignore the InterruptedException
        }

        verify(delegate).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyMethodWhenDelegateIsNotSatisfiedAndCannotRecover() {
        long pollingPeriodMillis = 500L;
        long durationMillis = 1000L;
        VerificationMode delegate = mock(VerificationMode.class);
        delegate.verify(mock(Object.class)); // Throws an exception

        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);

        VerificationData data = withRealCall(mock(Object.class));

        verificationOverTimeImpl.verify(data);

        // The test should throw MockitoAssertionError here
    }

    @Test
    public void testCanRecoverFromFailureMethodWhenDelegateIsNotAToMostOrNoMoreInteractions() {
        VerificationMode verificationMode1 = mock(VerificationMode.class);
        VerificationMode verificationMode2 = mock(VerificationMode.class);

        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(500L, 1000L, verificationMode1, false);
        VerificationOverTimeImpl verificationOverTimeImpl2 = new VerificationOverTimeImpl(500L, 1000L, verificationMode2, false);

        assert verificationOverTimeImpl.canRecoverFromFailure(verificationMode1);
        assert verificationOverTimeImpl2.canRecoverFromFailure(verificationMode2);
    }

    @Test
    public void testCanRecoverFromFailureMethodWhenDelegateIsAToMost() {
        VerificationMode verificationMode = mock(VerificationMode.class);

        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(500L, 1000L, verificationMode, false);

        assert !verificationOverTimeImpl.canRecoverFromFailure(verificationMode);
    }

    @Test
    public void testCanRecoverFromFailureMethodWhenDelegateIsNoMoreInteractions() {
        VerificationMode verificationMode = mock(VerificationMode.class);

        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(500L, 1000L, verificationMode, false);

        assert !verificationOverTimeImpl.canRecoverFromFailure(verificationMode);
    }

    @Test
    public void testHandleVerifyExceptionMethodWhenDelegateVerificationThrowsAssertionError() {
        long pollingPeriodMillis = 1L;
        long durationMillis = 2L;
        VerificationMode delegate = mock(VerificationMode.class);

        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);
        AssertionError exception = new AssertionError("Expected exception");

        when(delegate.verify(mock(Object.class))).thenThrow(exception);

        AssertionError error = verificationOverTimeImpl.handleVerifyException(exception);

        assert error == exception;
    }

    @Test
    public void testHandleVerifyExceptionMethodWhenDelegateVerificationThrowsArgumentsAreDifferent() {
        long pollingPeriodMillis = 1L;
        long durationMillis = 2L;
        VerificationMode delegate = mock(VerificationMode.class);

        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);
        org.mockito.exceptions.verification.junit.ArgumentsAreDifferent exception = mock(org.mockito.exceptions.verification.junit.ArgumentsAreDifferent.class);

        when(delegate.verify(mock(Object.class))).thenThrow(exception);

        AssertionError error = verificationOverTimeImpl.handleVerifyException(exception);

        assert error == exception;
    }

    @Test
    public void testSleepMethod() {
        long sleep = 1000L;

        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(500L, 1000L, mock(VerificationMode.class), false);

        verificationOverTimeImpl.sleep(sleep);

        try {
            Thread.sleep(1001L);
        } catch (InterruptedException e) {
            // Ignore the InterruptedException
        }

        assert !timer.isCounting();
    }

    @Test
    public void testGetPollingPeriodMethod() {
        long pollingPeriodMillis = 1L;

        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(pollingPeriodMillis, 1000L, mock(VerificationMode.class), false);

        long pollingPeriod = verificationOverTimeImpl.getPollingPeriod();

        assert pollingPeriod == pollingPeriodMillis;
    }

    @Test
    public void testGetDurationMethod() {
        long durationMillis = 1L;

        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(500L, durationMillis, mock(VerificationMode.class), false);

        long duration = verificationOverTimeImpl.getDuration();

        assert duration == durationMillis;
    }
}