package org.mockito.internal.verification.VerificationOverTimeImplTest;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationData;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.invocation.InvocationOnMock;

public class VerificationOverTimeImplTest {

    @Test
    public void testConstructor() {
        // Given
        VerificationMode delegateMock = Mockito.mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        long pollingPeriodMillis = 100L;
        long durationMillis = 500L;

        // When
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegateMock, returnOnSuccess);

        // Then
        VerificationData verificationData = Mockito.mock(VerificationData.class);
        verificationMode.verify(verificationData);
        Mockito.verify(delegateMock, Mockito.times(1)).verify(ArgumentMatchers.eq(verificationData));

        // Edge case: pollingPeriodMillis <= 0 or durationMillis <= 0
        // Throws: IllegalArgumentException

        // Error scenario: delegate is an instance of AtMost or NoMoreInteractions
        // Throws: MockitoAssertionError
    }

    @Test
    public void testVerify() {
        // Given
        VerificationMode delegateMock = Mockito.mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        long pollingPeriodMillis = 100L;
        long durationMillis = 500L;

        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegateMock, returnOnSuccess);

        VerificationData verificationData = Mockito.mock(VerificationData.class);

        // When
        verificationMode.verify(verificationData);

        // Then
        Mockito.verify(delegateMock, Mockito.times(1)).verify(ArgumentMatchers.eq(verificationData));

        // Edge case: Verify data is null
        // Throws: NullPointerException

        // Error scenario: Delegate verification fails and cannot be recovered from
        // Throws: MockitoAssertionError
    }

    @Test
    public void testHandleVerifyException() {
        // Given
        VerificationMode delegateMock = Mockito.mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        long pollingPeriodMillis = 100L;
        long durationMillis = 500L;

        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegateMock, returnOnSuccess);

        VerificationData verificationData = Mockito.mock(VerificationData.class);

        Mockito.doThrow(new AssertionError("Mockito error")).when(delegateMock).verify(verificationData);

        // When / Then
        try {
            verificationMode.verify(verificationData);
            fail("Should have thrown an exception");
        } catch (AssertionError e) {
            Assert.assertEquals("Mockito error", e.getMessage());

            // Edge case: Can recover from failure
            // PollingPeriodMillis > 0
            sleep(pollingPeriodMillis);

            // Verify data is not null
            verificationMode.verify(verificationData);
        }
    }
}