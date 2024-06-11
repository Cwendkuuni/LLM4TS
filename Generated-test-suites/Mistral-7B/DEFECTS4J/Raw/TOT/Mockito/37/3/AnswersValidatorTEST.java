package org.mockito.internal.stubbing.answers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doNothing;

public class AnswersValidatorTest {

 private AnswersValidator validator;

 @Before
 public void setup() {
 validator = new AnswersValidator();
 }

 @Test
 public void testValidateDoNothing() {
 // Test valid scenario
 Mockito.doNothing().when(mock(String.class)).someMethod();
 validator.validate(Mockito.doNothing(), new InvocationOnMock(mock(String.class), "someMethod", new Object[0]));

 // Test invalid scenario
 Mockito.doThrow(new RuntimeException("Test")).when(mock(String.class)).someMethod();
 try {
 validator.validate(Mockito.doNothing(), new InvocationOnMock(mock(String.class), "someMethod", new Object[0]));
 fail();
 } catch (AssertionError e) {
 // Expected
 }
 }

 @Test
 public void testValidateReturnValue() {
 // Test valid scenario
 Mockito.when(mock(String.class)).someMethod().thenReturn("Test");
 validator.validate(Mockito.when(mock(String.class)).thenReturn("Test"), new InvocationOnMock(mock(String.class), "someMethod", new Object[0]));

 // Test null return value with void method
 Mockito.doNothing().when(mock(String.class)).someMethod();
 validator.validate(Mockito.when(mock(String.class)).thenReturn(null), new InvocationOnMock(mock(String.class), "someMethod", new Object[0]));

 // Test invalid scenario - void method with return value
 Mockito.when(mock(String.class)).someMethod().thenReturn("Test");
 try {
 validator.validate(Mockito.when(mock(String.class)).thenReturn("Test"), new InvocationOnMock(mock(String.class), "someMethod", new Object[0]));
 fail();
 } catch (AssertionError e) {
 // Expected
 }

 // Test invalid scenario - invalid return type
 Mockito.when(mock(String.class)).someMethod().thenReturn(123);
 try {
 validator.validate(Mockito.when(mock(String.class)).thenReturn(123), new InvocationOnMock(mock(String.class), "someMethod", new Object[0]));
 fail();
 } catch (AssertionError e) {
 // Expected
 }
 }

 @Test
 public void testValidateException() {
 // Test valid scenario
 Mockito.doThrow(new IllegalArgumentException("Test")).when(mock(String.class)).someMethod();
 validator.validate(Mockito.doThrow(new IllegalArgumentException("Test")), new InvocationOnMock(mock(String.class), "someMethod", new Object[0]));

 // Test null throwable
 try {
 validator.validate(Mockito.doThrow(null), new InvocationOnMock(mock(String.class), "someMethod", new Object[0]));
 fail();
 } catch (AssertionError e) {
 // Expected
 }

 // Test checked exception
 try {
 validator.validate(Mockito.doThrow(new IOException("Test")), new InvocationOnMock(mock(String.class), "someMethod", new Object[0]));
 fail();
 } catch (AssertionError e) {
 // Expected
 }
 }
}