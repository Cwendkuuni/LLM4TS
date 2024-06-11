package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;

public class ReturnsEmptyValuesTest {

 @Test
 public void testAnswerToStringMethod() {
 MockName mockName = MockName.MOCK_NAME_1;
 Object mock = new Object() {
 };
 ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
 InvocationOnMock invocation = Mockito.mock(String.class).invocationOnAny();
 when(mock.toString()).thenReturn("MockName: " + mockName.getName());
 assertEquals("Mock for String, hashCode: " + mock.hashCode() + " MockName: " + mockName.getName(), returnsEmptyValues.answer(invocation));
 }

 @Test
 public void testAnswerCompareToMethod() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
 assertEquals(0, returnsEmptyValues.answer(new InvocationOnMock(new Comparable() {
 @Override
 public int compareTo(Object arg0) {
 return obj1.equals(arg0) ? 0 : 1;
 }
 }, obj1, new Object[0])).intValue());
 assertEquals(1, returnsEmptyValues.answer(new InvocationOnMock(new Comparable() {
 @Override
 public int compareTo(Object arg0) {
 return obj2.equals(arg0) ? 0 : 1;
 }
 }, obj1, new Object[0])).intValue());
 }

 @Test
 public void testReturnValueForPrimitiveTypes() {
 Class<?> primitiveBooleanType = boolean.class;
 Class<?> primitiveByteType = byte.class;
 Class<?> primitiveShortType = short.class;
 Class<?> primitiveIntType = int.class;
 Class<?> primitiveLongType = long.class;
 Class<?> primitiveFloatType = float.class;
 Class<?> primitiveDoubleType = double.class;
 Class<?> charWrapperType = Character.class;
 Class<?> intWrapperType = Integer.class;
 Class<?> longWrapperType = Long.class;
 Class<?> floatWrapperType = Float.class;
 Class<?> doubleWrapperType = Double.class;

 Object result = new ReturnsEmptyValues().returnValueFor(primitiveBooleanType);
 assertEquals(Boolean.FALSE, result);

 result = new ReturnsEmptyValues().returnValueFor(primitiveByteType);
 assertEquals((byte) 0, result);

 result = new ReturnsEmptyValues().returnValueFor(primitiveShortType);
 assertEquals((short) 0, result);

 result = new ReturnsEmptyValues().returnValueFor(primitiveIntType);
 assertEquals(0, result);

 result = new ReturnsEmptyValues().returnValueFor(primitiveLongType);
 assertEquals(0L, result);

 result = new ReturnsEmptyValues().returnValueFor(primitiveFloatType);
 assertEquals(0.0f, result, 0.01f);

 result = new ReturnsEmptyValues().returnValueFor(primitiveDoubleType);
 assertEquals(0.0, result, 0.01);

 result = new ReturnsEmptyValues().returnValueFor(charWrapperType);
 assertEquals((char) 0, result);

 result = new ReturnsEmptyValues().returnValueFor(intWrapperType);
 assertEquals(0, result);

 result = new ReturnsEmptyValues().returnValueFor(longWrapperType);
 assertEquals(0L, result);

 result = new ReturnsEmptyValues().returnValueFor(floatWrapperType);
 assertEquals(0.0f, result, 0.01f);

 result = new ReturnsEmptyValues().returnValueFor(doubleWrapperType);
 assertEquals(0.0, result, 0.01);
 }

 @Test
 public void testReturnEmptyCollectionForCollectionType() {
 Class<?> collectionType = Collection.class;
 Collection<Object> expected = new ArrayList<Object>();
 Collection<Object> result = new ReturnsEmptyValues().returnValueFor(collectionType);
 assertEquals(expected.getClass(), result.getClass());
 assertNotNull(result);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testReturnEmptySetForSetType() {
 Class<?> setType = Set.class;
 Set<Object> expected = new HashSet<Object>();
 Set<Object> result = new ReturnsEmptyValues().returnValueFor(setType);
 assertEquals(expected.getClass(), result.getClass());
 assertNotNull(result);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testReturnEmptyHashSetForHashSetType() {
 Class<?> hashSetType = HashSet.class;
 Set<Object> expected = new LinkedHashSet<Object>();
 Set<Object> result = new ReturnsEmptyValues().returnValueFor(hashSetType);
 assertEquals(expected.getClass(), result.getClass());
 assertNotNull(result);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testReturnEmptyTreeSetForTreeSetType() {
 Class<?> treeSetType = TreeSet.class;
 Set<Object> expected = new TreeSet<Object>();
 Set<Object> result = new ReturnsEmptyValues().returnValueFor(treeSetType);
 assertEquals(expected.getClass(), result.getClass());
 assertNotNull(result);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testReturnEmptyLinkedHashSetForLinkedHashSetType() {
 Class<?> linkedHashSetType = LinkedHashSet.class;
 Set<Object> expected = new LinkedHashSet<Object>();
 Set<Object> result = new ReturnsEmptyValues().returnValueFor(linkedHashSetType);
 assertEquals(expected.getClass(), result.getClass());
 assertNotNull(result);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testReturnEmptyListForListType() {
 Class<?> listType = List.class;
 List<Object> expected = new ArrayList<Object>();
 List<Object> result = new ReturnsEmptyValues().returnValueFor(listType);
 assertEquals(expected.getClass(), result.getClass());
 assertNotNull(result);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testReturnEmptyLinkedListForLinkedListType() {
 Class<?> linkedListType = LinkedList.class;
 List<Object> expected = new LinkedList<Object>();
 List<Object> result = new ReturnsEmptyValues().returnValueFor(linkedListType);
 assertEquals(expected.getClass(), result.getClass());
 assertNotNull(result);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testReturnEmptyArrayListForArrayListType() {
 Class<?> arrayListType = ArrayList.class;
 List<Object> expected = new ArrayList<Object>();
 List<Object> result = new ReturnsEmptyValues().returnValueFor(arrayListType);
 assertEquals(expected.getClass(), result.getClass());
 assertNotNull(result);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testReturnEmptyMapForMapType() {
 Class<?> mapType = Map.class;
 Map<String, Object> expected = new HashMap<String, Object>();
 Map<String, Object> result = new ReturnsEmptyValues().returnValueFor(mapType);
 assertEquals(expected.getClass(), result.getClass());
 assertNotNull(result);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testReturnEmptyHashMapForHashMapType() {
 Class<?> hashMapType = HashMap.class;
 Map<String, Object> expected = new HashMap<String, Object>();
 Map<String, Object> result = new ReturnsEmptyValues().returnValueFor(hashMapType);
 assertEquals(expected.getClass(), result.getClass());
 assertNotNull(result);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testReturnEmptyTreeMapForTreeMapType() {
 Class<?> treeMapType = TreeMap.class;
 SortedMap<String, Object> expected = new TreeMap<String, Object>();
 SortedMap<String, Object> result = new ReturnsEmptyValues().returnValueFor(treeMapType);
 assertEquals(expected.getClass(), result.getClass());
 assertNotNull(result);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testReturnEmptyLinkedHashMapForLinkedHashMapType() {
 Class<?> linkedHashMapType = LinkedHashMap.class;
 Map<String, Object> expected = new LinkedHashMap<String, Object>();
 Map<String, Object> result = new ReturnsEmptyValues().returnValueFor(linkedHashMapType);
 assertEquals(expected.getClass(), result.getClass());
 assertNotNull(result);
 assertTrue(result.isEmpty());
 }
}