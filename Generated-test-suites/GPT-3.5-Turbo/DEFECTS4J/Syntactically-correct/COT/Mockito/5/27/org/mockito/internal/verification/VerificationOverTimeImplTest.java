
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;
import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerify_callsDelegateVerify() {
        // Arrange
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        VerificationData data = mock(VerificationData.class);
        
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);

        // Act
        verificationOverTime.verify(data);

        // Assert
        verify(delegate).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_throwsMockitoAssertionError() {
        // Arrange
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        VerificationData data = mock(VerificationData.class);
        doThrow(new MockitoAssertionError("Delegate verification failed")).when(delegate).verify(data);
        
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);

        // Act
        verificationOverTime.verify(data);
    }

    @Test
    public void testGetPollingPeriod_returnsPollingPeriodMillis() {
        // Arrange
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);
        
        // Act
        long result = verificationOverTime.getPollingPeriod();
        
        // Assert
        assertEquals(pollingPeriodMillis, result);
    }

    @Test
    public void testGetDuration_returnsDurationMillis() {
        // Arrange
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);
        
        // Act
        long result = verificationOverTime.getDuration();
        
        // Assert
        assertEquals(durationMillis, result);
    }

    @Test
    public void testGetDelegate_returnsDelegate() {
        // Arrange
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);
        
        // Act
        VerificationMode result = verificationOverTime.getDelegate();
        
        // Assert
        assertEquals(delegate, result);
    }
}

