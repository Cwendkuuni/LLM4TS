import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;

import static org.junit.Assert.*;

public class VerificationOverTimeImplTest {
    private VerificationMode verificationModeMock;
    private VerificationOverTimeImpl verificationOverTime;

    @Before
    public void setUp() {
        verificationModeMock = Mockito.mock(VerificationMode.class);
        verificationOverTime = new VerificationOverTimeImpl(100, 500, verificationModeMock, true);
    }

    @Test
    public void testVerifySuccess() {
        VerificationData data = Mockito.mock(VerificationData.class);
        verificationOverTime.verify(data);
        assertTrue(verificationOverTime.isSatisfied());
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyFailureWithRecoverableMode() {
        VerificationData data = Mockito.mock(VerificationData.class);
        Mockito.doThrow(new MockitoAssertionError("Expected invocation on this mock to occur once but it occurred 2 times")).when(verificationModeMock).verify(data);

        verificationOverTime.verify(data);
        sleep(101); // sleep for longer than the polling period
        verificationOverTime.verify(data); // should now pass
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerifyFailureWithNonRecoverableMode() {
        VerificationData data = Mockito.mock(VerificationData.class);
        VerificationMode nonRecoverableVerificationMode = Mockito.mock(AtMost.class);
        verificationOverTime = new VerificationOverTimeImpl(100, 500, nonRecoverableVerificationMode, true);

        Mockito.doThrow(new MockitoAssertionError("Expected invocation on this mock to occur once but it occurred 2 times")).when(nonRecoverableVerificationMode).verify(data);

        verificationOverTime.verify(data);
    }

    @Test(expected = ArgumentsAreDifferent.class)
    public void testVerifyFailureWithArgumentsDifferentException() {
        VerificationData data = Mockito.mock(VerificationData.class);
        Mockito.doThrow(new ArgumentsAreDifferent(Mockito.argument(), Mockito.argument())).when(verificationModeMock).verify(data);

        verificationOverTime.verify(data);
    }

    @Test
    public void testZeroPollingPeriodAndDuration() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(0, 0, verificationModeMock, true);
        VerificationData data = Mockito.mock(VerificationData.class);
        try {
            verificationOverTime.verify(data);
            fail("Expected MockitoAssertionError");
        } catch (MockitoAssertionError e) {
            // expected
        }
    }

    @Test
    public void testLongPollingPeriod() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(10000, 500, verificationModeMock, true);
        VerificationData data = Mockito.mock(VerificationData.class);
        try {
            verificationOverTime.verify(data);
            fail("Expected MockitoAssertionError");
        } catch (MockitoAssertionError e) {
            // expected
        }

        sleep(10010); // sleep for longer than the polling period
        verificationOverTime.verify(data); // should now pass
    }

    @Test
    public void testShortDuration() {
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1, verificationModeMock, true);
        VerificationData data = Mockito.mock(VerificationData.class);
        try {
            verificationOverTime.verify(data);
            fail("Expected MockitoAssertionError");
        } catch (MockitoAssertionError e) {
            // expected
        }

        sleep(1); // sleep for the duration
        verificationOverTime.verify(data); // should now pass
    }

    private void sleep(long sleep) {
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException ie) {
            // oups. not much luck.
        }
    }
}