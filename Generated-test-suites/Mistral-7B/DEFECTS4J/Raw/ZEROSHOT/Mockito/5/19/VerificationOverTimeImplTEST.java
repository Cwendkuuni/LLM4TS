package org.mockito.internal.verification.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.verification.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.api.VerificationDataBuilder;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.impl.VerificationAfterDelayImpl;
import org.mockito.internal.verification.impl.VerificationWithTimeoutImpl;
import org.mockito.internal.verification.test.stub.MockitoTimerStub;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class VerificationOverTimeImplTest {

    private static final long POLLING_PERIOD_MILLIS = 50;
    private static final long DURATION_MILLIS = 300;
    private static final long DELAY_MILLIS = 150;

    private VerificationMode verificationMode;
    private VerificationMode delegateVerificationMode;
    private Timer timer;

    @Before
    public void setUp() {
        delegateVerificationMode = mock(VerificationWithTimeoutImpl.class);
        timer = mock(MockitoTimerStub.class);

        verificationMode = new VerificationOverTimeImpl(POLLING_PERIOD_MILLIS, DURATION_MILLIS, delegateVerificationMode, false, timer);
    }

    @Test
    public void testVerificationWithTimeoutSuccess() {
        // Given
        VerificationData verificationData = VerificationDataBuilder.aVerificationData();

        // When
        verificationMode.verify(verificationData);

        // Then
        verify(delegateVerificationMode).verify(verificationData);
    }

    @Test
    public void testVerificationWithTimeoutFailure() {
        // Given
        VerificationData verificationData = VerificationDataBuilder.aVerificationData();
        doThrow(new RuntimeException("Delegate verification error")).when(delegateVerificationMode).verify(verificationData);

        // When & Then
        assertThrows(RuntimeException.class, () -> verificationMode.verify(verificationData));
    }

    @Test
    public void testVerificationWithTimeoutRecoverableFailure() {
        // Given
        VerificationData verificationData = VerificationDataBuilder.aVerificationData();
        doThrow(new AssertionError("Delegate verification error")).when(delegateVerificationMode).verify(verificationData);

        // When
        verificationMode.verify(verificationData);

        // Then
        // Verify that delegate verification mode is called once before test failure
        verify(delegateVerificationMode).verify(verificationData);

        // Verify that no other calls to delegate verification mode are made
        verifyNoMoreInteractions(delegateVerificationMode);
    }

    @Test
    public void testVerificationWithTimeoutNonRecoverableFailure() {
        // Given
        VerificationData verificationData = VerificationDataBuilder.aVerificationData();
        doThrow(new AssertionError("Delegate verification error")).when(delegateVerificationMode).verify(verificationData);

        // When
        doCallRealMethod().when(verificationMode).verify(verificationData);

        // When & Then
        assertThrows(AssertionError.class, () -> verificationMode.verify(verificationData));
    }

    @Test
    public void testVerificationAfterDelaySuccess() {
        // Given
        VerificationData verificationData = VerificationDataBuilder.aVerificationData();

        // When
        verificationMode = new VerificationOverTimeImpl(POLLING_PERIOD_MILLIS, DURATION_MILLIS,
                new VerificationAfterDelayImpl(POLLING_PERIOD_MILLIS, DELAY_MILLIS, delegateVerificationMode), true, timer);

        sleep(DELAY_MILLIS);

        // Then
        verificationMode.verify(verificationData);

        // Verify that delegate verification mode is called once
        verify(delegateVerificationMode).verify(verificationData);
    }

    @Test
    public void testVerificationAfterDelayFailure() {
        // Given
        VerificationData verificationData = VerificationDataBuilder.aVerificationData();
        doThrow(new RuntimeException("Delegate verification error"))
                .when(delegateVerificationMode).verify(verificationData);

        // When & Then
        assertThrows(RuntimeException.class, () -> verificationMode.verify(verificationData));
    }

    @Test
    public void testVerificationWithTimeoutAndDelaySuccess() {
        // Given
        VerificationData verificationData = VerificationDataBuilder.aVerificationData();

        // When
        verificationMode = new VerificationOverTimeImpl(POLLING_PERIOD_MILLIS, DURATION_MILLIS,
                new VerificationWithTimeoutImpl(POLLING_PERIOD_MILLIS, DELAY_MILLIS, delegateVerificationMode), false, timer);

        sleep(DELAY_MILLIS);

        // Then
        verificationMode.verify(verificationData);

        // Verify that delegate verification mode is called once
        verify(delegateVerificationMode).verify(verificationData);
    }

    @Test
    public void testVerificationWithTimeoutAndDelayFailure() {
        // Given
        VerificationData verificationData = VerificationDataBuilder.aVerificationData();
        doThrow(new RuntimeException("Delegate verification error"))
                .when(delegateVerificationMode).verify(verificationData);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            verificationMode.verify(verificationData);
            sleep(DELAY_MILLIS);
        });
    }

    @Test
    public void testGetters() {
        // Given
        VerificationData verificationData = VerificationDataBuilder.aVerificationData();

        // When
        VerificationMode verificationMode = new VerificationOverTimeImpl(POLLING_PERIOD_MILLIS, DURATION_MILLIS,
                new VerificationWithTimeoutImpl(POLLING_PERIOD_MILLIS, DELAY_MILLIS, delegateVerificationMode), false, timer);

        // Then
        assertEquals(POLLING_PERIOD_MILLIS, verificationMode.getPollingPeriod());
        assertEquals(DURATION_MILLIS, verificationMode.getDuration());
        assertNotNull(verificationMode.getDelegate());
    }

    @Test
    public void testCanRecoverFromFailure() {
        // Given
        VerificationMode verificationMode = VerificationModeFactory.aVerificationMode();

        // When
        assertTrue(verificationMode.canRecoverFromFailure(verificationMode));
        assertTrue(verificationMode.canRecoverFromFailure(new VerificationWithTimeoutImpl(POLLING_PERIOD_MILLIS, DELAY_MILLIS, delegateVerificationMode)));
        assertFalse(verificationMode.canRecoverFromFailure(new AtMost(3)));
        assertFalse(verificationMode.canRecoverFromFailure(new NoMoreInteractions()));
    }

    private void sleep(long sleep) {
        try {
            TimeUnit.MILLISECONDS.sleep(sleep);
        } catch (InterruptedException e) {
            // Do nothing
        }
    }

    private void doCallRealMethod() {
        try {
            spy(verificationMode).invokeSuper("verify", ArgumentMatchers.any(VerificationData.class));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}