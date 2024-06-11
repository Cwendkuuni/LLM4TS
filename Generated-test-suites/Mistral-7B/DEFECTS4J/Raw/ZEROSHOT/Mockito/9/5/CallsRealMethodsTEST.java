package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CallsRealMethodsTest {

 private CallsRealMethods callsRealMethods = new CallsRealMethods();
 private Object realObject;

 @Before
 public void setUp() {
 realObject = mock(Object.class);
 }

 @Test
 public void testAnswerCallsRealMethod() throws Exception {
 ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

 // Call real method
 callsRealMethods.answer(new InvocationOnMock(realObject, "methodWithStringParameter", new String[]{"test"}));

 // Verify that real method was called
 verify(realObject).methodWithStringParameter(argumentCaptor.capture("stringArgument"));
 assertThat(argumentCaptor.getValue()).isEqualTo("test");
 }

 @Test
 public void testAnswerThrowsException() throws Exception {
 // Given
 doThrow(new RuntimeException("Test exception")).when(realObject).methodWithException();

 // When
 try {
 callsRealMethods.answer(new InvocationOnMock(realObject, "methodWithException", new Object[0]));
 fail("Exception should be thrown");
 } catch (RuntimeException e) {
 // Then
 assertThat(e.getMessage()).isEqualTo("Test exception");
 }
 }

 @Test
 public void testAnswerReturnsValue() throws Exception {
 // Given
 when(realObject.methodWithNoArguments()).thenReturn("Test value");

 // When
 String result = (String) callsRealMethods.answer(new InvocationOnMock(realObject, "methodWithNoArguments", new Object[0]));

 // Then
 assertThat(result).isEqualTo("Test value");
 }
}