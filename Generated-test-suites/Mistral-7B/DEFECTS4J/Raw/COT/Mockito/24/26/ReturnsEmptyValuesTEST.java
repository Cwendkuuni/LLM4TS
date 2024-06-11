package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.Serializable;
import java.util.*;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        Mockito<String> mockString = Mockito.mock(String.class);
        ReturnsEmptyValues answer = Mockito.mock(ReturnsEmptyValues.class);
        Mockito.when(answer.answer(ArgumentMatchers.any(InvocationOnMock.class))).then(answer);
        String result = (String) answer.answer(Mockito.argument(0));
        assertEquals("Mock for String, hashCode: " + mockString.hashCode(), result);
    }

    @Test
    public void testCompareToMethod() {
        Mockito<Comparable<Object>> mockComparableObject = Mockito.mock(Comparable.class);
        ReturnsEmptyValues answer = Mockito.mock(ReturnsEmptyValues.class);
        Mockito.when(answer.answer(ArgumentMatchers.any(InvocationOnMock.class))).then(answer);
        int result = (Integer) answer.answer(Mockito.argument(0));
        assertEquals(1, result);
    }

    @Test
    public void testPrimitiveTypes() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        byte expectedByte = Primitives.byteValue;
        byte resultByte = (byte) answer.returnValueFor(byte.class);
        assertEquals(expectedByte, resultByte);

        short expectedShort = Primitives.shortValue;
        short resultShort = (short) answer.returnValueFor(short.class);
        assertEquals(expectedShort, resultShort);

        int expectedInt = Primitives.intValue;
        int resultInt = (int) answer.returnValueFor(int.class);
        assertEquals(expectedInt, resultInt);

        long expectedLong = Primitives.longValue;
        long resultLong = (long) answer.returnValueFor(long.class);
        assertEquals(expectedLong, resultLong);

        char expectedChar = Primitives.charValue;
        char resultChar = (char) answer.returnValueFor(char.class);
        assertEquals(expectedChar, resultChar);

        boolean expectedBoolean = Primitives.booleanValue;
        boolean resultBoolean = (boolean) answer.returnValueFor(boolean.class);
        assertEquals(expectedBoolean, resultBoolean);

        float expectedFloat = Primitives.floatValue;
        float resultFloat = (float) answer.returnValueFor(float.class);
        assertEquals(expectedFloat, resultFloat, 0.0f);

        double expectedDouble = Primitives.doubleValue;
        double resultDouble = (double) answer.returnValueFor(double.class);
        assertEquals(expectedDouble, resultDouble, 0.0);
    }

    @Test
    public void testEmptyCollectionTypes() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        Class<?> collectionType = Collection.class;
        Collection<Object> expectedCollection = new LinkedList<>();
        Collection<Object> resultCollection = (Collection<Object>) answer.returnValueFor(collectionType);
        assertEquals(expectedCollection, resultCollection);

        Class<?> setType = Set.class;
        Set<Object> expectedSet = new HashSet<>();
        Set<Object> resultSet = (Set<Object>) answer.returnValueFor(setType);
        assertEquals(expectedSet, resultSet);

        Class<?> hashSetType = HashSet.class;
        Set<Object> expectedHashSet = new HashSet<>();
        Set<Object> resultHashSet = (Set<Object>) answer.returnValueFor(hashSetType);
        assertEquals(expectedHashSet, resultHashSet);

        Class<?> sortedSetType = SortedSet.class;
        SortedSet<Object> expectedSortedSet = new TreeSet<>();
        SortedSet<Object> resultSortedSet = (SortedSet<Object>) answer.returnValueFor(sortedSetType);
        assertEquals(expectedSortedSet, resultSortedSet);

        Class<?> treeSetType = TreeSet.class;
        SortedSet<Object> expectedTreeSet = new TreeSet<>();
        SortedSet<Object> resultTreeSet = (SortedSet<Object>) answer.returnValueFor(treeSetType);
        assertEquals(expectedTreeSet, resultTreeSet);

        Class<?> linkedHashSetType = LinkedHashSet.class;
        Set<Object> expectedLinkedHashSet = new LinkedHashSet<>();
        Set<Object> resultLinkedHashSet = (Set<Object>) answer.returnValueFor(linkedHashSetType);
        assertEquals(expectedLinkedHashSet, resultLinkedHashSet);

        Class<?> listType = List.class;
        List<Object> expectedList = new LinkedList<>();
        List<Object> resultList = (List<Object>) answer.returnValueFor(listType);
        assertEquals(expectedList, resultList);

        Class<?> linkedListType = LinkedList.class;
        List<Object> expectedLinkedList = new LinkedList<>();
        List<Object> resultLinkedList = (List<Object>) answer.returnValueFor(linkedListType);
        assertEquals(expectedLinkedList, resultLinkedList);

        Class<?> arrayListType = ArrayList.class;
        List<Object> expectedArrayList = new ArrayList<>();
        List<Object> resultArrayList = (List<Object>) answer.returnValueFor(arrayListType);
        assertEquals(expectedArrayList, resultArrayList);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNonEmptyCollectionTypes() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        answer.returnValueFor(Iterable.class);
    }

    @Test
    public void testMapTypes() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        Class<?> mapType = Map.class;
        Map<Object, Object> expectedMap = new HashMap<>();
        Map<Object, Object> resultMap = (Map<Object, Object>) answer.returnValueFor(mapType);
        assertEquals(expectedMap, resultMap);

        Class<?> hashMapType = HashMap.class;
        Map<Object, Object> expectedHashMap = new HashMap<>();
        Map<Object, Object> resultHashMap = (Map<Object, Object>) answer.returnValueFor(hashMapType);
        assertEquals(expectedHashMap, resultHashMap);

        Class<?> treeMapType = TreeMap.class;
        SortedMap<Object, Object> expectedTreeMap = new TreeMap<>();
        SortedMap<Object, Object> resultTreeMap = (SortedMap<Object, Object>) answer.returnValueFor(treeMapType);
        assertEquals(expectedTreeMap, resultTreeMap);

        Class<?> treeMapTypeWithCustomComparator = TreeMap.class;
        SortedMap<Object, Object> expectedTreeMapWithCustomComparator = new TreeMap<>(Mockito.mock(Comparator.class));
        SortedMap<Object, Object> resultTreeMapWithCustomComparator = (SortedMap<Object, Object>) answer.returnValueFor(treeMapTypeWithCustomComparator);
        assertEquals(expectedTreeMapWithCustomComparator, resultTreeMapWithCustomComparator);

        Class<?> linkedHashMapType = LinkedHashMap.class;
        Map<Object, Object> expectedLinkedHashMap = new LinkedHashMap<>();
        Map<Object, Object> resultLinkedHashMap = (Map<Object, Object>) answer.returnValueFor(linkedHashMapType);
        assertEquals(expectedLinkedHashMap, resultLinkedHashMap);
    }
}