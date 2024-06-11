import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerify_ReturnOnSuccess() {
        long pollingPeriod = 100;
        long duration = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(pollingPeriod, duration, delegate, true);

        VerificationData data = mock(VerificationData.class);

        // Behavior for the delegate.verify() method
        doThrow(new MockitoAssertionError()).doNothing().when(delegate).verify(data);

        try {
            verification.verify(data);
        } catch (MockitoAssertionError e) {
            fail("Should not throw exception");
        }
        
        // Verify that delegate.verify() was called twice
        verify(delegate, times(2)).verify(data);
    }

    @Test
    public void testVerify_NoReturnOnSuccess() {
        long pollingPeriod = 100;
        long duration = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(pollingPeriod, duration, delegate, false);

        VerificationData data = mock(VerificationData.class);

        // Behavior for the delegate.verify() method
        doThrow(new MockitoAssertionError()).doNothing().when(delegate).verify(data);

        try {
            verification.verify(data);
        } catch (MockitoAssertionError e) {
            fail("Should not throw exception");
        }
        
        // Verify that delegate.verify() was called twice
        verify(delegate, times(2)).verify(data);
    }
}