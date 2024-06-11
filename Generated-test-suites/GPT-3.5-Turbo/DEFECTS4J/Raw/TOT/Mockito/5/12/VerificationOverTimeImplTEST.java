import org.junit.Test;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockito.internal.verification.api.VerificationData;

import static org.mockito.Mockito.*;

public class VerificationOverTimeImplTest {
    
    @Test
    public void verify_shouldReturnIfDelegateIsSatisfiedBeforeDurationHasPassed() {
        // Arrange
        VerificationData data = mock(VerificationData.class);
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate instanceof org.mockito.verification.AtMost).thenReturn(false);
        when(delegate instanceof org.mockito.verification.NoMoreInteractions).thenReturn(false);
        
        // Act
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 200, delegate, true);
        verificationOverTime.verify(data);
        
        // Assert
        verify(delegate).verify(data);
    }
    
    @Test(expected = MockitoAssertionError.class)
    public void verify_shouldThrowIfDelegateDoesNotSucceedBeforeTimeout() {
        // Arrange
        VerificationData data = mock(VerificationData.class);
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate instanceof org.mockito.verification.AtMost).thenReturn(true);
        
        // Act
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 200, delegate, false);
        verificationOverTime.verify(data);
        
        // Assert
        verify(delegate).verify(data);
    }
    
    @Test(expected = ArgumentsAreDifferent.class)
    public void verify_shouldThrowIfArgumentsAreDifferent() {
        // Arrange
        VerificationData data = mock(VerificationData.class);
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate instanceof org.mockito.verification.AtMost).thenReturn(false);
        when(delegate instanceof org.mockito.verification.NoMoreInteractions).thenReturn(false);
        doThrow(new ArgumentsAreDifferent()).when(delegate).verify(data);
        
        // Act
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 200, delegate, true);
        verificationOverTime.verify(data);
        
        // Assert
        verify(delegate).verify(data);
    }
    
    @Test(expected = InterruptedException.class)
    public void verify_shouldThrowIfThreadIsInterruptedWhileSleeping() {
        // Arrange
        VerificationData data = mock(VerificationData.class);
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate instanceof org.mockito.verification.AtMost).thenReturn(false);
        when(delegate instanceof org.mockito.verification.NoMoreInteractions).thenReturn(false);
        doAnswer(invocation -> {
            Thread.currentThread().interrupt();
            return null;
        }).when(delegate).verify(data);
        
        // Act
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 200, delegate, true);
        verificationOverTime.verify(data);
        
        // Assert
        verify(delegate).verify(data);
    }
    
}