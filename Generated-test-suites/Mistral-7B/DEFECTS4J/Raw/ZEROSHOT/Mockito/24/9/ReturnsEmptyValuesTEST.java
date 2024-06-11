package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues returnsEmptyValues;

 @Before
 public void setup() {
 returnsEmptyValues = new ReturnsEmptyValues();
 }

 @Test
 public void testToStringAnswer() {
 Mockito.when(Mockito.mock(String.class)).thenAnswer(returnsEmptyValues);
 String mock = Mockito.mock(String.class);
 String result = Mockito.toString(mock);

 assertEquals("Mock for " + Mockito.typeOf(String.class).getSimpleName() + ", hashCode: " + mock.hashCode(), result);
 }

 @Test
 public void testCompareToAnswer() {
 Object obj1 = Mockito.mock(Comparable.class);
 Object obj2 = Mockito.mock(Comparable.class);
 Mockito.when(obj1.compareTo(obj2)).thenAnswer(returnsEmptyValues);

 int result = obj1.compareTo(obj2);

 assertEquals(1, result);
 }

 @Test
 public void testPrimitiveReturningMethods() {
 Mockito.when(Mockito.mock(int.class)).thenAnswer(returnsEmptyValues);
 int mockInt = Mockito.mock(int.class);

 int intResult = mockInt.intValue();
 int intDefaultValue = Primitives.getDefaultIntValue();

 assertEquals(intDefaultValue, intResult);
 }

 @Test
 public void testCollectionReturningMethods() {
 Mockito.when(Mockito.mock(Collection.class)).thenAnswer(returnsEmptyValues);
 Collection<Integer> mockCollection = Mockito.mock(Collection.class);

 Collection<Integer> result = mockCollection.isEmpty() ? mockCollection : mockCollection.stream().collect(Collectors.toList());

 assertEquals(true, result.isEmpty());
 }

 @Test
 public void testSetReturningMethods() {
 Mockito.when(Mockito.mock(Set.class)).thenAnswer(returnsEmptyValues);
 Set<String> mockSet = Mockito.mock(Set.class);

 Set<String> result = mockSet.isEmpty() ? mockSet : mockSet.stream().collect(Collectors.toList());

 assertEquals(true, result.isEmpty());
 }

 @Test
 public void testMapReturningMethods() {
 Mockito.when(Mockito.mock(Map.class)).thenAnswer(returnsEmptyValues);
 Map<String, Integer> mockMap = Mockito.mock(Map.class);

 Map<String, Integer> result = mockMap.isEmpty() ? mockMap : mockMap.entrySet().stream().collect(Collectors.toList());

 assertEquals(true, result.isEmpty());
 }

 @Test
 public void testSortedSetReturningMethods() {
 Mockito.when(Mockito.mock(SortedSet.class)).thenAnswer(returnsEmptyValues);
 SortedSet<String> mockSortedSet = Mockito.mock(SortedSet.class);

 SortedSet<String> result = mockSortedSet.isEmpty() ? mockSortedSet : mockSortedSet.stream().collect(Collectors.toList());

 assertEquals(true, result.isEmpty());
 }

 @Test
 public void testSortedMapReturningMethods() {
 Mockito.when(Mockito.mock(SortedMap.class)).thenAnswer(returnsEmptyValues);
 SortedMap<String, Integer> mockSortedMap = Mockito.mock(SortedMap.class);

 SortedMap<String, Integer> result = mockSortedMap.isEmpty() ? mockSortedMap : mockSortedMap.entrySet().stream().collect(Collectors.toList());

 assertEquals(true, result.isEmpty());
 }

 @Test
 public void testAnswerForOtherTypes() {
 Mockito.when(Mockito.mock(Object.class)).thenAnswer(returnsEmptyValues);
 Object mockObject = Mockito.mock(Object.class);

 Object result = returnsEmptyValues.answer(new InvocationOnMock(mockObject, Mockito.method(Object.class, "testMethod", new Class[0])));

 assertNotNull(result);
 }
}