package org.mockito.internal.stubbing.defaultanswers;

import org.junit.After;
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
 public void setUp() {
 returnsEmptyValues = new ReturnsEmptyValues();
 }

 @After
 public void tearDown() {
 Mockito.reset(returnsEmptyValues);
 }

 @Test
 public void testToString() {
 Mockito.when(returnsEmptyValues.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenAnswer(returnsEmptyValues);
 Object mock = Mockito.mock(Object.class);
 String toString = returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getName().equals("toString")));
 assertEquals("Mock for Object, hashCode: " + mock.hashCode(), toString);
 }

 @Test
 public void testCompareTo() {
 Mockito.when(returnsEmptyValues.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenAnswer(returnsEmptyValues);
 Object mock1 = Mockito.mock(Comparable.class);
 Object mock2 = Mockito.mock(Comparable.class);
 int compareTo = returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getName().equals("compareTo"))).compareTo(mock2);
 assertEquals(1, compareTo);
 }

 @Test
 public void testEmptyCollection() {
 Mockito.when(returnsEmptyValues.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenAnswer(returnsEmptyValues);
 Collection<String> collection = returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType() == Collection.class));
 assertNotNull(collection);
 assertEquals(0, collection.size());
 }

 @Test
 public void testEmptySet() {
 Mockito.when(returnsEmptyValues.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenAnswer(returnsEmptyValues);
 Set<String> set = returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType() == Set.class));
 assertNotNull(set);
 assertEquals(0, set.size());
 }

 @Test
 public void testEmptyHashSet() {
 Mockito.when(returnsEmptyValues.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenAnswer(returnsEmptyValues);
 Set<String> hashSet = returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType() == HashSet.class));
 assertNotNull(hashSet);
 assertEquals(0, hashSet.size());
 }

 @Test
 public void testEmptySortedSet() {
 Mockito.when(returnsEmptyValues.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenAnswer(returnsEmptyValues);
 SortedSet<String> sortedSet = returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType() == TreeSet.class));
 assertNotNull(sortedSet);
 assertEquals(0, sortedSet.size());
 }

 @Test
 public void testEmptyLinkedHashSet() {
 Mockito.when(returnsEmptyValues.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenAnswer(returnsEmptyValues);
 Set<String> linkedHashSet = returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType() == LinkedHashSet.class));
 assertNotNull(linkedHashSet);
 assertEquals(0, linkedHashSet.size());
 }

 @Test
 public void testEmptyList() {
 Mockito.when(returnsEmptyValues.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenAnswer(returnsEmptyValues);
 List<String> list = returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType() == List.class));
 assertNotNull(list);
 assertEquals(0, list.size());
 }

 @Test
 public void testEmptyLinkedList() {
 Mockito.when(returnsEmptyValues.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenAnswer(returnsEmptyValues);
 List<String> linkedList = returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType() == LinkedList.class));
 assertNotNull(linkedList);
 assertEquals(0, linkedList.size());
 }

 @Test
 public void testEmptyArrayList() {
 Mockito.when(returnsEmptyValues.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenAnswer(returnsEmptyValues);
 List<String> arrayList = returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType() == ArrayList.class));
 assertNotNull(arrayList);
 assertEquals(0, arrayList.size());
 }

 @Test
 public void testEmptyMap() {
 Mockito.when(returnsEmptyValues.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenAnswer(returnsEmptyValues);
 Map<String, String> map = returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType() == Map.class));
 assertNotNull(map);
 assertEquals(0, map.size());
 }

 @Test
 public void testEmptyHashMap() {
 Mockito.when(returnsEmptyValues.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenAnswer(returnsEmptyValues);
 Map<String, String> hashMap = returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType() == HashMap.class));
 assertNotNull(hashMap);
 assertEquals(0, hashMap.size());
 }

 @Test
 public void testEmptySortedMap() {
 Mockito.when(returnsEmptyValues.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenAnswer(returnsEmptyValues);
 SortedMap<String, String> sortedMap = returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType() == TreeMap.class));
 assertNotNull(sortedMap);
 assertEquals(0, sortedMap.size());
 }

 @Test
 public void testEmptyTreeMap() {
 Mockito.when(returnsEmptyValues.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenAnswer(returnsEmptyValues);
 SortedMap<String, String> treeMap = returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType() == TreeMap.class));
 assertNotNull(treeMap);
 assertEquals(0, treeMap.size());
 }

 @Test
 public void testEmptyLinkedHashMap() {
 Mockito.when(returnsEmptyValues.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenAnswer(returnsEmptyValues);
 Map<String, String> linkedHashMap = returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType() == LinkedHashMap.class));
 assertNotNull(linkedHashMap);
 assertEquals(0, linkedHashMap.size());
 }
}