package org.mockito.internal.verification;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerify_SuccessfulVerification_ReturnImmediately() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        Timer timer = mock(Timer.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, delegate, true, timer);

        // Act
        verification.verify(data);

        // Assert
        verify(delegate).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_FailedVerification_ThrowAssertionError() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        Timer timer = mock(Timer.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, delegate, true, timer);
        doThrow(new MockitoAssertionError("Verification failed")).when(delegate).verify(data);

        // Act
        verification.verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_FailedVerification_DurationElapsed_ThrowAssertionError() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        Timer timer = mock(Timer.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, delegate, false, timer);
        doThrow(new MockitoAssertionError("Verification failed")).when(delegate).verify(data);
        when(timer.isCounting()).thenReturn(false);

        // Act
        verification.verify(data);
    }

    @Test
    public void testVerify_FailedVerification_CanRecover_ReturnAfterSleep() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        Timer timer = mock(Timer.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, delegate, false, timer);
        MockitoAssertionError exception = new MockitoAssertionError("Verification failed");
        doThrow(exception).doNothing().when(delegate).verify(data);
        when(timer.isCounting()).thenReturn(true).thenReturn(false);

        // Act
        verification.verify(data);

        // Assert
        verify(delegate, times(2)).verify(data);
    }
}