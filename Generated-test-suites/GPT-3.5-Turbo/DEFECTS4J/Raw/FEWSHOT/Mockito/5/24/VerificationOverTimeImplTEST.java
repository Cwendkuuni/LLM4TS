import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class VerificationOverTimeImplTest {
    
    @Mock
    private VerificationMode delegate;
    
    private VerificationOverTimeImpl verificationOverTime;
    private VerificationData verificationData;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, true);
        verificationData = Mockito.mock(VerificationData.class);
    }
    
    @Test
    public void testVerify_ReturnOnSuccess() {
        Mockito.when(delegate.verify(verificationData)).thenReturn();
        
        verificationOverTime.verify(verificationData);
        
        Mockito.verify(delegate).verify(verificationData);
        assertTrue(verificationOverTime.getDelegate() == delegate);
        assertTrue(verificationOverTime.getDuration() == 1000);
        assertTrue(verificationOverTime.getPollingPeriod() == 100);
    }
    
    @Test(expected = MockitoAssertionError.class)
    public void testVerify_ThrowsAssertionError() {
        Mockito.doThrow(new MockitoAssertionError()).when(delegate).verify(verificationData);
        
        verificationOverTime.verify(verificationData);
        
        Mockito.verify(delegate).verify(verificationData);
        assertTrue(verificationOverTime.getDelegate() == delegate);
        assertTrue(verificationOverTime.getDuration() == 1000);
        assertTrue(verificationOverTime.getPollingPeriod() == 100);
    }
    
    @Test(expected = MockitoAssertionError.class)
    public void testVerify_ThrowsException() {
        Mockito.doThrow(new org.mockito.exceptions.verification.junit.ArgumentsAreDifferent()).when(delegate).verify(verificationData);
        
        verificationOverTime.verify(verificationData);
        
        Mockito.verify(delegate).verify(verificationData);
        assertTrue(verificationOverTime.getDelegate() == delegate);
        assertTrue(verificationOverTime.getDuration() == 1000);
        assertTrue(verificationOverTime.getPollingPeriod() == 100);
    }
    
    @Test
    public void testCanRecoverFromFailure() {
        VerificationMode atMost = Mockito.mock(org.mockito.verification.AtMost.class);
        VerificationMode noMoreInteractions = Mockito.mock(org.mockito.verification.NoMoreInteractions.class);
        
        assertTrue(verificationOverTime.canRecoverFromFailure(delegate));
        assertFalse(verificationOverTime.canRecoverFromFailure(atMost));
        assertFalse(verificationOverTime.canRecoverFromFailure(noMoreInteractions));
    }
    
}