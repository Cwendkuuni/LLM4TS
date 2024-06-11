package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

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
import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;

public class ReturnsEmptyValuesTest {

    @Test
    public void testDefaultAnswer() {
        ObjectMethodsGuru methodsGuru = new ObjectMethodsGuru();
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // Test for toString() method
        MockName mockName = mock(Object.class);
        MockUtil mockUtil = mock(MockUtil.class);
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setMethod(mock(Method.class));
        invocation.setArguments(new Object[] { mockName });

        assertEquals("Mock for [MockitoMock], hashCode: 214639613", answer.answer(invocation));

        // Test for Comparable#compareTo(T other) method
        Object mock = mock(Comparable.class);
        Object other = mock(Comparable.class);
        invocation.setMethod(mock.getClass().getMethod("compareTo", Comparable.class));
        invocation.setArguments(new Object[] { other });

        assertEquals(0, answer.answer(invocation));
        assertEquals(1, answer.answer(invocation).intValue());

        // Test for primitive and wrapper types
        for (Class<?> type : new Class[] { byte.class, int.class, long.class, boolean.class, double.class, Byte.class, Integer.class, Long.class, Boolean.class, Double.class }) {
            assertNotNull(returnsEmptyValues.returnValueFor(type));
            assertEquals(Primitives.unwrap(Primitives.defaultValueForPrimitiveOrWrapper(type)), returnsEmptyValues.returnValueFor(type));
        }

        // Test for collection types
        for (Class<?> collectionType : new Class[] { Collection.class, Set.class, HashSet.class, SortedSet.class, TreeSet.class, LinkedHashSet.class, List.class, LinkedList.class, ArrayList.class, HashMap.class, LinkedHashMap.class, TreeMap.class }) {
            assertNotNull(returnsEmptyValues.returnValueFor(collectionType));
            assertTrue(returnsEmptyValues.returnValueFor(collectionType).isEmpty());
        }

        // Test for Map types
        for (Class<?> mapType : new Class[] { Map.class, HashMap.class, SortedMap.class, TreeMap.class, LinkedHashMap.class }) {
            assertNotNull(returnsEmptyValues.returnValueFor(mapType));
            assertTrue(returnsEmptyValues.returnValueFor(mapType).isEmpty());
        }
    }

    @Test
    public void testNullReturn() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Class<?> nullType = null;

        assertNull(returnsEmptyValues.returnValueFor(nullType));
    }
}