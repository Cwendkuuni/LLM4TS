package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

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

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 @Test
 public void testToStringMethod() {
 MockitoAnswer mockAnswer = mock(Answer.class);
 String result = returnsEmptyValues.answer(Mockito.method(String.class, "toString", mockAnswer));
 assertEquals("Mock for [MockitoAnswer], hashCode: 1576629825", result);
 }

 @Test
 public void testCompareToMethod() {
 Object obj1 = new Object();
 Object obj2 = obj1;
 int result = returnsEmptyValues.answer(Mockito.method(Comparable.class, "compareTo", obj1));
 assertEquals(0, result);

 obj2 = new Object();
 result = returnsEmptyValues.answer(Mockito.method(Comparable.class, "compareTo", obj1));
 assertNotNull(result);
 assertNotSame(0, result);
 }

 @Test
 public void testPrimitiveReturningMethods() {
 assertEquals(0, returnsEmptyValues.answer(Mockito.method(byte.class, "getValue", any(Byte.class))).byteValue());
 assertEquals(0, returnsEmptyValues.answer(Mockito.method(short.class, "shortValue", any(Short.class))).shortValue());
 assertEquals(0, returnsEmptyValues.answer(Mockito.method(int.class, "intValue", any(Integer.class))).intValue());
 assertEquals(0L, returnsEmptyValues.answer(Mockito.method(long.class, "longValue", any(Long.class))).longValue());
 assertEquals(0f, returnsEmptyValues.answer(Mockito.method(float.class, "floatValue", any(Float.class))).floatValue());
 assertEquals(0d, returnsEmptyValues.answer(Mockito.method(double.class, "doubleValue", any(Double.class))).doubleValue());
 assertEquals(0, returnsEmptyValues.answer(Mockito.method(boolean.class, "booleanValue", any(Boolean.class))).booleanValue());
 assertEquals(0, returnsEmptyValues.answer(Mockito.method(char.class, "charValue", any(Character.class))).charValue());
 }

 @Test
 public void testCollectionReturningMethods() {
 Collection<Object> result = (Collection<Object>) returnsEmptyValues.answer(Mockito.method(Collection.class, "isEmpty", mock(Collection.class)));
 assertTrue(result.isEmpty());

 result = (Collection<Object>) returnsEmptyValues.answer(Mockito.method(Collection.class, "size", mock(Collection.class)));
 assertEquals(0, result.size());

 result = (Collection<Object>) returnsEmptyValues.answer(Mockito.method(Collection.class, "toArray", mock(Collection.class)));
 assertNotNull(result);
 assertEmpty(result);
 }

 @Test
 public void testSetReturningMethods() {
 Set<Object> result = (Set<Object>) returnsEmptyValues.answer(Mockito.method(Set.class, "isEmpty", mock(Set.class)));
 assertTrue(result.isEmpty());

 result = (Set<Object>) returnsEmptyValues.answer(Mockito.method(Set.class, "size", mock(Set.class)));
 assertEquals(0, result.size());

 result = (Set<Object>) returnsEmptyValues.answer(Mockito.method(Set.class, "add", mock(Set.class), any(Object.class)));
 assertFalse(result.isEmpty());
 }

 @Test
 public void testSortedSetReturningMethods() {
 SortedSet<Object> result = (SortedSet<Object>) returnsEmptyValues.answer(Mockito.method(SortedSet.class, "isEmpty", mock(SortedSet.class)));
 assertTrue(result.isEmpty());

 result = (SortedSet<Object>) returnsEmptyValues.answer(Mockito.method(SortedSet.class, "size", mock(SortedSet.class)));
 assertEquals(0, result.size());

 result = (SortedSet<Object>) returnsEmptyValues.answer(Mockito.method(SortedSet.class, "first", mock(SortedSet.class)));
 assertNull(result);
 }

 @Test
 public void testMapReturningMethods() {
 Map<Object, Object> result = (Map<Object, Object>) returnsEmptyValues.answer(Mockito.method(Map.class, "isEmpty", mock(Map.class)));
 assertTrue(result.isEmpty());

 result = (Map<Object, Object>) returnsEmptyValues.answer(Mockito.method(Map.class, "size", mock(Map.class)));
 assertEquals(0, result.size());

 result = (Map<Object, Object>) returnsEmptyValues.answer(Mockito.method(Map.class, "entrySet", mock(Map.class)));
 assertNotNull(result);
 assertEmpty(result);
 }

 @Test
 public void testSortedMapReturningMethods() {
 SortedMap<Object, Object> result = (SortedMap<Object, Object>) returnsEmptyValues.answer(Mockito.method(SortedMap.class, "isEmpty", mock(SortedMap.class)));
 assertTrue(result.isEmpty());

 result = (SortedMap<Object, Object>) returnsEmptyValues.answer(Mockito.method(SortedMap.class, "size", mock(SortedMap.class)));
 assertEquals(0, result.size());

 result = (SortedMap<Object, Object>) returnsEmptyValues.answer(Mockito.method(SortedMap.class, "firstEntry", mock(SortedMap.class)));
 assertNull(result);
 }

 @Test
 public void testLinkedHashMapReturningMethods() {
 Map<Object, Object> result = (Map<Object, Object>) returnsEmptyValues.answer(Mockito.method(LinkedHashMap.class, "isEmpty", mock(LinkedHashMap.class)));
 assertTrue(result.isEmpty());

 result = (Map<Object, Object>) returnsEmptyValues.answer(Mockito.method(LinkedHashMap.class, "size", mock(LinkedHashMap.class)));
 assertEquals(0, result.size());

 result = (Map<Object, Object>) returnsEmptyValues.answer(Mockito.method(LinkedHashMap.class, "entrySet", mock(LinkedHashMap.class)));
 assertNotNull(result);
 assertEmpty(result);
 }

 @Test
 public void testAnswerWithNull() {
 assertNull(returnsEmptyValues.answer(Mockito.method(Object.class, "getClass", null)));
 }

 private <T> void assertEmpty(Collection<T> collection) {
 assertEquals(0, collection.size());
 assertFalse(collection.isEmpty());
 for (T element : collection) {
 assertNull(element);
 }
 }

}