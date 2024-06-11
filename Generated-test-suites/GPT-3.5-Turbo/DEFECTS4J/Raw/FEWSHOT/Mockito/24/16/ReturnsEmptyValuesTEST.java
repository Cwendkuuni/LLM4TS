package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();

        // ToString Method
        assertEquals("Mock for Foo, hashCode: " + values.hashCode(), values.answer(null));

        // CompareTo Method
        assertEquals(1, values.answer(null));

        // Primitive Return Type
        assertEquals(0, values.answer(null));

        // Collection Return Type
        assertEquals(new LinkedList<Object>(), values.answer(null));

        // Set Return Type
        assertEquals(new HashSet<Object>(), values.answer(null));

        // HashSet Return Type
        assertEquals(new HashSet<Object>(), values.answer(null));

        // SortedSet Return Type
        assertEquals(new TreeSet<Object>(), values.answer(null));

        // TreeSet Return Type
        assertEquals(new TreeSet<Object>(), values.answer(null));

        // LinkedHashSet Return Type
        assertEquals(new LinkedHashSet<Object>(), values.answer(null));

        // List Return Type
        assertEquals(new LinkedList<Object>(), values.answer(null));

        // LinkedList Return Type
        assertEquals(new LinkedList<Object>(), values.answer(null));

        // ArrayList Return Type
        assertEquals(new ArrayList<Object>(), values.answer(null));

        // Map Return Type
        assertEquals(new HashMap<Object, Object>(), values.answer(null));

        // HashMap Return Type
        assertEquals(new HashMap<Object, Object>(), values.answer(null));

        // SortedMap Return Type
        assertEquals(new TreeMap<Object, Object>(), values.answer(null));

        // TreeMap Return Type
        assertEquals(new TreeMap<Object, Object>(), values.answer(null));

        // LinkedHashMap Return Type
        assertEquals(new LinkedHashMap<Object, Object>(), values.answer(null));

        // Other Return Types
        assertNull(values.answer(null));
    }
}