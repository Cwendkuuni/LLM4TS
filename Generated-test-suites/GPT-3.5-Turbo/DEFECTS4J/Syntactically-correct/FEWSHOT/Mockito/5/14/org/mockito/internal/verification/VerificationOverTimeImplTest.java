
package org.mockito.internal.verification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.exceptions.MockVerificationAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

public class VerificationOverTimeImplTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testVerify_Successful() {
        VerificationMode delegate = new VerificationModeStub();
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(100, 1000, delegate, true, new TimerStub());

        verificationMode.verify(new VerificationDataStub());

        assertTrue(delegate.isVerifyCalled());
    }

    @Test
    public void testVerify_Unsuccessful_ReturnOnSuccessTrue() {
        VerificationMode delegate = new VerificationModeStubFalse();
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(100, 1000, delegate, true, new TimerStub());

        exceptionRule.expect(MockVerificationAssertionError.class);
        verificationMode.verify(new VerificationDataStub());
    }

    @Test
    public void testVerify_Unsuccessful_ReturnOnSuccessFalse() {
        VerificationMode delegate = new VerificationModeStubFalse();
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(100, 1000, delegate, false, new TimerStub());

        exceptionRule.expect(MockVerificationAssertionError.class);
        verificationMode.verify(new VerificationDataStub());
    }

    @Test
    public void testGetPollingPeriod() {
        VerificationMode delegate = new VerificationModeStub();
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(100, 1000, delegate, true, new TimerStub());

        assertEquals(100, verificationMode.getPollingPeriod());
    }

    @Test
    public void testGetDuration() {
        VerificationMode delegate = new VerificationModeStub();
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(100, 1000, delegate, true, new TimerStub());

        assertEquals(1000, verificationMode.getDuration());
    }

    @Test
    public void testGetDelegate() {
        VerificationMode delegate = new VerificationModeStub();
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(100, 1000, delegate, true, new TimerStub());

        assertEquals(delegate, verificationMode.getDelegate());
    }
}

class VerificationModeStub implements VerificationMode {

    private boolean verifyCalled = false;

    public boolean isVerifyCalled() {
        return verifyCalled;
    }

    @Override
    public void verify(VerificationData data) {
        verifyCalled = true;
    }
}

class VerificationModeStubFalse implements VerificationMode {

    @Override
    public void verify(VerificationData data) {
        fail();
    }
}

class VerificationDataStub implements VerificationData {

    @Override
    public Object getWanted() {
        return null;
    }

    @Override
    public int getNumberOfActualInvocations() {
        return 0;
    }

    @Override
    public String getMockitoMethodName() {
        return null;
    }

    @Override
    public boolean hasVerificationThatMatches() {
        return false;
    }
}

class TimerStub implements Timer {

    @Override
    public void start() {

    }

    @Override
    public boolean isCounting() {
        return false;
    }
}
