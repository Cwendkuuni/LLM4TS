package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerForToStringMethod() {
        Object mock = mock(Object.class);
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        when(mock.toString()).thenAnswer(returnsEmptyValues);

        assertEquals("Mock for [class java.lang.Object], hashCode: " + mock.hashCode(), mock.toString());
        verify(mock, ArgumentMatchers.never()).hashCode();
    }

    @Test
    public void testAnswerForCompareToMethod() {
        Object obj1 = mock(Object.class);
        Object obj2 = mock(Object.class);
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        when(obj1.compareTo(ArgumentMatchers.any())).thenAnswer(returnsEmptyValues);
        when(obj2.compareTo(ArgumentMatchers.any())).thenAnswer(returnsEmptyValues);

        assertEquals(0, obj1.compareTo(obj1));
        assertEquals(1, obj1.compareTo(obj2));
        assertEquals(1, obj2.compareTo(obj1));
        verify(obj1, ArgumentMatchers.never()).equals(obj2);
        verify(obj2, ArgumentMatchers.never()).equals(obj1);
    }

    @Test
    public void testAnswerForPrimitiveTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        byte byteValue = returnsEmptyValues.answer(mock(InvocationOnMock.class, ArgumentMatchers.any(Byte.class))).byteValue();
        short shortValue = returnsEmptyValues.answer(mock(InvocationOnMock.class, ArgumentMatchers.any(Short.class))).shortValue();
        int intValue = returnsEmptyValues.answer(mock(InvocationOnMock.class, ArgumentMatchers.any(Integer.class))).intValue();
        long longValue = returnsEmptyValues.answer(mock(InvocationOnMock.class, ArgumentMatchers.any(Long.class))).longValue();
        float floatValue = returnsEmptyValues.answer(mock(InvocationOnMock.class, ArgumentMatchers.any(Float.class))).floatValue();
        double doubleValue = returnsEmptyValues.answer(mock(InvocationOnMock.class, ArgumentMatchers.any(Double.class))).doubleValue();
        boolean booleanValue = returnsEmptyValues.answer(mock(InvocationOnMock.class, ArgumentMatchers.any(Boolean.class))).booleanValue();

        assertEquals((byte) 0, byteValue);
        assertEquals((short) 0, shortValue);
        assertEquals(0, intValue);
        assertEquals(0L, longValue);
        assertEquals(0f, floatValue, 0.01F);
        assertEquals(0d, doubleValue, 0.01D);
        assertEquals(false, booleanValue);
    }

    @Test
    public void testAnswerForCollectionTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        List<Object> list = (List<Object>) returnsEmptyValues.answer(mock(InvocationOnMock.class, ArgumentMatchers.any(List.class)));
        Set<Object> set = (Set<Object>) returnsEmptyValues.answer(mock(InvocationOnMock.class, ArgumentMatchers.any(Set.class)));
        HashSet<Object> hashSet = (HashSet<Object>) returnsEmptyValues.answer(mock(InvocationOnMock.class, ArgumentMatchers.any(HashSet.class)));
        SortedSet<Object> sortedSet = (SortedSet<Object>) returnsEmptyValues.answer(mock(InvocationOnMock.class, ArgumentMatchers.any(SortedSet.class)));
        TreeSet<Object> treeSet = (TreeSet<Object>) returnsEmptyValues.answer(mock(InvocationOnMock.class, ArgumentMatchers.any(TreeSet.class)));
        LinkedHashSet<Object> linkedHashSet = (LinkedHashSet<Object>) returnsEmptyValues.answer(mock(InvocationOnMock.class, ArgumentMatchers.any(LinkedHashSet.class)));
        ArrayList<Object> arrayList = (ArrayList<Object>) returnsEmptyValues.answer(mock(InvocationOnMock.class, ArgumentMatchers.any(ArrayList.class)));
        LinkedList<Object> linkedList = (LinkedList<Object>) returnsEmptyValues.answer(mock(InvocationOnMock.class, ArgumentMatchers.any(LinkedList.class)));

        assertEquals(new LinkedList<>(), list);
        assertEquals(new HashSet<>(), set);
        assertEquals(new HashSet<>(), hashSet);
        assertEquals(new TreeSet<>(), sortedSet);
        assertEquals(new TreeSet<>(), treeSet);
        assertEquals(new LinkedHashSet<>(), linkedHashSet);
        assertEquals(new ArrayList<>(), arrayList);
        assertEquals(new LinkedList<>(), linkedList);
    }

    @Test
    public void testAnswerForOtherTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Map<Object, Object> map = (Map<Object, Object>) returnsEmptyValues.answer(mock(InvocationOnMock.class, ArgumentMatchers.any(Map.class)));
        HashMap<Object, Object> hashMap = (HashMap<Object, Object>) returnsEmptyValues.answer(mock(InvocationOnMock.class, ArgumentMatchers.any(HashMap.class)));
        SortedMap<Object, Object> sortedMap = (SortedMap<Object, Object>) returnsEmptyValues.answer(mock(InvocationOnMock.class, ArgumentMatchers.any(SortedMap.class)));
        TreeMap<Object, Object> treeMap = (TreeMap<Object, Object>) returnsEmptyValues.answer(mock(InvocationOnMock.class, ArgumentMatchers.any(TreeMap.class)));
        LinkedHashMap<Object, Object> linkedHashMap = (LinkedHashMap<Object, Object>) returnsEmptyValues.answer(mock(InvocationOnMock.class, ArgumentMatchers.any(LinkedHashMap.class)));

        assertEquals(new HashMap<>(), map);
        assertEquals(new HashMap<>(), hashMap);
        assertEquals(new TreeMap<>(), sortedMap);
        assertEquals(new TreeMap<>(), treeMap);
        assertEquals(new LinkedHashMap<>(), linkedHashMap);

        assertNull(returnsEmptyValues.answer(mock(InvocationOnMock.class, ArgumentMatchers.any(Object[].class))));
        assertNull(returnsEmptyValues.answer(mock(InvocationOnMock.class, ArgumentMatchers.any(String[].class))));
        assertNull(returnsEmptyValues.answer(mock(InvocationOnMock.class, ArgumentMatchers.any(String.class))));
        assertNull(returnsEmptyValues.answer(mock(InvocationOnMock.class, ArgumentMatchers.any(Number.class))));
        assertNull(returnsEmptyValues.answer(mock(InvocationOnMock.class, ArgumentMatchers.any(Character.class))));
        assertNull(returnsEmptyValues.answer(mock(InvocationOnMock.class, ArgumentMatchers.any(Byte.class))));
        assertNull(returnsEmptyValues.answer(mock(InvocationOnMock.class, ArgumentMatchers.any(Short.class))));
        assertNull(returnsEmptyValues.answer(mock(InvocationOnMock.class, ArgumentMatchers.any(Integer.class))));
        assertNull(returnsEmptyValues.answer(mock(InvocationOnMock.class, ArgumentMatchers.any(Long.class))));
        assertNull(returnsEmptyValues.answer(mock(InvocationOnMock.class, ArgumentMatchers.any(Float.class))));
        assertNull(returnsEmptyValues.answer(mock(InvocationOnMock.class, ArgumentMatchers.any(Double.class))));
        assertNull(returnsEmptyValues.answer(mock(InvocationOnMock.class, ArgumentMatchers.any(Boolean.class))));
        assertNull(returnsEmptyValues.answer(mock(InvocationOnMock.class, ArgumentMatchers.any(Serializable.class))));
    }
}