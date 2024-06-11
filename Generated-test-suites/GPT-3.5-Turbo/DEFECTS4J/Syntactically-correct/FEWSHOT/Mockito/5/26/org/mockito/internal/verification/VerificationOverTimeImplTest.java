
package org.mockito.internal.verification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.*;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerify_timeoutNotReached_returnOnSuccessTrue() throws InterruptedException {
        // Arrange
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;

        Timer timer = new Timer(durationMillis);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);

        VerificationData data = mock(VerificationData.class);

        // Act
        verificationOverTime.verify(data);

        // Assert
        verify(delegate).verify(data);
    }

    @Test
    public void testVerify_timeoutNotReached_returnOnSuccessFalse() throws InterruptedException {
        // Arrange
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = false;

        Timer timer = new Timer(durationMillis);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);

        VerificationData data = mock(VerificationData.class);

        // Act
        verificationOverTime.verify(data);

        // Assert
        verify(delegate).verify(data);
    }

    @Test(timeout = 1000)
    public void testVerify_timeoutReached_returnOnSuccessTrue() throws InterruptedException {
        // Arrange
        long pollingPeriodMillis = 100;
        long durationMillis = 100;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;

        Timer timer = new Timer(durationMillis);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);

        VerificationData data = mock(VerificationData.class);

        // Act & Assert
        try {
            verificationOverTime.verify(data);
            fail("Expected MockitoAssertionError to be thrown");
        } catch (MockitoAssertionError e) {
            // Success
        }
    }

    @Test(timeout = 1000)
    public void testVerify_timeoutReached_returnOnSuccessFalse() throws InterruptedException {
        // Arrange
        long pollingPeriodMillis = 100;
        long durationMillis = 100;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = false;

        Timer timer = new Timer(durationMillis);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);

        VerificationData data = mock(VerificationData.class);

        // Act & Assert
        try {
            verificationOverTime.verify(data);
            fail("Expected MockitoAssertionError to be thrown");
        } catch (MockitoAssertionError e) {
            // Success
        }
    }

    @Test
    public void testHandleVerifyException_canRecoverFromFailureTrue() throws InterruptedException {
        // Arrange
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;

        Timer timer = new Timer(durationMillis);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);

        AssertionError e = new MockitoAssertionError();

        // Act
        AssertionError result = verificationOverTime.handleVerifyException(e);

        // Assert
        assertEquals(e, result);
        verify(delegate, never()).verify(any(VerificationData.class));
    }

    @Test(expected = MockitoAssertionError.class)
    public void testHandleVerifyException_canRecoverFromFailureFalse() throws InterruptedException {
        // Arrange
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;

        Timer timer = new Timer(durationMillis);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);

        AssertionError e = new MockitoAssertionError();

        // Act
        verificationOverTime.handleVerifyException(e);
    }

    @Test
    public void testSleep() throws InterruptedException {
        // Arrange
        long sleep = 100;

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(0, 0, null, false);

        // Act
        long startTime = System.currentTimeMillis();
        verificationOverTime.sleep(sleep);
        long endTime = System.currentTimeMillis();

        // Assert
        long elapsedTime = endTime - startTime;
        assertEquals(sleep, elapsedTime, 20); // Allow 20ms variation in sleep time
    }

    @Test
    public void testGetPollingPeriod() {
        // Arrange
        long pollingPeriodMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, 0, delegate, false);

        // Act
        long result = verificationOverTime.getPollingPeriod();

        // Assert
        assertEquals(pollingPeriodMillis, result);
    }

    @Test
    public void testGetDuration() {
        // Arrange
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(0, durationMillis, delegate, false);

        // Act
        long result = verificationOverTime.getDuration();

        // Assert
        assertEquals(durationMillis, result);
    }

    @Test
    public void testGetDelegate() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(0, 0, delegate, false);

        // Act
        VerificationMode result = verificationOverTime.getDelegate();

        // Assert
        assertEquals(delegate, result);
    }
}
