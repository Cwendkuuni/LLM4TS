
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {

    private VerificationOverTimeImpl verificationOverTimeImpl;

    @Before
    public void setUp() {
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        Timer timer = mock(Timer.class);
        verificationOverTimeImpl = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
    }

    @Test
    public void shouldReturnImmediatelyWhenDelegateIsSatisfied() {
        VerificationData data = mock(VerificationData.class);

        doNothing().when(verificationOverTimeImpl.timer).start();
        when(verificationOverTimeImpl.timer.isCounting()).thenReturn(true, false);
        doNothing().when(verificationOverTimeImpl.delegate).verify(data);

        verificationOverTimeImpl.verify(data);

        verify(verificationOverTimeImpl.delegate, times(1)).verify(data);
        verify(verificationOverTimeImpl.timer, times(2)).isCounting();
    }

    @Test(expected = MockitoAssertionError.class)
    public void shouldThrowErrorWhenDelegateIsNotSatisfiedBeforeTimeout() {
        VerificationData data = mock(VerificationData.class);
        MockitoAssertionError assertionError = mock(MockitoAssertionError.class);

        doNothing().when(verificationOverTimeImpl.timer).start();
        when(verificationOverTimeImpl.timer.isCounting()).thenReturn(true, true);
        doThrow(assertionError).when(verificationOverTimeImpl.delegate).verify(data);

        verificationOverTimeImpl.verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void shouldThrowErrorWhenDelegateThrowsMockitoAssertionError() {
        VerificationData data = mock(VerificationData.class);
        MockitoAssertionError assertionError = mock(MockitoAssertionError.class);

        doNothing().when(verificationOverTimeImpl.timer).start();
        when(verificationOverTimeImpl.timer.isCounting()).thenReturn(true, false);
        doThrow(assertionError).when(verificationOverTimeImpl.delegate).verify(data);

        verificationOverTimeImpl.verify(data);
    }

    @Test(expected = org.mockito.exceptions.verification.junit.ArgumentsAreDifferent.class)
    public void shouldThrowErrorWhenDelegateThrowsArgumentsAreDifferent() {
        VerificationData data = mock(VerificationData.class);
        org.mockito.exceptions.verification.junit.ArgumentsAreDifferent argumentsAreDifferent = mock(org.mockito.exceptions.verification.junit.ArgumentsAreDifferent.class);

        doNothing().when(verificationOverTimeImpl.timer).start();
        when(verificationOverTimeImpl.timer.isCounting()).thenReturn(true, false);
        doThrow(argumentsAreDifferent).when(verificationOverTimeImpl.delegate).verify(data);

        verificationOverTimeImpl.verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void shouldThrowErrorWhenDelegateIsNotSatisfiedAndCannotRecover() {
        VerificationData data = mock(VerificationData.class);
        MockitoAssertionError assertionError = mock(MockitoAssertionError.class);

        doNothing().when(verificationOverTimeImpl.timer).start();
        when(verificationOverTimeImpl.timer.isCounting()).thenReturn(true, true);
        doThrow(assertionError).when(verificationOverTimeImpl.delegate).verify(data);

        verificationOverTimeImpl.verify(data);
    }

    @Test
    public void shouldSleepWhenDelegateFailsAndCanRecover() {
        VerificationData data = mock(VerificationData.class);
        MockitoAssertionError assertionError = mock(MockitoAssertionError.class);

        doNothing().when(verificationOverTimeImpl.timer).start();
        when(verificationOverTimeImpl.timer.isCounting()).thenReturn(true, true, false);
        doThrow(assertionError).when(verificationOverTimeImpl.delegate).verify(data);

        verificationOverTimeImpl.verify(data);

        verify(verificationOverTimeImpl.delegate, times(2)).verify(data);
        verify(verificationOverTimeImpl.timer, times(3)).isCounting();
        verify(verificationOverTimeImpl, times(1)).sleep(anyLong());
    }

    @Test
    public void shouldSleepWhenDelegateFailsWithArgumentsAreDifferent() {
        VerificationData data = mock(VerificationData.class);
        org.mockito.exceptions.verification.junit.ArgumentsAreDifferent argumentsAreDifferent = mock(org.mockito.exceptions.verification.junit.ArgumentsAreDifferent.class);

        doNothing().when(verificationOverTimeImpl.timer).start();
        when(verificationOverTimeImpl.timer.isCounting()).thenReturn(true, false);
        doThrow(argumentsAreDifferent).when(verificationOverTimeImpl.delegate).verify(data);

        verificationOverTimeImpl.verify(data);

        verify(verificationOverTimeImpl.delegate, times(1)).verify(data);
        verify(verificationOverTimeImpl.timer, times(2)).isCounting();
        verify(verificationOverTimeImpl, times(1)).sleep(anyLong());
    }
}
