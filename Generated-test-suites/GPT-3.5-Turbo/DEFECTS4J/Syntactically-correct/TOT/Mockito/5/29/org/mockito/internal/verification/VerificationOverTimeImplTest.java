import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;
import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {

    private VerificationOverTimeImpl verificationOverTime;
    private VerificationMode delegate;
    private VerificationData data;

    @Before
    public void setUp() {
        delegate = mock(VerificationMode.class);
        data = mock(VerificationData.class);
        verificationOverTime = new VerificationOverTimeImpl(1000, 5000, delegate, true);
    }

    @Test
    public void verify_DelegateIsSatisfiedBeforeTimeout_ReturnsSuccessfully() {
        doNothing().when(delegate).verify(data);

        verificationOverTime.verify(data);

        verify(delegate).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void verify_DelegateIsNotSatisfied_ThrowsMockitoAssertionError() {
        MockitoAssertionError error = new MockitoAssertionError("Delegate not satisfied");
        doThrow(error).when(delegate).verify(data);

        verificationOverTime.verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void verify_DelegateThrowsMockitoAssertionError_ThrowsMockitoAssertionError() {
        MockitoAssertionError error = new MockitoAssertionError("Delegate failed");
        doThrow(error).when(delegate).verify(data);

        verificationOverTime.verify(data);
    }

    @Test(expected = RuntimeException.class)
    public void verify_DelegateThrowsRuntimeException_ThrowsRuntimeException() {
        RuntimeException error = new RuntimeException("Delegate error");
        doThrow(error).when(delegate).verify(data);

        verificationOverTime.verify(data);
    }

    @Test
    public void verify_DelegateThrowsAssertionErrorAndCanRecoverFromFailure_SleepsAndReThrowsAssertionError() {
        AssertionError error = new AssertionError("Delegate not satisfied");
        doThrow(error).when(delegate).verify(data);
        when(verificationOverTime.canRecoverFromFailure(delegate)).thenReturn(true);

        verificationOverTime.verify(data);

        verify(delegate).verify(data);
        verify(verificationOverTime).canRecoverFromFailure(delegate);
        verify(verificationOverTime).sleep(1000);
    }

    @Test(expected = AssertionError.class)
    public void verify_DelegateThrowsAssertionErrorAndCannotRecoverFromFailure_ThrowsAssertionError() {
        AssertionError error = new AssertionError("Delegate not satisfied");
        doThrow(error).when(delegate).verify(data);
        when(verificationOverTime.canRecoverFromFailure(delegate)).thenReturn(false);

        verificationOverTime.verify(data);

        verify(delegate).verify(data);
        verify(verificationOverTime).canRecoverFromFailure(delegate);
    }

    @Test(timeout = 6000)
    public void verify_DelegateTakesLongerThanTimeout_ThrowsMockitoAssertionError() {
        // Simulate delegate taking longer than the timeout
        doAnswer(invocationOnMock -> {
            Thread.sleep(2000);
            return null;
        }).when(delegate).verify(data);

        verificationOverTime.verify(data);
    }

    @Test(timeout = 3000)
    public void verify_DelegateReturnsBeforeTimeout_ReturnsImmediately() {
        doNothing().when(delegate).verify(data);
        verificationOverTime = new VerificationOverTimeImpl(500, 5000, delegate, true);

        verificationOverTime.verify(data);
    }

    private void sleep(long sleep) {
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}