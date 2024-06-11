import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;

public class AnswersValidatorTest {

 private AnswersValidator validator;

 @Before
 public void setUp() {
 validator = new AnswersValidator();
 }

 @Test
 public void testValidateDoNothing_voidMethod() {
 Mockito.doNothing().when(Mockito.mock(String.class));
 validator.validate(Mockito.doNothing(), new InvocationOnMock(Mockito.mock(String.class), ArgumentMatchers.any(), (Object[]) null));
 }

 @Test
 public void testValidateDoNothing_nonVoidMethod() {
 Mockito.doThrow(new RuntimeException("Test Exception")).when(Mockito.mock(String.class)).someMethod();
 validator.validate(Mockito.doThrow(new RuntimeException("Test Exception")), new InvocationOnMock(Mockito.mock(String.class), "someMethod", (Object[]) null));
 validator.validate(Mockito.doNothing(), new InvocationOnMock(Mockito.mock(String.class), "someMethod", (Object[]) null)); // should throw an exception
 }

 @Test(expected = MockitoException.class)
 public void testValidateReturnValue_voidMethod() {
 Mockito.when(Mockito.mock(String.class)).thenReturn(1);
 validator.validate(Mockito.thenReturn(1), new InvocationOnMock(Mockito.mock(String.class), "someMethod", (Object[]) null));
 }

 @Test
 public void testValidateReturnValue_validReturnValue() {
 Mockito.when(Mockito.mock(String.class)).thenReturn("Test String");
 validator.validate(Mockito.thenReturn("Test String"), new InvocationOnMock(Mockito.mock(String.class), "someMethod", (Object[]) null));
 }

 @Test(expected = MockitoException.class)
 public void testValidateReturnValue_invalidReturnType() {
 Mockito.when(Mockito.mock(String.class)).thenReturn(1.0);
 validator.validate(Mockito.thenReturn(1.0), new InvocationOnMock(Mockito.mock(String.class), "someMethod", (Object[]) null));
 }

 @Test
 public void testValidateException_validExceptionType() {
 Mockito.doThrow(new IllegalStateException("Test Exception")).when(Mockito.mock(String.class)).someMethod();
 validator.validate(Mockito.doThrow(new IllegalStateException("Test Exception")), new InvocationOnMock(Mockito.mock(String.class), "someMethod", (Object[]) null));
 }

 @Test(expected = MockitoException.class)
 public void testValidateException_invalidExceptionType() {
 Mockito.doThrow(new IllegalArgumentException("Test Exception")).when(Mockito.mock(String.class)).someMethod();
 validator.validate(Mockito.doThrow(new IllegalArgumentException("Test Exception")), new InvocationOnMock(Mockito.mock(String.class), "someMethod", (Object[]) null));
 }

 @Test
 public void testValidateException_nullThrowable() {
 Answer<Object> answer = Mockito.doThrow(null).when(Mockito.mock(String.class)).someMethod();
 try {
 validator.validate(answer, new InvocationOnMock(Mockito.mock(String.class), "someMethod", (Object[]) null));
 } catch (MockitoException e) {
 assertEquals("Cannot stub with null throwable", e.getMessage());
 }
 }
}