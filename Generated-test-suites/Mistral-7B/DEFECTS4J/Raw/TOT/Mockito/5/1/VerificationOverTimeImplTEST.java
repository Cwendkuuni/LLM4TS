package org.mockito.internal.verification.impl; // replace with the actual package name if different

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.AssertionError;
import org.mockito.InvocationOnMockException;
import org.mockito.exceptions.base.MockitoAssertionError;

public class VerificationOverTimeImplTest {

    private VerificationMode delegate;
    private VerificationOverTimeImpl verificationOverTime;

    @Before
    public void setUp() {
        delegate = mock(VerificationMode.class);
        verificationOverTime = new VerificationOverTimeImpl(100L, 500L, delegate, true); // 500ms max time, 100ms polling period, returnOnSuccess = true
    }

    @Test
    public void testDelegateVerificationModeSatisfiedBeforeTimeout() {
        VerificationData data = new VerificationData(delegate, ArgumentMatchers.any());

        // when the delegate is satisfied before timeout, the verification should succeed
        when(delegate.verify(ArgumentMatchers.any())).thenReturn(null);

        verificationOverTime.verify(data);

        // no error thrown, so the test passes
    }

    @Test(expected = MockitoAssertionError.class)
    public void testDelegateVerificationModeNotSatisfiedBeforeTimeoutButCanRecover() {
        VerificationData data = new VerificationData(delegate, ArgumentMatchers.any());

        when(delegate.verify(ArgumentMatchers.any())).thenThrow(new AssertionError("Expected invocation but it did not occur."));

        try {
            verificationOverTime.verify(data);
            // this should not be reached, as the test expects an AssertionError to be thrown
        } catch (AssertionError e) {
            // checking that the exception is the same as the one thrown by the delegate
            AssertionError exceptionThrownByDelegate = (AssertionError) e.getCause();
            assertEquals("Expected invocation but it did not occur.", exceptionThrownByDelegate.getMessage());
        }
    }

    @Test(expected = MockitoAssertionError.class)
    public void testDelegateVerificationModeNotSatisfiedBeforeTimeoutCannotRecover() {
        VerificationData data = new VerificationData(mock(AtMost.class), ArgumentMatchers.any()); // AtMost cannot recover from failure

        when(delegate.verify(ArgumentMatchers.any())).thenThrow(new AssertionError("Expected invocation but it did not occur."));

        try {
            verificationOverTime.verify(data);
            fail("Expected MockitoAssertionError to be thrown");
        } catch (MockitoAssertionError e) {
            // the test passes
        }
    }

    @Test(expected = InterruptedException.class)
    public void testThreadInterruptedDuringVerification() {
        VerificationData data = new VerificationData(delegate, ArgumentMatchers.any());

        doThrow(new InterruptedException()).when(delegate).verify(ArgumentMatchers.any());

        verificationOverTime.verify(data);

        // InterruptedException should be thrown as the delegate verification fails and the verificationOverTime implementation cannot recover
    }

    @Test
    public void testGetPollingPeriod() {
        Assert.assertEquals(100L, verificationOverTime.getPollingPeriod());
    }

    @Test
    public void testGetDuration() {
        Assert.assertEquals(500L, verificationOverTime.getDuration());
    }

    @Test
    public void testGetDelegate() {
        Assert.assertSame(delegate, verificationOverTime.getDelegate());
    }
}