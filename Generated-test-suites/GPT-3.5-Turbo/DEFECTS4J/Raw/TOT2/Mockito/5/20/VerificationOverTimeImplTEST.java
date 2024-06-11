import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.mock;

public class VerificationOverTimeImplTest {

    private VerificationOverTimeImpl verificationOverTime;

    @Before
    public void setUp() {
        verificationOverTime = new VerificationOverTimeImpl(1000, 5000, mock(VerificationMode.class), false);
    }

    @Test
    public void testVerificationOverTimeConstructor() {
        new VerificationOverTimeImpl(1000, 5000, mock(VerificationMode.class), false);
    }

    @Test
    public void testVerifyMethod() {
        VerificationData data = mock(VerificationData.class);
        verificationOverTime.verify(data);
    }

    @Test
    public void testGetPollingPeriod() {
        long pollingPeriod = verificationOverTime.getPollingPeriod();
    }

    @Test
    public void testGetDuration() {
        long duration = verificationOverTime.getDuration();
    }

    @Test
    public void testGetDelegate() {
        VerificationMode delegate = verificationOverTime.getDelegate();
    }

}