package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        Object mock = mock(Object.class);
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        when(mock.toString()).thenAnswer(answer);

        assertEquals("Mock for Object, hashCode: 421554358", mock.toString());
    }

    @Test
    public void testCompareToMethod() {
        Object mock1 = mock(Comparable.class);
        Object mock2 = mock(Comparable.class);

        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        when(mock1.compareTo(mock2)).thenAnswer(answer);

        assertEquals(1, mock1.compareTo(mock2));
    }

    @Test
    public void testPrimitiveTypes() {
        byte mockByte = mock(Byte.class);
        short mockShort = mock(Short.class);
        int mockInt = mock(Integer.class);
        long mockLong = mock(Long.class);
        float mockFloat = mock(Float.class);
        double mockDouble = mock(Double.class);
        char mockChar = mock(Character.class);
        boolean mockBoolean = mock(Boolean.class);

        byte expectedByte = 0;
        short expectedShort = 0;
        int expectedInt = 0;
        long expectedLong = 0;
        float expectedFloat = 0f;
        double expectedDouble = 0d;
        char expectedChar = '\u0000';
        boolean expectedBoolean = false;

        verify(mockByte).byteValue();
        verify(mockByte).answers(eq(expectedByte));

        verify(mockShort).shortValue();
        verify(mockShort).answers(eq(expectedShort));

        verify(mockInt).intValue();
        verify(mockInt).answers(eq(expectedInt));

        verify(mockLong).longValue();
        verify(mockLong).answers(eq(expectedLong));

        verify(mockFloat).floatValue();
        verify(mockFloat).answers(eq(expectedFloat));

        verify(mockDouble).doubleValue();
        verify(mockDouble).answers(eq(expectedDouble));

        verify(mockChar).charValue();
        verify(mockChar).answers(eq(expectedChar));

        verify(mockBoolean).booleanValue();
        verify(mockBoolean).answers(eq(expectedBoolean));

        //Some JVMs return true for false and vice versa for primitive boolean types,
        //so it's important to use the correct value in the assertions.
        verify(mockBoolean, never()).answers(eq(true));
        verify(mockBoolean, never()).answers(eq(false));
        verify(mockBoolean).answers(eq(expectedBoolean));
    }

    @Test
    public void testEmptyCollections() {
        Collection<String> collection = mock(Collection.class);
        Set<String> set = mock(Set.class);
        HashSet<String> hashSet = mock(HashSet.class);
        SortedSet<String> sortedSet = mock(SortedSet.class);
        TreeSet<String> treeSet = mock(TreeSet.class);
        LinkedHashSet<String> linkedHashSet = mock(LinkedHashSet.class);
        List<String> list = mock(List.class);
        LinkedList<String> linkedList = mock(LinkedList.class);
        ArrayList<String> arrayList = mock(ArrayList.class);
        Map<String, String> map = mock(Map.class);
        HashMap<String, String> hashMap = mock(HashMap.class);
        SortedMap<String, String> sortedMap = mock(SortedMap.class);
        TreeMap<String, String> treeMap = mock(TreeMap.class);
        LinkedHashMap<String, String> linkedHashMap = mock(LinkedHashMap.class);

        verify(collection).isEmpty();
        verify(collection).answers(eq(Collections.emptyList()));

        verify(set).isEmpty();
        verify(set).answers(eq(new HashSet<>()));

        verify(hashSet).isEmpty();
        verify(hashSet).answers(eq(new HashSet<>()));

        verify(sortedSet).isEmpty();
        verify(sortedSet).answers(eq(new TreeSet<>()));

        verify(treeSet).isEmpty();
        verify(treeSet).answers(eq(new TreeSet<>()));

        verify(linkedHashSet).isEmpty();
        verify(linkedHashSet).answers(eq(new LinkedHashSet<>()));

        verify(list).isEmpty();
        verify(list).answers(eq(new LinkedList<>()));

        verify(linkedList).isEmpty();
        verify(linkedList).answers(eq(new LinkedList<>()));

        verify(arrayList).isEmpty();
        verify(arrayList).answers(eq(new ArrayList<>()));

        verify(map).isEmpty();
        verify(map).answers(eq(new HashMap<>()));

        verify(hashMap).isEmpty();
        verify(hashMap).answers(eq(new HashMap<>()));

        verify(sortedMap).isEmpty();
        verify(sortedMap).answers(eq(new TreeMap<>()));

        verify(treeMap).isEmpty();
        verify(treeMap).answers(eq(new TreeMap<>()));

        verify(linkedHashMap).isEmpty();
        verify(linkedHashMap).answers(eq(new LinkedHashMap<>()));
    }

    @Test
    public void testNullReturn() {
        Object mock = mock(Object.class);
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        verify(mock).answers(any(Class.class)).thenAnswer(answer);

        assertNull(mock.answer(any(InvocationOnMock.class)));
    }
}