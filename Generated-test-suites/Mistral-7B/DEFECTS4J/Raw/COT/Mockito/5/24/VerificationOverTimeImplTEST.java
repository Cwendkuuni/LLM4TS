package org.mockito.internal.verification.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.api.VerificationData.*;
import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.verification.VerificationMode;

public class VerificationOverTimeImplTest {

 private VerificationMode verificationMode;
 private VerificationOverTimeImpl verificationOverTime;

 @Before
 public void setUp() {
 verificationMode = mock(VerificationMode.class);
 verificationOverTime = new VerificationOverTimeImpl(100, 500, verificationMode, true);
 }

 @Test(expected = MockitoAssertionError.class)
 public void testDelegateIsNotSatisfiedWithinGivenDuration() {
 // given
 doNothing().when(verificationMode).verify(any(VerificationData.class));

 // when
 verificationOverTime.verify(createVerificationData());

 // then
 fail("Delegate verification mode should not have been satisfied within given duration.");
 }

 @Test
 public void testDelegateIsSatisfiedWithinGivenDurationAndReturnOnSuccessIsTrue() {
 // given
 doAnswer(invocation -> {
 throw new MockitoAssertionError("Intentional failure.");
 }).when(verificationMode).verify(any(VerificationData.class));

 // when
 verificationOverTime.verify(createVerificationData());

 // then
 // Delegate verification mode was satisfied and the verification method returned immediately
 // since returnOnSuccess is set to true
 verifyNoMoreInteractions(verificationMode);
 }

 @Test
 public void testDelegateIsSatisfiedWithinGivenDurationAndReturnOnSuccessIsFalse() {
 // given
 doAnswer(invocation -> {
 throw new MockitoAssertionError("Intentional failure.");
 }).when(verificationMode).verify(any(VerificationData.class));

 // when
 verificationOverTime.verify(createVerificationData());

 // then
 // Delegate verification mode was satisfied after the full given duration has passed
 verify(verificationMode).verify(any(VerificationData.class));
 }

 @Test(expected = MockitoAssertionError.class)
 public void testDelegateIsNotSatisfiedWithinGivenDurationAndReturnOnSuccessIsFalse() {
 // given
 doAnswer(invocation -> {
 throw new MockitoAssertionError("Intentional failure.");
 }).when(verificationMode).verify(any(VerificationData.class));

 // when
 verificationOverTime.verify(createVerificationData());

 // then
 // Delegate verification mode was not satisfied before the full given duration has passed,
 // but the full duration has passed, so a MockitoAssertionError is thrown
 verify(verificationMode, times(2)).verify(any(VerificationData.class));
 }

 @Test(expected = InterruptedException.class)
 public void testSleepMethodInterruptsExecution() {
 // given
 doNothing().when(verificationMode).verify(any(VerificationData.class));

 // when
 verificationOverTime.verify(createVerificationData());

 // then
 // The sleep method can be interrupted, leading to an InterruptedException
 assertTrue(Thread.interrupted());
 }

 @Test
 public void testGetters() {
 VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(100, 500, verificationMode, true, new Timer(500));

 assertEquals(100, verificationOverTime.getPollingPeriod());
 assertEquals(500, verificationOverTime.getDuration());
 assertEquals(verificationMode, verificationOverTime.getDelegate());
 }

}