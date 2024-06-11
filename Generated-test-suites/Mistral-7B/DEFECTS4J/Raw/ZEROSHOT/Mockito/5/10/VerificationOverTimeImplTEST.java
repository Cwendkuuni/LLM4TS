package org.mockito.internal.verification.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationModeWithTimeout;
import org.mockito.internal.verification.VerificationModeAfterDelay;
import org.mockito.internal.verification.VerificationWithTimeout;
import org.mockito.internal.verification.VerificationAfterDelay;
import org.mockito.internal.verification.VerificationModeThat;
import org.mockito.internal.verification.VerificationModeNever;
import org.mockito.internal.verification.VerificationModeAtLeastOnce;
import org.mockito.internal.verification.VerificationModeAtMost;
import org.mockito.internal.verification.VerificationModeNoMoreInteractions;
import org.mockito.internal.verification.api.StoredVerificationData;
import org.mockito.internal.verification.timer.Timer;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.verifyZeroInteractions;

public class VerificationOverTimeImplTest {

    private VerificationMode verificationWithTimeout;
    private VerificationMode verificationAfterDelay;
    private VerificationOverTimeImpl verificationOverTimeWithTimeout;
    private VerificationOverTimeImpl verificationOverTimeAfterDelay;
    private Timer timer;

    @Before
    public void setup() {
        timer = mock(Timer.class);
        verificationWithTimeout = new VerificationWithTimeout(ArgumentMatchers.any(), ArgumentMatchers.anyLong(), timer);
        verificationAfterDelay = new VerificationAfterDelay(ArgumentMatchers.any(), ArgumentMatchers.anyLong(), timer);
        verificationOverTimeWithTimeout = new VerificationOverTimeImpl(100L, 500L, verificationWithTimeout, true, timer);
        verificationOverTimeAfterDelay = new VerificationOverTimeImpl(100L, 500L, verificationAfterDelay, false, timer);
    }

    @Test
    public void testVerificationOverTimeWithTimeoutSuccess() {
        StoredVerificationData data = StoredVerificationData.createAndStore(mock(Object.class), "methodToMock", ArgumentMatchers.anyInt());
        verificationOverTimeWithTimeout.verify(data);
        verify(data.getMockedObject()).methodToMock(ArgumentMatchers.anyInt());
        verifyZeroInteractions(data.getMockedObject());
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerificationOverTimeWithTimeoutFailure() {
        StoredVerificationData data = StoredVerificationData.createAndStore(mock(Object.class), "methodToMock", ArgumentMatchers.anyInt());
        timer.reset();
        verificationOverTimeWithTimeout.verify(data);
        timer.assertTimeout(501, () -> {
            verify(data.getMockedObject()).methodToMock(ArgumentMatchers.anyInt());
            verifyZeroInteractions(data.getMockedObject());
        });
    }

    @Test
    public void testVerificationOverTimeAfterDelaySuccess() {
        StoredVerificationData data = StoredVerificationData.createAndStore(mock(Object.class), "methodToMock", ArgumentMatchers.anyInt());
        verificationOverTimeAfterDelay.verify(data);
        timer.assertTimeout(501, () -> {
            verify(data.getMockedObject()).methodToMock(ArgumentMatchers.anyInt());
            verifyNoMoreInteractions(data.getMockedObject());
        });
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerificationOverTimeAfterDelayFailure() {
        StoredVerificationData data = StoredVerificationData.createAndStore(mock(Object.class), "methodToMock", ArgumentMatchers.anyInt());
        timer.reset();
        verificationOverTimeAfterDelay.verify(data);
        timer.assertTimeout(501, () -> {
            verify(data.getMockedObject()).methodToMock(ArgumentMatchers.anyInt());
            verifyNoMoreInteractions(data.getMockedObject());
        });
    }

    @Test
    public void testVerificationOverTimeWithTimeoutAtLeastOnce() {
        StoredVerificationData data = StoredVerificationData.createAndStore(mock(Object.class), "methodToMock", ArgumentMatchers.anyInt());
        verificationOverTimeWithTimeout = new VerificationOverTimeImpl(100L, 500L, atLeastOnce(verificationWithTimeout), true, timer);
        verificationOverTimeWithTimeout.verify(data);
        verify(data.getMockedObject(), atLeastOnce()).methodToMock(ArgumentMatchers.anyInt());
        verifyZeroInteractions(data.getMockedObject());
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerificationOverTimeWithTimeoutAtLeastOnceTimeout() {
        StoredVerificationData data = StoredVerificationData.createAndStore(mock(Object.class), "methodToMock", ArgumentMatchers.anyInt());
        timer.reset();
        verificationOverTimeWithTimeout = new VerificationOverTimeImpl(100L, 500L, atLeastOnce(verificationWithTimeout), true, timer);
        verificationOverTimeWithTimeout.verify(data);
        timer.assertTimeout(501, () -> {
            verify(data.getMockedObject(), atLeastOnce()).methodToMock(ArgumentMatchers.anyInt());
            verifyZeroInteractions(data.getMockedObject());
        });
    }

    @Test
    public void testVerificationOverTimeWithTimeoutAtMost() {
        StoredVerificationData data = StoredVerificationData.createAndStore(mock(Object.class), "methodToMock", ArgumentMatchers.anyInt());
        verificationOverTimeWithTimeout = new VerificationOverTimeImpl(100L, 500L, atMost(verificationWithTimeout), true, timer);
        verificationOverTimeWithTimeout.verify(data);
        verify(data.getMockedObject(), atMost(1)).methodToMock(ArgumentMatchers.anyInt());
        verifyZeroInteractions(data.getMockedObject());
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerificationOverTimeWithTimeoutAtMostTimeout() {
        StoredVerificationData data = StoredVerificationData.createAndStore(mock(Object.class), "methodToMock", ArgumentMatchers.anyInt());
        timer.reset();
        verificationOverTimeWithTimeout = new VerificationOverTimeImpl(100L, 500L, atMost(verificationWithTimeout), true, timer);
        verificationOverTimeWithTimeout.verify(data);
        timer.assertTimeout(501, () -> {
            verify(data.getMockedObject(), atMost(1)).methodToMock(ArgumentMatchers.anyInt());
            verifyZeroInteractions(data.getMockedObject());
        });
    }

    @Test
    public void testVerificationOverTimeWithTimeoutNever() {
        StoredVerificationData data = StoredVerificationData.createAndStore(mock(Object.class), "methodToMock", ArgumentMatchers.anyInt());
        verificationOverTimeWithTimeout = new VerificationOverTimeImpl(100L, 500L, never(verificationWithTimeout), true, timer);
        verificationOverTimeWithTimeout.verify(data);
        verifyZeroInteractions(data.getMockedObject());
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerificationOverTimeWithTimeoutNeverTimeout() {
        StoredVerificationData data = StoredVerificationData.createAndStore(mock(Object.class), "methodToMock", ArgumentMatchers.anyInt());
        timer.reset();
        verificationOverTimeWithTimeout = new VerificationOverTimeImpl(100L, 500L, never(verificationWithTimeout), true, timer);
        verificationOverTimeWithTimeout.verify(data);
        timer.assertTimeout(501, () -> {
            verifyZeroInteractions(data.getMockedObject());
        });
    }
}