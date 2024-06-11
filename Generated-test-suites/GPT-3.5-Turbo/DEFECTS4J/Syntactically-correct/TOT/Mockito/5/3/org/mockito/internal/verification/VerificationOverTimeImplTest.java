import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class VerificationOverTimeImplTest {

    private VerificationOverTimeImpl verificationMode;
    private VerificationMode delegate;

    @Before
    public void setUp() {
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        delegate = mock(VerificationMode.class);
        verificationMode = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, true);
    }

    @Test
    public void verify_delegateIsSatisfied_beforeDuration() {
        VerificationData data = mock(VerificationData.class);
        doThrow(new MockitoAssertionError("Delegate not satisfied")).doNothing().when(delegate).verify(data);

        // Call the method under test
        verificationMode.verify(data);

        // Verify that the delegate is called once before the duration and returns successfully
        verify(delegate, times(1)).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void verify_delegateIsNotSatisfied_beforeDuration() {
        VerificationData data = mock(VerificationData.class);
        doThrow(new MockitoAssertionError("Delegate not satisfied")).doThrow(new MockitoAssertionError("Delegate still not satisfied")).when(delegate).verify(data);

        // Call the method under test
        verificationMode.verify(data);
    }

    @Test
    public void verify_delegateIsSatisfied_afterDuration() {
        VerificationData data = mock(VerificationData.class);
        doThrow(new MockitoAssertionError("Delegate not satisfied")).when(delegate).verify(data);
        Timer timer = mock(Timer.class);
        when(timer.isCounting()).thenReturn(true,true,true,false); //last true triggers returnOnSuccess

        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(100, 1000, delegate, true, timer);

        // Call the method under test
        verificationMode.verify(data);

        // Verify that the delegate is called multiple times before the duration and returns successfully
        verify(delegate, atLeast(3)).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void verify_delegateIsNotSatisfied_afterDuration() {
        VerificationData data = mock(VerificationData.class);
        doThrow(new MockitoAssertionError("Delegate not satisfied")).when(delegate).verify(data);
        Timer timer = mock(Timer.class);
        when(timer.isCounting()).thenReturn(true, true, true, true,false); //number of true should exceed pollingPeriod to exceed duration

        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(100, 1000, delegate, true, timer);

        // Call the method under test
        verificationMode.verify(data);
    }

    @Test
    public void verify_delegateThrowsExceptionAndRecovers() {
        VerificationData data = mock(VerificationData.class);
        doThrow(new MockitoAssertionError("Delegate not satisfied")).doNothing().when(delegate).verify(data);
        Timer timer = mock(Timer.class);
        when(timer.isCounting()).thenReturn(true, false); // first true triggers the exception, second false recovers

        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(100, 1000, delegate, true, timer);

        // Call the method under test
        verificationMode.verify(data);

        // Verify that the delegate is called twice before recovery
        verify(delegate, times(2)).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void verify_delegateThrowsExceptionAndDoesNotRecover() {
        VerificationData data = mock(VerificationData.class);
        doThrow(new MockitoAssertionError("Delegate not satisfied")).when(delegate).verify(data);
        Timer timer = mock(Timer.class);
        when(timer.isCounting()).thenReturn(true, true, false); // first true triggers the exception, second true prevents recovery

        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(100, 1000, delegate, true, timer);

        // Call the method under test
        verificationMode.verify(data);
    }

}