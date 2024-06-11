import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockito.verification.VerificationMode;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {

    private VerificationOverTimeImpl verificationOverTimeImpl;
    private VerificationData mockVerificationData;
    private VerificationMode mockVerificationMode;
    private Timer mockTimer;

    @Before
    public void setUp() {
        mockVerificationData = mock(VerificationData.class);
        mockVerificationMode = mock(VerificationMode.class);
        mockTimer = mock(Timer.class);
        verificationOverTimeImpl = new VerificationOverTimeImpl(1000L, 5000L, mockVerificationMode, true, mockTimer);
    }

    @Test
    public void testVerify_ReturnOnSuccess() {
        // Arrange
        when(mockTimer.isCounting()).thenReturn(true);
        doThrow(MockitoAssertionError.class)
                .doNothing()
                .when(mockVerificationMode).verify(mockVerificationData);

        // Act
        verificationOverTimeImpl.verify(mockVerificationData);

        // Assert
        verify(mockTimer).start();
        verify(mockVerificationMode, times(2)).verify(mockVerificationData);
        verify(mockTimer, times(5)).isCounting();
    }

    @Test
    public void testVerify_NotReturnOnSuccess() {
        // Arrange
        when(mockTimer.isCounting()).thenReturn(true);
        doThrow(MockitoAssertionError.class)
                .doNothing()
                .when(mockVerificationMode).verify(mockVerificationData);

        // Act
        verificationOverTimeImpl = new VerificationOverTimeImpl(1000L, 5000L, mockVerificationMode, false, mockTimer);
        verificationOverTimeImpl.verify(mockVerificationData);

        // Assert
        verify(mockTimer).start();
        verify(mockVerificationMode, times(2)).verify(mockVerificationData);
        verify(mockTimer, times(5)).isCounting();
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_ThrowsMockitoAssertionError() {
        // Arrange
        when(mockTimer.isCounting()).thenReturn(true);
        doThrow(MockitoAssertionError.class).when(mockVerificationMode).verify(mockVerificationData);

        // Act
        verificationOverTimeImpl.verify(mockVerificationData);

        // Assert
        verify(mockTimer).start();
        verify(mockVerificationMode).verify(mockVerificationData);
        verify(mockTimer, times(1)).isCounting();
    }

    @Test(expected = ArgumentsAreDifferent.class)
    public void testVerify_ThrowsArgumentsAreDifferent() {
        // Arrange
        when(mockTimer.isCounting()).thenReturn(true);
        doThrow(ArgumentsAreDifferent.class).when(mockVerificationMode).verify(mockVerificationData);

        // Act
        verificationOverTimeImpl.verify(mockVerificationData);

        // Assert
        verify(mockTimer).start();
        verify(mockVerificationMode).verify(mockVerificationData);
        verify(mockTimer, times(1)).isCounting();
    }

    @Test(expected = MockitoAssertionError.class)
    public void testVerify_ThrowsMockitoAssertionError_CannotRecoverFromFailure() {
        // Arrange
        when(mockTimer.isCounting()).thenReturn(true);
        doThrow(MockitoAssertionError.class).when(mockVerificationMode).verify(mockVerificationData);
        verificationOverTimeImpl = new VerificationOverTimeImpl(1000L, 5000L, mockVerificationMode, true, mockTimer);

        // Act
        verificationOverTimeImpl.verify(mockVerificationData);

        // Assert
        verify(mockTimer).start();
        verify(mockVerificationMode).verify(mockVerificationData);
        verify(mockTimer, times(1)).isCounting();
    }

    @Test(expected = ArgumentsAreDifferent.class)
    public void testVerify_ThrowsArgumentsAreDifferent_CannotRecoverFromFailure() {
        // Arrange
        when(mockTimer.isCounting()).thenReturn(true);
        doThrow(ArgumentsAreDifferent.class).when(mockVerificationMode).verify(mockVerificationData);
        verificationOverTimeImpl = new VerificationOverTimeImpl(1000L, 5000L, mockVerificationMode, true, mockTimer);

        // Act
        verificationOverTimeImpl.verify(mockVerificationData);

        // Assert
        verify(mockTimer).start();
        verify(mockVerificationMode).verify(mockVerificationData);
        verify(mockTimer, times(1)).isCounting();
    }

    @Test
    public void testGetPollingPeriod() {
        // Act
        long pollingPeriod = verificationOverTimeImpl.getPollingPeriod();

        // Assert
        assertEquals(1000L, pollingPeriod);
    }

    @Test
    public void testGetDuration() {
        // Act
        long duration = verificationOverTimeImpl.getDuration();

        // Assert
        assertEquals(5000L, duration);
    }

    @Test
    public void testGetDelegate() {
        // Act
        VerificationMode delegate = verificationOverTimeImpl.getDelegate();

        // Assert
        assertEquals(mockVerificationMode, delegate);
    }

}