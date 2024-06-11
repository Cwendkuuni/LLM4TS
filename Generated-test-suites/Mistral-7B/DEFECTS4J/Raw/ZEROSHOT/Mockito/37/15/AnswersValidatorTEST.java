package org.mockito.internal.stubbing.answers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.internal.util.reflection.Whitebox.setInternalState;

public class AnswersValidatorTest {

 private AnswersValidator validator;
 private Reporter reporter;

 @Before
 public void setup() {
 reporter = mock(Reporter.class);
 validator = new AnswersValidator();
 setInternalState(validator, "reporter", reporter);
 }

 @Test(expected = AssertionError.class)
 public void testValidateDoNothing_voidMethodCalled_exceptionThrown() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 DoesNothing answer = mock(DoesNothing.class);
 validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnValue_voidMethodCalled_answerReturnsValue() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
invocation.setReturnValue(1);
 Returns answer = mock(Returns.class);
 validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnValue_nonVoidMethodCalled_answerReturnsNullAndWrongType() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 invocation.setReturnValue(1);
 Returns answer = mock(Returns.class);
 setInternalState(answer, "returnValue", (Object) null);
 validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateException_nullThrowable() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 ThrowsException answer = mock(ThrowsException.class);
 validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateException_throwableNotInstanceOfRuntimeExceptionOrError() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 ThrowsException answer = mock(ThrowsException.class);
 Throwable throwable = new IllegalArgumentException("Test error");
 setInternalState(answer, "throwable", throwable);
 validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateException_throwableNotValidForInvocation() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 Invocation invocationSpy = spy(invocation);
 ThrowsException answer = mock(ThrowsException.class);
 Throwable throwable = new RuntimeException("Test error");
 setInternalState(answer, "throwable", throwable);
 validator.validate(answer, invocationSpy);
 }

 @Test
 public void testValidateDoNothing_voidMethodCalled_answerIsDoNothing() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 DoesNothing answer = mock(DoesNothing.class);
 validator.validate(answer, invocation);
 }

 @Test
 public void testValidateReturnValue_nonVoidMethodCalled_answerReturnsValidValue() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 Invocation invocationSpy = spy(invocation);
 Returns answer = mock(Returns.class);
 setInternalState(answer, "returnValue", 1);
 validator.validate(answer, invocationSpy);
 }

 @Test
 public void testValidateReturnValue_nonVoidMethodCalled_answerReturnsNull() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 Invocation invocationSpy = spy(invocation);
 Returns answer = mock(Returns.class);
 setInternalState(answer, "returnValue", (Object) null);
 validator.validate(answer, invocationSpy);
 }

 @Test
 public void testValidateException_throwableIsValid() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 Invocation invocationSpy = spy(invocation);
 ThrowsException answer = mock(ThrowsException.class);
 Throwable throwable = new IOException("Test error");
 setInternalState(answer, "throwable", throwable);
 validator.validate(answer, invocationSpy);
 }
}