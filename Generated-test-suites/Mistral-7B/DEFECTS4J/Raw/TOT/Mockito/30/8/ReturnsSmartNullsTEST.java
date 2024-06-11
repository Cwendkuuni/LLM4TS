package org.mockito.internal.stubbing.defaultanswers.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls;
 private Object mock;

 @Before
 public void setup() {
 mock = mock(Object.class, returnsSmartNulls);
 }

 @Test
 public void testSmartNullForToStringMethod() {
 String result = (String) doReturn("SmartNull returned by unstubbed method").when(mock).toString();
 assertEquals("SmartNull returned by unstubbed method", result);
 }

 @Test
 public void testNullReturnedByDelegate() {
 when(mock.methodThatReturnsNull(anyInt())).thenReturn(null);
 assertNull(mock.methodThatReturnsNull(1));
 }

 @Test
 public void testSmartNullForNonMockableType() {
 when(mock.methodThatReturnsFinalClass()).thenReturn(mock); // this should not work
 try {
 mock.methodThatReturnsFinalClass();
 fail("Should have thrown NullPointerException");
 } catch (NullPointerException e) {
 assertEquals("SmartNull returned by unstubbed method", e.getMessage());
 }
 }

 @Test(expected = NullPointerException.class)
 public void testNullReturnedByUnstubbedMethod() {
 mock.methodThatThrowsNullPointerException();
 }

 @Test
 public void testSmartNullForArray() {
 Object[] array = new Object[1];
 when(mock.methodThatReturnsArray()).thenReturn(array);
 assertNull(mock.methodThatReturnsArray()[0]);
 }

 @Test
 public void testSmartNullForPrimitiveType() {
 when(mock.methodThatReturnsInt()).thenReturn(Integer.MIN_VALUE);
 assertEquals(-2147483648, mock.methodThatReturnsInt());
 }

 @Test
 public void testSmartNullForCollection() {
 when(mock.methodThatReturnsList()).thenReturn(Collections.emptyList());
 assertFalse(mock.methodThatReturnsList().isEmpty());
 }
}