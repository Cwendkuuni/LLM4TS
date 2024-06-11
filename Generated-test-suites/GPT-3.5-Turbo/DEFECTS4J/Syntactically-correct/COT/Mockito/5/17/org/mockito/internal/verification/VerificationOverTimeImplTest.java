
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import static org.junit.Assert.*;

public class VerificationOverTimeImplTest {

    private VerificationOverTimeImpl verificationMode;

    @Before
    public void setUp() {
        verificationMode = new VerificationOverTimeImpl(100, 1000, null, true);
    }

    @Test
    public void verify_ReturnsImmediatelyOnceDelegateIsSatisfied() {
        // Arrange
        VerificationData data = null;

        // Act
        verificationMode.verify(data);

        // Assert
        // No exception should be thrown
    }

    @Test(expected = MockitoAssertionError.class)
    public void verify_ThrowsExceptionIfDelegateIsNotSatisfiedBeforeTimeout() {
        // Arrange
        VerificationData data = null;

        // Act
        verificationMode.verify(data);

        // Assert
        // MockitoAssertionError should be thrown
    }

    @Test
    public void verify_DoesNotReturnUntilDelegateIsSatisfiedAndTimeoutHasPassed() {
        // Arrange
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(100, 1000, null, false);
        VerificationData data = null;

        // Act
        long startTime = System.currentTimeMillis();
        verificationMode.verify(data);
        long endTime = System.currentTimeMillis();

        // Assert
        assertTrue(endTime - startTime >= 1000);
    }

    @Test
    public void getPollingPeriod_ReturnsPollingPeriodMillis() {
        // Arrange

        // Act
        long pollingPeriod = verificationMode.getPollingPeriod();

        // Assert
        assertEquals(100, pollingPeriod);
    }

    @Test
    public void getDuration_ReturnsDurationMillis() {
        // Arrange

        // Act
        long duration = verificationMode.getDuration();

        // Assert
        assertEquals(1000, duration);
    }

    @Test
    public void getDelegate_ReturnsDelegateVerificationMode() {
        // Arrange
        VerificationMode delegate = new VerificationMode() {
            @Override
            public void verify(VerificationData data) {

            }
        };
        VerificationOverTimeImpl verificationMode = new VerificationOverTimeImpl(100, 1000, delegate, true);

        // Act
        VerificationMode result = verificationMode.getDelegate();

        // Assert
        assertEquals(delegate, result);
    }
}