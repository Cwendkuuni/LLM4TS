package org.mockito.internal.verification;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.api.VerificationStatistics;
import org.mockito.internal.verification.api.VerificationStatisticsCollector;

public class VerificationOverTimeImplTest {

    private VerificationMode delegate;
    private VerificationStatisticsCollector statisticsCollector;
    private Timer timer;
    private VerificationMode verificationOverTime;
    private VerificationData verificationData;

    @Before
    public void setUp() {
        delegate = mock(VerificationMode.class);
        statisticsCollector = mock(VerificationStatisticsCollector.class);
        timer = mock(Timer.class);
        verificationOverTime = new VerificationOverTimeImpl(100L, 1000L, delegate, true, timer);
        verificationData = mock(VerificationData.class);
    }

    @Test
    public void testVerify_delegateSatisfiedBeforeFullDuration_returnsImmediately() {
        when(delegate.verify(verificationData)).thenReturn();

        verificationOverTime.verify(verificationData);

        verify(delegate, never()).verify(verificationData);
        verify(timer, never()).stop();
    }

    @Test
    public void testVerify_delegateSatisfiedBeforeFullDuration_returnsImmediately_statisticsCollected() {
        VerificationStatistics statistics = mock(VerificationStatistics.class);
        when(statisticsCollector.getVerificationStatistics()).thenReturn(statistics);
        when(delegate.verify(verificationData)).thenReturn();

        verificationOverTime.verify(verificationData, statisticsCollector);

        verify(delegate, never()).verify(verificationData);
        verify(timer, never()).stop();
        verify(statisticsCollector).collectVerificationStatistics(any(VerificationData.class), any(VerificationStatistics.class));
    }

    @Test
    public void testVerify_delegateNotSatisfiedBeforeFullDuration_throwsException() {
        when(delegate.verify(verificationData)).thenThrow(new MockitoAssertionError("Mockito error message"));

        assertThrows(MockitoAssertionError.class, () -> {
            verificationOverTime.verify(verificationData);
        });

        verify(delegate).verify(verificationData);
        verify(timer).stop();
    }

    @Test
    public void testVerify_delegateNotSatisfiedBeforeFullDuration_throwsException_statisticsCollected() {
        VerificationStatistics statistics = mock(VerificationStatistics.class);
        when(statisticsCollector.getVerificationStatistics()).thenReturn(statistics);
        when(delegate.verify(verificationData)).thenThrow(new MockitoAssertionError("Mockito error message"));

        assertThrows(MockitoAssertionError.class, () -> {
            verificationOverTime.verify(verificationData, statisticsCollector);
        });

        verify(delegate).verify(verificationData);
        verify(timer).stop();
        verify(statisticsCollector).collectVerificationStatistics(any(VerificationData.class), any(VerificationStatistics.class));
    }

    @Test
    public void testVerify_delegateSatisfiedAfterFullDuration_throwsException() {
        when(delegate.verify(verificationData)).thenThrow(new MockitoAssertionError("Mockito error message"));
        when(timer.isCounting()).thenReturn(true);

        assertThrows(MockitoAssertionError.class, () -> {
            verificationOverTime.verify(verificationData);
        });

        verify(delegate).verify(verificationData);
        verify(timer).stop();
    }

    @Test
    public void testVerify_delegateSatisfiedAfterFullDuration_throwsException_statisticsCollected() {
        VerificationStatistics statistics = mock(VerificationStatistics.class);
        when(statisticsCollector.getVerificationStatistics()).thenReturn(statistics);
        when(delegate.verify(verificationData)).thenThrow(new MockitoAssertionError("Mockito error message"));
        when(timer.isCounting()).thenReturn(true);

        assertThrows(MockitoAssertionError.class, () -> {
            verificationOverTime.verify(verificationData, statisticsCollector);
        });

        verify(delegate).verify(verificationData);
        verify(timer).stop();
        verify(statisticsCollector).collectVerificationStatistics(any(VerificationData.class), any(VerificationStatistics.class));
    }

    @Test
    public void testCanRecoverFromFailure() {
        VerificationMode verificationModeAtLeastOnce = mock(VerificationMode.class);
        assertFalse(verificationOverTime.canRecoverFromFailure(verificationModeAtLeastOnce));

        VerificationMode verificationModeNever = mock(VerificationMode.class);
        assertFalse(verificationOverTime.canRecoverFromFailure(verificationModeNever));

        VerificationMode verificationModeAtMost = mock(VerificationMode.class);
        assertTrue(verificationOverTime.canRecoverFromFailure(verificationModeAtMost));
    }
}