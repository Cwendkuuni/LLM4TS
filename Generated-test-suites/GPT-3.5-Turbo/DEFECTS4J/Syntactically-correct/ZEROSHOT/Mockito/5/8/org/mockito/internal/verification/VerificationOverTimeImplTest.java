import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.verification.VerificationMode;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationOverTimeImpl;

public class VerificationOverTimeImplTest {

    private VerificationOverTimeImpl verificationOverTimeImpl;
    private long pollingPeriodMillis;
    private long durationMillis;
    private VerificationMode delegate;
    private boolean returnOnSuccess;
    private Timer timer;

    @Before
    public void setup() {
        pollingPeriodMillis = 100;
        durationMillis = 1000;
        delegate = mock(VerificationMode.class);
        returnOnSuccess = true;
        timer = mock(Timer.class);
        verificationOverTimeImpl = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
    }

    @Test
    public void testVerify_Success() {
        VerificationData data = mock(VerificationData.class);
        when(timer.isCounting()).thenReturn(true, false);
        doNothing().when(delegate).verify(data);

        verificationOverTimeImpl.verify(data);

        verify(delegate).verify(data);
        verify(timer).start();
        verify(timer, times(2)).isCounting();
    }

    @Test
    public void testVerify_Success_ReturnOnSuccessFalse() {
        returnOnSuccess = false;
        verificationOverTimeImpl = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);

        VerificationData data = mock(VerificationData.class);
        when(timer.isCounting()).thenReturn(true, false);
        doNothing().when(delegate).verify(data);

        verificationOverTimeImpl.verify(data);

        verify(delegate).verify(data);
        verify(timer).start();
        verify(timer, times(2)).isCounting();
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_Timeout() {
        VerificationData data = mock(VerificationData.class);
        when(timer.isCounting()).thenReturn(true, true, false);
        doNothing().when(delegate).verify(data);

        verificationOverTimeImpl.verify(data);

        verify(delegate, times(2)).verify(data);
        verify(timer).start();
        verify(timer, times(3)).isCounting();
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_Timeout_ReturnOnSuccessFalse() {
        returnOnSuccess = false;
        verificationOverTimeImpl = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);

        VerificationData data = mock(VerificationData.class);
        when(timer.isCounting()).thenReturn(true, true, false);
        doNothing().when(delegate).verify(data);

        verificationOverTimeImpl.verify(data);

        verify(delegate, times(2)).verify(data);
        verify(timer).start();
        verify(timer, times(3)).isCounting();
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_Failure() {
        VerificationData data = mock(VerificationData.class);
        when(timer.isCounting()).thenReturn(true, false);
        doThrow(MockitoAssertionError.class).when(delegate).verify(data);

        verificationOverTimeImpl.verify(data);

        verify(delegate).verify(data);
        verify(timer).start();
        verify(timer, times(2)).isCounting();
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_Failure_ReturnOnSuccessFalse() {
        returnOnSuccess = false;
        verificationOverTimeImpl = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);

        VerificationData data = mock(VerificationData.class);
        when(timer.isCounting()).thenReturn(true, false);
        doThrow(MockitoAssertionError.class).when(delegate).verify(data);

        verificationOverTimeImpl.verify(data);

        verify(delegate).verify(data);
        verify(timer).start();
        verify(timer, times(2)).isCounting();
    }
}