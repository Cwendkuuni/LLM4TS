package org.mockito.internal.verification;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.VerificationWithTimeout;
import org.mockito.internal.verification.api.Invocation;
import org.mockito.internal.verification.api.InvocationData;
import org.mockito.internal.verification.api.Invocations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class VerificationOverTimeImplTest {
    private static final long POLLING_PERIOD_MILLIS = 10;
    private static final long DURATION_MILLIS = 200;
    private static final long TIMEOUT_MILLIS = 300;
    private VerificationData data;
    private VerificationMode verificationMode;
    private VerificationOverTimeImpl verificationOverTime;
    private Timer timer;

    @Before
    public void setUp() throws Exception {
        timer = mock(Timer.class);
        verificationMode = mock(VerificationMode.class);
        data = new VerificationData(mock(InvocationData.class), Invocations.mocked(Invocation.class));
        verificationOverTime = new VerificationOverTimeImpl(POLLING_PERIOD_MILLIS, DURATION_MILLIS, verificationMode, true, timer);
    }

    @After
    public void tearDown() throws Exception {
        verificationOverTime = null;
        verificationMode = null;
        data = null;
        timer = null;
    }

    @Test
    public void testVerify_VerificationModeIsSatisfiedBeforeTimeout() {
        // given
        long start = System.currentTimeMillis();

        // when
        verificationOverTime.verify(data);

        // then
        verify(verificationMode).verify(data);
        assertTrue(System.currentTimeMillis() - start < DURATION_MILLIS); // assert that the verification didn't take too long
    }

    @Test
    public void testVerify_VerificationModeIsSatisfiedAfterTimeout() {
        // given
        doNothing().when(verificationMode).verify(data);
        doThrow(new MockitoAssertionError("Expected exception")).when(verificationMode).verify(data);
        long start = System.currentTimeMillis();

        // when
        verificationOverTime.verify(data);

        // then
        verify(verificationMode).verify(data); // this should be called after the timeout
        assertTrue(System.currentTimeMillis() - start > DURATION_MILLIS); // assert that the verification took longer than expected
    }

    @Test
    public void testVerify_VerificationModeIsNotSatisfiedBeforeTimeout() {
        // given
        doNothing().when(verificationMode).verify(data);

        // when
        verificationOverTime.verify(data);

        // then
        verify(verificationMode, never()).verify(data);
        assertFalse(System.out.toString().contains("Expected exception")); // assert that the verification exception wasn't printed to console
    }

    @Test
    public void testVerify_VerificationModeIsNotSatisfiedAfterTimeout() {
        // given
        doThrow(new MockitoAssertionError("Expected exception")).when(verificationMode).verify(data);
        long start = System.currentTimeMillis();

        // when
        verificationOverTime.verify(data);

        // then
        verify(verificationMode).verify(data); // this should be called after the timeout
        assertTrue(System.currentTimeMillis() - start > DURATION_MILLIS); // assert that the verification took longer than expected
        assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(data)); // assert that an exception is thrown
    }

    @Test
    public void testVerify_VerificationModeIsSatisfiedAndReturnOnSuccessIsFalse() {
        // given
        long start = System.currentTimeMillis();

        // when
        verificationOverTime.verify(data);

        // then
        verify(verificationMode, never()).verify(data); // the verification shouldn't be satisfied yet
        assertTrue(System.currentTimeMillis() - start < DURATION_MILLIS); // assert that the verification didn't take too long
    }

    @Test
    public void testVerify_VerificationModeIsSatisfiedAndReturnOnSuccessIsTrue() {
        // given
        long start = System.currentTimeMillis();

        // when
        verificationOverTime.verify(data);

        // then
        verify(verificationMode).verify(data); // this should be called immediately if returnOnSuccess is true
        assertTrue(System.currentTimeMillis() - start < DURATION_MILLIS); // assert that the verification didn't take too long
    }

    @Test
    public void testVerify_VerificationModeThrowsRecoverableFailure() {
        // given
        doThrow(new AssertionError("Recoverable error")).when(verificationMode).verify(data);

        // when
        verificationOverTime.verify(data);

        // then
        verify(verificationMode).verify(data); // this should be called again after a sleep
    }

    @Test
    public void testVerify_VerificationModeThrowsNonRecoverableFailure() {
        // given
        doThrow(new MockitoAssertionError("Non-recoverable error")).when(verificationMode).verify(data);

        // when
        assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(data));

        // then
        verify(verificationMode, never()).verify(data); // this should never be called
    }

    @Test
    public void testVerify_Getters() {
        // given
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(POLLING_PERIOD_MILLIS, DURATION_MILLIS, verificationMode, true, timer);

        // when

        // then
        assertEquals(POLLING_PERIOD_MILLIS, verificationOverTime.getPollingPeriod());
        assertEquals(DURATION_MILLIS, verificationOverTime.getDuration());
        assertSame(verificationMode, verificationOverTime.getDelegate());
    }

    @Test
    public void testHandleVerifyException_RecoverableFailure() {
        // given
        MockitoAssertionError expectedException = mock(MockitoAssertionError.class);

        // when
        MockitoAssertionError actualException = verificationOverTime.handleVerifyException(expectedException);

        // then
        assertSame(expectedException, actualException);
    }

    @Test
    public void testHandleVerifyException_NonRecoverableFailure() {
        // given
        MockitoAssertionError expectedException = mock(MockitoAssertionError.class);

        // when
        MockitoAssertionError actualException = verificationOverTime.handleVerifyException(expectedException);

        // then
        assertNotSame(expectedException, actualException);
        assertTrue(actualException.getMessage().contains(" repeatedly"));
    }

    @Test
    public void testCanRecoverFromFailure_VerificationModeThatCanRecover() {
        // given
        VerificationWithTimeout verificationMode = mock(VerificationWithTimeout.class);

        // when
        assertTrue(verificationOverTime.canRecoverFromFailure(verificationMode));
    }

    @Test
    public void testCanRecoverFromFailure_VerificationModeThatCanNotRecover() {
        // given
        AtMost verificationMode = mock(AtMost.class);

        // when
        assertFalse(verificationOverTime.canRecoverFromFailure(verificationMode));
    }

    @Test
    public void testSleep() throws InterruptedException {
        // given
        long sleepTime = 20;

        // when
        verificationOverTime.sleep(sleepTime);

        // then
        Thread.sleep(sleepTime);
    }
}