package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;

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
 public void testToStringMethod() {
 InvocationOnMock invocation = Mockito.mock(Object.class);
 Object mock = invocation.getMock();
 MockName name = Mockito.mock(MockName.class);
 Mockito.when(name.isDefault()).thenReturn(true);
 Mockito.when(name.toString()).thenReturn("mockName");
 Mockito.when(mockUtil.getMockName(mock)).thenReturn(name);

 String result = (String) returnsEmptyValues.answer(invocation);

 assertEquals("Mock for org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValuesTest$MockTypeToMock at ReturnsEmptyValuesTest.java:24, hashCode: 311317314", result);
 }

 @Test
 public void testCompareToMethod() {
 InvocationOnMock invocation = Mockito.mock(Comparable.class);
 int result = (Integer) returnsEmptyValues.answer(invocation);
 assertEquals(1, result.intValue());
 }

 @Test
 public void testPrimitivesAndWrappers() {
 Class<?> types[] = {boolean.class, Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, Character.TYPE};
 for (Class<?> type : types) {
 Object result = returnsEmptyValues.returnValueFor(type);
 assertEquals(Primitives.defaultValueForPrimitiveOrWrapper(type), result);
 }
 }

 @Test(expected = AssertionError.class)
 public void testNullReturnType() {
 returnsEmptyValues.returnValueFor(null);
 }

 @Test
 public void testCollectionTypes() {
 Class<?> collectionTypes[] = {Collection.class, Set.class, HashSet.class, SortedSet.class, TreeSet.class, LinkedHashSet.class, List.class, LinkedList.class, ArrayList.class};
 for (Class<?> collectionType : collectionTypes) {
 Collection<? > result = (Collection<? >) returnsEmptyValues.returnValueFor(collectionType);
 assertNotNull(result);
 assertEquals(0, result.size());
 }
 }

 @Test
 public void testMapTypes() {
 Class<?> mapTypes[] = {Map.class, HashMap.class, SortedMap.class, TreeMap.class, LinkedHashMap.class};
 for (Class<?> mapType : mapTypes) {
 Map<?, ?> result = (Map<?, ?>) returnsEmptyValues.returnValueFor(mapType);
 assertNotNull(result);
 assertEquals(0, result.size());
 }
 }
}