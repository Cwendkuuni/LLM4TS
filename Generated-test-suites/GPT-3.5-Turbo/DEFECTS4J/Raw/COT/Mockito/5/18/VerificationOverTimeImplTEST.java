
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {

    @Test
    public void verify_callsDelegateVerifyMethod() {
        // Setup
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        VerificationOverTimeImpl verifier = new VerificationOverTimeImpl(100, 500, delegate, true);

        // Verify
        verifier.verify(data);

        // Verify that the delegate.verify() method is called once
        verify(delegate, times(1)).verify(data);
    }

    @Test
    public void verify_immediatelyReturnsOnSuccess() {
        // Setup
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        when(delegate.verify(data)).thenReturn(null);
        VerificationOverTimeImpl verifier = new VerificationOverTimeImpl(100, 500, delegate, true);

        // Verify
        verifier.verify(data);

        // Verify that the verifier immediately returns on success
        verify(delegate, times(1)).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void verify_throwsExceptionWhenDelegateFailsToSatisfy() {
        // Setup
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        when(delegate.verify(data)).thenThrow(MockitoAssertionError.class);
        VerificationOverTimeImpl verifier = new VerificationOverTimeImpl(100, 500, delegate, true);

        // Verify
        verifier.verify(data);
    }

    @Test
    public void verify_retriesWhenDelegateFailsToSatisfyAndCanRecover() {
        // Setup
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        MockitoAssertionError assertionError = mock(MockitoAssertionError.class);
        when(delegate.verify(data)).thenThrow(assertionError);
        VerificationOverTimeImpl verifier = new VerificationOverTimeImpl(100, 500, delegate, true);

        // Verify
        verifier.verify(data);

        // Verify that the verifier retries when the delegate fails to satisfy and can recover
        verify(delegate, times(5)).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void verify_throwsExceptionWhenDelegateFailsToSatisfyAndCannotRecover() {
        // Setup
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        MockitoAssertionError assertionError = mock(MockitoAssertionError.class);
        when(delegate.verify(data)).thenThrow(assertionError);
        VerificationOverTimeImpl verifier = new VerificationOverTimeImpl(100, 500, delegate, false);

        // Verify
        verifier.verify(data);
    }

    @Test
    public void verify_stopsRetryingWhenTimerExpires() {
        // Setup
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        MockitoAssertionError assertionError = mock(MockitoAssertionError.class);
        when(delegate.verify(data)).thenThrow(assertionError);
        Timer timer = mock(Timer.class);
        when(timer.isCounting()).thenReturn(true, true, true, false);
        VerificationOverTimeImpl verifier = new VerificationOverTimeImpl(100, 500, delegate, false, timer);

        // Verify
        verifier.verify(data);

        // Verify that the verifier stops retrying when the timer expires
        verify(delegate, times(3)).verify(data);
    }

    @Test
    public void getPollingPeriod_returnsPollingPeriodMillis() {
        // Setup
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verifier = new VerificationOverTimeImpl(100, 500, delegate, true);

        // Verify and assert
        assert verifier.getPollingPeriod() == 100;
    }

    @Test
    public void getDuration_returnsDurationMillis() {
        // Setup
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verifier = new VerificationOverTimeImpl(100, 500, delegate, true);

        // Verify and assert
        assert verifier.getDuration() == 500;
    }

    @Test
    public void getDelegate_returnsDelegate() {
        // Setup
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verifier = new VerificationOverTimeImpl(100, 500, delegate, true);

        // Verify and assert
        assert verifier.getDelegate().equals(delegate);
    }
}
