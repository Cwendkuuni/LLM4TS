import org.junit.*;
import static org.mockito.Mockito.*;
import org.mockito.exceptions.verification.ArgumentsAreDifferent;
import org.mockito.exceptions.verification.NoMoreInteractions;
import org.mockito.exceptions.verification.VerificationInOrderFailure;
import org.mockito.internal.verification.api.VerificationData;

public class VerificationOverTimeImplTest {

    private VerificationOverTimeImpl verificationOverTimeImpl;

    @Before
    public void setUp() {
        verificationOverTimeImpl = new VerificationOverTimeImpl(100, 1000, mock(VerificationMode.class), true);
    }

    @Test
    public void verify_shouldReturnSuccessfully_WhenDelegateVerificationModeSucceeds() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);

        doNothing().when(delegate).verify(data);

        verificationOverTimeImpl.verify(data);

        verify(delegate).verify(data);
    }

    @Test(expected = ArgumentsAreDifferent.class)
    public void verify_shouldThrowArgumentsAreDifferent_WhenDelegateVerificationModeThrowsArgumentsAreDifferent() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);

        doThrow(ArgumentsAreDifferent.class).when(delegate).verify(data);

        verificationOverTimeImpl.verify(data);
    }

    @Test(expected = NoMoreInteractions.class)
    public void verify_shouldThrowNoMoreInteractions_WhenDelegateVerificationModeThrowsNoMoreInteractions() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);

        doThrow(NoMoreInteractions.class).when(delegate).verify(data);

        verificationOverTimeImpl.verify(data);
    }

    @Test(expected = VerificationInOrderFailure.class)
    public void verify_shouldThrowVerificationInOrderFailure_WhenDelegateVerificationModeThrowsVerificationInOrderFailure() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);

        doThrow(VerificationInOrderFailure.class).when(delegate).verify(data);

        verificationOverTimeImpl.verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void verify_shouldThrowMockitoAssertionError_WhenDelegateVerificationModeThrowsMockitoAssertionError() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);

        doThrow(MockitoAssertionError.class).when(delegate).verify(data);

        verificationOverTimeImpl.verify(data);
    }

    @Test
    public void verify_shouldWaitUntilDelegateVerificationModeSucceeds_WhenReturnOnSuccessIsFalse() throws InterruptedException {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);

        doNothing().when(delegate).verify(data);

        verificationOverTimeImpl = new VerificationOverTimeImpl(100, 1000, delegate, false);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                verificationOverTimeImpl.verify(data);
            }
        });
        thread.start();
        Thread.sleep(500);
        verify(delegate).verify(data);
    }

    @Test
    public void getPollingPeriod_shouldReturnPollingPeriodMillis() {
        long pollingPeriodMillis = verificationOverTimeImpl.getPollingPeriod();
        Assert.assertEquals(100, pollingPeriodMillis);
    }

    @Test
    public void getDuration_shouldReturnDurationMillis() {
        long durationMillis = verificationOverTimeImpl.getDuration();
        Assert.assertEquals(1000, durationMillis);
    }

    @Test
    public void getDelegate_shouldReturnDelegateVerificationMode() {
        VerificationMode delegate = verificationOverTimeImpl.getDelegate();
        Assert.assertNotNull(delegate);
    }

}