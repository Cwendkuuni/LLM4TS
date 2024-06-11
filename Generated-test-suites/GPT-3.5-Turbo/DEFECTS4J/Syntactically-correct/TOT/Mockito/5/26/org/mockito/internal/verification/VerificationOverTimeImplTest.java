import org.junit.*;
import org.mockito.*;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerify_ReturnOnSuccess() {
        long pollingPeriodMillis = 1000;
        long durationMillis = 5000;
        
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, true);
        
        VerificationData data = Mockito.mock(VerificationData.class);
        
        verificationMode.verify(data);
        
        Mockito.verify(delegate).verify(data);
    }
    
    @Test
    public void testVerify_NotReturnOnSuccess() {
        long pollingPeriodMillis = 1000;
        long durationMillis = 5000;
        
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);
        
        VerificationData data = Mockito.mock(VerificationData.class);
        
        // Setting up the delegate to be satisfied after 3 polling periods
        Mockito.doThrow(new MockitoAssertionError("Delegate not satisfied yet")).doThrow(new MockitoAssertionError("Delegate not satisfied yet")).doNothing().when(delegate).verify(data);
        
        verificationMode.verify(data);
        
        Mockito.verify(delegate, Mockito.times(3)).verify(data);
    }
    
    @Test(expected = MockitoAssertionError.class)
    public void testVerify_DelegateFailsBeforeTimeout() {
        long pollingPeriodMillis = 1000;
        long durationMillis = 5000;
        
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, true);
        
        VerificationData data = Mockito.mock(VerificationData.class);
        
        Mockito.doThrow(new MockitoAssertionError("Delegate failed")).when(delegate).verify(data);
        
        verificationMode.verify(data);
    }
    
    @Test(timeout = 6000)
    public void testVerify_DelegateDoesNotSatisfyWithinTimeout() {
        long pollingPeriodMillis = 1000;
        long durationMillis = 5000;
        
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, true);
        
        VerificationData data = Mockito.mock(VerificationData.class);
        
        Mockito.doThrow(new MockitoAssertionError("Delegate not satisfied yet")).when(delegate).verify(data);
        
        verificationMode.verify(data);
    }

}