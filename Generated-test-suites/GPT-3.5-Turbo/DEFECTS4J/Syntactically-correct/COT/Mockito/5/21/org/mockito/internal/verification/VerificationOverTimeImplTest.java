import org.junit.Test;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.verification.VerificationData;

import static org.mockito.Mockito.mock;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerify_ReturnOnSuccessTrue() {
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(100, 500, mock(VerificationData.class), true);
        verificationMode.verify(mock(VerificationData.class));
    }
}