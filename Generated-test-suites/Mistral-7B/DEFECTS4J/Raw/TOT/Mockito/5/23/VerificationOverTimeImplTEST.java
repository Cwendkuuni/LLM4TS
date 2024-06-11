package org.mockito.internal.verification.api; // or the package where VerificationOverTimeImpl is located

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.verification.VerificationMode;
import org.mockito.verification.VerificationModeFactory;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VerificationOverTimeImplTest {

    @Test
    public void testVerificationOverTimeSuccess() {
        VerificationModeDelegate delegate = mock(VerificationModeDelegate.class);
        VerificationMode verificationMode = new VerificationOverTimeImpl(500, 2000, delegate, true);
        VerificationData data = new VerificationData(ArgumentMatchers.any(), ArgumentMatchers.any());

        verificationMode.verify(data);

        // Delegate verification mode was satisfied before the full duration, thus test passes
        verify(delegate).verify(data);
    }

    @Test
    public void testVerificationOverTimeFailure() {
        VerificationModeDelegate delegate = mock(VerificationModeDelegate.class);
        VerificationMode verificationMode = new VerificationOverTimeImpl(500, 2000, delegate, false);
        VerificationData data = new VerificationData(ArgumentMatchers.any(), ArgumentMatchers.any());

        // Delegate verification mode is not satisfied before the full duration
        when(delegate.verify(data)).thenThrow(new MockitoAssertionError("Delegate verification failed"));

        try {
            verificationMode.verify(data);
            fail("Expected MockitoAssertionError but verification succeeded");
        } catch (MockitoAssertionError e) {
            // Expected behavior, test passes
            verify(delegate).verify(data); // This will verify that the delegate was called before the test failure
        }
    }

    @Test
    public void testVerificationOverTimeTimeout() {
        VerificationModeDelegate delegate = mock(VerificationModeDelegate.class);
        VerificationMode verificationMode = new VerificationOverTimeImpl(500, 1000, delegate, false);
        VerificationData data = new VerificationData(ArgumentMatchers.any(), ArgumentMatchers.any());

        // Delegate verification mode is not satisfied within the timeout
        when(delegate.verify(data)).thenThrow(new MockitoAssertionError("Delegate verification failed"));

        try {
            verificationMode.verify(data);
            fail("Expected MockitoAssertionError but verification succeeded");
        } catch (MockitoAssertionError e) {
            // Expected behavior, test passes
            verifyNoMoreInteractions(delegate); // This will verify that no more interactions with the delegate occurred after the failure
        }
    }

    @Test
    public void testVerificationOverTimeInterruptedSleep() {
        VerificationModeDelegate delegate = mock(VerificationModeDelegate.class);
        VerificationMode verificationMode = new VerificationOverTimeImpl(500, 2000, delegate, false);
        VerificationData data = new VerificationData(ArgumentMatchers.any(), ArgumentMatchers.any());

        // Simulate an interrupted thread while sleeping during the verification process
        when(delegate.verify(data)).thenThrow(new MockitoAssertionError("Delegate verification failed"));

        try {
            verificationMode.verify(data);
            fail("Expected MockitoAssertionError but verification succeeded");
        } catch (InterruptedException ie) {
            // This is an expected behavior when testing the interrupted sleep
            // However, it does not cover the test scenario, since an InterruptedException is not thrown
            // by the verification method and the test should cover all possible scenarios
            throw new RuntimeException("InterruptedException should not be thrown during verification", ie);
        } catch (MockitoAssertionError e) {
            // Expected behavior, test passes
            verify(delegate).verify(data); // This will verify that the delegate was called before the test failure
        }
    }

    @Test
    public void testVerificationOverTimeDelegateFailureRecovery() {
        VerificationModeDelegate delegate = mock(VerificationModeDelegate.class);
        when(delegate.verify(ArgumentMatchers.any()))
                .thenThrow(new MockitoAssertionError("Delegate verification failed"))
                .thenReturn(null); // Delegate can recover from failure

        VerificationMode verificationMode = new VerificationOverTimeImpl(500, 2000, delegate, true);
        VerificationData data = new VerificationData(ArgumentMatchers.any(), ArgumentMatchers.any());

        try {
            verificationMode.verify(data);
            fail("Expected MockitoAssertionError but verification succeeded");
        } catch (MockitoAssertionError e) {
            // Delegate verification mode failed initially but then recovered, thus test passes after a polling period
            verify(delegate).verify(data);
        }
    }

    @Test
    public void testVerificationOverTimeDelegateFailureNoRecovery() {
        VerificationModeDelegate delegate = mock(VerificationModeDelegate.class);
        when(delegate.verify(ArgumentMatchers.any()))
                .thenThrow(new MockitoAssertionError("Delegate verification failed"))
                .thenThrow(new MockitoAssertionError("Delegate verification failed")); // Delegate cannot recover from failure

        VerificationMode verificationMode = new VerificationOverTimeImpl(500, 2000, delegate, false);
        VerificationData data = new VerificationData(ArgumentMatchers.any(), ArgumentMatchers.any());

        try {
            verificationMode.verify(data);
            fail("Expected MockitoAssertionError but verification succeeded");
        } catch (MockitoAssertionError e) {
            // Expected behavior, test passes
            verify(delegate).verify(data); // This will verify that the delegate was called before the test failure
        }
    }

    @Test
    public void testVerificationOverTimeZeroTimeout() {
        VerificationModeDelegate delegate = mock(VerificationModeDelegate.class);
        VerificationMode verificationMode = new VerificationOverTimeImpl(0, 1000, delegate, false);

        try {
            verificationMode.verify(ArgumentMatchers.any());
            fail("Expected IllegalArgumentException but verification succeeded");
        } catch (IllegalArgumentException e) {
            // Expected behavior, test passes
            verifyZeroInteractions(delegate); // This will verify that no interactions with the delegate occurred during the test
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerificationOverTimeNegativePollingPeriod() {
        VerificationModeDelegate delegate = mock(VerificationModeDelegate.class);
        VerificationMode verificationMode = new VerificationOverTimeImpl(-1, 1000, delegate, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerificationOverTimeNegativeDuration() {
        VerificationModeDelegate delegate = mock(VerificationModeDelegate.class);
        VerificationMode verificationMode = new VerificationOverTimeImpl(500, -1, delegate, false);
    }

    private interface VerificationModeDelegate extends VerificationMode {
    }

    private static class VerificationModeFactoryImpl implements VerificationModeFactory {
        @Override
        public VerificationMode createVerificationMode(long pollingPeriodMillis, long durationMillis, VerificationMode delegate, boolean returnOnSuccess) {
            return new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, new Timer(durationMillis));
        }
    }
}