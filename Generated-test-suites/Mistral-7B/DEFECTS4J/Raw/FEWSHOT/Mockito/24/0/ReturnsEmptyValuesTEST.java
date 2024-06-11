package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEmpty;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.internal.util.Primitives;
import org.mockito.internal.util.stubbing.DefaultAnswerTestHelper;

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
import java.util.PrimitiveIterator;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer_toStringMethod_returnsDescriptionOfMock() {
        // Given
        Object mock = new Object();

        // When
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        String actual = (String) returnsEmptyValues.answer(new InvocationOnMock(mock.getClass().getMethod("toString"), mock));

        // Then
        String expected = String.format("Mock for %s, hashCode: %s", mock.getClass().getSimpleName(), Integer.toHexString(mock.hashCode()));
        assertEquals(expected, actual);
    }

    @Test
    public void testAnswer_compareToMethod_returnsZeroIfReferencesAreEqual() {
        // Given
        Object mock1 = new Object();
        Object mock2 = mock1;

        // When
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        int actual = (Integer) returnsEmptyValues.answer(new InvocationOnMock(Comparable.class.getMethod("compareTo", Object.class), mock1));

        // Then
        assertEquals(0, actual);
    }

    @Test
    public void testAnswer_compareToMethod_returnsNonZeroIfReferencesAreNotEqual() {
        // Given
        Object mock1 = new Object();
        Object mock2 = new Object();

        // When
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        int actual = (Integer) returnsEmptyValues.answer(new InvocationOnMock(Comparable.class.getMethod("compareTo", Object.class), mock1));

        // Then
        assertNotEquals(0, actual);
    }

    @Test
    public void testAnswer_primitiveType_returnsDefaultValue() {
        // Given
        Class<Byte> byteType = Byte.TYPE;

        // When
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Byte actual = (Byte) returnsEmptyValues.answer(new InvocationOnMock(byteType.getMethod("valueOf", byte.class), null));

        // Then
        assertEquals(Byte.MIN_VALUE, actual);
    }

    @Test
    public void testAnswer_stringArray_returnsEmptyArray() {
        // Given
        Class<String[]> stringArrayType = String[].class;

        // When
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        String[] actual = (String[]) returnsEmptyValues.answer(new InvocationOnMock(stringArrayType.getMethod("newInstance"), null));

        // Then
        assertEmpty(actual);
    }

    @Test
    public void testAnswer_emptyList_returnsInstanceOfLinkedList() {
        // Given
        Class<List<Object>> listType = List.class;

        // When
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        List<Object> actual = (List<Object>) returnsEmptyValues.answer(new InvocationOnMock(listType.getMethod("ofSize", int.class), 0));

        // Then
        assertTrue(actual instanceof LinkedList);
        assertEmpty(actual);
    }

    @Test
    public void testAnswer_emptySet_returnsInstanceOfHashSet() {
        // Given
        Class<Set<Object>> setType = Set.class;

        // When
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Set<Object> actual = (Set<Object>) returnsEmptyValues.answer(new InvocationOnMock(setType.getMethod("ofSize", int.class), 0));

        // Then
        assertTrue(actual instanceof HashSet);
        assertEmpty(actual);
    }

    @Test
    public void testAnswer_emptySortedSet_returnsInstanceOfTreeSet() {
        // Given
        Class<SortedSet<Object>> sortedSetType = SortedSet.class;

        // When
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        SortedSet<Object> actual = (SortedSet<Object>) returnsEmptyValues.answer(new InvocationOnMock(sortedSetType.getMethod("newSortedSet", Comparator.class), null));

        // Then
        assertTrue(actual instanceof TreeSet);
        assertEmpty(actual);
    }

    @Test
    public void testAnswer_emptyMap_returnsInstanceOfHashMap() {
        // Given
        Class<Map<Object, Object>> mapType = Map.class;

        // When
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Map<Object, Object> actual = (Map<Object, Object>) returnsEmptyValues.answer(new InvocationOnMock(mapType.getMethod("ofSize", int.class), 0));

        // Then
        assertTrue(actual instanceof HashMap);
        assertEmpty(actual);
    }

    @Test
    public void testAnswer_emptyCollection_returnsInstanceOfLinkedListForListType() {
        // Given
        Class<Collection<Object>> collectionType = Collection.class;
        Class<List<Object>> listType = List.class;

        // When
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Collection<Object> actual = returnsEmptyValues.answer(new InvocationOnMock(collectionType.getMethod("emptyList"), null));

        // Then
        assertTrue(actual instanceof List);
        List<Object> listActual = (List<Object>) actual;
        assertTrue(listActual instanceof LinkedList);
        assertEmpty(listActual);
    }

    @Test
    public void testAnswer_emptySet_returnsInstanceOfHashSetForSetType() {
        // Given
        Class<Collection<Object>> collectionType = Collection.class;
        Class<Set<Object>> setType = Set.class;

        // When
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Collection<Object> actual = returnsEmptyValues.answer(new InvocationOnMock(collectionType.getMethod("emptySet"), null));

        // Then
        assertTrue(actual instanceof Set);
        Set<Object> setActual = (Set<Object>) actual;
        assertTrue(setActual instanceof HashSet);
        assertEmpty(setActual);
    }

    @Test
    public void testAnswer_emptyList_returnsEmptyArrayForPrimitiveIteratorType() {
        // Given
        Class<PrimitiveIterator.OfInt> primitiveIteratorType = PrimitiveIterator.OfInt.class;

        // When
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        PrimitiveIterator.OfInt actual = (PrimitiveIterator.OfInt) returnsEmptyValues.answer(new InvocationOnMock(primitiveIteratorType.getMethod("defaultIterator"), null));

        // Then
        assertNotNull(actual);
        assertFalse(actual.hasNext());
        assertArrayEquals(new int[0], actual.toArray(new int[0]));
    }

    @Test
    public void testAnswer_null_returnsNull() {
        // Given
        Class<Object> objectType = Object.class;

        // When
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object actual = returnsEmptyValues.answer(new InvocationOnMock(objectType.getMethod("valueOf", String.class), null));

        // Then
        assertNull(actual);
    }
}