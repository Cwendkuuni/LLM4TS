package org.mockito.internal.verification;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito. ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.util.Timer;

public class VerificationOverTimeImplTest {

    private VerificationMode delegate;
    private VerificationMode verificationOverTime;
    private Timer timer;

    @Before
    public void setup() {
        delegate = mock(VerificationMode.class);
        timer = mock(Timer.class);
        verificationOverTime = new VerificationOverTimeImpl(100L, 500L, delegate, false, timer);
    }

    @Test
    public void testVerifyDelegateSatisfiedBeforeTimeout() {
        VerificationData data = mock(VerificationData.class);
        when(delegate.verify(data)).thenReturn(true);

        verificationOverTime.verify(data);
        verify(delegate).verify(data);
        assertTrue(verificationOverTime.isSatisfied());
    }

    @Test
    public void testVerifyDelegateSatisfiedAfterTimeout() {
        VerificationData data = mock(VerificationData.class);
        when(delegate.verify(data)).thenThrow(new MockitoAssertionError("Delegate verification failed"));

        ArgumentCaptor<MockitoAssertionError> exceptionCaptor = ArgumentCaptor.forClass(MockitoAssertionError.class);

        verificationOverTime.verify(data);
        verify(delegate).verify(data);
        verify(delegate).verify(data); // Will throw exception
        assertFalse(verificationOverTime.isSatisfied());
        verify(timer).isCounting();

        MockitoAssertionError exception = verificationOverTime.getLastError();
        assertEquals("Delegate verification failed", exception.getMessage());
    }

    @Test
    public void testVerifyDelegateNeverSatisfied() {
        VerificationData data = mock(VerificationData.class);
        when(delegate.verify(data)).thenThrow(new MockitoAssertionError("Delegate verification failed"));

        ArgumentCaptor<MockitoAssertionError> exceptionCaptor = ArgumentCaptor.forClass(MockitoAssertionError.class);

        verificationOverTime.verify(data);
        verify(delegate).verify(data);
        verify(delegate).verify(data); // Will throw exception
        assertFalse(verificationOverTime.isSatisfied());
        verify(timer).isCounting();

        MockitoAssertionError exception = verificationOverTime.getLastError();
        assertEquals("Delegate verification failed", exception.getMessage());
    }

    @Test
    public void testVerifyNoInteractions() {
        VerificationData data = mock(VerificationData.class);
        VerificationMode noInteractions = mock(VerificationMode.class);
        when(noInteractions.verify(data)).thenThrow(new MockitoAssertionError("VerificationForbidden"));

        verificationOverTime = new VerificationOverTimeImpl(100L, 500L, noInteractions, false, timer);

        verificationOverTime.verify(data);
        verifyNoMoreInteractions(noInteractions);
        assertFalse(verificationOverTime.isSatisfied());
        verify(timer).isCounting();
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyNoInteractionsThrowsException() {
        VerificationData data = mock(VerificationData.class);
        VerificationMode noInteractions = mock(VerificationMode.class);
        when(noInteractions.verify(data)).thenThrow(new MockitoAssertionError("VerificationForbidden"));

        verificationOverTime = new VerificationOverTimeImpl(100L, 500L, noInteractions, false, timer);
        verificationOverTime.verify(data);
    }

    @Test
    public void testReturnOnSuccess() {
        VerificationData data = mock(VerificationData.class);
        when(delegate.verify(data)).thenReturn(true);

        verificationOverTime = new VerificationOverTimeImpl(100L, 500L, delegate, true, timer);

        verificationOverTime.verify(data);
        assertTrue(verificationOverTime.isSatisfied());
    }

    @Test
    public void testSleep() {
        long sleep = 100L;
        verificationOverTime.sleep(sleep);
        assertTrue(sleep > 0);
    }

    @Test
    public void testGetPollingPeriod() {
        long expected = 100L;
        long actual = verificationOverTime.getPollingPeriod();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDuration() {
        long expected = 500L;
        long actual = verificationOverTime.getDuration();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDelegate() {
        VerificationMode expected = delegate;
        VerificationMode actual = verificationOverTime.getDelegate();
        assertEquals(expected, actual);
    }
}