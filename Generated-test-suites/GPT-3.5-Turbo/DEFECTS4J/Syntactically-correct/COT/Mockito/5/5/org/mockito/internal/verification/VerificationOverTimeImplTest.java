import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {
    @Test
    public void testConstructor1() {
        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(1000, 5000, mock(VerificationMode.class), true);
        assertNotNull(verificationOverTimeImpl);
    }

    @Test
    public void testConstructor2() {
        Timer mockTimer = mock(Timer.class);
        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(1000, 5000, mock(VerificationMode.class), true, mockTimer);
        assertNotNull(verificationOverTimeImpl);
    }

    @Test
    public void testVerify() {
        VerificationMode mockVerificationMode = mock(VerificationMode.class);
        VerificationData mockVerificationData = mock(VerificationData.class);

        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(1000, 5000, mockVerificationMode, true);
        verificationOverTimeImpl.verify(mockVerificationData);

        verify(mockVerificationMode).verify(mockVerificationData);
    }

    @Test
    public void testGetPollingPeriod() {
        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(1000, 5000, mock(VerificationMode.class), true);
        assertEquals(1000, verificationOverTimeImpl.getPollingPeriod());
    }

    @Test
    public void testGetDuration() {
        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(1000, 5000, mock(VerificationMode.class), true);
        assertEquals(5000, verificationOverTimeImpl.getDuration());
    }

    @Test
    public void testGetDelegate() {
        VerificationMode mockVerificationMode = mock(VerificationMode.class);

        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(1000, 5000, mockVerificationMode, true);
        assertEquals(mockVerificationMode, verificationOverTimeImpl.getDelegate());
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_DelegateThrowsAssertionError() {
        VerificationMode mockVerificationMode = mock(VerificationMode.class);
        VerificationData mockVerificationData = mock(VerificationData.class);

        doThrow(new MockitoAssertionError("Delegate verification mode failed")).when(mockVerificationMode).verify(mockVerificationData);

        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(1000, 5000, mockVerificationMode, true);
        verificationOverTimeImpl.verify(mockVerificationData);
    }

    @Test(expected = ArgumentsAreDifferent.class)
    public void testVerify_DelegateThrowsArgumentsAreDifferent() {
        VerificationMode mockVerificationMode = mock(VerificationMode.class);
        VerificationData mockVerificationData = mock(VerificationData.class);

        doThrow(new ArgumentsAreDifferent("Arguments are different")).when(mockVerificationMode).verify(mockVerificationData);

        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(1000, 5000, mockVerificationMode, true);
        verificationOverTimeImpl.verify(mockVerificationData);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_DurationShorterThanPollingPeriod() {
        VerificationMode mockVerificationMode = mock(VerificationMode.class);
        VerificationData mockVerificationData = mock(VerificationData.class);

        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(5000, 1000, mockVerificationMode, true);
        verificationOverTimeImpl.verify(mockVerificationData);
    }

    @Test
    public void testVerify_DurationEqualToPollingPeriod() {
        VerificationMode mockVerificationMode = mock(VerificationMode.class);
        VerificationData mockVerificationData = mock(VerificationData.class);

        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(5000, 5000, mockVerificationMode, true);
        verificationOverTimeImpl.verify(mockVerificationData);

        verify(mockVerificationMode).verify(mockVerificationData);
    }

    @Test
    public void testVerify_DurationLargerThanPollingPeriod() {
        VerificationMode mockVerificationMode = mock(VerificationMode.class);
        VerificationData mockVerificationData = mock(VerificationData.class);

        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(1000, 5000, mockVerificationMode, true);
        verificationOverTimeImpl.verify(mockVerificationData);

        verify(mockVerificationMode).verify(mockVerificationData);
    }
}