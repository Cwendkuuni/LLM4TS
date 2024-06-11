package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoFailureReason;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.exceptions.Reporter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class AnswersValidatorTest {

 private Reporter reporter = mock(Reporter.class);
 private AnswersValidator validator = spy(new AnswersValidator());

 @Test(expected = AssertionError.class)
 public void testValidateThrowsExceptionNullAnswer() {
     Answer<?> answer = null;
     InvocationOnMock invocation = mock(InvocationOnMock.class);

     validator.validate(answer, invocation);
     verify(reporter).cannotStubWithNullAnswer();
 }

 @Test
 public void testValidateThrowsExceptionValidAnswer() {
     Throwable throwable = new IllegalStateException("Something went wrong");
     Answer<IllegalStateException> answer = new ThrowsException(throwable);
     InvocationOnMock invocation = mock(InvocationOnMock.class);

     validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateThrowsExceptionInvalidThrowable() {
     Answer<String> answer = new ThrowsException(new RuntimeException("Something went wrong"));
     InvocationOnMock invocation = mock(InvocationOnMock.class);

     validator.validate(answer, invocation);
     verify(reporter).checkedExceptionInvalid(isA(RuntimeException.class));
 }

 @Test
 public void testValidateThrowsExceptionValidThrowable() {
     Throwable throwable = new IllegalStateException("Something went wrong");
     Answer<String> answer = new ThrowsException(throwable);
     InvocationOnMock invocation = mock(InvocationOnMock.class);

     validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnsNullAndVoidInvocation() {
     Answer<String> answer = new Returns<String>(null);
     InvocationOnMock invocation = mock(InvocationOnMock.class).withNoArguments();

     validator.validate(answer, invocation);
     verify(reporter).cannotStubVoidMethodWithAReturnValue();
 }

 @Test
 public void testValidateReturnsNullAndNonVoidInvocation() {
     Answer<String> answer = new Returns<String>(null);
     InvocationOnMock invocation = mock(InvocationOnMock.class).withNoArguments();
     when(invocation.getMethod()).thenReturn(mock(Void.TYPE.getClass()));

     validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnsNonnullAndInvalidReturnType() {
     Answer<String> answer = new Returns<Integer>("Mockito");
     InvocationOnMock invocation = mock(InvocationOnMock.class).withNoArguments();

     validator.validate(answer, invocation);
     verify(reporter).wrongTypeOfReturnValue(isA(String.class.getTypeName()), isA(Integer.class.getTypeName()),
         eq("AnswerValidator.validate(Answer, Invocation)"));
 }

 @Test
 public void testValidateReturnsNonnullAndValidReturnType() {
     Answer<String> answer = new Returns<String>("Mockito");
     InvocationOnMock invocation = mock(InvocationOnMock.class).withNoArguments();

     validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnsNonnullAndInvalidReturnTypeForPrimitiveType() {
     Answer<Integer> answer = new Returns<Integer>(null);
     InvocationOnMock invocation = mock(InvocationOnMock.class).withNoArguments();

     validator.validate(answer, invocation);
     verify(reporter).wrongTypeOfReturnValue(isA(Integer.TYPE.getTypeName()), "null",
         eq("AnswerValidator.validate(Answer, Invocation)"));
 }

 @Test
 public void testValidateDoesNothingAndNonVoidInvocation() {
     Answer<String> answer = new DoesNothing();
     InvocationOnMock invocation = mock(InvocationOnMock.class).withNoArguments();

     validator.validate(answer, invocation);
     verifyNoMoreInteractions(invocation);
 }

 @Test
 public void testValidateDoesNothingAndVoidInvocation() {
     Answer<String> answer = new DoesNothing();
     InvocationOnMock invocation = mock(InvocationOnMock.class).withNoArguments();
     when(invocation.isVoid()).thenReturn(true);

     validator.validate(answer, invocation);
     verifyZeroInteractions(invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidateDoesNothingAndNonVoidInvocationWithReturnType() {
     Answer<String> answer = new DoesNothing();
     InvocationOnMock invocation = mock(InvocationOnMock.class).withNoArguments();
     when(invocation.getMethod()).thenReturn(mock(String.class.getClass()));

     validator.validate(answer, invocation);
     verify(reporter).onlyVoidMethodsCanBeSetToDoNothing();
 }
}