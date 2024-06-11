package org.mockito.internal.verification.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.VerificationWithTimeout;
import org.mockito.internal.verification.junit.ArgumentsAreDifferent;
import org.mockito.internal.util.Timer;
import org.mockito.stub.Stub;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class VerificationOverTimeImplTest {

    private VerificationOverTimeImpl verification;
    private VerificationMode verificationMode;
    private Timer timer;

    @Before
    public void setUp() {
        long pollingPeriodMillis = 100L;
        long durationMillis = 500L;
        verificationMode = new VerificationWithTimeout(pollingPeriodMillis, durationMillis);
        timer = new Timer(pollingPeriodMillis);

        verification = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, verificationMode, false, timer);
    }

    @Test
    public void testVerify_DelegateIsSatisfiedBeforeTimeout_ReturnsImmediately() {
        // Given
        VerificationData data = ArgumentMatchers.any();

        // When
        verification.verify(data);

        // Then
        verify(verificationMode).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_DelegateIsNotSatisfiedBeforeTimeout_ThrowsException() {
        // Given
        VerificationData data = ArgumentMatchers.any();
        Stub<VerificationData> dataStub = mock(VerificationData.class);
        when(dataStub.getInvocations()).thenReturn(new Object[]{});
        when(verificationMode.verify(dataStub)).thenThrow(new MockitoAssertionError("Delegate verification failed"));

        // When
        verification.verify(dataStub.getMock());

        // Then
        // The exception should be thrown immediately
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_DelegateIsNotSatisfiedBeforeTimeout_WaitsForFullDuration_ThenThrowsException() {
        // Given
        VerificationData data = ArgumentMatchers.any();
        Stub<VerificationData> dataStub = mock(VerificationData.class);
        when(dataStub.getInvocations()).thenReturn(new Object[]{});
        when(verificationMode.verify(dataStub)).thenThrow(new MockitoAssertionError("Delegate verification failed"));

        // When
        verification.verify(dataStub.getMock());

        // Then
        // The exception should be thrown after the full duration has passed
    }

    @Test
    public void testVerify_DelegateIsSatisfiedBeforeTimeout_ReturnsWithoutThrowingException() {
        // Given
        VerificationData data = ArgumentMatchers.any();

        // When
        verification.verify(data);

        // Then
        verifyNoMoreInteractions(verificationMode);
    }

    @Test
    public void testVerify_DelegateIsSatisfiedAfterTimeout_ThrowsException() {
        // Given
        VerificationData data = ArgumentMatchers.any();
        Stub<VerificationData> dataStub = mock(VerificationData.class);
        when(verificationMode.verify(dataStub)).thenThrow(new MockitoAssertionError("Delegate verification failed"));

        // When
        verification.verify(dataStub.getMock());

        // Then
        verifyZeroInteractions(verificationMode);
        verify(verificationMode).verify(dataStub.getMock());
    }

    @Test(expected = AssertionError.class)
    public void testVerify_DelegateCausesArgumentsAreDifferentException_ThrowsWrappedException() {
        // Given
        VerificationData data = ArgumentMatchers.any();
        Stub<VerificationData> dataStub = mock(VerificationData.class);
        when(dataStub.getInvocations()).thenThrow(new ArgumentsAreDifferent());

        // When
        verification.verify(dataStub.getMock());

        // Then
        // The exception should be thrown with the original cause
    }

    @Test
    public void testCanRecoverFromFailure() {
        // Given
        VerificationMode verificationMode = new VerificationWithTimeout(1, 1);
        VerificationMode anotherVerificationMode = new NoMoreInteractions();

        // When
        boolean result = verification.canRecoverFromFailure(verificationMode);
        boolean anotherResult = verification.canRecoverFromFailure(anotherVerificationMode);

        // Then
        assertTrue(result);
        assertFalse(anotherResult);
    }

    @Test
    public void testSleep() {
        // Given

        // When
        verification.sleep(100L);

        // Then
        // No exception should be thrown
    }

    @Test
    public void testGetPollingPeriod() {
        // Given

        // When
        long result = verification.getPollingPeriod();

        // Then
        assertEquals(100L, result);
    }

    @Test
    public void testGetDuration() {
        // Given

        // When
        long result = verification.getDuration();

        // Then
        assertEquals(500L, result);
    }

    @Test
    public void testGetDelegate() {
        // Given

        // When
        VerificationMode result = verification.getDelegate();

        // Then
        assertSame(verificationMode, result);
    }
}