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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsEmptyValuesTest {

 @Test
 public void testAnswerToStringMethod() {
 InvocationOnMock invocation = MockitoTestUtils.createMockInvocation("toString");
 Object answer = new ReturnsEmptyValues().answer(invocation);
 assertEquals("Mock for Object, hashCode: 151021331", answer);
 }

 @Test
 public void testAnswerCompareToMethod() {
 InvocationOnMock invocation = MockitoTestUtils.createMockInvocation("compareTo", "other");
 Object answer = new ReturnsEmptyValues().answer(invocation);
 assertEquals(1, answer);
 }

 @Test
 public void testReturnValueForPrimitiveTypes() {
 Class<?> intType = int.class;
 Object answer = new ReturnsEmptyValues().returnValueFor(intType);
 assertEquals(0, answer);

 Class<?> booleanType = boolean.class;
 Object answerBoolean = new ReturnsEmptyValues().returnValueFor(booleanType);
 assertEquals(false, answerBoolean);
 }

 @Test
 public void testReturnValueForCollectionTypes() {
 Class<?> collectionType = Collection.class;
 Object answerCollection = new ReturnsEmptyValues().returnValueFor(collectionType);
 assertNotNull(answerCollection);
 assertEquals(0, ((Collection<?>) answerCollection).size());

 Class<?> listType = List.class;
 Object answerList = new ReturnsEmptyValues().returnValueFor(listType);
 assertNotNull(answerList);
 assertEquals(0, ((List<?>) answerList).size());

 Class<?> linkedListType = LinkedList.class;
 Object answerLinkedList = new ReturnsEmptyValues().returnValueFor(linkedListType);
 assertNotNull(answerLinkedList);
 assertEquals(0, ((List<?>) answerLinkedList).size());

 Class<?> hashSetType = HashSet.class;
 Object answerHashSet = new ReturnsEmptyValues().returnValueFor(hashSetType);
 assertNotNull(answerHashSet);
 assertEquals(0, ((Set<?>) answerHashSet).size());

 Class<?> linkedHashSetType = LinkedHashSet.class;
 Object answerLinkedHashSet = new ReturnsEmptyValues().returnValueFor(linkedHashSetType);
 assertNotNull(answerLinkedHashSet);
 assertEquals(0, ((Set<?>) answerLinkedHashSet).size());

 Class<?> treeSetType = TreeSet.class;
 Object answerTreeSet = new ReturnsEmptyValues().returnValueFor(treeSetType);
 assertNotNull(answerTreeSet);
 assertEquals(0, ((SortedSet<?>) answerTreeSet).size());

 Class<?> arrayListType = ArrayList.class;
 Object answerArrayList = new ReturnsEmptyValues().returnValueFor(arrayListType);
 assertNotNull(answerArrayList);
 assertEquals(0, ((List<?>) answerArrayList).size());
 }

 @Test
 public void testReturnValueForMapTypes() {
 Class<?> mapType = Map.class;
 Object answerMap = new ReturnsEmptyValues().returnValueFor(mapType);
 assertNotNull(answerMap);
 assertEquals(0, ((Map<?, ?>) answerMap).size());

 Class<?> hashMapType = HashMap.class;
 Object answerHashMap = new ReturnsEmptyValues().returnValueFor(hashMapType);
 assertNotNull(answerHashMap);
 assertEquals(0, ((Map<?, ?>) answerHashMap).size());

 Class<?> treeMapType = TreeMap.class;
 Object answerTreeMap = new ReturnsEmptyValues().returnValueFor(treeMapType);
 assertNotNull(answerTreeMap);
 assertEquals(0, ((SortedMap<?, ?>) answerTreeMap).size());

 Class<?> linkedHashMapType = LinkedHashMap.class;
 Object answerLinkedHashMap = new ReturnsEmptyValues().returnValueFor(linkedHashMapType);
 assertNotNull(answerLinkedHashMap);
 assertEquals(0, ((Map<?, ?>) answerLinkedHashMap).size());
 }
}

class MockitoTestUtils {
 static InvocationOnMock createMockInvocation(String methodName, Object... arguments) {
 InvocationOnMock invocation = Mockito.mock(Object.class);
 Mockito.when(invocation.getMethod()).thenReturn(ReflectionUtils.getMethod(Object.class, methodName, arguments));
 return invocation;
 }
}