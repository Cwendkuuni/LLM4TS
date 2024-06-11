
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

public class VerificationOverTimeImplTest {

    private VerificationOverTimeImpl verification;

    @Test
    public void verify_callsDelegateVerify() {
        VerificationData data = mock(VerificationData.class);
        VerificationMode delegate = mock(VerificationMode.class);
        verification = new VerificationOverTimeImpl(100, 1000, delegate, true);
        
        verification.verify(data);

        verify(delegate).verify(data);
    }

    @Test
    public void verify_returnsImmediatelyOnSuccess() {
        VerificationData data = mock(VerificationData.class);
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate.toString()).thenReturn("Delegate VerificationMode");
        verification = new VerificationOverTimeImpl(100, 1000, delegate, true);

        verification.verify(data);

        // Verify that the delegate is called and immediately returns
        verify(delegate).verify(data);
        verify(data).reportVerified(delegate);

        // Verify that the toString() method is called on the delegate during the reportVerified() call
        verify(delegate).toString();
    }

    @Test
    public void verify_throwsErrorIfDelegateNotSatisfiedBeforeTimeout() {
        VerificationData data = mock(VerificationData.class);
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate.toString()).thenReturn("Delegate VerificationMode");
        MockitoAssertionError assertionError = new MockitoAssertionError("Delegate VerificationMode failed");
        when(delegate.verify(data)).thenThrow(assertionError);
        verification = new VerificationOverTimeImpl(100, 1000, delegate, false);

        try {
            verification.verify(data);
            fail("Expected MockitoAssertionError to be thrown");
        } catch (MockitoAssertionError e) {
            assertEquals(assertionError, e);
        }

        // Verify that the delegate is called multiple times with the given polling period
        verify(delegate).verify(data);
        verify(delegate).verify(data);
        verify(delegate).verify(data);

        // Verify that the toString() method is called on the delegate for the error message
        verify(delegate).toString();
    }
}