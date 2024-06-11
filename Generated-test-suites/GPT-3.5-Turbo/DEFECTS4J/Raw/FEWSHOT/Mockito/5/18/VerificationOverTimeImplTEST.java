
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.verification.VerificationMode;
import org.junit.Before;
import org.junit.Test;

public class VerificationOverTimeImplTest {

    private static final long POLLING_PERIOD = 1000;
    private static final long DURATION = 5000;

    private VerificationOverTimeImpl verificationOverTime;
    private Timer timer;
    private VerificationMode delegate;

    @Before
    public void setUp() {
        timer = mock(Timer.class);
        delegate = mock(VerificationMode.class);
        verificationOverTime = new VerificationOverTimeImpl(POLLING_PERIOD, DURATION, delegate, true, timer);
    }

    @Test
    public void verifyDelegateCalledUntilSatisfied() {
        VerificationData data = mock(VerificationData.class);
        when(timer.isCounting()).thenReturn(true, true, true, false);
        when(data.getVerificationMode()).thenReturn(delegate);

        try {
            verificationOverTime.verify(data);
        } catch (MockitoAssertionError e) {
            fail("Delegate verification mode should have been satisfied.");
        }

        verify(delegate, times(3)).verify(data);
    }

    @Test
    public void returnOnSuccessTrue_delegateSatisfied_returnImmediately() {
        VerificationData data = mock(VerificationData.class);
        when(timer.isCounting()).thenReturn(true);
        when(data.getVerificationMode()).thenReturn(delegate);

        try {
            verificationOverTime.verify(data);
        } catch (MockitoAssertionError e) {
            fail("Delegate verification mode should have been satisfied.");
        }

        verify(delegate, times(1)).verify(data);
    }

    @Test
    public void returnOnSuccessFalse_delegateSatisfied_returnAfterDuration() {
        VerificationData data = mock(VerificationData.class);
        when(timer.isCounting()).thenReturn(true, true, true, true, true, false);
        when(data.getVerificationMode()).thenReturn(delegate);

        try {
            verificationOverTime.verify(data);
        } catch (MockitoAssertionError e) {
            fail("Delegate verification mode should have been satisfied.");
        }

        verify(delegate, times(5)).verify(data);
    }

    @Test
    public void throwAssertionErrorWhenDelegateFailsAndCannotRecover() {
        VerificationData data = mock(VerificationData.class);
        when(timer.isCounting()).thenReturn(true);
        when(data.getVerificationMode()).thenReturn(delegate);
        doThrow(MockitoAssertionError.class).when(delegate).verify(data);
        when(delegate instanceof AtMost).thenReturn(true);

        try {
            verificationOverTime.verify(data);
            fail("Should throw MockitoAssertionError");
        } catch (MockitoAssertionError e) {
            assertEquals("Should throw MockitoAssertionError", MockitoAssertionError.class, e.getClass());
        }

        verify(delegate, times(1)).verify(data);
    }

    @Test
    public void handleVerifyExceptionAndSleepWhenDelegateFailsAndCanRecover() {
        VerificationData data = mock(VerificationData.class);
        when(timer.isCounting()).thenReturn(true, true, true, false);
        when(data.getVerificationMode()).thenReturn(delegate);
        doThrow(MockitoAssertionError.class).when(delegate).verify(data);
        when(delegate instanceof AtMost).thenReturn(false);

        try {
            verificationOverTime.verify(data);
        } catch (MockitoAssertionError e) {
            fail("Delegate verification mode should have been recovered.");
        }

        verify(delegate, times(3)).verify(data);
        verify(timer, times(3)).isCounting();
    }

    @Test
    public void sleepsForSpecifiedPollingPeriod() {
        VerificationData data = mock(VerificationData.class);
        when(timer.isCounting()).thenReturn(true);
        when(data.getVerificationMode()).thenReturn(delegate);
        when(delegate instanceof AtMost).thenReturn(true, true, true, true, true, false);

        verificationOverTime.verify(data);

        verify(timer, times(5)).isCounting();
        verify(timer, atLeast(5)).start();
        verify(timer, atLeast(5)).getTimeElapsed();
        verify(timer, atLeast(5)).wait(POLLING_PERIOD);
    }

    @Test
    public void getPollingPeriod() {
        assertEquals("Polling period is incorrect", POLLING_PERIOD, verificationOverTime.getPollingPeriod());
    }

    @Test
    public void getDuration() {
        assertEquals("Duration is incorrect", DURATION, verificationOverTime.getDuration());
    }

    @Test
    public void getDelegate() {
        assertEquals("Delegate is incorrect", delegate, verificationOverTime.getDelegate());
    }
}

