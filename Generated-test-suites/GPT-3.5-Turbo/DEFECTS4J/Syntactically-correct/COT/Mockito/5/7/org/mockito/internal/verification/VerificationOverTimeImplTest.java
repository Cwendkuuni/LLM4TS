import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerify() {
        long pollingPeriodMillis = 1000;
        long durationMillis = 5000;
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        Timer timer = new Timer(durationMillis);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);

        VerificationData data = Mockito.mock(VerificationData.class);

        verificationOverTime.verify(data);

        Mockito.verify(delegate).verify(data);
    }

    @Test
    public void testHandleVerifyException() {
        long pollingPeriodMillis = 1000;
        long durationMillis = 5000;
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        Timer timer = new Timer(durationMillis);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);

        AssertionError exception = new MockitoAssertionError("Assertion Error");
        AssertionError result = verificationOverTime.handleVerifyException(exception);

        Mockito.verify(delegate, Mockito.never()).verify(Mockito.any(VerificationData.class));
        Mockito.verifyZeroInteractions(data);

        assertEquals(exception, result);
    }

    @Test(expected = ArgumentsAreDifferent.class)
    public void testVerifyArgumentsAreDifferentException() {
        long pollingPeriodMillis = 1000;
        long durationMillis = 5000;
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        Timer timer = new Timer(durationMillis);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);

        VerificationData data = new VerificationData();

        Mockito.doThrow(ArgumentsAreDifferent.class).when(delegate).verify(data);

        verificationOverTime.verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyTimeout() {
        long pollingPeriodMillis = 1000;
        long durationMillis = 5000;
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        boolean returnOnSuccess = false;
        Timer timer = new Timer(durationMillis);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);

        VerificationData data = new VerificationData();

        Mockito.doThrow(MockitoAssertionError.class).when(delegate).verify(data);

        verificationOverTime.verify(data);
    }

    @Test
    public void testCanRecoverFromFailureWithAtMostVerificationMode() {
        long pollingPeriodMillis = 1000;
        long durationMillis = 5000;
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        Timer timer = new Timer(durationMillis);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);

        boolean result = verificationOverTime.canRecoverFromFailure(new AtMost());

        assertFalse(result);
    }

    @Test
    public void testCanRecoverFromFailureWithNoMoreInteractionsVerificationMode() {
        long pollingPeriodMillis = 1000;
        long durationMillis = 5000;
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        Timer timer = new Timer(durationMillis);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);

        boolean result = verificationOverTime.canRecoverFromFailure(new NoMoreInteractions());

        assertFalse(result);
    }

    @Test
    public void testSleep() {
        long sleep = 1000;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl();

        verificationOverTime.sleep(sleep);

        // Verify that the sleep method is called without throwing exception
    }

    @Test
    public void testGetPollingPeriod() {
        long pollingPeriodMillis = 1000;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, 5000, Mockito.mock(VerificationMode.class), true);

        long result = verificationOverTime.getPollingPeriod();

        assertEquals(pollingPeriodMillis, result);
    }

    @Test
    public void testGetDuration() {
        long durationMillis = 5000;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1000, durationMillis, Mockito.mock(VerificationMode.class), true);

        long result = verificationOverTime.getDuration();

        assertEquals(durationMillis, result);
    }

    @Test
    public void testGetDelegate() {
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1000, 5000, delegate, true);

        VerificationMode result = verificationOverTime.getDelegate();

        assertEquals(delegate, result);
    }

}