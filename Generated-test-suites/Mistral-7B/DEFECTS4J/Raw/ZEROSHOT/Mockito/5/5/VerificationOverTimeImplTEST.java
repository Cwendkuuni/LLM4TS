package org.mockito.internal.verification.test;

import static org.mockito.Mockito.*; // for importing static verifications
import static org.mockito.internal.verification.api.Invocations.*; // for important verification data
import static org.mockito.internal.verification.api.VerificationDataBuilder.*;
import static org.mockito.internal.verification.api.VerificationModeBuilder.*;
import static org.mockito.internal.verification.VerificationOverTimeImpl.VerificationModeType.*;
import static org.mockito.library.util.ReflectionUtils.*;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationDataBuilder;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationModeBuilder;
import org.mockito.internal.verification.api.VerificationModeType;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.VerificationWithTimeoutImpl;
import org.mockito.internal.verification.api.Timer;
import org.mockito.internal.verification.VerificationModeDelegate;

public class VerificationOverTimeImplTest {

    private VerificationMode verificationMode;
    private VerificationModeDelegate verificationModeDelegate;
    private Timer timer;

    @Before
    public void setup() throws Exception {
        long pollingPeriodMillis = 10;
        long durationMillis = 100;
        VerificationModeType verificationModeType = MOCKED;
        verificationModeDelegate = mock(VerificationModeDelegate.class);
        timer = new Timer(durationMillis);

        Class<VerificationOverTimeImpl> clazz = VerificationOverTimeImpl.class;
        Field pollingPeriodField = clazz.getDeclaredField("pollingPeriodMillis");
        pollingPeriodField.setAccessible(true);

        Field durationField = clazz.getDeclaredField("durationMillis");
        durationField.setAccessible(true);

        Field delegateField = clazz.getDeclaredField("delegate");
        delegateField.setAccessible(true);

        Field returnOnSuccessField = clazz.getDeclaredField("returnOnSuccess");
        returnOnSuccessField.setAccessible(true);

        Field timerField = clazz.getDeclaredField("timer");
        timerField.setAccessible(true);

        verificationMode = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis,
                verificationModeDelegate, true, timer);
        verificationMode.getClass(); // initialize the timer
    }

    @Test
    public void testSuccessfulVerification() throws Exception {
        // given
        VerificationData verificationData = verificationData(any(), eq("expected"));

        // when
        verificationMode.verify(verificationData);

        // then
        verify(verificationModeDelegate).verify(verificationData);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testUnsuccessfulVerificationWithAtMost() throws Exception {
        // given
        VerificationData verificationData = verificationData(any(), eq("expected"));
        VerificationMode verificationModeWithAtMost = new VerificationWithTimeoutImpl(
                1, durationMillis, verificationModeDelegate);
        when(verificationModeDelegate.canRecoverFromFailure(any())).thenReturn(false);

        // when - should throw
        verificationModeWithAtMost.verify(verificationData);

        // then
        verify(verificationModeDelegate).verify(verificationData);
        verify(verificationModeDelegate, times(1)).verify(verificationData);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testUnsuccessfulVerificationWithNoMoreInteractions() throws Exception {
        // given
        VerificationData verificationData = verificationData(any(), eq("expected"));
        VerificationMode verificationModeWithNoMoreInteractions = new VerificationModeNoMoreInteractionsImpl(
                verificationModeDelegate);
        when(verificationModeDelegate.canRecoverFromFailure(any())).thenReturn(false);

        // when - should throw
        verificationModeWithNoMoreInteractions.verify(verificationData);

        // then
        verify(verificationModeDelegate, never()).verify(verificationData);
    }

    @Test
    public void testSuccessfulVerificationWithTimeout() throws Exception {
        // given
        VerificationData verificationData = verificationData(any(), eq("expected"));

        // when
        long startTime = System.currentTimeMillis();
        verificationMode.verify(verificationData);
        long endTime = System.currentTimeMillis();

        // then
        long duration = endTime - startTime;
        Assert.assertTrue("Verification took too long: " + duration + "ms", duration <= durationMillis);
        verify(verificationModeDelegate).verify(verificationData);
        verifyNoMoreInteractions(verificationModeDelegate);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testUnsuccessfulVerificationWithTimeout() throws Exception {
        // given
        VerificationData verificationData = verificationData(any(), eq("expected"));

        // when - should throw
        long startTime = System.currentTimeMillis();
        verificationMode.verify(verificationData);

        // then
        long duration = System.currentTimeMillis() - startTime;
        Assert.assertTrue("Verification took too long: " + duration + "ms", duration > durationMillis);
        verify(verificationModeDelegate).verify(verificationData);
        verifyNoMoreInteractions(verificationModeDelegate);
    }

    @Test
    public void testReturnOnSuccess() throws Exception {
        // given
        VerificationData verificationData = verificationData(any(), eq("expected"));
        doReturn(true).when(verificationModeDelegate).canRecoverFromFailure(any());

        // when
        verificationMode.verify(verificationData);

        // then
        verifyNoMoreInteractions(verificationModeDelegate);
    }

    @Test
    public void testWaitForSuccessWithTimeout() throws Exception {
        // given
        VerificationData verificationData = verificationData(any(), eq("expected"));
        doReturn(false).when(verificationModeDelegate).canRecoverFromFailure(any());

        // when
        long startTime = System.currentTimeMillis();
        verificationMode.verify(verificationData);
        long endTime = System.currentTimeMillis();

        // then
        long duration = endTime - startTime;
        Assert.assertTrue("Verification took too long: " + duration + "ms", duration <= durationMillis);
        verify(verificationModeDelegate).verify(verificationData);
        verifyNoMoreInteractions(verificationModeDelegate);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testWaitForSuccessWithTimeoutAndFailure() throws Exception {
        // given
        VerificationData verificationData = verificationData(any(), eq("expected"));
        doReturn(false).when(verificationModeDelegate).canRecoverFromFailure(any());

        // when - should throw
        long startTime = System.currentTimeMillis();
        verificationMode.verify(verificationData);

        // then
        long duration = System.currentTimeMillis() - startTime;
        Assert.assertTrue("Verification took too long: " + duration + "ms", duration > durationMillis);
        verify(verificationModeDelegate).verify(verificationData);
        verifyNoMoreInteractions(verificationModeDelegate);
    }
}