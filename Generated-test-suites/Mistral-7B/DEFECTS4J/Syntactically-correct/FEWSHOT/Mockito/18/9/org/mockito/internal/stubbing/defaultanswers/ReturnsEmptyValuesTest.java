package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsEmptyValuesTest {

 @Test
 public void testToString() {
 MockName mockName = mock(Object.class);
 ReturnsEmptyValues answer = new ReturnsEmptyValues();
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 invocation.setMock(mockName);
 invocation.setMethod(mock(Method.class));
 Object result = answer.answer(invocation);
 assertEquals("Mock for [MockitoMock], hashCode: 42", result.toString());
 }

 @Test
 public void testCompareTo() {
 Object mock1 = mock(Comparable.class);
 Object mock2 = mock(Comparable.class);
 ReturnsEmptyValues answer = new ReturnsEmptyValues();
 InvocationOnMock invocation1 = mock(InvocationOnMock.class);
 invocation1.setMock(mock1);
 invocation1.setArguments(new Object[]{mock2});
 invocation1.setMethod(mock(Method.class));
 InvocationOnMock invocation2 = mock(InvocationOnMock.class);
 invocation2.setMock(mock2);
 invocation2.setArguments(new Object[]{mock1});
 invocation2.setMethod(mock(Method.class));
 int result1 = answer.answer(invocation1).hashCode();
 int result2 = answer.answer(invocation2).hashCode();
 assertEquals(1, result1 - result2);
 }

 @Test
 public void testPrimitiveTypes() {
 ReturnsEmptyValues answer = new ReturnsEmptyValues();
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 Class<?>[] types = new Class[12];
 types[0] = Boolean.TYPE;
 types[1] = Byte.TYPE;
 types[2] = Short.TYPE;
 types[3] = Integer.TYPE;
 types[4] = Long.TYPE;
 types[5] = Float.TYPE;
 types[6] = Double.TYPE;
 types[7] = Character.TYPE;
 types[8] = Byte.class;
 types[9] = Short.class;
 types[10] = Integer.class;
 types[11] = Long.class;
 for (Class<?> type : types) {
 Object result = answer.answer(invocation).answer(type);
 assertEquals(Primitives.defaultValueForPrimitiveOrWrapper(type), result);
 }
 }

 @Test
 public void testEmptyCollectionTypes() {
 ReturnsEmptyValues answer = new ReturnsEmptyValues();
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 Class<?>[] types = new Class[8];
 types[0] = Collection.class;
 types[1] = Set.class;
 types[2] = HashSet.class;
 types[3] = SortedSet.class;
 types[4] = TreeSet.class;
 types[5] = LinkedHashSet.class;
 types[6] = List.class;
 types[7] = LinkedList.class;
 for (Class<?> type : types) {
 Object result = answer.answer(invocation).answer(type);
 assertNotNull(result);
 }
 }

 @Test
 public void testEmptyMapTypes() {
 ReturnsEmptyValues answer = new ReturnsEmptyValues();
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 Class<?>[] types = new Class[7];
 types[0] = Map.class;
 types[1] = HashMap.class;
 types[2] = SortedMap.class;
 types[3] = TreeMap.class;
 types[4] = LinkedHashMap.class;
 types[5] = Map.class;
 types[6] = HashMap.class;
 for (Class<?> type : types) {
 Object result = answer.answer(invocation).answer(type);
 assertNotNull(result);
 }
 }
}