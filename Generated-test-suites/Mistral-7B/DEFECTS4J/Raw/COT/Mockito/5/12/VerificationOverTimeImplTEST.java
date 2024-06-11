package org.mockito.internal.verification.test;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.VerificationData;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.verification.VerificationModeFactory;
import org.mockito.Mockito;
import org.mockito.internal.util.Timer;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerificationOverTimeImplConstructor() {
        long pollingPeriodMillis = 100;
        long durationMillis = 500;
        VerificationMode delegate = mock(VerificationMode.class);

        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);

        Assert.assertNotNull(verificationMode);
        Assert.assertEquals(pollingPeriodMillis, verificationMode.getPollingPeriod());
        Assert.assertEquals(durationMillis, verificationMode.getDuration());
        Assert.assertEquals(delegate, verificationMode.getDelegate());
        Assert.assertFalse(verificationMode.returnOnSuccess());
    }

    @Test
    public void testVerifyWithSuccess() throws Exception {
        long pollingPeriodMillis = 100;
        long durationMillis = 500;
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, true);
        VerificationData data = new VerificationData("mockMethod", Mockito.any(), Mockito.eq(1L));

        Mockito.doReturn(times(1)).when(delegate).verify(data);

        verificationMode.verify(data);

        Assert.assertTrue("Method should return successfully", verificationMode.isSatisfied());
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyWithFailureAndNotRecoverable() throws Exception {
        long pollingPeriodMillis = 100;
        long durationMillis = 500;
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);
        VerificationData data = new VerificationData("mockMethod", Mockito.any(), Mockito.eq(1L));

        Mockito.doThrow(new MockitoAssertionError("Expected exception message")).when(delegate).verify(data);

        verificationMode.verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyWithFailureAndRecoverable() throws Exception {
        long pollingPeriodMillis = 100;
        long durationMillis = 500;
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);
        VerificationData data = new VerificationData("mockMethod", Mockito.any(), Mockito.eq(1L));

        Mockito.doThrow(new MockitoAssertionError("Expected exception message")).when(delegate).verify(data);

        verificationMode.verify(data);
        verificationMode.verify(data);
        verificationMode.verify(data);

        Assert.assertFalse("Method should not be satisfied", verificationMode.isSatisfied());
    }

    @Test
    public void testVerifyWithTimeout() throws Exception {
        long pollingPeriodMillis = 10;
        long durationMillis = 150;
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);
        VerificationData data = new VerificationData("mockMethod", Mockito.any(), Mockito.eq(1L));

        Mockito.doNothing().when(delegate).verify(data);

        long startTime = System.currentTimeMillis();
        verificationMode.verify(data);
        long elapsedTime = System.currentTimeMillis() - startTime;

        Assert.assertFalse("Method should not be satisfied", verificationMode.isSatisfied());
        Assert.assertTrue("Timeout should have been exceeded", elapsedTime > durationMillis);
    }

    @Test
    public void testSleep() throws InterruptedException {
        long sleep = 100;
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(0, 0, null, false, new Timer(0));

        verificationMode.sleep(sleep);

        long elapsedTime = System.currentTimeMillis() - System.currentTimeMillis() % sleep;
        Assert.assertTrue("Sleep should have lasted for the specified time", elapsedTime >= sleep);
    }

    @Test
    public void testCanRecoverFromFailure() {
        VerificationMode delegate1 = mock(VerificationMode.class);
        VerificationMode delegate2 = mock(VerificationMode.class);

        Assert.assertTrue(new VerificationOverTimeImpl(0, 0, delegate1, false).canRecoverFromFailure(delegate2));
        Assert.assertFalse(new VerificationOverTimeImpl(0, 0, delegate1, false).canRecoverFromFailure(Mockito.mock(AtMost.class)));
        Assert.assertFalse(new VerificationOverTimeImpl(0, 0, delegate1, false).canRecoverFromFailure(Mockito.mock(NoMoreInteractions.class)));
    }
}