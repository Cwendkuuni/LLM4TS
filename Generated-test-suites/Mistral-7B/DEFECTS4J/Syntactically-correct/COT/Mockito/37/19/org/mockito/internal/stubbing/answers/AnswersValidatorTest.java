package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class AnswersValidatorTest {

 private Reporter reporter = mock(Reporter.class);

 @Test(expected = AssertionError.class)
 public void testValidate_ThrowsException_NullThrowable() {
     AnswersValidator validator = spy(new AnswersValidator());
     Answer<?> answer = mock(Answer.class);
     Invocation invocation = mock(Invocation.class);

     validator.validate(answer, invocation);
     validator.validateException(null, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidate_ThrowsException_InvalidException() {
     AnswersValidator validator = new AnswersValidator();
     ThrowsException answer = mock(ThrowsException.class);
     Invocation invocation = mock(Invocation.class);
     Throwable throwable = new Exception("test exception");

     validator.validate(answer, invocation);
     validator.validateException(answer, invocation);
     validator.validateException(answer, invocation); // second call should pass

     validator.validateException(answer, invocation); // third call should throw an exception
     validator.reporter.checkedExceptionInvalid(ArgumentMatchers.any(Throwable.class));
 }

 @Test(expected = AssertionError.class)
 public void testValidate_Returns_VoidMethodWithReturnValue() {
     AnswersValidator validator = new AnswersValidator();
     Returns answer = mock(Returns.class);
     Invocation invocation = mock(Invocation.class);

     validator.validate(answer, invocation);
     validator.validateReturnValue(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidate_Returns_NullReturnValueAndPrimitiveMethod() {
     AnswersValidator validator = new AnswersValidator();
     Returns answer = mock(Returns.class);
     Invocation invocation = mock(Invocation.class);
     answer.returnsNull();

     validator.validate(answer, invocation);
     validator.validateReturnValue(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidate_Returns_InvalidReturnType() {
     AnswersValidator validator = new AnswersValidator();
     Returns answer = mock(Returns.class);
     Invocation invocation = mock(Invocation.class);
     Class<Integer> intType = Integer.class;

     validator.validate(answer, invocation);
     answer.returns(1);

     validator.validateReturnValue(answer, invocation);
     validator.reporter.wrongTypeOfReturnValue(invocation.printMethodReturnType(), intType.getName(), invocation.getMethodName());
 }

 @Test
 public void testValidate_DoesNothing_VoidMethod() {
     AnswersValidator validator = new AnswersValidator();
     DoesNothing answer = mock(DoesNothing.class);
     Invocation invocation = mock(Invocation.class);

     validator.validate(answer, invocation);
     validator.validateDoNothing(answer, invocation);
 }

 @Test
 public void testValidate_DoesNothing_NonVoidMethod() {
     AnswersValidator validator = new AnswersValidator();
     DoesNothing answer = mock(DoesNothing.class);
     Invocation invocation = mock(Invocation.class);
     Class<String> stringType = String.class;

     validator.validate(answer, invocation);
     validator.validateDoNothing(answer, invocation);
     validator.reporter.onlyVoidMethodsCanBeSetToDoNothing();
 }

 @Test
 public void testValidate_ValidAnswer() {
     AnswersValidator validator = new AnswersValidator();
     Returns answer = mock(Returns.class);
     Invocation invocation = mock(Invocation.class);

     validator.validate(answer, invocation);
 }

}