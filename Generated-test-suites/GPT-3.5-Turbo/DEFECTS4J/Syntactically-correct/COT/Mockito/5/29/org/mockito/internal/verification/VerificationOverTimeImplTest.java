
import org.junit.Test;
import static org.mockito.Mockito.*;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerify() throws Exception {
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = false;
        Timer timer = mock(Timer.class);

        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);

        VerificationData data = mock(VerificationData.class);
        verification.verify(data);

        verify(timer).start();
        verify(delegate).verify(data);
    }

    @Test
    public void testVerify_withReturnOnSuccess() throws Exception {
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        Timer timer = mock(Timer.class);

        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);

        VerificationData data = mock(VerificationData.class);
        verification.verify(data);

        verify(timer).start();
        verify(delegate).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_withAssertionError() throws Exception {
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = false;
        Timer timer = mock(Timer.class);

        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);

        VerificationData data = mock(VerificationData.class);
        doThrow(MockitoAssertionError.class).when(delegate).verify(data);
        verification.verify(data);

        verify(timer).start();
        verify(delegate).verify(data);
    }

    @Test
    public void testVerify_canRecoverFromFailure() throws Exception {
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = false;
        Timer timer = mock(Timer.class);

        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);

        VerificationData data = mock(VerificationData.class);
        AssertionError error = new AssertionError();
        doReturn(true).when(verification).canRecoverFromFailure(delegate);
        doReturn(error).when(delegate).verify(data);
        verification.verify(data);

        verify(timer).start();
        verify(delegate).verify(data);
        verify(verification).canRecoverFromFailure(delegate);
    }

    @Test(expected = AssertionError.class)
    public void testVerify_cannotRecoverFromFailure() throws Exception {
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = false;
        Timer timer = mock(Timer.class);

        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);

        VerificationData data = mock(VerificationData.class);
        AssertionError error = new AssertionError();
        doReturn(false).when(verification).canRecoverFromFailure(delegate);
        doReturn(error).when(delegate).verify(data);
        verification.verify(data);

        verify(timer).start();
        verify(delegate).verify(data);
        verify(verification).canRecoverFromFailure(delegate);
    }

    @Test
    public void testSleep() throws Exception {
        long sleep = 1000;
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(0, 0, null, false);

        verification.sleep(sleep);

        // No assertion needed, as sleep method only has side effects
    }

    @Test
    public void testGetPollingPeriod() throws Exception {
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = false;
        Timer timer = mock(Timer.class);

        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);

        long actualPollingPeriod = verification.getPollingPeriod();

        assertEquals(pollingPeriodMillis, actualPollingPeriod);
    }

    @Test
    public void testGetDuration() throws Exception {
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = false;
        Timer timer = mock(Timer.class);

        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);

        long actualDuration = verification.getDuration();

        assertEquals(durationMillis, actualDuration);
    }

    @Test
    public void testGetDelegate() throws Exception {
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = false;
        Timer timer = mock(Timer.class);

        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);

        VerificationMode actualDelegate = verification.getDelegate();

        assertEquals(delegate, actualDelegate);
    }

}
