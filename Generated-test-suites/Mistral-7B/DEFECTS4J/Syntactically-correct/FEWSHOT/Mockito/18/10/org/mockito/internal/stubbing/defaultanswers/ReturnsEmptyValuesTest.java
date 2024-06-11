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
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsEmptyValuesTest {

 @Test
 public void testToStringAnswer() {
 MockName mockName = MockName.parse("myMockName");
 InvocationOnMock invocation = Mockito.mock(String.class).toInvocationOnMock();
 ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 assertEquals("Mock for String, hashCode: 1452221831", returnsEmptyValues.answer(invocation));

 String mock = Mockito.mock(String.class);
 assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), returnsEmptyValues.answer(new InvocationOnMock(mock.getClass(), null, null, new Object[0])));

 assertEquals(mockName.toString(), returnsEmptyValues.answer(new InvocationOnMock(String.class, null, null, new Object[]{mockName})));
 }

 @Test
 public void testCompareToAnswer() {
 Object mock1 = Mockito.mock(String.class);
 Object mock2 = Mockito.mock(String.class);

 InvocationOnMock invocation1 = new InvocationOnMock(Comparable.class, "compareTo", new Class[]{Comparable.class}, new Object[]{mock1});
 InvocationOnMock invocation2 = new InvocationOnMock(Comparable.class, "compareTo", new Class[]{Comparable.class}, new Object[]{mock2});

 ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 assertEquals(0, (int) returnsEmptyValues.answer(invocation1));
 assertNull(returnsEmptyValues.answer(invocation2));
 }

 @Test
 public void testPrimitiveAnswer() {
 Class<?> intType = int.class;
 Class<?> booleanType = boolean.class;

 ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 assertEquals(0, (int) returnsEmptyValues.answer(new InvocationOnMock(intType, "valueOf", new Class[0], null)));
 assertEquals(false, (boolean) returnsEmptyValues.answer(new InvocationOnMock(booleanType, "valueOf", new Class[0], null)));
 }

 @Test
 public void testEmptyCollectionAnswer() {
 InvocationOnMock invocation = Mockito.mock(Collection.class).toInvocationOnMock();

 ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 assertEquals(new ArrayList<>(), returnsEmptyValues.answer(invocation));
 }

 @Test
 public void testEmptySetAnswer() {
 InvocationOnMock invocation = Mockito.mock(Set.class).toInvocationOnMock();

 ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 assertEquals(new HashSet<>(), returnsEmptyValues.answer(invocation));
 }

 @Test
 public void testEmptyHashSetAnswer() {
 InvocationOnMock invocation = Mockito.mock(HashSet.class).toInvocationOnMock();

 ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 assertEquals(new HashSet<>(), returnsEmptyValues.answer(invocation));
 }

 @Test
 public void testEmptyTreeSetAnswer() {
 InvocationOnMock invocation = Mockito.mock(SortedSet.class).toInvocationOnMock();

 ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 assertEquals(new TreeSet<>(), returnsEmptyValues.answer(invocation));
 }

 @Test
 public void testEmptyTreeMapAnswer() {
 InvocationOnMock invocation = Mockito.mock(SortedMap.class).toInvocationOnMock();

 ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 assertEquals(new TreeMap<>(), returnsEmptyValues.answer(invocation));
 }

 @Test
 public void testEmptyHashMapAnswer() {
 InvocationOnMock invocation = Mockito.mock(Map.class).toInvocationOnMock();

 ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 assertEquals(new HashMap<>(), returnsEmptyValues.answer(invocation));
 }

 @Test
 public void testEmptyLinkedHashMapAnswer() {
 InvocationOnMock invocation = Mockito.mock(LinkedHashMap.class).toInvocationOnMock();

 ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 assertEquals(new LinkedHashMap<>(), returnsEmptyValues.answer(invocation));
 }

 @Test
 public void testEmptyLinkedListAnswer() {
 InvocationOnMock invocation = Mockito.mock(List.class).toInvocationOnMock();

 ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 assertEquals(new LinkedList<>(), returnsEmptyValues.answer(invocation));
 }

 @Test
 public void testEmptyLinkedListAnswer2() {
 InvocationOnMock invocation = Mockito.mock(LinkedList.class).toInvocationOnMock();

 ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 assertEquals(new LinkedList<>(), returnsEmptyValues.answer(invocation));
 }

 @Test
 public void testEmptyArrayListAnswer() {
 InvocationOnMock invocation = Mockito.mock(List.class).toInvocationOnMock();

 ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 assertEquals(new ArrayList<>(), returnsEmptyValues.answer(invocation));
 }
}