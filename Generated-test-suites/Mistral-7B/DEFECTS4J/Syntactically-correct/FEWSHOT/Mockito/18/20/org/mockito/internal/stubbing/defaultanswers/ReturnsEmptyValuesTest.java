package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
import org.mockito.Mockito;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer_toString() {
        Object mock = Mockito.mock(Object.class);
        assertEquals("Mock for Object, hashCode: " + mock.hashCode(), ReturnsEmptyValues.answer(Mockito.method(Object.class, "toString", (Class[]) null, new Object[0], Mockito.any(InvocationOnMock.class))));
    }

    @Test
    public void testAnswer_compareTo() {
        Object mock1 = Mockito.mock(Comparable.class);
        Object mock2 = Mockito.mock(Comparable.class);
        assertEquals(0, ReturnsEmptyValues.answer(Mockito.method(Comparable.class, "compareTo", Comparable.class, new Object[] { mock1 }, Mockito.any(InvocationOnMock.class))).intValue());
        assertEquals(1, ReturnsEmptyValues.answer(Mockito.method(Comparable.class, "compareTo", Comparable.class, new Object[] { mock2 }, Mockito.any(InvocationOnMock.class))).intValue());
    }

    @Test
    public void testReturnValueFor_primitive() {
        for (Class<?> type : Primitives.PRIMITIVE_TYPES) {
            assertEquals(defaultValueForPrimitiveOrWrapper(type), ReturnsEmptyValues.returnValueFor(type));
        }

        for (Class<?> wrapperType : Primitives.WRAPPER_TYPES) {
            Object value = ReturnsEmptyValues.returnValueFor(wrapperType);
            assertEquals(defaultValueForPrimitiveOrWrapper(wrapperType.getComponentType()), value);
        }
    }

    @Test
    public void testReturnValueFor_Collection() {
        Collection<?> collection = ReturnsEmptyValues.returnValueFor(Collection.class);
        assertNotNull(collection);
        assertEquals(0, collection.size());
    }

    @Test
    public void testReturnValueFor_Set() {
        Set<?> set = ReturnsEmptyValues.returnValueFor(Set.class);
        assertNotNull(set);
        assertEquals(0, set.size());
    }

    @Test
    public void testReturnValueFor_HashSet() {
        Set<?> set = ReturnsEmptyValues.returnValueFor(HashSet.class);
        assertNotNull(set);
        assertEquals(0, set.size());
    }

    @Test
    public void testReturnValueFor_SortedSet() {
        SortedSet<?> sortedSet = ReturnsEmptyValues.returnValueFor(SortedSet.class);
        assertNotNull(sortedSet);
        assertEquals(0, sortedSet.size());
    }

    @Test
    public void testReturnValueFor_TreeSet() {
        SortedSet<?> sortedSet = ReturnsEmptyValues.returnValueFor(TreeSet.class);
        assertNotNull(sortedSet);
        assertEquals(0, sortedSet.size());
    }

    @Test
    public void testReturnValueFor_LinkedHashSet() {
        Set<?> set = ReturnsEmptyValues.returnValueFor(LinkedHashSet.class);
        assertNotNull(set);
        assertEquals(0, set.size());
    }

    @Test
    public void testReturnValueFor_List() {
        List<?> list = ReturnsEmptyValues.returnValueFor(List.class);
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void testReturnValueFor_LinkedList() {
        List<?> list = ReturnsEmptyValues.returnValueFor(LinkedList.class);
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void testReturnValueFor_ArrayList() {
        List<?> list = ReturnsEmptyValues.returnValueFor(ArrayList.class);
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void testReturnValueFor_Map() {
        Map<?, ?> map = ReturnsEmptyValues.returnValueFor(Map.class);
        assertNotNull(map);
        assertEquals(0, map.size());
    }

    @Test
    public void testReturnValueFor_HashMap() {
        Map<?, ?> map = ReturnsEmptyValues.returnValueFor(HashMap.class);
        assertNotNull(map);
        assertEquals(0, map.size());
    }

    @Test
    public void testReturnValueFor_SortedMap() {
        SortedMap<?, ?> sortedMap = ReturnsEmptyValues.returnValueFor(SortedMap.class);
        assertNotNull(sortedMap);
        assertEquals(0, sortedMap.size());
    }

    @Test
    public void testReturnValueFor_TreeMap() {
        SortedMap<?, ?> sortedMap = ReturnsEmptyValues.returnValueFor(TreeMap.class);
        assertNotNull(sortedMap);
        assertEquals(0, sortedMap.size());
    }

    @Test
    public void testReturnValueFor_LinkedHashMap() {
        Map<?, ?> map = ReturnsEmptyValues.returnValueFor(LinkedHashMap.class);
        assertNotNull(map);
        assertEquals(0, map.size());
    }
}