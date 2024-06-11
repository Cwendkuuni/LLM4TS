package org.mockito.internal.stubbing.answers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.ArgumentMatchers;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Answers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AnswersValidatorTest {

 private AnswersValidator validator;
 private Object mockObject;

 @Before
 public void setup() {
 mockObject = mock(Object.class);
 validator = new AnswersValidator();
 }

 @Test
 public void testValidateDoNothing_voidMethod() {
 when(mockObject).thenAnswer(Answers.callRealMethod());
 validator.validate(Answers.doNothing(), new InvocationOnMock(mockObject, null, null));
 verifyZeroInteractions(mockObject);
 }

 @Test
 public void testValidateDoNothing_nonVoidMethod() {
 when(mockObject.doSomething()).thenAnswer(Answers.doNothing());
 validator.validate(Answers.doNothing(), new InvocationOnMock(mockObject, null, null, "doSomething"));
 verifyNoMoreInteractions(mockObject);
 validator.reporter.onlyVoidMethodsCanBeSetToDoNothing();
 }

 @Test(expected = MockitoException.class)
 public void testValidateReturnValue_voidMethodWithReturnValue() {
 when(mockObject.doSomething()).thenAnswer(Answers.returnValues(1));
 AnswersValidator validator = new AnswersValidator();
 validator.validate(Answers.returnValues(1), new InvocationOnMock(mockObject, null, null, "doSomething"));
 validator.reporter.cannotStubVoidMethodWithAReturnValue();
 }

 @Test
 public void testValidateReturnValue_validReturnValue() {
 when(mockObject.doSomethingElse()).thenAnswer(Answers.returnValues(1, 2));
 validator.validate(Answers.returnValues(1, 2), new InvocationOnMock(mockObject, null, null, "doSomethingElse"));
 }

 @Test(expected = MockitoException.class)
 public void testValidateReturnValue_invalidReturnTypeNullAndPrimitive() {
 when(mockObject.giveNull()).thenAnswer(Answers.returning(null));
 validator.validate(Answers.returning(null), new InvocationOnMock(mockObject, null, "giveNull", Object.class));
 validator.reporter.wrongTypeOfReturnValue("null", "int", "giveNull");
 }

 @Test
 public void testValidateReturnValue_invalidReturnType() {
 when(mockObject.giveString()).thenAnswer(Answers.returning("mockito"));
 validator.validate(Answers.returning("mockito"), new InvocationOnMock(mockObject, null, "giveString", String[].class));
 validator.reporter.wrongTypeOfReturnValue("java.lang.String", "java.lang.String[]", "giveString");
 }

 @Test
 public void testValidateException_nullThrowable() {
 when(mockObject.throwException()).thenAnswer(Answers.throwing(new Exception()));
 validator.validate(Answers.throwing(new Exception()), new InvocationOnMock(mockObject, null, "throwException", null));
 validator.reporter.cannotStubWithNullThrowable();
 }

 @Test
 public void testValidateException_validThrowable() {
 when(mockObject.throwSomething()).thenAnswer(Answers.throwing(new IllegalArgumentException("mockito error")));
 validator.validate(Answers.throwing(new IllegalArgumentException("mockito error")), new InvocationOnMock(mockObject, null, "throwSomething", null));
 }

 @Test(expected = MockitoException.class)
 public void testValidateException_invalidExceptionType() {
 when(mockObject.throwSomething()).thenAnswer(Answers.throwing(new RuntimeException("mockito error")));
 AnswersValidator validator = new AnswersValidator();
 validator.validate(Answers.throwing(new RuntimeException("mockito error")), new InvocationOnMock(mockObject, null, "throwSomething", Number[].class));
 validator.reporter.checkedExceptionInvalid(new IllegalArgumentException("mockito error"));
 }
}