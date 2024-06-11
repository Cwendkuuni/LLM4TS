package org.mockito.internal.verification.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.VerificationWithTimeoutImpl;
import org.mockito.internal.util.Timer;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Stubber;

public class VerificationOverTimeImplTest {

 private VerificationMode verificationMode;
 private VerificationOverTimeImpl verificationOverTime;
 private Stubber stubber;
 private Timer timer;

 @Before
 public void setUp() {
 stubber = org.mockito.Mockito.mock(Stubber.class);
 timer = new Timer(100);
 verificationMode = new VerificationWithTimeoutImpl(1, 500, ArgumentMatchers.any(VerificationData.class), true);
 verificationOverTime = new VerificationOverTimeImpl(100, 1000, verificationMode, true, timer);
 }

 @Test
 public void testVerifyWithSuccess() {
 VerificationData verificationData = new VerificationData(new InvocationOnMock(stubber.mock(Object.class)), ArgumentMatchers.anyInt());
 verificationOverTime.verify(verificationData);
 // the test should not time out or throw an exception
 }

 @Test(expected = AssertionError.class)
 public void testVerifyWithTimeout() {
 VerificationData verificationData = new VerificationData(new InvocationOnMock(stubber.mock(Object.class)), ArgumentMatchers.anyInt());
 timer.setEnabled(false); // disable the timer to simulate a timeout
 verificationOverTime.verify(verificationData);
 }

 @Test(expected = MockitoAssertionError.class)
 public void testVerifyWithDelegateFailure() {
 VerificationData verificationData = new VerificationData(new InvocationOnMock(stubber.mock(Object.class)), ArgumentMatchers.anyInt());
 verificationMode = new VerificationWithTimeoutImpl(1, 50, verificationData, false); // a failing verification mode
 verificationOverTime = new VerificationOverTimeImpl(100, 1000, verificationMode, true, timer);
 verificationOverTime.verify(verificationData);
 }

 @Test(expected = MockitoAssertionError.class)
 public void testVerifyWithDelegateFailureAndNoRecovery() {
 VerificationData verificationData = new VerificationData(new InvocationOnMock(stubber.mock(Object.class)), ArgumentMatchers.anyInt());
 verificationMode = new AtLeastOnceImpl(verificationData); // a failing verification mode that doesn't recover
 verificationOverTime = new VerificationOverTimeImpl(100, 1000, verificationMode, true, timer);
 verificationOverTime.verify(verificationData);
 }

 @Test
 public void testGetPollingPeriod() {
 long pollingPeriod = verificationOverTime.getPollingPeriod();
 Assert.assertEquals(100, pollingPeriod);
 }

 @Test
 public void testGetDuration() {
 long duration = verificationOverTime.getDuration();
 Assert.assertEquals(1000, duration);
 }

 @Test
 public void testGetDelegate() {
 VerificationMode delegate = verificationOverTime.getDelegate();
 Assert.assertSame(verificationMode, delegate);
 }
}