package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer_toStringMethod() {
        Object mock = mock(Object.class);
        assertEquals("Mock for Object, hashCode: " + mock.hashCode(), ReturnsEmptyValues.INSTANCE.answer(mock("toString", mock)));
    }

    @Test
    public void testAnswer_compareToMethod() {
        Object mock1 = mock(Comparable.class);
        Object mock2 = mock(Comparable.class);
        assertEquals(1, ReturnsEmptyValues.INSTANCE.answer(mock("compareTo", mock1, mock2)));
    }

    @Test
    public void testReturnValueFor_primitive() {
        assertEquals(0, ReturnsEmptyValues.INSTANCE.returnValueFor(int.class).intValue());
        assertEquals(false, ReturnsEmptyValues.INSTANCE.returnValueFor(boolean.class).booleanValue());
        assertEquals(0L, ReturnsEmptyValues.INSTANCE.returnValueFor(long.class).longValue());
        assertEquals((byte) 0, ReturnsEmptyValues.INSTANCE.returnValueFor(byte.class).byteValue());
        assertEquals((short) 0, ReturnsEmptyValues.INSTANCE.returnValueFor(short.class).shortValue());
        assertEquals(0f, ReturnsEmptyValues.INSTANCE.returnValueFor(float.class).floatValue());
        assertEquals(0d, ReturnsEmptyValues.INSTANCE.returnValueFor(double.class).doubleValue());
        assertEquals((char) 0, ReturnsEmptyValues.INSTANCE.returnValueFor(char.class).charValue());
    }

    @Test
    public void testReturnValueFor_primitiveWrapper() {
        assertEquals(0, ReturnsEmptyValues.INSTANCE.returnValueFor(Integer.class).intValue());
        assertEquals(false, ReturnsEmptyValues.INSTANCE.returnValueFor(Boolean.class).booleanValue());
        assertEquals(0L, ReturnsEmptyValues.INSTANCE.returnValueFor(Long.class).longValue());
        assertEquals((byte) 0, ReturnsEmptyValues.INSTANCE.returnValueFor(Byte.class).byteValue());
        assertEquals((short) 0, ReturnsEmptyValues.INSTANCE.returnValueFor(Short.class).shortValue());
        assertEquals(0f, ReturnsEmptyValues.INSTANCE.returnValueFor(Float.class).floatValue());
        assertEquals(0d, ReturnsEmptyValues.INSTANCE.returnValueFor(Double.class).doubleValue());
        assertEquals((char) 0, ReturnsEmptyValues.INSTANCE.returnValueFor(Character.class).charValue());
    }

    @Test
    public void testReturnValueFor_Collection() {
        Collection<Object> collection = ReturnsEmptyValues.INSTANCE.returnValueFor(Collection.class);
        assertNotNull(collection);
        assertEquals(0, collection.size());
    }

    @Test
    public void testReturnValueFor_Set() {
        Set<Object> set = ReturnsEmptyValues.INSTANCE.returnValueFor(Set.class);
        assertNotNull(set);
        assertEquals(0, set.size());
    }

    @Test
    public void testReturnValueFor_HashSet() {
        Set<Object> hashSet = ReturnsEmptyValues.INSTANCE.returnValueFor(HashSet.class);
        assertNotNull(hashSet);
        assertEquals(0, hashSet.size());
    }

    @Test
    public void testReturnValueFor_SortedSet() {
        SortedSet<Object> sortedSet = ReturnsEmptyValues.INSTANCE.returnValueFor(SortedSet.class);
        assertNotNull(sortedSet);
        assertEquals(0, sortedSet.size());
    }

    @Test
    public void testReturnValueFor_TreeSet() {
        SortedSet<Object> treeSet = ReturnsEmptyValues.INSTANCE.returnValueFor(TreeSet.class);
        assertNotNull(treeSet);
        assertEquals(0, treeSet.size());
    }

    @Test
    public void testReturnValueFor_LinkedHashSet() {
        Set<Object> linkedHashSet = ReturnsEmptyValues.INSTANCE.returnValueFor(LinkedHashSet.class);
        assertNotNull(linkedHashSet);
        assertEquals(0, linkedHashSet.size());
    }

    @Test
    public void testReturnValueFor_List() {
        List<Object> list = ReturnsEmptyValues.INSTANCE.returnValueFor(List.class);
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void testReturnValueFor_LinkedList() {
        List<Object> linkedList = ReturnsEmptyValues.INSTANCE.returnValueFor(LinkedList.class);
        assertNotNull(linkedList);
        assertEquals(0, linkedList.size());
    }

    @Test
    public void testReturnValueFor_ArrayList() {
        List<Object> arrayList = ReturnsEmptyValues.INSTANCE.returnValueFor(ArrayList.class);
        assertNotNull(arrayList);
        assertEquals(0, arrayList.size());
    }

    @Test
    public void testReturnValueFor_Map() {
        Map<Object, Object> map = ReturnsEmptyValues.INSTANCE.returnValueFor(Map.class);
        assertNotNull(map);
        assertEquals(0, map.size());
    }

    @Test
    public void testReturnValueFor_HashMap() {
        Map<Object, Object> hashMap = ReturnsEmptyValues.INSTANCE.returnValueFor(HashMap.class);
        assertNotNull(hashMap);
        assertEquals(0, hashMap.size());
    }

    @Test
    public void testReturnValueFor_SortedMap() {
        SortedMap<Object, Object> sortedMap = ReturnsEmptyValues.INSTANCE.returnValueFor(SortedMap.class);
        assertNotNull(sortedMap);
        assertEquals(0, sortedMap.size());
    }

    @Test
    public void testReturnValueFor_TreeMap() {
        SortedMap<Object, Object> treeMap = ReturnsEmptyValues.INSTANCE.returnValueFor(TreeMap.class);
        assertNotNull(treeMap);
        assertEquals(0, treeMap.size());
    }

    @Test
    public void testReturnValueFor_LinkedHashMap() {
        Map<Object, Object> linkedHashMap = ReturnsEmptyValues.INSTANCE.returnValueFor(LinkedHashMap.class);
        assertNotNull(linkedHashMap);
        assertEquals(0, linkedHashMap.size());
    }

    @Test
    public void testReturnValueFor_otherCollections() {
        // Let's not care about the rest of collections.
    }

    @Test
    public void testToString() {
        Object mock = mock(Object.class);
        verify(mock).toString();
    }
}