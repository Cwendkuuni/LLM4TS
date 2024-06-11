
package org.mockito.internal.verification;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

public class VerificationOverTimeImplTest {

    @Test
    public void verify_WhenDelegateSucceedsBeforeTimeoutAndReturnOnSuccessTrue_ExpectNoErrorThrown() {
        // Arrange
        long pollingPeriodMillis = 1000;
        long durationMillis = 5000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        Timer timer = mock(Timer.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis,
                durationMillis, delegate, returnOnSuccess, timer);
        VerificationData data = mock(VerificationData.class);
        
        // Act
        verificationOverTime.verify(data);
        
        // Assert
        verify(delegate).verify(data);
        verify(timer).start();
    }
    
    @Test(expected = MockitoAssertionError.class)
    public void verify_WhenDelegateFailsToSatisfyInGivenDuration_ExpectAssertionErrorThrown() {
        // Arrange
        long pollingPeriodMillis = 1000;
        long durationMillis = 5000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = false;
        Timer timer = mock(Timer.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis,
                durationMillis, delegate, returnOnSuccess, timer);
        VerificationData data = mock(VerificationData.class);
        
        // Act
        verificationOverTime.verify(data);
        
        // Assert
        verify(delegate, atLeastOnce()).verify(data);
        verify(timer).start();
    }
}

