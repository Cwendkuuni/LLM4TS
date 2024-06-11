package org.mockito.internal.stubbing.defaultanswers.Tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;

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
import static org.mockito.Mockito.mock;

public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues returnsEmptyValues;

 @Before
 public void setup() {
 returnsEmptyValues = new ReturnsEmptyValues();
 }

 @Test
 public void testToStringMethod() {
 InvocationOnMock invocation = mock(String.class).invocationOnSelf();
 String mock = mock(String.class);
 String result = (String) returnsEmptyValues.answer(invocation);
 assertEquals("Mock for String, hashCode: " + mock.hashCode(), result);
 }

 @Test
 public void testCompareToMethod() {
 InvocationOnMock invocation = mock(Comparable.class).invocationOnSelf();
 Object result = returnsEmptyValues.answer(invocation);
 assertEquals(1, result);
 }

 @Test
 public void testPrimitiveTypes() {
 InvocationOnMock intInvocation = mock(int[].class).invocationOnSelf();
 int[] ints = (int[]) returnsEmptyValues.answer(intInvocation);
 assertEquals(0, ints[0]);

 InvocationOnMock booleanInvocation = mock(boolean[].class).invocationOnSelf();
 boolean[] booleans = (boolean[]) returnsEmptyValues.answer(booleanInvocation);
 assertEquals(false, booleans[0]);
 }

 @Test
 public void testEmptyCollection() {
 Class<?> collectionType = Collection.class;
 InvocationOnMock collectionInvocation = mock(collectionType).invocationOnSelf();
 Collection<?> collection = (Collection<?>) returnsEmptyValues.answer(collectionInvocation);
 assertEquals(0, collection.size());
 }

 @Test
 public void testEmptySet() {
 Class<?> setType = Set.class;
 InvocationOnMock setInvocation = mock(setType).invocationOnSelf();
 Set<?> set = (Set<?>) returnsEmptyValues.answer(setInvocation);
 assertEquals(0, set.size());
 }

 @Test
 public void testEmptyHashSet() {
 Class<?> hashSetType = HashSet.class;
 InvocationOnMock hashSetInvocation = mock(hashSetType).invocationOnSelf();
 Set<?> hashSet = (Set<?>) returnsEmptyValues.answer(hashSetInvocation);
 assertEquals(true, hashSet instanceof HashSet);
 assertEquals(0, hashSet.size());
 }

 @Test
 public void testEmptyTreeSet() {
 Class<?> treeSetType = TreeSet.class;
 InvocationOnMock treeSetInvocation = mock(treeSetType).invocationOnSelf();
 Set<?> treeSet = (Set<?>) returnsEmptyValues.answer(treeSetInvocation);
 assertEquals(true, treeSet instanceof TreeSet);
 assertEquals(0, treeSet.size());
 }

 @Test
 public void testEmptyLinkedHashSet() {
 Class<?> linkedHashSetType = LinkedHashSet.class;
 InvocationOnMock linkedHashSetInvocation = mock(linkedHashSetType).invocationOnSelf();
 Set<?> linkedHashSet = (Set<?>) returnsEmptyValues.answer(linkedHashSetInvocation);
 assertEquals(true, linkedHashSet instanceof LinkedHashSet);
 assertEquals(0, linkedHashSet.size());
 }

 @Test
 public void testEmptyList() {
 Class<?> listType = List.class;
 InvocationOnMock listInvocation = mock(listType).invocationOnSelf();
 List<?> list = (List<?>) returnsEmptyValues.answer(listInvocation);
 assertEquals(0, list.size());
 }

 @Test
 public void testEmptyLinkedList() {
 Class<?> linkedListType = LinkedList.class;
 InvocationOnMock linkedListInvocation = mock(linkedListType).invocationOnSelf();
 List<?> linkedList = (List<?>) returnsEmptyValues.answer(linkedListInvocation);
 assertEquals(true, linkedList instanceof LinkedList);
 assertEquals(0, linkedList.size());
 }

 @Test
 public void testEmptyArrayList() {
 Class<?> arrayListType = ArrayList.class;
 InvocationOnMock arrayListInvocation = mock(arrayListType).invocationOnSelf();
 List<?> arrayList = (List<?>) returnsEmptyValues.answer(arrayListInvocation);
 assertEquals(true, arrayList instanceof ArrayList);
 assertEquals(0, arrayList.size());
 }

 @Test
 public void testEmptyMap() {
 Class<?> mapType = Map.class;
 InvocationOnMock mapInvocation = mock(mapType).invocationOnSelf();
 Map<?> map = (Map<?, ?>) returnsEmptyValues.answer(mapInvocation);
 assertEquals(0, map.size());
 }

 @Test
 public void testEmptyHashMap() {
 Class<?> hashMapType = HashMap.class;
 InvocationOnMock hashMapInvocation = mock(hashMapType).invocationOnSelf();
 Map<?> hashMap = (Map<?, ?>) returnsEmptyValues.answer(hashMapInvocation);
 assertEquals(true, hashMap instanceof HashMap);
 assertEquals(0, hashMap.size());
 }

 @Test
 public void testEmptyTreeMap() {
 Class<?> treeMapType = TreeMap.class;
 InvocationOnMock treeMapInvocation = mock(treeMapType).invocationOnSelf();
 SortedMap<?> treeMap = (SortedMap<?, ?>) returnsEmptyValues.answer(treeMapInvocation);
 assertEquals(true, treeMap instanceof TreeMap);
 assertEquals(0, treeMap.size());
 }

 @Test
 public void testEmptyLinkedHashMap() {
 Class<?> linkedHashMapType = LinkedHashMap.class;
 InvocationOnMock linkedHashMapInvocation = mock(linkedHashMapType).invocationOnSelf();
 Map<?> linkedHashMap = (Map<?, ?>) returnsEmptyValues.answer(linkedHashMapInvocation);
 assertEquals(true, linkedHashMap instanceof LinkedHashMap);
 assertEquals(0, linkedHashMap.size());
 }
}