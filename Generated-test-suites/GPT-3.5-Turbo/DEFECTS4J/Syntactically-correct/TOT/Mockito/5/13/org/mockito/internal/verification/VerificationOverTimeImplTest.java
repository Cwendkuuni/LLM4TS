import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.mock.MockCreationSettings;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {

    @Test
    public void verify_delegateIsSatisfiedWithinDuration_returnOnSuccessTrue_success() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, true);

        VerificationData data = mock(VerificationData.class);

        // Act
        verificationOverTime.verify(data);

        // Assert
        verify(delegate).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void verify_delegateNotSatisfiedWithinDuration_returnOnSuccessTrue_timeout() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        doThrow(new MockitoAssertionError("Delegate verification failed"))
                .when(delegate).verify(any(VerificationData.class));

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, true);

        VerificationData data = mock(VerificationData.class);

        // Act
        verificationOverTime.verify(data);
    }

    @Test
    public void verify_delegateIsSatisfiedWithinDuration_returnOnSuccessFalse_success() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, false);

        VerificationData data = mock(VerificationData.class);

        // Act
        verificationOverTime.verify(data);

        // Assert
        verify(delegate).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void verify_delegateNotSatisfiedWithinDuration_returnOnSuccessFalse_timeout() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        doThrow(new MockitoAssertionError("Delegate verification failed"))
                .when(delegate).verify(any(VerificationData.class));

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, false);

        VerificationData data = mock(VerificationData.class);

        // Act
        verificationOverTime.verify(data);
    }

    @Test
    public void verify_delegateRecoversFromFailureWithinDuration_returnOnSuccessTrue_success() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        doThrow(new MockitoAssertionError("Delegate verification failed"))
                .doNothing()
                .when(delegate).verify(any(VerificationData.class));

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, true);

        VerificationData data = mock(VerificationData.class);

        // Act
        verificationOverTime.verify(data);

        // Assert
        verify(delegate, times(2)).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void verify_delegateRecoversFromFailureWithinDuration_returnOnSuccessFalse_timeout() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        doThrow(new MockitoAssertionError("Delegate verification failed"))
                .doNothing()
                .when(delegate).verify(any(VerificationData.class));

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, false);

        VerificationData data = mock(VerificationData.class);

        // Act
        verificationOverTime.verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void verify_delegateDoesNotRecoverFromFailureWithinDuration_returnOnSuccessTrue_timeout() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        doThrow(new MockitoAssertionError("Delegate verification failed"))
                .when(delegate).verify(any(VerificationData.class));

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, true);

        VerificationData data = mock(VerificationData.class);

        // Act
        verificationOverTime.verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void verify_delegateDoesNotRecoverFromFailureWithinDuration_returnOnSuccessFalse_timeout() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        doThrow(new MockitoAssertionError("Delegate verification failed"))
                .when(delegate).verify(any(VerificationData.class));

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, false);

        VerificationData data = mock(VerificationData.class);

        // Act
        verificationOverTime.verify(data);
    }

    @Test
    public void verify_delegateIsNoMoreInteractionsSuccess_returnOnSuccessTrue_success() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate instanceof VerificationMode).thenReturn(true);
        when(delegate instanceof NoMoreInteractions).thenReturn(true);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, true);

        VerificationData data = mock(VerificationData.class);

        // Act
        verificationOverTime.verify(data);

        // Assert
        verify(delegate).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void verify_delegateIsNoMoreInteractionsSuccess_returnOnSuccessTrue_timeout() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate instanceof VerificationMode).thenReturn(true);
        when(delegate instanceof NoMoreInteractions).thenReturn(true);
        doThrow(new MockitoAssertionError("Delegate verification failed"))
                .when(delegate).verify(any(VerificationData.class));

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, true);

        VerificationData data = mock(VerificationData.class);

        // Act
        verificationOverTime.verify(data);
    }

    @Test
    public void verify_delegateIsAtMostSuccess_returnOnSuccessTrue_success() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate instanceof AtMost).thenReturn(true);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, true);

        VerificationData data = mock(VerificationData.class);

        // Act
        verificationOverTime.verify(data);

        // Assert
        verify(delegate).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void verify_delegateIsAtMostSuccess_returnOnSuccessTrue_timeout() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate instanceof AtMost).thenReturn(true);
        doThrow(new MockitoAssertionError("Delegate verification failed"))
                .when(delegate).verify(any(VerificationData.class));

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, true);

        VerificationData data = mock(VerificationData.class);

        // Act
        verificationOverTime.verify(data);
    }

}