package org.mockito.internal.stubbing.answers.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.internal.stubbing.answers.AnswersValidator;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Type;

import static org.mockito.internal.util.reflection.Whitebox.getInternalState;
import static org.mockito.internal.util.reflection.Whitebox.setInternalState;

public class AnswersValidatorTest {

 private AnswersValidator validator;
 private Reporter reporter;

 @Before
 public void setup() {
 validator = new AnswersValidator();
 reporter = getInternalState(validator, "reporter");
 }

 @Test
 public void testValidate_validReturnsAnswer() {
 Invocation invocation = new InvocationOnMock("mock", "methodName", new Object[0]);
 Returns returnsAnswer = new Returns("returnValue");
 validator.validate(returnsAnswer, invocation);
 // no exception should be thrown
 }

 @Test(expected = AssertionError.class)
 public void testValidate_invalidThrowsAnswer() {
 Invocation invocation = new InvocationOnMock("mock", "methodName", new Object[0]);
 ThrowsException throwsAnswer = new ThrowsException(new RuntimeException("testException"));
 validator.validate(throwsAnswer, invocation);
 // Reporter.onlyVoidMethodsCanBeSetToDoNothing exception should be thrown
 }

 @Test(expected = AssertionError.class)
 public void testValidate_invalidDoesNothingAnswer() {
 Invocation invocation = new InvocationOnMock("mock", "methodName", new Object[0], new Class[0]);
 DoesNothing doesNothingAnswer = new DoesNothing();
 validator.validate(doesNothingAnswer, invocation);
 // Reporter.onlyVoidMethodsCanBeSetToDoNothing exception should be thrown
 }

 @Test(expected = AssertionError.class)
 public void testValidate_invalidReturnValueAnswer() {
 Invocation invocation = new InvocationOnMock("mock", "methodName", new Object[0], int.class);
 Returns returnsAnswer = new Returns(new Object());
 validator.validate(returnsAnswer, invocation);
 // Reporter.cannotStubVoidMethodWithAReturnValue exception should be thrown
 }

 @Test(expected = AssertionError.class)
 public void testValidate_invalidReturnNullAndPrimitiveAnswer() {
 Invocation invocation = new InvocationOnMock("mock", "methodName", new Object[0], int.class);
 Returns returnsAnswer = new Returns(null);
 validator.validate(returnsAnswer, invocation);
 // Reporter.wrongTypeOfReturnValue exception should be thrown
 }

 @Test(expected = AssertionError.class)
 public void testValidate_invalidReturnTypeAnswer() {
 Invocation invocation = new InvocationOnMock("mock", "methodName", new Object[0], String.class);
 Returns returnsAnswer = new Returns(new Object());
 validator.validate(returnsAnswer, invocation);
 // Reporter.wrongTypeOfReturnValue exception should be thrown
 }

 @Test(expected = AssertionError.class)
 public void testValidate_invalidThrowableAnswer() {
 Invocation invocation = new InvocationOnMock("mock", "methodName", new Object[0]);
 ThrowsException throwsAnswer = new ThrowsException(new Object());
 validator.validate(throwsAnswer, invocation);
 // Reporter.cannotStubWithNullThrowable exception should be thrown
 }

 @Test(expected = AssertionError.class)
 public void testValidate_invalidRuntimeExceptionOrErrorAnswer() {
 Invocation invocation = new InvocationOnMock("mock", "methodName", new Object[0]);
 ThrowsException throwsAnswer = new ThrowsException(new RuntimeException("testException"));
 validator.validate(throwsAnswer, invocation);
 // no exception should be thrown
 }

 @Test(expected = AssertionError.class)
 public void testValidate_invalidExceptionAnswer() {
 Invocation invocation = new InvocationOnMock("mock", "methodName", new Object[0]);
 ThrowsException throwsAnswer = new ThrowsException(new Object()[0].getClass());
 validator.validate(throwsAnswer, invocation);
 // Reporter.checkedExceptionInvalid exception should be thrown
 }

 @Test
 public void testValidate_validDoNothingAnswer() {
 Invocation invocation = new InvocationOnMock("mock", "methodName", new Object[0]);
 DoesNothing doesNothingAnswer = new DoesNothing();
 validator.validate(doesNothingAnswer, invocation);
 // no exception should be thrown
 }
}