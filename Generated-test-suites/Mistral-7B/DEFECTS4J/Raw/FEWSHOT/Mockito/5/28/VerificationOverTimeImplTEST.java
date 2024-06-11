package org.mockito.internal.verification;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import java.util.concurrent.TimeUnit;

public class VerificationOverTimeImplTest {

    private static final long POLLING_PERIOD_MILLIS = 10;
    private static final long DURATION_MILLIS = 500;
    private static final long SLEEP_MILLIS = 5;
    private VerificationMode mockVerificationMode;
    private VerificationData mockVerificationData;
    private Timer mockTimer;
    private VerificationOverTimeImpl verificationModeUnderTest;

    @Before
    public void setUp() {
        mockVerificationData = mock(VerificationData.class);
        mockTimer = mock(Timer.class);
        mockVerificationMode = mock(VerificationMode.class);
        verificationModeUnderTest = new VerificationOverTimeImpl(POLLING_PERIOD_MILLIS, DURATION_MILLIS, mockVerificationMode, true, mockTimer);
    }

    @Test
    public void testVerifySuccessWithImmediateReturn() {
        // Given
        mockVerificationMode.verify(mockVerificationData);

        // When
        verificationModeUnderTest.verify(mockVerificationData);

        // Then
        verify(mockVerificationMode).verify(mockVerificationData);
    }

    @Test
    public void testVerifySuccessWithDelayedReturn() {
        // Given
        mockVerificationMode.verify(mockVerificationData);

        // When
        VerificationOverTimeImpl verificationModeWithDelayedReturn = new VerificationOverTimeImpl(POLLING_PERIOD_MILLIS, DURATION_MILLIS, mockVerificationMode, false, mockTimer);
        verificationModeWithDelayedReturn.verify(mockVerificationData);

        // Then
        verify(mockVerificationMode).verify(mockVerificationData);
    }

    @Test
    public void testVerifyFailureWithImmediateReturn() {
        // Given
        mockVerificationMode.verify(mockVerificationData);
        doThrow(new MockitoAssertionError("Expected error message"))
                .when(mockVerificationMode).verify(mockVerificationData);

        // When
        verificationModeUnderTest.verify(mockVerificationData);

        // Then
        verify(mockVerificationMode).verify(mockVerificationData);
        verifyZeroInteractions(mockTimer);
    }

    @Test
    public void testVerifyFailureWithDelayedReturn() {
        // Given
        mockVerificationMode.verify(mockVerificationData);
        doThrow(new MockitoAssertionError("Expected error message"))
                .when(mockVerificationMode).verify(mockVerificationData);

        // When
        VerificationOverTimeImpl verificationModeWithDelayedReturn = new VerificationOverTimeImpl(POLLING_PERIOD_MILLIS, DURATION_MILLIS, mockVerificationMode, false, mockTimer);
        verificationModeWithDelayedReturn.verify(mockVerificationData);

        // Then
        verify(mockVerificationMode).verify(mockVerificationData);
        verifyZeroInteractions(mockTimer);
    }

    @Test
    public void testVerifyFailureRecoverable() {
        // Given
        mockVerificationMode.verify(mockVerificationData);
        doThrow(new MockitoAssertionError("Expected error message"))
                .when(mockVerificationMode).verify(mockVerificationData);

        // When
        VerificationOverTimeImpl verificationModeWithDelayedReturn = new VerificationOverTimeImpl(POLLING_PERIOD_MILLIS, DURATION_MILLIS, mockVerificationMode, false, mockTimer);

        // Then
        assertFalse(verificationModeUnderTest.isSatisfied());

        // When
        verificationModeUnderTest.verify(mockVerificationData);

        // Then
        verify(mockVerificationMode).verify(mockVerificationData);
        assertTrue(verificationModeUnderTest.isSatisfied());
    }

    @Test
    public void testVerifyFailureNonRecoverable() {
        // Given
        mockVerificationMode.verify(mockVerificationData);
        doThrow(new MockitoAssertionError("Expected error message"))
                .when(mockVerificationMode).verify(mockVerificationData);

        // When
        VerificationOverTimeImpl verificationModeWithDelayedReturn = new VerificationOverTimeImpl(POLLING_PERIOD_MILLIS, DURATION_MILLIS, mockVerificationMode, false, mockTimer);

        // Then
        assertFalse(verificationModeUnderTest.isSatisfied());

        // When
        doThrow(new AssertionError("Expected non-Mockito error message"))
                .when(mockVerificationMode).verify(mockVerificationData);

        // Then
        assertThrows(AssertionError.class, () -> verificationModeUnderTest.verify(mockVerificationData));
    }

    @Test
    public void testVerifyNoInteractions() {
        // Given
        mockVerificationMode.verifyNoInteractions(mockVerificationData);

        // When
        VerificationOverTimeImpl verificationModeUnderTest = new VerificationOverTimeImpl(POLLING_PERIOD_MILLIS, DURATION_MILLIS, mockVerificationMode, true, mockTimer);
        verificationModeUnderTest.verify(mockVerificationData);

        // Then
        verifyNoInteractions(mockVerificationMode);
    }

    @Test
    public void testVerifyInteractions() {
        // Given
        mockVerificationMode.verify(mockVerificationData, times(1));

        // When
        VerificationOverTimeImpl verificationModeUnderTest = new VerificationOverTimeImpl(POLLING_PERIOD_MILLIS, DURATION_MILLIS, mockVerificationMode, true, mockTimer);
        verificationModeUnderTest.verify(mockVerificationData);

        // Then
        verify(mockVerificationMode, times(1)).verify(mockVerificationData);
    }

    @Test
    public void testVerifyMultipleInteractions() {
        // Given
        mockVerificationMode.verify(mockVerificationData, times(3));

        // When
        VerificationOverTimeImpl verificationModeUnderTest = new VerificationOverTimeImpl(POLLING_PERIOD_MILLIS, DURATION_MILLIS, mockVerificationMode, true, mockTimer);
        verificationModeUnderTest.verify(mockVerificationData);

        // Then
        verify(mockVerificationMode, times(3)).verify(mockVerificationData);
    }

    @Test
    public void testVerifyAfterDelay() {
        // Given
        mockVerificationMode.verify(mockVerificationData);

        // When
        VerificationOverTimeImpl verificationModeWithDelay = new VerificationOverTimeImpl(POLLING_PERIOD_MILLIS, DURATION_MILLIS + SLEEP_MILLIS, mockVerificationMode, false, mockTimer);
        verificationModeWithDelay.verify(mockVerificationData);

        // Then
        timer.assertWaitedAtLeast(Duration.ofMillis(SLEEP_MILLIS));
        verify(mockVerificationMode).verify(mockVerificationData);
    }

    @Test
    public void testVerifyWithTimeout() {
        // Given
        mockVerificationMode.verify(mockVerificationData);

        // When
        VerificationOverTimeImpl verificationModeWithTimeout = new VerificationOverTimeImpl(POLLING_PERIOD_MILLIS, DURATION_MILLIS, mockVerificationMode, true, mockTimer);

        // Then
        long start = System.nanoTime();
        assertThrows(MockitoAssertionError.class, () -> verificationModeWithTimeout.verify(mockVerificationData));
        long end = System.nanoTime();

        // Assert timeout is reached
        long elapsed = TimeUnit.MILLISECONDS.toMillis(end - start);
        assertTrue(elapsed > DURATION_MILLIS);
    }

    @Test
    public void testVerifyWithTimeoutAndDelayedReturn() {
        // Given
        mockVerificationMode.verify(mockVerificationData);

        // When
        VerificationOverTimeImpl verificationModeWithTimeoutAndDelay = new VerificationOverTimeImpl(POLLING_PERIOD_MILLIS, DURATION_MILLIS, mockVerificationMode, false, mockTimer);

        // Then
        long start = System.nanoTime();
        verificationModeWithTimeoutAndDelay.verify(mockVerificationData);
        long end = System.nanoTime();

        // Assert timeout is reached
        long elapsed = TimeUnit.MILLISECONDS.toMillis(end - start);
        assertTrue(elapsed > DURATION_MILLIS);
    }
}