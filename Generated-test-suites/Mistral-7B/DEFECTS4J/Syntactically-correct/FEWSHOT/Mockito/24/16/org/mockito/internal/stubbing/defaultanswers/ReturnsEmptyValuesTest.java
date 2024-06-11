package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.internal.util.Primitives.isPrimitiveOrWrapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

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

public class ReturnsEmptyValuesTest {
    private ReturnsEmptyValues returnsEmptyValues;

    @Before
    public void setUp() {
        returnsEmptyValues = new ReturnsEmptyValues();
    }

    @Test
    public void testAnswerToStringMethod() {
        Object mock = mock(Object.class);
        InvocationOnMock invocation = Mockito.method(Object.class, "toString");

        assertEquals("Mock for [class org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues], hashCode: 1365564155", returnsEmptyValues.answer(invocation));
    }

    @Test
    public void testAnswerCompareToMethod() {
        Object obj1 = mock(Comparable.class);
        Object obj2 = mock(Comparable.class);

        assertEquals(1, returnsEmptyValues.answer(Mockito.method(Comparable.class, "compareTo", Object.class).invoke(obj1, obj2)));
    }

    @Test
    public void testAnswerPrimitiveType() {
        Class<Byte> byteType = Byte.TYPE;
        Object primitiveValue = returnsEmptyValues.returnValueFor(byteType);
        assertEquals((byte) 0, primitiveValue);

        Class<Integer> intType = Integer.TYPE;
        Object primitiveWrapperValue = returnsEmptyValues.returnValueFor(intType);
        assertEquals(0, primitiveWrapperValue);
    }

    @Test
    public void testAnswerCollectionType() {
        Class<Collection<String>> collectionType = Collection.class;
        Object collectionValue = returnsEmptyValues.returnValueFor(collectionType);
        assertTrue(collectionValue instanceof Collection);
        assertTrue(collectionValue instanceof LinkedList);
        assertEquals(0, ((Collection<?>) collectionValue).size());
    }

    @Test
    public void testAnswerSetType() {
        Class<Set<String>> setType = Set.class;
        Object setValue = returnsEmptyValues.returnValueFor(setType);
        assertTrue(setValue instanceof Set);
        assertTrue(setValue instanceof HashSet);
        assertEquals(0, ((Set<?>) setValue).size());
    }

    @Test
    public void testAnswerHashSetType() {
        Class<HashSet<String>> hashSetType = HashSet.class;
        Object hashSetValue = returnsEmptyValues.returnValueFor(hashSetType);
        assertTrue(hashSetValue instanceof Set);
        assertTrue(hashSetValue instanceof HashSet);
        assertEquals(0, ((Set<?>) hashSetValue).size());
    }

    @Test
    public void testAnswerSortedSetType() {
        Class<SortedSet<String>> sortedSetType = SortedSet.class;
        Object sortedSetValue = returnsEmptyValues.returnValueFor(sortedSetType);
        assertTrue(sortedSetValue instanceof SortedSet);
        assertTrue(sortedSetValue instanceof TreeSet);
        assertEquals(0, ((SortedSet<?>) sortedSetValue).size());
    }

    @Test
    public void testAnswerTreeSetType() {
        Class<TreeSet<String>> treeSetType = TreeSet.class;
        Object treeSetValue = returnsEmptyValues.returnValueFor(treeSetType);
        assertTrue(treeSetValue instanceof SortedSet);
        assertTrue(treeSetValue instanceof TreeSet);
        assertEquals(0, ((SortedSet<?>) treeSetValue).size());
    }

    @Test
    public void testAnswerLinkedHashSetType() {
        Class<LinkedHashSet<String>> linkedHashSetType = LinkedHashSet.class;
        Object linkedHashSetValue = returnsEmptyValues.returnValueFor(linkedHashSetType);
        assertTrue(linkedHashSetValue instanceof Set);
        assertTrue(linkedHashSetValue instanceof LinkedHashSet);
        assertEquals(0, ((Set<?>) linkedHashSetValue).size());
    }

    @Test
    public void testAnswerListType() {
        Class<List<String>> listType = List.class;
        Object listValue = returnsEmptyValues.returnValueFor(listType);
        assertTrue(listValue instanceof List);
        assertTrue(listValue instanceof LinkedList);
        assertEquals(0, ((List<?>) listValue).size());
    }

    @Test
    public void testAnswerLinkedListType() {
        Class<LinkedList<String>> linkedListType = LinkedList.class;
        Object linkedListValue = returnsEmptyValues.returnValueFor(linkedListType);
        assertTrue(linkedListValue instanceof List);
        assertTrue(linkedListValue instanceof LinkedList);
        assertEquals(0, ((List<?>) linkedListValue).size());
    }

    @Test
    public void testAnswerArrayListType() {
        Class<ArrayList<String>> arrayListType = ArrayList.class;
        Object arrayListValue = returnsEmptyValues.returnValueFor(arrayListType);
        assertTrue(arrayListValue instanceof List);
        assertTrue(arrayListValue instanceof ArrayList);
        assertEquals(0, ((List<?>) arrayListValue).size());
    }

    @Test
    public void testAnswerMapType() {
        Class<Map<String, String>> mapType = Map.class;
        Object mapValue = returnsEmptyValues.returnValueFor(mapType);
        assertTrue(mapValue instanceof Map);
        assertTrue(mapValue instanceof HashMap);
        assertEquals(0, ((Map<?, ?>) mapValue).size());
    }

    @Test
    public void testAnswerHashMapType() {
        Class<HashMap<String, String>> hashMapType = HashMap.class;
        Object hashMapValue = returnsEmptyValues.returnValueFor(hashMapType);
        assertTrue(hashMapValue instanceof Map);
        assertTrue(hashMapValue instanceof HashMap);
        assertEquals(0, ((Map<?, ?>) hashMapValue).size());
    }

    @Test
    public void testAnswerSortedMapType() {
        Class<SortedMap<String, String>> sortedMapType = SortedMap.class;
        Object sortedMapValue = returnsEmptyValues.returnValueFor(sortedMapType);
        assertTrue(sortedMapValue instanceof SortedMap);
        assertTrue(sortedMapValue instanceof TreeMap);
        assertEquals(0, ((SortedMap<?, ?>) sortedMapValue).size());
    }

    @Test
    public void testAnswerTreeMapType() {
        Class<TreeMap<String, String>> treeMapType = TreeMap.class;
        Object treeMapValue = returnsEmptyValues.returnValueFor(treeMapType);
        assertTrue(treeMapValue instanceof SortedMap);
        assertTrue(treeMapValue instanceof TreeMap);
        assertEquals(0, ((SortedMap<?, ?>) treeMapValue).size());
    }

    @Test
    public void testAnswerLinkedHashMapType() {
        Class<LinkedHashMap<String, String>> linkedHashMapType = LinkedHashMap.class;
        Object linkedHashMapValue = returnsEmptyValues.returnValueFor(linkedHashMapType);
        assertTrue(linkedHashMapValue instanceof Map);
        assertTrue(linkedHashMapValue instanceof LinkedHashMap);
        assertEquals(0, ((Map<?, ?>) linkedHashMapValue).size());
    }
}