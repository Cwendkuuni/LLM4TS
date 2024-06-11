package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

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
import static org.mockito.Mockito.verify;

public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues returnsEmptyValues;
 private ObjectMethodsGuru methodsGuru;

 @Before
 public void setup() {
 methodsGuru = new ObjectMethodsGuru();
 returnsEmptyValues = new ReturnsEmptyValues();
 }

 @Test
 public void testAnswer_toStringMethod() {
 Mockito<String> mock = mock(String.class);
 InvocationOnMock invocation = Mockito.mockMethod(mock, "toString");

 assertEquals("Mock for String, hashCode: " + mock.hashCode(), returnsEmptyValues.answer(invocation).toString());
 }

 @Test
 public void testAnswer_compareToMethod() {
 Object mock1 = mock(Comparable.class);
 Object mock2 = mock(Comparable.class);
 InvocationOnMock compareToInvocation1 = Mockito.mockMethod(mock1, "compareTo", ArgumentMatchers.any());
 InvocationOnMock compareToInvocation2 = Mockito.mockMethod(mock2, "compareTo", ArgumentMatchers.any());

 verify(mock1).compareTo(mock2);
 verify(mock2).compareTo(mock1);

 assertEquals(1, returnsEmptyValues.answer(compareToInvocation1).intValue());
 }

 @Test
 public void testReturnValueFor_CollectionType_ArrayList() {
 Collection<String> expectedCollection = new ArrayList<>();
 Collection<String> actualCollection = (Collection<String>) returnsEmptyValues.returnValueFor(ArrayList.class);

 assertEquals(expectedCollection, actualCollection);
 }

 @Test
 public void testReturnValueFor_CollectionType_LinkedList() {
 Collection<String> expectedCollection = new LinkedList<>();
 Collection<String> actualCollection = (Collection<String>) returnsEmptyValues.returnValueFor(LinkedList.class);

 assertEquals(expectedCollection, actualCollection);
 }

 @Test
 public void testReturnValueFor_CollectionType_HashSet() {
 Set<String> expectedSet = new HashSet<>();
 Set<String> actualSet = (Set<String>) returnsEmptyValues.returnValueFor(HashSet.class);

 assertEquals(expectedSet, actualSet);
 }

 @Test
 public void testReturnValueFor_CollectionType_LinkedHashSet() {
 Set<String> expectedSet = new LinkedHashSet<>();
 Set<String> actualSet = (Set<String>) returnsEmptyValues.returnValueFor(LinkedHashSet.class);

 assertEquals(expectedSet, actualSet);
 }

 @Test
 public void testReturnValueFor_CollectionType_TreeSet() {
 Set<String> expectedSet = new TreeSet<>();
 Set<String> actualSet = (Set<String>) returnsEmptyValues.returnValueFor(TreeSet.class);

 assertEquals(expectedSet, actualSet);
 }

 @Test
 public void testReturnValueFor_CollectionType_SortedSet() {
 SortedSet<String> expectedSet = new TreeSet<>();
 SortedSet<String> actualSet = (SortedSet<String>) returnsEmptyValues.returnValueFor(SortedSet.class);

 assertEquals(expectedSet, actualSet);
 }

 @Test
 public void testReturnValueFor_MapType_HashMap() {
 Map<String, String> expectedMap = new HashMap<>();
 Map<String, String> actualMap = (Map<String, String>) returnsEmptyValues.returnValueFor(HashMap.class);

 assertEquals(expectedMap, actualMap);
 }

 @Test
 public void testReturnValueFor_MapType_LinkedHashMap() {
 Map<String, String> expectedMap = new LinkedHashMap<>();
 Map<String, String> actualMap = (Map<String, String>) returnsEmptyValues.returnValueFor(LinkedHashMap.class);

 assertEquals(expectedMap, actualMap);
 }

 @Test
 public void testReturnValueFor_MapType_TreeMap() {
 Map<String, String> expectedMap = new TreeMap<>();
 Map<String, String> actualMap = (Map<String, String>) returnsEmptyValues.returnValueFor(TreeMap.class);

 assertEquals(expectedMap, actualMap);
 }

 @Test
 public void testReturnValueFor_SortedMapType_TreeMap() {
 SortedMap<String, String> expectedMap = new TreeMap<>();
 SortedMap<String, String> actualMap = (SortedMap<String, String>) returnsEmptyValues.returnValueFor(SortedMap.class);

 assertEquals(expectedMap, actualMap);
 }
}