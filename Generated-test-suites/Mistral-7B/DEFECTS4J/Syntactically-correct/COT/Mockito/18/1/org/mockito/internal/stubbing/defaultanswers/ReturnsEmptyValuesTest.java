package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.Serializable;
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void test_answer_toStringMethod() {
        // Given
        Object mock = Mockito.mock(String.class);

        // When
        String result = (String) new ReturnsEmptyValues().answer(Mockito.method(String.class, "toString"));

        // Then
        assertEquals("Mock for String, hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void test_answer_compareToMethod() {
        // Given
        Object obj1 = Mockito.mock(Comparable.class);
        Object obj2 = Mockito.mock(Comparable.class);

        // When
        int result = (Integer) new ReturnsEmptyValues().answer(Mockito.method(Comparable.class, "compareTo", Object.class)).intValue();

        // Then
        assertFalse(obj1.equals(obj2));
        assertEquals(1, result);
    }

    @Test
    public void test_returnValueFor_primitiveOrWrapperTypes() {
        // Given
        Class<?> type = boolean.class;

        // When
        Object result = new ReturnsEmptyValues().returnValueFor(type);

        // Then
        assertEquals(false, result);
    }

    @Test
    public void test_returnValueFor_Collection() {
        // Given
        Class<?> type = Collection.class;

        // When
        Collection<Object> result = (Collection<Object>) new ReturnsEmptyValues().returnValueFor(type);

        // Then
        assertTrue(result instanceof LinkedList);
        assertEquals(0, result.size());
    }

    @Test
    public void test_returnValueFor_Set() {
        // Given
        Class<?> type = Set.class;

        // When
        Set<Object> result = (Set<Object>) new ReturnsEmptyValues().returnValueFor(type);

        // Then
        assertTrue(result instanceof HashSet);
        assertEquals(0, result.size());
    }

    @Test
    public void test_returnValueFor_SortedSet() {
        // Given
        Class<?> type = SortedSet.class;

        // When
        SortedSet<Object> result = (SortedSet<Object>) new ReturnsEmptyValues().returnValueFor(type);

        // Then
        assertTrue(result instanceof TreeSet);
        assertEquals(0, result.size());
    }

    @Test
    public void test_returnValueFor_LinkedHashSet() {
        // Given
        Class<?> type = LinkedHashSet.class;

        // When
        Set<Object> result = (Set<Object>) new ReturnsEmptyValues().returnValueFor(type);

        // Then
        assertTrue(result instanceof LinkedHashSet);
        assertEquals(0, result.size());
    }

    @Test
    public void test_returnValueFor_List() {
        // Given
        Class<?> type = List.class;

        // When
        List<Object> result = (List<Object>) new ReturnsEmptyValues().returnValueFor(type);

        // Then
        assertTrue(result instanceof LinkedList);
        assertEquals(0, result.size());
    }

    @Test
    public void test_returnValueFor_LinkedList() {
        // Given
        Class<?> type = LinkedList.class;

        // When
        List<Object> result = (List<Object>) new ReturnsEmptyValues().returnValueFor(type);

        // Then
        assertTrue(result instanceof LinkedList);
        assertEquals(0, result.size());
    }

    @Test
    public void test_returnValueFor_ArrayList() {
        // Given
        Class<?> type = ArrayList.class;

        // When
        List<Object> result = (List<Object>) new ReturnsEmptyValues().returnValueFor(type);

        // Then
        assertTrue(result instanceof ArrayList);
        assertEquals(0, result.size());
    }

    @Test
    public void test_returnValueFor_Map() {
        // Given
        Class<?> type = Map.class;

        // When
        Map<Object, Object> result = (Map<Object, Object>) new ReturnsEmptyValues().returnValueFor(type);

        // Then
        assertTrue(result instanceof HashMap);
        assertEquals(0, result.size());
    }

    @Test
    public void test_returnValueFor_HashMap() {
        // Given
        Class<?> type = HashMap.class;

        // When
        Map<Object, Object> result = (Map<Object, Object>) new ReturnsEmptyValues().returnValueFor(type);

        // Then
        assertTrue(result instanceof HashMap);
        assertEquals(0, result.size());
    }

    @Test
    public void test_returnValueFor_SortedMap() {
        // Given
        Class<?> type = SortedMap.class;

        // When
        SortedMap<Object, Object> result = (SortedMap<Object, Object>) new ReturnsEmptyValues().returnValueFor(type);

        // Then
        assertTrue(result instanceof TreeMap);
        assertEquals(0, result.size());
    }

    @Test
    public void test_returnValueFor_TreeMap() {
        // Given
        Class<?> type = TreeMap.class;

        // When
        SortedMap<Object, Object> result = (SortedMap<Object, Object>) new ReturnsEmptyValues().returnValueFor(type);

        // Then
        assertTrue(result instanceof TreeMap);
        assertEquals(0, result.size());
    }

    @Test
    public void test_returnValueFor_LinkedHashMap() {
        // Given
        Class<?> type = LinkedHashMap.class;

        // When
        Map<Object, Object> result = (Map<Object, Object>) new ReturnsEmptyValues().returnValueFor(type);

        // Then
        assertTrue(result instanceof LinkedHashMap);
        assertEquals(0, result.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void test_returnValueFor_UnsupportedCollectionType() {
        // Given
        Class<?> type = Stack.class;

        // When
        Collection<Object> result = (Collection<Object>) new ReturnsEmptyValues().returnValueFor(type);

        // Then
        // Exception is expected because Stack is not supported
    }
}