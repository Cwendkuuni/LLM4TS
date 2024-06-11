package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockito.ArgumentsMatchers;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;

import java.util.ArrayList;
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
import static org.mockito.Mockito.when;

public class ReturnsEmptyValuesTest {

    @Test
    public void testToString() {
        Object mock = mock(String.class);
        ReturnsEmptyValues answer = Mockito.answer(new ReturnsEmptyValues());
        when(mock.toString()).thenAnswer(answer);

        String result = mock.toString();
        assertEquals("Mock for " + String.class.getSimpleName() + ", hashCode: " + mock.hashCode(), result);
        verify(mock).toString();
    }

    @Test
    public void testCompareTo() {
        Object mock1 = mock(Comparable.class);
        Object mock2 = mock(Comparable.class);
        ReturnsEmptyValues answer = Mockito.answer(new ReturnsEmptyValues());

        when(mock1.compareTo(mock2)).thenAnswer(answer);
        when(mock2.compareTo(mock1)).thenAnswer(answer);

        assertEquals(1, mock1.compareTo(mock2));
        assertEquals(-1, mock2.compareTo(mock1));
        assertEquals(0, mock1.compareTo(mock1));
        assertEquals(0, mock2.compareTo(mock2));
    }

    @Test
    public void testEmptyCollection() {
        Object mock = mock(Collection.class);
        ReturnsEmptyValues answer = Mockito.answer(new ReturnsEmptyValues());
        when(mock.isEmpty()).thenReturn(true);
        when(mock.size()).thenReturn(0);
        when(mock.iterator()).thenAnswer(answer);

        List<Object> list = (List<Object>) mock;
        assertNotNull(list);
        assertEquals(0, list.size());
        assertEquals(false, list.isEmpty());
        assertNotNull(list.iterator());
        assertFalse(list.iterator().hasNext());
    }

    @Test
    public void testEmptySet() {
        Object mock = mock(Set.class);
        ReturnsEmptyValues answer = Mockito.answer(new ReturnsEmptyValues());
        when(mock.isEmpty()).thenReturn(true);
        when(mock.size()).thenReturn(0);
        when(mock.iterator()).thenAnswer(answer);

        Set<Object> set = (Set<Object>) mock;
        assertNotNull(set);
        assertEquals(0, set.size());
        assertEquals(true, set.isEmpty());
        assertNotNull(set.iterator());
        assertFalse(set.iterator().hasNext());
    }

    @Test
    public void testEmptyHashSet() {
        Object mock = mock(HashSet.class);
        ReturnsEmptyValues answer = Mockito.answer(new ReturnsEmptyValues());
        when(mock.isEmpty()).thenReturn(true);
        when(mock.size()).thenReturn(0);
        when(mock.iterator()).thenAnswer(answer);

        HashSet<Object> hashSet = (HashSet<Object>) mock;
        assertNotNull(hashSet);
        assertEquals(0, hashSet.size());
        assertEquals(true, hashSet.isEmpty());
        assertNotNull(hashSet.iterator());
        assertFalse(hashSet.iterator().hasNext());
    }

    @Test
    public void testEmptyTreeSet() {
        Object mock = mock(TreeSet.class);
        ReturnsEmptyValues answer = Mockito.answer(new ReturnsEmptyValues());
        when(mock.isEmpty()).thenReturn(true);
        when(mock.size()).thenReturn(0);
        when(mock.iterator()).thenAnswer(answer);

        SortedSet<Object> treeSet = (SortedSet<Object>) mock;
        assertNotNull(treeSet);
        assertEquals(0, treeSet.size());
        assertEquals(true, treeSet.isEmpty());
        assertNotNull(treeSet.iterator());
        assertFalse(treeSet.iterator().hasNext());
    }

    @Test
    public void testEmptyLinkedHashSet() {
        Object mock = mock(LinkedHashSet.class);
        ReturnsEmptyValues answer = Mockito.answer(new ReturnsEmptyValues());
        when(mock.isEmpty()).thenReturn(true);
        when(mock.size()).thenReturn(0);
        when(mock.iterator()).thenAnswer(answer);

        LinkedHashSet<Object> linkedHashSet = (LinkedHashSet<Object>) mock;
        assertNotNull(linkedHashSet);
        assertEquals(0, linkedHashSet.size());
        assertEquals(true, linkedHashSet.isEmpty());
        assertNotNull(linkedHashSet.iterator());
        assertFalse(linkedHashSet.iterator().hasNext());
    }

    @Test
    public void testEmptyList() {
        Object mock = mock(List.class);
        ReturnsEmptyValues answer = Mockito.answer(new ReturnsEmptyValues());
        when(mock.isEmpty()).thenReturn(true);
        when(mock.size()).thenReturn(0);
        when(mock.iterator()).thenAnswer(answer);

        List<Object> list = (List<Object>) mock;
        assertNotNull(list);
        assertEquals(0, list.size());
        assertEquals(true, list.isEmpty());
        assertNotNull(list.iterator());
        assertFalse(list.iterator().hasNext());
    }

    @Test
    public void testEmptyLinkedList() {
        Object mock = mock(LinkedList.class);
        ReturnsEmptyValues answer = Mockito.answer(new ReturnsEmptyValues());
        when(mock.isEmpty()).thenReturn(true);
        when(mock.size()).thenReturn(0);
        when(mock.iterator()).thenAnswer(answer);

        List<Object> linkedList = (List<Object>) mock;
        assertNotNull(linkedList);
        assertEquals(0, linkedList.size());
        assertEquals(true, linkedList.isEmpty());
        assertNotNull(linkedList.iterator());
        assertFalse(linkedList.iterator().hasNext());
    }

    @Test
    public void testEmptyArrayList() {
        Object mock = mock(ArrayList.class);
        ReturnsEmptyValues answer = Mockito.answer(new ReturnsEmptyValues());
        when(mock.isEmpty()).thenReturn(true);
        when(mock.size()).thenReturn(0);
        when(mock.iterator()).thenAnswer(answer);

        List<Object> arrayList = (List<Object>) mock;
        assertNotNull(arrayList);
        assertEquals(0, arrayList.size());
        assertEquals(true, arrayList.isEmpty());
        assertNotNull(arrayList.iterator());
        assertFalse(arrayList.iterator().hasNext());
    }

    @Test
    public void testEmptyMap() {
        Object mock = mock(Map.class);
        ReturnsEmptyValues answer = Mockito.answer(new ReturnsEmptyValues());
        when(mock.isEmpty()).thenReturn(true);
        when(mock.size()).thenReturn(0);

        Map<Object, Object> map = (Map<Object, Object>) mock;
        assertNotNull(map);
        assertEquals(0, map.size());
        assertEquals(true, map.isEmpty());
        assertNull(map.get("key"));
        assertFalse(map.containsKey("key"));
        assertFalse(map.containsValue("value"));
    }

    @Test
    public void testEmptyHashMap() {
        Object mock = mock(HashMap.class);
        ReturnsEmptyValues answer = Mockito.answer(new ReturnsEmptyValues());
        when(mock.isEmpty()).thenReturn(true);
        when(mock.size()).thenReturn(0);

        Map<Object, Object> hashMap = (Map<Object, Object>) mock;
        assertNotNull(hashMap);
        assertEquals(0, hashMap.size());
        assertEquals(true, hashMap.isEmpty());
        assertNull(hashMap.get("key"));
        assertFalse(hashMap.containsKey("key"));
        assertFalse(hashMap.containsValue("value"));
    }

    @Test
    public void testEmptyTreeMap() {
        Object mock = mock(TreeMap.class);
        ReturnsEmptyValues answer = Mockito.answer(new ReturnsEmptyValues());
        when(mock.isEmpty()).thenReturn(true);
        when(mock.size()).thenReturn(0);

        SortedMap<Object, Object> treeMap = (SortedMap<Object, Object>) mock;
        assertNotNull(treeMap);
        assertEquals(0, treeMap.size());
        assertEquals(true, treeMap.isEmpty());
        assertNull(treeMap.get("key"));
        assertFalse(treeMap.containsKey("key"));
        assertFalse(treeMap.containsValue("value"));
    }

    @Test
    public void testEmptyLinkedHashMap() {
        Object mock = mock(LinkedHashMap.class);
        ReturnsEmptyValues answer = Mockito.answer(new ReturnsEmptyValues());
        when(mock.isEmpty()).thenReturn(true);
        when(mock.size()).thenReturn(0);

        Map<Object, Object> linkedHashMap = (Map<Object, Object>) mock;
        assertNotNull(linkedHashMap);
        assertEquals(0, linkedHashMap.size());
        assertEquals(true, linkedHashMap.isEmpty());
        assertNull(linkedHashMap.get("key"));
        assertFalse(linkedHashMap.containsKey("key"));
        assertFalse(linkedHashMap.containsValue("value"));
    }
}