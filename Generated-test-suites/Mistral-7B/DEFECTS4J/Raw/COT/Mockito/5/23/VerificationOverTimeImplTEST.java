package org.mockito.internal.verification.impl; // adjust the package if necessary

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito. ArgumentMatchers;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;
import org.mockito.verification.VerificationModeFactory;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {

    private VerificationMode verificationMode;
    private VerificationMode delegate;
    private Timer timer;

    @Before
    public void setup() {
        this.delegate = mock(VerificationMode.class);
        this.verificationMode = new VerificationOverTimeImpl(1, 1000, delegate, true);
        this.timer = new Timer(1000);
    }

    @Test
    public void testVerify_delegateIsSatisfiedBeforeTimeout_returnsImmediately() {
        when(delegate.verify(ArgumentMatchers.any(VerificationData.class))).thenReturn(null);
        verificationMode.verify(createVerificationData());
        verify(delegate).verify(ArgumentMatchers.any(VerificationData.class));
    }

    @Test
    public void testVerify_delegateIsSatisfiedWithinTimeout_returnsSuccess() {
        when(delegate.verify(ArgumentMatchers.any(VerificationData.class))).thenReturn(null);
        verificationMode.verify(createVerificationData());
        Assert.assertTrue(verificationMode.isSatisfied());
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_delegateIsNotSatisfiedWithinTimeout_throwsError() {
        when(delegate.verify(ArgumentMatchers.any(VerificationData.class))).thenThrow(new MockitoAssertionError("Delegate verification failed"));
        verificationMode.verify(createVerificationData());
    }

    @Test
    public void testVerify_delegateIsNotSatisfiedWithinTimeoutButCanRecover_pollsDelegate() {
        when(delegate.verify(ArgumentMatchers.any(VerificationData.class))).thenThrow(new MockitoAssertionError("Delegate verification failed"));
        verificationMode.verify(createVerificationData());
        sleep(verificationMode.getPollingPeriod());
        verify(delegate).verify(ArgumentMatchers.any(VerificationData.class));
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_delegateIsNotSatisfiedWithinTimeoutAndCannotRecover_throwsError() {
        VerificationMode verificationModeWithNoRecovery = new VerificationOverTimeImpl(1, 1000, delegate, false);
        when(delegate.verify(ArgumentMatchers.any(VerificationData.class))).thenThrow(new MockitoAssertionError("Delegate verification failed"));
        verificationModeWithNoRecovery.verify(createVerificationData());
    }

    @Test
    public void testCanRecoverFromFailure() {
        VerificationMode verificationModeWithNoRecoverableFailure = mock(VerificationMode.class);
        when(verificationModeWithNoRecoverableFailure.verify(ArgumentMatchers.any(VerificationData.class))).thenThrow(new AssertionError());
        Assert.assertFalse(verificationOverTimeImpl.canRecoverFromFailure(verificationModeWithNoRecoverableFailure));
    }

    @Test
    public void testCanRecoverFromFailure_otherVerificationModes() {
        VerificationMode verificationModeWithAtMost = VerificationModeFactory.withAtMost(1).times(ArgumentMatchers.any());
        VerificationMode verificationModeWithNoMoreInteractions = VerificationModeFactory.noMoreInteractions();
        Assert.assertTrue(verificationOverTimeImpl.canRecoverFromFailure(verificationModeWithAtMost));
        Assert.assertTrue(verificationOverTimeImpl.canRecoverFromFailure(verificationModeWithNoMoreInteractions));
    }

    @Test
    public void testGetPollingPeriod() {
        Assert.assertEquals(1, verificationMode.getPollingPeriod());
    }

    @Test
    public void testGetDuration() {
        Assert.assertEquals(1000, verificationMode.getDuration());
    }

    @Test
    public void testGetDelegate() {
        Assert.assertSame(delegate, verificationMode.getDelegate());
    }

    private VerificationData createVerificationData() {
        return new VerificationData(null, null);
    }

    private void sleep(long sleep) {
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException ie) {
            // oups. not much luck.
        }
    }

}