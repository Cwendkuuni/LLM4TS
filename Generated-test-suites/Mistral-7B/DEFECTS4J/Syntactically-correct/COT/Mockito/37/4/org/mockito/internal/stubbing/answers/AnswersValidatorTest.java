package org.mockito.internal.stubbing.answers.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMockImpl;
import org.mockito.stubbing.Answer;

import org.mockito.internal.stubbing.answers.AnswersValidator;
import org.mockito.exceptions.Reporter;

import java.lang.reflect.Type;
import java.util.RuntimeException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.util.reflection.Whitebox.getInternalState;
import static org.mockito.internal.util.reflection.Whitebox.getMethod;
import static org.mockito.internal.util.reflection.Whitebox.setInternalState;

@RunWith(MockitoJUnitRunner.class)
public class AnswersValidatorTest {

 private AnswersValidator validator = new AnswersValidator();
 private Reporter reporter = mock(Reporter.class);

 @Test(expected = AssertionError.class)
 public void testValidateExceptionNullThrowable() {
     Answer<?> answer = mock(Answer.class);
     Invocation invocation = new InvocationOnMockImpl(mock(Object.class), "testMethod", new Object[0]);
     validator.validate(answer, invocation);
     Mockito.verify(reporter).cannotStubWithNullThrowable();
 }

 @Test
 public void testDoNothingVoidMethod() {
     DoesNothing answer = Mockito.mock(DoesNothing.class);
     Invocation invocation = new InvocationOnMockImpl(mock(Object.class), "testMethod", new Object[0]);
     validator.validateDoNothing(answer, invocation);
     verifyZeroInteractions(reporter);
 }

 @Test(expected = AssertionError.class)
 public void testDoNothingInvalidReturnType() {
     DoesNothing answer = mock(DoesNothing.class);
     Invocation invocation = new InvocationOnMockImpl(mock(Object.class), "testMethod", new Object[0]);
     doAnswer(answer).when(mock(Object.class)).testMethod();
     validator.validateDoNothing(answer, invocation);
     Mockito.verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
 }

 @Test
 public void testValidateReturnValueNullReturn() {
     Returns answer = mock(Returns.class);
     Invocation invocation = new InvocationOnMockImpl(mock(Object.class), "testMethod", new Object[0]);
     validator.validateReturnValue(answer, invocation);
     verifyZeroInteractions(reporter);
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnValueVoidMethodAndReturnValue() {
     Returns answer = mock(Returns.class);
     answer.returnsNull();
     Invocation invocation = new InvocationOnMockImpl(mock(Object.class), "testMethod", new Object[0]);
     doReturn(new Object()).when(mock(Object.class)).testMethod();
     validator.validateReturnValue(answer, invocation);
     Mockito.verify(reporter).cannotStubVoidMethodWithAReturnValue();
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnValueInvalidReturnType() {
     Returns answer = mock(Returns.class);
     Invocation invocation = new InvocationOnMockImpl(mock(Object.class), "testMethod", new Object[0]);
     doAnswer(answer).when(mock(Object.class)).testMethod();
     Type returnType = getMethod(Object.class, "testMethod").getReturnType();
     validator.validateReturnValue(answer, invocation);
     Mockito.verify(reporter).wrongTypeOfReturnValue(returnType.getTypeName(), answer.printReturnType(), "testMethod");
 }

 @Test
 public void testValidateExceptionNullAnswer() {
     Invocation invocation = new InvocationOnMockImpl(mock(Object.class), "testMethod", new Object[0]);
     validator.validateException(null, invocation);
     verifyZeroInteractions(reporter);
 }

 @Test
 public void testValidateExceptionRuntimeExceptionOrError() {
     ThrowsException answer = mock(ThrowsException.class);
     Throwable throwable = new RuntimeException();
     answer.setThrowable(throwable);
     Invocation invocation = new InvocationOnMockImpl(mock(Object.class), "testMethod", new Object[0]);
     validator.validateException(answer, invocation);
     verifyNoMoreInteractions(reporter);
 }

 @Test(expected = AssertionError.class)
 public void testValidateExceptionInvalidException() {
     ThrowsException answer = mock(ThrowsException.class);
     Invocation invocation = new InvocationOnMockImpl(mock(Object.class), "testMethod", new Object[0]);
     answer.setThrowable(new Object());
     validator.validateException(answer, invocation);
     Mockito.verify(reporter).checkedExceptionInvalid(answer.getThrowable());
 }

 @Test
 public void testValidateReturnValueNullAndPrimitive() {
     Returns answer = mock(Returns.class);
     answer.returnsNull();
     Invocation invocation = new InvocationOnMockImpl(mock(Object.class), "testMethod", new Object[0]);
     doAnswer(answer).when(mock(Object.class)).testMethod();
     validator.validateReturnValue(answer, invocation);
     Type returnType = getMethod(Object.class, "testMethod").getReturnType();
     if (returnType.isPrimitive()) {
         Mockito.verify(reporter).wrongTypeOfReturnValue(returnType.getTypeName(), "null", "testMethod");
     }
 }
}