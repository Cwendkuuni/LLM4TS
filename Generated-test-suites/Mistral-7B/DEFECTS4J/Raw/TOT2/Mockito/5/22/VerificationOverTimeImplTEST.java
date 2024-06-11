import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.Mockito;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class VerificationOverTimeImplTest {
    private VerificationMode verificationMode;
    private VerificationData verificationData;

    @Before
    public void setUp() {
        this.verificationMode = mock(VerificationMode.class);
        this.verificationData = VerificationData.create( ArgumentMatchers.any(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(), this.verificationMode );
    }

    @Test
    public void testVerifySuccess() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 500, this.verificationMode, true);
        verificationOverTime.verify(this.verificationData);
        assertEquals(VerificationMode.VERIFIED, this.verificationMode.getState());
    }

    @Test
    public void testVerifyTimeout() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 50, this.verificationMode, true);
        try {
            verificationOverTime.verify(this.verificationData);
            fail("Expected MockitoAssertionError not thrown.");
        } catch (MockitoAssertionError e) {
            // expected
        }
        assertEquals(VerificationMode.VERIFICATION_FAILED, this.verificationMode.getState());
    }

    @Test
    public void testVerifyUnrecoverableException() {
        VerificationMode unrecoverableVerificationMode = mock(AtMost.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 500, unrecoverableVerificationMode, true);
        try {
            verificationOverTime.verify(this.verificationData);
            fail("Expected MockitoAssertionError not thrown.");
        } catch (MockitoAssertionError e) {
            // expected
        }
        assertEquals(VerificationMode.VERIFICATION_FAILED, this.verificationMode.getState());
    }

    // Add more test cases here for different polling periods, durations, null delegate, and null/invalid VerificationData

}