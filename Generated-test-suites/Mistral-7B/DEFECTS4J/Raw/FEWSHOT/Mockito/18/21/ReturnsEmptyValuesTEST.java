package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Test suite for ReturnsEmptyValues class
 */
public class ReturnsEmptyValuesTest {

 @Test
 public void testAnswerToStringMethod() {
 MockName mockName = MockUtil.spy(new Object());
 ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
 InvocationOnMock invocation = MockUtil.mockInvocation(MockUtil.getMockSettings(mockName.getMock()).getTypeToMock().getMethod("toString"));

 String expected = "Mock for " + mockName.getMock().getClass().getSimpleName() + ", hashCode: " + mockName.getMock().hashCode() + System.lineSeparator();
 String actual = (String) returnsEmptyValues.answer(invocation);

 assertEquals(expected, actual);
 }

 @Test
 public void testAnswerCompareToMethod() {
 Object mockObject1 = MockUtil.spy(new Object());
 Object mockObject2 = MockUtil.spy(new Object());
 ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
 InvocationOnMock invocation = MockUtil.mockInvocation(MockUtil.getMockSettings(mockObject1).getTypeToMock().getMethod("compareTo", Object.class), mockObject2);

 int actual = (Integer) returnsEmptyValues.answer(invocation);
 int expected = 1;
 assertEquals(expected, actual);
 }

 @Test
 public void testReturnValueForPrimitiveTypes() {
 ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 byte expectedByte = (byte) 0;
 short expectedShort = (short) 0;
 int expectedInt = 0;
 long expectedLong = 0L;
 float expectedFloat = 0.0f;
 double expectedDouble = 0.0;
 boolean expectedBoolean = false;
 char expectedChar = (char) 0;

 byte actualByte = (byte) returnsEmptyValues.returnValueFor(Byte.TYPE);
 short actualShort = (short) returnsEmptyValues.returnValueFor(Short.TYPE);
 int actualInt = (int) returnsEmptyValues.returnValueFor(Integer.TYPE);
 long actualLong = (long) returnsEmptyValues.returnValueFor(Long.TYPE);
 float actualFloat = (float) returnsEmptyValues.returnValueFor(Float.TYPE);
 double actualDouble = (double) returnsEmptyValues.returnValueFor(Double.TYPE);
 boolean actualBoolean = (boolean) returnsEmptyValues.returnValueFor(Boolean.TYPE);
 char actualChar = (char) returnsEmptyValues.returnValueFor(Character.TYPE);

 assertEquals(expectedByte, actualByte);
 assertEquals(expectedShort, actualShort);
 assertEquals(expectedInt, actualInt);
 assertEquals(expectedLong, actualLong);
 assertEquals(expectedFloat, actualFloat, 0.0f);
 assertEquals(expectedDouble, actualDouble, 0.0);
 assertEquals(expectedBoolean, actualBoolean);
 assertEquals(expectedChar, actualChar);
 }

 @Test
 public void testReturnValueForCollectionTypes() {
 ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 Collection<Integer> expectedCollection = new ArrayList<>();
 Collection<Integer> actualCollection = (Collection<Integer>) returnsEmptyValues.returnValueFor(Collection.class);
 assertEquals(expectedCollection, actualCollection);
 }

 @Test
 public void testReturnValueForSetTypes() {
 ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 Set<Integer> expectedSet = new HashSet<>();
 Set<Integer> actualSet = (Set<Integer>) returnsEmptyValues.returnValueFor(Set.class);
 assertEquals(expectedSet, actualSet);
 }

 @Test
 public void testReturnValueForHashSetTypes() {
 ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 Set<Integer> expectedHashSet = new HashSet<>();
 Set<Integer> actualHashSet = (Set<Integer>) returnsEmptyValues.returnValueFor(HashSet.class);
 assertEquals(expectedHashSet, actualHashSet);
 }

 @Test
 public void testReturnValueForSortedSetTypes() {
 ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 SortedSet<Integer> expectedSortedSet = new TreeSet<>();
 SortedSet<Integer> actualSortedSet = (SortedSet<Integer>) returnsEmptyValues.returnValueFor(SortedSet.class);
 assertEquals(expectedSortedSet, actualSortedSet);
 }

 @Test
 public void testReturnValueForTreeSetTypes() {
 ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 SortedSet<Integer> expectedTreeSet = new TreeSet<>();
 SortedSet<Integer> actualTreeSet = (SortedSet<Integer>) returnsEmptyValues.returnValueFor(TreeSet.class);
 assertEquals(expectedTreeSet, actualTreeSet);
 }

 @Test
 public void testReturnValueForLinkedHashSetTypes() {
 ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 Set<Integer> expectedLinkedHashSet = new LinkedHashSet<>();
 Set<Integer> actualLinkedHashSet = (Set<Integer>) returnsEmptyValues.returnValueFor(LinkedHashSet.class);
 assertEquals(expectedLinkedHashSet, actualLinkedHashSet);
 }

 @Test
 public void testReturnValueForListTypes() {
 ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 List<Integer> expectedList = new ArrayList<>();
 List<Integer> actualList = (List<Integer>) returnsEmptyValues.returnValueFor(List.class);
 assertEquals(expectedList, actualList);
 }

 @Test
 public void testReturnValueForLinkedListTypes() {
 ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 List<Integer> expectedLinkedList = new LinkedList<>();
 List<Integer> actualLinkedList = (List<Integer>) returnsEmptyValues.returnValueFor(LinkedList.class);
 assertEquals(expectedLinkedList, actualLinkedList);
 }

 @Test
 public void testReturnValueForArrayListTypes() {
 ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 List<Integer> expectedArrayList = new ArrayList<>();
 List<Integer> actualArrayList = (List<Integer>) returnsEmptyValues.returnValueFor(ArrayList.class);
 assertEquals(expectedArrayList, actualArrayList);
 }

 @Test
 public void testReturnValueForMapTypes() {
 ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 Map<String, Integer> expectedMap = new HashMap<>();
 Map<String, Integer> actualMap = (Map<String, Integer>) returnsEmptyValues.returnValueFor(Map.class);
 assertEquals(expectedMap, actualMap);
 }

 @Test
 public void testReturnValueForHashMapTypes() {
 ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 Map<String, Integer> expectedHashMap = new HashMap<>();
 Map<String, Integer> actualHashMap = (Map<String, Integer>) returnsEmptyValues.returnValueFor(HashMap.class);
 assertEquals(expectedHashMap, actualHashMap);
 }

 @Test
 public void testReturnValueForTreeMapTypes() {
 ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 SortedMap<String, Integer> expectedTreeMap = new TreeMap<>();
 SortedMap<String, Integer> actualTreeMap = (SortedMap<String, Integer>) returnsEmptyValues.returnValueFor(TreeMap.class);
 assertEquals(expectedTreeMap, actualTreeMap);
 }

 @Test
 public void testReturnValueForLinkedHashMapTypes() {
 ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 Map<String, Integer> expectedLinkedHashMap = new LinkedHashMap<>();
 Map<String, Integer> actualLinkedHashMap = (Map<String, Integer>) returnsEmptyValues.returnValueFor(LinkedHashMap.class);
 assertEquals(expectedLinkedHashMap, actualLinkedHashMap);
 }

 @Test
 public void testReturnNullForOtherTypes() {
 ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 String type = "java.lang.String";
 Object actual = returnsEmptyValues.returnValueFor(Class.forName(type));
 assertNull(actual);
 }

}