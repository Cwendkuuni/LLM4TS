package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;

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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        Object mock = mock(String.class);
        Answer answer = new ReturnsEmptyValues();
        when(mock.toString()).thenAnswer(answer);

        String actuallyReturnedValue = (String) mock.toString();
        String expectedReturnedValue = "Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode();

        Assert.assertEquals(expectedReturnedValue, actuallyReturnedValue);
        verify(mock).toString();
    }

    @Test
    public void testCompareToMethod() {
        Object mock1 = mock(Comparable.class);
        Object mock2 = mock(Comparable.class);
        Answer answer = new ReturnsEmptyValues();

        when(mock1.compareTo(mock2)).thenAnswer(answer);

        int actuallyReturnedValue = mock1.compareTo(mock2);
        int expectedReturnedValue = 1;

        Assert.assertEquals(expectedReturnedValue, actuallyReturnedValue);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCollectionModificationThrowsException() {
        Object mock = mock(Collection.class);
        Answer answer = new ReturnsEmptyValues();
        when(mock.add(new Object())).thenAnswer(answer);

        List<Object> emptyList = (List<Object>) mock;
        emptyList.add(new Object());
    }

    @Test
    public void testCollectionIsEmpty() {
        Object mock = mock(Collection.class);
        Answer answer = new ReturnsEmptyValues();
        when(mock.isEmpty()).thenReturn(true);
        when(mock.size()).thenReturn(0);

        Assert.assertTrue(mock.isEmpty());
        Assert.assertEquals(0, mock.size());
    }

    @Test
    public void testSetIsEmpty() {
        Object mock = mock(Set.class);
        Answer answer = new ReturnsEmptyValues();
        when(mock.isEmpty()).thenReturn(true);
        when(mock.size()).thenReturn(0);

        Assert.assertTrue(mock.isEmpty());
        Assert.assertEquals(0, mock.size());
    }

    @Test
    public void testHashSetIsEmpty() {
        Object mock = mock(HashSet.class);
        Answer answer = new ReturnsEmptyValues();
        when(mock.isEmpty()).thenReturn(true);
        when(mock.size()).thenReturn(0);

        Assert.assertTrue(mock.isEmpty());
        Assert.assertEquals(0, mock.size());
    }

    @Test
    public void testTreeSetIsEmpty() {
        Object mock = mock(SortedSet.class);
        Answer answer = new ReturnsEmptyValues();
        when(mock.isEmpty()).thenReturn(true);
        when(mock.size()).thenReturn(0);
        when(mock.comparator()).thenReturn(null);

        Assert.assertTrue(mock.isEmpty());
        Assert.assertEquals(0, mock.size());
    }

    @Test
    public void testLinkedHashSetIsEmpty() {
        Object mock = mock(LinkedHashSet.class);
        Answer answer = new ReturnsEmptyValues();
        when(mock.isEmpty()).thenReturn(true);
        when(mock.size()).thenReturn(0);

        Assert.assertTrue(mock.isEmpty());
        Assert.assertEquals(0, mock.size());
    }

    @Test
    public void testLinkedListIsEmpty() {
        Object mock = mock(List.class);
        Answer answer = new ReturnsEmptyValues();
        when(mock.isEmpty()).thenReturn(true);
        when(mock.size()).thenReturn(0);

        Assert.assertTrue(mock.isEmpty());
        Assert.assertEquals(0, mock.size());
    }

    @Test
    public void testLinkedListIsEmptyWhenListInterface() {
        Object mock = mock(List.class);
        Answer answer = new ReturnsEmptyValues();
        when(mock.isEmpty()).thenReturn(true);
        when(mock.size()).thenReturn(0);

        List<Object> emptyList = (List<Object>) mock;

        Assert.assertTrue(emptyList.isEmpty());
        Assert.assertEquals(0, emptyList.size());
    }

    @Test
    public void testArrayListIsEmpty() {
        Object mock = mock(List.class);
        Answer answer = new ReturnsEmptyValues();
        when(mock.isEmpty()).thenReturn(true);
        when(mock.size()).thenReturn(0);

        List<Object> emptyList = (List<Object>) mock;

        Assert.assertTrue(emptyList.isEmpty());
        Assert.assertEquals(0, emptyList.size());
    }

    @Test
    public void testMapIsEmpty() {
        Object mock = mock(Map.class);
        Answer answer = new ReturnsEmptyValues();
        when(mock.isEmpty()).thenReturn(true);
        when(mock.size()).thenReturn(0);

        Map<String, Object> emptyMap = (Map<String, Object>) mock;

        Assert.assertTrue(emptyMap.isEmpty());
        Assert.assertEquals(0, emptyMap.size());
    }

    @Test
    public void testHashMapIsEmpty() {
        Object mock = mock(Map.class);
        Answer answer = new ReturnsEmptyValues();
        when(mock.isEmpty()).thenReturn(true);
        when(mock.size()).thenReturn(0);

        Map<String, Object> emptyMap = (Map<String, Object>) mock;

        Assert.assertTrue(emptyMap.isEmpty());
        Assert.assertEquals(0, emptyMap.size());
    }

    @Test
    public void testTreeMapIsEmpty() {
        Object mock = mock(Map.class);
        Answer answer = new ReturnsEmptyValues();
        when(mock.isEmpty()).thenReturn(true);
        when(mock.size()).thenReturn(0);
        when(mock.comparator()).thenReturn(null);

        SortedMap<String, Object> emptyMap = (SortedMap<String, Object>) mock;

        Assert.assertTrue(emptyMap.isEmpty());
        Assert.assertEquals(0, emptyMap.size());
    }

    @Test
    public void testTreeMapIsEmptyWhenSortedMapInterface() {
        Object mock = mock(Map.class);
        Answer answer = new ReturnsEmptyValues();
        when(mock.isEmpty()).thenReturn(true);
        when(mock.size()).thenReturn(0);
        when(mock.comparator()).thenReturn(null);

        SortedMap<String, Object> emptyMap = (SortedMap<String, Object>) mock;

        Assert.assertTrue(emptyMap.isEmpty());
        Assert.assertEquals(0, emptyMap.size());
    }

    @Test
    public void testLinkedHashMapIsEmpty() {
        Object mock = mock(Map.class);
        Answer answer = new ReturnsEmptyValues();
        when(mock.isEmpty()).thenReturn(true);
        when(mock.size()).thenReturn(0);

        Map<String, Object> emptyMap = (Map<String, Object>) mock;

        Assert.assertTrue(emptyMap.isEmpty());
        Assert.assertEquals(0, emptyMap.size());
    }

    @Test
    public void testLinkedHashMapIsEmptyWhenMapInterface() {
        Object mock = mock(Map.class);
        Answer answer = new ReturnsEmptyValues();
        when(mock.isEmpty()).thenReturn(true);
        when(mock.size()).thenReturn(0);

        Map<String, Object> emptyMap = (Map<String, Object>) mock;

        Assert.assertTrue(emptyMap.isEmpty());
        Assert.assertEquals(0, emptyMap.size());
    }

    @Test
    public void testNullReturnedForOtherTypes() {
        Object mock = mock(Object.class);
        Answer answer = new ReturnsEmptyValues();

        Object actualReturnedValue = answer.answer(new InvocationOnMock(mock, new Object() {}.getClass().getMethod("toString")));

        Assert.assertNull(actualReturnedValue);
    }
}