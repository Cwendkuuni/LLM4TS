package org.mockito.internal.verification.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationWithTimeout;
import org.mockito.internal.verification.VerificationAfterDelay;
import org.mockito.internal.verification.VerificationModeCounterSpy;
import org.mockito.internal.verification.VerificationModeNoMoreInteractionsSpy;
import org.mockito.internal.verification.VerificationModeAtMostNTimesSpy;
import org.mockito.internal.util.Timer;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class VerificationOverTimeImplTest {

    private VerificationData data;
    private VerificationMode delegate;
    private VerificationOverTimeImpl verificationOverTime;
    private Timer timer;

    @Test
    public void testConstructor() {
        long pollingPeriodMillis = 50;
        long durationMillis = 200;
        VerificationMode delegate = spy(new VerificationWithTimeout(1));
        boolean returnOnSuccess = true;
        timer = new Timer(durationMillis);

        verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);

        Assert.assertEquals(pollingPeriodMillis, verificationOverTime.getPollingPeriod());
        Assert.assertEquals(durationMillis, verificationOverTime.getDuration());
        Assert.assertEquals(delegate, verificationOverTime.getDelegate());
        Assert.assertFalse(verificationOverTime.isSatisfied());
    }

    @Test
    public void testVerifySuccess() {
        long pollingPeriodMillis = 50;
        long durationMillis = 200;
        VerificationMode delegate = spy(new VerificationWithTimeout(1));
        boolean returnOnSuccess = true;
        timer = new Timer(durationMillis);

        verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);

        data = new VerificationData(delegate, 1);
        doAnswer(invocation -> {}).when(delegate).verify(data);

        verificationOverTime.verify(data);
        verify(delegate).verify(data);
        Assert.assertTrue(verificationOverTime.isSatisfied());
    }

    @Test
    public void testVerifyFailureWithRecoverableDelegate() {
        long pollingPeriodMillis = 50;
        long durationMillis = 200;
        VerificationMode delegate = spy(new VerificationWithTimeout(2));
        boolean returnOnSuccess = false;
        timer = new Timer(durationMillis);

        verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);

        data = new VerificationData(delegate, 1);
        doThrow(new MockitoAssertionError("Error")).when(delegate).verify(data);

        try {
            verificationOverTime.verify(data);
            fail("Expected MockitoAssertionError to be thrown");
        } catch (MockitoAssertionError e) {
            Assert.assertEquals("Error", e.getMessage());
            Assert.assertFalse(verificationOverTime.isSatisfied());
        }

        sleep(pollingPeriodMillis);
        verify(delegate).verify(data);
        Assert.assertTrue(verificationOverTime.isSatisfied());
    }

    @Test
    public void testVerifyFailureWithNonRecoverableDelegate() {
        long pollingPeriodMillis = 50;
        long durationMillis = 200;
        VerificationMode delegate = spy(new VerificationModeNoMoreInteractions());
        boolean returnOnSuccess = false;
        timer = new Timer(durationMillis);

        verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);

        data = new VerificationData(delegate, 1);
        doThrow(new MockitoAssertionError("Error")).when(delegate).verify(data);

        try {
            verificationOverTime.verify(data);
            fail("Expected MockitoAssertionError to be thrown");
        } catch (MockitoAssertionError e) {
            Assert.assertEquals("Error", e.getMessage());
            Assert.assertFalse(verificationOverTime.isSatisfied());
        }

        sleep(pollingPeriodMillis);
        verify(delegate).verify(data);
        Assert.assertFalse(verificationOverTime.isSatisfied());
    }

    @Test
    public void testVerifyFailureWithAtMostDelegate() {
        long pollingPeriodMillis = 50;
        long durationMillis = 200;
        VerificationMode delegate = spy(new VerificationModeAtMostNTimesSpy(0));
        boolean returnOnSuccess = false;
        timer = new Timer(durationMillis);

        verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);

        data = new VerificationData(delegate, 1);
        doAnswer(invocation -> {}).when(delegate).verify(data);

        verificationOverTime.verify(data);
        verify(delegate).verify(data);
        Assert.assertFalse(verificationOverTime.isSatisfied());

        sleep(pollingPeriodMillis);
        verify(delegate).verify(data, atLeastOnce());
        Assert.assertFalse(verificationOverTime.isSatisfied());

        sleep(pollingPeriodMillis);
        verify(delegate).verify(data, atLeastOnce());
        Assert.assertFalse(verificationOverTime.isSatisfied());

        sleep(pollingPeriodMillis);
        verify(delegate).verify(data, atLeastOnce());
        Assert.assertTrue(verificationOverTime.isSatisfied());
    }

    @Test
    public void testVerifySuccessWithAtMostDelegate() {
        long pollingPeriodMillis = 50;
        long durationMillis = 200;
        VerificationMode delegate = spy(new VerificationModeAtMostNTimesSpy(1));
        boolean returnOnSuccess = true;
        timer = new Timer(durationMillis);

        verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);

        data = new VerificationData(delegate, 1);
        doAnswer(invocation -> {}).when(delegate).verify(data);

        verificationOverTime.verify(data);
        verify(delegate).verify(data);
        Assert.assertTrue(verificationOverTime.isSatisfied());
    }

    @Test
    public void testVerifySuccessWithNoMoreInteractionsDelegate() {
        long pollingPeriodMillis = 50;
        long durationMillis = 200;
        VerificationMode delegate = spy(new VerificationModeNoMoreInteractionsSpy());
        boolean returnOnSuccess = true;
        timer = new Timer(durationMillis);

        verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);

        data = new VerificationData(delegate, 1);
        verifyNoMoreInteractions(delegate);
        Assert.assertTrue(verificationOverTime.isSatisfied());
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyFailureWithNoMoreInteractionsDelegate() {
        long pollingPeriodMillis = 50;
        long durationMillis = 200;
        VerificationMode delegate = spy(new VerificationModeNoMoreInteractionsSpy());
        boolean returnOnSuccess = false;
        timer = new Timer(durationMillis);

        verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);

        data = new VerificationData(delegate, 1);
        doAnswer(invocation -> {}).when(delegate).verify(data);

        verificationOverTime.verify(data);
        verifyNoMoreInteractions(delegate);
        Assert.assertFalse(verificationOverTime.isSatisfied());

        sleep(pollingPeriodMillis);
        verify(delegate).verify(data);
        Assert.assertTrue(verificationOverTime.isSatisfied());

        sleep(pollingPeriodMillis);
        verifyNoMoreInteractions(delegate);
        Assert.assertFalse(verificationOverTime.isSatisfied());

        sleep(pollingPeriodMillis);
        verifyNoMoreInteractions(delegate);
        Assert.assertTrue(verificationOverTime.isSatisfied());

        sleep(pollingPeriodMillis);
        verifyNoMoreInteractions(delegate);
        Assert.assertTrue(verificationOverTime.isSatisfied());

        sleep(pollingPeriodMillis);
        verifyNoMoreInteractions(delegate);
        Assert.assertTrue(verificationOverTime.isSatisfied());

        data = new VerificationData(delegate, 2);
        verificationOverTime.verify(data);
        Assert.assertFalse(verificationOverTime.isSatisfied());

        sleep(pollingPeriodMillis);
        verify(delegate).verify(data, atLeast(2));
        Assert.assertTrue(verificationOverTime.isSatisfied());
    }

    @Test
    public void testGetPollingPeriod() {
        long pollingPeriodMillis = 50;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, 200, null, false, null);

        Assert.assertEquals(pollingPeriodMillis, verificationOverTime.getPollingPeriod());
    }

    @Test
    public void testGetDuration() {
        long durationMillis = 200;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(50, durationMillis, null, false, null);

        Assert.assertEquals(durationMillis, verificationOverTime.getDuration());
    }

    @Test
    public void testIsSatisfied() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(50, 200, null, false, null);

        Assert.assertFalse(verificationOverTime.isSatisfied());
    }

    @Test
    public void testGetDelegate() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(50, 200, null, false, null);
        VerificationMode delegate = new VerificationWithTimeout(1);
        verificationOverTime.setDelegate(delegate);

        Assert.assertEquals(delegate, verificationOverTime.getDelegate());
    }

    private void sleep(long sleep) {
        try {
            TimeUnit.MILLISECONDS.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}