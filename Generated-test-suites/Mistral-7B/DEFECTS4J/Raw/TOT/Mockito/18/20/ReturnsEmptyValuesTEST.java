package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests for the ReturnsEmptyValues class.
 */
public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        Mockito<String> mockString = mock(String.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(mockString).thenReturn(new ReturnsEmptyValues().answer(ArgumentMatchers.any(InvocationOnMock.class)));
        String mockStringDescription = (String) invocation.proceed();

        assertEquals("Mock for " + String.class.getSimpleName() + ", hashCode: " + mockString.hashCode(), mockStringDescription);
    }

    @Test
    public void testCompareToMethod() {
        Object mockObj1 = new Object();
        Object mockObj2 = new Object();

        Mockito<Comparable<?>> mockComparable = mock(Comparable.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(mockComparable).thenReturn(new ReturnsEmptyValues().answer(ArgumentMatchers.any(InvocationOnMock.class)));

        int comparisonResult = (Integer) invocation.proceed(mockObj1, mockObj2);

        assertTrue(comparisonResult > 0);
    }

    @Test
    public void testEmptyCollectionTypes() {
        Mockito<Collection<Integer>> mockCollection = mock(Collection.class);

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(mockCollection).thenReturn(new ReturnsEmptyValues().answer(ArgumentMatchers.any(InvocationOnMock.class)));

        Collection<Integer> emptyCollection = (Collection<Integer>) invocation.proceed();

        assertTrue(emptyCollection.isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testModifiableEmptyCollection() {
        // This test case checks that the collection returned by the stub is not modifiable as per the contract.
        Mockito<List<String>> mockList = mock(List.class);

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(mockList).thenReturn(new ReturnsEmptyValues().answer(ArgumentMatchers.any(InvocationOnMock.class)));

        List<String> emptyList = (List<String>) invocation.proceed();

        emptyList.add("Test Element");
    }
}