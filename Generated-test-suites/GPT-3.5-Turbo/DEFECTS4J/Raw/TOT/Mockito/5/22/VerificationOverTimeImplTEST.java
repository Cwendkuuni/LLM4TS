import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Timer.class})
public class VerificationOverTimeImplTest {

    @Test
    public void testVerify_WhenDelegateIsSatisfiedBeforeTimeout_ReturnsImmediately() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1000, 5000, delegate, true);
        
        // Act
        verificationOverTime.verify(data);
        
        // Assert
        verify(delegate).verify(data);
    }

    @Test
    public void testVerify_WhenDelegateIsSatisfiedAfterTimeout_ThrowsAssertionError() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        
        doThrow(new MockitoAssertionError("Delegate verification failed")).when(delegate).verify(data);
        
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1000, 2000, delegate, false);
        
        // Act & Assert
        try {
            verificationOverTime.verify(data);
            fail("Expected AssertionError to be thrown");
        } catch (MockitoAssertionError e) {
            assertEquals("Delegate verification failed", e.getMessage());
            verify(delegate, times(2)).verify(data);
        }
    }

    @Test
    public void testVerify_WhenDelegateThrowsArgumentsAreDifferentException_ThrowsArgumentsAreDifferentException() {
        // Arrange
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        
        doThrow(new ArgumentsAreDifferent("Arguments are different")).when(delegate).verify(data);
        
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1000, 2000, delegate, false);
        
        // Act & Assert
        try {
            verificationOverTime.verify(data);
            fail("Expected ArgumentsAreDifferentException to be thrown");
        } catch (ArgumentsAreDifferent e) {
            assertEquals("Arguments are different", e.getMessage());
            verify(delegate, times(2)).verify(data);
        }
    }

    @Test
    public void testHandleVerifyException_WhenCanRecoverFromFailure_ReturnsError() {
        // Arrange
        VerificationMode verificationMode = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1000, 2000, verificationMode, false);
        
        AssertionError e = new AssertionError("Verification failed");
        
        // Act
        AssertionError result = verificationOverTime.handleVerifyException(e);
        
        // Assert
        assertSame(e, result);
        verify(verificationMode, never()).verify(any(VerificationData.class));
    }

    @Test(expected = AssertionError.class)
    public void testHandleVerifyException_WhenCannotRecoverFromFailure_ThrowsError() {
        // Arrange
        VerificationMode verificationMode = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1000, 2000, verificationMode, false);
        
        AssertionError e = new AssertionError("Verification failed");
        
        // Act & Assert
        verificationOverTime.handleVerifyException(e);
    }

    @Test
    public void testSleep_WhenInterruptedExceptionOccurs_CatchesException() {
        // Arrange
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1000, 2000, null, false);
        
        // Act
        verificationOverTime.sleep(1000);
        
        // Assert
        // No exception should be thrown
    }

    @Test
    public void testCanRecoverFromFailure_WhenVerificationModeIsNotAtMostOrNoMoreInteractions_ReturnsTrue() {
        // Arrange
        VerificationMode verificationMode = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1000, 2000, verificationMode, false);
        
        // Act
        boolean result = verificationOverTime.canRecoverFromFailure(verificationMode);
        
        // Assert
        assertTrue(result);
    }

    @Test
    public void testCanRecoverFromFailure_WhenVerificationModeIsAtMost_ReturnsFalse() {
        // Arrange
        VerificationMode verificationMode = mock(VerificationMode.class);
        when(verificationMode instanceof AtMost).thenReturn(true);
        
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1000, 2000, verificationMode, false);
        
        // Act
        boolean result = verificationOverTime.canRecoverFromFailure(verificationMode);
        
        // Assert
        assertFalse(result);
    }

    @Test
    public void testCanRecoverFromFailure_WhenVerificationModeIsNoMoreInteractions_ReturnsFalse() {
        // Arrange
        VerificationMode verificationMode = mock(VerificationMode.class);
        when(verificationMode instanceof NoMoreInteractions).thenReturn(true);
        
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1000, 2000, verificationMode, false);
        
        // Act
        boolean result = verificationOverTime.canRecoverFromFailure(verificationMode);
        
        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetPollingPeriod_ReturnsPollingPeriodMillis() {
        // Arrange
        VerificationMode verificationMode = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1000, 2000, verificationMode, false);
        
        // Act
        long result = verificationOverTime.getPollingPeriod();
        
        // Assert
        assertEquals(1000, result);
    }

    @Test
    public void testGetDuration_ReturnsDurationMillis() {
        // Arrange
        VerificationMode verificationMode = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1000, 2000, verificationMode, false);
        
        // Act
        long result = verificationOverTime.getDuration();
        
        // Assert
        assertEquals(2000, result);
    }

    @Test
    public void testGetDelegate_ReturnsDelegateVerificationMode() {
        // Arrange
        VerificationMode verificationMode = mock(VerificationMode.class);
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(1000, 2000, verificationMode, false);
        
        // Act
        VerificationMode result = verificationOverTime.getDelegate();
        
        // Assert
        assertSame(verificationMode, result);
    }
}