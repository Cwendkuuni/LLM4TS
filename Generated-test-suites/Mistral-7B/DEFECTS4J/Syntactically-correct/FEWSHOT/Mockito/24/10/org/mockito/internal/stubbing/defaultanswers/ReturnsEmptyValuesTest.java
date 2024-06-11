package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.internal.util.Primitives.isPrimitiveOrWrapper;
import static org.mockito.internal.util.Primitives.defaultValueForPrimitiveOrWrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;
import org.mockito.internal.stubbing.Answer;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsEmptyValuesTest {
    @Test
    public void testAnswer_toString() {
        Object mock = new Object();
        InvocationOnMock invocation = mock(Object.class).invoking(Object.class.getDeclaredMethod("toString"));
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        assertEquals("Mock for Object, hashCode: " + mock.hashCode(), returnsEmptyValues.answer(invocation));
    }

    @Test
    public void testAnswer_compareTo() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        InvocationOnMock invocation = mock(Comparable.class).invoking("compareTo", obj1);
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        assertEquals(1, returnsEmptyValues.answer(invocation).intValue());
    }

    @Test
    public void testReturnValueFor_primitiveOrWrapper() {
        for (Class<?> type : new Class<?>[] { byte.class, short.class, int.class, long.class, char.class, float.class, double.class, boolean.class, Byte.class, Short.class, Integer.class, Long.class, Character.class, Float.class, Double.class, Boolean.class }) {
            assertEquals(defaultValueForPrimitiveOrWrapper(type), new ReturnsEmptyValues().returnValueFor(type));
        }
    }

    @Test
    public void testReturnValueFor_Collection() {
        Collection<String> collection = new ReturnsEmptyValues().returnValueFor(Collection.class);
        assertEquals(new LinkedList<String>(), collection);
    }

    @Test
    public void testReturnValueFor_Set() {
        Set<String> set = new ReturnsEmptyValues().returnValueFor(Set.class);
        assertEquals(new HashSet<String>(), set);
    }

    @Test
    public void testReturnValueFor_HashSet() {
        Set<String> set = new ReturnsEmptyValues().returnValueFor(HashSet.class);
        assertEquals(new HashSet<String>(), set);
    }

    @Test
    public void testReturnValueFor_SortedSet() {
        SortedSet<String> sortedSet = new ReturnsEmptyValues().returnValueFor(SortedSet.class);
        assertEquals(new TreeSet<String>(), sortedSet);
    }

    @Test
    public void testReturnValueFor_TreeSet() {
        SortedSet<String> sortedSet = new ReturnsEmptyValues().returnValueFor(TreeSet.class);
        assertEquals(new TreeSet<String>(), sortedSet);
    }

    @Test
    public void testReturnValueFor_LinkedHashSet() {
        Set<String> set = new ReturnsEmptyValues().returnValueFor(LinkedHashSet.class);
        assertEquals(new LinkedHashSet<String>(), set);
    }

    @Test
    public void testReturnValueFor_List() {
        List<String> list = new ReturnsEmptyValues().returnValueFor(List.class);
        assertEquals(new LinkedList<String>(), list);
    }

    @Test
    public void testReturnValueFor_LinkedList() {
        List<String> list = new ReturnsEmptyValues().returnValueFor(LinkedList.class);
        assertEquals(new LinkedList<String>(), list);
    }

    @Test
    public void testReturnValueFor_ArrayList() {
        List<String> list = new ReturnsEmptyValues().returnValueFor(ArrayList.class);
        assertEquals(new ArrayList<String>(), list);
    }

    @Test
    public void testReturnValueFor_Map() {
        Map<String, String> map = new ReturnsEmptyValues().returnValueFor(Map.class);
        assertEquals(new HashMap<String, String>(), map);
    }

    @Test
    public void testReturnValueFor_HashMap() {
        Map<String, String> map = new ReturnsEmptyValues().returnValueFor(HashMap.class);
        assertEquals(new HashMap<String, String>(), map);
    }

    @Test
    public void testReturnValueFor_SortedMap() {
        SortedMap<String, String> sortedMap = new ReturnsEmptyValues().returnValueFor(SortedMap.class);
        assertEquals(new TreeMap<String, String>(), sortedMap);
    }

    @Test
    public void testReturnValueFor_TreeMap() {
        SortedMap<String, String> sortedMap = new ReturnsEmptyValues().returnValueFor(TreeMap.class);
        assertEquals(new TreeMap<String, String>(), sortedMap);
    }

    @Test
    public void testReturnValueFor_LinkedHashMap() {
        Map<String, String> map = new ReturnsEmptyValues().returnValueFor(LinkedHashMap.class);
        assertEquals(new LinkedHashMap<String, String>(), map);
    }

    @Test
    public void testReturnValueFor_otherTypes() {
        for (Class<?> type : new Class<?>[] { int[].class, int[].class, String[].class, String[].class, String.class, String.class }) {
            assertNull(new ReturnsEmptyValues().returnValueFor(type));
        }
    }
}