package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(org.mockito.runners.JUnit4.class)
public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 @Test
 public void testToStringMethod() {
 Mockito.when(mock.toString()).thenAnswer(returnsEmptyValues);
 String mockString = mock.toString();
 assertEquals("Mock for MockType, hashCode: 123456", mockString);
 }

 @Test
 public void testCompareToMethod() {
 Object mock1 = Mockito.mock(Comparable.class);
 Object mock2 = Mockito.mock(Comparable.class);
 Mockito.when(mock1.compareTo(mock2)).thenAnswer(returnsEmptyValues);
 assertEquals(0, mock1.compareTo(mock2));
 }

 @Test
 public void testEmptyCollectionReturnForList() {
 List<String> mockList = Mockito.mock(List.class);
 Mockito.when(mockList.size()).thenReturn(0);
 Mockito.when(mockList.isEmpty()).thenReturn(true);
 Mockito.when(mockList.add(ArgumentMatchers.anyObject())).thenThrow(UnsupportedOperationException.class);
 Mockito.when(mockList.toString()).thenAnswer(returnsEmptyValues);

 List<String> actualList = mockList;
 assertEquals("[]", actualList.toString());
 }

 @Test
 public void testEmptySetReturnForHashSet() {
 Set<Integer> mockSet = Mockito.mock(HashSet.class);
 Mockito.when(mockSet.isEmpty()).thenReturn(true);
 Mockito.when(mockSet.size()).thenReturn(0);
 Mockito.when(mockSet.add(ArgumentMatchers.anyInt())).thenThrow(UnsupportedOperationException.class);
 Mockito.when(mockSet.toString()).thenAnswer(returnsEmptyValues);

 Set<Integer> actualSet = mockSet;
 assertEquals("{}", actualSet.toString());
 }

 @Test
 public void testEmptyMapReturnForHashMap() {
 Map<String, Integer> mockMap = Mockito.mock(HashMap.class);
 Mockito.when(mockMap.isEmpty()).thenReturn(true);
 Mockito.when(mockMap.size()).thenReturn(0);
 Mockito.when(mockMap.put(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())).thenThrow(UnsupportedOperationException.class);
 Mockito.when(mockMap.toString()).thenAnswer(returnsEmptyValues);

 Map<String, Integer> actualMap = mockMap;
 assertEquals("{}", actualMap.toString());
 }

 @Test
 public void testNullReturnForEverythingElse() {
 Object mock = Mockito.mock(Object.class);
 Mockito.when(mock.toString()).thenAnswer(returnsEmptyValues);
 assertNull(mock.toString());
 }

 @Test
 public void testVerifyZeroInteractions() {
 Object mock = Mockito.mock(Object.class);
 Mockito.verifyZeroInteractions(mock);
 }
}