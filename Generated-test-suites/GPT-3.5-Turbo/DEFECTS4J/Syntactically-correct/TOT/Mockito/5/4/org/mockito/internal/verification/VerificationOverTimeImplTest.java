import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {

    @Test
    public void verify_ReturnOnSuccessTrue_DelegateSatisfiedBeforeDuration_ReturnsSuccessfully() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        Timer timer = mock(Timer.class);
        when(timer.isCounting()).thenReturn(true, false);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, true, timer);

        // Act
        verificationOverTime.verify(data);

        // Assert
        verify(delegate).verify(data);
        verifyNoMoreInteractions(delegate);
        verify(timer, times(2)).isCounting();
    }

    @Test
    public void verify_ReturnOnSuccessFalse_DelegateSatisfiedBeforeDuration_ReturnsSuccessfullyAfterFullDuration() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        Timer timer = mock(Timer.class);
        when(timer.isCounting()).thenReturn(true, true, false);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, false, timer);

        // Act
        verificationOverTime.verify(data);

        // Assert
        verify(delegate, times(2)).verify(data);
        verifyNoMoreInteractions(delegate);
        verify(timer, times(3)).isCounting();
    }

    @Test(expected = MockitoAssertionError.class)
    public void verify_DelegateNotSatisfiedBeforeDuration_ThrowsMockitoAssertionError() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        Timer timer = mock(Timer.class);
        when(timer.isCounting()).thenReturn(true, true, false);
        doThrow(MockitoAssertionError.class).when(delegate).verify(data);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, true, timer);

        // Act
        verificationOverTime.verify(data);

        // Assert (Expected exception)
    }

    @Test(expected = MockitoAssertionError.class)
    public void verify_DelegateNotSatisfiedBeforeDuration_CannotRecoverFromFailureThrowsMockitoAssertionError() {
        // Arrange
        AtMost atMostDelegate = mock(AtMost.class);
        VerificationData data = mock(VerificationData.class);
        Timer timer = mock(Timer.class);
        when(timer.isCounting()).thenReturn(true, true, false);
        doThrow(MockitoAssertionError.class).when(atMostDelegate).verify(data);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, atMostDelegate, true, timer);

        // Act
        verificationOverTime.verify(data);

        // Assert (Expected exception)
    }
}