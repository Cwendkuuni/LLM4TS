import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.*;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {
    
    @Test
    public void testVerify_withReturnOnSuccess() {
        // create mock objects
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        
        // create VerificationOverTimeImpl object
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, true);
        
        // configure delegate behavior
        doThrow(new MockitoAssertionError()).when(delegate).verify(data);
        
        // invoke the method to test
        verificationOverTime.verify(data);
        
        // verify that delegate.verify method is called once
        verify(delegate, times(1)).verify(data);
    }
    
    @Test(expected = MockitoAssertionError.class)
    public void testVerify_withoutReturnOnSuccess_throwError() {
        // create mock objects
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        
        // create VerificationOverTimeImpl object
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, false);
        
        // configure delegate behavior
        doThrow(new MockitoAssertionError()).when(delegate).verify(data);
        
        // invoke the method to test
        verificationOverTime.verify(data);
        
        // verify that delegate.verify method is called more than once
        verify(delegate, atLeast(2)).verify(data);
        verify(delegate, atMost(10)).verify(data);
    }
    
    @Test(expected = MockitoAssertionError.class)
    public void testVerify_withoutReturnOnSuccess_noRecovery() {
        // create mock objects
        AtMost atMost = mock(AtMost.class);
        VerificationData data = mock(VerificationData.class);
        
        // create VerificationOverTimeImpl object
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 500, atMost, false);
        
        // configure atMost behavior
        doThrow(new MockitoAssertionError()).when(atMost).verify(data);
        
        // invoke the method to test
        verificationOverTime.verify(data);
        
        // verify that atMost.verify method is called more than once
        verify(atMost, atLeast(2)).verify(data);
        verify(atMost, atMost(10)).verify(data);
    }
    
}