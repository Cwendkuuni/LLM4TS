package org.mockito.internal.verification.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.impl.VerificationOverTimeImpl;
import org.mockito.junit.jUpgrade.TestForAnnotations;

import java.util.concurrent.TimeUnit;

@TestForAnnotations(VerificationOverTimeImpl.class)
public class VerificationOverTimeImplTest {

    

    // Test constructor
    @Test
    public void testVerificationOverTimeImplConstruction() {
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);
        assertEquals(pollingPeriodMillis, verificationOverTime.getPollingPeriod());
        assertEquals(durationMillis, verificationOverTime.getDuration());
        assertEquals(delegate, verificationOverTime.getDelegate());
    }

    // Test basic functionality
    @Test
    public void testBasicFunctionality() throws InterruptedException {
        long pollingPeriodMillis = 10;
        long durationMillis = 100;
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate.verify(any(VerificationData.class))).thenReturn(null);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);

        VerificationData data = mock(VerificationData.class);
        verificationOverTime.verify(data);
        verify(delegate, times(1)).verify(data);
    }

    // Test timeout exception
    @Test(expected = MockitoAssertionError.class)
    public void testTimeoutException() throws InterruptedException {
        long pollingPeriodMillis = 10;
        long durationMillis = 5;
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate.verify(any(VerificationData.class))).thenThrow(new MockitoAssertionError("Mock not called as expected."));
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);

        VerificationData data = mock(VerificationData.class);
        verificationOverTime.verify(data);
    }

    // Test recoverable failure
    @Test
    public void testRecoverableFailure() throws InterruptedException {
        long pollingPeriodMillis = 10;
        long durationMillis = 50;
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate.verify(any(VerificationData.class))).thenThrow(new MockitoAssertionError("Mock not called as expected."));
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, true);

        VerificationData data = mock(VerificationData.class);
        verificationOverTime.verify(data);
        verify(delegate, times(1)).verify(data);
    }

    // Test non-recoverable failure
    @Test(expected = MockitoAssertionError.class)
    public void testNonRecoverableFailure() throws InterruptedException {
        long pollingPeriodMillis = 10;
        long durationMillis = 50;
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate.verify(any(VerificationData.class))).thenThrow(new AssertionError("Mock not called as expected."));
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);

        VerificationData data = mock(VerificationData.class);
        verificationOverTime.verify(data);
    }

    // Test getDuration()
    @Test
    public void testGetDuration() {
        long durationMillis = 100;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(10, durationMillis, mock(VerificationMode.class), false);
        assertEquals(durationMillis, verificationOverTime.getDuration());
    }

    // Test getPollingPeriod()
    @Test
    public void testGetPollingPeriod() {
        long pollingPeriodMillis = 10;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, 100, mock(VerificationMode.class), false);
        assertEquals(pollingPeriodMillis, verificationOverTime.getPollingPeriod());
    }

    // Test getDelegate()
    @Test
    public void testGetDelegate() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(10, 100, delegate, false);
        assertEquals(delegate, verificationOverTime.getDelegate());
    }

    

}