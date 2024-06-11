package org.mockito.internal.verification.VerificationOverTimeImplTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.exceptions.base.MockitoAssertionError;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class VerificationOverTimeImplTest {
    private VerificationMode verificationMode;
    private VerificationOverTimeImpl verificationOverTime;
    private VerificationData verificationData;

    @Before
    public void setUp() {
        verificationMode = mock(VerificationMode.class);
        verificationData = new VerificationData(ArgumentMatchers.any(), ArgumentMatchers.eq(1L), ArgumentMatchers.eq(2L));
        verificationOverTime = new VerificationOverTimeImpl(500L, 5000L, verificationMode, true);
    }

    @Test
    public void givenDelegateVerificationModeIsSatisfiedBeforeTimeout_whenVerificationModeIsCalled_thenShouldReturnSuccess() {
        // Arrange
        when(verificationMode.verify(verificationData)).thenReturn(null);

        // Act
        verificationOverTime.verify(verificationData);

        // Assert
        verifyNoMoreInteractions(verificationMode);
    }

    @Test
    public void givenDelegateVerificationModeIsNotSatisfied_whenVerificationModeIsCalled_thenShouldThrowAssertionError() {
        // Arrange
        when(verificationMode.verify(verificationData)).thenThrow(new MockitoAssertionError("Expected exception message"));

        // Act & Assert
        assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(verificationData));
    }

    @Test
    public void givenDelegateVerificationModeThrowsException_whenVerificationModeIsCalled_thenShouldThrowSameException() {
        // Arrange
        when(verificationMode.verify(verificationData)).thenThrow(new RuntimeException("Expected exception message"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> verificationOverTime.verify(verificationData));
    }

    @Test
    public void givenDelegateVerificationModeRecoversFromFailure_whenVerificationModeIsCalled_thenShouldReturnSuccess() {
        // Arrange
        when(verificationMode.verify(verificationData)).thenThrow(new MockitoAssertionError("Expected exception message"));

        // Act
        verificationOverTime.verify(verificationData);
        sleep(pollingPeriodMillis);

        // Assert
        verifyNoMoreInteractions(verificationMode);
    }

    @Test
    public void givenGetters_whenCalled_thenReturnsCorrectValues() {
        assertEquals(500L, verificationOverTime.getPollingPeriod());
        assertEquals(5000L, verificationOverTime.getDuration());
        assertEquals(verificationMode, verificationOverTime.getDelegate());
    }

    @Test
    public void givenHandleVerifyExceptionIsCalledWithAssertionError_whenCanRecoverFromFailureIsTrue_thenShouldReturnSameError() {
        // Arrange
        AssertionError expectedError = new AssertionError("expected error message");

        // Act
        AssertionError actualError = verificationOverTime.handleVerifyException(expectedError);

        // Assert
        assertEquals(expectedError, actualError);
    }

    @Test
    public void givenHandleVerifyExceptionIsCalledWithAssertionError_whenCanRecoverFromFailureIsFalse_thenShouldThrowSameError() {
        // Arrange
        AssertionError expectedError = new AssertionError("expected error message");

        // Act & Assert
        assertThrows(AssertionError.class, () -> verificationOverTime.handleVerifyException(expectedError));
    }

    @Test
    public void givenCanRecoverFromFailureIsCalledWithVerificationModeThatDoesNotRecover_thenShouldReturnFalse() {
        // Arrange
        VerificationMode verificationModeThatDoesNotRecover = mock(AtMost.class);

        // Act
        boolean result = verificationOverTime.canRecoverFromFailure(verificationModeThatDoesNotRecover);

        // Assert
        assertFalse(result);
    }

    @Test
    public void givenCanRecoverFromFailureIsCalledWithVerificationModeThatRecovers_thenShouldReturnTrue() {
        // Arrange
        VerificationMode verificationModeThatRecovers = mock(VerificationWithTimeout.class);

        // Act
        boolean result = verificationOverTime.canRecoverFromFailure(verificationModeThatRecovers);

        // Assert
        assertTrue(result);
    }

    @Test
    public void givenSleepIsCalled_whenPassedSleepTimeIsLessThanZero_thenThrowsIllegalArgumentException() {
        // Arrange

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> verificationOverTime.sleep(-1L));
    }

    @Test
    public void givenSleepIsCalled_whenPassedSleepTimeIsGreaterThanZero_thenSleepsForCorrectDuration() {
        // Arrange

        // Act
        long startTime = System.currentTimeMillis();
        verificationOverTime.sleep(500L);
        long endTime = System.currentTimeMillis();

        // Assert
        long sleepDuration = endTime - startTime;
        assertEquals(500L, sleepDuration);
    }
}