import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {

    private VerificationOverTimeImpl verificationOverTimeImpl;
    private long pollingPeriodMillis;
    private long durationMillis;
    private VerificationMode delegate;
    private boolean returnOnSuccess;
    private VerificationData data;

    @Before
    public void setUp() {
        pollingPeriodMillis = 1000;
        durationMillis = 5000;
        delegate = mock(VerificationMode.class);
        returnOnSuccess = true;
        data = mock(VerificationData.class);
        verificationOverTimeImpl = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);
    }

    @Test
    public void verify_DelegateSucceedForReturnOnSuccess_ReturnImmediately() {
        // Arrange
        doNothing().when(delegate).verify(data);

        // Act
        verificationOverTimeImpl.verify(data);

        // Assert
        verify(delegate).verify(data);
    }

    @Test
    public void verify_DelegateFailsForReturnOnSuccess_ThrowException() {
        // Arrange
        MockitoAssertionError expectedException = new MockitoAssertionError("Delegate failed");
        doThrow(expectedException).when(delegate).verify(data);

        // Act
        try {
            verificationOverTimeImpl.verify(data);
        } catch (MockitoAssertionError e) {
            // Assert
            verify(delegate).verify(data);
            assertEquals(expectedException, e);
        }
    }

    @Test
    public void verify_DelegateSucceedForReturnOnSuccessTrue_ReturnImmediately() {
        // Arrange
        doNothing().when(delegate).verify(data);

        // Act
        verificationOverTimeImpl.verify(data);

        // Assert
        verify(delegate).verify(data);
    }

    @Test
    public void verify_DelegateFailsForReturnOnSuccessTrue_ThrowException() {
        // Arrange
        MockitoAssertionError expectedException = new MockitoAssertionError("Delegate failed");
        doThrow(expectedException).when(delegate).verify(data);

        // Act
        try {
            verificationOverTimeImpl.verify(data);
        } catch (MockitoAssertionError e) {
            // Assert
            verify(delegate).verify(data);
            assertEquals(expectedException, e);
        }
    }

    @Test
    public void verify_DelegateFailsForReturnOnSuccessFalse_ThrowException() {
        // Arrange
        MockitoAssertionError expectedException = new MockitoAssertionError("Delegate failed");
        doThrow(expectedException).when(delegate).verify(data);
        returnOnSuccess = false;
        verificationOverTimeImpl = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);

        // Act
        try {
            verificationOverTimeImpl.verify(data);
        } catch (MockitoAssertionError e) {
            // Assert
            verify(delegate).verify(data);
            assertEquals(expectedException, e);
        }
    }

    @Test
    public void verify_DelegateThrowsArgumentsAreDifferent_ThrowException() {
        // Arrange
        ArgumentsAreDifferent expectedException = new ArgumentsAreDifferent("Arguments are different");
        doThrow(expectedException).when(delegate).verify(data);

        // Act
        try {
            verificationOverTimeImpl.verify(data);
        } catch (ArgumentsAreDifferent e) {
            // Assert
            verify(delegate).verify(data);
            assertEquals(expectedException, e);
        }
    }

    @Test
    public void verify_DelegateCannotRecoverFromFailure_ThrowException() {
        // Arrange
        MockitoAssertionError expectedException = new MockitoAssertionError("Delegate failed");
        doThrow(expectedException).when(delegate).verify(data);
        when(delegate instanceof AtMost).thenReturn(true); // Cannot recover from failure
        verificationOverTimeImpl = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);

        // Act
        try {
            verificationOverTimeImpl.verify(data);
        } catch (MockitoAssertionError e) {
            // Assert
            verify(delegate).verify(data);
            assertEquals(expectedException, e);
        }
    }

    @Test
    public void verify_DelegateRecoverFromFailure_Succeed() {
        // Arrange
        MockitoAssertionError expectedException = new MockitoAssertionError("Delegate failed");
        doThrow(expectedException).doNothing().when(delegate).verify(data);

        // Act
        verificationOverTimeImpl.verify(data);

        // Assert
        verify(delegate, times(2)).verify(data);
    }

    @Test
    public void verify_Interrupted_ThrowException() {
        // Arrange
        doAnswer(invocation -> {
            Thread.sleep(durationMillis * 2); // Interrupted
            return null;
        }).when(delegate).verify(data);

        // Act
        try {
            verificationOverTimeImpl.verify(data);
        } catch (MockitoAssertionError e) {
            // Assert
            verify(delegate, atLeastOnce()).verify(data);
        }
    }

}