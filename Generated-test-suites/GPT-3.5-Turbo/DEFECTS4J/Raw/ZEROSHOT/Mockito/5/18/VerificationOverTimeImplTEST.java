import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerify_ReturnOnSuccess() {
        // Setup
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);
        VerificationData data = mock(VerificationData.class);

        // Verify
        verificationOverTime.verify(data);

        // Validate
        verify(delegate).verify(data);
    }

    @Test
    public void testVerify_NoReturnOnSuccess() {
        // Setup
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = false;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);
        VerificationData data = mock(VerificationData.class);

        // Verify
        verificationOverTime.verify(data);

        // Validate
        verify(delegate).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_DelegateFailsBeforeTimeout() {
        // Setup
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);
        VerificationData data = mock(VerificationData.class);
        doThrow(new MockitoAssertionError("Delegate verification failed")).when(delegate).verify(data);

        // Verify
        verificationOverTime.verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_DelegateDoesNotRecoverFromFailure() {
        // Setup
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);
        VerificationData data = mock(VerificationData.class);
        doThrow(new AtMost(1, null)).when(delegate).verify(data);

        // Verify
        verificationOverTime.verify(data);
    }

    @Test
    public void testHandleVerifyException_RecoverFromFailure() {
        // Setup
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);
        AssertionError e = new AssertionError("Verification failed");

        // Verify
        AssertionError result = verificationOverTime.handleVerifyException(e);

        // Validate
        verify(delegate, never()).verify(any(VerificationData.class));
        assertEquals(e, result);
    }

    @Test(expected = AssertionError.class)
    public void testHandleVerifyException_CannotRecoverFromFailure() {
        // Setup
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);
        MockitoAssertionError e = new MockitoAssertionError("Verification failed");

        // Verify
        verificationOverTime.handleVerifyException(e);
    }
}