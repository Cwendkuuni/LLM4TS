import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.verification.VerificationMode;
import org.mockito.internal.util.Timer;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;

@RunWith(MockitoJUnitRunner.class)
public class VerificationOverTimeImplTest {

    @Test
    public void testConstructor1() {
        // Create VerificationOverTimeImpl object using the first constructor
        long pollingPeriodMillis = 1000;
        long durationMillis = 5000;
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        boolean returnOnSuccess = true;

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);

        // Verify that the object is created correctly with the given values
        assertEquals(pollingPeriodMillis, verificationOverTime.getPollingPeriod());
        assertEquals(durationMillis, verificationOverTime.getDuration());
        assertEquals(delegate, verificationOverTime.getDelegate());
    }

    @Test
    public void testConstructor2() {
        // Create VerificationOverTimeImpl object using the second constructor
        long pollingPeriodMillis = 1000;
        long durationMillis = 5000;
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        Timer timer = Mockito.mock(Timer.class);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);

        // Verify that the object is created correctly with the given values
        assertEquals(pollingPeriodMillis, verificationOverTime.getPollingPeriod());
        assertEquals(durationMillis, verificationOverTime.getDuration());
        assertEquals(delegate, verificationOverTime.getDelegate());
    }

    @Test
    public void testVerify() {
        // Create a mock VerificationData object
        VerificationData verificationData = Mockito.mock(VerificationData.class);

        // Create VerificationOverTimeImpl object and call the verify method
        long pollingPeriodMillis = 1000;
        long durationMillis = 5000;
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);

        verificationOverTime.verify(verificationData);

        // Verify that the verify method in the delegate verification mode is called correctly
        Mockito.verify(delegate).verify(verificationData);
    }

    @Test
    public void testGetPollingPeriod() {
        // Create VerificationOverTimeImpl object
        long pollingPeriodMillis = 1000;
        long durationMillis = 5000;
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);

        // Verify that the correct polling period is returned
        assertEquals(pollingPeriodMillis, verificationOverTime.getPollingPeriod());
    }

    @Test
    public void testGetDuration() {
        // Create VerificationOverTimeImpl object
        long pollingPeriodMillis = 1000;
        long durationMillis = 5000;
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);

        // Verify that the correct duration is returned
        assertEquals(durationMillis, verificationOverTime.getDuration());
    }

    @Test
    public void testGetDelegate() {
        // Create VerificationOverTimeImpl object
        long pollingPeriodMillis = 1000;
        long durationMillis = 5000;
        VerificationMode delegate = Mockito.mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);

        // Verify that the correct delegate verification mode is returned
        assertEquals(delegate, verificationOverTime.getDelegate());
    }
}