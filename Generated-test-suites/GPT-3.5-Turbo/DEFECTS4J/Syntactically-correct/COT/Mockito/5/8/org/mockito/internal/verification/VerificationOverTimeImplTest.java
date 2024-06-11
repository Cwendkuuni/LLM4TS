
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {

    private VerificationOverTimeImpl verificationOverTime;

    @Before
    public void setup() {
        verificationOverTime = new VerificationOverTimeImpl(1000, 5000, mock(VerificationMode.class), true);
    }

    @Test
    public void verify_callsDelegateVerify() {
        VerificationData data = mock(VerificationData.class);
        VerificationMode delegate = mock(VerificationMode.class);
        verificationOverTime = new VerificationOverTimeImpl(1000, 5000, delegate, true);

        verificationOverTime.verify(data);

        verify(delegate).verify(data);
    }

    @Test
    public void verify_returnsImmediatelyOnSuccess() {
        VerificationData data = mock(VerificationData.class);
        VerificationMode delegate = mock(VerificationMode.class);
        verificationOverTime = new VerificationOverTimeImpl(1000, 5000, delegate, true);

        verificationOverTime.verify(data);

        verify(delegate).verify(data);
        verifyNoMoreInteractions(delegate);
    }

    @Test(expected = MockitoAssertionError.class)
    public void verify_throwsExceptionIfDelegateDoesNotSucceedBeforeTimeout() {
        VerificationData data = mock(VerificationData.class);
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate.toString()).thenReturn("Delegate Verification Mode");
        verificationOverTime = new VerificationOverTimeImpl(1000, 2000, delegate, false);

        verificationOverTime.verify(data);
    }

    @Test(expected = NullPointerException.class)
    public void verify_throwsNullPointerExceptionIfDataIsNull() {
        VerificationMode delegate = mock(VerificationMode.class);
        verificationOverTime = new VerificationOverTimeImpl(1000, 5000, delegate, true);

        verificationOverTime.verify(null);
    }

    @Test
    public void canRecoverFromFailure_returnsTrueForVerificationModeOtherThanAtMostAndNoMoreInteractions() {
        VerificationMode verificationMode1 = mock(VerificationMode.class);
        VerificationMode verificationMode2 = mock(VerificationMode.class);
        VerificationMode verificationMode3 = mock(VerificationMode.class);
        VerificationMode verificationMode4 = mock(VerificationMode.class);

        verificationOverTime = new VerificationOverTimeImpl(1000, 5000, verificationMode1, true);
        boolean result1 = verificationOverTime.canRecoverFromFailure(verificationMode1);

        verificationOverTime = new VerificationOverTimeImpl(1000, 5000, verificationMode2, true);
        boolean result2 = verificationOverTime.canRecoverFromFailure(verificationMode2);

        verificationOverTime = new VerificationOverTimeImpl(1000, 5000, verificationMode3, true);
        boolean result3 = verificationOverTime.canRecoverFromFailure(verificationMode3);

        verificationOverTime = new VerificationOverTimeImpl(1000, 5000, verificationMode4, true);
        boolean result4 = verificationOverTime.canRecoverFromFailure(verificationMode4);

        assertTrue(result1);
        assertTrue(result2);
        assertTrue(result3);
        assertTrue(result4);
    }

    @Test
    public void canRecoverFromFailure_returnsFalseForAtMostVerificationMode() {
        VerificationMode atMostMode = mock(AtMost.class);
        verificationOverTime = new VerificationOverTimeImpl(1000, 5000, atMostMode, true);

        boolean result = verificationOverTime.canRecoverFromFailure(atMostMode);

        assertFalse(result);
    }

    @Test
    public void canRecoverFromFailure_returnsFalseForNoMoreInteractionsVerificationMode() {
        VerificationMode noMoreInteractionsMode = mock(NoMoreInteractions.class);
        verificationOverTime = new VerificationOverTimeImpl(1000, 5000, noMoreInteractionsMode, true);

        boolean result = verificationOverTime.canRecoverFromFailure(noMoreInteractionsMode);

        assertFalse(result);
    }
}
