
package org.mockito.internal.verification;

import static org.mockito.Mockito.*;

import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.junit.Before;
import org.junit.Test;

public class VerificationOverTimeImplTest {

    private long pollingPeriodMillis;
    private long durationMillis;
    private VerificationMode delegate;
    private boolean returnOnSuccess;
    private Timer timer;

    @Before
    public void setUp() {
        pollingPeriodMillis = 1000;
        durationMillis = 5000;
        delegate = mock(VerificationMode.class);
        returnOnSuccess = true;
        timer = mock(Timer.class);
    }

    @Test
    public void testVerify_Successful() {
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
        VerificationData data = mock(VerificationData.class);

        doNothing().when(timer).start();
        when(timer.isCounting()).thenReturn(true, true, false);
        doNothing().when(delegate).verify(data);

        verification.verify(data);

        verify(delegate, times(2)).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_Unsuccessful() {
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
        VerificationData data = mock(VerificationData.class);

        doNothing().when(timer).start();
        when(timer.isCounting()).thenReturn(true, true, true, true, true, false);
        doThrow(new MockitoAssertionError()).when(delegate).verify(data);

        verification.verify(data);
    }

    @Test
    public void testVerify_Unsuccessful_CanRecoverFromFailure() {
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
        VerificationData data = mock(VerificationData.class);
        VerificationMode atMostVerificationMode = mock(VerificationMode.class);

        doNothing().when(timer).start();
        when(timer.isCounting()).thenReturn(true, true, false);
        doThrow(new ArgumentsAreDifferent("", "")).when(delegate).verify(data);
        when(delegate instanceof AtMost || delegate instanceof NoMoreInteractions).thenReturn(false);
        when(atMostVerificationMode instanceof AtMost || atMostVerificationMode instanceof NoMoreInteractions).thenReturn(true);
        when(delegate.canRecoverFromFailure(atMostVerificationMode)).thenReturn(false);

        try {
            verification.verify(data);
        } catch (MockitoAssertionError e) {
            // Verify that proper exception was thrown
        }

        verify(delegate, times(2)).verify(data);
        verify(atMostVerificationMode).canRecoverFromFailure(delegate);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_Unsuccessful_CannotRecoverFromFailure() {
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
        VerificationData data = mock(VerificationData.class);
        VerificationMode atMostVerificationMode = mock(VerificationMode.class);

        doNothing().when(timer).start();
        when(timer.isCounting()).thenReturn(true, true, false);
        doThrow(new ArgumentsAreDifferent("", "")).when(delegate).verify(data);
        when(delegate instanceof AtMost || delegate instanceof NoMoreInteractions).thenReturn(false);
        when(atMostVerificationMode instanceof AtMost || atMostVerificationMode instanceof NoMoreInteractions).thenReturn(true);
        when(delegate.canRecoverFromFailure(atMostVerificationMode)).thenReturn(true);

        verification.verify(data);
    }

    @Test(expected = InterruptedException.class)
    public void testSleep_Interrupted() throws InterruptedException {
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
        long sleep = 100;

        doThrow(new InterruptedException()).when(Thread.class, "sleep", sleep);

        verification.sleep(sleep);
    }

}
