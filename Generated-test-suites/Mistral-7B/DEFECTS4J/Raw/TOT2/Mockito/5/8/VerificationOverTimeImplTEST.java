import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerify() {
        VerificationMode delegateMock = Mockito.mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTimeImpl = new VerificationOverTimeImpl(100, 500, delegateMock, true);
        VerificationData data = ArgumentMatchers.any(VerificationData.class);

        verificationOverTimeImpl.verify(data);
        Mockito.verify(delegateMock).verify(data);
    }
}