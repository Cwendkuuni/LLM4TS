package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.internal.stubbing.answers.AnswersValidator.reporter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.invocation;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AnswersValidatorTest {

 AnswersValidator validator = new AnswersValidator();
 Reporter reporter = new Reporter();

 @Test(expected = AssertionError.class)
 public void testDoNothingWithNonVoidMethod() {
 AnswersValidator validator = new AnswersValidator();
 Object mock = mock(String.class);
 DoesNothing answer = mock(DoesNothing.class);
 when(mock.someMethod()).then(answer);
 validator.validate(answer, invocation(mock, "someMethod"));
 reporter.onlyVoidMethodsCanBeSetToDoNothing();
 }

 @Test(expected = AssertionError.class)
 public void testReturnNullWithNonPrimitiveVoidMethod() {
 AnswersValidator validator = new AnswersValidator();
 Object mock = mock(String.class);
 Returns answer = mock(Returns.class);
 when(mock.someMethod()).then(answer);
 answer.returnsNull();
 validator.validate(answer, invocation(mock, "someMethod"));
 reporter.wrongTypeOfReturnValue("String", "null", "someMethod");
 }

 @Test(expected = AssertionError.class)
 public void testReturnNonValidTypeWithVoidMethod() {
 AnswersValidator validator = new AnswersValidator();
 Object mock = mock(String.class);
 Returns answer = mock(Returns.class);
 when(mock.someMethod()).then(answer);
 answer.returns(new Integer(1));
 validator.validate(answer, invocation(mock, "someMethod"));
 reporter.wrongTypeOfReturnValue("void", "int", "someMethod");
 }

 @Test(expected = AssertionError.class)
 public void testReturnNonValidTypeWithNonVoidMethod() {
 AnswersValidator validator = new AnswersValidator();
 Object mock = mock(String.class);
 Returns answer = mock(Returns.class);
 when(mock.someMethod()).then(answer);
 answer.returns(new Integer(1));
 validator.validate(answer, invocation(mock, "someMethod", new String[]{"arg1"}));
 reporter.wrongTypeOfReturnValue("String[]", "int", "someMethod");
 }

 @Test(expected = AssertionError.class)
 public void testThrowNullThrowable() {
 AnswersValidator validator = new AnswersValidator();
 Object mock = mock(String.class);
 ThrowsException answer = mock(ThrowsException.class);
 when(mock.someMethod()).then(answer);
 answer.getThrowable() = null;
 validator.validate(answer, invocation(mock, "someMethod"));
 reporter.cannotStubWithNullThrowable();
 }

 @Test(expected = AssertionError.class)
 public void testThrowInvalidCheckedException() {
 AnswersValidator validator = new AnswersValidator();
 Object mock = mock(String.class);
 ThrowsException answer = mock(ThrowsException.class);
 when(mock.someMethod()).then(answer);
 answer.getThrowable() = new RuntimeException();
 validator.validate(answer, invocation(mock, "someMethod"));
 }

 @Test
 public void testValidAnswer() {
 AnswersValidator validator = new AnswersValidator();
 Object mock = mock(String.class);
 DoesNothing answer = mock(DoesNothing.class);
 when(mock.someMethod()).then(answer);
 validator.validate(answer, invocation(mock, "someMethod"));
 verifyNoMoreInteractions(mock);
 }

 @Test
 public void testValidReturnValue() {
 AnswersValidator validator = new AnswersValidator();
 Object mock = mock(String.class);
 Returns answer = mock(Returns.class);
 when(mock.someMethod()).then(answer);
 answer.returns(new String("someValue"));
 validator.validate(answer, invocation(mock, "someMethod"));
 verifyNoMoreInteractions(mock);
 }

 @Test
 public void testValidThrowException() {
 AnswersValidator validator = new AnswersValidator();
 Object mock = mock(String.class);
 ThrowsException answer = mock(ThrowsException.class);
 when(mock.someMethod()).then(answer);
 answer.getThrowable() = new IllegalArgumentException("Some error message");
 validator.validate(answer, invocation(mock, "someMethod"));
 verifyNoMoreInteractions(mock);
 }

 @Test
 public void testValidAnswerWithVoidMethod() {
 AnswersValidator validator = new AnswersValidator();
 Object mock = mock(String.class);
 DoesNothing answer = mock(DoesNothing.class);
 when(mock.someMethod()).then(answer);
 validator.validate(answer, invocation(mock, "someMethod"));
 verifyZeroInteractions(mock);
 }

 @Test
 public void testValidReturnValueWithVoidMethod() {
 AnswersValidator validator = new AnswersValidator();
 Object mock = mock(String.class);
 Returns answer = mock(Returns.class);
 when(mock.someMethod()).then(answer);
 answer.returnsNull();
 validator.validate(answer, invocation(mock, "someMethod"));
 verifyZeroInteractions(mock);
 }
}