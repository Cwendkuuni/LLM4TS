import org.junit.*;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {

    private VerificationOverTimeImpl verificationOverTime;
    private VerificationData verificationData;
    private Timer timer;

    @Before
    public void setUp() {
        verificationOverTime = new VerificationOverTimeImpl(100, 1000, mock(VerificationMode.class), true);
        verificationData = mock(VerificationData.class);
        timer = mock(Timer.class);
    }

    @Test
    public void verify_WhenDelegateIsSatisfied_ReturnsSuccessfully() {
        when(timer.isCounting()).thenReturn(true, false);
        when(timer.start()).thenReturn(true);
        doNothing().when(verificationOverTime.getDelegate()).verify(verificationData);

        verificationOverTime.verify(verificationData);

        verify(verificationOverTime.getDelegate(), times(1)).verify(verificationData);
    }

    @Test
    public void verify_WhenDelegateIsNotSatisfiedAndCannotRecoverFromFailure_ThrowsError() {
        when(timer.isCounting()).thenReturn(true, false);
        when(timer.start()).thenReturn(true);
        doThrow(MockitoAssertionError.class).when(verificationOverTime.getDelegate()).verify(verificationData);
        when(verificationOverTime.canRecoverFromFailure(verificationOverTime.getDelegate())).thenReturn(false);

        try {
            verificationOverTime.verify(verificationData);
            Assert.fail("Expected MockitoAssertionError to be thrown");
        } catch (MockitoAssertionError e) {
            // Expected
        }

        verify(verificationOverTime.getDelegate(), times(1)).verify(verificationData);
    }

    @Test
    public void verify_WhenDelegateIsNotSatisfiedAndCanRecoverFromFailure_SleepsAndThrowsError() {
        when(timer.isCounting()).thenReturn(true, true, false);
        when(timer.start()).thenReturn(true);
        doThrow(MockitoAssertionError.class).when(verificationOverTime.getDelegate()).verify(verificationData);
        when(verificationOverTime.canRecoverFromFailure(verificationOverTime.getDelegate())).thenReturn(true);

        try {
            verificationOverTime.verify(verificationData);
            Assert.fail("Expected MockitoAssertionError to be thrown");
        } catch (MockitoAssertionError e) {
            // Expected
        }

        verify(verificationOverTime.getDelegate(), times(2)).verify(verificationData);
        verify(verificationOverTime, times(2)).sleep(anyLong());
    }

    @Test
    public void getPollingPeriod_ReturnsPollingPeriod() {
        long pollingPeriodMillis = 100;
        verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, 1000, mock(VerificationMode.class), true);

        long result = verificationOverTime.getPollingPeriod();

        Assert.assertEquals(pollingPeriodMillis, result);
    }

    @Test
    public void getDuration_ReturnsDuration() {
        long durationMillis = 1000;
        verificationOverTime = new VerificationOverTimeImpl(100, durationMillis, mock(VerificationMode.class), true);

        long result = verificationOverTime.getDuration();

        Assert.assertEquals(durationMillis, result);
    }

    @Test
    public void getDelegate_ReturnsDelegate() {
        VerificationMode delegate = mock(VerificationMode.class);
        verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, true);

        VerificationMode result = verificationOverTime.getDelegate();

        Assert.assertEquals(delegate, result);
    }

}