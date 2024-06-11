import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;
import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {

    private VerificationOverTimeImpl verificationOverTime;

    @Before
    public void setUp() {
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        Timer timer = mock(Timer.class);
        
        verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
    }

    @Test
    public void verify_Success_ReturnImmediately() {
        VerificationData data = mock(VerificationData.class);

        when(verificationOverTime.getDelegate().verify(data)).thenReturn();
        
        verificationOverTime.verify(data);

        verify(verificationOverTime.getDelegate(), times(1)).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void verify_Failure_ThrowsAssertionError() {
        VerificationData data = mock(VerificationData.class);

        doThrow(new MockitoAssertionError("Verification failed")).when(verificationOverTime.getDelegate()).verify(data);
        
        verificationOverTime.verify(data);

        verify(verificationOverTime.getDelegate(), times(1)).verify(data);
    }

    @Test(expected = ArgumentsAreDifferent.class)
    public void verify_Failure_ThrowsArgumentsAreDifferent() {
        VerificationData data = mock(VerificationData.class);

        doThrow(new ArgumentsAreDifferent("Arguments are different")).when(verificationOverTime.getDelegate()).verify(data);
        
        verificationOverTime.verify(data);

        verify(verificationOverTime.getDelegate(), times(1)).verify(data);
    }

}