package org.mockito.internal.verification.test;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMockException;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.VerificationWithTimeout;
import org.mockito.internal.verification.junit.ArgumentsAreDifferent;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class VerificationOverTimeImplTest {

    private VerificationMode verificationWithTimeout;
    private VerificationOverTimeImpl verificationOverTime;

    @Rule
    public MockitoRule mockitoRule = new MockitoRule();

    @Before
    public void setup() {
        verificationWithTimeout = mock(VerificationWithTimeout.class);
        verificationOverTime = new VerificationOverTimeImpl(100, 500, verificationWithTimeout, true);
    }

    @Test
    public void testVerificationWithSuccessAndLessThanDuration() {
        VerificationData verificationData = mock(VerificationData.class);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMockInvocation invocation) throws Throwable {
                // This answer simply returns and does nothing.
                return null;
            }
        }).when(verificationWithTimeout).verify(verificationData);

        verificationOverTime.verify(verificationData);
        assertTrue(verificationOverTime.isSatisfied());
    }

    @Test
    public void testVerificationWithSuccessAndExactlyDuration() {
        VerificationData verificationData = mock(VerificationData.class);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMockInvocation invocation) throws Throwable {
                // This answer simply returns and does nothing.
                return null;
            }
        }).when(verificationWithTimeout).verify(verificationData);

        long startTime = System.nanoTime();

        verificationOverTime.verify(verificationData);

        long elapsedTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
        assertEquals(500, elapsedTime);
        assertTrue(verificationOverTime.isSatisfied());
    }

    @Test
    public void testVerificationWithSuccessAndMoreThanDuration() {
        VerificationData verificationData = mock(VerificationData.class);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMockInvocation invocation) throws Throwable {
                // This answer simply returns and does nothing.
                return null;
            }
        }).when(verificationWithTimeout).verify(verificationData);

        long startTime = System.nanoTime();

        // Simulate a long running test
        Thread.sleep(1000);

        verificationOverTime.verify(verificationData);

        long elapsedTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
        assertTrue(elapsedTime > 500);
        assertTrue(verificationOverTime.isSatisfied());
    }

    @Test
    public void testVerificationWithFailureAndLessThanDuration() {
        VerificationData verificationData = mock(VerificationData.class);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMockInvocation invocation) throws Throwable {
                throw new InvocationOnMockException("Delegate verification failed.");
            }
        }).when(verificationWithTimeout).verify(verificationData);

        assertThrows(AssertionError.class, () -> verificationOverTime.verify(verificationData));
        assertNull(verificationOverTime.getError());
    }

    @Test
    public void testVerificationWithFailureAndMoreThanDuration() {
        VerificationData verificationData = mock(VerificationData.class);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMockInvocation invocation) throws Throwable {
                throw new InvocationOnMockException("Delegate verification failed.");
            }
        }).when(verificationWithTimeout).verify(verificationData);

        long startTime = System.nanoTime();

        // Simulate a long running test
        Thread.sleep(1000);

        assertThrows(AssertionError.class, () -> verificationOverTime.verify(verificationData));
        assertNull(verificationOverTime.getError());

        // Verify that the delegate verification mode was invoked multiple times
        verify(verificationWithTimeout, atLeast(1)).verify(verificationData);
        verify(verificationWithTimeout, never()).verifyNoMoreInteractions(verificationData);
    }

    @Test
    public void testVerificationWithFailureAndCanRecover() {
        VerificationData verificationData = mock(VerificationData.class);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMockInvocation invocation) throws Throwable {
                throw new InvocationOnMockException("Delegate verification failed.");
            }
        }).when(verificationWithTimeout).verify(verificationData);

        long startTime = System.nanoTime();

        // Simulate a long running test
        Thread.sleep(1000);

        assertThrows(AssertionError.class, () -> verificationOverTime.verify(verificationData));
        AssertionError error = verificationOverTime.getError();
        assertNotNull(error);
        assertEquals("Delegate verification failed.", error.getMessage());

        // Verify that the delegate verification mode was invoked multiple times
        verify(verificationWithTimeout, atLeast(1)).verify(verificationData);
    }

    @Test
    public void testVerificationWithFailureAndCannotRecover() {
        VerificationData verificationData = mock(VerificationData.class);

        VerificationMode failureMode = mock(NoMoreInteractions.class);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 500, failureMode, false);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMockInvocation invocation) throws Throwable {
                throw new InvocationOnMockException("Delegate verification failed.");
            }
        }).when(failureMode).verify(verificationData);

        long startTime = System.nanoTime();

        // Simulate a long running test
        Thread.sleep(1000);

        assertThrows(AssertionError.class, () -> verificationOverTime.verify(verificationData));
        AssertionError error = verificationOverTime.getError();
        assertNotNull(error);
        assertEquals("Delegate verification failed.", error.getMessage());

        // Verify that the delegate verification mode was invoked only once (not more than once)
        verify(failureMode, times(1)).verify(verificationData);
        verify(failureMode, never()).verifyNoMoreInteractions(verificationData);
    }

    @Test
    public void testVerificationWithTimeoutException() {
        VerificationData verificationData = mock(VerificationData.class);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMockInvocation invocation) throws Throwable {
                throw new org.mockito.exceptions.verification.junit.TimeoutException("Delegate verification timed out.");
            }
        }).when(verificationWithTimeout).verify(verificationData);

        assertThrows(org.mockito.exceptions.verification.junit.TimeoutException.class, () -> verificationOverTime.verify(verificationData));
        assertNull(verificationOverTime.getError());
    }

    @Test
    public void testVerificationWithArgumentsAreDifferentException() {
        VerificationData verificationData = mock(VerificationData.class);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMockInvocation invocation) throws Throwable {
                throw new ArgumentsAreDifferent(anyLong(), anyInt());
            }
        }).when(verificationWithTimeout).verify(verificationData);

        assertThrows(ArgumentsAreDifferent.class, () -> verificationOverTime.verify(verificationData));
        assertNull(verificationOverTime.getError());
    }

    @Test
    public void testGetPollingPeriod() {
        assertEquals(100, verificationOverTime.getPollingPeriod());
    }

    @Test
    public void testGetDuration() {
        assertEquals(500, verificationOverTime.getDuration());
    }

    @Test
    public void testGetDelegate() {
        assertEquals(verificationWithTimeout, verificationOverTime.getDelegate());
    }
}