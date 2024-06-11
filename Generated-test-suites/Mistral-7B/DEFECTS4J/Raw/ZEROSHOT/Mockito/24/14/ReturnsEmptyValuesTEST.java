package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues returnsEmptyValues;
 private ObjectMethodsGuru methodsGuru;
 private MockUtil mockUtil;

 @Before
 public void setup() {
 methodsGuru = new ObjectMethodsGuru();
 mockUtil = new MockUtil();
 returnsEmptyValues = new ReturnsEmptyValues();
 }

 @Test
 public void testToStringAnswer() {
 Mockito mock = mock(String.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 when(invocation.getMethod()).thenReturn(Mockito.method(String.class, "toString"));
 when(mockUtil.getMockName(mock)).thenReturn(new MockName("MockName"));

 assertEquals("Mock for String, hashCode: " + mock.hashCode(), returnsEmptyValues.answer(invocation));
 }

 @Test
 public void testCompareToAnswer() {
 Object mock = mock(Comparable.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 when(invocation.getMethod()).thenReturn(Mockito.method(Comparable.class, "compareTo", Object.class));
 when(methodsGuru.isCompareToMethod(invocation.getMethod())).thenReturn(true);

 assertEquals(1, returnsEmptyValues.answer(invocation));
 }

 @Test
 public void testPrimitiveAndWrapperTypes() {
 for (Class<?> type : new Class[] { byte.class, short.class, int.class, long.class, boolean.class, char.class, float.class, double.class, Byte.class, Short.class, Integer.class, Long.class, Boolean.class, Character.class, Float.class, Double.class }) {
 Object answer = returnsEmptyValues.returnValueFor(type);
 assertNotNull(answer);
 assertEquals(Primitives.defaultValueForPrimitiveOrWrapper(type), answer);
 }
}

 @Test
 public void testEmptyCollectionAnswers() {
 for (Class<? extends Collection<?>> collectionType : new Class[] { Collection.class, HashSet.class, TreeSet.class, LinkedHashSet.class, List.class, LinkedList.class, ArrayList.class, LinkedHashMap.class, TreeMap.class }) {
 Object answer = returnsEmptyValues.returnValueFor(collectionType);
 assertNotNull(answer);
 if (collectionType.isInstance(new ArrayList<>())) {
 assertEquals(new ArrayList<>(), answer);
 } else {
 assertEquals(new Collections.emptyList(), answer);
 }
 }
}

 @Test
 public void testOtherTypes() {
 for (Class<?> type : new Class[] { Map.class, HashMap.class, SortedMap.class, TreeMap.class, LinkedHashMap.class }) {
 Object answer = returnsEmptyValues.returnValueFor(type);
 assertNotNull(answer);
 assertEquals(new HashMap<>(), answer);
 }
}

}