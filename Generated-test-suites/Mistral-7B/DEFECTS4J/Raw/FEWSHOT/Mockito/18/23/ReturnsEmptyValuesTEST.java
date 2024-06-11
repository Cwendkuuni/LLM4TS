package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.internal.util.Primitives.isPrimitiveOrWrapper;

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
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.Primitives;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsEmptyValuesTest {

 private static final ObjectMethodsGuru methodsGuru = new ObjectMethodsGuru();
 private static final ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 @Test
 public void testStringAnswerForToStringMethod() {
 InvocationOnMock invocation = mock(Object.class);
 when(invocation.getMethod()).thenReturn(String.class.getDeclaredMethod("toString", new Class[0]));
 String answer = returnsEmptyValues.answer(invocation);

 assertEquals("Mock for [class org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValuesTest], hashCode: 154118534", answer);
 }

 @Test
 public void testZeroForCompareToMethod() {
 Object mock = mock(Comparable.class);
 InvocationOnMock invocation = mock(Object.class);
 when(invocation.getMock()).thenReturn(mock);
 when(invocation.getArguments()).thenReturn(new Object[]{ mock });
 when(invocation.getMethod()).thenReturn(Comparable.class.getDeclaredMethod("compareTo", new Class[]{ Object.class }));
 int answer = (Integer) returnsEmptyValues.answer(invocation);

 assertEquals(0, answer);
 }

 @Test
 public void testPrimitiveValues() {
 InvocationOnMock invocation = mock(Object.class);
 when(invocation.getMethod()).thenReturn(Byte.TYPE.getDeclaredMethod("valueOf", new Class[0]));
 byte answer = (Byte) returnsEmptyValues.answer(invocation);

 assertEquals((byte) 0, answer);

 when(invocation.getMethod()).thenReturn(Integer.TYPE.getDeclaredMethod("valueOf", new Class[0]));
 int answer1 = (Integer) returnsEmptyValues.answer(invocation);

 assertEquals(0, answer1);

 when(invocation.getMethod()).thenReturn(Character.TYPE.getDeclaredMethod("valueOf", new Class[0]));
 char answer2 = (Character) returnsEmptyValues.answer(invocation);

 assertEquals((char) 0, answer2);
 }

 @Test
 public void testEmptyCollectionForCollectionReturningMethods() {
 InvocationOnMock invocation = mock(Object.class);
 when(invocation.getMethod()).thenReturn(Collection.class.getDeclaredMethod("isEmpty", new Class[0]));
 when(invocation.getMethod()).thenReturn(Collection.class.getDeclaredMethod("size", new Class[0]));
 Collection<Integer> answer = (Collection<Integer>) returnsEmptyValues.answer(invocation);

 assertEquals(0, answer.size());
 assertNull(answer.iterator().next());
 }

 @Test
 public void testEmptySetForSetReturningMethods() {
 InvocationOnMock invocation = mock(Object.class);
 when(invocation.getMethod()).thenReturn(HashSet.class.getDeclaredMethod("isEmpty", new Class[0]));
 when(invocation.getMethod()).thenReturn(HashSet.class.getDeclaredMethod("size", new Class[0]));
 Set<Integer> answer = (Set<Integer>) returnsEmptyValues.answer(invocation);

 assertEquals(0, answer.size());
 assertNull(answer.iterator().next());
 }

 @Test
 public void testEmptyListForListReturningMethods() {
 InvocationOnMock invocation = mock(Object.class);
 when(invocation.getMethod()).thenReturn(ArrayList.class.getDeclaredMethod("isEmpty", new Class[0]));
 when(invocation.getMethod()).thenReturn(ArrayList.class.getDeclaredMethod("size", new Class[0]));
 List<Integer> answer = (List<Integer>) returnsEmptyValues.answer(invocation);

 assertEquals(0, answer.size());
 assertNull(answer.get(0));
 }

 @Test
 public void testEmptyMapForMapReturningMethods() {
 InvocationOnMock invocation = mock(Object.class);
 when(invocation.getMethod()).thenReturn(HashMap.class.getDeclaredMethod("isEmpty", new Class[0]));
 when(invocation.getMethod()).thenReturn(HashMap.class.getDeclaredMethod("size", new Class[0]));
 Map<String, Integer> answer = (Map<String, Integer>) returnsEmptyValues.answer(invocation);

 assertEquals(0, answer.size());
 assertNull(answer.get(""));
 }

 @Test
 public void testEmptySortedMapForSortedMapReturningMethods() {
 InvocationOnMock invocation = mock(Object.class);
 when(invocation.getMethod()).thenReturn(TreeMap.class.getDeclaredMethod("isEmpty", new Class[0]));
 when(invocation.getMethod()).thenReturn(TreeMap.class.getDeclaredMethod("size", new Class[0]));
 SortedMap<String, Integer> answer = (SortedMap<String, Integer>) returnsEmptyValues.answer(invocation);

 assertEquals(0, answer.size());
 assertNull(answer.get(""));
 }

 @Test
 public void testEmptySortedSetForSortedSetReturningMethods() {
 InvocationOnMock invocation = mock(Object.class);
 when(invocation.getMethod()).thenReturn(TreeSet.class.getDeclaredMethod("isEmpty", new Class[0]));
 when(invocation.getMethod()).thenReturn(TreeSet.class.getDeclaredMethod("size", new Class[0]));
 SortedSet<Integer> answer = (SortedSet<Integer>) returnsEmptyValues.answer(invocation);

 assertEquals(0, answer.size());
 assertNull(answer.iterator().next());
 }

 @Test
 public void testNullForOtherMethods() {
 InvocationOnMock invocation = mock(Object.class);
 when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethod("equals", new Class[]{ Object.class }));
 Object answer = returnsEmptyValues.answer(invocation);

 assertNull(answer);
 }

 @Test
 public void testNullForOtherMethodsWithArguments() {
 InvocationOnMock invocation = mock(Object.class);
 when(invocation.getMethod()).thenReturn(Boolean.TYPE.getDeclaredMethod("valueOf", new Class[]{ Boolean.TYPE.getTypeName() }));
 Object answer = returnsEmptyValues.answer(invocation);

 assertNull(answer);
 }

 @Test
 public void testReturnNullForUnknownTypes() {
 InvocationOnMock invocation = mock(Object.class);
 when(invocation.getMethod()).thenReturn(String[].class.getDeclaredMethod("length", new Class[0]));
 Object answer = returnsEmptyValues.answer(invocation);

 assertNull(answer);
 }

 @Test
 public void testReturnNullForUnknownMethods() {
 InvocationOnMock invocation = mock(Object.class);
 when(invocation.getMethod()).thenReturn(null);
 Object answer = returnsEmptyValues.answer(invocation);

 assertNull(answer);
 }

 @Test
 public void testReturnDefaultValueForUnknownMethodsWithArguments() {
 InvocationOnMock invocation = mock(Object.class);
 when(invocation.getMethod()).thenReturn(String[].class.getDeclaredMethod("toString", new Class[0]));
 Object answer = returnsEmptyValues.answer(invocation);

 assertEquals("", answer);
 }

 @Test
 public void testReturnDefaultValueForUnknownMethodsWithArgumentsAndReturnTypeIsCollection() {
 InvocationOnMock invocation = mock(Object.class);
 when(invocation.getMethod()).thenReturn(Collection.class.getDeclaredMethod("isEmpty", new Class[0]));
 when(invocation.getMethod()).thenReturn(Collection.class.getDeclaredMethod("size", new Class[0]));
 Object answer = returnsEmptyValues.answer(invocation);

 assertEquals(0, answer.getClass().getMethod("size").invoke(answer));
 assertNull(answer.getClass().getMethod("iterator").invoke(answer).next());
 }

 @Test
 public void testReturnDefaultValueForUnknownMethodsWithArgumentsAndReturnTypeIsMap() {
 InvocationOnMock invocation = mock(Object.class);
 when(invocation.getMethod()).thenReturn(Map.class.getDeclaredMethod("isEmpty", new Class[0]));
 when(invocation.getMethod()).thenReturn(Map.class.getDeclaredMethod("size", new Class[0]));
 Object answer = returnsEmptyValues.answer(invocation);

 assertEquals(0, answer.getClass().getMethod("size").invoke(answer));
 assertNull(answer.getClass().getMethod("get").invoke(answer, ""));
 }
}