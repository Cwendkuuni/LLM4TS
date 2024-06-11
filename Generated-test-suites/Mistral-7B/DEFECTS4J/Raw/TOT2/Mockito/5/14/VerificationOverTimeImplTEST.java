package org.mockito.internal.verification.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.verification.VerificationMode;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {
    private VerificationMode verificationModeMock;
    private VerificationData verificationDataMock;
    private VerificationOverTimeImpl verificationOverTimeImpl;

    @Before
    public void setup() {
        verificationModeMock = spy(mock(VerificationMode.class));
        verificationDataMock = mock(VerificationData.class);
        verificationOverTimeImpl = new VerificationOverTimeImpl(100, 500, verificationModeMock, true);
    }

    @Test
    public void testVerifySuccess() {
        verify(verificationModeMock).verify(verificationDataMock);
        verificationOverTimeImpl.verify(verificationDataMock);
    }

    @Test
    public void testVerifyFailure() {
        doThrow(new MockitoAssertionError("Expected exception")).when(verificationModeMock).verify(verificationDataMock);

        verify(verificationModeMock).verify(verificationDataMock);
        verificationOverTimeImpl.verify(verificationDataMock);
    }

    @Test
    public void testVerifySuccessAndSleep() throws InterruptedException {
        verify(verificationModeMock).verify(verificationDataMock);
        verificationOverTimeImpl.verify(verificationDataMock);
        TimeUnit.MILLISECONDS.sleep(101);
    }

    @Test
    public void testVerifyTimeout() throws InterruptedException {
        doThrow(new MockitoAssertionError("Expected exception")).when(verificationModeMock).verify(verificationDataMock);

        verificationOverTimeImpl.verify(verificationDataMock);
        TimeUnit.SECONDS.sleep(1);
        verifyZeroInteractions(verificationModeMock);
    }

    @Test
    public void testVerifyMultipleInvocations() throws InterruptedException {
        doAnswer(invocation -> {
            throw new MockitoAssertionError("Expected exception");
        }).when(verificationModeMock).verify(verificationDataMock);

        verificationOverTimeImpl.verify(verificationDataMock);
        verificationOverTimeImpl.verify(verificationDataMock);
        TimeUnit.SECONDS.sleep(1);
        verifyZeroInteractions(verificationModeMock);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyTimeoutWithAtMost() throws InterruptedException {
        VerificationMode atMostVerificationModeMock = mock(VerificationMode.class);
        when(atMostVerificationModeMock.verify(verificationDataMock)).thenThrow(new MockitoAssertionError("Expected exception"));

        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(100, 50, atMostVerificationModeMock, true);

        verificationOverTimeImpl.verify(verificationDataMock);
        TimeUnit.MILLISECONDS.sleep(101);
        verifyZeroInteractions(atMostVerificationModeMock);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyTimeoutWithNoMoreInteractions() throws InterruptedException {
        VerificationMode noMoreInteractionsVerificationModeMock = mock(VerificationMode.class);
        when(noMoreInteractionsVerificationModeMock.verify(verificationDataMock)).thenThrow(new MockitoAssertionError("Expected exception"));

        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(100, 50, noMoreInteractionsVerificationModeMock, true);

        verificationOverTimeImpl.verify(verificationDataMock);
        verificationOverTimeImpl.verify(verificationDataMock);
        TimeUnit.MILLISECONDS.sleep(101);
        verifyZeroInteractions(noMoreInteractionsVerificationModeMock);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyNegativePollingPeriod() {
        try {
            new VerificationOverTimeImpl(-100, 500, verificationModeMock, true);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyNegativeDuration() {
        try {
            new VerificationOverTimeImpl(100, -500, verificationModeMock, true);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyNullDelegate() {
        try {
            new VerificationOverTimeImpl(100, 500, null, true);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyNullVerificationData() {
        try {
            verificationOverTimeImpl.verify(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}