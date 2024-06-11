package com.example.mockito; // replace with your test package name

import org.junit.Test;
import org.junit.runner.RunWith;
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

import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(org.mockito.runners.JUnit4.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void test_toString_method() {
        Mockito<String> stringMock = Mockito.mock(String.class);
        Answer answer = new ReturnsEmptyValues();
        String mockName = "mockName";

        verifyZeroInteractions(stringMock); // to ensure mock is not interacted before answer is returned
        String result = (String) answer.answer(Mockito.argThat(i -> i.getMethod().getName().equals("toString")));
        Mockito.verify(stringMock).toString();
        Mockito.verifyNoMoreInteractions(stringMock);

        assert result.contains("Mock for ");
        assert result.contains(mockName);
    }

    @Test
    public void test_compareTo_method() {
        Object object1 = new Object();
        Object object2 = new Object();

        Answer answer = new ReturnsEmptyValues();

        int result = (Integer) answer.answer(Mockito.argThat(i -> i.getMethod().getName().equals("compareTo") && "java.lang.Object".equals(i.getMock().getClass().getName())));

        assert result != 0;
    }

    @Test
    public void test_return_empty_collection() {
        Answer answer = new ReturnsEmptyValues();

        Class<?> collectionType = Collection.class;

        Collection collection = (Collection) answer.answer(Mockito.argThat(i -> i.getMethod().getReturnType().equals(collectionType)));

        assert collection.isEmpty();
    }

    @Test
    public void test_return_empty_set() {
        Answer answer = new ReturnsEmptyValues();

        Class<?> setType = Set.class;

        Set set = (Set) answer.answer(Mockito.argThat(i -> i.getMethod().getReturnType().equals(setType)));

        assert set.isEmpty();
    }

    @Test
    public void test_return_empty_hash_set() {
        Answer answer = new ReturnsEmptyValues();

        Class<?> hashSetType = HashSet.class;

        Set set = (Set) answer.answer(Mockito.argThat(i -> i.getMethod().getReturnType().equals(hashSetType)));

        assert set.isEmpty();
    }

    @Test
    public void test_return_empty_tree_set() {
        Answer answer = new ReturnsEmptyValues();

        Class<?> treeSetType = TreeSet.class;

        Set set = (Set) answer.answer(Mockito.argThat(i -> i.getMethod().getReturnType().equals(treeSetType)));

        assert set.isEmpty();
    }

    @Test
    public void test_return_empty_linked_hash_set() {
        Answer answer = new ReturnsEmptyValues();

        Class<?> linkedHashSetType = LinkedHashSet.class;

        Set set = (Set) answer.answer(Mockito.argThat(i -> i.getMethod().getReturnType().equals(linkedHashSetType)));

        assert set.isEmpty();
    }

    @Test
    public void test_return_empty_list() {
        Answer answer = new ReturnsEmptyValues();

        Class<?> listType = List.class;

        List list = (List) answer.answer(Mockito.argThat(i -> i.getMethod().getReturnType().equals(listType)));

        assert list.isEmpty();
    }

    @Test
    public void test_return_empty_linked_list() {
        Answer answer = new ReturnsEmptyValues();

        Class<?> linkedListType = LinkedList.class;

        List list = (List) answer.answer(Mockito.argThat(i -> i.getMethod().getReturnType().equals(linkedListType)));

        assert list.isEmpty();
    }

    @Test
    public void test_return_empty_array_list() {
        Answer answer = new ReturnsEmptyValues();

        Class<?> arrayListType = ArrayList.class;

        List list = (List) answer.answer(Mockito.argThat(i -> i.getMethod().getReturnType().equals(arrayListType)));

        assert list.isEmpty();
    }

    @Test
    public void test_return_empty_map() {
        Answer answer = new ReturnsEmptyValues();

        Class<?> mapType = Map.class;

        Map map = (Map) answer.answer(Mockito.argThat(i -> i.getMethod().getReturnType().equals(mapType)));

        assert map.isEmpty();
    }

    @Test
    public void test_return_empty_hash_map() {
        Answer answer = new ReturnsEmptyValues();

        Class<?> hashMapType = HashMap.class;

        Map map = (Map) answer.answer(Mockito.argThat(i -> i.getMethod().getReturnType().equals(hashMapType)));

        assert map.isEmpty();
    }

    @Test
    public void test_return_empty_tree_map() {
        Answer answer = new ReturnsEmptyValues();

        Class<?> treeMapType = TreeMap.class;

        Map map = (Map) answer.answer(Mockito.argThat(i -> i.getMethod().getReturnType().equals(treeMapType)));

        assert map.isEmpty();
    }

    @Test
    public void test_return_empty_linked_hash_map() {
        Answer answer = new ReturnsEmptyValues();

        Class<?> linkedHashMapType = LinkedHashMap.class;

        Map map = (Map) answer.answer(Mockito.argThat(i -> i.getMethod().getReturnType().equals(linkedHashMapType)));

        assert map.isEmpty();
    }

}