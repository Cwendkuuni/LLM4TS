package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        MockName mockName = Mockito.mock(MockName.class);
        when(mockName.isDefault()).thenReturn(true);
        when(mockName.toString()).thenReturn("MockName");

        ObjectsUnderTest objectsUnderTest = new ObjectsUnderTest();
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Object result = returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withRealMethod(objectsUnderTest.toStringMethod()));

        assertEquals("Mock for Mockito$1@8a9c46b, hashCode: 123456", result.toString());
    }

    @Test
    public void testCompareToMethod() {
        Object mock = mock(Comparable.class);
        Object other = mock(Comparable.class);

        when(mock.compareTo(ArgumentMatchers.any())).thenCallRealMethod();

        int result = ((Answer<Integer>) new ReturnsEmptyValues()).answer(Mockito.mock(InvocationOnMock.class).withArguments(mock, other).withRealMethod(ArgumentMatchers.method(Comparable.class, "compareTo"))).get();

        assertNotNull(result);
        assertEquals(1, result);
    }

    @Test
    public void testPrimitiveTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        byte resultByte = (byte) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withRealMethod(Primitives::byteValue)).get();
        short resultShort = (short) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withRealMethod(Primitives::shortValue)).get();
        int resultInt = (int) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withRealMethod(Primitives::intValue)).get();
        long resultLong = (long) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withRealMethod(Primitives::longValue)).get();
        float resultFloat = (float) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withRealMethod(Primitives::floatValue)).get();
        double resultDouble = (double) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withRealMethod(Primitives::doubleValue)).get();
        char resultChar = (char) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withRealMethod(Primitives::charValue)).get();
        boolean resultBoolean = (boolean) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withRealMethod(Primitives::booleanValue)).get();

        assertEquals(0, resultByte);
        assertEquals(0, resultShort);
        assertEquals(0, resultInt);
        assertEquals(0L, resultLong);
        assertEquals(0.0f, resultFloat, 0.001f);
        assertEquals(0.0d, resultDouble, 0.001d);
        assertEquals((char) 0, resultChar);
        assertEquals(false, resultBoolean);
    }

    @Test
    public void testEmptyCollections() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Collection<String> resultCollection = (Collection<String>) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withRealMethod(Collection::new)).get();
        Set<String> resultSet = (Set<String>) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withRealMethod(Set::new)).get();
        HashSet<String> resultHashSet = (HashSet<String>) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withRealMethod(HashSet::new)).get();
        LinkedHashSet<String> resultLinkedHashSet = (LinkedHashSet<String>) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withRealMethod(LinkedHashSet::new)).get();
        List<String> resultList = (List<String>) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withRealMethod(List::new)).get();
        LinkedList<String> resultLinkedList = (LinkedList<String>) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withRealMethod(LinkedList::new)).get();
        ArrayList<String> resultArrayList = (ArrayList<String>) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withRealMethod(ArrayList::new)).get();
        Map<String, String> resultMap = (Map<String, String>) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withRealMethod(Map::new)).get();
        HashMap<String, String> resultHashMap = (HashMap<String, String>) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withRealMethod(HashMap::new)).get();
        SortedMap<String, String> resultSortedMap = (SortedMap<String, String>) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withRealMethod(SortedMap::new)).get();
        TreeMap<String, String> resultTreeMap = (TreeMap<String, String>) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withRealMethod(TreeMap::new)).get();
        LinkedHashMap<String, String> resultLinkedHashMap = (LinkedHashMap<String, String>) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withRealMethod(LinkedHashMap::new)).get();

        assertEquals(0, resultCollection.size());
        assertEquals(0, resultSet.size());
        assertEquals(0, resultHashSet.size());
        assertEquals(0, resultLinkedHashSet.size());
        assertEquals(0, resultList.size());
        assertEquals(0, resultLinkedList.size());
        assertEquals(0, resultArrayList.size());
        assertEquals(0, resultMap.size());
        assertEquals(0, resultHashMap.size());
        assertEquals(0, resultSortedMap.size());
        assertEquals(0, resultTreeMap.size());
        assertEquals(0, resultLinkedHashMap.size());
    }

    private interface Comparable {}

    private static class ObjectsUnderTest {
        public String toStringMethod() {
            return "test";
        }
    }
}