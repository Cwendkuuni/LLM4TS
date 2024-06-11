
import org.junit.Test;
import static org.mockito.Mockito.*;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

public class VerificationOverTimeImplTest {
    @Test
    public void testVerify_DelegateSatisfiedWithinDuration_Success() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, delegate, true);
        VerificationData data = mock(VerificationData.class);
        // Act
        verification.verify(data);
        // Assert
        verify(delegate).verify(data);
    }
    
    @Test(expected = MockitoAssertionError.class)
    public void testVerify_DelegateNotSatisfiedWithinDuration_Failure() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        doThrow(new MockitoAssertionError("Delegate verification failed"))
            .when(delegate).verify(any(VerificationData.class));
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, delegate, true);
        VerificationData data = mock(VerificationData.class);
        // Act
        verification.verify(data);
        // Assert - MockitoAssertionError should be thrown
    }
    
    @Test
    public void testVerify_DelegateNotSatisfiedWithinDuration_CanRecover() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        doThrow(new MockitoAssertionError("Delegate verification failed"))
            .when(delegate).verify(any(VerificationData.class));
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, delegate, false);
        VerificationData data = mock(VerificationData.class);
        // Act
        verification.verify(data);
        // Assert
        verify(delegate, atLeastOnce()).verify(data);
    }
    
    @Test
    public void testVerify_DelegateIsAtMost_Failure() {
        // Arrange
        VerificationMode delegate = mock(AtMost.class);
        doThrow(new MockitoAssertionError("Delegate verification failed"))
            .when(delegate).verify(any(VerificationData.class));
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, delegate, true);
        VerificationData data = mock(VerificationData.class);
        // Act
        verification.verify(data);
        // Assert - MockitoAssertionError should be thrown
    }

    @Test
    public void testGetPollingPeriod() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, delegate, true);
        // Act
        long pollingPeriod = verification.getPollingPeriod();
        // Assert
        assertEquals(1000, pollingPeriod);
    }
    
    @Test
    public void testGetDuration() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, delegate, true);
        // Act
        long duration = verification.getDuration();
        // Assert
        assertEquals(5000, duration);
    }
    
    @Test
    public void testGetDelegate() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, delegate, true);
        // Act
        VerificationMode delegateResult = verification.getDelegate();
        // Assert
        assertEquals(delegate, delegateResult);
    }
}
