package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

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

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Mockito<Object> mock = mock(Object.class);

        // When
        String result = returnsEmptyValues.answer(argument(ArgumentMatchers.any(InvocationOnMock.class)).getMock());

        // Then
        assertEquals("Mock for Object, hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void testCompareToAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock = mock(Comparable.class);
        Object other = mock(Comparable.class);

        // When
        int result = (Integer) returnsEmptyValues.answer(argument(ArgumentMatchers.any(InvocationOnMock.class))
                .getMethod(ArgumentMatchers.anyString("compareTo"))
                .invoke(mock, other));

        // Then
        assertNotEquals(0, result);
    }

    @Test
    public void testCollectionEmptyListAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        Collection<String> result = (Collection<String>) returnsEmptyValues.answer(argument(ArgumentMatchers.any(InvocationOnMock.class))
                .getMethod(ArgumentMatchers.anyString("new"))
                .invoke(ArrayList.class));

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSetEmptyHashSetAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        Set<String> result = (Set<String>) returnsEmptyValues.answer(argument(ArgumentMatchers.any(InvocationOnMock.class))
                .getMethod(ArgumentMatchers.anyString("new"))
                .invoke(HashSet.class));

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSortedSetEmptyTreeSetAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        SortedSet<String> result = (SortedSet<String>) returnsEmptyValues.answer(argument(ArgumentMatchers.any(InvocationOnMock.class))
                .getMethod(ArgumentMatchers.anyString("new"))
                .invoke(TreeSet.class));

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSortedMapEmptyTreeMapAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        SortedMap<String, String> result = (SortedMap<String, String>) returnsEmptyValues.answer(argument(ArgumentMatchers.any(InvocationOnMock.class))
                .getMethod(ArgumentMatchers.anyString("new"))
                .invoke(TreeMap.class));

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    public void testMapEmptyHashMapAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        Map<String, String> result = (Map<String, String>) returnsEmptyValues.answer(argument(ArgumentMatchers.any(InvocationOnMock.class))
                .getMethod(ArgumentMatchers.anyString("new"))
                .invoke(HashMap.class));

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLinkedHashMapEmptyLinkedHashMapAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        Map<String, String> result = (Map<String, String>) returnsEmptyValues.answer(argument(ArgumentMatchers.any(InvocationOnMock.class))
                .getMethod(ArgumentMatchers.anyString("new"))
                .invoke(LinkedHashMap.class));

        // Then
        assertTrue(result.isEmpty());
    }
}