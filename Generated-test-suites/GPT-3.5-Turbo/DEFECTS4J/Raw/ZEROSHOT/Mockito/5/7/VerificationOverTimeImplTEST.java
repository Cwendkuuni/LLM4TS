
package org.mockito.internal.verification;

import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockito.internal.verification.NoMoreInteractions;

import static org.mockito.Mockito.mock;

public class VerificationOverTimeImplTest {

    private VerificationOverTimeImpl verificationOverTimeImpl;
    private VerificationData data;
    private VerificationMode delegate;

    @Before
    public void setUp() {
        long pollingPeriodMillis = 1000;
        long durationMillis = 5000;
        delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        Timer timer = mock(Timer.class);
        verificationOverTimeImpl = new VerificationOverTimeImpl(
                pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
        data = mock(VerificationData.class);
    }

    @Test
    public void testVerify_SuccessBeforeDuration_ReturnImmediately() {
        Mockito.doNothing().when(delegate).verify(data);

        verificationOverTimeImpl.verify(data);

        Mockito.verify(delegate).verify(data);
    }

    @Test
    public void testVerify_SuccessBeforeDuration_WaitForFullDuration() {
        Mockito.doNothing().when(delegate).verify(data);

        verificationOverTimeImpl = new VerificationOverTimeImpl(
                1000, 5000, delegate, false, mock(Timer.class));

        verificationOverTimeImpl.verify(data);

        Mockito.verify(delegate).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_DelegateDoesNotSatisfyBeforeTimeout_ThrowAssertionError() {
        Mockito.doThrow(MockitoAssertionError.class).when(delegate).verify(data);

        verificationOverTimeImpl.verify(data);
    }

    @Test(expected = ArgumentsAreDifferent.class)
    public void testVerify_DelegateThrowsExceptionBeforeTimeout_ThrowArgumentsAreDifferent() {
        Mockito.doThrow(ArgumentsAreDifferent.class).when(delegate).verify(data);

        verificationOverTimeImpl.verify(data);
    }

    @Test
    public void testCanRecoverFromFailure_DelegateIsNotAtMostOrNoMoreInteractions_ReturnTrue() {
        VerificationMode verificationMode = mock(VerificationMode.class);

        boolean recoverable = verificationOverTimeImpl.canRecoverFromFailure(verificationMode);

        Assert.assertTrue(recoverable);
    }

    @Test
    public void testCanRecoverFromFailure_DelegateIsAtMost_ReturnFalse() {
        VerificationMode verificationMode = mock(VerificationMode.class);
        Mockito.when(verificationMode instanceof AtMost).thenReturn(true);

        boolean recoverable = verificationOverTimeImpl.canRecoverFromFailure(verificationMode);

        Assert.assertFalse(recoverable);
    }

    @Test
    public void testCanRecoverFromFailure_DelegateIsNoMoreInteractions_ReturnFalse() {
        VerificationMode verificationMode = mock(VerificationMode.class);
        Mockito.when(verificationMode instanceof NoMoreInteractions).thenReturn(true);

        boolean recoverable = verificationOverTimeImpl.canRecoverFromFailure(verificationMode);

        Assert.assertFalse(recoverable);
    }

    @Test
    public void testSleep() {
        long sleepTime = 1000;

        verificationOverTimeImpl.sleep(sleepTime);

        Mockito.verify(Thread.class).sleep(sleepTime);
    }

    @Test
    public void testGetPollingPeriod() {
        long expectedPollingPeriodMillis = 1000;

        long pollingPeriodMillis = verificationOverTimeImpl.getPollingPeriod();

        Assert.assertEquals(expectedPollingPeriodMillis, pollingPeriodMillis);
    }

    @Test
    public void testGetDuration() {
        long expectedDurationMillis = 5000;

        long durationMillis = verificationOverTimeImpl.getDuration();

        Assert.assertEquals(expectedDurationMillis, durationMillis);
    }

    @Test
    public void testGetDelegate() {
        VerificationMode actualDelegate = verificationOverTimeImpl.getDelegate();

        Assert.assertEquals(delegate, actualDelegate);
    }
}
