import org.junit.*;
import static org.mockito.Mockito.*;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

public class VerificationOverTimeImplTest {
  
  @Test
  public void verify_ReturnsImmediatelyWhenDelegateSatisfiedAndReturnOnSuccessTrue() {
    // Arrange
    long pollingPeriodMillis = 100;
    long durationMillis = 1000;
    VerificationMode delegate = mock(VerificationMode.class);
    when(delegate.toString()).thenReturn("Delegate");

    VerificationOverTimeImpl verification = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, true);
    VerificationData data = mock(VerificationData.class);

    // Act
    verification.verify(data);

    // Assert
    verify(delegate).verify(data);
  }

  @Test
  public void verify_ReturnsAfterFullDurationWhenDelegateSatisfiedAndReturnOnSuccessFalse() {
    // Arrange
    long pollingPeriodMillis = 100;
    long durationMillis = 1000;
    VerificationMode delegate = mock(VerificationMode.class);
    when(delegate.toString()).thenReturn("Delegate");

    VerificationOverTimeImpl verification = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);
    VerificationData data = mock(VerificationData.class);

    // Act
    verification.verify(data);

    // Assert
    verify(delegate).verify(data);
    Assert.assertEquals(durationMillis, verification.getDuration());
  }

  @Test
  public void verify_ThrowsExceptionWhenDelegateNotSatisfiedWithinTimeout() {
    // Arrange
    long pollingPeriodMillis = 100;
    long durationMillis = 1000;
    VerificationMode delegate = mock(VerificationMode.class);
    when(delegate.toString()).thenReturn("Delegate");
    doThrow(MockitoAssertionError.class).when(delegate).verify(any(VerificationData.class));

    VerificationOverTimeImpl verification = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegate, false);
    VerificationData data = mock(VerificationData.class);

    // Act & Assert
    try {
      verification.verify(data);
      Assert.fail("Expected MockitoAssertionError to be thrown");
    } catch (MockitoAssertionError e) {
      verify(delegate).verify(data);
      Assert.assertEquals(durationMillis, verification.getDuration());
    }
  }

  @Test
  public void handleVerifyException_ReturnsErrorAndSleepsWhenCanRecoverFromFailureTrue() {
    // Arrange
    VerificationMode verificationMode = mock(VerificationMode.class);
    when(verificationMode.toString()).thenReturn("VerificationMode");

    VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, verificationMode, false);
    AssertionError error = new MockitoAssertionError("Assertion error message");

    // Act
    AssertionError result = verification.handleVerifyException(error);

    // Assert
    Assert.assertEquals(error, result);
    verify(verificationMode, times(10)).toString();
    verify(verificationMode, times(10)).getClass();
    verify(verificationMode, times(10)).hashCode();
    verify(verificationMode, times(10)).equals(any());
    verify(verificationMode, times(10)).canRecoverFromFailure(any(VerificationMode.class));
    // Wait for 10 times the polling period
    verify(verificationMode, times(10)).sleep(100);
  }

  @Test(expected = AssertionError.class)
  public void handleVerifyException_ThrowsErrorWhenCanRecoverFromFailureFalse() {
    // Arrange
    VerificationMode verificationMode = mock(VerificationMode.class);
    when(verificationMode.toString()).thenReturn("VerificationMode");
    when(verificationMode.canRecoverFromFailure(any())).thenReturn(false);

    VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, verificationMode, false);
    AssertionError error = new MockitoAssertionError("Assertion error message");

    // Act & Assert
    verification.handleVerifyException(error);
  }

  @Test
  public void canRecoverFromFailure_ReturnsTrueForVerificationModeNotInstanceOfAtMostOrNoMoreInteractions() {
    // Arrange
    VerificationMode verificationMode = mock(VerificationMode.class);
    when(verificationMode.toString()).thenReturn("VerificationMode");

    VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, verificationMode, false);

    // Act
    boolean result = verification.canRecoverFromFailure(verificationMode);

    // Assert
    Assert.assertTrue(result);
  }

  @Test
  public void canRecoverFromFailure_ReturnsFalseForVerificationModeInstanceOfAtMost() {
    // Arrange
    VerificationMode verificationMode = mock(AtMost.class);
    when(verificationMode.toString()).thenReturn("AtMost");

    VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, verificationMode, false);

    // Act
    boolean result = verification.canRecoverFromFailure(verificationMode);

    // Assert
    Assert.assertFalse(result);
  }

  @Test
  public void canRecoverFromFailure_ReturnsFalseForVerificationModeInstanceOfNoMoreInteractions() {
    // Arrange
    VerificationMode verificationMode = mock(NoMoreInteractions.class);
    when(verificationMode.toString()).thenReturn("NoMoreInteractions");

    VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, verificationMode, false);

    // Act
    boolean result = verification.canRecoverFromFailure(verificationMode);

    // Assert
    Assert.assertFalse(result);
  }

  @Test
  public void sleep_CallsThreadSleepWithGivenDuration() {
    // Arrange
    VerificationOverTimeImpl verification = new VerificationOverTimeImpl(100, 1000, null, false);

    // Act
    verification.sleep(500);

    // Assert
    verify(Thread.class, times(1)).sleep(500);
  }

}