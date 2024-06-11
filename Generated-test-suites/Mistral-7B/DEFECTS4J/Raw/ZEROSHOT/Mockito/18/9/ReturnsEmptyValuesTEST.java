package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
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
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues;

    @Before
    public void setup() {
        returnsEmptyValues = new ReturnsEmptyValues();
    }

    @Test
    public void testToStringAnswer() {
        Object mock = mock(String.class);
        Answer<String> answer = returnsEmptyValues;
        when(mock.toString()).thenAnswer(answer);

        String actual = mock.toString();
        String expected = "Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode();
        verify(mock).toString();
        verify(mock).toString(); //verify that it does not throw an exception when called twice

        assertSame(expected, actual);
    }

    @Test
    public void testCompareToAnswer() {
        Object mock = mock(Comparable.class);
        Object other = mock(Comparable.class);

        Answer<Integer> answer = returnsEmptyValues;
        when(mock.compareTo(ArgumentMatchers.any())).thenAnswer(answer);

        int actual = mock.compareTo(other);
        int expected = 1;
        verify(mock).compareTo(other);

        assertSame(expected, actual);
    }

    @Test
    public void testEmptyCollectionAnswer() {
        Object mock = mock(Collection.class);
        Answer<Collection> answer = returnsEmptyValues;
        when(mock.getClass()).thenReturn(ArrayList.class); //we need to know the exact type, otherwise JUnit will infer Collection as the return type
        when(mock.toString()).thenAnswer(answer); //otherwise JUnit would print something like "Collection@12345" instead of the actual test name

        Collection actual = mock.isEmpty() ? mock.getClass().newInstance() : mock.isEmpty();
        Collection expected = new ArrayList<>();

        assertTrue(mock.isEmpty());
        assertTrue(mock.isEmpty()); //verify that it does not throw an UnsupportedOperationException when called twice

        assertSame(expected, actual);
    }

    @Test
    public void testEmptySetAnswer() {
        Object mock = mock(Set.class);
        Answer<Set> answer = returnsEmptyValues;
        when(mock.getClass()).thenReturn(HashSet.class);
        when(mock.toString()).thenAnswer(answer);

        Set actual = mock.isEmpty() ? mock.getClass().newInstance() : mock.isEmpty();
        Set expected = new HashSet<>();

        assertTrue(mock.isEmpty());
        assertTrue(mock.isEmpty());

        assertSame(expected, actual);
    }

    @Test
    public void testEmptyLinkedSetAnswer() {
        Object mock = mock(SortedSet.class);
        Answer<SortedSet> answer = returnsEmptyValues;
        when(mock.getClass()).thenReturn(TreeSet.class);
        when(mock.toString()).thenAnswer(answer);

        SortedSet actual = mock.isEmpty() ? mock.getClass().newInstance() : mock.isEmpty();
        SortedSet expected = new TreeSet<>();

        assertTrue(mock.isEmpty());
        assertTrue(mock.isEmpty());

        assertSame(expected, actual);
    }

    //Add similar test methods for other collections, e.g. LinkedHashMap, LinkedList, ArrayList, HashMap, TreeMap, etc.

}