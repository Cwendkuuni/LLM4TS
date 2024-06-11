package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
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
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.invocation;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        Object mock = mock(String.class, Answers.RETURNS_DEEP_STUBS);
        Answer answer = new ReturnsEmptyValues();
        when(mock.toString()).thenAnswer(answer);

        assertEquals("Mock for String, hashCode: " + mock.hashCode(), mock.toString());
        verify(mock).toString();
    }

    @Test
    public void testCompareToMethod() {
        Object mock1 = mock(Comparable.class, Answers.RETURNS_DEEP_STUBS);
        Object mock2 = mock(Comparable.class, Answers.RETURNS_DEEP_STUBS);
        Answer answer = new ReturnsEmptyValues();

        when(mock1.compareTo(mock2)).thenAnswer(answer);

        verify(mock1).compareTo(mock2);
        assertEquals(1, mock1.compareTo(mock2));
    }

    @Test
    public void testPrimitives() {
        Answer answer = new ReturnsEmptyValues();

        byte byteValue = answer.answer(Mockito.mock(Byte.class, Answers.RETURNS_DEEP_STUBS));
        short shortValue = answer.answer(Mockito.mock(Short.class, Answers.RETURNS_DEEP_STUBS));
        int intValue = answer.answer(Mockito.mock(Integer.class, Answers.RETURNS_DEEP_STUBS));
        long longValue = answer.answer(Mockito.mock(Long.class, Answers.RETURNS_DEEP_STUBS));
        float floatValue = answer.answer(Mockito.mock(Float.class, Answers.RETURNS_DEEP_STUBS));
        double doubleValue = answer.answer(Mockito.mock(Double.class, Answers.RETURNS_DEEP_STUBS));
        boolean booleanValue = answer.answer(Mockito.mock(Boolean.class, Answers.RETURNS_DEEP_STUBS));
        char charValue = answer.answer(Mockito.mock(Character.class, Answers.RETURNS_DEEP_STUBS));

        assertEquals(0, byteValue.byteValue());
        assertEquals(0, shortValue.shortValue());
        assertEquals(0, intValue);
        assertEquals(0L, longValue.longValue());
        assertEquals(0.0f, floatValue, 0.0001f);
        assertEquals(0.0d, doubleValue, 0.0001d);
        assertEquals(false, booleanValue);
        assertEquals((char) 0, charValue);
    }

    @Test
    public void testEmptyCollection() {
        Answer answer = new ReturnsEmptyValues();

        Collection<Integer> collection = answer.answer(Mockito.mock(Collection.class, Answers.RETURNS_DEEP_STUBS));

        assertEquals(0, collection.size());
        verify(collection, Mockito.never()).add(any());
        verify(collection, Mockito.never()).remove(any());
        verify(collection, Mockito.never()).clear();
    }

    @Test
    public void testEmptySet() {
        Answer answer = new ReturnsEmptyValues();

        Set<Integer> set = answer.answer(Mockito.mock(Set.class, Answers.RETURNS_DEEP_STUBS));

        assertEquals(0, set.size());
        verify(set, Mockito.never()).add(any());
        verify(set, Mockito.never()).remove(any());
        verify(set, Mockito.never()).clear();
    }

    @Test
    public void testEmptyHashSet() {
        Answer answer = new ReturnsEmptyValues();

        Set<Integer> hashSet = answer.answer(Mockito.mock(HashSet.class, Answers.RETURNS_DEEP_STUBS));

        assertEquals(0, hashSet.size());
        verify(hashSet, Mockito.never()).add(any());
        verify(hashSet, Mockito.never()).remove(any());
        verify(hashSet, Mockito.never()).clear();
    }

    @Test
    public void testEmptyTreeSet() {
        Answer answer = new ReturnsEmptyValues();

        SortedSet<Integer> treeSet = answer.answer(Mockito.mock(TreeSet.class, Answers.RETURNS_DEEP_STUBS));

        assertEquals(0, treeSet.size());
        verify(treeSet, Mockito.never()).add(any());
        verify(treeSet, Mockito.never()).remove(any());
        verify(treeSet, Mockito.never()).clear();
    }

    @Test
    public void testEmptyLinkedHashSet() {
        Answer answer = new ReturnsEmptyValues();

        Set<Integer> linkedHashSet = answer.answer(Mockito.mock(LinkedHashSet.class, Answers.RETURNS_DEEP_STUBS));

        assertEquals(0, linkedHashSet.size());
        verify(linkedHashSet, Mockito.never()).add(any());
        verify(linkedHashSet, Mockito.never()).remove(any());
        verify(linkedHashSet, Mockito.never()).clear();
    }

    @Test
    public void testEmptyList() {
        Answer answer = new ReturnsEmptyValues();

        List<Integer> list = answer.answer(Mockito.mock(List.class, Answers.RETURNS_DEEP_STUBS));

        assertEquals(0, list.size());
        verify(list, Mockito.never()).add(any());
        verify(list, Mockito.never()).remove(any());
        verify(list, Mockito.never()).clear();
    }

    @Test
    public void testEmptyLinkedList() {
        Answer answer = new ReturnsEmptyValues();

        List<Integer> linkedList = answer.answer(Mockito.mock(LinkedList.class, Answers.RETURNS_DEEP_STUBS));

        assertEquals(0, linkedList.size());
        verify(linkedList, Mockito.never()).add(any());
        verify(linkedList, Mockito.never()).remove(any());
        verify(linkedList, Mockito.never()).clear();
    }

    @Test
    public void testEmptyArrayList() {
        Answer answer = new ReturnsEmptyValues();

        List<Integer> arrayList = answer.answer(Mockito.mock(ArrayList.class, Answers.RETURNS_DEEP_STUBS));

        assertEquals(0, arrayList.size());
        verify(arrayList, Mockito.never()).add(any());
        verify(arrayList, Mockito.never()).remove(any());
        verify(arrayList, Mockito.never()).clear();
    }

    @Test
    public void testEmptyMap() {
        Answer answer = new ReturnsEmptyValues();

        Map<String, Integer> map = answer.answer(Mockito.mock(Map.class, Answers.RETURNS_DEEP_STUBS));

        assertEquals(0, map.size());
        verify(map, Mockito.never()).put(any(), any());
        verify(map, Mockito.never()).remove(any());
        verify(map, Mockito.never()).clear();
    }

    @Test
    public void testEmptyHashMap() {
        Answer answer = new ReturnsEmptyValues();

        Map<String, Integer> hashMap = answer.answer(Mockito.mock(HashMap.class, Answers.RETURNS_DEEP_STUBS));

        assertEquals(0, hashMap.size());
        verify(hashMap, Mockito.never()).put(any(), any());
        verify(hashMap, Mockito.never()).remove(any());
        verify(hashMap, Mockito.never()).clear();
    }

    @Test
    public void testEmptyTreeMap() {
        Answer answer = new ReturnsEmptyValues();

        SortedMap<String, Integer> treeMap = answer.answer(Mockito.mock(TreeMap.class, Answers.RETURNS_DEEP_STUBS));

        assertEquals(0, treeMap.size());
        verify(treeMap, Mockito.never()).put(any(), any());
        verify(treeMap, Mockito.never()).remove(any());
        verify(treeMap, Mockito.never()).clear();
    }

    @Test
    public void testEmptyLinkedHashMap() {
        Answer answer = new ReturnsEmptyValues();

        Map<String, Integer> linkedHashMap = answer.answer(Mockito.mock(LinkedHashMap.class, Answers.RETURNS_DEEP_STUBS));

        assertEquals(0, linkedHashMap.size());
        verify(linkedHashMap, Mockito.never()).put(any(), any());
        verify(linkedHashMap, Mockito.never()).remove(any());
        verify(linkedHashMap, Mockito.never()).clear();
    }

    // Add more test cases for missing collections types if needed
}