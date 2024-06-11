package org.mockito.internal.stubbing.answers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMockException;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Type;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyThrow;

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
 public void validate_whenAnswerIsThrowsException_shouldNotThrowException() {
     ThrowsException throwsException = mock(ThrowsException.class);
     Invocation invocation = Mockito.mock(Invocation.class);

     validator.validate(throwsException, invocation);
     verifyZeroInteractions(reporter);
 }

 @Test
 public void validate_whenAnswerIsReturns_shouldNotThrowException() {
     Returns returns = mock(Returns.class);
     Invocation invocation = Mockito.mock(Invocation.class);

     validator.validate(returns, invocation);
     verifyZeroInteractions(reporter);
 }

 @Test
 public void validate_whenAnswerIsDoNothing_shouldNotThrowException() {
     DoesNothing doesNothing = mock(DoesNothing.class);
     Invocation invocation = Mockito.mock(Invocation.class);

     validator.validate(doesNothing, invocation);
     if (invocation.isVoid()) {
         verifyZeroInteractions(reporter);
     } else {
         verifyThrow(IllegalArgumentException.class).usingCallRealMethod().on(validator);
     }
 }

 @Test
 public void validateDoNothing_whenInvocationIsVoid_shouldNotThrowException() {
     DoesNothing doesNothing = mock(DoesNothing.class);
     Invocation invocation = Mockito.mock(Invocation.class);

     validator.validateDoNothing(doesNothing, invocation);
     verifyZeroInteractions(reporter);
 }

 @Test
 public void validateDoNothing_whenInvocationIsNotVoid_shouldThrowException() {
     DoesNothing doesNothing = mock(DoesNothing.class);
     Invocation invocation = Mockito.mock(Invocation.class);

     doAnswer(invocation1 -> { }).when(doesNothing).answer(ArgumentMatchers.any());

     assertThrows(IllegalArgumentException.class, () -> validator.validateDoNothing(doesNothing, invocation));
 }

 @Test
 public void validateReturnValue_whenInvocationIsVoid_shouldThrowException() {
     Returns returns = mock(Returns.class);
     Invocation invocation = Mockito.mock(Invocation.class);

     validator.validateReturnValue(returns, invocation);
     verifyThrow(IllegalArgumentException.class).usingCallRealMethod().on(validator);
 }

 @Test
 public void validateReturnValue_whenReturnValueIsNullAndInvocationReturnsPrimitive_shouldThrowException() {
     Returns returns = mock(Returns.class);
     Invocation invocation = Mockito.mock(Invocation.class);

     doAnswer(invocation1 -> 42).when(invocation).callRealMethod();

     assertThrows(IllegalArgumentException.class, () -> validator.validateReturnValue(returns, invocation));
 }

 @Test
 public void validateReturnValue_whenReturnValueIsNotNullAndInvocationInvalidReturnType_shouldThrowException() {
     Returns returns = mock(Returns.class);
     Invocation invocation = Mockito.mock(Invocation.class);
     Type returnType = Integer.TYPE;

     doAnswer(invocation1 -> "Hello World").when(returns).answer(ArgumentMatchers.any());

     assertThrows(IllegalArgumentException.class, () -> validator.validateReturnValue(returns, invocation)).withMessage("expected:<java.lang.Integer> but:<java.lang.String>");
 }

 @Test
 public void validateException_whenThrowableIsNull_shouldThrowException() {
     ThrowsException throwsException = mock(ThrowsException.class);
     Invocation invocation = Mockito.mock(Invocation.class);

     assertThrows(IllegalArgumentException.class, () -> validator.validateException(throwsException, invocation));
 }

 @Test
 public void validateException_whenThrowableIsRuntimeExceptionOrError_shouldNotThrowException() {
     ThrowsException throwsException = mock(ThrowsException.class);
     Invocation invocation = Mockito.mock(Invocation.class);
     RuntimeException runtimeException = new RuntimeException("Test Exception");

     doAnswer(invocation1 -> throwsException).when(throwsException).answer(ArgumentMatchers.any());

     validator.validateException(throwsException, invocation);
     verifyZeroInteractions(reporter);
 }

 @Test
 public void validateException_whenThrowableIsNotValidException_shouldThrowException() {
     ThrowsException throwsException = mock(ThrowsException.class);
     Invocation invocation = Mockito.mock(Invocation.class);
     Throwable throwable = new Exception("Test Exception");

     doAnswer(invocation1 -> throwsException).when(throwsException).answer(ArgumentMatchers.any());

     assertThrows(IllegalArgumentException.class, () -> validator.validateException(throwsException, invocation));
 }
}