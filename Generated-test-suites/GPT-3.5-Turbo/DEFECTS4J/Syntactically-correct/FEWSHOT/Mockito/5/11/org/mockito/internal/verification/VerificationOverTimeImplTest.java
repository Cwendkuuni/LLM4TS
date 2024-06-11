
package org.mockito.internal.verification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

public class VerificationOverTimeImplTest {

    private VerificationMode delegate;
    private VerificationData data;
    private Timer timer;
    private VerificationOverTimeImpl verification;

    @Before
    public void setUp() {
        delegate = mock(VerificationMode.class);
        data = mock(VerificationData.class);
        timer = mock(Timer.class);
        verification = new VerificationOverTimeImpl(1000, 5000, delegate, true, timer);
    }

    @Test
    public void testConstructorAndGetters() {
        assertEquals(1000, verification.getPollingPeriod());
        assertEquals(5000, verification.getDuration());
        assertEquals(delegate, verification.getDelegate());
    }

    @Test
    public void testVerify_ReturnOnSuccess() {
        when(timer.isCounting()).thenReturn(true, true, false);
        
        verification.verify(data);
        
        verify(delegate, times(2)).verify(data);
        verify(timer).start();
        verify(timer, times(2)).isCounting();
        verify(timer).getElapsedTime();
    }

    @Test
    public void testVerify_DoNotReturnOnSuccess() {
        verification = new VerificationOverTimeImpl(1000, 5000, delegate, false, timer);

        when(timer.isCounting()).thenReturn(true, true, false);

        verification.verify(data);

        verify(delegate, times(2)).verify(data);
        verify(timer).start();
        verify(timer, times(2)).isCounting();
        verify(timer).getElapsedTime();
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_DelegateThrowsException() {
        when(delegate.verify(data)).thenThrow(new MockitoAssertionError("Delegate verification failed"));
        when(timer.isCounting()).thenReturn(true, true, false);

        verification.verify(data);

        verify(delegate, times(2)).verify(data);
        verify(timer).start();
        verify(timer, times(2)).isCounting();
        verify(timer).getElapsedTime();
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_DelegateThrowsAssertionError() {
        when(delegate.verify(data)).thenThrow(new MockitoAssertionError("Delegate verification failed"));
        when(timer.isCounting()).thenReturn(true, true, true, false);

        verification.verify(data);

        verify(delegate, times(3)).verify(data);
        verify(timer).start();
        verify(timer, times(3)).isCounting();
        verify(timer).getElapsedTime();
    }

    @Test(expected = AssertionError.class)
    public void testHandleVerifyException_CanNotRecoverFromFailure() {
        VerificationMode atMost = mock(VerificationMode.class);
        when(atMost instanceof AtMost).thenReturn(true);

        verification.handleVerifyException(new MockitoAssertionError("Delegate verification failed"));

        verify(delegate).verify(data);
        verify(timer).getElapsedTime();
    }

    @Test
    public void testHandleVerifyException_CanRecoverFromFailure() {
        VerificationMode verificationMode = mock(VerificationMode.class);
        when(verificationMode instanceof AtMost).thenReturn(false);
        when(timer.isCounting()).thenReturn(true, false);

        AssertionError error = new MockitoAssertionError("Delegate verification failed");
        AssertionError result = verification.handleVerifyException(error);

        verify(delegate).verify(data);
        verify(timer).isCounting();
        verify(timer).getElapsedTime();
        assertTrue(result == error);
    }

    @Test
    public void testSleep() throws InterruptedException {
        verification.sleep(1000);

        verify(timer).start();
        verify(timer).isCounting();
        verify(timer).getElapsedTime();
        verify(timer).sleep(1000);
    }
}
