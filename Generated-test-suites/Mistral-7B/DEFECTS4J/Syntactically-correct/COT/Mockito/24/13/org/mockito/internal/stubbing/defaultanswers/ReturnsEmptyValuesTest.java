package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
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
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        Object mock = Mockito.mock(String.class);
        ReturnsEmptyValues answer = Mockito.mock(ReturnsEmptyValues.class);
        Mockito.when(answer.answer(Mockito.any(InvocationOnMock.class))).then(answer);
        Mockito.when(answer.answer(Mockito.eq(Mockito.method(String.class, "toString")))).thenCallRealMethod();

        String actual = (String) answer.answer(Mockito.method(Mockito.type(String.class), "toString"));
        String expected = "Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode();

        assertEquals(expected, actual);
    }

    @Test
    public void testCompareToMethod() {
        Mockito.when(ReturnsEmptyValues.answer(Mockito.any(InvocationOnMock.class)))
                .then(invocation -> 1);

        int actual = (Integer) ReturnsEmptyValues.answer(Mockito.method(Comparable.class, "compareTo", Mockito.type(Object.class)));

        assertNotNull(actual);
        assertEquals(1, actual);
    }

    @Test
    public void testPrimitiveTypes() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        byte expectedByte = Byte.MIN_VALUE;
        short expectedShort = Short.MIN_VALUE;
        int expectedInt = Integer.MIN_VALUE;
        long expectedLong = Long.MIN_VALUE;
        float expectedFloat = Float.NaN;
        double expectedDouble = Double.NaN;
        char expectedChar = Character.MIN_VALUE;
        boolean expectedBoolean = false;

        byte actualByte = (byte) answer.returnValueFor(Byte.TYPE);
        short actualShort = (short) answer.returnValueFor(Short.TYPE);
        int actualInt = (int) answer.returnValueFor(Integer.TYPE);
        long actualLong = (long) answer.returnValueFor(Long.TYPE);
        float actualFloat = (float) answer.returnValueFor(Float.TYPE);
        double actualDouble = (double) answer.returnValueFor(Double.TYPE);
        char actualChar = (char) answer.returnValueFor(Character.TYPE);
        boolean actualBoolean = (boolean) answer.returnValueFor(Boolean.TYPE);

        assertEquals(expectedByte, actualByte);
        assertEquals(expectedShort, actualShort);
        assertEquals(expectedInt, actualInt);
        assertEquals(expectedLong, actualLong);
        assertEquals(expectedFloat, actualFloat, 0.0F);
        assertEquals(expectedDouble, actualDouble, 0.0D);
        assertEquals(expectedChar, actualChar);
        assertEquals(expectedBoolean, actualBoolean);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testModifyingReturnedCollection() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        List<String> list = (List<String>) answer.returnValueFor(List.class);
        list.add("test");
    }

    @Test
    public void testEmptyCollection() {
        Collection<?> emptyCollection = (Collection<?>) ReturnsEmptyValues.answer(Mockito.method(Collection.class, ""));
        assertNotNull(emptyCollection);
        assertEquals(0, emptyCollection.size());
    }

    @Test
    public void testEmptySet() {
        Set<?> emptySet = (Set<?>) ReturnsEmptyValues.answer(Mockito.method(Set.class, "<init>"));
        assertNotNull(emptySet);
        assertEquals(0, emptySet.size());
    }

    @Test
    public void testEmptyHashSet() {
        Set<?> emptyHashSet = (Set<?>) ReturnsEmptyValues.answer(Mockito.method(HashSet.class, "<init>"));
        assertNotNull(emptyHashSet);
        assertEquals(0, emptyHashSet.size());
    }

    @Test
    public void testEmptyTreeSet() {
        SortedSet<?> emptyTreeSet = (SortedSet<?>) ReturnsEmptyValues.answer(Mockito.method(TreeSet.class, "<init>"));
        assertNotNull(emptyTreeSet);
        assertEquals(0, emptyTreeSet.size());
    }

    @Test
    public void testEmptyLinkedHashSet() {
        Set<?> emptyLinkedHashSet = (Set<?>) ReturnsEmptyValues.answer(Mockito.method(LinkedHashSet.class, "<init>"));
        assertNotNull(emptyLinkedHashSet);
        assertEquals(0, emptyLinkedHashSet.size());
    }

    @Test
    public void testEmptyMap() {
        Map<Object, Object> emptyMap = (Map<Object, Object>) ReturnsEmptyValues.answer(Mockito.method(Map.class, "<init>"));
        assertNotNull(emptyMap);
        assertEquals(0, emptyMap.size());
    }

    @Test
    public void testEmptyHashMap() {
        Map<Object, Object> emptyHashMap = (Map<Object, Object>) ReturnsEmptyValues.answer(Mockito.method(HashMap.class, "<init>"));
        assertNotNull(emptyHashMap);
        assertEquals(0, emptyHashMap.size());
    }

    @Test
    public void testEmptyTreeMap() {
        SortedMap<Object, Object> emptyTreeMap = (SortedMap<Object, Object>) ReturnsEmptyValues.answer(Mockito.method(TreeMap.class, "<init>"));
        assertNotNull(emptyTreeMap);
        assertEquals(0, emptyTreeMap.size());
    }

    @Test
    public void testEmptyLinkedHashMap() {
        Map<Object, Object> emptyLinkedHashMap = (Map<Object, Object>) ReturnsEmptyValues.answer(Mockito.method(LinkedHashMap.class, "<init>"));
        assertNotNull(emptyLinkedHashMap);
        assertEquals(0, emptyLinkedHashMap.size());
    }

    @Test
    public void testEmptyList() {
        List<Object> emptyList = (List<Object>) ReturnsEmptyValues.answer(Mockito.method(List.class, "<init>"));
        assertNotNull(emptyList);
        assertEquals(0, emptyList.size());
    }

    @Test
    public void testEmptyLinkedList() {
        List<Object> emptyLinkedList = (List<Object>) ReturnsEmptyValues.answer(Mockito.method(LinkedList.class, "<init>"));
        assertNotNull(emptyLinkedList);
        assertEquals(0, emptyLinkedList.size());
    }

    @Test
    public void testEmptyArrayList() {
        List<Object> emptyArrayList = (List<Object>) ReturnsEmptyValues.answer(Mockito.method(ArrayList.class, "<init>"));
        assertNotNull(emptyArrayList);
        assertEquals(0, emptyArrayList.size());
    }
}