package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
    public void testAnswerToStringMethod() {
        Object mock = mock(String.class);
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        String result = (String) returnsEmptyValues.answer(InvocationOnMock.method(String.class, "toString"));
        assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void testAnswerCompareToMethod() {
        Object mock1 = mock(String.class);
        Object mock2 = mock(String.class);
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        int result = (Integer) returnsEmptyValues.answer(InvocationOnMock.method(String.class, "compareTo", ArgumentMatchers.any(String.class)));
        verify(mock1).equals(mock2);
        assertEquals(0, result);
    }

    @Test
    public void testAnswerPrimitiveTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        byte result1 = (Byte) returnsEmptyValues.answer(InvocationOnMock.method(Byte.TYPE, "valueOf", (Object[]) null));
        char result2 = (Character) returnsEmptyValues.answer(InvocationOnMock.method(Character.TYPE, "valueOf", (Object[]) null));
        short result3 = (Short) returnsEmptyValues.answer(InvocationOnMock.method(Short.TYPE, "valueOf", (Object[]) null));
        int result4 = (Integer) returnsEmptyValues.answer(InvocationOnMock.method(Integer.TYPE, "valueOf", (Object[]) null));
        long result5 = (Long) returnsEmptyValues.answer(InvocationOnMock.method(Long.TYPE, "valueOf", (Object[]) null));
        float result6 = (Float) returnsEmptyValues.answer(InvocationOnMock.method(Float.TYPE, "valueOf", (Object[]) null));
        double result7 = (Double) returnsEmptyValues.answer(InvocationOnMock.method(Double.TYPE, "valueOf", (Object[]) null));
        boolean result8 = (Boolean) returnsEmptyValues.answer(InvocationOnMock.method(Boolean.TYPE, "valueOf", (Object[]) null));

        assertEquals((byte) 0, result1);
        assertEquals((char) 0, result2);
        assertEquals((short) 0, result3);
        assertEquals(0, result4);
        assertEquals(0L, result5);
        assertEquals(0.0f, result6, 0.001F);
        assertEquals(0.0d, result7, 0.001D);
        assertEquals(false, result8);
    }

    @Test
    public void testAnswerCollectionEmptyList() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        List<String> result = (List<String>) returnsEmptyValues.answer(InvocationOnMock.method(Collection.class, "isEmpty", ArgumentMatchers.any(Collection.class)));
        List<String> emptyList = new LinkedList<>();
        assertEquals(emptyList, result);
    }

    @Test
    public void testAnswerSetEmptyHashSet() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Set<String> result = (Set<String>) returnsEmptyValues.answer(InvocationOnMock.method(Set.class, "isEmpty", ArgumentMatchers.any(Set.class)));
        Set<String> emptySet = new HashSet<>();
        assertEquals(emptySet, result);
    }

    @Test
    public void testAnswerSortedSetEmptyTreeSet() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        SortedSet<String> result = (SortedSet<String>) returnsEmptyValues.answer(InvocationOnMock.method(SortedSet.class, "isEmpty", ArgumentMatchers.any(SortedSet.class)));
        SortedSet<String> emptySet = new TreeSet<>();
        assertEquals(emptySet, result);
    }

    @Test
    public void testAnswerLinkedHashSetEmptyLinkedHashSet() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Set<String> result = (Set<String>) returnsEmptyValues.answer(InvocationOnMock.method(LinkedHashSet.class, "isEmpty", ArgumentMatchers.any(LinkedHashSet.class)));
        Set<String> emptySet = new LinkedHashSet<>();
        assertEquals(emptySet, result);
    }

    @Test
    public void testAnswerArrayListEmptyArrayList() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        List<String> result = (List<String>) returnsEmptyValues.answer(InvocationOnMock.method(ArrayList.class, "isEmpty", ArgumentMatchers.any(ArrayList.class)));
        List<String> emptyList = new ArrayList<>();
        assertEquals(emptyList, result);
    }

    @Test
    public void testAnswerLinkedListEmptyLinkedList() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        List<String> result = (List<String>) returnsEmptyValues.answer(InvocationOnMock.method(LinkedList.class, "isEmpty", ArgumentMatchers.any(LinkedList.class)));
        List<String> emptyList = new LinkedList<>();
        assertEquals(emptyList, result);
    }

    @Test
    public void testAnswerMapEmptyHashMap() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Map<String, String> result = (Map<String, String>) returnsEmptyValues.answer(InvocationOnMock.method(Map.class, "isEmpty", ArgumentMatchers.any(Map.class)));
        Map<String, String> emptyMap = new HashMap<>();
        assertEquals(emptyMap, result);
    }

    @Test
    public void testAnswerLinkedHashMapEmptyLinkedHashMap() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Map<String, String> result = (Map<String, String>) returnsEmptyValues.answer(InvocationOnMock.method(LinkedHashMap.class, "isEmpty", ArgumentMatchers.any(LinkedHashMap.class)));
        Map<String, String> emptyMap = new LinkedHashMap<>();
        assertEquals(emptyMap, result);
    }

    @Test
    public void testAnswerSortedMapEmptyTreeMap() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        SortedMap<String, String> result = (SortedMap<String, String>) returnsEmptyValues.answer(InvocationOnMock.method(SortedMap.class, "isEmpty", ArgumentMatchers.any(SortedMap.class)));
        SortedMap<String, String> emptyMap = new TreeMap<>();
        assertEquals(emptyMap, result);
    }

    @Test
    public void testAnswerTreeMapEmptyTreeMap() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Map<String, String> result = (Map<String, String>) returnsEmptyValues.answer(InvocationOnMock.method(TreeMap.class, "isEmpty", ArgumentMatchers.any(TreeMap.class)));
        Map<String, String> emptyMap = new TreeMap<>();
        assertEquals(emptyMap, result);
    }

    @Test
    public void testAnswerNull() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object result = returnsEmptyValues.answer(InvocationOnMock.method(String.class, "hashCode"));
        assertNull(result);
    }
}