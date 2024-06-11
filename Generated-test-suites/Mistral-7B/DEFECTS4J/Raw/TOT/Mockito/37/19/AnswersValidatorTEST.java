package org.mockito.internal.stubbing.answers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.AnswerWithProvider;
import org.mockito.stubbing.AnswerWrapper;
import org.mockito.stubbing.DoNothing;
import org.mockito.stubbing.ReturnValues;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

public class AnswersValidatorTest {

 private AnswersValidator validator;

 @Before
 public void setUp() {
 validator = new AnswersValidator();
 }

 @Test
 public void testValidateDoNothing_voidMethod() {
 doNothing().when(mock(Object.class)).doNothing();
 Invocation invocation = new InvocationOnMock(mock(Object.class), "doNothing", new Object[0]);
 validator.validate(new DoesNothing(), invocation);
 }

 @Test
 public void testValidateDoNothing_nonVoidMethod() {
 when(mock(Object.class).method()).then(new DoesNothing());
 Invocation invocation = new InvocationOnMock(mock(Object.class), "method", new Object[0]);
 assertThrows(MockitoException.class, () -> validator.validate(new DoesNothing(), invocation));
 }

 @Test
 public void testValidateReturnValue_voidMethod() {
 when(mock(Object.class).voidMethod()).then(new Returns(null));
 Invocation invocation = new InvocationOnMock(mock(Object.class), "voidMethod", new Object[0]);
 validator.validate(new Returns(null), invocation);
 }

 @Test
 public void testValidateReturnValue_nonVoidMethod_valid() {
 when(mock(Object.class).method()).then(new Returns("test"));
 Invocation invocation = new InvocationOnMock(mock(Object.class), "method", new Object[0]);
 validator.validate(new Returns("test"), invocation);
 }

 @Test
 public void testValidateReturnValue_nonVoidMethod_invalid() {
 when(mock(Object.class).method()).then(new Returns(123));
 Invocation invocation = new InvocationOnMock(mock(Object.class), "method", new Object[0]);
 validator.validate(new Returns(new Object()), invocation); // invalid return type
 validator.validate(new Returns(123.0), invocation); // valid, but different type
 }

 @Test
 public void testValidateException_nullThrowable() {
 when(mock(Object.class).method()).then(new ThrowsException(null));
 Invocation invocation = new InvocationOnMock(mock(Object.class), "method", new Object[0]);
 assertThrows(MockitoException.class, () -> validator.validate(new ThrowsException(null), invocation));
 }

 @Test
 public void testValidateException_valid() {
 when(mock(Object.class).method()).thenThrow(new RuntimeException("test"));
 Invocation invocation = new InvocationOnMock(mock(Object.class), "method", new Object[0]);
 validator.validate(new ThrowsException(new RuntimeException("test")), invocation);
 }

 @Test
 public void testValidateException_invalid() {
 when(mock(Object.class).method()).thenThrow(new IllegalStateException("test"));
 Invocation invocation = new InvocationOnMock(mock(Object.class), "method", new Object[0]);
 assertThrows(MockitoException.class, () -> validator.validate(new ThrowsException(new IllegalStateException("test")), invocation));
 }

 @Test
 public void testValidateAnswerWithProvider() {
 AnswerWithProvider<Object, String> answer = new AnswerWithProvider<Object, String>() {
 @Override
 public String answer(InvocationOnMock invocation) {
 return "test";
 }
 };
 Invocation invocation = new InvocationOnMock(mock(Object.class), "method", new Object[0]);
 validator.validate(answer, invocation);
 }

 @Test
 public void testValidateAnswerWrapper() {
 AnswerWrapper<Object, String> answer = new AnswerWrapper<Object, String>(new Returns("test"));
 Invocation invocation = new InvocationOnMock(mock(Object.class), "method", new Object[0]);
 validator.validate(answer, invocation);
 }

 @Test
 public void testValidateReturnValues() {
 ReturnValues returnValues = new ReturnValues();
 Invocation invocation = new InvocationOnMock(mock(Object.class), "method", new Object[0]);
 validator.validate(returnValues, invocation);
 }
}