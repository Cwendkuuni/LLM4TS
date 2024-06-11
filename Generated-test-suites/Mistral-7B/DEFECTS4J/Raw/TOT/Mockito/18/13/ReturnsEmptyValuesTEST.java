package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.*;

public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        Mockito.<ReturnsEmptyValues>spy(ReturnsEmptyValues.class);
        ReturnsEmptyValues returnsEmptyValues = Mockito.doReturn(new ReturnsEmptyValues()).when(ReturnsEmptyValues.class);

        MockName mockName = Mockito.mock(MockName.class);
        Mockito.when(mockName.toString()).thenReturn("TestMock");

        Mockito.when(ReturnsEmptyValues.class.isInstance(returnsEmptyValues)).thenReturn(true);
        Mockito.when(returnsEmptyValues.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenCallRealMethod();

        String actual = returnsEmptyValues.answer(Mockito.argThat(invocation -> methodsGuru.isToString(invocation.getMethod())));
        String expected = "Mock for " + ReturnsEmptyValues.class.getSimpleName() + ", hashCode: " + returnsEmptyValues.hashCode() + "\nTestMock";

        assertEquals(expected, actual);
    }

    @Test
    public void testCompareToMethod() {
        Mockito.<ReturnsEmptyValues>spy(ReturnsEmptyValues.class);
        ReturnsEmptyValues returnsEmptyValues = Mockito.doReturn(new ReturnsEmptyValues()).when(ReturnsEmptyValues.class);

        Object anotherObject = new Object();

        int actual = returnsEmptyValues.answer(Mockito.argThat(invocation -> methodsGuru.isCompareToMethod(invocation.getMethod())));
        int expected = returnsEmptyValues == anotherObject ? 0 : 1;

        assertEquals(expected, actual);
    }

    @Test
    public void testReturnPrimitives() {
        Mockito.<ReturnsEmptyValues>spy(ReturnsEmptyValues.class);
        ReturnsEmptyValues returnsEmptyValues = Mockito.doReturn(new ReturnsEmptyValues()).when(ReturnsEmptyValues.class);

        byte expectedByte = 0;
        short expectedShort = 0;
        int expectedInt = 0;
        long expectedLong = 0;
        float expectedFloat = 0f;
        double expectedDouble = 0d;
        char expectedChar = (char) 0;
        boolean expectedBoolean = false;

        byte actualByte = (byte) returnsEmptyValues.answer(Mockito.argThat(invocation -> Primitives.isPrimitiveOrWrapper(invocation.getMethod().getReturnType()) && invocation.getMethod().getReturnType().isPrimitive()));
        short actualShort = (short) returnsEmptyValues.answer(Mockito.argThat(invocation -> Primitives.isPrimitiveOrWrapper(invocation.getMethod().getReturnType()) && invocation.getMethod().getReturnType() != byte.class && invocation.getMethod().getReturnType() != short.class));
        int actualInt1 = (int) returnsEmptyValues.answer(Mockito.argThat(invocation -> Primitives.isPrimitiveOrWrapper(invocation.getMethod().getReturnType()) && invocation.getMethod().getReturnType() != byte.class && invocation.getMethod().getReturnType() != short.class && invocation.getMethod().getReturnType() != int.class));
        int actualInt2 = (int) returnsEmptyValues.answer(Mockito.argThat(invocation -> Primitives.isPrimitiveOrWrapper(invocation.getMethod().getReturnType()) && Primitives.isIntOrWrapper(invocation.getMethod().getReturnType())));
        long actualLong = returnsEmptyValues.answer(Mockito.argThat(invocation -> Primitives.isPrimitiveOrWrapper(invocation.getMethod().getReturnType()) && invocation.getMethod().getReturnType() != byte.class && invocation.getMethod().getReturnType() != short.class && invocation.getMethod().getReturnType() != int.class && invocation.getMethod().getReturnType() != long.class));
        float actualFloat = (float) returnsEmptyValues.answer(Mockito.argThat(invocation -> Primitives.isPrimitiveOrWrapper(invocation.getMethod().getReturnType()) && invocation.getMethod().getReturnType() != byte.class && invocation.getMethod().getReturnType() != short.class && invocation.getMethod().getReturnType() != int.class && invocation.getMethod().getReturnType() != long.class && invocation.getMethod().getReturnType() != float.class));
        double actualDouble = returnsEmptyValues.answer(Mockito.argThat(invocation -> Primitives.isPrimitiveOrWrapper(invocation.getMethod().getReturnType()) && invocation.getMethod().getReturnType() != byte.class && invocation.getMethod().getReturnType() != short.class && invocation.getMethod().getReturnType() != int.class && invocation.getMethod().getReturnType() != long.class && invocation.getMethod().getReturnType() != float.class && invocation.getMethod().getReturnType() != double.class));
        char actualChar = (char) returnsEmptyValues.answer(Mockito.argThat(invocation -> Primitives.isPrimitiveOrWrapper(invocation.getMethod().getReturnType()) && invocation.getMethod().getReturnType() != byte.class && invocation.getMethod().getReturnType() != short.class && invocation.getMethod().getReturnType() != int.class && invocation.getMethod().getReturnType() != long.class && invocation.getMethod().getReturnType() != float.class && invocation.getMethod().getReturnType() != double.class && invocation.getMethod().getReturnType() != char.class && Primitives.isCharType(invocation.getMethod().getReturnType())));
        boolean actualBoolean = returnsEmptyValues.answer(Mockito.argThat(invocation -> Primitives.isPrimitiveOrWrapper(invocation.getMethod().getReturnType()) && invocation.getMethod().getReturnType() != byte.class && invocation.getMethod().getReturnType() != short.class && invocation.getMethod().getReturnType() != int.class && invocation.getMethod().getReturnType() != long.class && invocation.getMethod().getReturnType() != float.class && invocation.getMethod().getReturnType() != double.class && invocation.getMethod().getReturnType() != char.class));

        assertEquals(expectedByte, actualByte);
        assertEquals(expectedShort, actualShort);
        assertEquals(expectedInt, actualInt1);
        assertEquals(expectedInt, actualInt2);
        assertEquals(expectedLong, actualLong);
        assertEquals(expectedFloat, actualFloat, 0f);
        assertEquals(expectedDouble, actualDouble, 0d);
        assertEquals(expectedChar, actualChar);
        assertEquals(expectedBoolean, actualBoolean);
    }

    @Test
    public void testReturnEmptyCollections() {
        Mockito.<ReturnsEmptyValues>spy(ReturnsEmptyValues.class);
        ReturnsEmptyValues returnsEmptyValues = Mockito.doReturn(new ReturnsEmptyValues()).when(ReturnsEmptyValues.class);

        Collection<Integer> expectedCollection = Collections.emptyList();
        Set<Integer> expectedSet = Collections.emptySet();
        HashSet<Integer> expectedHashSet = Collections.emptySet();
        LinkedHashSet<Integer> expectedLinkedHashSet = Collections.emptySet();
        SortedSet<Integer> expectedSortedSet = Collections.emptySortedSet();
        TreeSet<Integer> expectedTreeSet = Collections.emptySortedSet();
        Map<Integer, Integer> expectedMap = Collections.emptyMap();
        HashMap<Integer, Integer> expectedHashMap = Collections.emptyMap();
        LinkedHashMap<Integer, Integer> expectedLinkedHashMap = Collections.emptyMap();
        SortedMap<Integer, Integer> expectedSortedMap = Collections.emptySortedMap();
        TreeMap<Integer, Integer> expectedTreeMap = Collections.emptySortedMap();

        Collection<Integer> actualCollection = returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType().isAssignableFrom(Collection.class)));
        Set<Integer> actualSet = returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType().isAssignableFrom(Set.class)));
        HashSet<Integer> actualHashSet = returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType().isAssignableFrom(HashSet.class)));
        LinkedHashSet<Integer> actualLinkedHashSet = returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType().isAssignableFrom(LinkedHashSet.class)));
        SortedSet<Integer> actualSortedSet = returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType().isAssignableFrom(SortedSet.class)));
        TreeSet<Integer> actualTreeSet = returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType().isAssignableFrom(TreeSet.class)));
        Map<Integer, Integer> actualMap = returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType().isAssignableFrom(Map.class)));
        HashMap<Integer, Integer> actualHashMap = returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType().isAssignableFrom(HashMap.class)));
        LinkedHashMap<Integer, Integer> actualLinkedHashMap = returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType().isAssignableFrom(LinkedHashMap.class)));
        SortedMap<Integer, Integer> actualSortedMap = returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType().isAssignableFrom(SortedMap.class)));
        TreeMap<Integer, Integer> actualTreeMap = returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType().isAssignableFrom(TreeMap.class)));

        assertEquals(expectedCollection, actualCollection);
        assertEquals(expectedSet, actualSet);
        assertEquals(expectedHashSet, actualHashSet);
        assertEquals(expectedLinkedHashSet, actualLinkedHashSet);
        assertEquals(expectedSortedSet, actualSortedSet);
        assertEquals(expectedTreeSet, actualTreeSet);
        assertEquals(expectedMap, actualMap);
        assertEquals(expectedHashMap, actualHashMap);
        assertEquals(expectedLinkedHashMap, actualLinkedHashMap);
        assertEquals(expectedSortedMap, actualSortedMap);
        assertEquals(expectedTreeMap, actualTreeMap);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testModifyReturnedCollection() {
        Mockito.<ReturnsEmptyValues>spy(ReturnsEmptyValues.class);
        ReturnsEmptyValues returnsEmptyValues = Mockito.doReturn(new ReturnsEmptyValues()).when(ReturnsEmptyValues.class);

        Collection<Integer> collection = new ArrayList<>();
        Mockito.when(returnsEmptyValues.answer(Mockito.any(InvocationOnMock.class))).thenReturn(collection);

        collection.add(1);
    }
}