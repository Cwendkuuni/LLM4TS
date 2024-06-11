package org.mockito.internal.stubbing.answers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class AnswersValidatorTest {

 private AnswersValidator validator;
 private Reporter reporter;
 private Object mockObject;

 @Before
 public void setup() {
 validator = new AnswersValidator();
 reporter = mock(Reporter.class);
 validator.setReporter(reporter);
 mockObject = mock(Object.class);
 }

 @Test
 public void testValidateDoNothingWithVoidMethod() {
 when(mockObject.doSomething()).thenAnswer(InvocationOnMock.answer(new DoesNothing()));
 validator.validate(new DoesNothing(), new InvocationOnMock(mockObject, "doSomething", null));
 // no exception should be thrown
 }

 @Test(expected = AssertionError.class)
 public void testValidateDoNothingWithNonVoidMethod() {
 when(mockObject.doSomethingElse()).thenAnswer(InvocationOnMock.answer(new DoesNothing()));
 validator.validate(new DoesNothing(), new InvocationOnMock(mockObject, "doSomethingElse", new String[0]));
 // AssertionError should be thrown
 }

 @Test
 public void testValidateReturnValueWithVoidMethod() {
 when(mockObject.voidMethod()).thenAnswer(InvocationOnMock.answer(new Returns("hello")));
 validator.validate(new Returns("hello"), new InvocationOnMock(mockObject, "voidMethod", null));
 // no exception should be thrown
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnValueWithNonVoidMethodAndNullReturnValue() {
 when(mockObject.returnNull()).thenAnswer(InvocationOnMock.answer(new Returns(null)));
 validator.validate(new Returns(null), new InvocationOnMock(mockObject, "returnNull", new Object[0]));
 // AssertionError should be thrown
 }

 @Test(expected = AssertionError.class)
 public void testValidateReturnValueWithNonVoidMethodAndInvalidReturnType() {
 when(mockObject.returnString()).thenAnswer(InvocationOnMock.answer(new Returns(123)));
 validator.validate(new Returns(123), new InvocationOnMock(mockObject, "returnString", new Object[0]));
 // AssertionError should be thrown
 }

 @Test
 public void testValidateReturnValueWithNonVoidMethodAndValidReturnType() {
 when(mockObject.returnString()).thenAnswer(InvocationOnMock.answer(new Returns("hello")));
 validator.validate(new Returns("hello"), new InvocationOnMock(mockObject, "returnString", new Object[0]));
 // no exception should be thrown
 }

 @Test(expected = AssertionError.class)
 public void testValidateExceptionWithInvalidThrowable() {
 when(mockObject.throwException()).thenAnswer(InvocationOnMock.answer(new ThrowsException(new IllegalArgumentException())));
 validator.validate(new ThrowsException(new IllegalArgumentException()), new InvocationOnMock(mockObject, "throwException", new Object[0]));
 // AssertionError should be thrown
 }

 @Test
 public void testValidateExceptionWithValidThrowable() {
 when(mockObject.throwException()).thenAnswer(InvocationOnMock.answer(new ThrowsException(new RuntimeException())));
 validator.validate(new ThrowsException(new RuntimeException()), new InvocationOnMock(mockObject, "throwException", new Object[0]));
 // no exception should be thrown
 }

 @Test(expected = AssertionError.class)
 public void testValidateExceptionWithCheckedException() {
 when(mockObject.throwCheckedException()).thenAnswer(InvocationOnMock.answer(new ThrowsException(new IOException())));
 validator.validate(new ThrowsException(new IOException()), new InvocationOnMock(mockObject, "throwCheckedException", new Object[0]));
 // AssertionError should be thrown
 }

 @Test
 public void testValidateExceptionWithRuntimeExceptionOrError() {
 when(mockObject.throwRuntimeExceptionOrError()).thenAnswer(InvocationOnMock.answer(new ThrowsException(new RuntimeException())));
 validator.validate(new ThrowsException(new RuntimeException()), new InvocationOnMock(mockObject, "throwRuntimeExceptionOrError", new Object[0]));
 // no exception should be thrown
 }

 @Test
 public void testValidateExceptionWithCheckedExceptionAndValidArguments() {
 when(mockObject.throwCheckedExceptionWithArguments()).thenAnswer(InvocationOnMock.answer(new ThrowsException(new IOException("message"))));
 validator.validate(new ThrowsException(new IOException("message")), new InvocationOnMock(mockObject, "throwCheckedExceptionWithArguments", new Object[0]));
 // no exception should be thrown
 }

 @Test(expected = AssertionError.class)
 public void testValidateExceptionWithCheckedExceptionAndInvalidArguments() {
 when(mockObject.throwCheckedExceptionWithArguments()).thenAnswer(InvocationOnMock.answer(new ThrowsException(new IOException(new String[]{"arg1", "arg2"}))));
 validator.validate(new ThrowsException(new IOException(new String[]{"arg1", "arg2"})), new InvocationOnMock(mockObject, "throwCheckedExceptionWithArguments", new Object[0]));
 // AssertionError should be thrown
 }

 @Test
 public void testValidateExceptionWithNullThrowable() {
 when(mockObject.throwNull()).thenAnswer(InvocationOnMock.answer(new ThrowsException(null)));
 validator.validate(new ThrowsException(null), new InvocationOnMock(mockObject, "throwNull", new Object[0]));
 // no exception should be thrown
 }

 @Test(expected = AssertionError.class)
 public void testValidateExceptionWithNonCheckedException() {
 when(mockObject.throwNonCheckedException()).thenAnswer(InvocationOnMock.answer(new ThrowsException(new NumberFormatException())));
 validator.validate(new ThrowsException(new NumberFormatException()), new InvocationOnMock(mockObject, "throwNonCheckedException", new Object[0]));
 // AssertionError should be thrown
 }
}