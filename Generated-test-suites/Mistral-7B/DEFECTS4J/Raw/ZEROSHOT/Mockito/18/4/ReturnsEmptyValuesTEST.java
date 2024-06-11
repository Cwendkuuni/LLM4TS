package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;

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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for ReturnsEmptyValues.java
 */
@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        Mockito<String> mockString = Mockito.mock(String.class);
        Mockito<Integer> mockInteger = Mockito.mock(Integer.class);

        when(mockString.toString()).thenAnswer(new ReturnsEmptyValues());
        when(mockInteger.toString()).thenAnswer(new ReturnsEmptyValues());

        String mockStringName = Mockito.mockName(mockString);
        Integer mockIntegerName = Mockito.mockName(mockInteger);

        verify(mockString).toString();
        verify(mockString).toString();

        System.out.println("MockString name: " + mockStringName);
        System.out.println("MockInteger name: " + mockIntegerName);

        System.out.println("MockString toString: " + mockString.toString());
        System.out.println("MockInteger toString: " + mockInteger.toString());
    }

    @Test
    public void testCompareToMethod() {
        Mockito<Comparable<Integer>> mockComparableInteger = Mockito.mock(Comparable.class);

        when(mockComparableInteger.compareTo(ArgumentMatchers.any())).thenAnswer(new ReturnsEmptyValues());

        int comparisonResult = mockComparableInteger.compareTo(1);
        int comparisonResult2 = mockComparableInteger.compareTo(2);

        System.out.println("Comparison result 1: " + comparisonResult);
        System.out.println("Comparison result 2: " + comparisonResult2);
    }

    @Test
    public void testReturnEmptyCollection() {
        Mockito<Collection<Integer>> mockCollection = Mockito.mock(Collection.class);

        when(mockCollection.isEmpty()).thenReturn(true);
        when(mockCollection.size()).thenReturn(0);
        when(mockCollection.add(ArgumentMatchers.any())).thenThrow(UnsupportedOperationException.class);

        when(mockCollection.toString()).thenAnswer(new ReturnsEmptyValues());

        Collection<Integer> collection = mockCollection;

        List<Integer> emptyList = new ArrayList<>();
        List<Integer> emptyLinkedList = new LinkedList<>();
        Set<Integer> emptyHashSet = new HashSet<>();
        SortedSet<Integer> emptyTreeSet = new TreeSet<>();
        LinkedHashSet<Integer> emptyLinkedHashSet = new LinkedHashSet<>();
        Map<Integer, Integer> emptyHashMap = new HashMap<>();
        SortedMap<Integer, Integer> emptyTreeMap = new TreeMap<>();
        LinkedHashMap<Integer, Integer> emptyLinkedHashMap = new LinkedHashMap<>();

        testCollectionsAreEqual(collection, emptyList);
        testCollectionsAreEqual(collection, emptyLinkedList);
        testCollectionsAreEqual(collection, emptyHashSet);
        testCollectionsAreEqual(collection, emptyTreeSet);
        testCollectionsAreEqual(collection, emptyLinkedHashSet);
        testCollectionsAreEqual(collection, emptyHashMap);
        testCollectionsAreEqual(collection, emptyTreeMap);
        testCollectionsAreEqual(collection, emptyLinkedHashMap);
    }

    private void testCollectionsAreEqual(Collection<?> collection1, Collection<?> collection2) {
        assertEquals(collection1.size(), collection2.size());
        assertEquals(collection1.isEmpty(), collection2.isEmpty());

        for (Object element : collection1) {
            try {
                collection2.add(element);
            } catch (UnsupportedOperationException e) {
                // Expected
            }
        }

        for (Object element : collection2) {
            try {
                collection1.add(element);
            } catch (UnsupportedOperationException e) {
                // Expected
            }
        }

        assertEquals(collection1, collection2);
    }
}