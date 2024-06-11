
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerify_Success() {
        // Setup
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, delegate, true);
        VerificationData data = mock(VerificationData.class);

        // Exercise
        verification.verify(data);

        // Verify
        verify(delegate).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_Failure() {
        // Setup
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate.verify(any())).thenThrow(new MockitoAssertionError("Delegate failed"));
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, delegate, true);
        VerificationData data = mock(VerificationData.class);

        // Exercise
        verification.verify(data);
    }

    @Test
    public void testVerify_SuccessWithDelay() {
        // Setup
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, delegate, false);
        VerificationData data = mock(VerificationData.class);

        // Exercise
        verification.verify(data);

        // Verify
        verify(delegate).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_FailureWithDelay() {
        // Setup
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate.verify(any())).thenThrow(new MockitoAssertionError("Delegate failed"));
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, delegate, false);
        VerificationData data = mock(VerificationData.class);

        // Exercise
        verification.verify(data);
    }

    @Test
    public void testVerify_CanRecoverFromFailure() {
        // Setup
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate instanceof VerificationOverTimeImpl).thenReturn(true);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, delegate, false);
        VerificationData data = mock(VerificationData.class);

        // Exercise
        verification.verify(data);

        // Verify
        verify(delegate).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_CannotRecoverFromFailure() {
        // Setup
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate instanceof VerificationOverTimeImpl).thenReturn(false);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, delegate, false);
        VerificationData data = mock(VerificationData.class);

        // Exercise
        verification.verify(data);
    }

}
