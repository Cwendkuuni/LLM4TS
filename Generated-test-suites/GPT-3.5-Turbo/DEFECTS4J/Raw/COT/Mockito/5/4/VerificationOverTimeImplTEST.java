
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {

    private VerificationOverTimeImpl verificationOverTimeImpl;

    @Test
    public void testVerify_withoutError() throws MockitoAssertionError {
        VerificationData verificationData = mock(VerificationData.class);
        VerificationMode delegate = mock(VerificationMode.class);

        verificationOverTimeImpl = new VerificationOverTimeImpl(1000, 2000, delegate, true, new Timer(2000));
        verificationOverTimeImpl.verify(verificationData);

        verify(delegate).verify(verificationData);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_withError() throws MockitoAssertionError {
        VerificationData verificationData = mock(VerificationData.class);
        VerificationMode delegate = mock(VerificationMode.class);

        doThrow(new MockitoAssertionError("Assertion Error")).when(delegate).verify(verificationData);

        verificationOverTimeImpl = new VerificationOverTimeImpl(1000, 2000, delegate, true, new Timer(2000));
        verificationOverTimeImpl.verify(verificationData);
    }

    @Test
    public void testVerify_withRecoverableError() throws MockitoAssertionError {
        VerificationData verificationData = mock(VerificationData.class);
        VerificationMode delegate = mock(VerificationMode.class);

        doThrow(new MockitoAssertionError("Assertion Error")).when(delegate).verify(verificationData);

        verificationOverTimeImpl = new VerificationOverTimeImpl(1000, 2000, delegate, false, new Timer(2000));
        verificationOverTimeImpl.verify(verificationData);

        verify(delegate, times(2)).verify(verificationData);
    }
}
