
package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;


public class ReturnsEmptyValuesTest {

    ReturnsEmptyValues returnsEmptyValues;

    @Before
    public void setUp() {
        returnsEmptyValues = new ReturnsEmptyValues();
    }

    // Test answer() method
    @Test
    public void testAnswer() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = returnsEmptyValues.answer(invocation);

        assertNull(result);
    }

    // Test returnValueFor() method
    @Test
    public void testReturnValueFor() {
        Class<?> type = Collection.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        assertTrue(result instanceof LinkedList);

        type = Set.class;
        result = returnsEmptyValues.returnValueFor(type);
        assertTrue(result instanceof HashSet);

        type = HashSet.class;
        result = returnsEmptyValues.returnValueFor(type);
        assertTrue(result instanceof HashSet);

        type = SortedSet.class;
        result = returnsEmptyValues.returnValueFor(type);
        assertTrue(result instanceof TreeSet);

        type = TreeSet.class;
        result = returnsEmptyValues.returnValueFor(type);
        assertTrue(result instanceof TreeSet);

        type = LinkedHashSet.class;
        result = returnsEmptyValues.returnValueFor(type);
        assertTrue(result instanceof LinkedHashSet);

        type = List.class;
        result = returnsEmptyValues.returnValueFor(type);
        assertTrue(result instanceof LinkedList);

        type = LinkedList.class;
        result = returnsEmptyValues.returnValueFor(type);
        assertTrue(result instanceof LinkedList);

        type = ArrayList.class;
        result = returnsEmptyValues.returnValueFor(type);
        assertTrue(result instanceof ArrayList);

        type = Map.class;
        result = returnsEmptyValues.returnValueFor(type);
        assertTrue(result instanceof HashMap);

        type = HashMap.class;
        result = returnsEmptyValues.returnValueFor(type);
        assertTrue(result instanceof HashMap);

        type = SortedMap.class;
        result = returnsEmptyValues.returnValueFor(type);
        assertTrue(result instanceof TreeMap);

        type = TreeMap.class;
        result = returnsEmptyValues.returnValueFor(type);
        assertTrue(result instanceof TreeMap);

        type = LinkedHashMap.class;
        result = returnsEmptyValues.returnValueFor(type);
        assertTrue(result instanceof LinkedHashMap);
    }

}
