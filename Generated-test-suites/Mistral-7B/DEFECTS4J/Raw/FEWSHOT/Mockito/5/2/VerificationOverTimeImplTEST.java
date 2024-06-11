package org.mockito.internal.verification;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.internal.verification.api.VerificationData.verificationData;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

public class VerificationOverTimeImplTest {

    @Test
    public void testNewInstance() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 100, delegate, true);
        assertSame(delegate, verificationOverTime.getDelegate());
        assertTrue(verificationOverTime.returnOnSuccess);
    }

    @Test
    public void testNewInstanceWithFalseReturnOnSuccess() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 100, delegate, false);
        assertSame(delegate, verificationOverTime.getDelegate());
        assertFalse(verificationOverTime.returnOnSuccess);
    }

    @Test
    public void testNewInstanceWithTimer() {
        VerificationMode delegate = mock(VerificationMode.class);
        Timer timer = mock(Timer.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 100, delegate, true, timer);
        assertSame(delegate, verificationOverTime.getDelegate());
        assertTrue(verificationOverTime.returnOnSuccess);
        assertSame(timer, verificationOverTime.timer);
    }

    @Test
    public void testVerifyNoCalls() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 100, delegate, true);

        VerificationData data = verificationData();
        verificationOverTime.verify(data);
        assertNull(data.getInvocations());
    }

    @Test
    public void testVerifyOneCall() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 100, delegate, true);

        VerificationData data = verificationData("testMethod");
        data.addInvocation("testMethod", "arg1", "arg2");

        verificationOverTime.verify(data);
        assertEquals(1, data.getInvocations().size());
    }

    @Test
    public void testVerifyNotSatisfiedBeforeTimeout() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(10, 50, delegate, true);

        VerificationData data = verificationData();

        AssertionError exception = assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(data));
        assertEquals("Delegate verification failed: " + delegate.verifyMessage(), exception.getMessage());
    }

    @Test
    public void testVerifySatisfiedBeforeTimeout() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 100, delegate, true);

        VerificationData data = verificationData();
        data.addInvocation("testMethod", "arg1", "arg2");

        verificationOverTime.verify(data);
        assertNull(data.getInvocations());
    }

    @Test
    public void testVerifySatisfiedAfterTimeout() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(10, 150, delegate, false);

        VerificationData data = verificationData();

        AssertionError exception = assertThrows(MockitoAssertionError.class, () -> verificationOverTime.verify(data));
        assertEquals("Delegate verification failed: " + delegate.verifyMessage(), exception.getMessage());

        // verify again after timeout
        data = verificationData();
        data.addInvocation("testMethod", "arg1", "arg2");

        verificationOverTime.verify(data);
        assertEquals(1, data.getInvocations().size());
    }

    @Test
    public void testCanRecoverFromFailure() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 100, delegate, true);
        assertTrue(verificationOverTime.canRecoverFromFailure(delegate));

        AtMost atMost = mock(AtMost.class);
        verificationOverTime = new VerificationOverTimeImpl(100, 100, atMost, true);
        assertFalse(verificationOverTime.canRecoverFromFailure(atMost));
    }

}