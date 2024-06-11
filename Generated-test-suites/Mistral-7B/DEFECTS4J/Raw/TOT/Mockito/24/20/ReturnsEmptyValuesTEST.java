package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.ReturnsEmptyValues;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerToStringMethod() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.spell(ReturnsEmptyValues.class);
        Object mock = Mockito.mock(String.class);

        // Test when mock name is default
        assertEquals("Mock for Object, hashCode: " + mock.hashCode(), returnsEmptyValues.answer(Mockito.method(String.class, "toString", (Class[]) null, mock)));

        // Test when mock name is not default
        Mockito.when(mock.toString()).thenReturn("Custom Name");
        Mockito.when(mock.hashCode()).thenReturn(1);
        assertEquals("Custom Name", returnsEmptyValues.answer(Mockito.method(String.class, "toString", (Class[]) null, mock)));
    }

    @Test
    public void testAnswerCompareToMethod() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.spell(ReturnsEmptyValues.class);
        Object mock1 = Mockito.mock(Comparable.class);
        Object mock2 = Mockito.mock(Comparable.class);

        Mockito.when(mock1.compareTo(mock2)).thenReturn(1);

        assertEquals(1, returnsEmptyValues.answer(Mockito.method(Comparable.class, "compareTo", Comparable.class, mock1, mock2)));
    }

    @Test
    public void testAnswerPrimitiveTypes() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.spell(ReturnsEmptyValues.class);

        assertEquals(0, (int) returnsEmptyValues.answer(Mockito.method(int[].class, "length", int[].class, Mockito.any(int[].class))));
        assertEquals(false, (boolean) returnsEmptyValues.answer(Mockito.method(boolean[].class, "isEmpty", boolean[].class, Mockito.any(boolean[].class))));
        assertEquals(0L, (long) returnsEmptyValues.answer(Mockito.method(long[].class, "length", long[].class, Mockito.any(long[].class))));
        assertEquals((byte) 0, (byte) returnsEmptyValues.answer(Mockito.method(byte[].class, "length", byte[].class, Mockito.any(byte[].class))));
        assertEquals(0d, (double) returnsEmptyValues.answer(Mockito.method(double[].class, "length", double[].class, Mockito.any(double[].class))));
        assertEquals(0f, (float) returnsEmptyValues.answer(Mockito.method(float[].class, "length", float[].class, Mockito.any(float[].class))));
        assertEquals(0.0, (double) returnsEmptyValues.answer(Mockito.method(double.class, "doubleValue", Double.class, Mockito.any(Double.class))));
        assertEquals(false, (boolean) returnsEmptyValues.answer(Mockito.method(boolean.class, "booleanValue", Boolean.class, Mockito.any(Boolean.class))));

        // Test for boolean wrapper
        assertEquals(false, (boolean) returnsEmptyValues.answer(Mockito.method(Boolean.class, "booleanValue", Boolean.class, Mockito.any(Boolean.class))));
        assertEquals(false, (boolean) returnsEmptyValues.answer(Mockito.method(Boolean.class, "booleanValue", Boolean.class, Mockito.argument(0, Boolean.class))));

        // Test for integer wrapper
        assertEquals(0, (int) returnsEmptyValues.answer(Mockito.method(Integer.class, "intValue", Integer.class, Mockito.any(Integer.class))));
        assertEquals(0, (int) returnsEmptyValues.answer(Mockito.method(Integer.class, "intValue", Integer.class, Mockito.argument(0, Integer.class))));

        // Test for long wrapper
        assertEquals(0L, (long) returnsEmptyValues.answer(Mockito.method(Long.class, "longValue", Long.class, Mockito.any(Long.class))));
        assertEquals(0L, (long) returnsEmptyValues.answer(Mockito.method(Long.class, "longValue", Long.class, Mockito.argument(0, Long.class))));

        // Test for float wrapper
        assertEquals(0f, (float) returnsEmptyValues.answer(Mockito.method(Float.class, "floatValue", Float.class, Mockito.any(Float.class))));
        assertEquals(0f, (float) returnsEmptyValues.answer(Mockito.method(Float.class, "floatValue", Float.class, Mockito.argument(0, Float.class))));

        // Test for double wrapper
        assertEquals(0d, (double) returnsEmptyValues.answer(Mockito.method(Double.class, "doubleValue", Double.class, Mockito.any(Double.class))));
        assertEquals(0d, (double) returnsEmptyValues.answer(Mockito.method(Double.class, "doubleValue", Double.class, Mockito.argument(0, Double.class))));
    }

    @Test
    public void testAnswerEmptyCollections() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.spell(ReturnsEmptyValues.class);

        // Test for empty Collection
        Collection<?> collection = returnsEmptyValues.answer(Mockito.method(Collection.class, "isEmpty", Collection.class, Mockito.any(Collection.class)));
        assertNull(collection);
        assertEquals(0, collection.size());

        // Test for empty List
        List<?> list = returnsEmptyValues.answer(Mockito.method(List.class, "isEmpty", List.class, Mockito.any(List.class)));
        assertNull(list);
        assertEquals(0, list.size());

        // Test for empty Set
        Set<?> set = returnsEmptyValues.answer(Mockito.method(Set.class, "isEmpty", Set.class, Mockito.any(Set.class)));
        assertNull(set);
        assertEquals(0, set.size());

        // Test for empty HashSet
        Set<Integer> hashSet = returnsEmptyValues.answer(Mockito.method(HashSet.class, "isEmpty", HashSet.class, Mockito.any(HashSet.class)));
        assertNull(hashSet);
        assertEquals(0, hashSet.size());

        // Test for empty LinkedHashSet
        Set<Integer> linkedHashSet = returnsEmptyValues.answer(Mockito.method(LinkedHashSet.class, "isEmpty", LinkedHashSet.class, Mockito.any(LinkedHashSet.class)));
        assertNull(linkedHashSet);
        assertEquals(0, linkedHashSet.size());

        // Test for empty TreeSet
        Set<Integer> treeSet = returnsEmptyValues.answer(Mockito.method(TreeSet.class, "isEmpty", TreeSet.class, Mockito.any(TreeSet.class)));
        assertNull(treeSet);
        assertEquals(0, treeSet.size());

        // Test for empty ArrayList
        List<Integer> arrayList = returnsEmptyValues.answer(Mockito.method(ArrayList.class, "isEmpty", ArrayList.class, Mockito.any(ArrayList.class)));
        assertNull(arrayList);
        assertEquals(0, arrayList.size());

        // Test for empty LinkedList
        List<Integer> linkedList = returnsEmptyValues.answer(Mockito.method(LinkedList.class, "isEmpty", LinkedList.class, Mockito.any(LinkedList.class)));
        assertNull(linkedList);
        assertEquals(0, linkedList.size());

        // Test for empty HashMap
        Map<String, Integer> map = returnsEmptyValues.answer(Mockito.method(HashMap.class, "isEmpty", HashMap.class, Mockito.any(HashMap.class)));
        assertNull(map);
        assertEquals(0, map.size());

        // Test for empty LinkedHashMap
        Map<String, Integer> linkedMap = returnsEmptyValues.answer(Mockito.method(LinkedHashMap.class, "isEmpty", LinkedHashMap.class, Mockito.any(LinkedHashMap.class)));
        assertNull(linkedMap);
        assertEquals(0, linkedMap.size());

        // Test for empty TreeMap
        Map<String, Integer> treeMap = returnsEmptyValues.answer(Mockito.method(TreeMap.class, "isEmpty", TreeMap.class, Mockito.any(TreeMap.class)));
        assertNull(treeMap);
        assertEquals(0, treeMap.size());
    }

    @Test
    public void testAnswerOtherTypes() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.spell(ReturnsEmptyValues.class);

        // Test for empty array
        Object[] array = returnsEmptyValues.answer(Mockito.method(Object[].class, "length", Object[].class, Mockito.any(Object[].class)));
        assertNull(array);

        // Test for empty Iterator
        Iterator<?> iterator = (Iterator<?>) returnsEmptyValues.answer(Mockito.method(Iterator.class, "hasNext", Iterator.class, Mockito.any(Iterator.class)));
        assertFalse(iterator.hasNext());

        // Test for empty Iterable
        Iterable<?> iterable = (Iterable<?>) returnsEmptyValues.answer(Mockito.method(Iterable.class, "iterator", Iterable.class, Mockito.any(Iterable.class)));
        assertNull(iterable.iterator());

        // Test for empty Map.Entry
        Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) returnsEmptyValues.answer(Mockito.method(Map.Entry.class, "getValue", Map.Entry.class, Mockito.any(Map.Entry.class)));
        assertNull(entry.getValue());
    }
}