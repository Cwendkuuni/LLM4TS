package org.mockito.internal.verification.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.verification.VerificationData;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.VerificationWithTimeout;
import org.mockito.internal.verification.VerificationAfterDelay;
import org.mockito.internal.util.Timer;
import org.mockito.exceptions.base.MockitoAssertionError;

public class VerificationOverTimeImplTest {

 private VerificationOverTimeImpl verificationOverTime;
 private VerificationWithTimeout verificationWithTimeout;
 private VerificationAfterDelay verificationAfterDelay;
 private Timer timer;

 @Before
 public void setUp() {
 verificationWithTimeout = new VerificationWithTimeout(100, VerificationWithTimeout.mode(ArgumentMatchers.any(VerificationData.class)));
 verificationAfterDelay = new VerificationAfterDelay(100, VerificationAfterDelay.mode(ArgumentMatchers.any(VerificationData.class)));
 verificationOverTime = new VerificationOverTimeImpl(50, 500, verificationWithTimeout, true);
 verificationOverTime = new VerificationOverTimeImpl(50, 500, verificationAfterDelay, false);
 timer = new Timer(500);
 }

 @Test
 public void testVerificationWithTimeoutSuccess() {
 // Arrange
 VerificationData data = new VerificationData(ArgumentMatchers.any(), ArgumentMatchers.any());

 // Act
 verificationOverTime.verify(data);

 // Assert
 // No exception should be thrown
 }

 @Test(expected = MockitoAssertionError.class)
 public void testVerificationWithTimeoutFailure() throws InterruptedException {
 // Arrange
 VerificationData data = new VerificationData(ArgumentMatchers.any(), ArgumentMatchers.any());

 // Act
 timer.start();
 while (timer.isCounting()) {
 try {
 verificationWithTimeout.verify(data);
 break;
 } catch (MockitoAssertionError e) {
 // wait for the timeout to be reached
 try {
 Thread.sleep(51);
 } catch (InterruptedException ie) {
 // handle interruptions
 }
 }
 }

 // Assert
 // An exception should be thrown
 }

 @Test(expected = MockitoAssertionError.class)
 public void testVerificationAfterDelaySuccess() throws InterruptedException {
 // Arrange
 VerificationData data = new VerificationData(ArgumentMatchers.any(), ArgumentMatchers.any());

 // Act
 timer.start();
 while (!verificationAfterDelay.hasVerified(data)) {
 try {
 Thread.sleep(51);
 } catch (InterruptedException ie) {
 // handle interruptions
 }
 }

 // Assert
 // No exception should be thrown
 }

 @Test(expected = MockitoAssertionError.class)
 public void testVerificationAfterDelayFailure() throws InterruptedException {
 // Arrange
 VerificationData data = new VerificationData(ArgumentMatchers.any(), ArgumentMatchers.any());

 // Act
 timer.start();
 while (!verificationAfterDelay.hasVerified(data)) {
 try {
 Thread.sleep(501);
 } catch (InterruptedException ie) {
 // handle interruptions
 }
 }

 // Assert
 // An exception should be thrown
 }

}