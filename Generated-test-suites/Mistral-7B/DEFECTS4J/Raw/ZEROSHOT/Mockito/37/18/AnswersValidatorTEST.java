package org.mockito.internal.stubbing.answers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.verify;

public class AnswersValidatorTest {

 private AnswersValidator validator;
 private Reporter reporter;

 @Before
 public void setUp() {
 validator = new AnswersValidator();
 reporter = mock(Reporter.class);
 }

 @Test
 public void testValidateDoNothingWithVoidMethod() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 validator.validate(mock(DoesNothing.class), invocation);
 verifyZeroInteractions(reporter);
 }

 @Test(expected = AssertionError.class)
 public void testValidateDoNothingWithNonVoidMethod() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 validator.validate(mock(DoesNothing.class), invocation);
 reporter.onlyVoidMethodsCanBeSetToDoNothing();
 }

 @Test
 public void testValidateReturnValueWithValidReturn() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 Class<Integer> returnType = Integer.class;
 validator.validate(mock(Returns.class), invocation);
 verifyZeroInteractions(reporter);
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnValueWithVoidMethod() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 validator.validate(mock(Returns.class), invocation);
 reporter.cannotStubVoidMethodWithAReturnValue();
 }

 @Test
 public void testValidateReturnValueWithNullReturnAndPrimitiveType() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 validator.validate(mock(Returns.class), invocation);
 reporter.wrongTypeOfReturnValue(invocation.printMethodReturnType(), "null", invocation.getMethodName());
 }

 @Test
 public void testValidateReturnValueWithInvalidReturnType() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 Class<String> returnType = String.class;
 validator.validate(mock(Returns.class), invocation);
 reporter.wrongTypeOfReturnValue(invocation.printMethodReturnType(), returnType.getName(), invocation.getMethodName());
 }

 @Test
 public void testValidateReturnValueWithValidNullReturn() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 validator.validate(mock(Returns.class), invocation);
 verifyZeroInteractions(reporter);
 }

 @Test(expected = AssertionError.class)
 public void testValidateExceptionWithNullThrowable() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 validator.validate(mock(ThrowsException.class), invocation);
 reporter.cannotStubWithNullThrowable();
 }

 @Test
 public void testValidateExceptionWithRuntimeException() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 Throwable throwable = new RuntimeException();
 validator.validate(mock(ThrowsException.class), invocation);
 verifyNoMoreInteractions(reporter);
 }

 @Test(expected = AssertionError.class)
 public void testValidateExceptionWithInvalidCheckedException() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 Throwable throwable = new IllegalStateException();
 validator.validate(mock(ThrowsException.class), invocation);
 reporter.checkedExceptionInvalid(throwable);
 }

}