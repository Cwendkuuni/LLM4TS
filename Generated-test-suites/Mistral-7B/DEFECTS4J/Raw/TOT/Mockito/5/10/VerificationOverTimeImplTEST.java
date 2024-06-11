package org.mockito.internal.verification.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.verification.VerificationMode;
import org.mockito.verification.VerificationModeFactory;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class VerificationOverTimeImplTest {

    private VerificationMode verificationMode;
    private VerificationMode delegate;
    private Timer timer;

    @Before
    public void setup() {
        delegate = Mockito.mock(VerificationMode.class);
        verificationMode = new VerificationOverTimeImpl(100L, 500L, delegate, true, Mockito.mock(Timer.class));
        timer = Mockito.mock(Timer.class);
    }

    @Test
    public void testDelegateIsSatisfiedBeforeTimeout() {
        // Given
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        VerificationData data = new VerificationData(invocation, ArgumentMatchers.any());

        // When
        when(delegate.verify(data)).thenReturn(null);
        verificationMode.verify(data);

        // Then
        verify(delegate).verify(data);
    }

    @Test
    public void testDelegateIsSatisfiedAfterTimeout() {
        // Given
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        VerificationData data = new VerificationData(invocation, ArgumentMatchers.any());

        // When
        when(delegate.verify(data)).thenThrow(new MockitoAssertionError("Delegate error"));

        // Then
        try {
            verificationMode.verify(data);
            fail("Expected MockitoAssertionError");
        } catch (AssertionError e) {
            verify(delegate).verify(data); // AssertionError thrown by delegate should be re-thrown
            assertEquals("Delegate error", e.getMessage());
        }
    }

    @Test
    public void testDelegateNeverRecovers() {
        // Given
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        VerificationData data = new VerificationData(invocation, ArgumentMatchers.any());

        when(delegate.verify(data)).thenThrow(new MockitoAssertionError("Delegate error"));

        // When & Then
        try {
            verificationMode.verify(data);
            fail("Expected MockitoAssertionError");
        } catch (AssertionError e) {
            assertEquals("Delegate error", e.getMessage());
            verify(delegate, never()).verify(data); // Delegate should not recover
        }
    }

    @Test
    public void testDelegateIsSatisfiedAfterDelay() {
        // Given
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        VerificationData data = new VerificationData(invocation, ArgumentMatchers.any());

        when(delegate.verify(data)).thenReturn(null);

        // When
        verificationMode.verify(data);

        // Then
        verify(delegate, never()).verify(data); // VerificationOverTimeImpl should wait for delegate to be satisfied
        try {
            Thread.sleep(VerificationOverTimeImpl.this.getDuration() + 100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        verify(delegate).verify(data);
    }

    @Test
    public void testDelegateIsNotSatisfiedAfterDelay() {
        // Given
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        VerificationData data = new VerificationData(invocation, ArgumentMatchers.any());

        when(delegate.verify(data)).thenThrow(new MockitoAssertionError("Delegate error"));

        // When
        verificationMode.verify(data);

        // Then
        try {
            Thread.sleep(VerificationOverTimeImpl.this.getDuration() + 100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        verifyZeroInteractions(delegate); // VerificationOverTimeImpl should throw DelegateError if delegate is not satisfied
    }

    @Test
    public void testSetTimer() {
        // Given
        VerificationMode verificationMode = new VerificationOverTimeImpl(100L, 500L, delegate, true, timer);

        // When

        // Then
        assertEquals(100L, verificationMode.getPollingPeriod());
        assertEquals(500L, verificationMode.getDuration());
    }

    @Test
    public void testDelegateIsSatisfiedImmediately() {
        // Given
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        VerificationData data = new VerificationData(invocation, ArgumentMatchers.any());

        when(delegate.verify(data)).thenReturn(null);
        VerificationMode verificationMode = new VerificationOverTimeImpl(0L, 0L, delegate, true, timer);

        // When
        verificationMode.verify(data);

        // Then
        verify(delegate).verify(data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativePollingPeriod() {
        // Given
        VerificationMode verificationMode = new VerificationOverTimeImpl(-1L, 1L, delegate, false, timer);

        // When & Then
        verificationMode.verify(Mockito.mock(VerificationData.class));
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDuration() {
        // Given
        VerificationMode verificationMode = new VerificationOverTimeImpl(1L, -1L, delegate, false, timer);

        // When & Then
        verificationMode.verify(Mockito.mock(VerificationData.class));
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = NullPointerException.class)
    public void testNullDelegate() {
        // Given
        VerificationMode verificationMode = new VerificationOverTimeImpl(1L, 1L, null, false, timer);

        // When & Then
        verificationMode.verify(Mockito.mock(VerificationData.class));
        fail("Expected NullPointerException");
    }

    @Test(expected = NullPointerException.class)
    public void testNullTimer() {
        // Given
        VerificationMode verificationMode = new VerificationOverTimeImpl(1L, 1L, delegate, false, null);

        // When & Then
        verificationMode.verify(Mockito.mock(VerificationData.class));
        fail("Expected NullPointerException");
    }
}