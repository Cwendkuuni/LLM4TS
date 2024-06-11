import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {

    private VerificationOverTimeImpl verificationOverTime;

    @Before
    public void setUp() {
        verificationOverTime = new VerificationOverTimeImpl(100, 500, mock(VerificationMode.class), true, mock(Timer.class));
    }

    @Test
    public void verify_ReturnOnSuccess_True_ReturnsImmediately() {
        VerificationData verificationData = mock(VerificationData.class);
        VerificationMode verificationMode = mock(VerificationMode.class);

        verificationOverTime.verify(verificationData);

        verify(verificationData, times(1)).verify(verificationMode);
        verifyNoMoreInteractions(verificationData);
    }

    @Test
    public void verify_ReturnOnSuccess_False_ReturnsAfterFullDurationAndDelegateSuccess() {
        VerificationData verificationData = mock(VerificationData.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        
        when(verificationData.verify(verificationMode)).thenThrow(new MockitoAssertionError("Delegate not satisfied"));

        verificationOverTime.verify(verificationData);

        verify(verificationData, times(6)).verify(verificationMode);
        verifyNoMoreInteractions(verificationData);
    }

    @Test(expected = MockitoAssertionError.class)
    public void verify_ReturnOnSuccess_False_ReturnsAfterFullDurationAndDelegateNotSatisfied() {
        VerificationData verificationData = mock(VerificationData.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        
        when(verificationData.verify(verificationMode)).thenThrow(new MockitoAssertionError("Delegate not satisfied"));

        verificationOverTime.verify(verificationData);
    }

    @Test
    public void verify_DelegateThrowsExceptionRecoverable_ReturnsAfterFullDuration() {
        VerificationData verificationData = mock(VerificationData.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        
        when(verificationData.verify(verificationMode)).thenThrow(new MockitoAssertionError("Delegate not satisfied"));
        when(verificationMode instanceof AtMost || verificationMode instanceof NoMoreInteractions).thenReturn(true);

        verificationOverTime.verify(verificationData);

        verify(verificationData, times(6)).verify(verificationMode);
        verifyNoMoreInteractions(verificationData);
    }

    @Test(expected = MockitoAssertionError.class)
    public void verify_DelegateThrowsExceptionNonRecoverable_ThrowsAssertionError() {
        VerificationData verificationData = mock(VerificationData.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        
        when(verificationData.verify(verificationMode)).thenThrow(new MockitoAssertionError("Delegate not satisfied"));
        when(verificationMode instanceof AtMost || verificationMode instanceof NoMoreInteractions).thenReturn(false);

        verificationOverTime.verify(verificationData);
    }
}