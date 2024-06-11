import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {

    private long pollingPeriodMillis = 1000;
    private long durationMillis = 5000;
    private VerificationMode delegate = mock(VerificationMode.class);
    private boolean returnOnSuccess = true;
    private VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);

    @Test
    public void verify_ReturnOnSuccess_DelegateVerificationSuccess_ReturnsImmediately() {
        VerificationData data = mock(VerificationData.class);
        when(delegate.toString()).thenReturn("Delegate Verification");

        verificationOverTime.verify(data);

        verify(delegate).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void verify_ReturnOnSuccess_DelegateVerificationFailure_ThrowsAssertionError() {
        VerificationData data = mock(VerificationData.class);
        when(delegate.toString()).thenReturn("Delegate Verification");
        doThrow(new MockitoAssertionError("Delegate Verification Error")).when(delegate).verify(data);

        verificationOverTime.verify(data);
    }

}