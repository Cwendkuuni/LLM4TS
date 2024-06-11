package org.mockito.internal.stubbing.answers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AnswersValidatorTest {

 private AnswersValidator validator;
 private Reporter reporter;

 @Before
 public void setup() {
 validator = new AnswersValidator();
 reporter = mock(Reporter.class);
 validator.reporter = reporter;
 }

 // Test for validate method
 @Test
 public void testValidate_ValidAnswer() {
 Invocation invocation = mock(InvocationOnMock.class);
 Answer<?> answer = mock(Answer.class);
 validator.validate(answer, invocation);
 verify(reporter, never()).onlyVoidMethodsCanBeSetToDoNothing();
 verify(reporter, never()).cannotStubVoidMethodWithAReturnValue();
 verify(reporter, never()).wrongTypeOfReturnValue(String.class, String.class, String.class);
 verify(reporter, never()).wrongTypeOfReturnValue(String.class, Class.class, String.class);
 verify(reporter, never()).checkedExceptionInvalid(Throwable.class);
 verify(reporter, never()).cannotStubWithNullThrowable();
 }

 // Test for validate method with ThrowsException answer
 @Test
 public void testValidate_ThrowsException() {
 Invocation invocation = mock(InvocationOnMock.class);
 Throwable throwable = new RuntimeException("Exception message");
 ThrowsException answer = new ThrowsException(throwable);
 validator.validate(answer, invocation);
 }

 // Test for validateDoNothing method with void invocation
 @Test(expected = AssertionError.class)
 public void testValidateDoNothing_VoidInvocation() {
 Invocation invocation = mock(InvocationOnMock.class);
 when(invocation.isVoid()).thenReturn(true);
 DoesNothing answer = mock(DoesNothing.class);
 validator.validateDoNothing(answer, invocation);
 }

 // Test for validateDoNothing method with non-void invocation
 @Test(expected = AssertionError.class)
 public void testValidateDoNothing_NonVoidInvocation() {
 Invocation invocation = mock(InvocationOnMock.class);
 when(invocation.isVoid()).thenReturn(false);
 DoesNothing answer = mock(DoesNothing.class);
 validator.validateDoNothing(answer, invocation);
 }

 // Test for validateReturnValue method with null return value and void invocation
 @Test(expected = AssertionError.class)
 public void testValidateReturnValue_NullReturnValue_VoidInvocation() {
 Invocation invocation = mock(InvocationOnMock.class);
 when(invocation.isVoid()).thenReturn(true);
 Returns answer = mock(Returns.class);
 when(answer.returnsNull()).thenReturn(true);
 validator.validateReturnValue(answer, invocation);
 }

 // Test for validateReturnValue method with null return value and non-void invocation of primitive type
 @Test(expected = AssertionError.class)
 public void testValidateReturnValue_NullReturnValue_PrimitiveType() {
 Invocation invocation = mock(InvocationOnMock.class);
 when(invocation.isVoid()).thenReturn(false);
 when(invocation.isValidReturnType(String.class)).thenReturn(true);
 when(invocation.isValidReturnType(byte.class)).thenReturn(false);
 Returns answer = mock(Returns.class);
 when(answer.returnsNull()).thenReturn(true);
 validator.validateReturnValue(answer, invocation);
 }

 // Test for validateReturnValue method with non-null return value and invalid return type
 @Test(expected = AssertionError.class)
 public void testValidateReturnValue_InvalidReturnType() {
 Invocation invocation = mock(InvocationOnMock.class);
 when(invocation.isValidReturnType(String.class)).thenReturn(false);
 Returns answer = mock(Returns.class);
 when(answer.getReturnType()).thenReturn(int.class);
 validator.validateReturnValue(answer, invocation);
 }

 // Test for validateException method with null throwable
 @Test(expected = AssertionError.class)
 public void testValidateException_NullThrowable() {
 Invocation invocation = mock(InvocationOnMock.class);
 ThrowsException answer = mock(ThrowsException.class);
 validator.validateException(answer, invocation);
 }

 // Test for validateException method with checked exception
 @Test(expected = AssertionError.class)
 public void testValidateException_CheckedException() {
 Invocation invocation = mock(InvocationOnMock.class);
 Throwable throwable = new IllegalStateException("Exception message");
 ThrowsException answer = new ThrowsException(throwable);
 validator.validateException(answer, invocation);
 }

 // Test for validateException method with runtime exception or error
 @Test
 public void testValidateException_RuntimeExceptionOrError() {
 Invocation invocation = mock(InvocationOnMock.class);
 Throwable throwable = new RuntimeException("Exception message");
 ThrowsException answer = new ThrowsException(throwable);
 validator.validateException(answer, invocation);
 }
}