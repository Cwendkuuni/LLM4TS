package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collections;
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
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        Object mock = mock(String.class);
        Answer<String> answer = new ReturnsEmptyValues();
        when(mock.toString()).thenAnswer(answer);

        String value = mock.toString();
        assertEquals("Mock for String, hashCode: " + mock.hashCode(), value);
    }

    @Test
    public void testCompareToMethod() {
        Object mock = mock(Comparable.class);
        Object anotherObject = mock(Comparable.class);

        Answer<Integer> answer = new ReturnsEmptyValues();
        when(mock.compareTo(ArgumentMatchers.any())).thenAnswer(answer);

        int comparisonResult = mock.compareTo(anotherObject);
        assertEquals(1, comparisonResult);
    }

    @Test
    public void testReturnEmptyCollectionForCollectionType() {
        Object mock = mock(Collection.class);
        Answer<Collection> answer = new ReturnsEmptyValues();
        when(mock.getClass()).thenReturn(ArrayList.class);
        when(mock.isEmpty()).thenReturn(true);
        when(mock.toString()).thenReturn("[]");

        Collection collection = (Collection) mock;
        assertNotNull(collection);
        assertEquals(Collections.emptyList(), collection);
    }

    @Test
    public void testReturnEmptySetForSetType() {
        Object mock = mock(Set.class);
        Answer<Set> answer = new ReturnsEmptyValues();
        when(mock.isEmpty()).thenReturn(true);
        when(mock.toString()).thenReturn("{}");

        Set<Object> set = (Set<Object>) mock;
        assertNotNull(set);
        assertEquals(Collections.emptySet(), set);
    }

    @Test
    public void testReturnEmptyListForArrayListType() {
        Object mock = mock(List.class);
        Answer<List> answer = new ReturnsEmptyValues();
        when(mock.isEmpty()).thenReturn(true);
        when(mock.toString()).thenReturn("[]");

        List<Object> list = (List<Object>) mock;
        assertNotNull(list);
        assertEquals(Collections.emptyList(), list);
    }

    @Test
    public void testReturnEmptyMapForHashMapType() {
        Object mock = mock(Map.class);
        Answer<Map> answer = new ReturnsEmptyValues();
        when(mock.isEmpty()).thenReturn(true);
        when(mock.toString()).thenReturn("{}");

        Map<Object, Object> map = (Map<Object, Object>) mock;
        assertNotNull(map);
        assertEquals(Collections.emptyMap(), map);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testModifyingReturnedCollection() {
        Object mock = mock(List.class);
        Answer<List> answer = new ReturnsEmptyValues();
        when(mock.getClass()).thenReturn(ArrayList.class);
        when(mock).thenAnswer(answer);

        List<Object> list = (List<Object>) mock;
        list.add(new Object());
    }

}