package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;

public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls;

 @Test
 public void testAnswerNull() {
     returnsSmartNulls = new ReturnsSmartNulls();
     Object result = returnsSmartNulls.answer(mock(Object.class));
     assertNull(result);
 }

 @Test
 public void testAnswerEmptyList() {
     returnsSmartNulls = new ReturnsSmartNulls();
     List<String> mock = mock(List.class);
     when(mock.size()).thenAnswer(returnsSmartNulls);
     List<String> result = mock.size();
     assertNull(result);
 }

 @Test
 public void testAnswerEmptyArray() {
     returnsSmartNulls = new ReturnsSmartNulls();
     String[] mock = mock(String[].class);
     when(mock.length).thenAnswer(returnsSmartNulls);
     String[] result = mock.length;
     assertNull(result);
 }

 @Test
 public void testAnswerString() {
     returnsSmartNulls = new ReturnsSmartNulls();
     StringMockito mock = mock(String.class);
     when(mock.toString()).thenAnswer(returnsSmartNulls);
     String result = mock.toString();
     assertEquals("SmartNull returned by unstubbed toString method on mock", result.toString());
 }

 @Test
 public void testAnswerInstanceOfMock() {
     returnsSmartNulls = new ReturnsSmartNulls();
     ObjectMockito mock = mock(Object.class);
     when(mock.toString()).thenAnswer(returnsSmartNulls);
     Object result = mock.toString();
     assertNotNull(result);
 }

 @Test
 public void testAnswerInstanceOfFinalClass() {
     returnsSmartNulls = new ReturnsSmartNulls();
     final String FINAL_STRING = "final string";
     when(mock(String.class).toString()).thenAnswer(returnsSmartNulls);
     String result = mock(String.class).toString();
     assertNull(result);
 }

 @Test
 public void testAnswerInstanceOfNonMockableClass() {
     returnsSmartNulls = new ReturnsSmartNulls();
     when(mock(BigDecimal.class).toString()).thenAnswer(returnsSmartNulls);
     BigDecimal result = mock(BigDecimal.class).toString();
     assertNull(result);
 }
}