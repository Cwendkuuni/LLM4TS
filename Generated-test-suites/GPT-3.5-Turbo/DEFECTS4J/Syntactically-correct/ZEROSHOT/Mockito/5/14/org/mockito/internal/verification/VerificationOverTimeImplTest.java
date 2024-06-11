import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {
    private VerificationOverTimeImpl verificationOverTime;

    @Before
    public void setUp() {
        verificationOverTime = new VerificationOverTimeImpl(100, 1000, mock(VerificationMode.class), true);
    }

    @Test
    public void testVerify_SuccessNoError() {
        VerificationData verificationData = mock(VerificationData.class);

        verify(verificationData, times(2)).verify();

        verificationOverTime.verify(verificationData);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_DelegateThrowsAssertionError() {
        VerificationData verificationData = mock(VerificationData.class);

        doThrow(new MockitoAssertionError("Delegate verification failed")).when(verificationData).verify();

        verificationOverTime.verify(verificationData);
    }

    @Test(expected = ArgumentsAreDifferent.class)
    public void testVerify_DelegateThrowsArgumentsAreDifferent() {
        VerificationData verificationData = mock(VerificationData.class);

        doThrow(new ArgumentsAreDifferent("Arguments are different")).when(verificationData).verify();

        verificationOverTime.verify(verificationData);
    }
}