import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {
    
    private VerificationOverTimeImpl verificationOverTime;
    private long pollingPeriodMillis;
    private long durationMillis;
    private VerificationMode delegate;
    private boolean returnOnSuccess;
    private Timer timer;
    private VerificationData verificationData;
    
    @Before
    public void setUp() {
        pollingPeriodMillis = 1000;
        durationMillis = 5000;
        delegate = mock(VerificationMode.class);
        returnOnSuccess = true;
        timer = mock(Timer.class);
        verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
        verificationData = mock(VerificationData.class);
    }
    
    @After
    public void tearDown() {
        verificationOverTime = null;
        pollingPeriodMillis = 0;
        durationMillis = 0;
        delegate = null;
        returnOnSuccess = false;
        timer = null;
        verificationData = null;
    }
    
    @Test
    public void verify_shouldReturnImmediatelyWhenDelegateIsSatisfiedAndReturnOnSuccessIsTrue() {
        when(timer.isCounting()).thenReturn(true, false);
        
        verificationOverTime.verify(verificationData);
        
        verify(delegate).verify(verificationData);
    }
    
    @Test
    public void verify_shouldWaitUntilDelegateIsSatisfiedAndDurationHasPassedWhenDelegateIsNotSatisfied() {
        MockitoAssertionError error = new MockitoAssertionError("Delegate not satisfied");
        when(timer.isCounting()).thenReturn(true, true, true, true, true, false);
        when(delegate.verify(verificationData)).thenThrow(error);
        
        try {
            verificationOverTime.verify(verificationData);
        } catch (MockitoAssertionError e) {
            verify(delegate, times(6)).verify(verificationData);
            throw e;
        }
    }
    
    @Test
    public void verify_shouldThrowErrorWhenDelegateIsNotSatisfiedBeforeTimeout() {
        MockitoAssertionError error = new MockitoAssertionError("Delegate not satisfied");
        when(timer.isCounting()).thenReturn(true, true, true, true, true, true, true, true, true, true);
        when(delegate.verify(verificationData)).thenThrow(error);
        
        try {
            verificationOverTime.verify(verificationData);
        } catch (MockitoAssertionError e) {
            verify(delegate, times(10)).verify(verificationData);
            throw e;
        }
    }
    
    @Test
    public void verify_shouldHandleOtherExceptionsThrownByDelegate() {
        org.mockito.exceptions.verification.junit.ArgumentsAreDifferent error = new org.mockito.exceptions.verification.junit.ArgumentsAreDifferent("Arguments are different");
        when(timer.isCounting()).thenReturn(true, false);
        when(delegate.verify(verificationData)).thenThrow(error);
        
        try {
            verificationOverTime.verify(verificationData);
        } catch (org.mockito.exceptions.verification.junit.ArgumentsAreDifferent e) {
            verify(delegate).verify(verificationData);
            throw e;
        }
    }
    
    @Test
    public void handleVerifyException_shouldSleepAndReturnTheErrorWhenCanRecoverFromFailureReturnsTrue() {
        MockitoAssertionError error = new MockitoAssertionError("Delegate not satisfied");
        VerificationMode verificationMode = mock(VerificationMode.class);
        when(verificationMode instanceof VerificationOverTimeImpl).thenReturn(true);
        when(verificationOverTime.canRecoverFromFailure(verificationMode)).thenReturn(true);
        when(timer.isCounting()).thenReturn(true);
        
        AssertionError result = verificationOverTime.handleVerifyException(error);
        
        verify(verificationOverTime, times(1)).canRecoverFromFailure(verificationMode);
        verify(timer).start();
        verify(timer).isCounting();
        verify(timer).isCounting();
        verify(verificationOverTime).sleep(pollingPeriodMillis);
        verify(delegate).verify(verificationData);
        assertEquals(error, result);
    }
    
    @Test(expected = MockitoAssertionError.class)
    public void handleVerifyException_shouldThrowTheErrorWhenCanRecoverFromFailureReturnsFalse() {
        MockitoAssertionError error = new MockitoAssertionError("Delegate not satisfied");
        VerificationMode verificationMode = mock(VerificationMode.class);
        when(verificationMode instanceof VerificationOverTimeImpl).thenReturn(true);
        when(verificationOverTime.canRecoverFromFailure(verificationMode)).thenReturn(false);
        
        verificationOverTime.handleVerifyException(error);
    }
    
    @Test
    public void canRecoverFromFailure_shouldReturnTrueWhenVerificationModeIsNotAtMostOrNoMoreInteractions() {
        VerificationMode verificationMode = mock(VerificationMode.class);
        when(verificationMode instanceof org.mockito.verification.AtMost).thenReturn(false);
        when(verificationMode instanceof org.mockito.verification.NoMoreInteractions).thenReturn(false);
        
        boolean result = verificationOverTime.canRecoverFromFailure(verificationMode);
        
        assertTrue(result);
    }
    
    @Test
    public void canRecoverFromFailure_shouldReturnFalseWhenVerificationModeIsAtMost() {
        VerificationMode verificationMode = mock(VerificationMode.class);
        when(verificationMode instanceof org.mockito.verification.AtMost).thenReturn(true);
        
        boolean result = verificationOverTime.canRecoverFromFailure(verificationMode);
        
        assertFalse(result);
    }
    
    @Test
    public void canRecoverFromFailure_shouldReturnFalseWhenVerificationModeIsNoMoreInteractions() {
        VerificationMode verificationMode = mock(VerificationMode.class);
        when(verificationMode instanceof org.mockito.verification.NoMoreInteractions).thenReturn(true);
        
        boolean result = verificationOverTime.canRecoverFromFailure(verificationMode);
        
        assertFalse(result);
    }
    
    @Test
    public void sleep_shouldCallThreadSleepWithPassedSleepParameter() throws Exception {
        long sleep = 2000;
        
        verificationOverTime.sleep(sleep);
        
        verify(Thread.class).sleep(sleep);
    }
    
    @Test(expected = InterruptedException.class)
    public void sleep_shouldThrowInterruptedExceptionWhenThreadSleepThrowsInterruptedException() throws Exception {
        long sleep = 2000;
        doThrow(InterruptedException.class).when(Thread.class);
        
        verificationOverTime.sleep(sleep);
    }
    
    @Test
    public void getPollingPeriod_shouldReturnPollingPeriodMillis() {
        long result = verificationOverTime.getPollingPeriod();
        
        assertEquals(pollingPeriodMillis, result);
    }
    
    @Test
    public void getDuration_shouldReturnDurationMillis() {
        long result = verificationOverTime.getDuration();
        
        assertEquals(durationMillis, result);
    }
    
    @Test
    public void getDelegate_shouldReturnDelegate() {
        VerificationMode result = verificationOverTime.getDelegate();
        
        assertEquals(delegate, result);
    }
}