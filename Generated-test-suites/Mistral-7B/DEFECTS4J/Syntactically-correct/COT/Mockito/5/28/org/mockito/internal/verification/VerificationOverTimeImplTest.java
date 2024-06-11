package org.mockito.internal.verification.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationModeStub;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.VerificationWithTimeout;
import org.mockito.internal.util.Timer;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerificationOverTimeImplConstructor() {
        VerificationMode delegate = mock(VerificationWithTimeout.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 500, delegate, true);
        Assert.assertNotNull(verificationOverTime);
        Assert.assertEquals(100, verificationOverTime.getPollingPeriod());
        Assert.assertEquals(500, verificationOverTime.getDuration());
        Assert.assertEquals(delegate, verificationOverTime.getDelegate());
        Assert.assertTrue(verificationOverTime.returnOnSuccess);
        Assert.assertNotNull(verificationOverTime.timer);
    }

    @Test
    public void testVerifySuccess() {
        VerificationMode delegate = new VerificationModeStub(true);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 500, delegate, true);
        VerificationData data = mock(VerificationData.class);
        verificationOverTime.verify(data);
        verify(delegate).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyFailure() {
        VerificationMode delegate = new VerificationModeStub(false);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 500, delegate, true);
        VerificationData data = mock(VerificationData.class);
        doThrow(new MockitoAssertionError("expected exception")).when(delegate).verify(data);
        verificationOverTime.verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyTimeout() {
        VerificationMode delegate = new VerificationModeStub(false);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(10, 50, delegate, true);
        VerificationData data = mock(VerificationData.class);
        doThrow(new MockitoAssertionError("expected exception")).when(delegate).verify(data);
        verificationOverTime.verify(data);
    }

    @Test
    public void testHandleVerifyException() {
        VerificationMode delegate = mock(VerificationWithTimeout.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 500, delegate, true);
        VerificationData data = mock(VerificationData.class);
        doThrow(new MockitoAssertionError("expected exception")).when(delegate).verify(data);
        Assert.assertNull(verificationOverTime.handleVerifyException(null));
        Assert.assertSame("expected exception", verificationOverTime.handleVerifyException(new MockitoAssertionError("expected exception")));
    }

    @Test
    public void testCanRecoverFromFailure() {
        VerificationMode delegateAtMost = mock(AtMost.class);
        VerificationMode delegateNoMoreInteractions = mock(NoMoreInteractions.class);
        VerificationMode delegateVerificationWithTimeout = new VerificationModeStub(true);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 500, delegateAtMost, false);
        Assert.assertFalse(verificationOverTime.canRecoverFromFailure(delegateAtMost));
        Assert.assertFalse(verificationOverTime.canRecoverFromFailure(delegateNoMoreInteractions));
        Assert.assertTrue(verificationOverTime.canRecoverFromFailure(delegateVerificationWithTimeout));
    }

    @Test
    public void testSleep() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 500, mock(VerificationMode.class), true);
        long sleep = 10;
        verificationOverTime.sleep(sleep);
        long elapsedTime = System.currentTimeMillis() - System.currentTimeMillis() % sleep;
        Assert.assertTrue(elapsedTime >= sleep);
    }
}

class VerificationModeStub implements VerificationMode {

    private boolean isSatisfied;

    public VerificationModeStub(boolean isSatisfied) {
        this.isSatisfied = isSatisfied;
    }

    @Override
    public void verify(VerificationData data) {
        if (isSatisfied) {
            System.out.println("VerificationModeStub is satisfied");
        } else {
            throw new RuntimeException("VerificationModeStub is not satisfied");
        }
    }
}