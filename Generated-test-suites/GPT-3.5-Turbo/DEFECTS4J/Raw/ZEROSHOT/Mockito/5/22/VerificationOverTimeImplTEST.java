
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {

    private VerificationOverTimeImpl verificationOverTime;
    private VerificationMode delegate;

    @Before
    public void setUp() {
        delegate = mock(VerificationMode.class);
        verificationOverTime = new VerificationOverTimeImpl(1000, 5000, delegate, true);
    }

    @Test
    public void verify_DelegateSucceedsBeforeTimeout_ReturnsSuccessfully() {
        VerificationData data = mock(VerificationData.class);

        // Set up the delegate to succeed immediately
        doNothing().when(delegate).verify(data);

        // Verify the verificationOverTime instance
        verificationOverTime.verify(data);

        // Assert that the delegate was called
        verify(delegate).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void verify_DelegateFailsBeforeTimeout_ThrowsMockitoAssertionError() {
        VerificationData data = mock(VerificationData.class);

        // Set up the delegate to throw an assertion error
        doThrow(MockitoAssertionError.class).when(delegate).verify(data);

        // Verify the verificationOverTime instance
        verificationOverTime.verify(data);

        // Assert that the delegate was called
        verify(delegate).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void verify_DelegateDoesNotSucceedBeforeTimeout_ThrowsMockitoAssertionError() {
        VerificationData data = mock(VerificationData.class);

        // Set up the delegate to never succeed
        doThrow(MockitoAssertionError.class).when(delegate).verify(data);

        // Verify the verificationOverTime instance
        verificationOverTime.verify(data);

        // Assert that the delegate was called multiple times within the timeout
        verify(delegate, atLeastOnce()).verify(data);
    }
}
