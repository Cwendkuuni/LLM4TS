package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
import org.junit.Test;
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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues;

    @Before
    public void setUp() {
        returnsEmptyValues = new ReturnsEmptyValues();
    }

    @Test
    public void testToStringAnswer() {
        Object mock = mock(String.class);
        InvocationOnMock invocation = Mockito.methodCall(mock, "toString");
        String result = (String) returnsEmptyValues.answer(invocation);
        String expected = "Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode();
        assertEquals(expected, result);
    }

    @Test
    public void testCompareToAnswer() {
        Object mock = mock(Comparable.class);
        Object anotherObject = mock(Comparable.class);
        InvocationOnMock invocation = Mockito.methodCall(mock, "compareTo", ArgumentMatchers.any());
        int result = (Integer) returnsEmptyValues.answer(invocation);
        verify(mock).equals(anotherObject);
        assertEquals(1, result); // mocks return 1 for compareTo method
    }

    @Test
    public void testEmptyCollectionAnswer() {
        Object mock = mock(Collection.class);
        InvocationOnMock invocation = Mockito.methodCall(mock, "isEmpty");
        boolean result = (Boolean) returnsEmptyValues.answer(invocation);
        assertEquals(true, result);

        invocation = Mockito.methodCall(mock, "size");
        result = (Integer) returnsEmptyValues.answer(invocation);
        assertEquals(0, result);

        invocation = Mockito.methodCall(mock, "iterator");
        Collection<Object> collection = (Collection<Object>) returnsEmptyValues.answer(invocation);
        assertNotNull(collection);
        assertEquals(0, collection.size());
    }

    @Test
    public void testEmptyListAnswer() {
        Object mock = mock(List.class);
        InvocationOnMock invocation = Mockito.methodCall(mock, "isEmpty");
        boolean result = (Boolean) returnsEmptyValues.answer(invocation);
        assertEquals(true, result);

        invocation = Mockito.methodCall(mock, "size");
        result = (Integer) returnsEmptyValues.answer(invocation);
        assertEquals(0, result);

        invocation = Mockito.methodCall(mock, "get", ArgumentMatchers.anyInt());
        Object element = returnsEmptyValues.answer(invocation);
        assertNull(element);
    }

    @Test
    public void testEmptySetAnswer() {
        Object mock = mock(Set.class);
        InvocationOnMock invocation = Mockito.methodCall(mock, "isEmpty");
        boolean result = (Boolean) returnsEmptyValues.answer(invocation);
        assertEquals(true, result);

        invocation = Mockito.methodCall(mock, "size");
        result = (Integer) returnsEmptyValues.answer(invocation);
        assertEquals(0, result);

        invocation = Mockito.methodCall(mock, "contains", ArgumentMatchers.any());
        boolean contains = (Boolean) returnsEmptyValues.answer(invocation);
        assertFalse(contains);
    }

    @Test
    public void testEmptyMapAnswer() {
        Object mock = mock(Map.class);
        InvocationOnMock invocation = Mockito.methodCall(mock, "isEmpty");
        boolean result = (Boolean) returnsEmptyValues.answer(invocation);
        assertEquals(true, result);

        invocation = Mockito.methodCall(mock, "size");
        result = (Integer) returnsEmptyValues.answer(invocation);
        assertEquals(0, result);

        invocation = Mockito.methodCall(mock, "get", ArgumentMatchers.eq("key"));
        Object value = returnsEmptyValues.answer(invocation);
        assertNull(value);
    }
}