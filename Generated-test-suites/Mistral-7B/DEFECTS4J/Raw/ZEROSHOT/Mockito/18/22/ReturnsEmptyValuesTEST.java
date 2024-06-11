package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import org.junit.Test;
import org.mockito.Answer;
import org.mockito.InternalStubbingHelper;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        Object mock = mock(String.class);
        Answer answer = new ReturnsEmptyValues();

        when(mock.toString()).thenAnswer(answer);

        String expected = "Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode();
        String actual = (String) answer.answer(new InvocationOnMock(mock, "toString()"));

        assertEquals(expected, actual);
    }

    @Test
    public void testCompareToMethod() {
        Object mock1 = mock(Comparable.class);
        Object mock2 = mock(Comparable.class);

        Answer answer = new ReturnsEmptyValues();

        when(mock1.compareTo(mock2)).thenAnswer(answer);

        int actual = (Integer) answer.answer(new InvocationOnMock(mock1, "compareTo(Object)", mock2));

        assertEquals(1, actual);
    }

    @Test
    public void testReturnsEmptyList() {
        Object mock = mock(Collection.class);
        Answer answer = new ReturnsEmptyValues();

        when(mock.toString()).thenReturn("List");
        when(mock.isEmpty()).thenReturn(true);

        when(mock.getClass()).thenReturn(ArrayList.class);
        when(mock.newInstance()).thenReturn(answer.returnValueFor(ArrayList.class));

        Collection<Object> actual = (Collection<Object>) answer.answer(new InvocationOnMock(mock, "newInstance()"));

        assertEquals(0, actual.size());
    }

    @Test
    public void testReturnsEmptySet() {
        Object mock = mock(Set.class);
        Answer answer = new ReturnsEmptyValues();

        when(mock.toString()).thenReturn("Set");
        when(mock.isEmpty()).thenReturn(true);

        when(mock.getClass()).thenReturn(HashSet.class);
        when(mock.newInstance()).thenReturn(answer.returnValueFor(HashSet.class));

        Set<Object> actual = (Set<Object>) answer.answer(new InvocationOnMock(mock, "newInstance()"));

        assertEquals(0, actual.size());
    }

    @Test
    public void testReturnsEmptyTreeSet() {
        Object mock = mock(SortedSet.class);
        Answer answer = new ReturnsEmptyValues();

        when(mock.toString()).thenReturn("TreeSet");
        when(mock.isEmpty()).thenReturn(true);

        when(mock.getClass()).thenReturn(TreeSet.class);
        when(mock.newInstance()).thenReturn(answer.returnValueFor(TreeSet.class));

        SortedSet<Object> actual = (SortedSet<Object>) answer.answer(new InvocationOnMock(mock, "newInstance()"));

        assertEquals(0, actual.size());
    }

    @Test
    public void testReturnsEmptyLinkedHashSet() {
        Object mock = mock(Set.class);
        Answer answer = new ReturnsEmptyValues();

        when(mock.toString()).thenReturn("LinkedHashSet");
        when(mock.isEmpty()).thenReturn(true);

        when(mock.getClass()).thenReturn(LinkedHashSet.class);
        when(mock.newInstance()).thenReturn(answer.returnValueFor(LinkedHashSet.class));

        Set<Object> actual = (Set<Object>) answer.answer(new InvocationOnMock(mock, "newInstance()"));

        assertEquals(0, actual.size());
    }

    @Test
    public void testReturnsEmptyMap() {
        Object mock = mock(Map.class);
        Answer answer = new ReturnsEmptyValues();

        when(mock.toString()).thenReturn("Map");
        when(mock.isEmpty()).thenReturn(true);

        when(mock.getClass()).thenReturn(HashMap.class);
        when(mock.newInstance()).thenReturn(answer.returnValueFor(HashMap.class));

        Map<Object, Object> actual = (Map<Object, Object>) answer.answer(new InvocationOnMock(mock, "newInstance()"));

        assertEquals(0, actual.size());
    }

    @Test
    public void testReturnsEmptyTreeMap() {
        Object mock = mock(Map.class);
        Answer answer = new ReturnsEmptyValues();

        when(mock.toString()).thenReturn("TreeMap");
        when(mock.isEmpty()).thenReturn(true);

        when(mock.getClass()).thenReturn(TreeMap.class);
        when(mock.newInstance()).thenReturn(answer.returnValueFor(TreeMap.class));

        Map<Object, Object> actual = (Map<Object, Object>) answer.answer(new InvocationOnMock(mock, "newInstance()"));

        assertEquals(0, actual.size());
    }

    @Test
    public void testReturnsEmptyLinkedHashMap() {
        Object mock = mock(Map.class);
        Answer answer = new ReturnsEmptyValues();

        when(mock.toString()).thenReturn("LinkedHashMap");
        when(mock.isEmpty()).thenReturn(true);

        when(mock.getClass()).thenReturn(LinkedHashMap.class);
        when(mock.newInstance()).thenReturn(answer.returnValueFor(LinkedHashMap.class));

        Map<Object, Object> actual = (Map<Object, Object>) answer.answer(new InvocationOnMock(mock, "newInstance()"));

        assertEquals(0, actual.size());
    }

    @Test
    public void testReturnsNullForOtherTypes() {
        Object mock = mock(Object.class);
        Answer answer = new ReturnsEmptyValues();

        assertEquals(null, answer.answer(new InvocationOnMock(mock, "hashCode()")));
        assertEquals(null, answer.answer(new InvocationOnMock(mock, "getClass()")));
        assertEquals(null, answer.answer(new InvocationOnMock(mock, "newInstance()")));
    }
}