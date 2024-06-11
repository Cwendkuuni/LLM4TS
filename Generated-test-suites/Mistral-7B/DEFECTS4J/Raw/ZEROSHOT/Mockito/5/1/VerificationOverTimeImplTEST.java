package org.mockito.internal.verification.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMockException;
import org.mockito.Mockito;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.verification.VerificationMode;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class VerificationOverTimeImplTest {

    private VerificationMode verificationMode;
    private VerificationOverTimeImpl verificationOverTimeImpl;
    private Timer timer;

    @Before
    public void setup() {
        verificationMode = Mockito.spy(VerificationMode.times(1));
        timer = spy(new Timer(1000));
        verificationOverTimeImpl = new VerificationOverTimeImpl(500, 2000, verificationMode, true, timer);
    }

    @Test
    public void testVerifyMethod_whenDelegateIsSatisfiedBeforeTimeout_thenVerifyMethodReturnsImmediately() {
        VerificationData verificationData = new VerificationData(ArgumentMatchers.any());

        verificationMode.verify(verificationData);

        verificationOverTimeImpl.verify(verificationData);
        assertEquals(VerificationResult.success(), verificationOverTimeImpl.getVerificationResult());
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyMethod_whenDelegateIsNotSatisfiedBeforeTimeout_thenVerifyThrowsException() {
        VerificationData verificationData = new VerificationData(ArgumentMatchers.any());

        verificationOverTimeImpl.verify(verificationData);

        Mockito.verify(verificationMode, Mockito.times(0)).verify(verificationData);
        throw new AssertionError();
    }

    @Test
    public void testVerifyMethod_whenDelegateIsSatisfiedAfterTimeout_thenVerifyThrowsException() throws InterruptedException {
        VerificationData verificationData = new VerificationData(ArgumentMatchers.any());
        timer.start();
        Mockito.verify(verificationMode, Mockito.times(0)).verify(verificationData);

        Thread.sleep(3000);

        verify(verificationMode).verify(verificationData);
    }

    @Test(expected = InvocationOnMockException.class)
    public void testVerifyMethod_whenDelegateThrowsException_thenVerifyThrowsException() {
        VerificationData verificationData = new VerificationData(ArgumentMatchers.any());

        doThrow(new IllegalStateException("Mockito Exception")).when(verificationMode).verify(verificationData);

        verificationOverTimeImpl.verify(verificationData);
        verify(verificationMode).verify(verificationData);
    }

    @Test
    public void testGetPollingPeriod_whenCalled_thenReturnsPollingPeriod() {
        assertEquals(500L, verificationOverTimeImpl.getPollingPeriod());
    }

    @Test
    public void testGetDuration_whenCalled_thenReturnsDuration() {
        assertEquals(2000L, verificationOverTimeImpl.getDuration());
    }

    @Test
    public void testGetDelegate_whenCalled_thenReturnsDelegate() {
        assertEquals(verificationMode, verificationOverTimeImpl.getDelegate());
    }
}