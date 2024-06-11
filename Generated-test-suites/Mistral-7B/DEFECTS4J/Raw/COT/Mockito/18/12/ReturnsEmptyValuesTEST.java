package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.Answer;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
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
 public void testAnswer_toStringMethod() {
 InvocationOnMock invocation = mock(String.class).invocationOnMethod("toString");
 Object answer = returnsEmptyValues.answer(invocation);
 MockName name = mock(MockName.class);
 name.setMockName("mockName");
 assertEquals("Mock for String, hashCode: 1609315353", answer);
 }

 @Test
 public void testAnswer_compareToMethod() {
 Object mock = mock(Comparable.class);
 Object other = mock(Comparable.class);
 InvocationOnMock invocation = mock(Comparable.class).invocationOnMethod("compareTo", other);
 Object answer = returnsEmptyValues.answer(invocation);
 assertEquals(1, answer);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAnswer_ArrayList_add() {
 List<String> list = (List<String>) returnsEmptyValues.returnValueFor(ArrayList.class);
 list.add("test");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAnswer_HashSet_add() {
 Set<String> set = (Set<String>) returnsEmptyValues.returnValueFor(HashSet.class);
 set.add("test");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAnswer_LinkedHashSet_add() {
 Set<String> set = (Set<String>) returnsEmptyValues.returnValueFor(LinkedHashSet.class);
 set.add("test");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAnswer_TreeSet_add() {
 SortedSet<String> set = (SortedSet<String>) returnsEmptyValues.returnValueFor(TreeSet.class);
 set.add("test");
 }

 @Test
 public void testAnswer_Map_put() {
 Map<String, String> map = (Map<String, String>) returnsEmptyValues.returnValueFor(Map.class);
 map.put("key", "value");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAnswer_HashMap_put() {
 Map<String, String> map = (Map<String, String>) returnsEmptyValues.returnValueFor(HashMap.class);
 map.put("key", "value");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAnswer_TreeMap_put() {
 SortedMap<String, String> map = (SortedMap<String, String>) returnsEmptyValues.returnValueFor(TreeMap.class);
 map.put("key", "value");
 }

 @Test
 public void testAnswer_LinkedHashMap_put() {
 Map<String, String> map = (Map<String, String>) returnsEmptyValues.returnValueFor(LinkedHashMap.class);
 map.put("key", "value");
 }
}