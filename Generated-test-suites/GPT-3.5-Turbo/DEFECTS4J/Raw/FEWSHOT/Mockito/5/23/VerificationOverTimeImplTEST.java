
package org.mockito.internal.verification;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerify_ReturnOnSuccess() {
        // Given
        long pollingPeriodMillis = 100;
        long durationMillis = 500;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        Timer timer = mock(Timer.class);
        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
        VerificationData data = mock(VerificationData.class);
        
        // When
        verificationOverTimeImpl.verify(data);
        
        // Then
        verify(delegate).verify(data);
        verify(timer).start();
        verify(timer).isCounting();
        verify(timer, times(0)).stop();
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_NoReturnOnSuccess() {
        // Given
        long pollingPeriodMillis = 100;
        long durationMillis = 500;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = false;
        Timer timer = mock(Timer.class);
        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
        VerificationData data = mock(VerificationData.class);
        
        // When
        verificationOverTimeImpl.verify(data);
        
        // Then
        verify(delegate).verify(data);
        verify(timer).start();
        verify(timer, times(2)).isCounting();
        verify(timer).stop();
    }

    @Test
    public void testHandleVerifyException_CanRecoverFromFailure() {
        // Given
        VerificationMode verificationMode = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(0, 0, verificationMode, true);
        AssertionError e = mock(AssertionError.class);
        
        // When
        verificationOverTimeImpl.handleVerifyException(e);
        
        // Then
        verify(verificationMode).canRecoverFromFailure(verificationMode);
        verify(Thread.currentThread()).sleep(0);
        verify(e, times(0)).getMessage();
    }

    @Test(expected = AssertionError.class)
    public void testHandleVerifyException_CannotRecoverFromFailure() {
        // Given
        VerificationMode verificationMode = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(0, 0, verificationMode, true);
        AtMost atMost = mock(AtMost.class);
        AssertionError e = mock(AssertionError.class);
        when(verificationMode instanceof AtMost).thenReturn(true);
        
        // When
        verificationOverTimeImpl.handleVerifyException(e);
        
        // Then
        verify(verificationMode).canRecoverFromFailure(verificationMode);
        verify(Thread.currentThread(), times(0)).sleep(0);
        verify(e, times(0)).getMessage();
    }
}
