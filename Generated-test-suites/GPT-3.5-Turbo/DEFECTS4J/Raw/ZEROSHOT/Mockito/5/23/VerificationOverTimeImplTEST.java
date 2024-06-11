import org.junit.Test;

import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {

    @Test
    public void testVerify_successReturnOnSuccess() {
        // Given
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        Timer timer = mock(Timer.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
        VerificationData data = mock(VerificationData.class);

        // When
        verificationOverTime.verify(data);

        // Then
        verify(timer).start();
        verify(delegate).verify(data);
    }

    @Test
    public void testVerify_successNotReturnOnSuccess() {
        // Given
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = false;
        Timer timer = mock(Timer.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
        VerificationData data = mock(VerificationData.class);

        // When
        verificationOverTime.verify(data);

        // Then
        verify(timer).start();
        verify(delegate).verify(data);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_delegateThrowsAssertionError() {
        // Given
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        Timer timer = mock(Timer.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
        VerificationData data = mock(VerificationData.class);
        when(delegate.verify(data)).thenThrow(MockitoAssertionError.class);

        // When
        verificationOverTime.verify(data);

        // Then (expected exception)
    }

    @Test(expected = org.mockito.exceptions.verification.junit.ArgumentsAreDifferent.class)
    public void testVerify_delegateThrowsArgumentsAreDifferent() {
        // Given
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        Timer timer = mock(Timer.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
        VerificationData data = mock(VerificationData.class);
        when(delegate.verify(data)).thenThrow(org.mockito.exceptions.verification.junit.ArgumentsAreDifferent.class);

        // When
        verificationOverTime.verify(data);

        // Then (expected exception)
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_delegateThrowsAssertionError_noRecoverFromFailure() {
        // Given
        long pollingPeriodMillis = 100;
        long durationMillis = 1000;
        VerificationMode delegate = mock(VerificationMode.class);
        boolean returnOnSuccess = true;
        Timer timer = mock(Timer.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, timer);
        VerificationData data = mock(VerificationData.class);

        // When
        verificationOverTime.handleVerifyException(new MockitoAssertionError());

        // Then (expected exception)
    }
}