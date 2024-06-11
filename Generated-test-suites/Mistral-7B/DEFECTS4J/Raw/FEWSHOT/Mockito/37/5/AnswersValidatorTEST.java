package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.exceptions.Reporter.cannotStubWithNullThrowable;
import static org.mockito.exceptions.Reporter.onlyVoidMethodsCanBeSetToDoNothing;
import static org.mockito.exceptions.Reporter.wrongTypeOfReturnValue;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

public class AnswersValidatorTest {

 Reporter reporter = new Reporter();
 AnswersValidator validator = new AnswersValidator();

 @Test
 public void testValidateDoNothing_voidMethod() {
 Invocation invocation = new Invocation(Object.class, "doNothing", new Class[0]);
 DoesNothing answer = new DoesNothing();
 validator.validate(answer, invocation);
 assertTrue(true);
 }

 @Test
 public void testValidateDoNothing_nonVoidMethod() {
 Invocation invocation = new Invocation(Object.class, "doSomething", new Class[0]);
 DoesNothing answer = new DoesNothing();
 validator.validate(answer, invocation);
 assertFalse(invocation.isVoid());
 reporter.onlyVoidMethodsCanBeSetToDoNothing();
 assertNull(validator.getLastReporterMessage());
 }

 @Test
 public void testValidateReturnValue_validReturnValue() {
 Invocation invocation = new Invocation(Object.class, "returnValue", new Class[0]);
 Returns answer = new Returns("foo");
 assertNull(validator.getLastReporterMessage());
 validator.validateReturnValue(answer, invocation);
 assertTrue(true);
 }

 @Test
 public void testValidateReturnValue_voidMethod() {
 Invocation invocation = new Invocation(Object.class, "doNothing", new Class[0]);
 Returns answer = new Returns("foo");
 validator.validateReturnValue(answer, invocation);
 assertFalse(invocation.isVoid());
 reporter.cannotStubVoidMethodWithAReturnValue();
 assertNull(validator.getLastReporterMessage());
 }

 @Test
 public void testValidateReturnValue_nullAndPrimitive() {
 Invocation invocation = new Invocation(int.class, "returnNull", new Class[0]);
 Returns answer = new Returns(null);
 validator.validateReturnValue(answer, invocation);
 assertFalse(invocation.isValidReturnType(answer.getReturnType()));
 reporter.wrongTypeOfReturnValue(invocation.printMethodReturnType(), "null", invocation.getMethodName());
 assertNull(validator.getLastReporterMessage());
 }

 @Test
 public void testValidateReturnValue_validType() {
 Invocation invocation = new Invocation(Object.class, "returnValue", new Class[0]);
 Returns answer = new Returns("foo");
 validator.validateReturnValue(answer, invocation);
 assertTrue(true);
 }

 @Test
 public void testValidateReturnValue_invalidType() {
 Invocation invocation = new Invocation(int.class, "returnValue", new Class[0]);
 Returns answer = new Returns(new Object());
 validator.validateReturnValue(answer, invocation);
 assertFalse(invocation.isValidReturnType(answer.getReturnType()));
 reporter.wrongTypeOfReturnValue(invocation.printMethodReturnType(), answer.printReturnType(), invocation.getMethodName());
 assertNull(validator.getLastReporterMessage());
 }

 @Test
 public void testValidateException_validException() {
 Invocation invocation = new Invocation(Object.class, "throwException", new Class[0]);
 ThrowsException answer = new ThrowsException(new RuntimeException("foo"));
 validator.validateException(answer, invocation);
 assertNull(validator.getLastReporterMessage());
 }

 @Test
 public void testValidateException_invalidException() {
 Invocation invocation = new Invocation(Object.class, "throwException", new Class[0]);
 ThrowsException answer = new ThrowsException(new Exception("foo"));
 validator.validateException(answer, invocation);
 assertFalse(invocation.isValidException(answer.getThrowable()));
 reporter.checkedExceptionInvalid(answer.getThrowable());
 assertNull(validator.getLastReporterMessage());
 }

 @Test
 public void testValidateException_nullThrowable() {
 ThrowsException answer = new ThrowsException(null);
 validator.validateException(answer, null);
 assertFalse(answer.getThrowable() == null);
 reporter.cannotStubWithNullThrowable();
 assertNull(validator.getLastReporterMessage());
 }

 @Test
 public void testValidateException_runtimeExceptionOrError() {
 Invocation invocation = new Invocation(Object.class, "throwException", new Class[0]);
 ThrowsException answer = new ThrowsException(new RuntimeException("foo"));
 validator.validateException(answer, invocation);
 assertTrue(true);
 }

 @Test
 public void testValidateException_error() {
 Invocation invocation = new Invocation(Object.class, "throwException", new Class[0]);
 ThrowsException answer = new ThrowsException(new Error("foo"));
 validator.validateException(answer, invocation);
 assertTrue(true);
 }

 @Test
 public void testGetLastReporterMessage_noMessage() {
 assertNull(validator.getLastReporterMessage());
 }

 @Test
 public void testGetLastReporterMessage_messageSet() {
 validator.reporter.error("Error message");
 String message = validator.getLastReporterMessage();
 assertEquals("Error message", message);
 }
}