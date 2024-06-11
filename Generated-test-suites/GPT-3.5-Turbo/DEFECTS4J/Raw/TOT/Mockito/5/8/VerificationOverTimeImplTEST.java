import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

public class VerificationOverTimeImplTest {

    @Mock
    private VerificationData data;

    @Test
    public void testVerify_WhenDelegateIsSatisfiedWithinDuration_ReturnImmediately() {
        VerificationMode delegateMock = mock(VerificationMode.class);
        when(delegateMock.toString()).thenReturn("Mock VerificationMode");
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, delegateMock, true);

        try {
            verification.verify(data);
        } catch (MockitoAssertionError e) {
            fail("Should not have thrown an error");
        }

        verify(delegateMock).verify(data);
    }

    @Test
    public void testVerify_WhenDelegateIsNotSatisfiedWithinDuration_ReturnAfterDuration() {
        VerificationMode delegateMock = mock(VerificationMode.class);
        when(delegateMock.toString()).thenReturn("Mock VerificationMode");
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(50, 100, delegateMock, false);

        try {
            verification.verify(data);
            fail("Should have thrown an error");
        } catch (MockitoAssertionError e) {
            assertEquals("Mock VerificationMode", e.getMessage());
        }

        verify(delegateMock, atLeastOnce()).verify(data);
    }

    @Test
    public void testVerify_WhenDelegateThrowsAssertionError_CanRecoverFromFailure() {
        VerificationMode delegateMock = mock(VerificationMode.class);
        when(delegateMock.toString()).thenReturn("Mock VerificationMode");
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, delegateMock, false);

        try {
            doThrow(new MockitoAssertionError("Delegate assertion error")).when(delegateMock).verify(data);
            verification.verify(data);
            fail("Should have thrown an error");
        } catch (MockitoAssertionError e) {
            fail("Should not have thrown an error");
        }

        verify(delegateMock, atLeastOnce()).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_WhenDelegateThrowsAssertionError_CannotRecoverFromFailure() {
        VerificationMode delegateMock = mock(VerificationMode.class);
        when(delegateMock.toString()).thenReturn("Mock VerificationMode");
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(50, 100, delegateMock, false);

        doThrow(new MockitoAssertionError("Delegate assertion error")).when(delegateMock).verify(data);
        verification.verify(data);
    }

    @Test
    public void testVerify_WhenDelegateThrowsUnsupportedOperationException_CannotRecoverFromFailure() {
        VerificationMode delegateMock = mock(VerificationMode.class);
        when(delegateMock.toString()).thenReturn("Mock VerificationMode");
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(50, 100, delegateMock, false);

        doThrow(new UnsupportedOperationException("Delegate unsupported operation")).when(delegateMock).verify(data);
        try {
            verification.verify(data);
            fail("Should have thrown an error");
        } catch (UnsupportedOperationException e) {
            assertEquals("Delegate unsupported operation", e.getMessage());
        }

        verify(delegateMock, atLeastOnce()).verify(data);
    }

    @Test
    public void testVerify_WhenDelegateThrowsInterruptedException_CanRecoverFromFailure() {
        VerificationMode delegateMock = mock(VerificationMode.class);
        when(delegateMock.toString()).thenReturn("Mock VerificationMode");
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, delegateMock, false);

        doThrow(new InterruptedException()).when(delegateMock).verify(data);

        try {
            verification.verify(data);
            fail("Should have thrown an error");
        } catch (InterruptedException e) {
            fail("Should not have thrown an error");
        }

        verify(delegateMock, atLeastOnce()).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_WhenDelegateThrowsOtherException_CannotRecoverFromFailure() {
        VerificationMode delegateMock = mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(50, 100, delegateMock, false);

        doThrow(new RuntimeException("Delegate runtime error")).when(delegateMock).verify(data);
        verification.verify(data);
    }

}