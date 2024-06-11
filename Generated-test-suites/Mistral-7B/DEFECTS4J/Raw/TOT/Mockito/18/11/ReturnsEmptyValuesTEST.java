package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.test.stubbing.answer.AnswerWithDefaultValue;

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

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerToStringMethod() {
        // given
        Object mock = mock(String.class);
        Answer<String> answer = new ReturnsEmptyValues();

        // when
        String result = answer.answer(new InvocationOnMock(mock, mock.getClass().getMethod("toString"), (Object[]) null));

        // then
        verify(mock).toString();
        assertEquals("Mock for String, hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void testAnswerCompareToMethod() {
        // given
        Object mock = mock(Comparable.class);
        Object anotherMock = mock(Comparable.class);
        Answer<Integer> answer = new ReturnsEmptyValues();

        // when
        int result = answer.answer(new InvocationOnMock(mock, mock.getClass().getMethod("compareTo", Object.class), new Object[]{anotherMock}));

        // then
        verify(mock).compareTo(anotherMock);
        verifyNoMoreInteractions(mock);
        verifyNoMoreInteractions(anotherMock);
        assertEquals(1, result);
    }

    @Test
    public void testAnswerForPrimitiveTypes() {
        // given
        Answer<Byte> answer = new ReturnsEmptyValues();

        // when
        Byte result = answer.answer(new InvocationOnMock(null, Byte.TYPE, null));

        // then
        assertEquals((byte) 0, result);
    }

    @Test
    public void testAnswerForCollectionTypes_ArrayList() {
        // given
        Answer<Collection<String>> answer = new ReturnsEmptyValues();

        // when
        Collection<String> result = answer.answer(new InvocationOnMock(null, Collection.class, null));

        // then
        assertTrue(result instanceof ArrayList);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswerForCollectionTypes_LinkedList() {
        // given
        Answer<Collection<String>> answer = new ReturnsEmptyValues();

        // when
        Collection<String> result = answer.answer(new InvocationOnMock(null, LinkedList.class, null));

        // then
        assertTrue(result instanceof LinkedList);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswerForCollectionTypes_HashSet() {
        // given
        Answer<Set<String>> answer = new ReturnsEmptyValues();

        // when
        Set<String> result = answer.answer(new InvocationOnMock(null, HashSet.class, null));

        // then
        assertTrue(result instanceof HashSet);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswerForCollectionTypes_LinkedHashSet() {
        // given
        Answer<Set<String>> answer = new ReturnsEmptyValues();

        // when
        Set<String> result = answer.answer(new InvocationOnMock(null, LinkedHashSet.class, null));

        // then
        assertTrue(result instanceof LinkedHashSet);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswerForCollectionTypes_TreeSet() {
        // given
        Answer<Set<String>> answer = new ReturnsEmptyValues();

        // when
        Set<String> result = answer.answer(new InvocationOnMock(null, TreeSet.class, null));

        // then
        assertTrue(result instanceof TreeSet);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswerForCollectionTypes_ArrayListWithArgument() {
        // given
        Answer<Collection<String>> answer = new ReturnsEmptyValues();

        // when
        Collection<String> result = answer.answer(new InvocationOnMock(null, Collection.class, new Object[]{"testString"}));

        // then
        assertTrue(result instanceof ArrayList);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswerForCollectionTypes_LinkedListWithArgument() {
        // given
        Answer<Collection<String>> answer = new ReturnsEmptyValues();

        // when
        Collection<String> result = answer.answer(new InvocationOnMock(null, LinkedList.class, new Object[]{"testString"}));

        // then
        assertTrue(result instanceof LinkedList);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswerForCollectionTypes_HashSetWithArgument() {
        // given
        Answer<Set<String>> answer = new ReturnsEmptyValues();

        // when
        Set<String> result = answer.answer(new InvocationOnMock(null, HashSet.class, new Object[]{"testString"}));

        // then
        assertTrue(result instanceof HashSet);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswerForCollectionTypes_LinkedHashSetWithArgument() {
        // given
        Answer<Set<String>> answer = new ReturnsEmptyValues();

        // when
        Set<String> result = answer.answer(new InvocationOnMock(null, LinkedHashSet.class, new Object[]{"testString"}));

        // then
        assertTrue(result instanceof LinkedHashSet);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswerForCollectionTypes_TreeSetWithArgument() {
        // given
        Answer<Set<String>> answer = new ReturnsEmptyValues();

        // when
        Set<String> result = answer.answer(new InvocationOnMock(null, TreeSet.class, new Object[]{"testString"}));

        // then
        assertTrue(result instanceof TreeSet);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswerForMapTypes_HashMap() {
        // given
        Answer<Map<String, Integer>> answer = new ReturnsEmptyValues();

        // when
        Map<String, Integer> result = answer.answer(new InvocationOnMock(null, Map.class, null));

        // then
        assertTrue(result instanceof HashMap);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswerForMapTypes_TreeMap() {
        // given
        Answer<Map<String, Integer>> answer = new ReturnsEmptyValues();

        // when
        Map<String, Integer> result = answer.answer(new InvocationOnMock(null, TreeMap.class, null));

        // then
        assertTrue(result instanceof TreeMap);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswerForMapTypes_LinkedHashMap() {
        // given
        Answer<Map<String, Integer>> answer = new ReturnsEmptyValues();

        // when
        Map<String, Integer> result = answer.answer(new InvocationOnMock(null, LinkedHashMap.class, null));

        // then
        assertTrue(result instanceof LinkedHashMap);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswerForMapTypes_HashMapWithArgument() {
        // given
        Answer<Map<String, Integer>> answer = new ReturnsEmptyValues();

        // when
        Map<String, Integer> result = answer.answer(new InvocationOnMock(null, Map.class, new Object[]{"testKey", 1}));

        // then
        assertTrue(result instanceof HashMap);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswerForMapTypes_TreeMapWithArgument() {
        // given
        Answer<Map<String, Integer>> answer = new ReturnsEmptyValues();

        // when
        Map<String, Integer> result = answer.answer(new InvocationOnMock(null, TreeMap.class, new Object[]{"testKey", 1}));

        // then
        assertTrue(result instanceof TreeMap);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswerForMapTypes_LinkedHashMapWithArgument() {
        // given
        Answer<Map<String, Integer>> answer = new ReturnsEmptyValues();

        // when
        Map<String, Integer> result = answer.answer(new InvocationOnMock(null, LinkedHashMap.class, new Object[]{"testKey", 1}));

        // then
        assertTrue(result instanceof LinkedHashMap);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswerForMapsWithArgumentKeyAndValue() {
        // given
        Answer<Map<String, Integer>> answer = new ReturnsEmptyValues();

        // when
        Map<String, Integer> result = answer.answer(new InvocationOnMock(null, Map.class, new Object[]{"testKey", "testValue", 1}));

        // then
        assertTrue(result instanceof HashMap);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswerForMapsWithArgumentKeyAndValueWithNullKey() {
        // given
        Answer<Map<String, Integer>> answer = new ReturnsEmptyValues();

        // when
        Map<String, Integer> result = answer.answer(new InvocationOnMock(null, Map.class, new Object[]{null, "testValue", 1}));

        // then
        assertTrue(result instanceof HashMap);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswerForMapsWithArgumentKeyAndValueWithNullValue() {
        // given
        Answer<Map<String, Integer>> answer = new ReturnsEmptyValues();

        // when
        Map<String, Integer> result = answer.answer(new InvocationOnMock(null, Map.class, new Object[]{"testKey", null, 1}));

        // then
        assertTrue(result instanceof HashMap);
        assertTrue(result.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAnswerForUnknownCollectionTypes() {
        // given
        Answer<Object> answer = new ReturnsEmptyValues();

        // when
        answer.answer(new InvocationOnMock(null, Object.class, null));

        // then
        fail("Answer should throw IllegalArgumentException for unknown collection types");
    }
}