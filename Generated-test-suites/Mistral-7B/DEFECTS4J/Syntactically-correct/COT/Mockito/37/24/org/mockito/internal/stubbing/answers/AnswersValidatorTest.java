package org.mockito.internal.stubbing.answers.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import org.mockito.internal.stubbing.answers.AnswersValidator;
import org.mockito.exceptions.Reporter;

public class AnswersValidatorTest {

 private AnswersValidator validator = new AnswersValidator();
 private Reporter reporter = new Reporter();

 @Test(expected = AssertionError.class)
 public void testValidate_ThrowsException_Valid() {
     Answer<Object> answer = new ThrowsException(new RuntimeException("Test Exception"));
     InvocationOnMock invocation = new InvocationOnMock(null, "testMethod", new Class[]{}, new Object[0]);
     validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidate_ThrowsException_InvalidType() {
     Answer<Object> answer = new ThrowsException(new Exception("Test Exception"));;
     InvocationOnMock invocation = new InvocationOnMock(null, "testMethod", new Class[]{}, new Object[0]);
     validator.validate(answer, invocation);
 }

 @Test
 public void testValidate_ThrowsException_VoidMethod() {
     Answer<Object> answer = new ThrowsException(new RuntimeException("Test Exception"));
     InvocationOnMock invocation = new InvocationOnMock(null, "testVoidMethod", new Class[]{}, new Object[0]);
     validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidate_Returns_VoidMethod() {
     Answer<Object> answer = new Returns(null);
     InvocationOnMock invocation = new InvocationOnMock(null, "testVoidMethod", new Class[]{}, new Object[0]);
     validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidate_Returns_NullAndPrimitive() {
     Answer<Object> answer = new Returns(null);
     InvocationOnMock invocation = new InvocationOnMock(null, "testMethod", new Class[]{int.class}, new Object[]{1});
     validator.validate(answer, invocation);
 }

 @Test
 public void testValidate_Returns_Valid() {
     Answer<Object> answer = new Returns("Test String");
     InvocationOnMock invocation = new InvocationOnMock(null, "testMethod", new Class[]{String.class}, new Object[]{"Test String"});
     validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidate_Returns_InvalidType() {
     Answer<Object> answer = new Returns("Test String");
     InvocationOnMock invocation = new InvocationOnMock(null, "testMethod", new Class[]{int.class}, new Object[]{1});
     validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidate_NullThrowable() {
     Answer<Object> answer = new ThrowsException(null);
     InvocationOnMock invocation = new InvocationOnMock(null, "testMethod", new Class[]{String.class}, new Object[]{"Test String"});
     validator.validate(answer, invocation);
 }

 @Test
 public void testValidate_ExceptionTypeMatch() {
     Answer<Object> answer = new ThrowsException(new IllegalArgumentException("Test Exception"));
     InvocationOnMock invocation = new InvocationOnMock(null, "testMethod", new Class[]{String.class}, new Object[]{"Test String"});
     validator.validate(answer, invocation);
 }

 @Test(expected = AssertionError.class)
 public void testValidate_ExceptionTypeMismatch() {
     Answer<Object> answer = new ThrowsException(new IllegalArgumentException("Test Exception"));
     InvocationOnMock invocation = new InvocationOnMock(null, "testMethod", new Class[]{String[].class}, new Object[]{"Test String"});
     validator.validate(answer, invocation);
 }

}