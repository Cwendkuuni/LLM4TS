import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.api.VerificationData;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerify() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 500, null, true);
        VerificationData data = new VerificationData();

        try {
            verificationOverTime.verify(data);
        } catch (MockitoAssertionError e) {
            fail("Verification failed");
        }
    }

    @Test
    public void testVerifyWithNegativePollingPeriod() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(-100, 500, null, true);
        VerificationData data = new VerificationData();

        try {
            verificationOverTime.verify(data);
            fail("Verification with negative polling period should fail");
        } catch (MockitoAssertionError e) {
            // expected
        }
    }

    @Test
    public void testVerifyWithNegativeDuration() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, -500, null, true);
        VerificationData data = new VerificationData();

        try {
            verificationOverTime.verify(data);
            fail("Verification with negative duration should fail");
        } catch (MockitoAssertionError e) {
            // expected
        }
    }

    @Test
    public void testVerifyWithNullDelegate() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 500, null, true);
        VerificationData data = new VerificationData();

        try {
            verificationOverTime.verify(data);
            fail("Verification with null delegate should fail");
        } catch (MockitoAssertionError e) {
            // expected
        }
    }
}