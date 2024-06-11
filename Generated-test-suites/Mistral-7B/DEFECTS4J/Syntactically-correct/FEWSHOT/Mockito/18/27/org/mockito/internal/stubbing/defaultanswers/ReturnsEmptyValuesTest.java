package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;

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

    @Test
    public void testAnswer_toString() {
        Object mock = new Object();
        InvocationOnMock invocation = MockitoTestUtils.createInvocationOnMock(mock, "toString");

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object answer = returnsEmptyValues.answer(invocation);

        assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), answer);
    }

    @Test
    public void testAnswer_compareTo() {
        Object mock1 = new Object();
        Object mock2 = new Object();

        InvocationOnMock invocation = MockitoTestUtils.createInvocationOnMock(mock1, "compareTo", mock2);

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        int answer = ((Number) returnsEmptyValues.answer(invocation)).intValue();

        assertEquals(1, answer);
    }

    @Test
    public void testReturnValueFor_primitive() {
        Class<?> intType = int.class;

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object answer = returnsEmptyValues.returnValueFor(intType);

        assertEquals(0, ((Number) answer).intValue());
    }

    @Test
    public void testReturnValueFor_primitiveWrapper() {
        Class<?> intWrapperType = Integer.class;

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object answer = returnsEmptyValues.returnValueFor(intWrapperType);

        assertEquals(0, ((Number) answer).intValue());
    }

    @Test
    public void testReturnValueFor_Collection() {
        Class<?> collectionType = Collection.class;

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Collection<?> answer = (Collection<?>) returnsEmptyValues.returnValueFor(collectionType);

        assertTrue(answer.isEmpty());
    }

    @Test
    public void testReturnValueFor_Set() {
        Class<?> setType = Set.class;

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Set<?> answer = (Set<?>) returnsEmptyValues.returnValueFor(setType);

        assertTrue(answer.isEmpty());
    }

    @Test
    public void testReturnValueFor_HashSet() {
        Class<?> hashSetType = HashSet.class;

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Set<?> answer = (Set<?>) returnsEmptyValues.returnValueFor(hashSetType);

        assertTrue(answer.isEmpty());
    }

    @Test
    public void testReturnValueFor_SortedSet() {
        Class<?> sortedSetType = SortedSet.class;

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        SortedSet<?> answer = (SortedSet<?>) returnsEmptyValues.returnValueFor(sortedSetType);

        assertTrue(answer.isEmpty());
    }

    @Test
    public void testReturnValueFor_TreeSet() {
        Class<?> treeSetType = TreeSet.class;

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        SortedSet<?> answer = (SortedSet<?>) returnsEmptyValues.returnValueFor(treeSetType);

        assertTrue(answer.isEmpty());
    }

    @Test
    public void testReturnValueFor_LinkedHashSet() {
        Class<?> linkedHashSetType = LinkedHashSet.class;

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Set<?> answer = (Set<?>) returnsEmptyValues.returnValueFor(linkedHashSetType);

        assertTrue(answer.isEmpty());
    }

    @Test
    public void testReturnValueFor_List() {
        Class<?> listType = List.class;

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        List<?> answer = (List<?>) returnsEmptyValues.returnValueFor(listType);

        assertTrue(answer.isEmpty());
    }

    @Test
    public void testReturnValueFor_LinkedList() {
        Class<?> linkedListType = LinkedList.class;

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        List<?> answer = (List<?>) returnsEmptyValues.returnValueFor(linkedListType);

        assertTrue(answer.isEmpty());
    }

    @Test
    public void testReturnValueFor_ArrayList() {
        Class<?> arrayListType = ArrayList.class;

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        List<?> answer = (List<?>) returnsEmptyValues.returnValueFor(arrayListType);

        assertTrue(answer.isEmpty());
    }

    @Test
    public void testReturnValueFor_Map() {
        Class<?> mapType = Map.class;

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Map<?, ?> answer = (Map<?, ?>) returnsEmptyValues.returnValueFor(mapType);

        assertTrue(answer.isEmpty());
    }

    @Test
    public void testReturnValueFor_HashMap() {
        Class<?> hashMapType = HashMap.class;

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Map<?, ?> answer = (Map<?, ?>) returnsEmptyValues.returnValueFor(hashMapType);

        assertTrue(answer.isEmpty());
    }

    @Test
    public void testReturnValueFor_SortedMap() {
        Class<?> sortedMapType = SortedMap.class;

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        SortedMap<?, ?> answer = (SortedMap<?, ?>) returnsEmptyValues.returnValueFor(sortedMapType);

        assertTrue(answer.isEmpty());
    }

    @Test
    public void testReturnValueFor_TreeMap() {
        Class<?> treeMapType = TreeMap.class;

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        SortedMap<?, ?> answer = (SortedMap<?, ?>) returnsEmptyValues.returnValueFor(treeMapType);

        assertTrue(answer.isEmpty());
    }

    @Test
    public void testReturnValueFor_LinkedHashMap() {
        Class<?> linkedHashMapType = LinkedHashMap.class;

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Map<?, ?> answer = (Map<?, ?>) returnsEmptyValues.returnValueFor(linkedHashMapType);

        assertTrue(answer.isEmpty());
    }
}