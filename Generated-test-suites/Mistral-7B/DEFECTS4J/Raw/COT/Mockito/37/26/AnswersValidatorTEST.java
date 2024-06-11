package org.mockito.internal.stubbing.answers.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.exceptions.Reporter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doAnswerReturning;
import static org.mockito.Mockito.doAnswerThrowing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.doCallRealMethod;

import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.exceptions.Reporter;

import java.lang.reflect.Method;
import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class AnswersValidatorTest {

 private Reporter reporter = mock(Reporter.class);
 private AnswersValidator validator = new AnswersValidator();

 @Test
 public void testValidate_ReturnsAnswer_NoExceptions() {
     Answer<String> answer = mock(Answer.class);
     InvocationOnMock invocation = mock(InvocationOnMock.class);

     validator.validate(answer, invocation);

     verifyNoInteractions(reporter);
 }

 @Test
 public void testValidate_ThrowsExceptionAnswer_NoExceptions() {
     ThrowsException answer = mock(ThrowsException.class);
     InvocationOnMock invocation = mock(InvocationOnMock.class);

     when(answer.getThrowable()).thenReturn(new RuntimeException());

     validator.validate(answer, invocation);

     verifyNoInteractions(reporter);
 }

 @Test(expected = RuntimeException.class)
 public void testValidate_ThrowsExceptionAnswer_InvalidThrowableType() {
     ThrowsException answer = mock(ThrowsException.class);
     InvocationOnMock invocation = mock(InvocationOnMock.class);

     when(answer.getThrowable()).thenReturn(new IllegalStateException());

     validator.validate(answer, invocation);

     then(reporter).should().checkedExceptionInvalid(eq(new IllegalStateException()));
 }

 @Test
 public void testValidate_DoesNothingAnswer_InvalidMethodReturnType() {
     DoesNothing answer = mock(DoesNothing.class);
     InvocationOnMock invocation = mock(InvocationOnMock.class);

     when(invocation.getMethod()).thenReturn(new Method(Object.class, "testMethod", new Class[0]));
     doAnswer(willDoNothing()).when(answer).answer(any(InvocationOnMock.class));

     validator.validate(answer, invocation);

     then(reporter).should().onlyVoidMethodsCanBeSetToDoNothing();
 }

 @Test
 public void testValidate_ReturnsAnswer_InvalidReturnsNull() {
     Returns answer = mock(Returns.class);
     InvocationOnMock invocation = mock(InvocationOnMock.class);

     doAnswerReturning(isNull(), any(InvocationOnMock.class)).when(answer).answer(any(InvocationOnMock.class));
     when(invocation.isVoid()).thenReturn(false);

     validator.validate(answer, invocation);

     then(reporter).should().cannotStubVoidMethodWithAReturnValue();
 }

 @Test
 public void testValidate_ReturnsAnswer_InvalidReturnType() {
     Returns answer = mock(Returns.class);
     InvocationOnMock invocation = mock(InvocationOnMock.class);

     doAnswerReturning(1, any(InvocationOnMock.class)).when(answer).answer(any(InvocationOnMock.class));
     when(invocation.getMethod()).thenReturn(new Method(Object.class, "testMethod", new Class[0]));
     when(invocation.isValidReturnType(answer.getReturnType())).thenReturn(false);

     validator.validate(answer, invocation);

     then(reporter).should().wrongTypeOfReturnValue(eq("int"), eq(answer.getReturnType().getName()), eq("testMethod"));
 }

 @Test(expected = AssertionError.class)
 public void testValidate_ValidateException_NullAnswer() {
     ThrowsException answer = null;
     InvocationOnMock invocation = mock(InvocationOnMock.class);

     validator.validate(answer, invocation);

     then(reporter).should().cannotStubWithNullThrowable();
 }

 @Test
 public void testValidate_ValidateException_ValidThrowable() {
     ThrowsException answer = mock(ThrowsException.class);
     InvocationOnMock invocation = mock(InvocationOnMock.class);

     when(answer.getThrowable()).thenReturn(new IOException("Test error"));

     validator.validate(answer, invocation);

     verifyNoMoreInteractions(reporter);
 }

 @Test(expected = AssertionError.class)
 public void testValidate_ValidateException_InvalidExceptionType() {
     ThrowsException answer = mock(ThrowsException.class);
     InvocationOnMock invocation = mock(InvocationOnMock.class);

     when(answer.getThrowable()).thenReturn(new NullPointerException());

     validator.validate(answer, invocation);

     then(reporter).should().checkedExceptionInvalid(eq(new NullPointerException()));
 }

 @Test
 public void testValidate_ValidateReturnValue_ValidReturnValue() {
     Returns answer = mock(Returns.class);
     InvocationOnMock invocation = mock(InvocationOnMock.class);

     when(invocation.isValidReturnType(answer.getReturnType())).thenReturn(true);
     when(invocation.isVoid()).thenReturn(false);
     doAnswerReturning(1.0, any(InvocationOnMock.class)).when(answer).answer(any(InvocationOnMock.class));

     validator.validate(answer, invocation);

     verifyNoInteractions(reporter);
 }

 @Test
 public void testValidate_ValidateReturnValue_InvalidReturnType() {
     Returns answer = mock(Returns.class);
     InvocationOnMock invocation = mock(InvocationOnMock.class);

     when(invocation.isValidReturnType(answer.getReturnType())).thenReturn(false);
     when(invocation.isVoid()).thenReturn(true);

     validator.validate(answer, invocation);

     then(reporter).should().cannotStubVoidMethodWithAReturnValue();
 }

 @Test
 public void testValidate_ValidateReturnValue_ValidNullReturn() {
     Returns answer = mock(Returns.class);
     InvocationOnMock invocation = mock(InvocationOnMock.class);

     when(invocation.isVoid()).thenReturn(true);
     when(answer.returnsNull()).thenReturn(true);
     when(invocation.returnsPrimitive()).thenReturn(true);

     validator.validate(answer, invocation);

     verifyNoInteractions(reporter);
 }

 @Test
 public void testValidate_ValidateReturnValue_InvalidNullReturn() {
     Returns answer = mock(Returns.class);
     InvocationOnMock invocation = mock(InvocationOnMock.class);

     when(invocation.isVoid()).thenReturn(false);
     when(answer.returnsNull()).thenReturn(true);
     when(invocation.isValidReturnType(answer.getReturnType())).thenReturn(true);

     validator.validate(answer, invocation);

     then(reporter).should().wrongTypeOfReturnValue(eq("void"), eq("null"), eq("testMethod"));
 }

 @Test
 public void testValidate_ValidateReturnValue_InvalidNonPrimitiveReturnType() {
     Returns answer = mock(Returns.class);
     InvocationOnMock invocation = mock(InvocationOnMock.class);

     when(invocation.isValidReturnType(answer.getReturnType())).thenReturn(true);
     when(answer.getReturnType()).thenReturn(String[].class);
     when(invocation.isVoid()).thenReturn(false);

     validator.validate(answer, invocation);

     then(reporter).should().wrongTypeOfReturnValue(eq("void"), eq("java.lang.String[]"), eq("testMethod"));
 }
}