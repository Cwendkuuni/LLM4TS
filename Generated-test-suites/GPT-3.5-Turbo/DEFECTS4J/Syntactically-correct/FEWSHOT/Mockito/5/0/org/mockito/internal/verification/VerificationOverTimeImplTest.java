import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;
import static org.junit.Assert.*;
import org.junit.Test;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerify_withReturnOnSuccess() {
        VerificationData data = createMockVerificationData();
        VerificationMode delegate = createMockVerificationMode(true);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1000, 5000, delegate, true);

        verificationOverTime.verify(data);

        verify(delegate).verify(data);
    }

    @Test
    public void testVerify_withoutReturnOnSuccess() {
        VerificationData data = createMockVerificationData();
        VerificationMode delegate = createMockVerificationMode(true);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1000, 5000, delegate, false);

        verificationOverTime.verify(data);

        assertNotNull(verificationOverTime);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_timeout() {
        VerificationData data = createMockVerificationData();
        VerificationMode delegate = createMockVerificationMode(false);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1000, 2000, delegate, true);

        verificationOverTime.verify(data);
    }

    private VerificationData createMockVerificationData() {
        return new VerificationData() {
            @Override
            public Object getWanted() {
                return null;
            }

            @Override
            public int getActualCount() {
                return 0;
            }
        };
    }

    private VerificationMode createMockVerificationMode(boolean succeeded) {
        return new VerificationMode() {
            @Override
            public void verify(VerificationData data) {
                if (!succeeded) {
                    throw new MockitoAssertionError("Verification failed");
                }
            }
        };
    }
}