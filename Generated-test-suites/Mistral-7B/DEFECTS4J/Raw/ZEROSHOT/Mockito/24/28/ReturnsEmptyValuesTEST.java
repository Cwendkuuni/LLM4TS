package com.yourpackage.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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

public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringAnswer() {
        MockitoAnswer answer = Mockito.mock(Answer.class);
        Mockito.when(answer.answer(ArgumentMatchers.any(InvocationOnMock.class)))
                .thenAnswer(new ReturnsEmptyValues());

        Object mock = Mockito.mock(String.class);
        MockName name = Mockito.mock(MockName.class);
        Mockito.when(name.isDefault()).thenReturn(true);
        Mockito.when(name.toString()).thenReturn("MockName");

        Mockito.when(answer.answer(Mockito.any(InvocationOnMock.class))).thenCallRealMethod();

        String result = (String) answer.answer(Mockito.argThat(invocation ->
                invocation.getMethod().getName().equals("toString"))).getMock();

        Assertions.assertEquals("Mock for Mockito$MockName, hashCode: 1", result);
    }

    @Test
    public void testCompareToAnswer() {
        MockitoAnswer answer = Mockito.mock(Answer.class);
        Mockito.when(answer.answer(ArgumentMatchers.any(InvocationOnMock.class)))
                .thenAnswer(new ReturnsEmptyValues());

        Object mock1 = Mockito.mock(Comparable.class);
        Object mock2 = Mockito.mock(Comparable.class);

        Mockito.when(answer.answer(Mockito.any(InvocationOnMock.class))).thenCallRealMethod();

        int result = (Integer) answer.answer(Mockito.argThat(invocation ->
                invocation.getMethod().getName().equals("compareTo")))
                .getArguments()[1]
                .getClass()
                .cast(mock1)
                .compareTo(mock2);

        Assertions.assertEquals(1, result);
    }

    @Test
    public void testCollectionAnswer() {
        MockitoAnswer answer = Mockito.mock(Answer.class);
        Mockito.when(answer.answer(ArgumentMatchers.any(InvocationOnMock.class)))
                .thenAnswer(new ReturnsEmptyValues());

        Collection<String> expected = new ArrayList<>();
        Collection<String> actual = (Collection<String>) answer.answer(Mockito.argThat(invocation ->
                invocation.getMethod().getName().equals("someMethod")))
                .getMock();

        Assertions.assertEquals(expected, actual);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    public void testSetAnswer() {
        MockitoAnswer answer = Mockito.mock(Answer.class);
        Mockito.when(answer.answer(ArgumentMatchers.any(InvocationOnMock.class)))
                .thenAnswer(new ReturnsEmptyValues());

        Set<String> expected = new HashSet<>();
        Set<String> actual = (Set<String>) answer.answer(Mockito.argThat(invocation ->
                invocation.getMethod().getName().equals("someOtherMethod")))
                .getMock();

        Assertions.assertEquals(expected, actual);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    public void testMapAnswer() {
        MockitoAnswer answer = Mockito.mock(Answer.class);
        Mockito.when(answer.answer(ArgumentMatchers.any(InvocationOnMock.class)))
                .thenAnswer(new ReturnsEmptyValues());

        Map<String, String> expected = new HashMap<>();
        Map<String, String> actual = (Map<String, String>) answer.answer(Mockito.argThat(invocation ->
                invocation.getMethod().getName().equals("someMethodWithMap")))
                .getMock();

        Assertions.assertEquals(expected, actual);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    public void testSortedSetAnswer() {
        MockitoAnswer answer = Mockito.mock(Answer.class);
        Mockito.when(answer.answer(ArgumentMatchers.any(InvocationOnMock.class)))
                .thenAnswer(new ReturnsEmptyValues());

        SortedSet<String> expected = new TreeSet<>();
        SortedSet<String> actual = (SortedSet<String>) answer.answer(Mockito.argThat(invocation ->
                invocation.getMethod().getName().equals("someSortedSetMethod")))
                .getMock();

        Assertions.assertEquals(expected, actual);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    public void testSortedMapAnswer() {
        MockitoAnswer answer = Mockito.mock(Answer.class);
        Mockito.when(answer.answer(ArgumentMatchers.any(InvocationOnMock.class)))
                .thenAnswer(new ReturnsEmptyValues());

        SortedMap<String, String> expected = new TreeMap<>();
        SortedMap<String, String> actual = (SortedMap<String, String>) answer.answer(Mockito.argThat(invocation ->
                invocation.getMethod().getName().equals("someSortedMapMethod")))
                .getMock();

        Assertions.assertEquals(expected, actual);
        Assertions.assertTrue(actual.isEmpty());
    }
}