package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
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

@RunWith(org.mockito.runners.JUnit4.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void test_toString_method() {
        Object mock = mock(String.class, Answers.CALLS_REAL_METHODS);
        Answer answer = new ReturnsEmptyValues();
        Mockito.doAnswer(answer).when(mock).toString();

        assertEquals("Mock for String, hashCode: 123456", mock.toString());
        verify(mock).toString();
    }

    @Test(expected = AssertionError.class)
    public void test_compareTo_method_sameObjects() {
        Object mock1 = mock(Comparable.class);
        Object mock2 = mock(Comparable.class);
        Answer answer = new ReturnsEmptyValues();

        Mockito.doAnswer(answer).when(mock1).compareTo(mock2);

        assertEquals(0, mock1.compareTo(mock2));
    }

    @Test(expected = AssertionError.class)
    public void test_compareTo_method_differentObjects() {
        Object mock1 = mock(Comparable.class);
        Object mock2 = mock(Comparable.class);
        Answer answer = new ReturnsEmptyValues();

        Mockito.doAnswer(answer).when(mock1).compareTo(mock2);

        assertEquals(1, mock1.compareTo(mock2));
    }

    @Test
    public void test_emptyList_method() {
        Object mock = mock(Collection.class, Answers.CALLS_REAL_METHODS);
        Answer answer = new ReturnsEmptyValues();
        Mockito.doAnswer(answer).when(mock).getClass();

        List list = (List) mock;
        assertEquals(0, list.size());
        assertEquals(false, list.isEmpty());
    }

    @Test
    public void test_emptySet_method() {
        Object mock = mock(Set.class, Answers.CALLS_REAL_METHODS);
        Answer answer = new ReturnsEmptyValues();
        Mockito.doAnswer(answer).when(mock).getClass();

        Set set = (Set) mock;
        assertEquals(0, set.size());
        assertEquals(set.isEmpty());
    }

    @Test
    public void test_emptyHashSet_method() {
        Object mock = mock(HashSet.class, Answers.CALLS_REAL_METHODS);
        Answer answer = new ReturnsEmptyValues();
        Mockito.doAnswer(answer).when(mock).getClass();

        Set<Object> hashSet = (Set<Object>) mock;
        assertEquals(0, hashSet.size());
        assertEquals(hashSet.isEmpty());
    }

    @Test
    public void test_emptyTreeSet_method() {
        Object mock = mock(TreeSet.class, Answers.CALLS_REAL_METHODS);
        Answer answer = new ReturnsEmptyValues();
        Mockito.doAnswer(answer).when(mock).getClass();

        SortedSet<Object> treeSet = (SortedSet<Object>) mock;
        assertEquals(0, treeSet.size());
        assertEquals(treeSet.isEmpty());
    }

    @Test
    public void test_emptyTreeMap_method() {
        Object mock = mock(SortedMap.class, Answers.CALLS_REAL_METHODS);
        Answer answer = new ReturnsEmptyValues();
        Mockito.doAnswer(answer).when(mock).getClass();

        SortedMap<Object, Object> treeMap = (SortedMap<Object, Object>) mock;
        assertEquals(0, treeMap.size());
        assertEquals(treeMap.isEmpty());
    }

    @Test
    public void test_emptyHashMap_method() {
        Object mock = mock(Map.class, Answers.CALLS_REAL_METHODS);
        Answer answer = new ReturnsEmptyValues();
        Mockito.doAnswer(answer).when(mock).getClass();

        Map<Object, Object> map = (Map<Object, Object>) mock;
        assertEquals(0, map.size());
        assertEquals(map.isEmpty());
    }

    @Test
    public void test_emptyLinkedHashMap_method() {
        Object mock = mock(LinkedHashMap.class, Answers.CALLS_REAL_METHODS);
        Answer answer = new ReturnsEmptyValues();
        Mockito.doAnswer(answer).when(mock).getClass();

        Map<Object, Object> linkedHashMap = (Map<Object, Object>) mock;
        assertEquals(0, linkedHashMap.size());
        assertEquals(linkedHashMap.isEmpty());
    }

    @Test
    public void test_emptyArrayList_method() {
        Object mock = mock(List.class, Answers.CALLS_REAL_METHODS);
        Answer answer = new ReturnsEmptyValues();
        Mockito.doAnswer(answer).when(mock).getClass();

        List<Object> arrayList = (List<Object>) mock;
        assertEquals(0, arrayList.size());
        assertEquals(arrayList.isEmpty());
    }

    @Test
    public void test_emptyLinkedList_method() {
        Object mock = mock(List.class, Answers.CALLS_REAL_METHODS);
        Answer answer = new ReturnsEmptyValues();
        Mockito.doAnswer(answer).when(mock).getClass();

        List<Object> linkedList = (List<Object>) mock;
        assertEquals(0, linkedList.size());
        assertEquals(linkedList.isEmpty());
    }

    @Test
    public void test_emptyArrayList_method() {
        Object mock = mock(List.class, Answers.CALLS_REAL_METHODS);
        Answer answer = new ReturnsEmptyValues();
        Mockito.doAnswer(answer).when(mock).getClass();

        List<Object> arrayList = (List<Object>) mock;
        assertEquals(0, arrayList.size());
        assertEquals(arrayList.isEmpty());
    }
}