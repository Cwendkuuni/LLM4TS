
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;
import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerify() {
        long pollingPeriodMillis = 1000;
        long durationMillis = 5000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);

        VerificationData data = mock(VerificationData.class);

        verificationOverTime.verify(data);

        verify(delegate).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_throwsAssertionError() {
        long pollingPeriodMillis = 1000;
        long durationMillis = 5000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);

        VerificationData data = mock(VerificationData.class);

        doThrow(MockitoAssertionError.class).when(delegate).verify(data);

        verificationOverTime.verify(data);
    }

    @Test(expected = org.mockito.exceptions.verification.junit.ArgumentsAreDifferent.class)
    public void testVerify_throwsArgumentsAreDifferent() {
        long pollingPeriodMillis = 1000;
        long durationMillis = 5000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);

        VerificationData data = mock(VerificationData.class);

        doThrow(org.mockito.exceptions.verification.junit.ArgumentsAreDifferent.class).when(delegate).verify(data);

        verificationOverTime.verify(data);
    }

    @Test
    public void testVerify_noExceptionThrown() {
        long pollingPeriodMillis = 1000;
        long durationMillis = 5000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);

        VerificationData data = mock(VerificationData.class);

        doNothing().when(delegate).verify(data);

        verificationOverTime.verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_delegateUnsatisfied_throwAssertionError() {
        long pollingPeriodMillis = 1000;
        long durationMillis = 5000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = false;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);

        VerificationData data = mock(VerificationData.class);

        doThrow(MockitoAssertionError.class).when(delegate).verify(data);

        verificationOverTime.verify(data);
    }

    @Test(expected = org.mockito.exceptions.verification.junit.ArgumentsAreDifferent.class)
    public void testVerify_delegateUnsatisfied_throwArgumentsAreDifferent() {
        long pollingPeriodMillis = 1000;
        long durationMillis = 5000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = false;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);

        VerificationData data = mock(VerificationData.class);

        doThrow(org.mockito.exceptions.verification.junit.ArgumentsAreDifferent.class).when(delegate).verify(data);

        verificationOverTime.verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_delegateUnsatisfied_noExceptionThrown_throwAssertionError() {
        long pollingPeriodMillis = 1000;
        long durationMillis = 5000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = false;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);

        VerificationData data = mock(VerificationData.class);

        doNothing().when(delegate).verify(data);

        verificationOverTime.verify(data);
    }
}

