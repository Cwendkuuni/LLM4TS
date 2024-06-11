import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerificationOverTimeImpl() {
        long pollingPeriodMillis = 100;
        long durationMillis = 500;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess);

        VerificationData data = mock(VerificationData.class);

        try {
            verificationOverTime.verify(data);
            assertTrue(true);
        } catch (MockitoAssertionError e) {
            assertTrue(false);
        }
    }

    @Test
    public void testCanRecoverFromFailure() {
        VerificationMode atMostVerification = mock(AtMost.class);
        VerificationMode noMoreInteractionsVerification = mock(NoMoreInteractions.class);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 500, atMostVerification, true);
        assertTrue(verificationOverTime.canRecoverFromFailure(atMostVerification));

        verificationOverTime = new VerificationOverTimeImpl(100, 500, noMoreInteractionsVerification, true);
        assertTrue(!verificationOverTime.canRecoverFromFailure(noMoreInteractionsVerification));
    }

}