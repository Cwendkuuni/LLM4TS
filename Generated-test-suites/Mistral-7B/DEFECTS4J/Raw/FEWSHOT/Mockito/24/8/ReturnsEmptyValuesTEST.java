package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.internal.util.Primitives.isPrimitiveOrWrapper;
import static org.mockito.internal.util.Primitives.defaultValueForPrimitiveOrWrapper;

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
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsEmptyValuesTest {
    @Test
    public void testAnswer() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // Test for toString() method
        MockUtil mockUtil = MockUtil.createMock(String.class);
        InvocationOnMock invocation = mockUtil.createInvocationOnMock("toString");
        Object result = returnsEmptyValues.answer(invocation);
        assertEquals("Mock for java.lang.String, hashCode: " + mockUtil.getMockName(mockUtil.getMock()).hashCode(), result.toString());

        // Test for Comparable#compareTo(T other) method
        Integer mockInteger = MockUtil.createMock(Integer.class);
        invocation = mockUtil.createInvocationOnMock("compareTo", mockInteger);
        int resultCompareTo = (int) returnsEmptyValues.answer(invocation);
        assertEquals(1, resultCompareTo);

        // Test for primitive types and their wrappers
        for (Class<?> type : new Class[] { byte.class, short.class, int.class, long.class, char.class, float.class, double.class, boolean.class, Byte.class, Short.class, Integer.class, Long.class, Character.class, Float.class, Double.class, Boolean.class }) {
            Object resultPrimitive = returnsEmptyValues.returnValueFor(type);
            Object expected = defaultValueForPrimitiveOrWrapper(type);
            assertEquals(expected, resultPrimitive);
        }

        // Test for Collection, Set, Map, List, etc.
        for (Class<?> type : new Class[] { Collection.class, Set.class, HashSet.class, SortedSet.class, TreeSet.class, LinkedHashSet.class, List.class, LinkedList.class, ArrayList.class, Map.class, HashMap.class, SortedMap.class, TreeMap.class, LinkedHashMap.class }) {
            Object resultEmptyCollection = returnsEmptyValues.returnValueFor(type);
            if (type.isInterface()) {
                assertNull(resultEmptyCollection);
            } else {
                Collection expected = (type == Collection.class) ? new ArrayList<Object>() : (type == Set.class) ? new HashSet<Object>() : new ArrayList<Object>();
                assertEquals(expected, resultEmptyCollection);
            }
        }
    }
}