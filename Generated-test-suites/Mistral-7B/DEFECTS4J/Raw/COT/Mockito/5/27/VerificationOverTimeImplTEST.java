package org.mockito.internal.verification.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Stubber;

import java.util.concurrent.TimeUnit;

public class VerificationOverTimeImplTest {

    private VerificationMode verificationMode;
    private VerificationOverTimeImpl verificationOverTime;
    private Stubber stubber;

    @Before
    public void setUp() {
        verificationMode = mock(VerificationMode.class);
        verificationOverTime = new VerificationOverTimeImpl(100L, 500L, verificationMode, true);
        stubber = stub(verificationMode);
    }

    @Test
    public void testVerify_DelegateIsSatisfiedBeforeTimeout_ReturnsImmediately() {
        // Given
        VerificationData verificationData = createVerificationData();

        // When
        verificationOverTime.verify(verificationData);

        // Then
        verify(verificationMode).verify(verificationData);
    }

    @Test
    public void testVerify_DelegateIsSatisfiedBeforeTimeoutWithSleeps_DoesNotSleep() {
        // Given
        VerificationData verificationData = createVerificationData();

        // When
        verificationOverTime.verify(verificationData);

        // Then
        verifyNoMoreInteractions(verificationMode);
        verifyNoMoreInteractions(timer());
    }

    @Test
    public void testVerify_DelegateIsSatisfiedWithinDuration_ThrowsException() {
        // Given
        VerificationData verificationData = createVerificationData();

        // When / Then
        assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(verificationData));
    }

    @Test
    public void testVerify_DelegateIsSatisfiedWithinDurationAndCanRecover_ThrowsLastException() {
        // Given
        VerificationData verificationData = createVerificationData();
        doThrow(new MockitoAssertionError("Delegate error")).when(verificationMode).verify(verificationData);

        // When
        verificationOverTime.verify(verificationData);

        // Then
        AssertionError actualError = assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(verificationData));
        assertThat(actualError.getMessage(), containsString("Delegate error"));
    }

    @Test
    public void testVerify_DelegateIsSatisfiedWithinDurationAndCannotRecover_ThrowsLastException() {
        // Given
        VerificationData verificationData = createVerificationData();
        doThrow(new MockitoAssertionError("Delegate error")).when(verificationMode).verify(verificationData);

        // When
        verificationOverTime.verify(verificationData);

        // Then
        AssertionError actualError = assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(verificationData));
        assertThat(actualError.getMessage(), containsString("Delegate error"));
    }

    @Test
    public void testVerify_DelegateIsNeverSatisfied_ThrowsException() {
        // Given
        VerificationData verificationData = createVerificationData();

        doNothing().doThrow(new MockitoAssertionError("Delegate error")).when(verificationMode).verify(verificationData);

        // When / Then
        assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(verificationData));
    }

    @Test
    public void testVerify_DelegateIsNeverSatisfiedAndCanRecover_ThrowsLastException() {
        // Given
        VerificationData verificationData = createVerificationData();

        doNothing().doThrow(new MockitoAssertionError("Delegate error")).when(verificationMode).verify(verificationData);

        // When
        verificationOverTime.verify(verificationData);

        // Then
        AssertionError actualError = assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(verificationData));
        assertThat(actualError.getMessage(), containsString("Delegate error"));
    }

    @Test
    public void testGetPollingPeriod() {
        // When
        long pollingPeriod = verificationOverTime.getPollingPeriod();

        // Then
        assertEquals(100L, pollingPeriod);
    }

    @Test
    public void testGetDuration() {
        // When
        long duration = verificationOverTime.getDuration();

        // Then
        assertEquals(500L, duration);
    }

    @Test
    public void testGetDelegate() {
        // When
        VerificationMode delegate = verificationOverTime.getDelegate();

        // Then
        assertEquals(verificationMode, delegate);
    }

    private VerificationData createVerificationData() {
        return new VerificationData(any(Class.class), any(InvocationOnMock.class));
    }

    private Timer timer() {
        return verificationOverTime.timer;
    }
}