package org.mockito.internal.stubbing.answers.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Exceptions;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.assertThrows;

@RunWith(MockitoJUnitRunner.class)
public class AnswersValidatorTest {

 private AnswersValidator validator = new AnswersValidator();

 @Test(expected = AssertionError.class)
 public void testValidateThrowsExceptionWithNullAnswer() {
  validator.validate(null, new InvocationOnMock(Mockito.mock(Class.class), new Object[0], new Object[0][], 0));
 }

 @Test
 public void testValidateThrowsException() {
  Answer<?> answer = Mockito.spy(new ThrowsException(new RuntimeException("Exception message")){});
  Invocation invocation = new InvocationOnMock(Mockito.mock(Class.class), new Object[0], new Object[0][], 0);

  validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateThrowsExceptionWithInvalidExceptionType() {
  Answer<?> answer = Mockito.spy(new ThrowsException(new Object[0].getClass()){});
  Invocation invocation = new InvocationOnMock(Mockito.mock(Class.class), new Object[0], new Object[0][], 0);

  validator.validate(answer, invocation);
 }

 @Test
 public void testValidateReturns() {
  Answer<?> answer = Mockito.spy(new Returns<String>("test"){});
  Invocation invocation = new InvocationOnMock(Mockito.mock(Class.class), new Object[0], new Object[0][], 0);

  validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnsWithVoidMethod() {
  Answer<?> answer = Mockito.spy(new Returns<String>("test"){});
  Invocation invocation = new InvocationOnMock(Mockito.mock(Class.class), new Object[0], null, 0);

  validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnsWithInvalidReturnType() {
  Answer<Integer> answer = Mockito.spy(new Returns<Integer>(1){});
  Invocation invocation = new InvocationOnMock(Mockito.mock(Class.class), new Object[0], new Object[0][], 0);

  validator.validate(answer, invocation);
 }

 @Test
 public void testValidateReturnsNull() {
  Answer<String> answer = Mockito.spy(new Returns<String>(null){});
  Invocation invocation = new InvocationOnMock(Mockito.mock(Class.class), new Object[0], new Object[0][], 0);

  if (invocation.isVoid()) {
    validator.validate(answer, invocation);
  } else {
    verify(answer).returnsNull();
    validator.validateReturnValue(answer, invocation);
  }
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnsNullWithPrimitiveType() {
  Answer<Boolean> answer = Mockito.spy(new Returns<Boolean>(null){});
  Invocation invocation = new InvocationOnMock(Mockito.mock(Class.class), new Object[0], new Object[0][], 0);

  validator.validate(answer, invocation);
 }

 @Test
 public void testValidateDoNothing() {
  Answer<?> answer = Mockito.spy(new DoesNothing(){});
  Invocation invocation = new InvocationOnMock(Mockito.mock(Class.class), new Object[0], null, 0);

  validator.validateDoNothing(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateDoNothingWithNonVoidMethod() {
  Answer<?> answer = Mockito.spy(new DoesNothing(){});
  Invocation invocation = new InvocationOnMock(Mockito.mock(Class.class), new Object[0], new Object[0][], 1);

  validator.validateDoNothing(answer, invocation);
 }
}