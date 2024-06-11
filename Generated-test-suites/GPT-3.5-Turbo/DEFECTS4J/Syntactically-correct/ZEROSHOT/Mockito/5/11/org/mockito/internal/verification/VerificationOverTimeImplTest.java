import static org.mockito.Mockito.*;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;
import org.junit.Test;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerify_ReturnOnSuccess() {
        // Setup
        long pollingPeriodMillis = 1000;
        long durationMillis = 5000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        Timer timer = mock(Timer.class);
        
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
        VerificationData data = mock(VerificationData.class);
        
        // Verify
        verificationOverTime.verify(data);
        
        // Assert
        verify(delegate).verify(data);
    }
    
    @Test(expected = MockitoAssertionError.class)
    public void testVerify_ThrowException() {
        // Setup
        long pollingPeriodMillis = 1000;
        long durationMillis = 5000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        Timer timer = mock(Timer.class);
        
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
        VerificationData data = mock(VerificationData.class);
        
        doThrow(new MockitoAssertionError("Verification failed")).when(delegate).verify(data);
        
        // Verify
        verificationOverTime.verify(data);
    }
    
    @Test(expected = MockitoAssertionError.class)
    public void testVerify_ThrowArgumentsAreDifferentException() {
        // Setup
        long pollingPeriodMillis = 1000;
        long durationMillis = 5000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        Timer timer = mock(Timer.class);
        
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
        VerificationData data = mock(VerificationData.class);
        
        doThrow(new org.mockito.exceptions.verification.junit.ArgumentsAreDifferent("Arguments are different")).when(delegate).verify(data);
        
        // Verify
        verificationOverTime.verify(data);
    }
    
    @Test
    public void testVerify_ReturnAfterDuration() {
        // Setup
        long pollingPeriodMillis = 1000;
        long durationMillis = 5000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = false;
        Timer timer = mock(Timer.class);
        
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
        VerificationData data = mock(VerificationData.class);
        
        // Verify
        verificationOverTime.verify(data);
        
        // Assert
        verify(delegate).verify(data);
    }
    
    @Test(expected = MockitoAssertionError.class)
    public void testVerify_DelegateUnsatisfied_ErrorNotRecovered() {
        // Setup
        long pollingPeriodMillis = 1000;
        long durationMillis = 5000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = false;
        Timer timer = mock(Timer.class);
        
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
        VerificationData data = mock(VerificationData.class);
        
        doThrow(new MockitoAssertionError("Verification failed")).when(delegate).verify(data);
        
        // Verify
        verificationOverTime.verify(data);
    }
    
    @Test(expected = MockitoAssertionError.class)
    public void testVerify_DelegateUnsatisfied_ErrorRecovered() {
        // Setup
        long pollingPeriodMillis = 1000;
        long durationMillis = 5000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = false;
        Timer timer = mock(Timer.class);
        
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
        VerificationData data = mock(VerificationData.class);
        
        doThrow(new MockitoAssertionError("Verification failed")).doNothing().when(delegate).verify(data);
        
        // Verify
        verificationOverTime.verify(data);
    }
}