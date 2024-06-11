import org.junit.Test;
import static org.mockito.Mockito.*;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

public class VerificationOverTimeImplTest {

    @Test
    public void testConstructor1() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, delegate, false);
        assertNotNull(verification);
    }

    @Test
    public void testConstructor2() {
        VerificationMode delegate = mock(VerificationMode.class);
        Timer timer = mock(Timer.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, delegate, false, timer);
        assertNotNull(verification);
    }

    @Test
    public void testVerify() {
        VerificationData data = mock(VerificationData.class);
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, delegate, false);
        verification.verify(data);
    }

    @Test
    public void testGetPollingPeriod() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, delegate, false);
        assertEquals(1000, verification.getPollingPeriod());
    }

    @Test
    public void testGetDuration() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, delegate, false);
        assertEquals(5000, verification.getDuration());
    }

    @Test
    public void testGetDelegate() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verification = new VerificationOverTimeImpl(1000, 5000, delegate, false);
        assertEquals(delegate, verification.getDelegate());
    }
}