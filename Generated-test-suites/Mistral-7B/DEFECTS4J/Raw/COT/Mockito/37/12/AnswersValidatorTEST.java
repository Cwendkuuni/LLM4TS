package org.mockito.internal.stubbing.answers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMockException;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class AnswersValidatorTest {

 private AnswersValidator validator;
 private Reporter reporter;

 @Before
 public void setUp() {
 validator = new AnswersValidator();
 reporter = mock(Reporter.class);
 validator.reporter = reporter;
 }

 // Test for validate method
 @Test
 public void testValidate_ValidAnswer() {
 Invocation invocation = Mockito.mock(Invocation.class);
 Returns returnsAnswer = Mockito.mock(Returns.class);
 validator.validate(returnsAnswer, invocation);
 }

 // Test for validate method with ThrowsException answer
 @Test
 public void testValidate_ThrowsExceptionAnswer() {
 Invocation invocation = Mockito.mock(Invocation.class);
 ThrowsException throwsAnswer = Mockito.mock(ThrowsException.class);
 validator.validate(throwsAnswer, invocation);
 }

 // Test for validate method with DoesNothing answer
 @Test
 public void testValidate_DoesNothingAnswer() {
 Invocation invocation = Mockito.mock(Invocation.class);
 DoesNothing doesNothingAnswer = Mockito.mock(DoesNothing.class);
 validator.validate(doesNothingAnswer, invocation);
 }

 // Test for validateDoNothing method with non-void method
 @Test(expected = InvocationOnMockException.class)
 public void testValidateDoNothing_NonVoidMethod() {
 Invocation invocation = Mockito.mock(Invocation.class);
 DoesNothing doesNothingAnswer = spy(Mockito.mock(DoesNothing.class));
 doesNothingAnswer.answer(ArgumentMatchers.any());
 Validator validatorSpy = spy(validator);
 validatorSpy.validateDoNothing(doesNothingAnswer, invocation);
 }

 // Test for validateReturnValue method with void method and null return value
 @Test(expected = InvocationOnMockException.class)
 public void testValidateReturnValue_VoidMethodAndNullReturnValue() {
 Invocation invocation = Mockito.mock(Invocation.class);
 Returns returnsAnswer = Mockito.mock(Returns.class);
 returnsAnswer.returnsNull();
 Validator validatorSpy = spy(validator);
 validatorSpy.validateReturnValue(returnsAnswer, invocation);
 }

 // Test for validateReturnValue method with non-void method and incorrect return type
 @Test(expected = InvocationOnMockException.class)
 public void testValidateReturnValue_IncorrectReturnType() {
 Invocation invocation = Mockito.mock(Invocation.class);
 Returns returnsAnswer = Mockito.mock(Returns.class);
 returnsAnswer.returns(1); // int return value for a method that should return String
 Validator validatorSpy = spy(validator);
 validatorSpy.validateReturnValue(returnsAnswer, invocation);
 }

 // Test for validateException method with null throwable
 @Test(expected = InvocationOnMockException.class)
 public void testValidateException_NullThrowable() {
 Invocation invocation = Mockito.mock(Invocation.class);
 ThrowsException throwsAnswer = Mockito.mock(ThrowsException.class);
 ThrowsException throwsAnswerSpy = spy(throwsAnswer);
 throwsAnswerSpy.getThrowable = null;
 Validator validatorSpy = spy(validator);
 validatorSpy.validateException(throwsAnswer, invocation);
 }

 // Test for validateException method with incorrect exception type
 @Test(expected = InvocationOnMockException.class)
 public void testValidateException_IncorrectExceptionType() {
 Invocation invocation = Mockito.mock(Invocation.class);
 ThrowsException throwsAnswer = Mockito.mock(ThrowsException.class);
 Throwable throwable = new IllegalStateException();
 validator.validateException(throwsAnswer, invocation);
 }
}