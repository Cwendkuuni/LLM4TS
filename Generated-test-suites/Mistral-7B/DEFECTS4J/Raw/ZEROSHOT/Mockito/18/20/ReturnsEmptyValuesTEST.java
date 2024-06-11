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

@RunWith(org.mockito.runners.JUnit4.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerToStringMethod() {
        // given
        Object mock = mock(String.class);

        // when
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        String actual = (String) returnsEmptyValues.answer(Mockito.argumentThat(ArgumentMatchers.isInstanceOf(String.class))
                .andReturn(mock).andCallRealMethod());

        // then
        assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), actual);
    }

    @Test
    public void testAnswerCompareToMethod() {
        // given
        Object mock1 = mock(Comparable.class);
        Object mock2 = mock(Comparable.class);

        // when
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        int actual = (Integer) returnsEmptyValues.answer(Mockito.argumentThat(ArgumentMatchers.isInstanceOf(Comparable.class))
                .andReturn(mock1).andCallRealMethod());
        int compareToResult = (Integer) returnsEmptyValues.answer(Mockito.argumentThat(ArgumentMatchers.isInstanceOf(Comparable.class))
                .andReturn(mock2).andCallRealMethod());

        // then
        // mocks should return 0 if references are the same and 1 if references are different
        assertEquals(0, actual);
        assertEquals(1, compareToResult);
    }

    @Test
    public void testAnswerCollectionType() {
        // given
        Object mock = mock(Collection.class);

        // when
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Collection<Object> actual = (Collection<Object>) returnsEmptyValues.answer(Mockito.argumentThat(ArgumentMatchers.isInstanceOf(Collection.class))
                .andReturn(mock).andCallRealMethod());

        // then
        // Collection returned should be empty
        assertEquals(true, actual.isEmpty());
    }

    @Test
    public void testAnswerSetType() {
        // given
        Object mock = mock(Set.class);

        // when
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Set<Object> actual = (Set<Object>) returnsEmptyValues.answer(Mockito.argumentThat(ArgumentMatchers.isInstanceOf(Set.class))
                .andReturn(mock).andCallRealMethod());

        // then
        // Set returned should be empty
        assertEquals(true, actual.isEmpty());
    }

    @Test
    public void testAnswerHashSetType() {
        // given
        Object mock = mock(HashSet.class);

        // when
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Set<Object> actual = (Set<Object>) returnsEmptyValues.answer(Mockito.argumentThat(ArgumentMatchers.isInstanceOf(HashSet.class))
                .andReturn(mock).andCallRealMethod());

        // then
        // HashSet returned should be empty
        assertEquals(true, actual.isEmpty());
    }

    @Test
    public void testAnswerSortedSetType() {
        // given
        Object mock = mock(SortedSet.class);

        // when
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        SortedSet<Object> actual = (SortedSet<Object>) returnsEmptyValues.answer(Mockito.argumentThat(ArgumentMatchers.isInstanceOf(SortedSet.class))
                .andReturn(mock).andCallRealMethod());

        // then
        // SortedSet returned should be empty
        assertEquals(true, actual.isEmpty());
    }

    @Test
    public void testAnswerTreeSetType() {
        // given
        Object mock = mock(TreeSet.class);

        // when
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        SortedSet<Object> actual = (SortedSet<Object>) returnsEmptyValues.answer(Mockito.argumentThat(ArgumentMatchers.isInstanceOf(TreeSet.class))
                .andReturn(mock).andCallRealMethod());

        // then
        // TreeSet returned should be empty
        assertEquals(true, actual.isEmpty());
    }

    @Test
    public void testAnswerLinkedHashSetType() {
        // given
        Object mock = mock(LinkedHashSet.class);

        // when
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Set<Object> actual = (Set<Object>) returnsEmptyValues.answer(Mockito.argumentThat(ArgumentMatchers.isInstanceOf(LinkedHashSet.class))
                .andReturn(mock).andCallRealMethod());

        // then
        // LinkedHashSet returned should be empty
        assertEquals(true, actual.isEmpty());
    }

    @Test
    public void testAnswerListType() {
        // given
        Object mock = mock(List.class);

        // when
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        List<Object> actual = (List<Object>) returnsEmptyValues.answer(Mockito.argumentThat(ArgumentMatchers.isInstanceOf(List.class))
                .andReturn(mock).andCallRealMethod());

        // then
        // List returned should be empty
        assertEquals(true, actual.isEmpty());
    }

    @Test
    public void testAnswerLinkedListType() {
        // given
        Object mock = mock(LinkedList.class);

        // when
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        List<Object> actual = (List<Object>) returnsEmptyValues.answer(Mockito.argumentThat(ArgumentMatchers.isInstanceOf(LinkedList.class))
                .andReturn(mock).andCallRealMethod());

        // then
        // LinkedList returned should be empty
        assertEquals(true, actual.isEmpty());
    }

    @Test
    public void testAnswerArrayListType() {
        // given
        Object mock = mock(ArrayList.class);

        // when
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        List<Object> actual = (List<Object>) returnsEmptyValues.answer(Mockito.argumentThat(ArgumentMatchers.isInstanceOf(ArrayList.class))
                .andReturn(mock).andCallRealMethod());

        // then
        // ArrayList returned should be empty
        assertEquals(true, actual.isEmpty());
    }

    @Test
    public void testAnswerMapType() {
        // given
        Object mock = mock(Map.class);

        // when
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Map<Object, Object> actual = (Map<Object, Object>) returnsEmptyValues.answer(Mockito.argumentThat(ArgumentMatchers.isInstanceOf(Map.class))
                .andReturn(mock).andCallRealMethod());

        // then
        // Map returned should be empty
        assertEquals(true, actual.isEmpty());
    }

    @Test
    public void testAnswerHashMapType() {
        // given
        Object mock = mock(HashMap.class);

        // when
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Map<Object, Object> actual = (Map<Object, Object>) returnsEmptyValues.answer(Mockito.argumentThat(ArgumentMatchers.isInstanceOf(HashMap.class))
                .andReturn(mock).andCallRealMethod());

        // then
        // HashMap returned should be empty
        assertEquals(true, actual.isEmpty());
    }

    @Test
    public void testAnswerSortedMapType() {
        // given
        Object mock = mock(SortedMap.class);

        // when
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        SortedMap<Object, Object> actual = (SortedMap<Object, Object>) returnsEmptyValues.answer(Mockito.argumentThat(ArgumentMatchers.isInstanceOf(SortedMap.class))
                .andReturn(mock).andCallRealMethod());

        // then
        // SortedMap returned should be empty
        assertEquals(true, actual.isEmpty());
    }

    @Test
    public void testAnswerTreeMapType() {
        // given
        Object mock = mock(TreeMap.class);

        // when
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        SortedMap<Object, Object> actual = (SortedMap<Object, Object>) returnsEmptyValues.answer(Mockito.argumentThat(ArgumentMatchers.isInstanceOf(TreeMap.class))
                .andReturn(mock).andCallRealMethod());

        // then
        // TreeMap returned should be empty
        assertEquals(true, actual.isEmpty());
    }

    @Test
    public void testAnswerLinkedHashMapType() {
        // given
        Object mock = mock(LinkedHashMap.class);

        // when
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Map<Object, Object> actual = (Map<Object, Object>) returnsEmptyValues.answer(Mockito.argumentThat(ArgumentMatchers.isInstanceOf(LinkedHashMap.class))
                .andReturn(mock).andCallRealMethod());

        // then
        // LinkedHashMap returned should be empty
        assertEquals(true, actual.isEmpty());
    }
}