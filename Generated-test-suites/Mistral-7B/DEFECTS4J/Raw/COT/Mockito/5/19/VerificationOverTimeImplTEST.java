package org.mockito.internal.verification.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.VerificationWithTimeout;
import org.mockito.internal.util.Timer;
import org.mockito.verification.VerificationModeFactoryImpl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class VerificationOverTimeImplTest {

 private VerificationMode verificationMode;
 private VerificationOverTimeImpl verificationOverTime;
 private Timer timer;

 @Before
 public void setup() {
 verificationMode = mock(VerificationWithTimeout.class);
 timer = new Timer(500); // 500ms for testing purposes

 verificationOverTime = new VerificationOverTimeImpl(100, 1000, verificationMode, false, timer);
 }

 @Test
 public void testBasicFunctionality() {
 // Test basic functionality of VerificationOverTimeImpl by verifying a mock with it
 VerificationData verificationData = new VerificationData(verificationMode, null, null);
 verificationOverTime.verify(verificationData);

 // Verify that the delegate verification mode was called at least once
 verify(verificationMode, times(1)).verify(verificationData);
 }

 @Test(expected = MockitoAssertionError.class)
 public void testTimeoutWhenDelegateThrowsException() {
 // Set up the delegate verification mode to throw an exception
 doThrow(new MockitoAssertionError("Expected exception")).when(verificationMode).verify(verificationData);

 // Test the timeout scenario when the delegate verification mode throws an exception
 verificationOverTime.verify(verificationData);
 }

 @Test(expected = MockitoAssertionError.class)
 public void testTimeoutWhenDelegateDoesNotSatisfy() {
 // Test the timeout scenario when the delegate verification mode does not satisfy
 // (even though it can recover from failure)
 doNothing().when(verificationMode).verify(verificationData);

 // Wait for the timeout to elapse
 timer.start();
 threadSleep(1050); // 1050ms to make sure the timeout has elapsed

 // Verify that an exception is thrown
 verificationOverTime.verify(verificationData);
 }

 @Test(expected = MockitoAssertionError.class)
 public void testTimeoutWhenDelegateCannotRecoverFromFailure() {
 // Test the timeout scenario when the delegate verification mode cannot recover from failure
 VerificationMode nonRecoverableVerificationMode = spy(new AtLeastOnce());

 verificationOverTime = new VerificationOverTimeImpl(100, 1000, nonRecoverableVerificationMode, false, timer);

 // Wait for the timeout to elapse
 timer.start();
 threadSleep(1050); // 1050ms to make sure the timeout has elapsed

 // Verify that an exception is thrown
 verificationOverTime.verify(verificationData);
 }

 @Test
 public void testReturnOnSuccess() {
 // Test the scenario when returnOnSuccess is set to true
 verificationOverTime = new VerificationOverTimeImpl(100, 100, verificationMode, true, timer);

 // Verify that the delegate verification mode is called once and the test passes
 VerificationData verificationData = new VerificationData(verificationMode, null, null);
 verificationOverTime.verify(verificationData);

 // Verify that the delegate verification mode was called at least once
 verify(verificationMode, times(1)).verify(verificationData);
 }

 private void threadSleep(long sleep) {
 try {
 Thread.sleep(sleep);
 } catch (InterruptedException ie) {
 // oups. not much luck.
 }
 }

}