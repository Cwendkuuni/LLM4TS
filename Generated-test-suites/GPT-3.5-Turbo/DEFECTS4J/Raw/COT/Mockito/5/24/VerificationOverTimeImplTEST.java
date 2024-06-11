
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;
import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {
    
    private VerificationOverTimeImpl verificationOverTime;
    private VerificationMode delegate;
    
    @Before
    public void setup() {
        Timer timer = mock(Timer.class);
        delegate = mock(VerificationMode.class);
        verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, false, timer);
    }
    
    @Test
    public void testVerify_DelegateSucceedsBeforeTimeout_ReturnsImmediately() {
        VerificationData data = mock(VerificationData.class);
        when(delegate.verify(data)).thenReturn(true);
        
        verificationOverTime.verify(data);
        
        verify(delegate).verify(data);
    }
    
    @Test
    public void testVerify_DelegateFailsBeforeTimeout_ThrowsAssertionError() {
        VerificationData data = mock(VerificationData.class);
        when(delegate.verify(data)).thenThrow(new MockitoAssertionError("Delegate verification failed"));
        
        try {
            verificationOverTime.verify(data);
        }
        catch (MockitoAssertionError e) {
            // Verify that the error thrown by the delegate is propagated
            assertEquals("Delegate verification failed", e.getMessage());
        }
        
        verify(delegate).verify(data);
    }
    
    @Test
    public void testVerify_DelegateThrowsArgumentsAreDifferentException_CanRecoverFromFailure() {
        VerificationData data = mock(VerificationData.class);
        when(delegate.verify(data)).thenThrow(new ArgumentsAreDifferent("Arguments are different"));
        
        verificationOverTime.verify(data);
        
        verify(delegate).verify(data);
    }
    
    @Test(expected = ArgumentsAreDifferent.class)
    public void testVerify_DelegateThrowsArgumentsAreDifferentException_CannotRecoverFromFailure() {
        VerificationData data = mock(VerificationData.class);
        when(delegate.verify(data)).thenThrow(new ArgumentsAreDifferent("Arguments are different"));
        
        verificationOverTime.verify(data);
    }
}
