import org.junit.*;
import org.mockito.*;
import org.mockito.exceptions.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

public class VerificationOverTimeImplTest {

    private VerificationMode delegate;
    private VerificationOverTimeImpl verificationOverTime;

    @Before
    public void setup() {
        delegate = Mockito.mock(VerificationMode.class);
        verificationOverTime = new VerificationOverTimeImpl(100, 500, delegate, true);
    }

    @Test
    public void verify_ReturnOnSuccessSetToTrue_ReturnImmediatelyWhenDelegateIsSatisfied() {
        VerificationData data = Mockito.mock(VerificationData.class);
        
        // Mock the delegate to return successfully
        Mockito.when(delegate.verify(Mockito.any(VerificationData.class))).thenReturn(null);
        
        // Call the verify method
        verificationOverTime.verify(data);
        
        // Assert that the delegate.verify() method was called once
        Mockito.verify(delegate, Mockito.times(1)).verify(Mockito.any(VerificationData.class));
    }

    @Test(expected = MockitoAssertionError.class)
    public void verify_ReturnOnSuccessSetToTrue_ThrowAssertionErrorWhenDelegateIsNotSatisfiedBeforeTimeout() {
        VerificationData data = Mockito.mock(VerificationData.class);
        
        // Mock the delegate to return unsuccessfully
        Mockito.doThrow(new MockitoAssertionError("Delegate not satisfied")).when(delegate).verify(data);
        
        // Call the verify method
        verificationOverTime.verify(data);
    }

    @Test
    public void verify_ReturnOnSuccessSetToFalse_ReturnOnlyWhenDelegateIsSatisfiedAndFullDurationHasPassed() throws InterruptedException {
        VerificationData data = Mockito.mock(VerificationData.class);

        // Mock the delegate to return unsuccessfully in the first iteration and successfully in the second iteration
        Mockito.doThrow(new MockitoAssertionError("Delegate not satisfied"))
                .doNothing()
                .when(delegate)
                .verify(data);

        // Start the timer
        Timer timer = Mockito.mock(Timer.class);
        Mockito.when(timer.isCounting()).thenReturn(true).thenReturn(false);
        verificationOverTime = new VerificationOverTimeImpl(100, 500, delegate, false, timer);

        // Call the verify method
        verificationOverTime.verify(data);

        // Assert that the delegate.verify() method was called twice
        Mockito.verify(delegate, Mockito.times(2)).verify(Mockito.any(VerificationData.class));
    }

    @Test(expected = MockitoAssertionError.class)
    public void verify_ReturnOnSuccessSetToFalse_ThrowAssertionErrorWhenDelegateIsNotSatisfiedBeforeTimeoutAndCannotRecoverFromFailure() {
        VerificationData data = Mockito.mock(VerificationData.class);

        // Mock the delegate to return unsuccessfully
        Mockito.doThrow(new MockitoAssertionError("Delegate not satisfied")).when(delegate).verify(data);

        // Mock the canRecoverFromFailure method to return false
        VerificationOverTimeImpl verificationOverTime = Mockito.mock(VerificationOverTimeImpl.class);
        Mockito.when(verificationOverTime.canRecoverFromFailure(Mockito.any(VerificationMode.class))).thenReturn(false);

        // Call the verify method
        verificationOverTime.verify(data);
    }

    @Test
    public void verify_ReturnOnSuccessSetToFalse_RecoverFromFailureWhenDelegateIsNotSatisfiedBeforeTimeout() throws InterruptedException {
        VerificationData data = Mockito.mock(VerificationData.class);

        // Mock the delegate to return unsuccessfully in the first iteration and successfully in the second iteration
        Mockito.doThrow(new MockitoAssertionError("Delegate not satisfied"))
                .doNothing()
                .when(delegate)
                .verify(data);

        // Mock the canRecoverFromFailure method to return true
        VerificationOverTimeImpl verificationOverTime = Mockito.mock(VerificationOverTimeImpl.class);
        Mockito.when(verificationOverTime.canRecoverFromFailure(Mockito.any(VerificationMode.class))).thenReturn(true);

        // Mock the sleep method to do nothing
        Mockito.doNothing().when(verificationOverTime).sleep(Mockito.anyLong());

        // Call the verify method
        verificationOverTime.verify(data);

        // Assert that the delegate.verify() method was called twice
        Mockito.verify(delegate, Mockito.times(2)).verify(Mockito.any(VerificationData.class));
    }

    @Test
    public void getPollingPeriod_ReturnPollingPeriod() {
        long pollingPeriod = verificationOverTime.getPollingPeriod();

        Assert.assertEquals(100, pollingPeriod);
    }

    @Test
    public void getDuration_ReturnDuration() {
        long duration = verificationOverTime.getDuration();

        Assert.assertEquals(500, duration);
    }

    @Test
    public void getDelegate_ReturnDelegate() {
        VerificationMode actualDelegate = verificationOverTime.getDelegate();

        Assert.assertEquals(delegate, actualDelegate);
    }

}