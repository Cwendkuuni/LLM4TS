import static org.mockito.Mockito.*;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerify() {
        VerificationData data = mock(VerificationData.class);

        VerificationMode delegate = mock(VerificationMode.class);

        Timer timer = mock(Timer.class);
        when(timer.isCounting()).thenReturn(true).thenReturn(false);

        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, delegate, true, timer);

        verification.verify(data);

        verify(delegate).verify(data);
        verify(timer).start();
        verify(timer, times(2)).isCounting();
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_timeout() {
        VerificationData data = mock(VerificationData.class);

        VerificationMode delegate = mock(VerificationMode.class);
        doThrow(MockitoAssertionError.class).when(delegate).verify(data);

        Timer timer = mock(Timer.class);
        when(timer.isCounting()).thenReturn(true).thenReturn(true);

        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, delegate, true, timer);

        verification.verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_verificationException() {
        VerificationData data = mock(VerificationData.class);

        VerificationMode delegate = mock(VerificationMode.class);
        doThrow(MockitoAssertionError.class).when(delegate).verify(data);

        Timer timer = mock(Timer.class);
        when(timer.isCounting()).thenReturn(true).thenReturn(false);

        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, delegate, true, timer);

        verification.verify(data);
    }

    @Test
    public void testHandleVerifyException_canRecover() {
        VerificationMode verificationMode = mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, verificationMode, true);

        assertFalse(verification.canRecoverFromFailure(verificationMode));
    }

    @Test
    public void testHandleVerifyException_cannotRecover() {
        VerificationMode verificationMode = mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, verificationMode, false);

        assertTrue(verification.canRecoverFromFailure(verificationMode));
    }

    @Test
    public void testSleep() {
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, null, false);

        verification.sleep(1000);

        verifyStatic(Thread.class);
        Thread.sleep(1000);
    }

    @Test
    public void testGetPollingPeriod() {
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(2000, 5000, null, false);

        assertEquals(2000, verification.getPollingPeriod());
    }

    @Test
    public void testGetDuration() {
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(2000, 5000, null, false);

        assertEquals(5000, verification.getDuration());
    }

    @Test
    public void testGetDelegate() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(2000, 5000, delegate, false);

        assertEquals(delegate, verification.getDelegate());
    }

}