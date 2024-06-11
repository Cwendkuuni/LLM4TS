package org.mockito.internal.verification.test;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.test.TestUtils;

public class VerificationOverTimeImplTest {

 private VerificationMode delegate;
 private VerificationOverTimeImpl verificationOverTime;

 @Before
 public void setUp() {
 delegate = mock(VerificationMode.class);
 verificationOverTime = new VerificationOverTimeImpl(100, 1000, delegate, true);
 }

 @Test
 public void testVerify_DelegateIsSatisfiedBeforeTimeout() {
 // Given: delegate is satisfied before timeout
 VerificationData data = TestUtils.getVerificationData();
 doThrow(new RuntimeException("Test exception")).when(delegate).verify(data);

 // When: verificationOverTime.verify(data) is called
 verificationOverTime.verify(data);

 // Then: verificationOverTime is satisfied and no exception is thrown
 verify(delegate, times(1)).verify(data);
 }

 @Test
 public void testVerify_DelegateIsSatisfiedAfterTimeout() {
 // Given: delegate is not satisfied before timeout
 doNothing().when(delegate).verify(any(VerificationData.class));

 // When: verificationOverTime.verify(data) is called
 verificationOverTime.verify(TestUtils.getVerificationData());

 // Then: exception is thrown from delegate
 AssertionError exception = assertThrows(MockitoAssertionError.class, () -> {
 verificationOverTime.verify(TestUtils.getVerificationData());
 });
 }

 @Test
 public void testVerify_DelegateThrowsDifferentException() {
 // Given: delegate throws a different exception
 doThrow(new AssertionError("Test error")).when(delegate).verify(TestUtils.getVerificationData());

 // When: verificationOverTime.verify(data) is called
 verificationOverTime.verify(TestUtils.getVerificationData());

 // Then: the same exception is thrown from verificationOverTime
 AssertionError actualException = assertThrows(AssertionError.class, () -> {
 verificationOverTime.verify(TestUtils.getVerificationData());
 });
 assertSame(exception.getCause(), actualException.getCause());
 }

 @Test
 public void testVerify_DelegateIsNotVerifiable() {
 // Given: delegate is not verifiable
 doThrow(new MockitoAssertionError("Test error")).when(delegate).verify(any(VerificationData.class));

 // When: verificationOverTime.verify(data) is called
 verificationOverTime.verify(TestUtils.getVerificationData());

 // Then: the same exception is thrown from verificationOverTime
 AssertionError actualException = assertThrows(MockitoAssertionError.class, () -> {
 verificationOverTime.verify(TestUtils.getVerificationData());
 });
 assertSame(exception.getCause(), actualException.getCause());
 }

 @Test
 public void testVerify_DelegateThrowsArgumentsAreDifferent() {
 // Given: delegate throws ArgumentsAreDifferent exception
 doThrow(new org.mockito.exceptions.verification.junit.ArgumentsAreDifferent("Test error"))
 .when(delegate).verify(TestUtils.getVerificationData());

 // When: verificationOverTime.verify(data) is called
 verificationOverTime.verify(TestUtils.getVerificationData());

 // Then: the same exception is thrown from verificationOverTime
 AssertionError actualException = assertThrows(org.mockito.exceptions.verification.junit.ArgumentsAreDifferent.class, () -> {
 verificationOverTime.verify(TestUtils.getVerificationData());
 });
 assertSame(exception.getCause(), actualException.getCause());
 }

 @Test
 public void testVerify_DelegateThrowsInterruptedException() {
 // Given: delegate throws InterruptedException
 doThrow(new InterruptedException("Test interruption"))
 .when(delegate).verify(TestUtils.getVerificationData());

 // When: verificationOverTime.verify(data) is called
 verificationOverTime.verify(TestUtils.getVerificationData());

 // Then: the same InterruptedException is thrown from verificationOverTime
 InterruptedException actualException = assertThrows(InterruptedException.class, () -> {
 verificationOverTime.verify(TestUtils.getVerificationData());
 });
 assertSame(exception.getCause(), actualException.getCause());
 }

 @Test
 public void testCanRecoverFromFailure_DelegateCanRecover() {
 // Given: delegate can recover from failure
 VerificationMode recoverableDelegate = mock(VerificationMode.class);
 doNothing().when(recoverableDelegate).verify(any(VerificationData.class));

 // When: canRecoverFromFailure(recoverableDelegate) is called
 boolean result = verificationOverTime.canRecoverFromFailure(recoverableDelegate);

 // Then: result is true
 assertTrue(result);
 }

 @Test
 public void testCanRecoverFromFailure_DelegateCannotRecover() {
 // Given: delegate cannot recover from failure
 VerificationMode nonRecoverableDelegate = mock(AtMost.class);

 // When: canRecoverFromFailure(nonRecoverableDelegate) is called
 boolean result = verificationOverTime.canRecoverFromFailure(nonRecoverableDelegate);

 // Then: result is false
 assertFalse(result);
 }

 @Test
 public void testHandleVerifyException_DelegateCanRecover() {
 // Given: delegate can recover from failure
 VerificationMode recoverableDelegate = mock(VerificationMode.class);
 doNothing().when(recoverableDelegate).verify(any(VerificationData.class));

 // When: handleVerifyException(exception) is called
 AssertionError exception = new AssertionError("Test exception");
 boolean result = verificationOverTime.handleVerifyException(exception);

 // Then: result is false and no sleep is called
 assertFalse(result);
 verifyZeroInteractions(verificationOverTime);
 }

 @Test
 public void testHandleVerifyException_DelegateCannotRecover() {
 // Given: delegate cannot recover from failure
 VerificationMode nonRecoverableDelegate = mock(AtMost.class);
 doNothing().when(nonRecoverableDelegate).verify(any(VerificationData.class));

 // When: handleVerifyException(exception) is called
 AssertionError exception = new AssertionError("Test exception");
 boolean result = verificationOverTime.handleVerifyException(exception);

 // Then: result is false, exception is thrown and sleep is called
 assertFalse(result);
 verify(verificationOverTime).handleVerifyException(exception);
 verify(verificationOverTime).sleep(100L);
 }

 @Test
 public void testSleep() {
 // Given: sleep is called
 verificationOverTime.sleep(500L);

 // Then: Thread sleeps for the given amount of time
 long actualSleepTime = System.currentTimeMillis() - System.currentTimeMillis() % 500L;
 assertEquals(500L, actualSleepTime);
 }
}