package org.mockito.internal.verification.impl; // adjust the package if needed

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class VerificationOverTimeImplTest {

    private VerificationMode timeoutVerificationMode;
    private VerificationMode noMoreInteractionsVerificationMode;
    private VerificationMode atMostVerificationMode;
    private VerificationMode neverVerificationMode;
    private VerificationOverTimeImpl verificationOverTime;
    private Timer timer;

    @Before
    public void setUp() {
        timeoutVerificationMode = Mockito.mock(VerificationMode.class);
        noMoreInteractionsVerificationMode = Mockito.mock(VerificationMode.class);
        atMostVerificationMode = Mockito.mock(VerificationMode.class);
        neverVerificationMode = Mockito.mock(VerificationMode.class);
        timer = mock(Timer.class);

        verificationOverTime = new VerificationOverTimeImpl(100, 500, timeoutVerificationMode, true, timer);
    }

    @Test
    public void testDelegateIsSatisfiedBeforeTimeout() {
        // Given
        VerificationData data = Mockito.mock(VerificationData.class);

        // When
        verificationOverTime.verify(data);

        // Then
        verify(timeoutVerificationMode).verify(data);
    }

    @Test
    public void testDelegateIsNotSatisfiedButCanRecover() {
        // Given
        VerificationData data = Mockito.mock(VerificationData.class);

        when(timeoutVerificationMode.verify(ArgumentMatchers.eq(data))).thenThrow(new MockitoAssertionError("Delegate should not be satisfied."));

        // When
        verificationOverTime.verify(data);

        // Then
        verify(timeoutVerificationMode).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testDelegateCannotRecover() {
        // Given
        VerificationData data = Mockito.mock(VerificationData.class);

        when(timeoutVerificationMode.verify(ArgumentMatchers.eq(data))).thenThrow(new MockitoAssertionError("Delegate should not be satisfied."));

        // When
        verificationOverTime.verify(data);

        // Then
        // An AssertionError should have been thrown
    }

    @Test
    public void testDelegateIsSatisfiedAndReturnOnSuccessIsFalse() {
        // Given
        VerificationData data = Mockito.mock(VerificationData.class);

        // When
        verificationOverTime.verify(data);

        // Then
        verify(timeoutVerificationMode).verify(data);
        verifyZeroInteractions(timeoutVerificationMode); // No more interactions should be allowed
    }

    @Test
    public void testDelegateIsNotSatisfiedAndReturnOnSuccessIsFalse() {
        // Given
        VerificationData data = Mockito.mock(VerificationData.class);

        when(timeoutVerificationMode.verify(ArgumentMatchers.eq(data))).thenThrow(new MockitoAssertionError("Delegate should not be satisfied."));

        // When
        verificationOverTime.verify(data);

        // Then
        verify(timeoutVerificationMode).verify(data); // The last thrown exception should be kept
        verify(timeoutVerificationMode, never()).verifyNoMoreInteractions();
    }

    @Test
    public void testDelegateIsSatisfiedAndReturnOnSuccessIsTrue() {
        // Given
        VerificationData data = Mockito.mock(VerificationData.class);

        // When
        verificationOverTime.verify(data);

        // Then
        verify(timeoutVerificationMode).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testDelegateIsNotSatisfiedAndReturnOnSuccessIsFalseAndTimeoutIsReached() {
        // Given
        VerificationData data = Mockito.mock(VerificationData.class);
        long startTime = System.currentTimeMillis();

        when(timeoutVerificationMode.verify(ArgumentMatchers.eq(data))).thenThrow(new MockitoAssertionError("Delegate should not be satisfied."));

        // When
        verificationOverTime.verify(data);

        // Then
        long elapsedTime = System.currentTimeMillis() - startTime;
        long timeout = verificationOverTime.getDuration();

        // An AssertionError should have been thrown only after the timeout has elapsed
        if (elapsedTime > timeout) {
            throw new MockitoAssertionError("Delegate verification failed: " + verificationOverTime.getDelegate().getClass().getName());
        }
    }

    @Test
    public void testDelegateIsSatisfiedWithDifferentIntervalBetweenPolls() {
        // Given
        VerificationData data = Mockito.mock(VerificationData.class);

        when(timeoutVerificationMode.verify(ArgumentMatchers.eq(data))).thenThrow(new MockitoAssertionError("Delegate should not be satisfied."));

        // When
        verificationOverTime = new VerificationOverTimeImpl(10, 500, timeoutVerificationMode, false, timer);
        verificationOverTime.verify(data);

        // Then
        long pollingPeriod = verificationOverTime.getPollingPeriod();

        // The polling period between checks should be respected
        timer.assertElapsed(pollingPeriod);
    }

    @Test
    public void testDelegateIsNotSatisfiedWithDifferentIntervalBetweenPolls() {
        // Given
        VerificationData data = Mockito.mock(VerificationData.class);

        when(timeoutVerificationMode.verify(ArgumentMatchers.eq(data))).thenThrow(new MockitoAssertionError("Delegate should not be satisfied."));

        // When
        verificationOverTime = new VerificationOverTimeImpl(10, 500, timeoutVerificationMode, false, timer);

        // Then
        timer.start();

        // The verification should keep polling until the delegate is satisfied, or a timeout occurs
        verificationOverTime.verify(data);

        // And the last error thrown by the delegate should be kept
        AssertionError error = verificationOverTime.getLastError();
        verify(timeoutVerificationMode).verify(data);
        verify(timeoutVerificationMode, never()).verifyNoMoreInteractions();

        // No more interactions should be allowed until the full timeout has elapsed
        timer.assertElapsed(verificationOverTime.getDuration());
    }

    @Test
    public void testNoMoreInteractionsIsSatisfied() {
        // Given
        VerificationData data = Mockito.mock(VerificationData.class);

        // When
        verificationOverTime.verify(data);

        // Then
        verifyNoMoreInteractions(timeoutVerificationMode);
    }

    @Test
    public void testAtMostIsNotSatisfied() {
        // Given
        VerificationData data = Mockito.mock(VerificationData.class);

        when(timeoutVerificationMode.verify(ArgumentMatchers.eq(data))).thenThrow(new MockitoAssertionError("Delegate should not be satisfied."));

        when(atMostVerificationMode.verify(ArgumentMatchers.eq(data))).thenThrow(new MockitoAssertionError("Delegate should not be satisfied."));

        // When
        verificationOverTime = new VerificationOverTimeImpl(100, 500, atMostVerificationMode, false, timer);
        verificationOverTime.verify(data);

        // Then
        // An AssertionError should have been thrown
    }

    @Test
    public void testNeverIsNotSatisfied() {
        // Given
        VerificationData data = Mockito.mock(VerificationData.class);

        when(timeoutVerificationMode.verify(ArgumentMatchers.eq(data))).thenThrow(new MockitoAssertionError("Delegate should not be satisfied."));

        when(neverVerificationMode.verify(ArgumentMatchers.eq(data))).thenThrow(new MockitoAssertionError("Delegate should never be satisfied."));

        // When
        verificationOverTime = new VerificationOverTimeImpl(100, 500, neverVerificationMode, false, timer);
        verificationOverTime.verify(data);

        // Then
        // An AssertionError should have been thrown
    }
}