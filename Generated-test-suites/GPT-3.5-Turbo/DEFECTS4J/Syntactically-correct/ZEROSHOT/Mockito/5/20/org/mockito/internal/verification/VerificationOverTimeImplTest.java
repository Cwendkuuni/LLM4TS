import org.junit.Test;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;
import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {
    
    @Test
    public void testVerifyVerify() {
        Timer timer = mock(Timer.class);
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
        
        VerificationData data = mock(VerificationData.class);
        delegate.verify(data);
        verify(timer, atLeastOnce()).start();
        
        verify(verificationOverTime).verify(data);
        verify(delegate).verify(data);
        verify(timer).isCounting();
        verify(timer, atLeastOnce()).start();
        verifyNoMoreInteractions(data, delegate, timer);
    }
    
    @Test(expected = MockitoAssertionError.class)
    public void testVerifyVerifyThrowsError() {
        Timer timer = mock(Timer.class);
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
        
        VerificationData data = mock(VerificationData.class);
        MockitoAssertionError error = mock(MockitoAssertionError.class);
        doThrow(error).when(delegate).verify(data);
        doNothing().when(timer).start();
        
        try {
            verificationOverTime.verify(data);
        } finally {
            verify(error).getCause();
            verify(error).getMessage();
            verify(delegate).verify(data);
            verify(timer).start();
            verify(timer).isCounting();
            verifyNoMoreInteractions(data, delegate, timer, error);
        }
    }
    
    @Test(expected = AssertionError.class)
    public void testHandleVerifyExceptionCannotRecover() {
        Timer timer = mock(Timer.class);
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
        
        AssertionError e = mock(AssertionError.class);
        assertFalse(verificationOverTime.canRecoverFromFailure(delegate));
        verificationOverTime.handleVerifyException(e);
        
        verify(verificationOverTime).canRecoverFromFailure(delegate);
        verifyNoMoreInteractions(e);
    }
    
    @Test
    public void testHandleVerifyExceptionCanRecover() {
        Timer timer = mock(Timer.class);
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
        
        AssertionError e = mock(AssertionError.class);
        assertTrue(verificationOverTime.canRecoverFromFailure(delegate));
        verificationOverTime.handleVerifyException(e);
        
        verify(verificationOverTime).canRecoverFromFailure(delegate);
        verify(timer).sleep(pollingPeriodMillis);
        verifyNoMoreInteractions(e);
    }
    
    @Test
    public void testSleep() {
        Timer timer = mock(Timer.class);
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
        
        long sleep = 500;
        verificationOverTime.sleep(sleep);
        
        verify(timer).sleep(sleep);
        verifyNoMoreInteractions(timer);
    }
    
    @Test
    public void testGetPollingPeriod() {
        Timer timer = mock(Timer.class);
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
        
        assertEquals(pollingPeriodMillis, verificationOverTime.getPollingPeriod());
    }
    
    @Test
    public void testGetDuration() {
        Timer timer = mock(Timer.class);
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
        
        assertEquals(durationMillis, verificationOverTime.getDuration());
    }
    
    @Test
    public void testGetDelegate() {
        Timer timer = mock(Timer.class);
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
        
        assertEquals(delegate, verificationOverTime.getDelegate());
    }
}