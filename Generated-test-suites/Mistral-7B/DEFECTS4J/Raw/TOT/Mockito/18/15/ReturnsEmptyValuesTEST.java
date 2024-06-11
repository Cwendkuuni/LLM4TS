package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues answer;

    @Before
    public void setup() {
        answer = new ReturnsEmptyValues();
    }

    @Test
    public void testToString() {
        Object mock = mock(String.class);
        InvocationOnMock invocation = Mockito.methodCall(mock, "toString");
        Object result = answer.answer(invocation);
        assertEquals("Mock for String, hashCode: " + mock.hashCode(), result.toString());
    }

    @Test
    public void testIsSameInstance() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        InvocationOnMock invocation = Mockito.methodCall(mock1, "compareTo", ArgumentMatchers.any());
        invocation.argsFor(mock2);
        int result = (int) answer.answer(invocation);
        assertEquals(0, result);
    }

    @Test
    public void testCompareToDifferentInstances() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        InvocationOnMock invocation = Mockito.methodCall(mock1, "compareTo", ArgumentMatchers.any());
        invocation.argsFor(mock2);
        int result = (int) answer.answer(invocation);
        assertEquals(1, result);
    }

    @Test
    public void testPrimitiveTypes() {
        Integer mock = mock(Integer.class);
        InvocationOnMock intInvocation = Mockito.methodCall(mock, "intValue");
        int intResult = (int) answer.answer(intInvocation);
        assertEquals(0, intResult);

        Float mockFloat = mock(Float.class);
        InvocationOnMock floatInvocation = Mockito.methodCall(mockFloat, "floatValue");
        float floatResult = (float) answer.answer(floatInvocation);
        assertEquals(0.0f, floatResult, 0.001f);
    }

    @Test
    public void testEmptyCollections() {
        Collection<Integer> mockCollection = mock(Collection.class);
        InvocationOnMock collectionInvocation = Mockito.methodCall(mockCollection, "isEmpty");
        boolean result = (boolean) answer.answer(collectionInvocation);
        assertEquals(true, result);

        Set<Integer> mockSet = mock(Set.class);
        InvocationOnMock setInvocation = Mockito.methodCall(mockSet, "isEmpty");
        boolean resultSet = (boolean) answer.answer(setInvocation);
        assertEquals(true, resultSet);

        List<String> mockList = mock(List.class);
        InvocationOnMock listInvocation = Mockito.methodCall(mockList, "isEmpty");
        boolean resultList = (boolean) answer.answer(listInvocation);
        assertEquals(true, resultList);
    }

    @Test
    public void testMaps() {
        Map<String, Integer> mockMap = mock(Map.class);
        InvocationOnMock mapInvocation = Mockito.methodCall(mockMap, "isEmpty");
        boolean resultMap = (boolean) answer.answer(mapInvocation);
        assertEquals(true, resultMap);

        HashMap<String, Integer> mockHashMap = mock(HashMap.class);
        InvocationOnMock hashMapInvocation = Mockito.methodCall(mockHashMap, "isEmpty");
        boolean resultHashMap = (boolean) answer.answer(hashMapInvocation);
        assertEquals(true, resultHashMap);

        SortedMap<String, Integer> mockSortedMap = mock(SortedMap.class);
        InvocationOnMock sortedMapInvocation = Mockito.methodCall(mockSortedMap, "isEmpty");
        boolean resultSortedMap = (boolean) answer.answer(sortedMapInvocation);
        assertEquals(true, resultSortedMap);

        TreeMap<String, Integer> mockTreeMap = mock(TreeMap.class);
        InvocationOnMock treeMapInvocation = Mockito.methodCall(mockTreeMap, "isEmpty");
        boolean resultTreeMap = (boolean) answer.answer(treeMapInvocation);
        assertEquals(true, resultTreeMap);
        // continue testing other methods as needed
    }

    @Test
    public void testOtherClasses() {
        Object otherMock = mock(Object.class);
        InvocationOnMock otherInvocation = Mockito.methodCall(otherMock, "toString");
        Object resultOther = answer.answer(otherInvocation);
        assertEquals(null, resultOther);
    }
}