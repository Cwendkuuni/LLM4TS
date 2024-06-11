import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationOverTimeImpl;

import org.junit.Test;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerify() {
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1000, 5000, delegate, true);
        VerificationData data = Mockito.mock(VerificationData.class);
        
        verificationOverTime.verify(data);
        
        verify(delegate).verify(data);
    }
    
    @Test
    public void testVerifyWithTimeout() {
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(2000, 3000, delegate, false);
        VerificationData data = Mockito.mock(VerificationData.class);
        
        verificationOverTime.verify(data);
        
        verify(delegate).verify(data);
    }
    
    @Test(expected = MockitoAssertionError.class)
    public void testVerifyThrowsException() {
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(500, 2000, delegate, true);
        VerificationData data = Mockito.mock(VerificationData.class);
        
        Mockito.doThrow(new MockitoAssertionError("Error")).when(delegate).verify(data);
        
        verificationOverTime.verify(data);
    }
    
    @Test(expected = MockitoAssertionError.class)
    public void testVerifyWithNoRecovery() {
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1000, 2000, delegate, true);
        VerificationData data = Mockito.mock(VerificationData.class);
        
        Mockito.when(delegate.verify(data)).thenThrow(new NoMoreInteractions());
        
        verificationOverTime.verify(data);
    }
    
    @Test(expected = InterruptedException.class)
    public void testVerifyInterruptedException() {
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(500, 1000, delegate, true);
        VerificationData data = Mockito.mock(VerificationData.class);
        
        Mockito.doThrow(new InterruptedException()).when(delegate).verify(data);
        
        verificationOverTime.verify(data);
    }

}