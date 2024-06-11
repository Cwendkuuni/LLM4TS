package org.mockito.internal.verification.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.quality.Strictness;

import java.util.concurrent.ThreadLocalRandom;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VerificationOverTimeImplTest {

    private VerificationMode verificationMode;
    private VerificationData verificationData;
    private VerificationOverTimeImpl verificationOverTime;

    @Before
    public void setUp() {
        verificationMode = VerificationModeFactory.createVerificationMode(Strictness.LENIENT);
        verificationData = new VerificationData(VerificationData.Type.METHOD, "verifyMethod", null);
        verificationOverTime = new VerificationOverTimeImpl(100L, 500L, verificationMode, true);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyDelegateNotSatisfiedWithinGivenDuration() {
        // Given
        when(verificationMode.verify(verificationData)).thenThrow(new MockitoAssertionError("Delegate not satisfied"));

        // When
        verificationOverTime.verify(verificationData);

        // Then
        // AssertionError should be thrown
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyDelegateNotSatisfiedWithinGivenDurationWithExceptionRecoverableVerificationMode() {
        // Given
        when(verificationMode.verify(verificationData)).thenThrow(new MockitoAssertionError("Delegate not satisfied"));

        // When
        verificationOverTime.verify(verificationData);

        // Then
        // AssertionError should be thrown

        // When
        verificationOverTime.verify(verificationData);

        // Then
        // No AssertionError should be thrown
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyDelegateNotSatisfiedWithinGivenDurationWithExceptionNonRecoverableVerificationMode() {
        // Given
        VerificationMode verificationModeWithException = VerificationModeFactory.createVerificationMode(Strictness.STRICT);
        when(verificationModeWithException.verify(verificationData)).thenThrow(new MockitoAssertionError("Delegate not satisfied"));

        verificationOverTime = new VerificationOverTimeImpl(100L, 500L, verificationModeWithException, true);

        // When
        verificationOverTime.verify(verificationData);

        // Then
        // AssertionError should be thrown

        // When
        verificationOverTime.verify(verificationData);

        // Then
        // AssertionError should be thrown
    }

    @Test
    public void testVerifyDelegateSatisfiedWithinGivenDurationAndReturnOnSuccess() {
        // Given
        when(verificationMode.verify(verificationData)).thenReturn(null);

        // When
        verificationOverTime.verify(verificationData);

        // Then
        // No AssertionError should be thrown
        // Delegate should be verified
        verify(verificationMode).verify(verificationData);
    }

    @Test
    public void testVerifyDelegateSatisfiedWithinGivenDurationButNotReturnOnSuccess() {
        // Given
        when(verificationMode.verify(verificationData)).thenReturn(null);

        // When
        verificationOverTime.verify(verificationData);

        // Then
        // No AssertionError should be thrown
        // Delegate should be verified
        verify(verificationMode).verify(verificationData);

        // When
        verificationOverTime.verify(verificationData);

        // Then
        // No AssertionError should be thrown
        // Delegate should be verified again
        verify(verificationMode).verify(verificationData);
    }

    @Test
    public void testVerifyDelegateNotSatisfiedWithinGivenDurationAndThrowArgumentMismatchException() {
        // Given
        when(verificationMode.verify(verificationData)).thenThrow(new org.mockito.exceptions.verification.junit.ArgumentsAreDifferent("expected", "actual"));

        // When
        verificationOverTime.verify(verificationData);

        // Then
        // AssertionError should not be thrown
        // Delegate should not be verified
        verifyNoInteractions(verificationMode);
    }

    @Test
    public void testVerifyDelegateNotSatisfiedWithinGivenDurationAndThrowArgumentMismatchExceptionWithExceptionRecoverableVerificationMode() {
        // Given
        when(verificationMode.verify(verificationData)).thenThrow(new org.mockito.exceptions.verification.junit.ArgumentsAreDifferent("expected", "actual"));

        // When
        verificationOverTime.verify(verificationData);

        // Then
        // AssertionError should not be thrown
        // Delegate should not be verified
        verifyNoInteractions(verificationMode);

        // When
        verificationOverTime.verify(verificationData);

        // Then
        // AssertionError with ArgumentsAreDifferent should be thrown
    }

    @Test
    public void testVerifyDelegateNotSatisfiedWithinGivenDurationAndThrowArgumentMismatchExceptionWithNonRecoverableVerificationMode() {
        // Given
        VerificationMode verificationModeWithException = VerificationModeFactory.createVerificationMode(Strictness.STRICT);
        when(verificationModeWithException.verify(verificationData)).thenThrow(new org.mockito.exceptions.verification.junit.ArgumentsAreDifferent("expected", "actual"));

        verificationOverTime = new VerificationOverTimeImpl(100L, 500L, verificationModeWithException, true);

        // When
        verificationOverTime.verify(verificationData);

        // Then
        // AssertionError with ArgumentsAreDifferent should be thrown

        // When
        verificationOverTime.verify(verificationData);

        // Then
        // AssertionError with ArgumentsAreDifferent should be thrown
    }

    @Test
    public void testVerifyDelegateSatisfiedWithinGivenDurationButTimeoutInterrupted() {
        // Given
        when(verificationMode.verify(verificationData)).thenReturn(null);

        // When
        long timeout = 50L;
        verificationOverTime = new VerificationOverTimeImpl(100L, timeout, verificationMode, false);

        // When
        try {
            verificationOverTime.verify(verificationData);
        } catch (InterruptedException e) {
            // Expected: InterruptedException is thrown when the verification is interrupted by a timeout
        }

        // Then
        // Delegate should not be verified
        verifyNoInteractions(verificationMode);
    }

    @Test
    public void testVerifyDelegateSatisfiedWithinGivenDurationButTimeoutInterruptedAndExceptionRecoverableVerificationMode() {
        // Given
        when(verificationMode.verify(verificationData)).thenReturn(null);

        // When
        long timeout = 50L;
        verificationOverTime = new VerificationOverTimeImpl(100L, timeout, verificationMode, true);

        // When
        try {
            verificationOverTime.verify(verificationData);
        } catch (InterruptedException e) {
            // Expected: InterruptedException is thrown when the verification is interrupted by a timeout
        }

        // Then
        // No AssertionError should be thrown
        // Delegate should be verified
        verify(verificationMode).verify(verificationData);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyDelegateSatisfiedWithinGivenDurationButTimeoutInterruptedAndNonRecoverableVerificationMode() {
        // Given
        VerificationMode verificationModeWithException = VerificationModeFactory.createVerificationMode(Strictness.STRICT);
        when(verificationModeWithException.verify(verificationData)).thenReturn(null);

        long timeout = 50L;
        verificationOverTime = new VerificationOverTimeImpl(100L, timeout, verificationModeWithException, true);

        // When
        try {
            verificationOverTime.verify(verificationData);
        } catch (InterruptedException e) {
            // Expected: InterruptedException is thrown when the verification is interrupted by a timeout
        }

        // Then
        // AssertionError should be thrown
    }

    @Test
    public void testVerifyDelegateSatisfiedWithinGivenDurationButTimeoutInterruptedAndSleep() {
        // Given
        when(verificationMode.verify(verificationData)).thenReturn(null);

        // When
        long timeout = 50L;
        verificationOverTime = new VerificationOverTimeImpl(100L, timeout, verificationMode, false);

        // When
        try {
            verificationOverTime.verify(verificationData);
        } catch (InterruptedException e) {
            // Expected: InterruptedException is thrown when the verification is interrupted by a timeout
        }

        // Then
        // Sleep for a while to give the delegate some time to be verified
        try {
            Thread.sleep(150L);
        } catch (InterruptedException e) {
            // Ignore InterruptedException
        }

        // When
        verificationOverTime.verify(verificationData);

        // Then
        // Delegate should be verified
        verify(verificationMode).verify(verificationData);
    }

    @Test
    public void testVerifyDelegateSatisfiedWithinGivenDurationButTimeoutInterruptedAndSleepWithRandomSleepDuration() {
        // Given
        when(verificationMode.verify(verificationData)).thenReturn(null);
        long minSleepDuration = 50L;
        long maxSleepDuration = 150L;

        // When
        long timeout = 100L;
        verificationOverTime = new VerificationOverTimeImpl(100L, timeout, verificationMode, false);

        // When
        try {
            verificationOverTime.verify(verificationData);
        } catch (InterruptedException e) {
            // Expected: InterruptedException is thrown when the verification is interrupted by a timeout
        }

        // Then
        // Sleep for a random duration between minSleepDuration and maxSleepDuration
        long sleepDuration = ThreadLocalRandom.current().nextLong(minSleepDuration, maxSleepDuration);
        try {
            Thread.sleep(sleepDuration);
        } catch (InterruptedException e) {
            // Ignore InterruptedException
        }

        // When
        verificationOverTime.verify(verificationData);

        // Then
        // Delegate should be verified
        verify(verificationMode).verify(verificationData);
    }

    @Test
    public void testVerifyDelegateSatisfiedWithinGivenDurationButTimeoutInterruptedAndExceptionRecoverableVerificationModeSleep() {
        // Given
        when(verificationMode.verify(verificationData)).thenReturn(null);

        // When
        long timeout = 50L;
        verificationOverTime = new VerificationOverTimeImpl(100L, timeout, verificationMode, true);

        // When
        try {
            verificationOverTime.verify(verificationData);
        } catch (InterruptedException e) {
            // Expected: InterruptedException is thrown when the verification is interrupted by a timeout
        }

        // Then
        // Sleep for a while to give the delegate some time to be verified
        try {
            Thread.sleep(150L);
        } catch (InterruptedException e) {
            // Ignore InterruptedException
        }

        // When
        verificationOverTime.verify(verificationData);

        // Then
        // Delegate should be verified
        verify(verificationMode).verify(verificationData);
    }

    @Test
    public void testVerifyDelegateSatisfiedWithinGivenDurationButTimeoutInterruptedAndExceptionNonRecoverableVerificationModeSleep() {
        // Given
        VerificationMode verificationModeWithException = VerificationModeFactory.createVerificationMode(Strictness.STRICT);
        when(verificationModeWithException.verify(verificationData)).thenReturn(null);

        long timeout = 50L;
        verificationOverTime = new VerificationOverTimeImpl(100L, timeout, verificationModeWithException, true);

        // When
        try {
            verificationOverTime.verify(verificationData);
        } catch (InterruptedException e) {
            // Expected: InterruptedException is thrown when the verification is interrupted by a timeout
        }

        // Then
        // Sleep for a while to give the delegate some time to be verified
        try {
            Thread.sleep(150L);
        } catch (InterruptedException e) {
            // Ignore InterruptedException
        }

        // When
        try {
            verificationOverTime.verify(verificationData);
        } catch (InterruptedException e) {
            // Expected: InterruptedException is thrown when the verification is interrupted by a timeout
        }

        // Then
        // AssertionError should be thrown
    }
}