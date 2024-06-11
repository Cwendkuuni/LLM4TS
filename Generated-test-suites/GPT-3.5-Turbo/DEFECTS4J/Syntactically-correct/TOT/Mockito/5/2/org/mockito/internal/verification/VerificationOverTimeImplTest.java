import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {

    private VerificationOverTimeImpl verificationOverTime;
    private VerificationMode delegate;

    @Before
    public void setUp() {
        delegate = mock(VerificationMode.class);
        verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, true);
    }

    @Test
    public void verify_ReturnsSuccessfully_WhenDelegateSatisfiedWithinDuration() {
        // Arrange
        VerificationData data = mock(VerificationData.class);

        // Act
        verificationOverTime.verify(data);

        // Assert
        verify(delegate).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void verify_ThrowsMockitoAssertionError_WhenDelegateNotSatisfiedWithinDuration() {
        // Arrange
        VerificationData data = mock(VerificationData.class);
        doThrow(new MockitoAssertionError("Delegate not satisfied")).when(delegate).verify(data);

        // Act
        verificationOverTime.verify(data);
    }

    @Test
    public void verify_ContinuesVerification_WhenDelegateIsNotSatisfiedAndCanRecover() {
        // Arrange
        VerificationData data = mock(VerificationData.class);
        doThrow(new MockitoAssertionError("Delegate not satisfied")).when(delegate).verify(data);
        when(verificationOverTime.canRecoverFromFailure(delegate)).thenReturn(true);

        // Act
        verificationOverTime.verify(data);

        // Assert
        verify(delegate, times(2)).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void verify_ThrowsMockitoAssertionError_WhenDelegateIsNotSatisfiedAndCannotRecover() {
        // Arrange
        VerificationData data = mock(VerificationData.class);
        doThrow(new MockitoAssertionError("Delegate not satisfied")).when(delegate).verify(data);
        when(verificationOverTime.canRecoverFromFailure(delegate)).thenReturn(false);

        // Act
        verificationOverTime.verify(data);
    }

    @Test
    public void getPollingPeriod_ReturnsCorrectValue() {
        // Arrange
        long expectedPollingPeriod = 100;

        // Act
        long actualPollingPeriod = verificationOverTime.getPollingPeriod();

        // Assert
        assertEquals(expectedPollingPeriod, actualPollingPeriod);
    }

    @Test
    public void getDuration_ReturnsCorrectValue() {
        // Arrange
        long expectedDuration = 1000;

        // Act
        long actualDuration = verificationOverTime.getDuration();

        // Assert
        assertEquals(expectedDuration, actualDuration);
    }

    @Test
    public void getDelegate_ReturnsCorrectValue() {
        // Arrange
        VerificationMode expectedDelegate = delegate;

        // Act
        VerificationMode actualDelegate = verificationOverTime.getDelegate();

        // Assert
        assertEquals(expectedDelegate, actualDelegate);
    }
}