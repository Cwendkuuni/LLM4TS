import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.*;

import org.junit.Test;

public class VerificationOverTimeImplTest {

    @Test
    public void verify_ReturnOnSuccessTrue_DelegateSatisfiedBeforeDuration_NoErrorThrown() {

        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, delegate, true);

        try {
            verification.verify(data);

            verify(delegate).verify(data);
        } catch (MockitoAssertionError error) {
            fail("Error thrown: " + error.getMessage());
        }
    }

    @Test
    public void verify_ReturnOnSuccessTrue_DelegateNotSatisfiedBeforeDuration_ErrorThrown() {

        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, delegate, true);

        MockitoAssertionError error = assertThrows(MockitoAssertionError.class, () -> {
            verification.verify(data);
        });

        verify(delegate).verify(data);
    }

    @Test
    public void verify_ReturnOnSuccessFalse_DelegateSatisfiedBeforeDuration_NoErrorThrown() {

        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, delegate, false);

        try {
            verification.verify(data);

            verify(delegate).verify(data);
        } catch (MockitoAssertionError error) {
            fail("Error thrown: " + error.getMessage());
        }
    }

    @Test
    public void verify_ReturnOnSuccessFalse_DelegateSatisfiedAfterDuration_NoErrorThrown() {

        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 2000, delegate, false);

        try {
            Thread.sleep(3000);

            verification.verify(data);

            verify(delegate).verify(data);
        } catch (InterruptedException e) {
            fail("Thread interrupted: " + e.getMessage());
        } catch (MockitoAssertionError error) {
            fail("Error thrown: " + error.getMessage());
        }
    }

    @Test
    public void verify_DelegateThrowsMockitoAssertionErrorBeforeDuration_ErrorThrown() {

        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, delegate, true);

        MockitoAssertionError delegateError = new MockitoAssertionError("Delegate Assertion Error");
        doThrow(delegateError).when(delegate).verify(data);

        MockitoAssertionError error = assertThrows(MockitoAssertionError.class, () -> {
            verification.verify(data);
        });

        assertEquals(delegateError, error);
    }

    @Test
    public void verify_DelegateThrowsAnExceptionBeforeDuration_ErrorThrown() {

        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, delegate, true);

        RuntimeException delegateError = new RuntimeException("Delegate Error");
        doThrow(delegateError).when(delegate).verify(data);

        RuntimeException error = assertThrows(RuntimeException.class, () -> {
            verification.verify(data);
        });

        assertEquals(delegateError, error);
    }

    @Test
    public void verify_DelegateNeverRecoversFromFailure_ErrorThrown() {

        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, delegate, true);

        MockitoAssertionError delegateError = new MockitoAssertionError("Delegate Assertion Error");
        doThrow(delegateError).when(delegate).verify(data);

        doReturn(false).when(verification).canRecoverFromFailure(delegate);

        MockitoAssertionError error = assertThrows(MockitoAssertionError.class, () -> {
            verification.verify(data);
        });

        assertEquals(delegateError, error);
    }

    @Test
    public void verify_DelegateRecoversFromFailure_NoErrorThrown() {

        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, delegate, true);

        MockitoAssertionError delegateError = new MockitoAssertionError("Delegate Assertion Error");
        doThrow(delegateError).doNothing().when(delegate).verify(data);

        doReturn(true).when(verification).canRecoverFromFailure(delegate);

        try {
            verification.verify(data);

            verify(delegate, times(2)).verify(data);
        } catch (MockitoAssertionError error) {
            fail("Error thrown: " + error.getMessage());
        }
    }
}