package org.mockito.internal.verification;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerify_DelegateSuccessfulInTime_ReturnImmediately() {
        // Arrange
        VerificationData data = mock(VerificationData.class);
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate.canRecoverFromFailure(delegate)).thenReturn(true);
        when(delegate.canRecoverFromFailure(delegate)).thenReturn(true);
        when(delegate.canRecoverFromFailure(delegate)).thenReturn(true);
        when(delegate.canRecoverFromFailure(delegate)).thenReturn(true);
        when(delegate.canRecoverFromFailure(delegate)).thenReturn(true);
        when(delegate.canRecoverFromFailure(delegate)).thenReturn(true);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, true, new Timer());

        // Act
        verificationOverTime.verify(data);

        // Assert
        verify(delegate).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_DelegateNotSuccessfulInTime_ThrowException() {
        // Arrange
        VerificationData data = mock(VerificationData.class);
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate.canRecoverFromFailure(delegate)).thenReturn(true);
        when(delegate.canRecoverFromFailure(delegate)).thenReturn(true);
        when(delegate.canRecoverFromFailure(delegate)).thenReturn(true);
        when(delegate.canRecoverFromFailure(delegate)).thenReturn(false);
        when(delegate.canRecoverFromFailure(delegate)).thenReturn(false);
        when(delegate.canRecoverFromFailure(delegate)).thenReturn(false);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, true, new Timer());

        // Act
        verificationOverTime.verify(data);
    }

    @Test
    public void testVerify_DelegateSuccessfulAfterDuration_ReturnAfterFullTime() {
        // Arrange
        VerificationData data = mock(VerificationData.class);
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate.canRecoverFromFailure(delegate)).thenReturn(true);
        when(delegate.canRecoverFromFailure(delegate)).thenReturn(true);
        when(delegate.canRecoverFromFailure(delegate)).thenReturn(true);
        when(delegate.canRecoverFromFailure(delegate)).thenReturn(true);
        when(delegate.canRecoverFromFailure(delegate)).thenReturn(true);
        when(delegate.canRecoverFromFailure(delegate)).thenReturn(true);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, false, new Timer());

        // Act
        verificationOverTime.verify(data);

        // Assert
        verify(delegate).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_DelegateNotSuccessfulAfterDuration_ThrowException() {
        // Arrange
        VerificationData data = mock(VerificationData.class);
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate.canRecoverFromFailure(delegate)).thenReturn(true);
        when(delegate.canRecoverFromFailure(delegate)).thenReturn(true);
        when(delegate.canRecoverFromFailure(delegate)).thenReturn(true);
        when(delegate.canRecoverFromFailure(delegate)).thenReturn(false);
        when(delegate.canRecoverFromFailure(delegate)).thenReturn(false);
        when(delegate.canRecoverFromFailure(delegate)).thenReturn(false);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, false, new Timer());

        // Act
        verificationOverTime.verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_DelegateThrowException_CanRecoverFromFailure_ReturnAfterDelay() {
        // Arrange
        VerificationData data = mock(VerificationData.class);
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate.canRecoverFromFailure(delegate)).thenReturn(false);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, false, new Timer());

        // Act
        verificationOverTime.verify(data);
    }

    @Test(expected = RuntimeException.class)
    public void testVerify_DelegateThrowException_CannotRecoverFromFailure_ThrowException() {
        // Arrange
        VerificationData data = mock(VerificationData.class);
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate.canRecoverFromFailure(delegate)).thenReturn(true);
        when(delegate.canRecoverFromFailure(delegate)).thenReturn(true);
        when(delegate.canRecoverFromFailure(delegate)).thenReturn(true);
        when(delegate.canRecoverFromFailure(delegate)).thenReturn(true);
        when(delegate.canRecoverFromFailure(delegate)).thenReturn(true);
        when(delegate.canRecoverFromFailure(delegate)).thenReturn(true);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, false, new Timer());

        // Act
        verificationOverTime.verify(data);
    }
}