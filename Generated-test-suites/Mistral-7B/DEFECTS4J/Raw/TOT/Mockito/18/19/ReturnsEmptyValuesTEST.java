package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Arguments;
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
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testToString() {
        // Given
        Object mock = mock(String.class);
        Answer<Object> answer = new ReturnsEmptyValues();

        // When
        String result = answer.answer(Arguments.invocationWith(mock, "toString()"));

        // Then
        assertEquals("Mock for String, hashCode: " + mock.hashCode(), result.toString());
    }

    @Test
    public void testCompareTo() {
        // Given
        Object mock = mock(Comparable.class);
        Object anotherMock = mock(Comparable.class);
        Answer<Object> answer = new ReturnsEmptyValues();

        // When
        int result = ((Comparable) answer).answer(Arguments.invocationWith(mock, anotherMock, "compareTo(Comparable other)"));

        // Then
        assertEquals(1, result);
    }

    @Test
    public void testEmptyCollection() {
        // Given
        Collection<Integer> mock = mock(Collection.class);
        Answer<Object> answer = new ReturnsEmptyValues();

        // When
        Collection<Integer> result = (Collection<Integer>) answer.answer(Arguments.invocationWith(mock, ""));

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testEmptySet() {
        // Given
        Set<Integer> mock = mock(Set.class);
        Answer<Object> answer = new ReturnsEmptyValues();

        // When
        Set<Integer> result = (Set<Integer>) answer.answer(Arguments.invocationWith(mock, ""));

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testEmptyHashSet() {
        // Given
        Set<Integer> mock = mock(HashSet.class);
        Answer<Object> answer = new ReturnsEmptyValues();

        // When
        Set<Integer> result = (Set<Integer>) answer.answer(Arguments.invocationWith(mock, ""));

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testEmptySortedSet() {
        // Given
        SortedSet<Integer> mock = mock(SortedSet.class);
        Answer<Object> answer = new ReturnsEmptyValues();

        // When
        SortedSet<Integer> result = (SortedSet<Integer>) answer.answer(Arguments.invocationWith(mock, ""));

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testEmptyTreeSet() {
        // Given
        SortedSet<Integer> mock = mock(TreeSet.class);
        Answer<Object> answer = new ReturnsEmptyValues();

        // When
        SortedSet<Integer> result = (SortedSet<Integer>) answer.answer(Arguments.invocationWith(mock, ""));

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testEmptyLinkedHashSet() {
        // Given
        Set<Integer> mock = mock(LinkedHashSet.class);
        Answer<Object> answer = new ReturnsEmptyValues();

        // When
        Set<Integer> result = (Set<Integer>) answer.answer(Arguments.invocationWith(mock, ""));

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testEmptyList() {
        // Given
        List<Integer> mock = mock(List.class);
        Answer<Object> answer = new ReturnsEmptyValues();

        // When
        List<Integer> result = (List<Integer>) answer.answer(Arguments.invocationWith(mock, ""));

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testEmptyLinkedList() {
        // Given
        List<Integer> mock = mock(LinkedList.class);
        Answer<Object> answer = new ReturnsEmptyValues();

        // When
        List<Integer> result = (List<Integer>) answer.answer(Arguments.invocationWith(mock, ""));

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testEmptyArrayList() {
        // Given
        List<Integer> mock = mock(ArrayList.class);
        Answer<Object> answer = new ReturnsEmptyValues();

        // When
        List<Integer> result = (List<Integer>) answer.answer(Arguments.invocationWith(mock, ""));

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testEmptyMap() {
        // Given
        Map<String, Integer> mock = mock(Map.class);
        Answer<Object> answer = new ReturnsEmptyValues();

        // When
        Map<String, Integer> result = (Map<String, Integer>) answer.answer(Arguments.invocationWith(mock, ""));

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testEmptyHashMap() {
        // Given
        Map<String, Integer> mock = mock(HashMap.class);
        Answer<Object> answer = new ReturnsEmptyValues();

        // When
        Map<String, Integer> result = (Map<String, Integer>) answer.answer(Arguments.invocationWith(mock, ""));

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testEmptySortedMap() {
        // Given
        SortedMap<String, Integer> mock = mock(SortedMap.class);
        Answer<Object> answer = new ReturnsEmptyValues();

        // When
        SortedMap<String, Integer> result = (SortedMap<String, Integer>) answer.answer(Arguments.invocationWith(mock, ""));

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testEmptyTreeMap() {
        // Given
        SortedMap<String, Integer> mock = mock(TreeMap.class);
        Answer<Object> answer = new ReturnsEmptyValues();

        // When
        SortedMap<String, Integer> result = (SortedMap<String, Integer>) answer.answer(Arguments.invocationWith(mock, ""));

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testEmptyLinkedHashMap() {
        // Given
        Map<String, Integer> mock = mock(LinkedHashMap.class);
        Answer<Object> answer = new ReturnsEmptyValues();

        // When
        Map<String, Integer> result = (Map<String, Integer>) answer.answer(Arguments.invocationWith(mock, ""));

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testNull() {
        // Given
        Object mock = mock(Object.class);
        Answer<Object> answer = new ReturnsEmptyValues();

        // When
        Object result = answer.answer(Arguments.invocationWith(mock, null, "answer(InvocationOnMock invocation)"));

        // Then
        assertNotNull(result);
        assertEquals(null, result);
    }

    @Test
    public void testNonPrimitiveReturnType() {
        // Given
        Object mock = mock(Object.class);
        Answer<Object> answer = new ReturnsEmptyValues();

        // When
        Object result = answer.answer(Arguments.invocationWith(mock, "return a non-primitive type"));

        // Then
        assertNotNull(result);
        assertEquals(null, result);
    }
}