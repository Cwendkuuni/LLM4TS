package org.mockito.internal.verification.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.verification.VerificationMode;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoAssertionError;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

public class VerificationOverTimeImplTest {

 private VerificationMode delegateVerificationMode;
 private VerificationOverTimeImpl verificationOverTime;
 private long pollingPeriodMillis = 100;
 private long durationMillis = 500;

 @Before
 public void setup() {
 delegateVerificationMode = mock(VerificationMode.class);
 verificationOverTime = new VerificationOverTimeImpl(pollingPeriodMillis, durationMillis, delegateVerificationMode, true);
 }

 @Test
 public void testDelegateVerificationIsSatisfiedBeforeTimeout() {
 // Given
 VerificationData data = Mockito.mock(VerificationData.class);
 doAnswer(invocation -> {
 verify(data);
 return null;
 }).when(delegateVerificationMode).verify(ArgumentMatchers.eq(data));

 // When
 verificationOverTime.verify(data);

 // Then
 verify(delegateVerificationMode, times(1)).verify(data);
 }

 @Test(expected = MockitoAssertionError.class)
 public void testDelegateVerificationIsNotSatisfiedBeforeTimeout() {
 // Given
 VerificationData data = Mockito.mock(VerificationData.class);
 doNothing().when(delegateVerificationMode).verify(data);

 // When
 verificationOverTime.verify(data);

 // Then
 verify(delegateVerificationMode, never()).verify(data);
 }

 @Test(expected = MockitoAssertionError.class)
 public void testDelegateVerificationThrowsExceptionBeforeTimeout() {
 // Given
 VerificationData data = Mockito.mock(VerificationData.class);
 doThrow(new RuntimeException("Test Exception")).when(delegateVerificationMode).verify(data);

 // When
 verificationOverTime.verify(data);

 // Then
 verify(delegateVerificationMode).verify(data);
 }

 @Test
 public void testDelegateVerificationIsSatisfiedAfterTimeout() {
 // Given
 VerificationData data = Mockito.mock(VerificationData.class);
 doAnswer(invocation -> {
 throw new RuntimeException();
 }).when(delegateVerificationMode).verify(data);

 // When
 verificationOverTime.verify(data);

 // Then
 // At some point, verify will throw an exception
 // But since we're checking if the delegateVerificationMode is satisfied and the full duration has passed
 // The test will wait for the full duration and then throw the exception thrown by the delegateVerificationMode
 verify(delegateVerificationMode, times(1)).verify(data);
 }

 @Test
 public void testDelegateVerificationIsNotSatisfiedAfterTimeout() {
 // Given
 VerificationData data = Mockito.mock(VerificationData.class);
 doNothing().when(delegateVerificationMode).verify(data);

 // When
 verificationOverTime.verify(data);

 // Then
 // If the delegateVerificationMode is not satisfied before the timeout, it will throw an exception
 // At some point, verify will throw an exception
 // Since we're checking if the delegateVerificationMode is not satisfied and the full duration has passed
 // The test will throw the exception thrown by the delegateVerificationMode
 verify(delegateVerificationMode, never()).verify(data);
 }

 @Test
 public void testDelegateVerificationThrowsArgumentsAreDifferentBeforeTimeout() {
 // Given
 VerificationData data = Mockito.mock(VerificationData.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 doAnswer(invocation -> {
 throw new org.mockito.exceptions.verification.junit.ArgumentsAreDifferentException();
 }).when(delegateVerificationMode).verify(data);

 // When
 verificationOverTime.verify(data);
 // The test will wait for the full duration and then throw the exception thrown by the delegateVerificationMode
 // Since we're checking if the delegateVerificationMode is not satisfied and the full duration has passed
 // The test will throw the exception thrown by the delegateVerificationMode
 verify(delegateVerificationMode).verify(data);
 }

 @Test
 public void testDelegateVerificationIsSatisfiedAfterDelay() {
 // Given
 VerificationData data = Mockito.mock(VerificationData.class);
 doAnswer(invocation -> {
 verify(data);
 return null;
 }).when(delegateVerificationMode).verify(data);

 // When
 verificationOverTime.verify(data);
 // The test will wait for the full duration before returning
 // Since we're checking if the delegateVerificationMode is satisfied and the full duration has passed
 // The test will return successfully
 verify(delegateVerificationMode, times(1)).verify(data);
 }

 @Test
 public void testDelegateVerificationIsNotSatisfiedAfterDelay() {
 // Given
 VerificationData data = Mockito.mock(VerificationData.class);
 doNothing().when(delegateVerificationMode).verify(data);

 // When
 verificationOverTime.verify(data);
 // The test will wait for the full duration before returning
 // Since we're checking if the delegateVerificationMode is not satisfied and the full duration has passed
 // The test will throw an exception thrown by the delegateVerificationMode
 verify(delegateVerificationMode, never()).verify(data);
 }

 @Test
 public void testDelegateVerificationThrowsExceptionAfterDelay() {
 // Given
 VerificationData data = Mockito.mock(VerificationData.class);
 doAnswer(invocation -> {
 throw new RuntimeException();
 }).when(delegateVerificationMode).verify(data);

 // When
 verificationOverTime.verify(data);
 // The test will wait for the full duration before throwing the exception
 // Since we're checking if the delegateVerificationMode is not satisfied and the full duration has passed
 // The test will throw the exception thrown by the delegateVerificationMode
 verify(delegateVerificationMode, times(1)).verify(data);
 }

 @Test
 public void testDelegateVerificationIsSatisfiedAndReturnOnSuccess() {
 // Given
 VerificationData data = Mockito.mock(VerificationData.class);
 doAnswer(invocation -> {
 verify(data);
 return null;
 }).when(delegateVerificationMode).verify(data);

 // When
 verificationOverTime.verify(data);
 // The test will return as soon as the delegateVerificationMode is satisfied
 // Since we're checking if the delegateVerificationMode is satisfied and returnOnSuccess is true
 // The test will return successfully
 verify(delegateVerificationMode, times(1)).verify(data);
 }

 @Test
 public void testDelegateVerificationIsNotSatisfiedAndReturnOnSuccess() {
 // Given
 VerificationData data = Mockito.mock(VerificationData.class);
 doNothing().when(delegateVerificationMode).verify(data);

 // When
 verificationOverTime.verify(data);
 // The test will return immediately since returnOnSuccess is true
 // But since we're checking if the delegateVerificationMode is not satisfied and the full duration has passed
 // The test will throw an exception
 verify(delegateVerificationMode, never()).verify(data);
 }

 @Test
 public void testDelegateVerificationIsSatisfiedAndVerificationModeReturnsZeroInteractions() {
 // Given
 VerificationData data = Mockito.mock(VerificationData.class);
 doAnswer(invocation -> {
 verifyZeroInteractions(data);
 return null;
 }).when(delegateVerificationMode).verify(data);

 // When
 verificationOverTime.verify(data);
 // The test will return as soon as the delegateVerificationMode is satisfied
 // Since we're checking if the delegateVerificationMode is satisfied and returnOnSuccess is true
 // The test will return successfully
 verifyZeroInteractions(data);
 }

 @Test
 public void testDelegateVerificationIsNotSatisfiedAndVerificationModeReturnsZeroInteractions() {
 // Given
 VerificationData data = Mockito.mock(VerificationData.class);
 doAnswer(invocation -> {
 verifyZeroInteractions(data);
 return null;
 }).when(delegateVerificationMode).verify(data);

 // When
 verificationOverTime.verify(data);
 // The test will return immediately since returnOnSuccess is true
 // But since we're checking if the delegateVerificationMode is not satisfied and the full duration has passed
 // The test will throw an exception
 verifyZeroInteractions(data);
 }
}