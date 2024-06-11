import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerify_ReturnOnSuccess_True() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);

        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, delegate, true);
        verification.verify(data);

        verify(delegate).verify(data);
    }

    @Test
    public void testVerify_ReturnOnSuccess_False() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);

        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, delegate, false);
        verification.verify(data);

        verify(delegate).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_ThrowsMockitoAssertionError() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);

        doThrow(new MockitoAssertionError("Assertion Error")).when(delegate).verify(data);

        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, delegate, false);
        verification.verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_ThrowsArgumentsAreDifferent() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);

        doThrow(new org.mockito.exceptions.verification.junit.ArgumentsAreDifferent("Arguments Are Different")).when(delegate).verify(data);

        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, delegate, false);
        verification.verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_DelegateDoesNotSucceedBeforeTimeout() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);

        doThrow(new MockitoAssertionError("Delegate Assertion Error")).when(delegate).verify(data);

        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, delegate, false);
        verification.verify(data);
    }

}