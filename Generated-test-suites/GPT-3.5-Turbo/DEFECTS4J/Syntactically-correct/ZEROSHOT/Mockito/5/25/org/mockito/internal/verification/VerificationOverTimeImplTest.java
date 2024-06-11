import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerifyWhenDelegateSatisfiedBeforeDuration() {
        long pollingPeriodMillis = 1000L;
        long durationMillis = 5000L;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;

        VerificationData data = mock(VerificationData.class);
        when(data.getWanted()).thenReturn(mock(VerificationMode.class));

        Timer timer = mock(Timer.class);
        when(timer.isCounting()).thenReturn(true, true, true, false);

        VerificationOverTimeImpl v = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
        v.verify(data);

        verify(delegate).verify(data);
        verify(data, times(1)).getWanted();
        verify(timer, times(4)).isCounting();
        verifyNoMoreInteractions(delegate, data, timer);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyWhenDelegateNotSatisfiedWithinDuration() {
        long pollingPeriodMillis = 1000L;
        long durationMillis = 5000L;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = false;

        VerificationData data = mock(VerificationData.class);
        when(data.getWanted()).thenReturn(mock(VerificationMode.class));

        Timer timer = mock(Timer.class);
        when(timer.isCounting()).thenReturn(true, true, true, true, true, false);

        VerificationOverTimeImpl v = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
        v.verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyWhenDelegateThrowsAssertionError() {
        long pollingPeriodMillis = 1000L;
        long durationMillis = 5000L;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = false;

        VerificationData data = mock(VerificationData.class);
        when(data.getWanted()).thenReturn(mock(VerificationMode.class));

        Timer timer = mock(Timer.class);
        when(timer.isCounting()).thenReturn(true, true, true, false);

        doThrow(new MockitoAssertionError("Assertion error")).when(delegate).verify(data);

        VerificationOverTimeImpl v = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
        v.verify(data);
    }

    @Test(expected = ArgumentsAreDifferent.class)
    public void testVerifyWhenDelegateThrowsArgumentsAreDifferent() {
        long pollingPeriodMillis = 1000L;
        long durationMillis = 5000L;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = false;

        VerificationData data = mock(VerificationData.class);
        when(data.getWanted()).thenReturn(mock(VerificationMode.class));

        Timer timer = mock(Timer.class);
        when(timer.isCounting()).thenReturn(true, true, true, false);

        doThrow(new ArgumentsAreDifferent("Arguments are different")).when(delegate).verify(data);

        VerificationOverTimeImpl v = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
        v.verify(data);
    }
}