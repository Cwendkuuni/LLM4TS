package org.mockito.internal.verification.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.verification.VerificationMode;
import org.mockito.verification.VerificationModeFactory;

import java.util.stream.Stream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

public class VerificationOverTimeImplTest {

    private VerificationMode verificationMode;
    private VerificationData verificationData;

    @BeforeEach
    public void setup() {
        VerificationModeFactory verificationModeFactory = Mockito.mock(VerificationModeFactory.class);
        verificationMode = new VerificationOverTimeImpl(100, 500, verificationModeFactory, true, new Timer(500));
        verificationData = Mockito.mock(VerificationData.class);
    }

    @Test
    public void testVerification_DelegateIsSatisfiedBeforeTimeout() {
        // Given
        Mockito.when(verificationModeFactory.createVerificationMode(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong(), ArgumentMatchers.any(VerificationMode.class), ArgumentMatchers.anyBoolean())).thenReturn(Mockito.mock(VerificationMode.class));

        // When
        verificationMode.verify(verificationData);

        // Then
        Mockito.verify(verificationModeFactory, times(1)).createVerificationMode(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong(), ArgumentMatchers.any(VerificationMode.class), ArgumentMatchers.anyBoolean());
        Mockito.verifyNoMoreInteractions(verificationModeFactory);
    }

    @Test
    public void testVerification_DelegateIsSatisfiedAfterTimeout() {
        // Given
        Mockito.when(verificationModeFactory.createVerificationMode(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong(), ArgumentMatchers.any(VerificationMode.class), ArgumentMatchers.anyBoolean())).thenThrow(new MockitoAssertionError("Delegate verification failed."));

        // When & Then
        Assertions.assertThrows(MockitoAssertionError.class, () -> verificationMode.verify(verificationData));
    }

    @Test
    public void testVerification_DelegateIsSatisfiedOnceAndReturnOnSuccess() {
        // Given
        Mockito.when(verificationModeFactory.createVerificationMode(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong(), ArgumentMatchers.any(VerificationMode.class), ArgumentMatchers.anyBoolean())).thenAnswer(invocation -> {
            InvocationOnMock invocationMock = (InvocationOnMock) invocation.getArguments()[0];
            Mockito.verify(invocationMock, times(1)).someMethod();
            return Mockito.mock(VerificationMode.class);
        });

        // When
        verificationMode.verify(verificationData);

        // Then
        Mockito.verify(verificationMode, times(1)).verify(verificationData);
    }

    @Test
    public void testVerification_DelegateIsNotSatisfiedOnceAndReturnOnSuccess() {
        // Given
        Mockito.when(verificationModeFactory.createVerificationMode(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong(), ArgumentMatchers.any(VerificationMode.class), ArgumentMatchers.anyBoolean())).thenAnswer(invocation -> {
            InvocationOnMock invocationMock = (InvocationOnMock) invocation.getArguments()[0];
            Mockito.never(invocationMock, ArgumentMatchers.any());
            return null;
        });

        // When & Then
        Assertions.assertThrows(MockitoAssertionError.class, () -> verificationMode.verify(verificationData));
    }

    @Test
    public void testVerification_DelegateIsSatisfiedAndFullDurationPassed() {
        // Given
        Mockito.when(verificationModeFactory.createVerificationMode(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong(), ArgumentMatchers.any(VerificationMode.class), ArgumentMatchers.anyBoolean())).thenThrow(new MockitoAssertionError("Delegate verification failed."));

        // When
        verificationMode.verify(verificationData);

        // Then
        Mockito.verify(verificationMode, times(1)).verify(verificationData);
    }

    @ParameterizedTest
    @MethodSource("providePollingPeriodsAndDurations")
    public void testVerification_PollingPeriodAndDuration(long pollingPeriod, long duration) {
        // Given
        VerificationMode verificationMode = new VerificationOverTimeImpl(pollingPeriod, duration, Mockito.mock(VerificationMode.class), false, new Timer(duration));

        // When & Then
        Assertions.assertEquals(pollingPeriod, verificationMode.getPollingPeriod());
        Assertions.assertEquals(duration, verificationMode.getDuration());
    }

    private static Stream<Arguments> providePollingPeriodsAndDurations() {
        return Stream.of(
                Arguments.of(100, 500),
                Arguments.of(50, 100),
                Arguments.of(1000, 5000)
        );
    }
}