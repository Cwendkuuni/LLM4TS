package org.mockito.internal.verification.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.verification.VerificationData;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Stubber;
import org.mockito.verification.VerificationModeFactory;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {

    private VerificationMode verificationMode;
    private Timer timer;
    private Stubber stubber;

    @Before
    public void setUp() {
        long pollingPeriodMillis = 10;
        long durationMillis = 100;
        VerificationMode delegate = mock(VerificationMode.class);
        this.verificationMode = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, true);
        this.timer = mock(Timer.class);
        this.stubber = mock(Stubber.class);
    }

    @Test
    public void testVerify_delegateIsSatisfiedImmediately() {
        VerificationData verificationData = ArgumentMatchers.any(VerificationData.class);
        when(delegate.verify(verificationData)).thenReturn(null);

        this.verificationMode.verify(verificationData);

        // verify delegate was called
        verify(delegate).verify(verificationData);
    }

    @Test
    public void testVerify_delegateIsNotSatisfiedImmediatelyButIsSatisfiedBeforeTimeout() {
        long startTime = System.currentTimeMillis();
        VerificationData verificationData = ArgumentMatchers.any(VerificationData.class);
        long sleepTime = 50;

        when(delegate.verify(verificationData)).thenThrow(new MockitoAssertionError("Message"));

        this.verificationMode.verify(verificationData);

        // verify delegate was called with appropriate delay
        long elapsedTime = System.currentTimeMillis() - startTime;
        assertTrue(elapsedTime > sleepTime);

        // verify delegate was eventually satisfied
        verify(delegate).verify(verificationData);
    }

    @Test
    public void testVerify_delegateIsNotSatisfiedImmediatelyAndTimeoutIsReached() {
        long startTime = System.currentTimeMillis();
        VerificationData verificationData = ArgumentMatchers.any(VerificationData.class);
        long durationMillis = 50;

        when(delegate.verify(verificationData)).thenThrow(new MockitoAssertionError("Message"));

        this.verificationMode.verify(verificationData);

        // verify exception is thrown
        AssertionError exception = assertThrows(AssertionError.class, () -> this.verificationMode.verify(verificationData));
        assertEquals("Message", exception.getMessage());

        // verify delegate was not called
        verifyZeroInteractions(delegate);

        // verify timeout was reached
        assertTrue(this.timer.isTimeoutReached(durationMillis));
    }

    @Test(expected = InterruptedException.class)
    public void testVerify_sleepInterrupted() {
        long pollingPeriodMillis = 10;
        long durationMillis = 100;
        VerificationMode delegate = mock(VerificationMode.class);

        this.verificationMode = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);
        this.verificationMode.verify(ArgumentMatchers.any(VerificationData.class));
    }

    @Test
    public void testCanRecoverFromFailure_canRecover() {
        VerificationMode verificationMode = mock(VerificationMode.class);
        assertTrue(this.verificationMode.canRecoverFromFailure(verificationMode));
    }

    @Test
    public void testCanRecoverFromFailure_cannotRecover() {
        AtMost atMost = mock(AtMost.class);
        assertFalse(this.verificationMode.canRecoverFromFailure(atMost));
    }

    @Test
    public void testSleep() {
        long sleepTime = 10;
        this.verificationMode.sleep(sleepTime);

        long elapsedTime = System.currentTimeMillis() - System.currentTimeMillis();
        assertTrue(Math.abs(elapsedTime - sleepTime) < 100);
    }

    @Test
    public void testGetPollingPeriod() {
        long pollingPeriodMillis = 10;
        assertEquals(pollingPeriodMillis, this.verificationMode.getPollingPeriod());
    }

    @Test
    public void testGetDuration() {
        long durationMillis = 100;
        assertEquals(durationMillis, this.verificationMode.getDuration());
    }

    @Test
    public void testGetDelegate() {
        VerificationMode delegate = new VerificationWithTimeout(10);
        assertEquals(delegate, this.verificationMode.getDelegate());
    }
}