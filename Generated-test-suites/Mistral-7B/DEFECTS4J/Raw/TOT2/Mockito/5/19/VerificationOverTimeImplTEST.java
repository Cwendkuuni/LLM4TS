import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.verification.VerificationData;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class VerificationOverTimeImplTest {

    private VerificationMode verificationMode;
    private VerificationData verificationData;

    @Before
    public void setUp() {
        verificationMode = mock(VerificationMode.class);
        verificationData = new VerificationData(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void testVerifySatisfiedBeforeTimeout() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1, 100, verificationMode, true);
        verificationOverTime.verify(verificationData);
        verify(verificationMode).verify(verificationData);
    }

    @Test
    public void testVerifyNotSatisfiedBeforeTimeout() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1, 100, verificationMode, false);
        verificationOverTime.verify(verificationData);
        verify(verificationMode, Mockito.times(2)).verify(verificationData);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyTimeout() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1000, 100, verificationMode, false);
        verificationOverTime.verify(verificationData);
    }
}