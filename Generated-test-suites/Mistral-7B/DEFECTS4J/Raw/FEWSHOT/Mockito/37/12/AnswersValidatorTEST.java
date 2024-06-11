package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import static org.mockito.internal.stubbing.answers.AnswersValidator.reporter;

public class AnswersValidatorTest {

 Reporter reporter = new Reporter();

 @Test
 public void testValidate_nullAnswer() {
 Answer<?> answer = null;
 Invocation invocation = new Invocation(null, null, null, null, null);
 AnswersValidator validator = new AnswersValidator();

 validator.validate(answer, invocation);

 assertEquals("Answer cannot be null", reporter.getLastReportedMessage());
 }

 @Test
 public void testValidate_nullInvocation() {
 Answer<?> answer = new ThrowsException(new RuntimeException("Exception message"));
 Invocation invocation = null;
 AnswersValidator validator = new AnswersValidator();

 validator.validate(answer, invocation);

 assertEquals("Invocation cannot be null", reporter.getLastReportedMessage());
 }

 @Test
 public void testValidate_noAnswer() {
 Invocation invocation = new Invocation(null, null, null, null, null);
 AnswersValidator validator = new AnswersValidator();

 validator.validate(null, invocation);

 assertNull(reporter.getLastReportedMessage());
 }

 @Test
 public void testValidate_throwsException() {
 Answer<RuntimeException> answer = new ThrowsException(new RuntimeException("Exception message"));
 Invocation invocation = new Invocation(null, null, null, null, null);
 AnswersValidator validator = new AnswersValidator();

 validator.validate(answer, invocation);

 assertNull(reporter.getLastReportedMessage());
 }

 @Test
 public void testValidate_returns() {
 Answer<String> answer = new Returns<>("test");
 Invocation invocation = new Invocation(null, null, String.class, null, null);
 AnswersValidator validator = new AnswersValidator();

 validator.validate(answer, invocation);

 assertNull(reporter.getLastReportedMessage());
 }

 @Test
 public void testValidate_voidMethod_doNothing() {
 Answer<?> answer = new DoesNothing();
 Invocation invocation = new Invocation(null, null, null, null, null);
 AnswersValidator validator = new AnswersValidator();

 validator.validate(answer, invocation);

 assertNull(reporter.getLastReportedMessage());
 }

 @Test
 public void testValidate_voidMethod_notDoNothing() {
 Answer<?> answer = new Returns("test");
 Invocation invocation = new Invocation(null, null, null, null, null);
 AnswersValidator validator = new AnswersValidator();

 validator.validate(answer, invocation);

 assertEquals("Only void methods can be set to 'do nothing'", reporter.getLastReportedMessage());
 }

 @Test
 public void testValidate_nonVoidMethod_doNothing() {
 Answer<?> answer = new DoesNothing();
 Invocation invocation = new Invocation(null, null, String.class, null, null);
 AnswersValidator validator = new AnswersValidator();

 validator.validate(answer, invocation);

 assertEquals("Only void methods can be set to 'do nothing'", reporter.getLastReportedMessage());
 }

 @Test
 public void testValidate_nonVoidMethod_returnNull() {
 Answer<String> answer = new Returns(null);
 Invocation invocation = new Invocation(null, null, String.class, null, null);
 AnswersValidator validator = new AnswersValidator();

 validator.validate(answer, invocation);

 assertEquals("Cannot stub void method with a return value", reporter.getLastReportedMessage());
 }

 @Test
 public void testValidate_nonVoidMethod_nonNullReturn() {
 Answer<String> answer = new Returns("test");
 Invocation invocation = new Invocation(null, null, String.class, null, null);
 AnswersValidator validator = new AnswersValidator();

 validator.validate(answer, invocation);

 if (invocation.getReturnType().isPrimitive()) {
 assertEquals("Wrong type of return value for invocation " + invocation.getMethodName() + ". Expected: " + invocation.printMethodReturnType() + ", but was: " + answer.getReturnType().getName(), reporter.getLastReportedMessage());
 } else {
 assertNull(reporter.getLastReportedMessage());
 }
 }

 @Test
 public void testValidate_nonVoidMethod_returnWrongType() {
 Answer<Integer> answer = new Returns("test");
 Invocation invocation = new Invocation(null, null, String.class, null, null);
 AnswersValidator validator = new AnswersValidator();

 validator.validate(answer, invocation);

 assertEquals("Wrong type of return value for invocation " + invocation.getMethodName() + ". Expected: " + invocation.printMethodReturnType() + ", but was: " + answer.getReturnType().getName(), reporter.getLastReportedMessage());
 }

 @Test
 public void testValidate_voidMethod_throwsException() {
 Answer<?> answer = new ThrowsException(new RuntimeException("Exception message"));
 Invocation invocation = new Invocation(null, null, null, null, null);
 AnswersValidator validator = new AnswersValidator();

 validator.validate(answer, invocation);

 assertNull(reporter.getLastReportedMessage());
 }

 @Test
 public void testValidate_nonVoidMethod_throwsException() {
 Answer<String> answer = new ThrowsException(new RuntimeException("Exception message"));
 Invocation invocation = new Invocation(null, null, String.class, null, null);
 AnswersValidator validator = new AnswersValidator();

 validator.validate(answer, invocation);

 assertEquals("Checked exception " + answer.getThrowable().getClass().getName() + " does not match expected exception", reporter.getLastReportedMessage());
 }

 @Test
 public void testValidate_nonVoidMethod_throwsNullException() {
 Answer<String> answer = new ThrowsException(null);
 Invocation invocation = new Invocation(null, null, String.class, null, null);
 AnswersValidator validator = new AnswersValidator();

 validator.validate(answer, invocation);

 assertEquals("Cannot stub with null throwable", reporter.getLastReportedMessage());
 }

 @Test
 public void testValidate_nonVoidMethod_throwsRuntimeException() {
 Answer<String> answer = new ThrowsException(new RuntimeException("Exception message"));
 Invocation invocation = new Invocation(null, null, String.class, null, null);
 AnswersValidator validator = new AnswersValidator();

 validator.validate(answer, invocation);

 assertNull(reporter.getLastReportedMessage());
 }

 @Test
 public void testValidate_nonVoidMethod_throwsError() {
 Answer<String> answer = new ThrowsException(new Error("Exception message"));
 Invocation invocation = new Invocation(null, null, String.class, null, null);
 AnswersValidator validator = new AnswersValidator();

 validator.validate(answer, invocation);

 assertNull(reporter.getLastReportedMessage());
 }

}