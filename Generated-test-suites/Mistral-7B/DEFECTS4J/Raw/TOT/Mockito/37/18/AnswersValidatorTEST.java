import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class AnswersValidatorTest {

 private AnswersValidator validator;
 private Object returnValue;
 private Invocation invocation;
 private Answer<?> answer;

 @Before
 public void setUp() {
 validator = new AnswersValidator();
 returnValue = new Object();
 invocation = new Invocation(null, "testMethod", new Object[0], null);
 answer = Mockito.mock(Answer.class);
 }

 @Test
 public void testValidateDoNothingVoidMethod() {
 // given
 Mockito.doNothing().when(answer).answer(ArgumentMatchers.any());

 // when
 validator.validate(answer, invocation);

 // then
 verifyNoMoreInteractions(answer);
 }

 @Test
 public void testValidateDoNothingNonVoidMethod() {
 // given
 invocation.setReturnType(String.class);
 Mockito.doNothing().when(answer).answer(ArgumentMatchers.any());

 // when
 validator.validate(answer, invocation);

 // then
 verify(answer).answer(ArgumentMatchers.any());
 validator.reporter.onlyVoidMethodsCanBeSetToDoNothing();
 }

 @Test
 public void testValidateReturnValueVoidMethod() {
 // given
 Mockito.when(answer.answer(ArgumentMatchers.any())).thenReturn(returnValue);

 // when
 validator.validate(answer, invocation);

 // then
 verify(answer).answer(ArgumentMatchers.any());
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnValueNonVoidMethodWithWrongType() {
 // given
 invocation.setReturnType(int.class);
 Mockito.when(answer.answer(ArgumentMatchers.any())).thenReturn(returnValue);

 // when
 validator.validate(answer, invocation);

 // then
 verify(answer).answer(ArgumentMatchers.any());
 validator.reporter.wrongTypeOfReturnValue(invocation.printMethodReturnType(), answer.getReturnType().getTypeName(), invocation.getMethodName());
 }

 @Test
 public void testValidateReturnValueNonNullableAndCorrectType() {
 // given
 Mockito.when(answer.answer(ArgumentMatchers.any())).thenReturn(returnValue);

 // when
 validator.validate(answer, invocation);

 // then
 verify(answer).answer(ArgumentMatchers.any());
 }

 @Test(expected = AssertionError.class)
 public void testValidateExceptionNullThrowable() {
 // given
 Mockito.when(answer.answer(ArgumentMatchers.any())).thenThrow(null);

 // when
 validator.validate(answer, invocation);

 // then
 validator.reporter.cannotStubWithNullThrowable();
 }

 @Test
 public void testValidateExceptionNonRuntimeException() {
 // given
 Mockito.when(answer.answer(ArgumentMatchers.any())).thenThrow(new IllegalStateException("test"));

 // when
 validator.validate(answer, invocation);

 // then
 verify(answer).answer(ArgumentMatchers.any());
 }

 @Test(expected = AssertionError.class)
 public void testValidateExceptionInvalidExceptionType() {
 // given
 Mockito.when(answer.answer(ArgumentMatchers.any())).thenThrow(new IOException("test"));

 // when
 validator.validate(answer, invocation);

 // then
 validator.reporter.checkedExceptionInvalid(IOException.class);
 }
}