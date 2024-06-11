package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.junit.runner.RunWith;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.JUnit4.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringReturnValue() {
        // Given
        Object mock = mock(String.class);
        Answer answer = new ReturnsEmptyValues();

        // When
        String result = (String) answer.answer(new InvocationOnMock(mock, Mockito.method(String.class, "toString")));

        // Then
        verify(mock).toString();
        assertNotNull(result);
        assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void testCompareToMethodReturnValue() {
        // Given
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Answer answer = new ReturnsEmptyValues();

        // When
        when(mock1.toString()).thenReturn("mock1");
        when(mock2.toString()).thenReturn("mock2");
        int result = (int) answer.answer(new InvocationOnMock(mock1, Mockito.method(Object.class, "compareTo", ArgumentMatchers.any(Object.class))));

        // Then
        verify(mock1).compareTo(mock2);
        assertEquals(1, result);
    }

    @Test
    public void testEmptyCollectionReturnValue() {
        // Given
        Answer answer = new ReturnsEmptyValues();

        // When
        Class<?> collectionType = Collection.class;
        Object result = answer.returnValueFor(collectionType);

        // Then
        assertTrue(result instanceof Collection);
        assertTrue(result instanceof LinkedList);
        assertFalse(result.isEmpty());
        assertEquals(0, ((Collection) result).size());
    }

    @Test
    public void testEmptySetReturnValue() {
        // Given
        Answer answer = new ReturnsEmptyValues();

        // When
        Class<?> setType = Set.class;
        Object result = answer.returnValueFor(setType);

        // Then
        assertTrue(result instanceof Set);
        assertTrue(result instanceof HashSet);
        assertFalse(result.isEmpty());
        assertEquals(0, ((Set) result).size());
    }

    @Test
    public void testEmptyHashSetReturnValue() {
        // Given
        Answer answer = new ReturnsEmptyValues();

        // When
        Class<?> hashSetType = HashSet.class;
        Object result = answer.returnValueFor(hashSetType);

        // Then
        assertTrue(result instanceof Set);
        assertTrue(result instanceof HashSet);
        assertFalse(result.isEmpty());
        assertEquals(0, ((Set) result).size());
    }

    @Test
    public void testEmptyTreeSetReturnValue() {
        // Given
        Answer answer = new ReturnsEmptyValues();

        // When
        Class<?> treeSetType = TreeSet.class;
        Object result = answer.returnValueFor(treeSetType);

        // Then
        assertTrue(result instanceof SortedSet);
        assertTrue(result instanceof TreeSet);
        assertFalse(result.isEmpty());
        assertEquals(0, ((SortedSet) result).size());
    }

    @Test
    public void testEmptyLinkedHashSetReturnValue() {
        // Given
        Answer answer = new ReturnsEmptyValues();

        // When
        Class<?> linkedHashSetType = LinkedHashSet.class;
        Object result = answer.returnValueFor(linkedHashSetType);

        // Then
        assertTrue(result instanceof Set);
        assertTrue(result instanceof LinkedHashSet);
        assertFalse(result.isEmpty());
        assertEquals(0, ((Set) result).size());
    }

    @Test
    public void testEmptyListReturnValue() {
        // Given
        Answer answer = new ReturnsEmptyValues();

        // When
        Class<?> listType = List.class;
        Object result = answer.returnValueFor(listType);

        // Then
        assertTrue(result instanceof List);
        assertTrue(result instanceof LinkedList);
        assertFalse(result.isEmpty());
        assertEquals(0, ((List) result).size());
    }

    @Test
    public void testEmptyLinkedListReturnValue() {
        // Given
        Answer answer = new ReturnsEmptyValues();

        // When
        Class<?> linkedListType = LinkedList.class;
        Object result = answer.returnValueFor(linkedListType);

        // Then
        assertTrue(result instanceof List);
        assertTrue(result instanceof LinkedList);
        assertFalse(result.isEmpty());
        assertEquals(0, ((List) result).size());
    }

    @Test
    public void testEmptyArrayListReturnValue() {
        // Given
        Answer answer = new ReturnsEmptyValues();

        // When
        Class<?> arrayListType = ArrayList.class;
        Object result = answer.returnValueFor(arrayListType);

        // Then
        assertTrue(result instanceof List);
        assertTrue(result instanceof ArrayList);
        assertFalse(result.isEmpty());
        assertEquals(0, ((List) result).size());
    }

    @Test
    public void testEmptyHashMapReturnValue() {
        // Given
        Answer answer = new ReturnsEmptyValues();

        // When
        Class<?> hashMapType = Map.class;
        Object result = answer.returnValueFor(hashMapType);

        // Then
        assertTrue(result instanceof Map);
        assertTrue(result instanceof HashMap);
        assertFalse(result.isEmpty());
        assertEquals(0, ((Map) result).size());
    }

    @Test
    public void testEmptyHashMapReturnValueWithGenericType() {
        // Given
        Answer answer = new ReturnsEmptyValues();

        // When
        Class<?> hashMapType = Map.class;
        Class<?> keyType = String.class;
        Class<?> valueType = Integer.class;
        Object result = answer.returnValueFor(Map.class, keyType, valueType);

        // Then
        assertTrue(result instanceof Map);
        assertTrue(result instanceof HashMap);
        assertTrue(result.getClass().isAssignableFrom(Map.class));
        assertTrue(result.getClass().isAssignableFrom(Map.class));
        assertFalse(result.isEmpty());
        assertEquals(0, ((Map) result).size());
    }

    @Test
    public void testEmptyTreeMapReturnValue() {
        // Given
        Answer answer = new ReturnsEmptyValues();

        // When
        Class<?> treeMapType = Map.class;
        Object result = answer.returnValueFor(treeMapType);

        // Then
        assertTrue(result instanceof Map);
        assertTrue(result instanceof TreeMap);
        assertFalse(result.isEmpty());
        assertEquals(0, ((Map) result).size());
    }

    @Test
    public void testEmptyTreeMapReturnValueWithGenericType() {
        // Given
        Answer answer = new ReturnsEmptyValues();

        // When
        Class<?> treeMapType = SortedMap.class;
        Class<?> keyType = String.class;
        Class<?> valueType = Integer.class;
        Object result = answer.returnValueFor(SortedMap.class, keyType, valueType);

        // Then
        assertTrue(result instanceof Map);
        assertTrue(result instanceof TreeMap);
        assertTrue(result.getClass().isAssignableFrom(SortedMap.class));
        assertTrue(result.getClass().isAssignableFrom(Map.class));
        assertFalse(result.isEmpty());
        assertEquals(0, ((Map) result).size());
    }

    @Test
    public void testEmptyLinkedHashMapReturnValue() {
        // Given
        Answer answer = new ReturnsEmptyValues();

        // When
        Class<?> linkedHashMapType = Map.class;
        Object result = answer.returnValueFor(linkedHashMapType);

        // Then
        assertTrue(result instanceof Map);
        assertTrue(result instanceof LinkedHashMap);
        assertFalse(result.isEmpty());
        assertEquals(0, ((Map) result).size());
    }

    @Test
    public void testEmptyLinkedHashMapReturnValueWithGenericType() {
        // Given
        Answer answer = new ReturnsEmptyValues();

        // When
        Class<?> linkedHashMapType = Map.class;
        Class<?> keyType = String.class;
        Class<?> valueType = Integer.class;
        Object result = answer.returnValueFor(Map.class, keyType, valueType);

        // Then
        assertTrue(result instanceof Map);
        assertTrue(result instanceof LinkedHashMap);
        assertTrue(result.getClass().isAssignableFrom(Map.class));
        assertTrue(result.getClass().isAssignableFrom(Map.class));
        assertFalse(result.isEmpty());
        assertEquals(0, ((Map) result).size());
    }
}