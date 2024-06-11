import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerify_ReturnOnSuccessTrue() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        Timer timer = mock(Timer.class);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, true, timer);

        when(timer.isCounting()).thenReturn(true, false);
        verify(delegate, never()).verify(data);

        verificationOverTime.verify(data);

        verify(delegate).verify(data);
    }

    @Test
    public void testVerify_ReturnOnSuccessFalse() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        Timer timer = mock(Timer.class);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, false, timer);

        when(timer.isCounting()).thenReturn(true, false);
        verify(delegate, never()).verify(data);

        verificationOverTime.verify(data);

        verify(delegate).verify(data);
    }

    @Test
    public void testVerify_ExceptionThrown_ReturnOnSuccessTrue() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        Timer timer = mock(Timer.class);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, true, timer);

        when(timer.isCounting()).thenReturn(true, false);
        doThrow(MockitoAssertionError.class).when(delegate).verify(data);

        try {
            verificationOverTime.verify(data);
        } catch (MockitoAssertionError e) {
            assertEquals("Verification failed", e.getMessage());
        }

        verify(delegate).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_ExceptionThrown_ReturnOnSuccessFalse() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        Timer timer = mock(Timer.class);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, false, timer);

        when(timer.isCounting()).thenReturn(true, false);
        doThrow(MockitoAssertionError.class).when(delegate).verify(data);

        verificationOverTime.verify(data);

        verify(delegate).verify(data);
    }

    @Test(expected = ArgumentsAreDifferent.class)
    public void testVerify_ArgumentsAreDifferentException_ReturnOnSuccessFalse() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        Timer timer = mock(Timer.class);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, false, timer);

        when(timer.isCounting()).thenReturn(true, false);
        doThrow(ArgumentsAreDifferent.class).when(delegate).verify(data);

        verificationOverTime.verify(data);

        verify(delegate).verify(data);
    }
}