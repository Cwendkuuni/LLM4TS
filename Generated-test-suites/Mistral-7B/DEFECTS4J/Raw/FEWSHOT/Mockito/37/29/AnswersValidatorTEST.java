package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.exceptions.Reporter.cannotStubWithNullThrowable;
import static org.mockito.exceptions.Reporter.checkedExceptionInvalid;
import static org.mockito.exceptions.Reporter.cannotStubVoidMethodWithAReturnValue;
import static org.mockito.exceptions.Reporter.onlyVoidMethodsCanBeSetToDoNothing;
import static org.mockito.exceptions.Reporter.wrongTypeOfReturnValue;
import static org.mockito.internal.invocation.InvocationUtil.isVoid;
import static org.mockito.internal.invocation.InvocationUtil.isValidException;
import static org.mockito.internal.invocation.InvocationUtil.isValidReturnType;
import static org.mockito.internal.invocation.InvocationUtil.printMethodReturnType;
import static org.mockito.internal.invocation.InvocationUtil.printReturnType;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

public class AnswersValidatorTest {

 private AnswersValidator validator = new AnswersValidator();
 private Reporter reporter = new Reporter();

 @Test
 public void testValidate_nullAnswer_doesNotThrowException() {
     Invocation invocation = createInvocation("void testMethod()");
     validator.validate(null, invocation);
 }

 @Test
 public void testValidate_nullInvocation_doesNotThrowException() {
     Answer<String> answer = createReturnsAnswer("hello");
     validator.validate(answer, null);
 }

 @Test(expected = AssertionError.class)
 public void testValidate_answerIsThrowsExceptionButInvocationIsVoid_throwsError() {
     Invocation invocation = createInvocation("void testMethod()");
     ThrowsException answer = createThrowsException(new RuntimeException("Test Error"));
     validator.validate(answer, invocation);
 }

 @Test
 public void testValidate_answerIsReturnsNullButInvocationIsNotVoid_doesNotThrowError() {
     Invocation invocation = createInvocation("int testMethod()");
     DoesNothing answer = createDoesNothing();
     validator.validate(answer, invocation);
 }

 @Test
 public void testValidate_answerIsReturnsNullButInvocationIsVoid_throwsError() {
     Invocation invocation = createInvocation("void testMethod()");
     DoesNothing answer = createDoesNothing();
     validator = new AnswersValidator(reporter);
     validator.validate(answer, invocation);
     assertFalse(reporter.hasThrownError());
     reporter.reset();
     validator.validate(answer, invocation);
     assertTrue(reporter.hasThrownError());
     assertSame(onlyVoidMethodsCanBeSetToDoNothing(), reporter.getLastThrowable());
 }

 @Test
 public void testValidate_answerIsReturnsNull_validReturnType() {
     Invocation invocation = createInvocation("String testMethod()");
     DoesNothing answer = createDoesNothing();
     validator.validate(answer, invocation);
 }

 @Test
 public void testValidate_answerIsReturnsNull_invalidReturnType() {
     Invocation invocation = createInvocation("int testMethod()");
     DoesNothing answer = createDoesNothing();
     validator.validate(answer, invocation);
     assertFalse(reporter.hasThrownError());
     reporter.reset();
     validator.validate(answer, invocation);
     assertTrue(reporter.hasThrownError());
     assertSame(wrongTypeOfReturnValue(printMethodReturnType(invocation), "", invocation.getMethodName()), reporter.getLastThrowable());
 }

 @Test
 public void testValidate_answerIsReturnsNonNullButInvalidReturnType_throwsError() {
     Invocation invocation = createInvocation("int testMethod()");
     Returns<Integer> answer = createReturnsAnswer(42);
     validator = new AnswersValidator(reporter);
     validator.validate(answer, invocation);
     assertFalse(reporter.hasThrownError());
     reporter.reset();
     validator.validate(answer, invocation);
     assertTrue(reporter.hasThrownError());
     assertSame(wrongTypeOfReturnValue(printMethodReturnType(invocation), printReturnType(answer.getReturnType()), invocation.getMethodName()), reporter.getLastThrowable());
 }

 @Test
 public void testValidate_answerIsReturnsNonnullPrimtiveTypeButInvocationReturnsObject_doesNotThrowError() {
     Invocation invocation = createInvocation("Object testMethod()");
     Returns<String> answer = createReturnsAnswer("hello");
     validator.validate(answer, invocation);
 }

 @Test
 public void testValidate_answerIsReturnsNonnullPrimitiveTypeButInvocationReturnsPrimitiveType_doesNotThrowError() {
     Invocation invocation = createInvocation("int testMethod()");
     Returns<Integer> answer = createReturnsAnswer(42);
     validator.validate(answer, invocation);
 }

 @Test
 public void testValidate_answerIsReturnsNonnullObjectButInvocationReturnsPrimitiveType_throwsError() {
     Invocation invocation = createInvocation("int testMethod()");
     Returns<String> answer = createReturnsAnswer("hello");
     validator = new AnswersValidator(reporter);
     validator.validate(answer, invocation);
     assertFalse(reporter.hasThrownError());
     reporter.reset();
     validator.validate(answer, invocation);
     assertTrue(reporter.hasThrownError());
     assertSame(cannotStubVoidMethodWithAReturnValue(), reporter.getLastThrowable());
 }

 @Test
 public void testValidate_answerIsReturnsNonnullObjectButInvocationReturnsObject_doesNotThrowError() {
     Invocation invocation = createInvocation("Object testMethod()");
     Returns<String> answer = createReturnsAnswer("hello");
     validator.validate(answer, invocation);
 }

 @Test
 public void testValidate_answerIsThrowsExceptionButInvalidThrowableType_throwsError() {
     Invocation invocation = createInvocation("int testMethod()");
     ThrowsException answer = createThrowsException(new String("Test Error"));
     validator = new AnswersValidator(reporter);
     validator.validate(answer, invocation);
     assertFalse(reporter.hasThrownError());
     reporter.reset();
     validator.validate(answer, invocation);
     assertTrue(reporter.hasThrownError());
     assertSame(checkedExceptionInvalid(answer.getThrowable()), reporter.getLastThrowable());
 }

 @Test
 public void testValidate_answerIsThrowsExceptionButValidThrowableType_doesNotThrowError() {
     Invocation invocation = createInvocation("int testMethod() throws Exception");
     ThrowsException answer = createThrowsException(new RuntimeException("Test Error"));
     validator.validate(answer, invocation);
 }

 @Test
 public void testValidate_answerIsThrowsExceptionButInvocationDoesNotExpectException_throwsError() {
     Invocation invocation = createInvocation("int testMethod()");
     ThrowsException answer = createThrowsException(new RuntimeException("Test Error"));
     validator = new AnswersValidator(reporter);
     validator.validate(answer, invocation);
     assertFalse(reporter.hasThrownError());
     reporter.reset();
     validator.validate(answer, invocation);
     assertTrue(reporter.hasThrownError());
     assertSame(checkedExceptionInvalid(answer.getThrowable()), reporter.getLastThrowable());
 }

 @Test
 public void testValidate_answerIsThrowsNull_throwsError() {
     Invocation invocation = createInvocation("int testMethod()");
     ThrowsException answer = createThrowsException(null);
     validator = new AnswersValidator(reporter);
     validator.validate(answer, invocation);
     assertFalse(reporter.hasThrownError());
     reporter.reset();
     validator.validate(answer, invocation);
     assertTrue(reporter.hasThrownError());
     assertSame(cannotStubWithNullThrowable(), reporter.getLastThrowable());
 }

 private Invocation createInvocation(String methodName) {
     return new Invocation("Mockito", methodName, new Object[0]);
 }

 private Returns<String> createReturnsAnswer(String value) {
     return new Returns<String>(value);
 }

 private ThrowsException createThrowsException(Throwable throwable) {
     return new ThrowsException(throwable);
 }

 private DoesNothing createDoesNothing() {
     return new DoesNothing();
 }

}