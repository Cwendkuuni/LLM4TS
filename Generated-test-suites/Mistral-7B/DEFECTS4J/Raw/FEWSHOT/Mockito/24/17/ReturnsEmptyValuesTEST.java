package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        Object mock = mock(Object.class);
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), returnsEmptyValues.answer(mock(InvocationOnMock.class)));
        assertNull(returnsEmptyValues.answer(mock(InvocationOnMock.class, String.class)));
    }

    @Test
    public void testAnswerCompareToMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Object mock1 = mock(Comparable.class);
        Object mock2 = mock(Comparable.class);

        assertEquals(1, returnsEmptyValues.answer(mock(InvocationOnMock.class, mock1.getClass(), mock1)).intValue());
        assertEquals(1, returnsEmptyValues.answer(mock(InvocationOnMock.class, mock2.getClass(), mock2)).intValue());
        assertNotSame(mock1, mock2);
        assertEquals(0, mock1.compareTo(mock1));
        assertEquals(0, mock2.compareTo(mock2));
        assertEquals(1, mock1.compareTo(mock2));
        assertEquals(-1, mock2.compareTo(mock1));
    }

    @Test
    public void testReturnValueForPrimitiveTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        assertEquals(0, returnsEmptyValues.returnValueFor(int.class).intValue());
        assertEquals(0, returnsEmptyValues.returnValueFor(int[].class)[0]);
        assertEquals(0, returnsEmptyValues.returnValueFor(char.class).charValue());
        assertEquals(0, returnsEmptyValues.returnValueFor(char[].class)[0].charValue());
        assertEquals(false, returnsEmptyValues.returnValueFor(boolean.class).booleanValue());
        assertEquals(false, returnsEmptyValues.returnValueFor(boolean[].class)[0].booleanValue());
        assertEquals(0L, returnsEmptyValues.returnValueFor(long.class).longValue());
        assertEquals(0L, returnsEmptyValues.returnValueFor(long[].class)[0].longValue());
        assertEquals((byte) 0, returnsEmptyValues.returnValueFor(byte.class).byteValue());
        assertEquals((short) 0, returnsEmptyValues.returnValueFor(short.class).shortValue());
        assertEquals(0f, returnsEmptyValues.returnValueFor(float.class).floatValue());
        assertEquals(0d, returnsEmptyValues.returnValueFor(double.class).doubleValue());
    }

    @Test
    public void testReturnValueForCollectionTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Collection<Integer> emptyCollection = returnsEmptyValues.returnValueFor(Collection.class);
        List<String> emptyList = (List<String>) returnsEmptyValues.returnValueFor(List.class);
        Set<Character> emptySet = returnsEmptyValues.returnValueFor(Set.class);
        HashSet<Character> emptyHashSet = (HashSet<Character>) returnsEmptyValues.returnValueFor(HashSet.class);
        SortedSet<Double> emptyTreeSet = (SortedSet<Double>) returnsEmptyValues.returnValueFor(SortedSet.class);
        TreeSet<Double> emptyTreeHashSet = (TreeSet<Double>) returnsEmptyValues.returnValueFor(TreeSet.class);
        LinkedHashSet<Character> emptyLinkedHashSet = (LinkedHashSet<Character>) returnsEmptyValues.returnValueFor(LinkedHashSet.class);
        Map<String, Integer> emptyMap = returnsEmptyValues.returnValueFor(Map.class);
        HashMap<String, Integer> emptyHashMap = (HashMap<String, Integer>) returnsEmptyValues.returnValueFor(HashMap.class);
        SortedMap<String, Integer> emptyTreeMap = (SortedMap<String, Integer>) returnsEmptyValues.returnValueFor(SortedMap.class);
        TreeMap<String, Integer> emptyTreeHashMap = (TreeMap<String, Integer>) returnsEmptyValues.returnValueFor(TreeMap.class);
        LinkedHashMap<String, Integer> emptyLinkedHashMap = (LinkedHashMap<String, Integer>) returnsEmptyValues.returnValueFor(LinkedHashMap.class);

        assertTrue(emptyCollection.isEmpty());
        assertFalse(emptyCollection.contains(1));
        assertTrue(emptyList.isEmpty());
        assertFalse(emptyList.contains("foo"));
        assertTrue(emptySet.isEmpty());
        assertFalse(emptySet.contains('a'));
        assertTrue(emptyTreeSet.isEmpty());
        assertTrue(emptyTreeHashSet.isEmpty());
        assertTrue(emptyLinkedHashSet.isEmpty());
        assertTrue(emptyMap.isEmpty());
        assertTrue(emptyHashMap.isEmpty());
        assertTrue(emptyTreeMap.isEmpty());
        assertTrue(emptyTreeHashMap.isEmpty());
        assertTrue(emptyLinkedHashMap.isEmpty());
    }
}