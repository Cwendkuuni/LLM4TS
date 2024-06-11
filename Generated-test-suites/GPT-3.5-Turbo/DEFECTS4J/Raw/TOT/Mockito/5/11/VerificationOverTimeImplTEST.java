import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class VerificationOverTimeImplTest {

    @Mock
    private VerificationMode delegate;

    @Mock
    private VerificationData data;

    private VerificationOverTimeImpl verificationOverTime;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, true, new Timer(1000));
    }

    @Test
    public void verify_ReturnOnSuccess_WhenDelegateSuccessfullySatisfiesVerificationMode() {
        when(delegate.toString()).thenReturn("MockitoAssertionError");
        doNothing().when(delegate).verify(data);
        verificationOverTime.verify(data);
        verify(delegate, times(1)).verify(data);
    }

    @Test
    public void verify_ReturnOnSuccess_WhenDelegateSatisfiesVerificationModeBeforeTimeout() {
        when(delegate.toString()).thenReturn("MockitoAssertionError");
        doNothing().doThrow(new MockitoAssertionError("Delegate did not satisfy verification mode"))
                .when(delegate).verify(data);
        verificationOverTime.verify(data);
        verify(delegate, times(2)).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void verify_ThrowMockitoAssertionError_WhenDelegateFailsToSatisfyVerificationMode() {
        when(delegate.toString()).thenReturn("MockitoAssertionError");
        doThrow(new MockitoAssertionError("Delegate did not satisfy verification mode"))
                .when(delegate).verify(data);
        verificationOverTime.verify(data);
        verify(delegate, times(1)).verify(data);
    }

    @Test
    public void verify_ThrowMockitoAssertionError_AfterFullDurationHasPassed_WhenDelegateFailsToSatisfyVerificationMode() {
        when(delegate.toString()).thenReturn("MockitoAssertionError");
        doThrow(new MockitoAssertionError("Delegate did not satisfy verification mode"))
                .doNothing().when(delegate).verify(data);
        verificationOverTime.verify(data);
        verify(delegate, times(2)).verify(data);
    }

    @Test(expected = NullPointerException.class)
    public void verify_ThrowNullPointerException_WhenDelegateIsNull() {
        verificationOverTime = new VerificationOverTimeImpl(100, 1000, null, true, new Timer(1000));
        verificationOverTime.verify(data);
    }

    @Test
    public void getPollingPeriod_ReturnCorrectPollingPeriod() {
        long pollingPeriodMillis = verificationOverTime.getPollingPeriod();
        assertEquals(100, pollingPeriodMillis);
    }

    @Test
    public void getDuration_ReturnCorrectDuration() {
        long durationMillis = verificationOverTime.getDuration();
        assertEquals(1000, durationMillis);
    }

    @Test
    public void getDelegate_ReturnCorrectDelegate() {
        VerificationMode returnedDelegate = verificationOverTime.getDelegate();
        assertEquals(delegate, returnedDelegate);
    }
}