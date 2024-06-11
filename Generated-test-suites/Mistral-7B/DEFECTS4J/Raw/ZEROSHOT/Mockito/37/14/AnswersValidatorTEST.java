package org.mockito.internal.stubbing.answers;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.runners.DefaultMockitoRule;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class AnswersValidatorTest {

 private AnswersValidator validator;
 private Reporter reporter;

 @Rule
 public DefaultMockitoRule mockitoRule = new DefaultMockitoRule(this);

 @Before
 public void setUp() {
 validator = new AnswersValidator();
 reporter = mock(Reporter.class);
 }

 @Test
 public void validate_nullAnswer_shouldThrowException() {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    Answer<?> answer = null;

    assertThrows("Answer cannot be null", () -> validator.validate(answer, invocation, reporter));
  }

 @Test
 public void validate_nullInvocation_shouldThrowException() {
    Answer<?> answer = mock(Answer.class);

    assertThrows("Invocation cannot be null", () -> validator.validate(answer, null, reporter));
  }

 @Test
 public void validate_whenAnswerIsThrowsException_shouldCallValidateException() {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    Throwable throwable = new RuntimeException("Test Exception");
    ThrowsException answer = mock(ThrowsException.class);
    when(answer.getThrowable()).thenReturn(throwable);

    validator.validate(answer, invocation, reporter);

    verifyNoMoreInteractions(reporter);
  }

 @Test
 public void validate_whenAnswerIsThrowsException_andExceptionIsRuntimeOrError_shouldDoNothing() {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    Throwable throwable = new RuntimeException("Test Exception");
    ThrowsException answer = mock(ThrowsException.class);
    when(answer.getThrowable()).thenReturn(throwable);

    validator.validate(answer, invocation, reporter);

    verifyZeroInteractions(reporter);
  }

 @Test
 public void validate_whenAnswerIsThrowsException_andExceptionIsNotRuntimeOrError_shouldCallReporter() {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    Class<?> returnType = int.class;
    Throwable throwable = new IllegalStateException("Test Exception");
    ThrowsException answer = mock(ThrowsException.class);
    when(answer.getThrowable()).thenReturn(throwable);

    validator.validate(answer, invocation, reporter);

    verify(reporter).checkedExceptionInvalid(throwable);
  }

 @Test
 public void validate_whenAnswerIsReturns_andInvocationIsVoid_shouldCallReporter() {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    Class<?> returnType = String.class;
    Returns answer = mock(Returns.class);

    validator.validate(answer, invocation, reporter);

    verify(reporter).cannotStubVoidMethodWithAReturnValue();
  }

 @Test
 public void validate_whenAnswerIsReturns_andInvocationIsNotVoid_andReturnValueIsNull_andMethodReturnTypeIsPrimitive_shouldCallReporter() {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    Class<?> methodReturnType = byte.class;
    Returns answer = mock(Returns.class);
    when(answer.getReturnValue()).thenReturn(null);

    validator.validate(answer, invocation, reporter);

    verify(reporter).wrongTypeOfReturnValue(methodReturnType.getName(), "null", invocation.getMethod().getName());
  }

 @Test
 public void validate_whenAnswerIsReturns_andInvocationIsNotVoid_andReturnValueIsNull_andMethodReturnTypeIsNotPrimitive_shouldCallReporter() {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    Class<?> methodReturnType = String.class;
    Returns answer = mock(Returns.class);
    when(answer.getReturnValue()).thenReturn(null);

    validator.validate(answer, invocation, reporter);

    verify(reporter).wrongTypeOfReturnValue(methodReturnType.getName(), "null", invocation.getMethod().getName());
  }

 @Test
 public void validate_whenAnswerIsReturns_andInvocationIsNotVoid_andReturnValueIsNotNull_andMethodReturnTypeIsNotSameAsReturnValue_shouldCallReporter() {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    Class<?> methodReturnType = String.class;
    String expectedReturnValue = "Test Value";
    Class<?> returnType = Integer.class;
    Returns answer = mock(Returns.class);
    when(answer.getReturnValue()).thenReturn(expectedReturnValue);

    validator.validate(answer, invocation, reporter);

    verify(reporter).wrongTypeOfReturnValue(methodReturnType.getName(), expectedReturnValue.getClass().getName(), invocation.getMethod().getName());
  }

 @Test
 public void validate_whenAnswerIsDoesNothing_andMethodIsVoid_shouldCallReporter() {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    DoesNothing answer = mock(DoesNothing.class);

    validator.validate(answer, invocation, reporter);

    verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
  }

 @Test
 public void validate_whenAnswerIsDoesNothing_andMethodIsNotVoid_shouldDoNothing() {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    DoesNothing answer = mock(DoesNothing.class);

    validator.validate(answer, invocation, reporter);

    verifyZeroInteractions(reporter);
  }
}