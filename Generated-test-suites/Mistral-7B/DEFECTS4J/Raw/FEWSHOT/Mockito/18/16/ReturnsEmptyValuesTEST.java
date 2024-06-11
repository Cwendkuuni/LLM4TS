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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerToStringMethod() {
        Object mock = mock(String.class);
        ReturnsEmptyValues answers = new ReturnsEmptyValues();

        assertEquals("Mock for java.lang.String, hashCode: " + mock.hashCode(), answers.answer(new InvocationOnMock(null, null, null, null, null, null, null, null, new Object[]{}, new Class[0], "toString()")));
    }

    @Test
    public void testAnswerCompareToMethod() {
        Object mock = mock(Comparable.class);
        ReturnsEmptyValues answers = new ReturnsEmptyValues();

        // test equals case
        assertEquals(0, answers.answer(new InvocationOnMock(null, null, null, null, null, null, null, null, new Object[] { mock }, new Class[] { Object.class }, "compareTo(Object)")).compareTo(mock));

        // test not equals case
        Object other = mock(Comparable.class);
        assertEquals(1, answers.answer(new InvocationOnMock(null, null, null, null, null, null, null, null, new Object[] { mock }, new Class[] { Object.class }, "compareTo(Object)")).compareTo(other));
    }

    @Test
    public void testAnswerPrimitiveTypes() {
        ReturnsEmptyValues answers = new ReturnsEmptyValues();

        byte byteValue = answers.returnValueFor(Byte.TYPE);
        assertEquals((byte) 0, byteValue);

        short shortValue = answers.returnValueFor(Short.TYPE);
        assertEquals((short) 0, shortValue);

        int intValue = answers.returnValueFor(Integer.TYPE);
        assertEquals(0, intValue);

        long longValue = answers.returnValueFor(Long.TYPE);
        assertEquals(0L, longValue);

        float floatValue = answers.returnValueFor(Float.TYPE);
        assertEquals(0.0f, floatValue, 0.001f);

        double doubleValue = answers.returnValueFor(Double.TYPE);
        assertEquals(0.0d, doubleValue, 0.001d);

        char charValue = answers.returnValueFor(Character.TYPE);
        assertEquals('\u0000', charValue);

        boolean booleanValue = answers.returnValueFor(Boolean.TYPE);
        assertEquals(false, booleanValue);
    }

    @Test
    public void testAnswerCollectionTypes() {
        ReturnsEmptyValues answers = new ReturnsEmptyValues();

        Collection<?> collection = answers.returnValueFor(Collection.class);
        assertNotNull(collection);
        assertEquals(0, collection.size());

        Set<?> set = answers.returnValueFor(Set.class);
        assertNotNull(set);
        assertEquals(0, set.size());

        HashSet<?> hashSet = answers.returnValueFor(HashSet.class);
        assertNotNull(hashSet);
        assertEquals(0, hashSet.size());

        LinkedHashSet<?> linkedHashSet = answers.returnValueFor(LinkedHashSet.class);
        assertNotNull(linkedHashSet);
        assertEquals(0, linkedHashSet.size());

        List<?> list = answers.returnValueFor(List.class);
        assertNotNull(list);
        assertEquals(0, list.size());

        LinkedList<?> linkedList = answers.returnValueFor(LinkedList.class);
        assertNotNull(linkedList);
        assertEquals(0, linkedList.size());

        ArrayList<?> arrayList = answers.returnValueFor(ArrayList.class);
        assertNotNull(arrayList);
        assertEquals(0, arrayList.size());
    }

    @Test
    public void testAnswerMapTypes() {
        ReturnsEmptyValues answers = new ReturnsEmptyValues();

        Map<String, String> map = answers.returnValueFor(Map.class);
        assertNotNull(map);
        assertEquals(0, map.size());

        HashMap<String, String> hashMap = answers.returnValueFor(HashMap.class);
        assertNotNull(hashMap);
        assertEquals(0, hashMap.size());

        TreeMap<String, String> treeMap = answers.returnValueFor(TreeMap.class);
        assertNotNull(treeMap);
        assertEquals(0, treeMap.size());

        LinkedHashMap<String, String> linkedHashMap = answers.returnValueFor(LinkedHashMap.class);
        assertNotNull(linkedHashMap);
        assertEquals(0, linkedHashMap.size());
    }
}