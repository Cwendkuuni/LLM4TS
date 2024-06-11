package org.mockito.internal.verification.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.VerificationWithTimeout;
import org.mockito.internal.verification.VerificationAfterDelay;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.internal.verification.VerificationModeFactory.timeout;
import static org.mockito.internal.verification.VerificationModeFactory.delay;

public class VerificationOverTimeImplTest {

    private VerificationMode timeoutVerificationMode;
    private VerificationMode delayVerificationMode;
    private VerificationOverTimeImpl verificationOverTime;

    @Before
    public void setUp() {
        timeoutVerificationMode = timeout(1, TimeUnit.SECONDS);
        delayVerificationMode = delay(1, TimeUnit.SECONDS);
        verificationOverTime = new VerificationOverTimeImpl(500, 1000, timeoutVerificationMode, true);
    }

    @Test(expected = MockitoAssertionError.class)
    public void shouldThrowErrorWhenDelegateVerificationFailsAndCannotRecover() {
        // given
        VerificationData verificationData = mock(VerificationData.class);

        // when
        verificationOverTime.verify(verificationData);

        // then
        then(timeoutVerificationMode).verify(ArgumentMatchers.eq(verificationData));
        verifyNoMoreInteractions(timeoutVerificationMode);
    }

    @Test(expected = MockitoAssertionError.class)
    public void shouldThrowErrorWhenDelegateVerificationFailsAndCannotRecoverWithDelayVerificationMode() {
        // given
        VerificationData verificationData = mock(VerificationData.class);

        VerificationOverTimeImpl verificationOverTimeWithDelay = new VerificationOverTimeImpl(500, 1000, delayVerificationMode, false);

        // when
        verificationOverTimeWithDelay.verify(verificationData);

        // then
        then(delayVerificationMode).verify(ArgumentMatchers.eq(verificationData));
        verifyNoMoreInteractions(delayVerificationMode);
    }

    @Test
    public void shouldSatisfyDelegateVerificationModeAndReturnImmediately() {
        // given
        VerificationData verificationData = mock(VerificationData.class);

        // when
        doReturn(null).when(timeoutVerificationMode).verify(verificationData);

        verificationOverTime.verify(verificationData);

        // then
        verify(timeoutVerificationMode).verify(verificationData);
        verify(timeoutVerificationMode, never()).verifyNoMoreInteractions();
    }

    @Test
    public void shouldWaitForDelegateVerificationModeToSatisfyAndReturnSuccess() {
        // given
        VerificationData verificationData = mock(VerificationData.class);
        InvocationOnMock invocationOnMock = mock(InvocationOnMock.class);
        Answer<Void> answer = arg -> {
            throw new RuntimeException("Delegate verification mode could not be satisfied");
        };

        doAnswer(answer).when(timeoutVerificationMode).verify(verificationData);

        // when
        verificationOverTime.verify(verificationData);

        // then
        verify(timeoutVerificationMode).verify(verificationData);
        verify(timeoutVerificationMode, never()).verifyNoMoreInteractions();

        // When the delegate verification mode is modified to be satisfied, the test should pass
        doReturn(null).when(timeoutVerificationMode).verify(verificationData);

        verificationOverTime.verify(verificationData);
        verifyNoMoreInteractions(verificationOverTime);
    }
}