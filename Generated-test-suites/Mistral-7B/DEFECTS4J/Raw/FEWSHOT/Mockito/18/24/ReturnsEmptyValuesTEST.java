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
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.invocation.InvocationOnMock;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testPrimitivesAndWrappers() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);
        assertEquals(0, returnsEmptyValues.answer(ArgumentMatchers.method(Integer.class, "valueOf", new Class[0])).intValue());
        assertEquals(false, returnsEmptyValues.answer(ArgumentMatchers.method(Boolean.class, "valueOf", new Class[0])).booleanValue());
        assertEquals(0L, returnsEmptyValues.answer(ArgumentMatchers.method(Long.class, "valueOf", new Class[0])).longValue());
        assertEquals(0d, returnsEmptyValues.answer(ArgumentMatchers.method(Double.class, "valueOf", new Class[0])).doubleValue());
        assertEquals(0f, returnsEmptyValues.answer(ArgumentMatchers.method(Float.class, "valueOf", new Class[0])).floatValue());
        assertEquals(0, returnsEmptyValues.answer(ArgumentMatchers.method(Byte.class, "valueOf", new Class[0])).byteValue());
        assertEquals(0, returnsEmptyValues.answer(ArgumentMatchers.method(Short.class, "valueOf", new Class[0])).shortValue());
        assertEquals(0, returnsEmptyValues.answer(ArgumentMatchers.method(Character.class, "valueOf", new Class[0])).charValue());
        assertEquals(0, returnsEmptyValues.answer(ArgumentMatchers.method(Byte.TYPE, "MIN_VALUE")).byteValue());
        assertEquals(0, returnsEmptyValues.answer(ArgumentMatchers.method(Short.TYPE, "MIN_VALUE")).shortValue());
        assertEquals(0, returnsEmptyValues.answer(ArgumentMatchers.method(Integer.TYPE, "MIN_VALUE")).intValue());
        assertEquals(0L, returnsEmptyValues.answer(ArgumentMatchers.method(Long.TYPE, "MIN_VALUE")).longValue());
        assertEquals(0f, returnsEmptyValues.answer(ArgumentMatchers.method(Float.TYPE, "MIN_VALUE")).floatValue());
        assertEquals(0d, returnsEmptyValues.answer(ArgumentMatchers.method(Double.TYPE, "MIN_VALUE")).doubleValue());
        assertEquals(0, returnsEmptyValues.answer(ArgumentMatchers.method(Character.TYPE, "MIN_VALUE")).charValue());
    }

    @Test
    public void testEmptyCollections() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);

        assertEquals(0, returnsEmptyValues.returnValueFor(Collection.class).size());
        assertEquals(0, returnsEmptyValues.returnValueFor(Set.class).size());
        assertEquals(0, returnsEmptyValues.returnValueFor(HashSet.class).size());
        assertEquals(0, returnsEmptyValues.returnValueFor(SortedSet.class).size());
        assertEquals(0, returnsEmptyValues.returnValueFor(TreeSet.class).size());
        assertEquals(0, returnsEmptyValues.returnValueFor(LinkedHashSet.class).size());
        assertEquals(0, returnsEmptyValues.returnValueFor(List.class).size());
        assertEquals(0, returnsEmptyValues.returnValueFor(LinkedList.class).size());
        assertEquals(0, returnsEmptyValues.returnValueFor(ArrayList.class).size());

        assertEquals(0, returnsEmptyValues.returnValueFor(Map.class).size());
        assertEquals(0, returnsEmptyValues.returnValueFor(HashMap.class).size());
        assertEquals(0, returnsEmptyValues.returnValueFor(SortedMap.class).size());
        assertEquals(0, returnsEmptyValues.returnValueFor(TreeMap.class).size());
        assertEquals(0, returnsEmptyValues.returnValueFor(LinkedHashMap.class).size());

        Collection<String> collection = returnsEmptyValues.returnValueFor(Collection.class);
        assertNotNull(collection);
        assertNull(collection.iterator().next());

        Set<String> set = returnsEmptyValues.returnValueFor(Set.class);
        assertNotNull(set);
        assertFalse(set.contains("foo"));

        HashSet<String> hashSet = returnsEmptyValues.returnValueFor(HashSet.class);
        assertNotNull(hashSet);
        assertFalse(hashSet.contains("foo"));

        SortedSet<String> sortedSet = returnsEmptyValues.returnValueFor(SortedSet.class);
        assertNotNull(sortedSet);
        assertFalse(sortedSet.contains("foo"));

        TreeSet<String> treeSet = returnsEmptyValues.returnValueFor(TreeSet.class);
        assertNotNull(treeSet);
        assertFalse(treeSet.contains("foo"));

        LinkedHashSet<String> linkedHashSet = returnsEmptyValues.returnValueFor(LinkedHashSet.class);
        assertNotNull(linkedHashSet);
        assertFalse(linkedHashSet.contains("foo"));

        List<String> list = returnsEmptyValues.returnValueFor(List.class);
        assertNotNull(list);
        assertNull(list.get(0));

        LinkedList<String> linkedList = returnsEmptyValues.returnValueFor(LinkedList.class);
        assertNotNull(linkedList);
        assertNull(linkedList.getFirst());

        ArrayList<String> arrayList = returnsEmptyValues.returnValueFor(ArrayList.class);
        assertNotNull(arrayList);
        assertNull(arrayList.get(0));

        Map<String, String> map = returnsEmptyValues.returnValueFor(Map.class);
        assertNotNull(map);
        assertNull(map.get("foo"));

        HashMap<String, String> hashMap = returnsEmptyValues.returnValueFor(HashMap.class);
        assertNotNull(hashMap);
        assertNull(hashMap.get("foo"));

        SortedMap<String, String> sortedMap = returnsEmptyValues.returnValueFor(SortedMap.class);
        assertNotNull(sortedMap);
        assertNull(sortedMap.get("foo"));

        TreeMap<String, String> treeMap = returnsEmptyValues.returnValueFor(TreeMap.class);
        assertNotNull(treeMap);
        assertNull(treeMap.get("foo"));

        LinkedHashMap<String, String> linkedHashMap = returnsEmptyValues.returnValueFor(LinkedHashMap.class);
        assertNotNull(linkedHashMap);
        assertNull(linkedHashMap.get("foo"));
    }

    @Test
    public void testToString() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);
        String toString = returnsEmptyValues.answer(ArgumentMatchers.method(Object.class, "toString")).toString();
        assertEquals("ReturnsEmptyValues@" + Integer.toHexString(System.identityHashCode(returnsEmptyValues)) + ", serialVersionUID=1998191268711234347", toString);
    }

    @Test
    public void testCompareToZeroIfSameReference() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);
        Object object = new Object();
        assertEquals(0, returnsEmptyValues.answer(ArgumentMatchers.method(Object.class, "compareTo", ArgumentMatchers.any(Object.class))).intValue());
        assertEquals(object, returnsEmptyValues.answer(ArgumentMatchers.method(Object.class, "compareTo", ArgumentMatchers.eq(object))).getMock());
    }

    @Test
    public void testCompareToNonZeroIfDifferentReferences() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);
        Object object1 = new Object();
        Object object2 = new Object();
        assertEquals(1, returnsEmptyValues.answer(ArgumentMatchers.method(Object.class, "compareTo", ArgumentMatchers.any(Object.class))).intValue());
        assertEquals(object1, returnsEmptyValues.answer(ArgumentMatchers.method(Object.class, "compareTo", ArgumentMatchers.eq(object1))).getMock());
        assertEquals(object2, returnsEmptyValues.answer(ArgumentMatchers.method(Object.class, "compareTo", ArgumentMatchers.eq(object2))).getMock());
    }

    @Test
    public void testReturnNullForOtherTypes() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);
        Class<?> type = String[].class;
        assertNull(returnsEmptyValues.returnValueFor(type));
    }
}