
package org.mockito.internal.verification;

import org.junit.Test;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.mock;

public class VerificationOverTimeImplTest {

    @Test
    public void verify_withDelegateVerificationMode_verifyCalled() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1000, 2000, delegate, true);
        VerificationData data = mock(VerificationData.class);

        verificationOverTime.verify(data);

        verify(delegate).verify(data);
    }

    @Test(expected = ArgumentsAreDifferent.class)
    public void verify_withDelegateVerificationMode_throwExceptionIfDelegateFails() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1000, 2000, delegate, true);
        VerificationData data = mock(VerificationData.class);

        doThrow(new ArgumentsAreDifferent()).when(delegate).verify(data);

        verificationOverTime.verify(data);
    }

    @Test(expected = AssertionError.class)
    public void verify_withDelegateVerificationMode_throwExceptionIfTimeoutExceeded() {
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1000, 2000, delegate, false);
        VerificationData data = mock(VerificationData.class);

        doThrow(new MockitoAssertionError()).when(delegate).verify(data);

        verificationOverTime.verify(data);
    }

}
