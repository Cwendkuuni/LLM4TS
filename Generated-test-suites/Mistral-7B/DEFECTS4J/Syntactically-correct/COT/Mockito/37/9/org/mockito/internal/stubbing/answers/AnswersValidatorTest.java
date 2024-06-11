package org.mockito.internal.stubbing.answers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;
import static org.mockito.internal.stubbing.answers.AnswersValidator.reporter;

public class AnswersValidatorTest {

 private AnswersValidator validator;
 private Reporter reporter;

 @Before
 public void setUp() {
 validator = new AnswersValidator();
 reporter = mock(Reporter.class);
 validator.reporter = reporter;
 }

 @Test
 public void testValidate_doNothingAnswer_voidInvocation() {
     Invocation invocation = mock(InvocationOnMock.class);
     when(invocation.isVoid()).thenReturn(true);
     Answer<?> answer = mock(DoesNothing.class);

     validator.validate(answer, invocation);
     verifyNoMoreInteractions(reporter);
 }

 @Test(expected = AssertionError.class)
 public void testValidate_doNothingAnswer_nonVoidInvocation() {
     Invocation invocation = mock(InvocationOnMock.class);
     when(invocation.isVoid()).thenReturn(false);
     Answer<?> answer = mock(DoesNothing.class);

     validator.validate(answer, invocation);
     verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
 }

 @Test
 public void testValidate_returnValueAnswer_voidInvocation() {
     Invocation invocation = mock(InvocationOnMock.class);
     when(invocation.isVoid()).thenReturn(true);
     Answer<?> answer = mock(Returns.class);
     when(answer.getReturnValue()).thenReturn(1);

     validator.validate(answer, invocation);
     verifyNoMoreInteractions(reporter);
 }

 @Test(expected = AssertionError.class)
 public void testValidate_returnValueAnswer_nonVoidInvocation() {
     Invocation invocation = mock(InvocationOnMock.class);
     when(invocation.isVoid()).thenReturn(false);
     Answer<Integer> answer = mock(Returns.class);
     when(answer.getReturnValue()).thenReturn(1);

     validator.validate(answer, invocation);
     verify(reporter).cannotStubVoidMethodWithAReturnValue();
 }

 @Test
 public void testValidate_returnValueAnswer_nullValue_voidInvocation() {
     Invocation invocation = mock(InvocationOnMock.class);
     when(invocation.isVoid()).thenReturn(true);
     Answer<String> answer = mock(Returns.class);
     when(answer.getReturnValue()).thenReturn(null);

     validator.validate(answer, invocation);
     verifyNoMoreInteractions(reporter);
 }

 @Test(expected = AssertionError.class)
 public void testValidate_returnValueAnswer_nullValue_nonVoidInvocation() {
     Invocation invocation = mock(InvocationOnMock.class);
     when(invocation.isVoid()).thenReturn(false);
     Answer<String> answer = mock(Returns.class);
     when(answer.getReturnValue()).thenReturn(null);

     validator.validate(answer, invocation);
     verify(reporter).wrongTypeOfReturnValue("void", "null", eq("validate(Answer, Invocation)"));
 }

 @Test
 public void testValidate_returnValueAnswer_validValue_nonVoidInvocation() {
     Invocation invocation = mock(InvocationOnMock.class);
     when(invocation.isVoid()).thenReturn(false);
     Answer<Integer> answer = mock(Returns.class);
     when(answer.getReturnType()).thenReturn(int.class);
     when(answer.getReturnValue()).thenReturn(1);

     validator.validate(answer, invocation);
     verifyNoMoreInteractions(reporter);
 }

 @Test(expected = AssertionError.class)
 public void testValidate_returnValueAnswer_invalidType_nonVoidInvocation() {
     Invocation invocation = mock(InvocationOnMock.class);
     when(invocation.isVoid()).thenReturn(false);
     Answer<Integer> answer = mock(Returns.class);
     when(answer.getReturnType()).thenReturn(double.class);
     when(answer.getReturnValue()).thenReturn(1);

     validator.validate(answer, invocation);
     verify(reporter).wrongTypeOfReturnValue("int", "double", eq("validate(Answer, Invocation)"));
 }

 @Test
 public void testValidate_exceptionAnswer_nullThrowable() {
     Invocation invocation = mock(InvocationOnMock.class);
     Answer<?> answer = mock(ThrowsException.class);

     validator.validate(answer, invocation);
     verifyNoMoreInteractions(reporter);
 }

 @Test(expected = AssertionError.class)
 public void testValidate_exceptionAnswer_runtimeExceptionOrError() {
     Invocation invocation = mock(InvocationOnMock.class);
     Answer<?> answer = mock(ThrowsException.class);
     when(answer.getThrowable()).thenThrow(new RuntimeException());

     validator.validate(answer, invocation);
     verifyNoMoreInteractions(reporter);
 }

 @Test(expected = AssertionError.class)
 public void testValidate_exceptionAnswer_invalidThrowable_nonVoidInvocation() {
     Invocation invocation = mock(InvocationOnMock.class);
     when(invocation.isVoid()).thenReturn(false);
     Answer<?> answer = mock(ThrowsException.class);
     when(answer.getThrowable()).thenReturn(new IllegalArgumentException());

     validator.validate(answer, invocation);
     verify(reporter).checkedExceptionInvalid(instanceOf(IllegalArgumentException.class));
 }

 @Test(expected = AssertionError.class)
 public void testValidate_exceptionAnswer_invalidThrowable_voidInvocation() {
     Invocation invocation = mock(InvocationOnMock.class);
     when(invocation.isVoid()).thenReturn(true);
     Answer<?> answer = mock(ThrowsException.class);
     when(answer.getThrowable()).thenReturn(new IllegalArgumentException());

     validator.validate(answer, invocation);
     verify(reporter).cannotStubWithNullThrowable();
 }
}