package test.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

public class ReturnsSmartNullsTest {

 @Test
 public void testAnswer() {
 ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
 Answer<Object> answer = returnsSmartNulls;
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(answer.answer(invocation)).thenReturn("Test Answer");

 Object result = answer.answer(invocation);
 assertEquals("Test Answer", result);
 }

 @Test
 public void testAnswer_WithNullInvocation() {
 ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
 Answer<Object> answer = returnsSmartNulls;
 InvocationOnMock invocation = null;

 try {
 answer.answer(invocation);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testAnswer_WithNullMethodReturn() {
 ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
 Answer<Object> answer = returnsSmartNulls;
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(answer.answer(invocation)).thenReturn(null);

 try {
 Object result = answer.answer(invocation);
 assertNull(result);
 } catch (NullPointerException e) {
 fail("Unexpected NullPointerException");
 }
 }

 @Test
 public void testAnswer_WithUnsupportedReturnType() {
 ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
 Answer<Object> answer = returnsSmartNulls;
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(answer.answer(invocation)).thenReturn(new byte[0]);

 try {
 byte[] result = (byte[]) answer.answer(invocation);
 fail("Expected ClassCastException");
 } catch (ClassCastException e) {
 // Expected
 }
 }
}