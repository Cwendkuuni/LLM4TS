import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerify_ReturnOnSuccessTrue_DelegateSatisfiedBeforeTimeout() {
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(100, 1000, new DummyVerificationMode(), true);
        VerificationData data = new VerificationData();

        verificationMode.verify(data);
    }

    @Test
    public void testVerify_ReturnOnSuccessTrue_DelegateSatisfiedAfterTimeout() {
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(100, 1000, new DummyVerificationMode(), true);
        VerificationData data = new VerificationData();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        verificationMode.verify(data);
    }

    @Test
    public void testVerify_ReturnOnSuccessFalse_DelegateSatisfiedBeforeTimeout() {
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(100, 1000, new DummyVerificationMode(), false);
        VerificationData data = new VerificationData();

        verificationMode.verify(data);
    }

    @Test
    public void testVerify_ReturnOnSuccessFalse_DelegateSatisfiedAfterTimeout() {
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(100, 1000, new DummyVerificationMode(), false);
        VerificationData data = new VerificationData();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        verificationMode.verify(data);
    }

    private class DummyVerificationMode implements VerificationMode {

        public void verify(VerificationData data) throws MockitoAssertionError {

        }
    }
}