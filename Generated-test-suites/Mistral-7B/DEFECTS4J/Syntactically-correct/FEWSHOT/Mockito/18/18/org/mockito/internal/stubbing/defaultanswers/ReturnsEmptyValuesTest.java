package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.mockito.internal.util.MockUtil.getMockName;
import static org.mockito.internal.util.ObjectMethodsGuru.isToString;
import static org.mockito.internal.util.ObjectMethodsGuru.isCompareToMethod;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.mock.MockName;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

 @Test
 public void testReturnDefaultValueForPrimitiveTypes() {
  ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);

  assertEquals(0, returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, new Object[] { Integer.TYPE })).intValue());
  assertEquals(0.0f, returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, new Object[] { Float.TYPE })).floatValue());
  assertEquals(0.0d, returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, new Object[] { Double.TYPE })).doubleValue());
  assertEquals((byte) 0, returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, new Object[] { Byte.TYPE })).byteValue());
  assertEquals((short) 0, returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, new Object[] { Short.TYPE })).shortValue());
  assertEquals(0, returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, new Object[] { Character.TYPE })).charValue());
  assertEquals(false, returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, new Object[] { Boolean.TYPE })).booleanValue());
  assertEquals(0L, returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, new Object[] { Long.TYPE })).longValue());
 }

 @Test
 public void testReturnEmptyCollectionForCollectionType() {
  ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);

  Collection<Integer> collection = returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, new Object[] { Collection.class }));
  assertEquals(0, collection.size());
  assertFalse(collection.isEmpty());
  assertNull(collection.toArray());
 }

 @Test
 public void testReturnEmptySetForSetType() {
  ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);

  Set<Integer> set = returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, new Object[] { Set.class }));
  assertEquals(0, set.size());
  assertTrue(set.isEmpty());
  assertNull(set.toArray());
 }

 @Test
 public void testReturnEmptyHashSetForHashSetType() {
  ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);

  Set<Integer> hashSet = returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, new Object[] { HashSet.class }));
  assertEquals(0, hashSet.size());
  assertTrue(hashSet.isEmpty());
  assertNull(hashSet.toArray());
 }

 @Test
 public void testReturnEmptyTreeSetForTreeSetType() {
  ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);

  SortedSet<Integer> treeSet = returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, new Object[] { TreeSet.class }));
  assertEquals(0, treeSet.size());
  assertTrue(treeSet.isEmpty());
  assertFalse(treeSet.isEmpty());
  assertNull(treeSet.toArray());
 }

 @Test
 public void testReturnEmptyLinkedHashSetForLinkedHashSetType() {
  ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);

  Set<Integer> linkedHashSet = returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, new Object[] { LinkedHashSet.class }));
  assertEquals(0, linkedHashSet.size());
  assertTrue(linkedHashSet.isEmpty());
  assertNull(linkedHashSet.toArray());
 }

 @Test
 public void testReturnEmptyListForListType() {
  ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);

  List<Integer> list = returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, new Object[] { List.class }));
  assertEquals(0, list.size());
  assertTrue(list.isEmpty());
  assertNull(list.toArray());
 }

 @Test
 public void testReturnEmptyLinkedListForLinkedListType() {
  ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);

  List<Integer> linkedList = returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, new Object[] { LinkedList.class }));
  assertEquals(0, linkedList.size());
  assertTrue(linkedList.isEmpty());
  assertNull(linkedList.toArray());
 }

 @Test
 public void testReturnEmptyArrayListForArrayListType() {
  ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);

  List<Integer> arrayList = returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, new Object[] { ArrayList.class }));
  assertEquals(0, arrayList.size());
  assertTrue(arrayList.isEmpty());
  assertNull(arrayList.toArray());
 }

 @Test
 public void testReturnEmptyHashMapForMapType() {
  ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);

  Map<String, Integer> map = returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, new Object[] { Map.class }));
  assertEquals(0, map.size());
  assertTrue(map.isEmpty());
  assertNull(map.get(""));
  assertNull(map.entrySet().toArray());
 }

 @Test
 public void testReturnEmptyLinkedHashMapForLinkedHashMapType() {
  ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);

  Map<String, Integer> linkedHashMap = returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, new Object[] { LinkedHashMap.class }));
  assertEquals(0, linkedHashMap.size());
  assertTrue(linkedHashMap.isEmpty());
  assertNull(linkedHashMap.get(""));
  assertNull(linkedHashMap.entrySet().toArray());
 }

 @Test
 public void testReturnEmptyTreeMapForTreeMapType() {
  ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);

  SortedMap<String, Integer> treeMap = returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, new Object[] { TreeMap.class }));
  assertEquals(0, treeMap.size());
  assertTrue(treeMap.isEmpty());
  assertNull(treeMap.get(""));
  assertNull(treeMap.firstKey());
  assertNull(treeMap.lastKey());
  assertNull(treeMap.entrySet().toArray());
 }

 @Test
 public void testToStringMethod() {
  Object mock = Mockito.mock(Object.class);
  ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);

  String expectedToString = String.format("Mock for %s, hashCode: %s", getMockName(mock).getClassName(), mock.hashCode());
  assertEquals(expectedToString, returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, new Object[] { InvocationOnMock.class })).toString());
 }

 @Test
 public void testCompareToMethod() {
  Object mock1 = Mockito.mock(Object.class);
  Object mock2 = Mockito.mock(Object.class);
  ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);

  //Same mock instance
  assertEquals(0, returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, new Object[] { InvocationOnMock.class, Object.class })).compareTo(mock1));
  assertEquals(0, returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, new Object[] { InvocationOnMock.class, Object.class })).compareTo(mock2));

  //Different mock instances
  assertEquals(1, returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, new Object[] { InvocationOnMock.class, Object.class })).compareTo(mock2));
  assertEquals(1, returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, new Object[] { InvocationOnMock.class, Object.class })).compareTo(mock1));
 }

 @Test
 public void testReturnNullForOtherTypes() {
  ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);

  assertNull(returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, new Object[] { String.class })));
  assertNull(returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, new Object[] { byte[].class })));
  assertNull(returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, new Object[] { Double.class })));
 }

}