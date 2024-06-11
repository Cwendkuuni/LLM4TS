package org.mockito.internal.verification.impl; // adjust the package if necessary

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.verification.VerificationMode;
import org.mockito.verification.VerificationModeFactory;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class VerificationOverTimeImplTest {

    private VerificationMode verificationMode;
    private VerificationMode timeoutVerificationMode;
    private VerificationMode delayVerificationMode;
    private VerificationData verificationData;

    @Before
    public void setUp() {
        verificationMode = Mockito.mock(VerificationMode.class);
        timeoutVerificationMode = Mockito.mock(org.mockito.verification.VerificationWithTimeout.class);
        delayVerificationMode = Mockito.mock(org.mockito.verification.VerificationAfterDelay.class);
        verificationData = new VerificationData(Mockito.mock(Object.class));

        VerificationModeFactory verificationModeFactory = Mockito.mock(VerificationModeFactory.class);
        when(verificationModeFactory.createVerificationMode(any(VerificationMode.class), any(Timer.class)))
                .thenReturn(verificationMode);
        when(verificationModeFactory.createVerificationMode(ArgumentMatchers.eq(timeoutVerificationMode), any(Timer.class)))
                .thenReturn(new VerificationOverTimeImpl(1, 100, timeoutVerificationMode, true));
        when(verificationModeFactory.createVerificationMode(ArgumentMatchers.eq(delayVerificationMode), any(Timer.class)))
                .thenReturn(new VerificationOverTimeImpl(1, 100, delayVerificationMode, false));
    }

    @Test
    public void testVerifySuccessWithTimeoutVerificationMode() {
        when(timeoutVerificationMode.verify(verificationData)).thenReturn(null);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1, 100, timeoutVerificationMode, true);
        verificationOverTime.verify(verificationData);

        verify(timeoutVerificationMode).verify(verificationData);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyFailureWithTimeoutVerificationMode() {
        doThrow(new AssertionError()).when(timeoutVerificationMode).verify(verificationData);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1, 100, timeoutVerificationMode, true);
        verificationOverTime.verify(verificationData);

        verify(timeoutVerificationMode).verify(verificationData);
    }

    @Test
    public void testVerifySuccessWithDelayVerificationMode() {
        verifyZeroInteractions(verificationMode);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1, 100, delayVerificationMode, false);
        verificationOverTime.verify(verificationData);

        verifyZeroInteractions(verificationMode);
        verifyNoMoreInteractions(verificationMode);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyFailureWithDelayVerificationMode() {
        doThrow(new AssertionError()).when(delayVerificationMode).verify(verificationData);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1, 100, delayVerificationMode, false);
        verificationOverTime.verify(verificationData);

        verifyZeroInteractions(verificationMode);
        verifyNoMoreInteractions(verificationMode);

        // verify that the delay verification mode is thrown
        verify(delayVerificationMode).verify(verificationData);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyFailureWithAtMostVerificationMode() {
        when(verificationMode.verify(verificationData)).thenThrow(new AssertionError());

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1, 100, verificationMode, false);
        verificationOverTime.verify(verificationData);

        // should not reach here since the verification mode is AtMost
        verify(verificationMode).verify(verificationData);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyFailureWithNoMoreInteractionsVerificationMode() {
        when(verificationMode.verify(verificationData)).thenThrow(new AssertionError());

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1, 100, verificationMode, false);
        verificationOverTime.verify(verificationData);

        // should not reach here since the verification mode is NoMoreInteractions
        verify(verificationMode).verify(verificationData);
        verifyNoMoreInteractions(verificationMode);
    }

    @Test
    public void testGetPollingPeriod() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1, 100, verificationMode, false);

        assertEquals(1, verificationOverTime.getPollingPeriod());
    }

    @Test
    public void testGetDuration() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1, 100, verificationMode, false);

        assertEquals(100, verificationOverTime.getDuration());
    }

    @Test
    public void testGetDelegate() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1, 100, verificationMode, false);

        assertEquals(verificationMode, verificationOverTime.getDelegate());
    }
}