
package org.mockito.internal.verification;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;
import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {
    private VerificationOverTimeImpl verificationMode;

    @Before
    public void setUp() {
        verificationMode = new VerificationOverTimeImpl(100, 500, mock(VerificationMode.class), true);
    }

    @Test
    public void testVerify_ReturnOnSuccess() {
        VerificationData data = mock(VerificationData.class);
        Timer timer = mock(Timer.class);
        when(timer.isCounting()).thenReturn(true, true, true, true, false);
        when(data.getArguments()).thenReturn(new Object[]{});

        MockitoAssertionError e = new MockitoAssertionError("Verification failed");
        doThrow(e).when(verificationMode.getDelegate()).verify(data);

        verificationMode.verify(data);

        verify(verificationMode.getDelegate(), times(4)).verify(data);
        verify(timer, times(5)).isCounting();
        verify(verificationMode, times(5)).handleVerifyException(e);
        verify(verificationMode, never()).sleep(anyLong());
    }

    @Test
    public void testVerify_NotReturnOnSuccess() {
        VerificationData data = mock(VerificationData.class);
        Timer timer = mock(Timer.class);
        when(timer.isCounting()).thenReturn(true, true, true, true, false);
        when(data.getArguments()).thenReturn(new Object[]{});

        MockitoAssertionError e = new MockitoAssertionError("Verification failed");
        doThrow(e).when(verificationMode.getDelegate()).verify(data);

        verificationMode = new VerificationOverTimeImpl(100, 500, mock(VerificationMode.class), false);
        verificationMode.verify(data);


        verify(verificationMode.getDelegate(), times(4)).verify(data);
        verify(timer, times(5)).isCounting();
        verify(verificationMode, times(5)).handleVerifyException(e);
        verify(verificationMode, times(4)).sleep(100);
    }

    @Test(expected = AssertionError.class)
    public void testVerify_ThrowAssertionError() {
        VerificationData data = mock(VerificationData.class);
        Timer timer = mock(Timer.class);
        when(timer.isCounting()).thenReturn(true, true, true, true, false);
        when(data.getArguments()).thenReturn(new Object[]{});

        MockitoAssertionError e = new MockitoAssertionError("Verification failed");
        doThrow(e).when(verificationMode.getDelegate()).verify(data);

        verificationMode = new VerificationOverTimeImpl(100, 500, mock(AtMost.class), true);
        verificationMode.verify(data);

        verify(verificationMode.getDelegate(), times(1)).verify(data);
        verify(timer, times(1)).isCounting();
        verify(verificationMode, times(1)).handleVerifyException(e);
        verify(verificationMode, never()).sleep(anyLong());
    }
}
