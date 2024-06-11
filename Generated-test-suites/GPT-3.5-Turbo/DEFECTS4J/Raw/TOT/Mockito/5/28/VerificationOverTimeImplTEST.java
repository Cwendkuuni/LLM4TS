import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;
import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {

    private VerificationOverTimeImpl verificationOverTime;
    private VerificationMode mockVerificationMode;
    private VerificationData mockVerificationData;
    private Timer mockTimer;

    @Before
    public void setUp() {
        mockVerificationMode = mock(VerificationMode.class);
        mockVerificationData = mock(VerificationData.class);
        mockTimer = mock(Timer.class);
        verificationOverTime = new VerificationOverTimeImpl(100, 1000, mockVerificationMode, true, mockTimer);
    }

    @Test
    public void testVerify_ReturnOnSuccess() {
        doThrow(MockitoAssertionError.class).when(mockVerificationMode).verify(mockVerificationData);

        verificationOverTime.verify(mockVerificationData);

        verify(mockVerificationMode, times(10)).verify(mockVerificationData);
        verify(mockTimer, times(11)).isCounting();
        verify(mockTimer, times(10)).start();
        verify(mockVerificationMode, never()).verify(null);
        verify(mockVerificationMode, never()).hashCode();
        verifyNoMoreInteractions(mockVerificationMode);
    }

    @Test
    public void testVerify_SuccessAfterDuration() {
        verify(mockVerificationMode, never()).verify(mockVerificationData);

        verificationOverTime.verify(mockVerificationData);

        verify(mockVerificationMode, times(1)).verify(mockVerificationData);
        verify(mockTimer, times(2)).isCounting();
        verify(mockTimer, times(1)).start();
        verify(mockVerificationMode, never()).verify(null);
        verify(mockVerificationMode, never()).hashCode();
        verifyNoMoreInteractions(mockVerificationMode);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_FailureAfterDuration() {
        doThrow(MockitoAssertionError.class).when(mockVerificationMode).verify(mockVerificationData);
        verify(mockVerificationMode, never()).verify(mockVerificationData);

        try {
            verificationOverTime.verify(mockVerificationData);
        } finally {
            verify(mockVerificationMode, times(10)).verify(mockVerificationData);
            verify(mockTimer, times(11)).isCounting();
            verify(mockTimer, times(10)).start();
            verify(mockVerificationMode, never()).verify(null);
            verify(mockVerificationMode, never()).hashCode();
        }
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_FailureAndCannotRecover() {
        doThrow(MockitoAssertionError.class).when(mockVerificationMode).verify(mockVerificationData);
        doReturn(false).when(verification).canRecoverFromFailure(any(VerificationMode.class));
        verify(mockVerificationMode, never()).verify(mockVerificationData);

        try {
            verificationOverTime.verify(mockVerificationData);
        } finally {
            verify(mockVerificationMode, times(1)).verify(mockVerificationData);
            verify(mockTimer, times(2)).isCounting();
            verify(mockTimer, times(1)).start();
            verify(mockVerificationMode, never()).verify(null);
            verify(mockVerificationMode, never()).hashCode();
        }
    }

    @Test(expected = InterruptedException.class)
    public void testSleep_InterruptedException() throws InterruptedException {
        doThrow(InterruptedException.class).when(mockTimer).sleep(1000);

        verificationOverTime.sleep(1000);

        verify(mockTimer, times(1)).sleep(1000);
    }

    @Test
    public void testGetPollingPeriod() {
        long pollingPeriod = verificationOverTime.getPollingPeriod();

        assertEquals(pollingPeriod, 100);
    }

    @Test
    public void testGetDuration() {
        long duration = verificationOverTime.getDuration();

        assertEquals(duration, 1000);
    }

    @Test
    public void testGetDelegate() {
        VerificationMode delegate = verificationOverTime.getDelegate();

        assertEquals(delegate, mockVerificationMode);
    }

}