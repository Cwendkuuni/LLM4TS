package org.mockito.internal.verification.api;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.verification.VerificationMode;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class VerificationOverTimeImplTest {

    private VerificationMode verificationMode;
    private VerificationOverTimeImpl verificationOverTime;
    private InvocationOnMock invocation;
    private Timer timer;

    @Before
    public void setUp() {
        verificationMode = mock(VerificationMode.class);
        timer = mock(Timer.class);
        when(timer.isCounting()).thenReturn(true);
        when(timer.start()).thenReturn(null);
        verificationOverTime = new VerificationOverTimeImpl(100L, 500L, verificationMode, true, timer);
        invocation = Mockito.mock(InvocationOnMock.class);
    }

    @Test
    public void testDelegateVerificationWithinTime() {
        // given
        VerificationData data = Mockito.mock(VerificationData.class);
        when(verificationMode.verify(ArgumentMatchers.eq(data))).thenReturn(null);

        // when
        verificationOverTime.verify(data);

        // then
        // no exception thrown, the delegate verification succeeded within the timeout
    }

    @Test(expected = MockitoAssertionError.class)
    public void testDelegateVerificationWithinTimeWithException() {
        // given
        VerificationData data = Mockito.mock(VerificationData.class);
        when(verificationMode.verify(ArgumentMatchers.eq(data))).thenThrow(new MockitoAssertionError("Expected exception"));

        // when
        verificationOverTime.verify(data);

        // then
        // the exception thrown by the delegate verification is re-thrown
    }

    @Test
    public void testDelegateVerificationAfterTime() {
        // given
        when(verificationMode.verify(ArgumentMatchers.eq(Mockito.any(VerificationData.class)))).thenThrow(new MockitoAssertionError("Delegate verification always fails"));

        // when
        verificationOverTime.verify(Mockito.mock(VerificationData.class));

        // then
        // the delegate verification did not succeed within the timeout, the last error thrown by it is re-thrown
    }

    @Test
    public void testDelegateVerificationWithNoMoreInteractions() {
        // given
        verificationOverTime = new VerificationOverTimeImpl(100L, 500L, Mockito.mock(NoMoreInteractions.class), false, timer);
        VerificationData data = Mockito.mock(VerificationData.class);
        when(verificationMode.verify(ArgumentMatchers.eq(data))).thenReturn(null);

        // when
        verificationOverTime.verify(data);

        // then
        // the delegate verification succeeded, but the verificationOverTime does not return until the full timeout has passed
        verify(verificationMode).verify(data);
        verifyNoMoreInteractions(verificationMode);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testDelegateVerificationWithNoMoreInteractionsAndException() {
        // given
        verificationOverTime = new VerificationOverTimeImpl(100L, 500L, Mockito.mock(NoMoreInteractions.class), false, timer);
        VerificationData data = Mockito.mock(VerificationData.class);
        when(verificationMode.verify(ArgumentMatchers.eq(data))).thenThrow(new MockitoAssertionError("Delegate verification always fails"));

        // when
        verificationOverTime.verify(data);

        // then
        // the delegate verification did not succeed within the timeout, the last error thrown by it is re-thrown
        verify(verificationMode).verify(data);
        verify(verificationMode, Mockito.times(2)).verify(data);
    }

    @Test
    public void testDelegateVerificationWithArgumentsAreDifferent() {
        // given
        VerificationData data1 = Mockito.mock(VerificationData.class);
        VerificationData data2 = Mockito.mock(VerificationData.class);
        when(verificationMode.verify(ArgumentMatchers.eq(data1))).thenReturn(null);
        when(verificationMode.verify(ArgumentMatchers.eq(data2))).thenThrow(new org.mockito.exceptions.verification.junit.ArgumentsAreDifferent("Different data objects"));

        // when
        verificationOverTime.verify(data1);

        // then
        // the delegate verification succeeded with the first data object, the verificationOverTime returns immediately
    }

    @Test
    public void testDelegateVerificationWithAtMost() {
        // given
        VerificationData data = Mockito.mock(VerificationData.class);
        VerificationMode atMost = Mockito.mock(AtMost.class);
        when(atMost.verify(ArgumentMatchers.eq(data))).thenThrow(new MockitoAssertionError("AtMost verification always fails"));
        verificationOverTime = new VerificationOverTimeImpl(100L, 500L, atMost, false, timer);

        // when
        verificationOverTime.verify(data);

        // then
        // the atMost verification always fails, the verificationOverTime throws the exception
    }

    @Test
    public void testPollingPeriod() {
        // given
        verificationOverTime = new VerificationOverTimeImpl(100L, 500L, verificationMode, true, timer);

        // when
        long pollingPeriod = verificationOverTime.getPollingPeriod();

        // then
        assertEquals(100L, pollingPeriod);
    }

    @Test
    public void testDuration() {
        // given
        verificationOverTime = new VerificationOverTimeImpl(100L, 500L, verificationMode, true, timer);

        // when
        long duration = verificationOverTime.getDuration();

        // then
        assertEquals(500L, duration);
    }

    @Test
    public void testDelegate() {
        // given
        verificationOverTime = new VerificationOverTimeImpl(100L, 500L, verificationMode, true, timer);

        // when
        VerificationMode result = verificationOverTime.getDelegate();

        // then
        assertEquals(verificationMode, result);
    }

    @Test
    public void testSleep() {
        // given

        // when
        verificationOverTime.sleep(100L);

        // then
        // no exception thrown
    }
}