import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.verification.VerificationModeFactory;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class VerificationOverTimeImplTest {

    private VerificationMode verificationMode;
    private VerificationMode mockVerificationMode;
    private Timer timer;

    @Captor
    private ArgumentCaptor<VerificationData> verificationDataCaptor;

    @Before
    public void setUp() {
        mockVerificationMode = Mockito.mock(VerificationMode.class);
        verificationMode = new VerificationOverTimeImpl(100, 500, mockVerificationMode, false, new Timer(500));
        timer = Mockito.mock(Timer.class);
        Mockito.when(timer.isCounting()).thenReturn(true);
        Mockito.when(timer.start()).thenReturn(null);
        Mockito.when(timer.stop()).thenReturn(null);
        Mockito.when(timer.reset()).thenReturn(null);
    }

    @Test
    public void testDelegateVerifiedWithinTimeout() {
        VerificationData verificationData = Mockito.mock(VerificationData.class);

        Mockito.when(mockVerificationMode.verify(verificationDataCaptor.capture(verificationData))).thenReturn(null);

        verificationMode.verify(verificationData);

        InOrder inOrder = Mockito.inOrder(mockVerificationMode);
        inOrder.verify(mockVerificationMode).verify(verificationData);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testDelegateNotVerifiedWithinTimeout() {
        VerificationData verificationData = Mockito.mock(VerificationData.class);

        ArgumentCaptor<MockitoAssertionError> exceptionCaptor = ArgumentCaptor.forClass(MockitoAssertionError.class);

        Mockito.doThrow(new MockitoAssertionError("Delegate verification failed"))
                .when(mockVerificationMode)
                .verify(verificationData);

        verificationMode.verify(verificationData);

        Mockito.verify(mockVerificationMode).verify(verificationData); // this should throw an exception
        Mockito.verify(mockVerificationMode).verify(verificationData, Mockito.times(2)); // this should fail as an assertion error was thrown
        Mockito.verifyNoMoreInteractions(mockVerificationMode);

        // the exception from the delegate should be propagated
        Mockito.verify(mockVerificationMode).verify(verificationData, exceptionCaptor.capture());
    }

    @Test
    public void testDelegateVerifiedAfterTimeout() {
        VerificationData verificationData = Mockito.mock(VerificationData.class);
        Mockito.when(mockVerificationMode.verify(verificationData)).thenReturn(null);

        verificationMode.verify(verificationData);

        Mockito.verify(mockVerificationMode).verify(verificationData); // this should be called after the timeout
    }

    @Test
    public void testDelegateNotVerifiedAfterTimeout() {
        VerificationData verificationData = Mockito.mock(VerificationData.class);
        Mockito.when(mockVerificationMode.verify(verificationData)).thenThrow(new MockitoAssertionError("Delegate verification failed"));

        verificationMode.verify(verificationData);

        Mockito.verify(mockVerificationMode).verify(verificationData, never());
        Mockito.verifyZeroInteractions(mockVerificationMode);
    }

    @Test
    public void testTimeoutReset() {
        VerificationData verificationData = Mockito.mock(VerificationData.class);
        Mockito.when(mockVerificationMode.verify(verificationData)).thenReturn(null);

        verificationMode.verify(verificationData);

        Mockito.verify(timer).reset();
        Mockito.verify(timer).start();
    }

    @Test
    public void testVerificationWithTimeout() {
        VerificationMode verificationWithTimeout = VerificationModeFactory.verificationWithTimeout(100, 500, mockVerificationMode);
        VerificationData verificationData = Mockito.mock(VerificationData.class);

        Mockito.when(mockVerificationMode.verify(verificationData)).thenReturn(null);

        verificationWithTimeout.verify(verificationData);

        InOrder inOrder = Mockito.inOrder(mockVerificationMode);
        inOrder.verify(mockVerificationMode).verify(verificationData);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerificationWithTimeoutDelegateFailure() {
        VerificationMode verificationWithTimeout = VerificationModeFactory.verificationWithTimeout(100, 500, mockVerificationMode);
        VerificationData verificationData = Mockito.mock(VerificationData.class);

        ArgumentCaptor<MockitoAssertionError> exceptionCaptor = ArgumentCaptor.forClass(MockitoAssertionError.class);

        Mockito.doThrow(new MockitoAssertionError("Delegate verification failed"))
                .when(mockVerificationMode)
                .verify(verificationData);

        verificationWithTimeout.verify(verificationData);

        Mockito.verify(mockVerificationMode).verify(verificationData, Mockito.times(1)); // this should be called with the exception
        Mockito.verify(mockVerificationMode).verify(verificationData, never()); // this should not be called
        Mockito.verifyZeroInteractions(mockVerificationMode);

        // the exception from the delegate should be propagated
        Mockito.verify(mockVerificationMode).verify(verificationData, exceptionCaptor.capture());
    }

    @Test
    public void testVerificationAfterDelay() {
        VerificationMode verificationAfterDelay = VerificationModeFactory.verificationAfterDelay(100, 500, mockVerificationMode, false);
        VerificationData verificationData = Mockito.mock(VerificationData.class);

        Mockito.when(mockVerificationMode.verify(verificationData)).thenReturn(null);

        verificationAfterDelay.verify(verificationData);

        InOrder inOrder = Mockito.inOrder(mockVerificationMode);
        inOrder.verify(mockVerificationMode).verify(verificationData);
    }

    @Test
    public void testVerificationAfterDelayDelegateFailure() {
        VerificationMode verificationAfterDelay = VerificationModeFactory.verificationAfterDelay(100, 500, mockVerificationMode, false);
        VerificationData verificationData = Mockito.mock(VerificationData.class);

        ArgumentCaptor<MockitoAssertionError> exceptionCaptor = ArgumentCaptor.forClass(MockitoAssertionError.class);

        Mockito.doThrow(new MockitoAssertionError("Delegate verification failed"))
                .when(mockVerificationMode)
                .verify(verificationData);

        verificationAfterDelay.verify(verificationData);

        Mockito.verify(mockVerificationMode).verify(verificationData, Mockito.times(1)); // this should be called with the exception
        Mockito.verify(mockVerificationMode).verify(verificationData, never()); // this should not be called
        Mockito.verifyZeroInteractions(mockVerificationMode);

        // the exception from the delegate should be propagated
        Mockito.verify(mockVerificationMode).verify(verificationData, exceptionCaptor.capture());
    }
}