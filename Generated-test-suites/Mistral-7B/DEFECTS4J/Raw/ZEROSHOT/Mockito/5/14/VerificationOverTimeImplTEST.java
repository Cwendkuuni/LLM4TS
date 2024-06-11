package org.mockito.internal.verification.impl; // adjust the package name if necessary

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.internal.verification.api.VerificationModeWithTimeout;
import org.mockito.verification.VerificationModeFactory;

import java.util.concurrent.TimeUnit;

public class VerificationOverTimeImplTest {

    private VerificationMode delegate;
    private VerificationModeWithTimeout verificationMode;
    private Timer timer;

    @Before
    public void setUp() {
        delegate = mock(VerificationMode.class);
        timer = spy(new Timer(1000));
        VerificationModeFactory verificationModeFactory = mock(VerificationModeFactory.class);
        when(verificationModeFactory.createVerificationMode(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong(), ArgumentMatchers.eq(delegate), ArgumentMatchers.anyBoolean())).thenReturn(verificationMode);
        verificationMode = (VerificationModeWithTimeout) verificationModeFactory.createVerificationMode(100, 500, delegate, true); // replace true or false based on the returnOnSuccess constructor parameter
    }

    @After
    public void tearDown() {
        timer.stop();
    }

    @Test
    public void testVerifyWithDelegateSatisfiedBeforeTimeout() {
        // Given

        // When
        VerificationData data = new VerificationData(ArgumentMatchers.any(), ArgumentMatchers.eq(delegate));
        verificationMode.verify(data);

        // Then
        verify(delegate).verify(data);
    }

    @Test
    public void testVerifyWithDelegateSatisfiedAfterTimeout() {
        // Given
        doThrow(new MockitoAssertionError("Delegate verification error")).when(delegate).verify(ArgumentMatchers.any());

        // When
        VerificationData data = new VerificationData(ArgumentMatchers.any(), ArgumentMatchers.eq(delegate));
        long start = System.nanoTime();
        long timeout = TimeUnit.MILLISECONDS.toNanos(verificationMode.getDuration()) - (System.nanoTime() - start);
        verificationMode.verify(data);

        // Then
        // The delegate verification mode is not called before the timeout, and an exception is thrown
        verifyZeroInteractions(delegate);
        verify(delegate).verify(data);
        throw new MockitoAssertionError("Delegate verification error");
    }

    @Test
    public void testVerifyWithDelegateNotSatisfiedBeforeTimeout() {
        // Given
        doThrow(new MockitoAssertionError("Delegate verification error")).when(delegate).verify(ArgumentMatchers.any());

        // When
        VerificationData data = new VerificationData(ArgumentMatchers.any(), ArgumentMatchers.eq(delegate));

        // Then
        // The delegate verification mode is not called before the timeout, and an exception is thrown
        verifyZeroInteractions(delegate);
        try {
            verificationMode.verify(data);
            fail("Expected verificationMode.verify(data) to throw an exception");
        } catch (MockitoAssertionError e) {
            // Expected exception
        }
    }

    @Test
    public void testVerifyWithDelegateNotSatisfiedAfterTimeout() {
        // Given
        doThrow(new MockitoAssertionError("Delegate verification error")).when(delegate).verify(ArgumentMatchers.any());

        // When
        VerificationData data = new VerificationData(ArgumentMatchers.any(), ArgumentMatchers.eq(delegate));
        long start = System.nanoTime();
        long timeout = TimeUnit.MILLISECONDS.toNanos(verificationMode.getDuration());
        verificationMode.verify(data);

        // Then
        // The delegate verification mode is called repeatedly until it is satisfied or the full timeout has passed
        verify(delegate).verify(data);
        verify(delegate).verify(data);
        // The verificationMode.verify(data) call throws an exception if the delegate verification mode is not satisfied after the timeout
        try {
            verificationMode.verify(data);
            fail("Expected verificationMode.verify(data) to throw an exception");
        } catch (MockitoAssertionError e) {
            // Expected exception
        }
    }

    @Test
    public void testCanRecoverFromFailure() {
        // Given
        VerificationMode verificationModeWithUnrecoverableFailure = spy(delegate);
        doThrow(new AssertionError("Delegate verification error")).when(verificationModeWithUnrecoverableFailure).verify(ArgumentMatchers.any());

        // When & Then
        assertFalse(verificationModeWithOverTimeImplInstance.canRecoverFromFailure(verificationModeWithUnrecoverableFailure));
    }

    @Test
    public void testCanRecoverFromFailureWithRecoverableFailure() {
        // Given
        VerificationMode verificationModeWithRecoverableFailure = mock(VerificationMode.class);
        doThrow(new AssertionError("Delegate verification error")).when(verificationModeWithRecoverableFailure).verify(ArgumentMatchers.any());

        // When
        VerificationData data = new VerificationData(ArgumentMatchers.any(), ArgumentMatchers.eq(verificationModeWithRecoverableFailure));

        // Then
        // The delegate verification mode is retried until it is satisfied or a recoverable failure occurs
        when(verificationModeWithRecoverableFailure.verify(data)).thenThrow(new AssertionError("Delegate retryable failure"));
        when(verificationModeWithRecoverableFailure.verify(data)).thenReturn(null);
        verificationModeWithOverTimeImplInstance.verify(data);

        // The delegate verification mode is called once, and then returns successfully
        verify(verificationModeWithRecoverableFailure).verify(data);
        verifyNoMoreInteractions(verificationModeWithRecoverableFailure);
    }

    private VerificationOverTimeImpl verificationModeWithOverTimeImplInstance = new VerificationOverTimeImpl(100, 500, delegate, true, timer);
}