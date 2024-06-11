package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
    public void testAnswer() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        
        // Test toString() method
        assertEquals("Mock for Object, hashCode: 0", returnsEmptyValues.answer(new InvocationOnMock(null, Object.class.getMethods()[0], null)));
        
        // Test compareTo() method
        assertEquals(1, returnsEmptyValues.answer(new InvocationOnMock(null, Comparable.class.getMethods()[0], null)));
        
        // Test primitive-returning methods
        assertEquals(0, returnsEmptyValues.answer(new InvocationOnMock(null, int.class.getMethods()[0], null)));
        assertEquals(0, returnsEmptyValues.answer(new InvocationOnMock(null, Integer.class.getMethods()[0], null)));
        
        // Test collection-returning methods
        assertEquals(new LinkedList<Object>(), returnsEmptyValues.answer(new InvocationOnMock(null, Collection.class.getMethods()[0], null)));
        assertEquals(new HashSet<Object>(), returnsEmptyValues.answer(new InvocationOnMock(null, Set.class.getMethods()[0], null)));
        assertEquals(new HashSet<Object>(), returnsEmptyValues.answer(new InvocationOnMock(null, HashSet.class.getMethods()[0], null)));
        assertEquals(new TreeSet<Object>(), returnsEmptyValues.answer(new InvocationOnMock(null, SortedSet.class.getMethods()[0], null)));
        assertEquals(new TreeSet<Object>(), returnsEmptyValues.answer(new InvocationOnMock(null, TreeSet.class.getMethods()[0], null)));
        assertEquals(new LinkedHashSet<Object>(), returnsEmptyValues.answer(new InvocationOnMock(null, LinkedHashSet.class.getMethods()[0], null)));
        assertEquals(new LinkedList<Object>(), returnsEmptyValues.answer(new InvocationOnMock(null, List.class.getMethods()[0], null)));
        assertEquals(new LinkedList<Object>(), returnsEmptyValues.answer(new InvocationOnMock(null, LinkedList.class.getMethods()[0], null)));
        assertEquals(new ArrayList<Object>(), returnsEmptyValues.answer(new InvocationOnMock(null, ArrayList.class.getMethods()[0], null)));
        assertEquals(new HashMap<Object, Object>(), returnsEmptyValues.answer(new InvocationOnMock(null, Map.class.getMethods()[0], null)));
        assertEquals(new HashMap<Object, Object>(), returnsEmptyValues.answer(new InvocationOnMock(null, HashMap.class.getMethods()[0], null)));
        assertEquals(new TreeMap<Object, Object>(), returnsEmptyValues.answer(new InvocationOnMock(null, SortedMap.class.getMethods()[0], null)));
        assertEquals(new TreeMap<Object, Object>(), returnsEmptyValues.answer(new InvocationOnMock(null, TreeMap.class.getMethods()[0], null)));
        assertEquals(new LinkedHashMap<Object, Object>(), returnsEmptyValues.answer(new InvocationOnMock(null, LinkedHashMap.class.getMethods()[0], null)));
        
        // Test other methods
        assertNull(returnsEmptyValues.answer(new InvocationOnMock(null, ReturnsEmptyValues.class.getMethods()[0], null)));
    }
}