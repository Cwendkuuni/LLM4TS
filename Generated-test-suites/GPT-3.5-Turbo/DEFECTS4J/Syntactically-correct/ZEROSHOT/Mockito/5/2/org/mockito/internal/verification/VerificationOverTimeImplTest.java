
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerify_ReturnOnSuccess() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 2000, delegate, true);

        verification.verify(data);

        verify(delegate).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_ThrowAssertionError() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        doThrow(MockitoAssertionError.class).when(delegate).verify(data);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 2000, delegate, true);

        verification.verify(data);
    }

    @Test
    public void testVerify_ReturnOnSuccess_False() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 2000, delegate, false);

        verification.verify(data);

        verify(delegate).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_ThrowAssertionError_False() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        doThrow(MockitoAssertionError.class).when(delegate).verify(data);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 2000, delegate, false);

        verification.verify(data);
    }

    @Test
    public void testHandleVerifyException_CanRecover() {
        VerificationMode verificationMode = mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 2000, verificationMode, true);
       
        verification.handleVerifyException(new MockitoAssertionError("Assertion error"));

        verify(verificationMode).verify(any());
        verify(verificationMode, never()).handleVerifyException(any());
    }

    @Test(expected = MockitoAssertionError.class)
    public void testHandleVerifyException_CannotRecover() {
        VerificationMode verificationMode = mock(VerificationMode.class);
        doReturn(false).when(verificationMode).canRecoverFromFailure(any());
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 2000, verificationMode, true);
       
        verification.handleVerifyException(new MockitoAssertionError("Assertion error"));
    }

    @Test
    public void testCanRecoverFromFailure_True() {
        VerificationMode verificationMode = mock(VerificationMode.class);
        assertTrue(new VerificationOverTimeImpl(1000, 2000, verificationMode, true)
                .canRecoverFromFailure(verificationMode));
    }

    @Test
    public void testCanRecoverFromFailure_False() {
        VerificationMode atMost = mock(AtMost.class);
        VerificationMode noMoreInteractions = mock(NoMoreInteractions.class);
        assertFalse(new VerificationOverTimeImpl(1000, 2000, atMost, true)
                .canRecoverFromFailure(atMost));
        assertFalse(new VerificationOverTimeImpl(1000, 2000, noMoreInteractions, true)
                .canRecoverFromFailure(noMoreInteractions));
    }

    @Test
    public void testSleep() {
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 2000, mock(VerificationMode.class), true);
       
        verification.sleep(1000);

        verifyStatic(Thread.class);
        Thread.sleep(1000);
    }

    @Test
    public void testGetPollingPeriod() {
        long pollingPeriodMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(
                pollingPeriodMillis, 2000, delegate, true);

        long result = verification.getPollingPeriod();

        assertEquals(pollingPeriodMillis, result);
    }

    @Test
    public void testGetDuration() {
        long durationMillis = 2000;
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(
                1000, durationMillis, delegate, true);

        long result = verification.getDuration();

        assertEquals(durationMillis, result);
    }

    @Test
    public void testGetDelegate() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(
                1000, 2000, delegate, true);

        VerificationMode result = verification.getDelegate();

        assertEquals(delegate, result);
    }
}
