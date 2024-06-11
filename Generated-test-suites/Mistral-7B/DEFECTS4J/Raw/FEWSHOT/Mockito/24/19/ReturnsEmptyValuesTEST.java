package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.internal.util.Primitives.isPrimitiveOrWrapper;
import static org.mockito.internal.util.Primitives.defaultValueForPrimitiveOrWrapper;

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
import org.mockito.internal.util.MockUtil;

public class ReturnsEmptyValuesTest {

 private ObjectMethodsGuru methodsGuru = new ObjectMethodsGuru();
 private MockUtil mockUtil = new MockUtil();
 private ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 @Test
 public void testPrimitives() {
 for (Class<?> type : new Class[] { byte.class, short.class, int.class, long.class, float.class, double.class, char.class, boolean.class }) {
 assertEquals(defaultValueForPrimitiveOrWrapper(type), returnsEmptyValues.returnValueFor(type));
 }
 }

 @Test
 public void testCollections() {
 testEmptyCollection(List.class);
 testEmptyCollection(LinkedList.class);
 testEmptyCollection(ArrayList.class);
 testEmptyCollection(Collection.class);
 testEmptyCollection(Set.class);
 testEmptyCollection(HashSet.class);
 testEmptyCollection(LinkedHashSet.class);
 testEmptyCollection(SortedSet.class);
 testEmptyCollection(TreeSet.class);
 testEmptyCollection(Map.class);
 testEmptyCollection(HashMap.class);
 testEmptyCollection(SortedMap.class);
 testEmptyCollection(TreeMap.class);
 testEmptyCollection(LinkedHashMap.class);
 }

 @Test
 public void testToString() {
 Object mock = new Object();
 MockName mockName = mockUtil.getMockName(mock);
 assertEquals("Mock for " + mockUtil.getMockSettings(mock).getTypeToMock().getSimpleName() + ", hashCode: " + mock.hashCode(), returnsEmptyValues.answer(mockInvocationWithToStringMethod()));
 }

 @Test
 public void testCompareTo() {
 assertEquals(1, returnsEmptyValues.answer(mockInvocationWithCompareToMethod()));
 }

 @Test
 public void testOther() {
 assertNull(returnsEmptyValues.answer(mockInvocationWithOtherMethod()));
 }

 private InvocationOnMock mockInvocationWithToStringMethod() {
 Method toStringMethod = Object.class.getMethod("toString");
 return mockinvocation(toStringMethod);
 }

 private InvocationOnMock mockInvocationWithCompareToMethod() {
 Method compareToMethod = String.class.getMethod("compareTo", String.class);
 return mockinvocation(compareToMethod);
 }

 private InvocationOnMock mockInvocationWithOtherMethod() {
 Method otherMethod = Integer.class.getMethod("valueOf", String.class);
 return mockinvocation(otherMethod);
 }

 private InvocationOnMock mockinvocation(Method method) {
 return mock(Object.class).invokeMockMethod(method, null);
 }

 private void testEmptyCollection(Class<? extends Collection<?>> collectionType) {
 Collection<?> collection = returnsEmptyValues.returnValueFor(collectionType);
 assertTrue(collection.isEmpty());
 }
}