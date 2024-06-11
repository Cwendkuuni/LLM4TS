package org.mockito.internal.stubbing.answers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
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
 validator.reporter = reporter;
 }

 @Test
 public void testValidate_AnswerIsThrowsException() {
 ThrowsException answer = mock(ThrowsException.class);
 answer.getThrowable = () -> new IllegalArgumentException("Test Exception");
 Invocation invocation = mock(InvocationOnMock.class);

 validator.validate(answer, invocation);
 verify(reporter, never()).onlyVoidMethodsCanBeSetToDoNothing();
 verify(reporter, never()).cannotStubWithNullThrowable();
 verify(reporter, never()).checkedExceptionInvalid(any(Throwable.class));
 }

 @Test
 public void testValidate_AnswerIsReturns() {
 Returns answer = mock(Returns.class);
 answer.getReturnValue = () -> "Test String";
 Invocation invocation = mock(InvocationOnMock.class);
 invokeMethodWithReturnValue(invocation, "returnValueType", "Test String");

 validator.validate(answer, invocation);
 verify(reporter, never()).onlyVoidMethodsCanBeSetToDoNothing();
 verify(reporter, never()).cannotStubVoidMethodWithAReturnValue();
 verify(reporter, never()).wrongTypeOfReturnValue(anyString(), anyString(), anyString());
 }

 @Test
 public void testValidate_AnswerIsDoNothing() {
 DoesNothing answer = mock(DoesNothing.class);
 Invocation invocation = mock(InvocationOnMock.class);

 validator.validate(answer, invocation);
 verify(reporter, never()).onlyVoidMethodsCanBeSetToDoNothing();
 }

 @Test(expected = AssertionError.class)
 public void testValidate_AnswerIsDoNothingButInvocationIsVoid() {
 DoesNothing answer = mock(DoesNothing.class);
 Invocation invocation = mock(InvocationOnMock.class);
 when(invocation.isVoid()).thenReturn(true);

 validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidate_AnswerIsReturnsNullButInvocationIsPrimitive() {
 Returns answer = mock(Returns.class);
 answer.returnsNull = true;
 Invocation invocation = mock(InvocationOnMock.class);
 when(invocation.isVoid()).thenReturn(false);
 when(invocation.isValidReturnType(anyClass())).thenReturn(false);
 when(invocation.returnsPrimitive()).thenReturn(true);

 validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidate_AnswerIsReturnsNonNullButInvalidType() {
 Returns answer = mock(Returns.class);
 answer.returnsNull = false;
 Invocation invocation = mock(InvocationOnMock.class);
 when(invocation.isValidReturnType(anyClass())).thenReturn(false);

 validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidate_AnswerIsThrowsExceptionButInvalidType() {
 ThrowsException answer = mock(ThrowsException.class);
 answer.getThrowable = () -> new IllegalStateException("Test Exception");
 Invocation invocation = mock(InvocationOnMock.class);
 when(invocation.isValidException(any(Throwable.class))).thenReturn(false);

 validator.validate(answer, invocation);
 }

 @Test
 public void testValidate_AnswerIsThrowsRuntimeExceptionOrError() {
 ThrowsException answer = mock(ThrowsException.class);
 answer.getThrowable = () -> new RuntimeException("Test Exception");
 Invocation invocation = mock(InvocationOnMock.class);

 validator.validate(answer, invocation);
 verifyNoMoreInteractions(reporter);
 }

 private void invokeMethodWithReturnValue(InvocationOnMock invocation, String returnType, String returnValue) {
 when(invocation.isVoid()).thenReturn(false);
 when(invocation.isValidReturnType(anyClass())).thenReturn(true);
 when(invocation.getMethod()).thenReturn(() -> mock(Method.class));
 when(invocation.getMethod().getReturnType()).thenReturn(returnType.equals("void") ? null : Class.forName(returnType));
 when(invocation.getMethodName()).thenReturn("testMethod");
 when(invocation.getThis()).thenReturn(mock(Object.class));
 when(invocation.getArguments()).thenReturn(new Object[0]);
 when(invocation.getProxy()).thenReturn(mock(Object.class));
 when(invocation.getTarget()).thenReturn(mock(Object.class));
 when(invocation.getMock()).thenReturn(mock(Object.class));
 when(invocation.getCallSite()).thenReturn(mock(CallSite.class));
 when(invocation.getInvocation()).thenReturn(invocation);
 when(invocation.getAnswer()).thenReturn(answer);
 }

}