package org.mockito.internal.stubbing.answers.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answer;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.runners.JUnit4;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(JUnit4.class)
public class AnswersValidatorTest {

 private AnswersValidator validator = new AnswersValidator();

 @Test
 public void testValidate_Returns_Null() {
     Answer<String> answer = Mockito.mock(Answer.class);
     Invocation<String> invocation = mock(Invocation.class);

     validator.validate(answer, invocation);

     verifyZeroInteractions(answer);
     verifyZeroInteractions(invocation);
 }

 @Test
 public void testValidate_DoesNothing() {
     Answer<Void> answer = Mockito.mock(Answers.DoesNothing.class);
     Invocation<Void> invocation = mock(Invocation.class);

     validator.validate(answer, invocation);

     verify(answer).answer(invocation.getMock(), invocation.getMethod(), invocation.getArguments());
     verifyNoMoreInteractions(answer);
     verifyZeroInteractions(invocation);
 }

 @Test
 public void testValidate_ThrowsException_NullThrowable() {
     Answer<String> answer = Mockito.mock(ThrowsException.class);
     Invocation<String> invocation = mock(Invocation.class);

     validator.validate(answer, invocation);

     verify(answer).answer(invocation.getMock(), invocation.getMethod(), invocation.getArguments());
     verifyZeroInteractions(answer);
     verifyZeroInteractions(invocation);

     verify(reporter).cannotStubWithNullThrowable();
 }

 @Test
 public void testValidate_ThrowsException_InvalidException() {
     Answer<String> answer = mock(ThrowsException.class);
     Throwable throwable = new IllegalArgumentException("Invalid argument");
     Invocation<String> invocation = mock(Invocation.class);

     answer.setThrowable(throwable);

     validator.validate(answer, invocation);

     verify(answer).answer(invocation.getMock(), invocation.getMethod(), invocation.getArguments());
     verifyZeroInteractions(answer);
     verifyZeroInteractions(invocation);

     verify(reporter).checkedExceptionInvalid(throwable);
 }

 @Test
 public void testValidate_ThrowsException_ValidException() {
     Answer<String> answer = mock(ThrowsException.class);
     Throwable throwable = new RuntimeException("Runtime error");
     Invocation<String> invocation = mock(Invocation.class);

     answer.setThrowable(throwable);

     validator.validate(answer, invocation);

     verify(answer).answer(invocation.getMock(), invocation.getMethod(), invocation.getArguments());
     verifyZeroInteractions(answer);
     verifyZeroInteractions(invocation);
 }

 @Test
 public void testValidate_Returns_Void_DoNothing() {
     Answer<Void> answer = Mockito.mock(Answers.DoesNothing.class);
     Invocation<Void> invocation = mock(Invocation.class);
     invocation.markVoid();

     validator.validate(answer, invocation);

     verify(answer).answer(invocation.getMock(), invocation.getMethod(), invocation.getArguments());
     verifyNoMoreInteractions(answer);
     verifyZeroInteractions(invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidate_Returns_Primitive_Null() {
     Answer<Integer> answer = Mockito.mock(Returns.class);
     answer.returnsNull();
     Invocation<Integer> invocation = mock(Invocation.class);
     invocation.returnsPrimitive();

     validator.validate(answer, invocation);

     verify(answer).answer(invocation.getMock(), invocation.getMethod(), invocation.getArguments());
     verifyZeroInteractions(answer);
     verifyZeroInteractions(invocation);

     verify(reporter).wrongTypeOfReturnValue(invocation.printMethodReturnType(), "null", invocation.getMethodName());
 }

 @Test(expected = AssertionError.class)
 public void testValidate_Returns_InvalidType() {
     Answer<String> answer = Mockito.mock(Returns.class);
     answer.returns(new Object());
     Invocation<String> invocation = mock(Invocation.class);

     validator.validate(answer, invocation);

     verify(answer).answer(invocation.getMock(), invocation.getMethod(), invocation.getArguments());
     verifyZeroInteractions(answer);
     verifyZeroInteractions(invocation);

     verify(reporter).wrongTypeOfReturnValue(invocation.printMethodReturnType(), answer.printReturnType(), invocation.getMethodName());
 }
}