package org.mockito.internal.verification;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.verification.VerificationModeFactory;

public class VerificationOverTimeImplTest {

    private VerificationMode delegate;
    private Timer timer;
    private VerificationMode verificationMode;

    @Before
    public void setup() {
        delegate = mock(VerificationMode.class);
        timer = mock(Timer.class);
        verificationMode = new VerificationOverTimeImpl(100L, 500L, delegate, false, timer);
    }

    @Test
    public void testVerifySuccessWithinDuration() {
        VerificationData data = mock(VerificationData.class);
        verificationMode.verify(data);
        verify(delegate).verify(data);
    }

    @Test
    public void testVerifySuccessImmediately() {
        VerificationData data = mock(VerificationData.class);
        verificationMode.verify(data);
        assertTrue(verificationMode.isSatisfied());
    }

    @Test
    public void testVerifyFailWithinDuration() {
        VerificationData data = mock(VerificationData.class);
        verify(delegate, never()).verify(data);

        AssertionError expectedException = new MockitoAssertionError("Expected: 0 times, but was 0 times.");
        assertThrows(expectedException.getClass(), () -> verificationMode.verify(data));

        verify(delegate).verify(data);
    }

    @Test
    public void testVerifyFailEventually() {
        VerificationData data = mock(VerificationData.class);
        delegate.verify(data);

        AssertionError expectedException = new MockitoAssertionError("Expected: 1 time, but was never invoked.");
        assertThrows(expectedException.getClass(), () -> verificationMode.verify(data));

        verify(delegate).verify(data);
    }

    @Test
    public void testVerifyFailEarly() {
        VerificationData data = mock(VerificationData.class);
        delegate.verify(data);
        delegate.verify(data);

        AssertionError expectedException = new MockitoAssertionError("Expected: 2 times, but was 1 time.");
        assertThrows(expectedException.getClass(), () -> verificationMode.verify(data));

        verify(delegate, times(2)).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyTimeout() {
        VerificationData data = mock(VerificationData.class);
        delegate.verify(data);

        timer.stop();

        AssertionError expectedException = new MockitoAssertionError("Timeout is over: 500 millis elapsed.");
        verificationMode.verify(data);
    }

    @Test(expected = InterruptedException.class)
    public void testVerifyInterrupted() {
        VerificationData data = mock(VerificationData.class);
        delegate.verify(data);

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000);
                verificationMode.verify(data);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
        thread.interrupt();
    }

    @Test
    public void testCanRecoverFromFailure() {
        VerificationMode verificationMode = mock(VerificationMode.class);
        assertTrue(verificationOverTimeImpl.canRecoverFromFailure(verificationMode));
    }

    @Test
    public void testCannotRecoverFromFailure() {
        AtMost atMost = mock(AtMost.class);
        assertFalse(verificationOverTimeImpl.canRecoverFromFailure(atMost));
    }

    private VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(100L, 500L, delegate, false, timer);

}