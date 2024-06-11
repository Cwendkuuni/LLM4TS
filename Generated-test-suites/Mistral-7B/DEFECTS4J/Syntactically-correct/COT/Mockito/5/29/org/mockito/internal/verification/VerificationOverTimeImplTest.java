package org.mockito.internal.verification.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationModeWithTimeout;
import org.mockito.internal.verification.VerificationWithTimeout;

public class VerificationOverTimeImplTest {

    private Timer timer;
    private VerificationMode verificationMode;
    private VerificationModeWithTimeout verificationModeWithTimeout;
    private VerificationWithTimeout verificationWithTimeout;

    @Before
    public void setUp() {
        timer = mock(Timer.class);
        verificationMode = mock(VerificationMode.class);
        verificationModeWithTimeout = new VerificationModeWithTimeout(1L, 1L, verificationMode, true);
        verificationWithTimeout = new VerificationWithTimeout(1L, 1L, verificationMode, true);
    }

    @Test
    public void testVerifyWithDelegateSatisfiedImmediately() {
        VerificationData data = mock(VerificationData.class);
        when(verificationMode.verify(data)).thenReturn(null);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1L, 1L, verificationMode, true, timer);
        verificationOverTime.verify(data);

        verify(verificationMode).verify(data);
    }

    @Test
    public void testVerifyWithDelegateNotSatisfiedImmediately() {
        VerificationData data = mock(VerificationData.class);

        doThrow(new MockitoAssertionError("Delegate verification failed"))
                .when(verificationMode).verify(data);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1L, 1L, verificationMode, false, timer);
        verificationOverTime.verify(data);

        verify(verificationMode).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyWithDelegateNotSatisfiedAndTimeoutReached() {
        VerificationData data = mock(VerificationData.class);

        doThrow(new MockitoAssertionError("Delegate verification failed"))
                .when(verificationMode).verify(data);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1L, 2L, verificationMode, false, timer);
        verificationOverTime.verify(data);
    }

    @Test(expected = AssertionError.class)
    public void testVerifyWithDelegateNotSatisfiedAndPollingPeriodPassed() {
        VerificationData data = mock(VerificationData.class);

        ArgumentCaptor<AssertionError> errorCaptor = ArgumentCaptor.forClass(AssertionError.class);

        doThrow(new MockitoAssertionError("Delegate verification failed"))
                .when(verificationMode).verify(data);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1L, 1L, verificationMode, false, timer);
        verificationOverTime.verify(data);

        verify(timer).start();
        verify(timer).isCounting();
        verifyZeroInteractions(verificationMode);
        verify(timer).stop();

        verify(timer).start();
        verify(timer).isCounting();
        verify(verificationMode).verify(data);
        verify(timer).stop();

        verify(timer).start();
        verify(timer).isCounting();
        verify(errorCaptor.capture()).isInstanceOf(MockitoAssertionError.class);
        verifyZeroInteractions(verificationMode);
        verify(timer).stop();

        throw errorCaptor.getValue();
    }

    @Test
    public void testCanRecoverFromFailure() {
        VerificationMode verificationMode = mock(VerificationMode.class);
        assertFalse(verificationOverTimeImpl.canRecoverFromFailure(new VerificationWithTimeout(1L, 1L, verificationMode, true)));
        assertTrue(verificationOverTimeImpl.canRecoverFromFailure(verificationMode));
    }

    @Test(expected = InterruptedException.class)
    public void testSleep() throws InterruptedException {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1L, 1L, null, true, timer);
        verificationOverTime.sleep(1L);
    }

    @Test
    public void testGetPollingPeriod() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1L, 1L, null, true, timer);
        assertEquals(1L, verificationOverTime.getPollingPeriod());
    }

    @Test
    public void testGetDuration() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1L, 1L, null, true, timer);
        assertEquals(1L, verificationOverTime.getDuration());
    }

    @Test
    public void testGetDelegate() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1L, 1L, null, true, timer);
        assertEquals(null, verificationOverTime.getDelegate());
    }
}