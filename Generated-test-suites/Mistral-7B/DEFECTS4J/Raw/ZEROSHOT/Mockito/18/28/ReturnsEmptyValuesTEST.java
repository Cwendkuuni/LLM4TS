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
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues;

    @Before
    public void setup() {
        returnsEmptyValues = new ReturnsEmptyValues();
    }

    @Test
    public void testToStringAnswer() {
        Object mock = mock(String.class);
        InvocationOnMock invocation = Mockito.methodCall(mock, "toString");
        Answer<Object> answer = returnsEmptyValues;

        String result = (String) answer.answer(invocation);
        Mockito.verify(mock, Mockito.times(1)).toString();

        assertEquals("Mock for [class java.lang.String], hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void testCompareToAnswer() {
        Object mock1 = mock(Comparable.class);
        Object mock2 = mock(Comparable.class);

        InvocationOnMock invocation = Mockito.methodCall(mock1, "compareTo", ArgumentMatchers.any());
        invocation.setArguments(mock2);

        Answer<Integer> answer = returnsEmptyValues;

        int result = answer.answer(invocation);
        Mockito.verify(mock1, Mockito.times(1)).compareTo(mock2);

        assertEquals(0, result);
    }

    @Test
    public void testPrimitiveAnswer() {
        InvocationOnMock invocation = Mockito.methodCall(mock(Byte.class), "byteValue");

        Byte result = (Byte) returnsEmptyValues.answer(invocation);
        Mockito.verify(invocation.getMock(), Mockito.times(1)).byteValue();

        assertEquals((byte) 0, result);
    }

    @Test
    public void testCollectionAnswer() {
        Collection<Integer> expected = new ArrayList<>();
        InvocationOnMock invocation = Mockito.methodCall(mock(Collection.class), "isEmpty");
        Answer<Boolean> answer = returnsEmptyValues;

        Boolean result = answer.answer(invocation);
        Mockito.verify(invocation.getMock(), Mockito.times(1)).isEmpty();

        assertEquals(true, result);

        invocation = Mockito.methodCall(mock(Collection.class), "size");
        result = (Collection<Integer>) answer.answer(invocation);
        Mockito.verify(invocation.getMock(), Mockito.times(1)).size();

        assertEquals(0, result.size());

        invocation = Mockito.methodCall(mock(Collection.class), "add", ArgumentMatchers.any());
        answer = returnsEmptyValues;

        answer.answer(invocation);
        Mockito.verify(invocation.getMock(), Mockito.times(1)).add(ArgumentMatchers.any());

        result = result;
    }

    @Test
    public void testSetAnswer() {
        Set<String> expected = new HashSet<>();
        InvocationOnMock invocation = Mockito.methodCall(mock(Set.class), "isEmpty");
        Answer<Boolean> answer = returnsEmptyValues;

        Boolean result = answer.answer(invocation);
        Mockito.verify(invocation.getMock(), Mockito.times(1)).isEmpty();

        assertEquals(true, result);

        invocation = Mockito.methodCall(mock(Set.class), "add", ArgumentMatchers.any());
        answer = returnsEmptyValues;

        answer.answer(invocation);
        Mockito.verify(invocation.getMock(), Mockito.times(1)).add(ArgumentMatchers.any());

        result = result;
    }

    @Test
    public void testSortedSetAnswer() {
        SortedSet<Integer> expected = new TreeSet<>();
        InvocationOnMock invocation = Mockito.methodCall(mock(SortedSet.class), "isEmpty");
        Answer<Boolean> answer = returnsEmptyValues;

        Boolean result = answer.answer(invocation);
        Mockito.verify(invocation.getMock(), Mockito.times(1)).isEmpty();

        assertEquals(true, result);

        invocation = Mockito.methodCall(mock(SortedSet.class), "size");
        result = (SortedSet<Integer>) answer.answer(invocation);
        Mockito.verify(invocation.getMock(), Mockito.times(1)).size();

        assertEquals(0, result.size());
    }

    @Test
    public void testMapAnswer() {
        Map<String, Integer> expected = new HashMap<>();
        InvocationOnMock invocation = Mockito.methodCall(mock(Map.class), "isEmpty");
        Answer<Boolean> answer = returnsEmptyValues;

        Boolean result = answer.answer(invocation);
        Mockito.verify(invocation.getMock(), Mockito.times(1)).isEmpty();

        assertEquals(true, result);

        invocation = Mockito.methodCall(mock(Map.class), "put", ArgumentMatchers.anyString(), ArgumentMatchers.anyInt());
        answer = returnsEmptyValues;

        answer.answer(invocation);
        Mockito.verify(invocation.getMock(), Mockito.times(1)).put(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt());

        result = result;
    }

    @Test
    public void testSortedMapAnswer() {
        SortedMap<String, Integer> expected = new TreeMap<>();
        InvocationOnMock invocation = Mockito.methodCall(mock(SortedMap.class), "isEmpty");
        Answer<Boolean> answer = returnsEmptyValues;

        Boolean result = answer.answer(invocation);
        Mockito.verify(invocation.getMock(), Mockito.times(1)).isEmpty();

        assertEquals(true, result);

        invocation = Mockito.methodCall(mock(SortedMap.class), "put", ArgumentMatchers.anyString(), ArgumentMatchers.anyInt());
        answer = returnsEmptyValues;

        answer.answer(invocation);
        Mockito.verify(invocation.getMock(), Mockito.times(1)).put(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt());

        result = result;
    }

    @Test
    public void testLinkedHashMapAnswer() {
        Map<String, Integer> expected = new LinkedHashMap<>();
        InvocationOnMock invocation = Mockito.methodCall(mock(Map.class), "isEmpty");
        Answer<Boolean> answer = returnsEmptyValues;

        Boolean result = answer.answer(invocation);
        Mockito.verify(invocation.getMock(), Mockito.times(1)).isEmpty();

        assertEquals(true, result);

        invocation = Mockito.methodCall(mock(Map.class), "put", ArgumentMatchers.anyString(), ArgumentMatchers.anyInt());
        answer = returnsEmptyValues;

        answer.answer(invocation);
        Mockito.verify(invocation.getMock(), Mockito.times(1)).put(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt());

        result = result;
    }

    @Test
    public void testLinkedListAnswer() {
        List<Integer> expected = new LinkedList<>();
        InvocationOnMock invocation = Mockito.methodCall(mock(List.class), "isEmpty");
        Answer<Boolean> answer = returnsEmptyValues;

        Boolean result = answer.answer(invocation);
        Mockito.verify(invocation.getMock(), Mockito.times(1)).isEmpty();

        assertEquals(true, result);

        invocation = Mockito.methodCall(mock(List.class), "add", ArgumentMatchers.anyInt());
        answer = returnsEmptyValues;

        answer.answer(invocation);
        Mockito.verify(invocation.getMock(), Mockito.times(1)).add(ArgumentMatchers.anyInt());

        result = result;
    }

    @Test
    public void testLinkedHashSetAnswer() {
        Set<Integer> expected = new LinkedHashSet<>();
        InvocationOnMock invocation = Mockito.methodCall(mock(Set.class), "isEmpty");
        Answer<Boolean> answer = returnsEmptyValues;

        Boolean result = answer.answer(invocation);
        Mockito.verify(invocation.getMock(), Mockito.times(1)).isEmpty();

        assertEquals(true, result);

        invocation = Mockito.methodCall(mock(Set.class), "add", ArgumentMatchers.anyInt());
        answer = returnsEmptyValues;

        answer.answer(invocation);
        Mockito.verify(invocation.getMock(), Mockito.times(1)).add(ArgumentMatchers.anyInt());

        result = result;
    }
}