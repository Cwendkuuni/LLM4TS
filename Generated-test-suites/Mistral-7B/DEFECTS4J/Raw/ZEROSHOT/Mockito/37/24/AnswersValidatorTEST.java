package org.mockito.internal.stubbing.answers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class AnswersValidatorTest {

 private AnswersValidator validator;
 private Reporter reporter;

 @Before
 public void setUp() {
 validator = new AnswersValidator();
 reporter = mock(Reporter.class);
 }

 @Test
 public void testValidateDoNothing_voidMethod() {
 DoesNothing answer = mock(DoesNothing.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 validator.validate(answer, invocation);
 verifyZeroInteractions(reporter);
 }

 @Test(expected = AssertionError.class)
 public void testValidateDoNothing_nonVoidMethod() {
 DoesNothing answer = mock(DoesNothing.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(invocation.isVoid()).thenReturn(false);
 validator.validate(answer, invocation);
 verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
 }

 @Test
 public void testValidateReturnValue_voidMethod() {
 Returns answer = mock(Returns.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(invocation.isVoid()).thenReturn(true);
 validator.validate(answer, invocation);
 verifyZeroInteractions(reporter);
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnValue_nonVoidMethod_nullReturnValue() {
 Returns answer = mock(Returns.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(invocation.isVoid()).thenReturn(false);
 when(answer.getReturnType()).thenReturn(int[].class);
 validator.validate(answer, invocation);
 verify(reporter).wrongTypeOfReturnValue("void", "null", "testMethod");
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnValue_nonVoidMethod_invalidReturnType() {
 Returns answer = mock(Returns.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(invocation.isVoid()).thenReturn(false);
 when(answer.getReturnType()).thenReturn(String.class);
 when(invocation.getMockType()).thenReturn(String.class);
 validator.validate(answer, invocation);
 verify(reporter).wrongTypeOfReturnValue("java.lang.String", "java.util.Arrays[Ljava.lang.Integer;", "testMethod");
 }

 @Test
 public void testValidateReturnValue_nonVoidMethod_validReturnType() {
 Returns answer = mock(Returns.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(invocation.isVoid()).thenReturn(false);
 when(answer.getReturnType()).thenReturn(int.class);
 when(invocation.getMockType()).thenReturn(String.class);
 validator.validate(answer, invocation);
 verifyZeroInteractions(reporter);
 }

 @Test(expected = AssertionError.class)
 public void testValidateException_nullThrowable() {
 ThrowsException answer = mock(ThrowsException.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 validator.validate(answer, invocation);
 verify(reporter).cannotStubWithNullThrowable();
 }

 @Test
 public void testValidateException_validThrowable() {
 ThrowsException answer = mock(ThrowsException.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(answer.getThrowable()).thenReturn(new IllegalArgumentException("Test error"));
 validator.validate(answer, invocation);
 verifyZeroInteractions(reporter);
 }

 @Test(expected = AssertionError.class)
 public void testValidateException_invalidThrowable_runtimeExceptionOrError() {
 ThrowsException answer = mock(ThrowsException.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(answer.getThrowable()).thenReturn(new RuntimeException("Test error"));
 validator.validate(answer, invocation);
 // This test should not throw an AssertionError
 }

 @Test(expected = AssertionError.class)
 public void testValidateException_invalidThrowable_notRuntimeExceptionOrError() {
 ThrowsException answer = mock(ThrowsException.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(answer.getThrowable()).thenReturn(new Exception("Test error"));
 validator.validate(answer, invocation);
 verify(reporter).checkedExceptionInvalid(new Exception("Test error"));
 }
}