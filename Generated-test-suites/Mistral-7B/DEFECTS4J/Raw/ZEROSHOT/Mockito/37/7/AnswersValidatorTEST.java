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
 public void setup() {
 validator = new AnswersValidator();
 reporter = mock(Reporter.class);
 }

 @Test
 public void testValidate_nullAnswer() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 AnswersValidator validator = new AnswersValidator();
 validator.validate(null, invocation);
 verifyZeroInteractions(reporter);
 }

 @Test(expected = NullPointerException.class)
 public void testValidate_nullInvocation() {
 AnswersValidator validator = new AnswersValidator();
 validator.validate(mock(Answer.class), null);
 }

 @Test
 public void testValidate_throwException() {
 ThrowsException answer = mock(ThrowsException.class);
 when(answer.getThrowable()).thenReturn(new RuntimeException("Test Exception"));
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 validator.validate(answer, invocation);
 verifyNoMoreInteractions(reporter);
 }

 @Test
 public void testValidate_throwCheckedException() {
 ThrowsException answer = mock(ThrowsException.class);
 when(answer.getThrowable()).thenReturn(new IOException("Test Exception"));
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 validator.validate(answer, invocation);
 verify(reporter).checkedExceptionInvalid(isA(IOException.class));
 }

 @Test
 public void testValidate_throwNullThrowable() {
 ThrowsException answer = mock(ThrowsException.class);
 when(answer.getThrowable()).thenReturn(null);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 validator.validate(answer, invocation);
 verify(reporter).cannotStubWithNullThrowable();
 }

 @Test
 public void testValidate_doesNothing() {
 DoesNothing answer = mock(DoesNothing.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 validator.validate(answer, invocation);
 verifyNoMoreInteractions(reporter);
 }

 @Test
 public void testValidate_voidMethod_doNothing() {
 DoesNothing answer = mock(DoesNothing.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(invocation.isVoid()).thenReturn(true);
 validator.validate(answer, invocation);
 verifyNoMoreInteractions(reporter);
 }

 @Test
 public void testValidate_voidMethod_notDoNothing() {
 DoesNothing answer = mock(DoesNothing.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(invocation.isVoid()).thenReturn(false);
 validator.validate(answer, invocation);
 verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
 }

 @Test
 public void testValidate_notVoidMethod_doNothing() {
 DoesNothing answer = mock(DoesNothing.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 validator.validate(answer, invocation);
 verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
 }

 @Test
 public void testValidate_returnsNull() {
 Returns answer = mock(Returns.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 validator.validate(answer, invocation);
 verifyNoMoreInteractions(reporter);
 }

 @Test
 public void testValidate_voidMethod_returnsNull() {
 Returns answer = mock(Returns.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(invocation.isVoid()).thenReturn(true);
 validator.validate(answer, invocation);
 verifyNoMoreInteractions(reporter);
 }

 @Test
 public void testValidate_notVoidMethod_returnsNull() {
 Returns answer = mock(Returns.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 validator.validate(answer, invocation);
 verify(reporter).cannotStubVoidMethodWithAReturnValue();
 }

 @Test
 public void testValidate_invalidReturnType() {
 Returns answer = mock(Returns.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(answer.getReturnType()).thenReturn(String.class);
 when(invocation.getMethod()).thenReturn(mock(Method.class));
 when(invocation.getMethod().getReturnType()).thenReturn(Integer.class);
 validator.validate(answer, invocation);
 verify(reporter).wrongTypeOfReturnValue(isA(Integer.class.getTypeName()), isA(String.class.getTypeName()), anyString());
 }

 @Test
 public void testValidate_validReturnType() {
 Returns answer = mock(Returns.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(answer.getReturnType()).thenReturn(String.class);
 when(invocation.getMethod()).thenReturn(mock(Method.class));
 when(invocation.getMethod().getReturnType()).thenReturn(String.class);
 validator.validate(answer, invocation);
 verifyNoMoreInteractions(reporter);
 }

 @Test
 public void testValidate_invalidPrimitiveReturnTypeAndNull() {
 Returns answer = mock(Returns.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(answer.getReturnType()).thenReturn(boolean.class);
 when(invocation.returnsPrimitive()).thenReturn(true);
 validator.validate(answer, invocation);
 verify(reporter).wrongTypeOfReturnValue(isA("boolean"), "null", anyString());
 }

 @Test
 public void testValidate_validPrimitiveReturnType() {
 Returns answer = mock(Returns.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(answer.getReturnType()).thenReturn(boolean.class);
 when(invocation.returnsPrimitive()).thenReturn(true);
 validator.validate(answer, invocation);
 verifyNoMoreInteractions(reporter);
 }

 @Test
 public void testValidate_invalidPrimitiveReturnTypeAndInvalidType() {
 Returns answer = mock(Returns.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(answer.getReturnType()).thenReturn(int.class);
 when(invocation.getMethod()).thenReturn(mock(Method.class));
 when(invocation.getMethod().getReturnType()).thenReturn(boolean.class);
 validator.validate(answer, invocation);
 verify(reporter).wrongTypeOfReturnValue(isA("boolean"), isA("int"), anyString());
 }

 @Test
 public void testValidate_validPrimitiveReturnTypeAndValidType() {
 Returns answer = mock(Returns.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(answer.getReturnType()).thenReturn(int.class);
 when(invocation.getMethod()).thenReturn(mock(Method.class));
 when(invocation.getMethod().getReturnType()).thenReturn(int.class);
 validator.validate(answer, invocation);
 verifyNoMoreInteractions(reporter);
 }
}