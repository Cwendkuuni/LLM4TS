import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.verification.VerificationAfterDelay;
import org.mockito.verification.VerificationWithTimeout;

public class VerificationOverTimeImplTest {

@Test
public void verify_withDelegateSatisfiedBeforeTimeout_returnImmediately() {
    // Arrange
    VerificationMode delegate = mock(VerificationMode.class);
    when(delegate.toString()).thenReturn("delegate");
    
    // Act
    VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, true);
    verificationOverTime.verify(mock(VerificationData.class));
    
    // Assert
    verify(delegate).verify(any(VerificationData.class));
}

@Test
public void verify_withDelegateSatisfiedAfterTimeout_throwAssertionError() {
    // Arrange
    VerificationMode delegate = mock(VerificationMode.class);
    when(delegate.toString()).thenReturn("delegate");
    doThrow(new MockitoAssertionError("Delegate not satisfied within timeout")).when(delegate).verify(any(VerificationData.class));
    
    // Act
    VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, true);
    try {
        verificationOverTime.verify(mock(VerificationData.class));
    } catch (MockitoAssertionError e) {
        // Assert
        verify(delegate).verify(any(VerificationData.class));
        return;
    }
    
    fail("Expected MockitoAssertionError");
}

@Test
public void verify_withDelegateSatisfiedBeforeTimeoutAndFullTimePass_returnAfterFullTime() {
    // Arrange
    VerificationMode delegate = mock(VerificationMode.class);
    when(delegate.toString()).thenReturn("delegate");
    
    // Act
    VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, false);
    verificationOverTime.verify(mock(VerificationData.class));
    
    // Assert
    verify(delegate, times(10)).verify(any(VerificationData.class));
}

@Test
public void verify_withDelegateSatisfiedAtLastIterationAfterFullTime_throwAssertionError() {
    // Arrange
    VerificationMode delegate = mock(VerificationMode.class);
    when(delegate.toString()).thenReturn("delegate");
    doThrow(new MockitoAssertionError("Delegate not satisfied within timeout")).when(delegate).verify(any(VerificationData.class));
    
    // Act
    VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, false);
    try {
        verificationOverTime.verify(mock(VerificationData.class));
    } catch (MockitoAssertionError e) {
        // Assert
        verify(delegate, times(10)).verify(any(VerificationData.class));
        return;
    }
    
    fail("Expected MockitoAssertionError");
}

@Test
public void verify_withDelegateThrowingMockitoAssertionError_canRecoverFromFailure() {
    // Arrange
    VerificationMode delegate = mock(VerificationMode.class);
    when(delegate.toString()).thenReturn("delegate");
    doThrow(new MockitoAssertionError("Delegate not satisfied within timeout")).doNothing().when(delegate).verify(any(VerificationData.class));
    
    // Act
    VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, false);
    verificationOverTime.verify(mock(VerificationData.class));
    
    // Assert
    verify(delegate, times(2)).verify(any(VerificationData.class));
}

@Test(expected = MockitoAssertionError.class)
public void verify_withDelegateThrowingMockitoAssertionError_canNotRecoverFromFailure_throwAssertionError() {
    // Arrange
    VerificationMode delegate = mock(VerificationMode.class);
    when(delegate.toString()).thenReturn("delegate");
    doThrow(new MockitoAssertionError("Delegate not satisfied within timeout")).when(delegate).verify(any(VerificationData.class));
    
    // Act
    VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, false);
    verificationOverTime.verify(mock(VerificationData.class));
    
    // Assert
    verify(delegate, times(1)).verify(any(VerificationData.class));
}

@Test
public void handleVerifyException_withCanRecoverFromFailure_sleepBeforeReturning() {
    // Arrange
    VerificationMode delegate = mock(VerificationMode.class);
    when(delegate.toString()).thenReturn("delegate");
    doThrow(new MockitoAssertionError("Delegate not satisfied within timeout")).when(delegate).verify(any(VerificationData.class));
    
    VerificationOverTimeImpl verificationOverTime = spy(new VerificationOverTimeImpl(100, 1000, delegate, false));
    
    // Act
    AssertionError exception = new AssertionError();
    doReturn(exception).when(verificationOverTime).handleVerifyException(any(AssertionError.class));
    verificationOverTime.verify(mock(VerificationData.class));
    
    // Assert
    verify(delegate, times(1)).verify(any(VerificationData.class));
    verify(verificationOverTime, times(10)).sleep(100);
}

@Test(expected = MockitoAssertionError.class)
public void handleVerifyException_withCanNotRecoverFromFailure_throwOriginalError() {
    // Arrange
    VerificationMode delegate = mock(VerificationMode.class);
    when(delegate.toString()).thenReturn("delegate");
    doThrow(new MockitoAssertionError("Delegate not satisfied within timeout")).when(delegate).verify(any(VerificationData.class));
    
    VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, false);
    
    // Act
    verificationOverTime.verify(mock(VerificationData.class));
    
    // Assert
    verify(delegate, times(1)).verify(any(VerificationData.class));
}

@Test
public void sleep_interrupted_whileSleeping_doNothing() {
    // Arrange
    VerificationMode delegate = mock(VerificationMode.class);
    when(delegate.toString()).thenReturn("delegate");
    
    VerificationOverTimeImpl verificationOverTime = spy(new VerificationOverTimeImpl(100, 1000, delegate, false));
    
    // Act
    Thread.currentThread().interrupt();
    verificationOverTime.sleep(100);
    
    // Assert
    verify(verificationOverTime, times(1)).sleep(100);
}

@Test
public void getPollingPeriod_returnPollingPeriod() {
    // Arrange
    VerificationMode delegate = mock(VerificationMode.class);
    when(delegate.toString()).thenReturn("delegate");
    
    VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, false);
    
    // Act
    long pollingPeriod = verificationOverTime.getPollingPeriod();
    
    // Assert
    assertEquals(100, pollingPeriod);
}

@Test
public void getDuration_returnDuration() {
    // Arrange
    VerificationMode delegate = mock(VerificationMode.class);
    when(delegate.toString()).thenReturn("delegate");
    
    VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, false);
    
    // Act
    long duration = verificationOverTime.getDuration();
    
    // Assert
    assertEquals(1000, duration);
}

@Test
public void getDelegate_returnDelegate() {
    // Arrange
    VerificationMode delegate = mock(VerificationMode.class);
    when(delegate.toString()).thenReturn("delegate");
    
    VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, false);
    
    // Act
    VerificationMode actualDelegate = verificationOverTime.getDelegate();
    
    // Assert
    assertEquals(delegate, actualDelegate);
}


}