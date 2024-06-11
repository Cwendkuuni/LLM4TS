
package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import java.util.*;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        // Test toString() method
        Object mock = new Object();
        mock.hashCode();
        String expectedToString = "Mock for java.lang.Object, hashCode: " + mock.hashCode();
        assertEquals(expectedToString, answer.answer(null));

        // Test Comparable#compareTo() method
        assertEquals(1, answer.answer(null));

        // Test primitive-returning methods
        assertEquals(0, answer.answer(null));

        // Test wrapper class-returning methods
        assertEquals(0, answer.answer(null));

        // Test collection-returning methods
        assertNotNull(answer.answer(null));

        // Test other methods
        assertNull(answer.answer(null));
    }

    @Test
    public void testReturnValueFor() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        // Test primitive or wrapper class types
        assertEquals(0, answer.returnValueFor(int.class));
        assertEquals(0, answer.returnValueFor(Integer.class));
        assertEquals(0L, answer.returnValueFor(long.class));
        assertEquals(0L, answer.returnValueFor(Long.class));
        assertEquals(0.0f, answer.returnValueFor(float.class), 0.0001);
        assertEquals(0.0f, answer.returnValueFor(Float.class), 0.0001);
        assertEquals(0.0, answer.returnValueFor(double.class), 0.0001);
        assertEquals(0.0, answer.returnValueFor(Double.class), 0.0001);
        assertEquals(false, answer.returnValueFor(boolean.class));
        assertEquals(false, answer.returnValueFor(Boolean.class));
        assertEquals(NULL_CHAR, answer.returnValueFor(char.class));
        assertEquals(NULL_CHAR, answer.returnValueFor(Character.class));
        assertEquals("", answer.returnValueFor(String.class));

        // Test collection types
        assertTrue(answer.returnValueFor(Collection.class) instanceof LinkedList);
        assertTrue(answer.returnValueFor(Set.class) instanceof HashSet);
        assertTrue(answer.returnValueFor(HashSet.class) instanceof HashSet);
        assertTrue(answer.returnValueFor(SortedSet.class) instanceof TreeSet);
        assertTrue(answer.returnValueFor(TreeSet.class) instanceof TreeSet);
        assertTrue(answer.returnValueFor(LinkedHashSet.class) instanceof LinkedHashSet);
        assertTrue(answer.returnValueFor(List.class) instanceof LinkedList);
        assertTrue(answer.returnValueFor(LinkedList.class) instanceof LinkedList);
        assertTrue(answer.returnValueFor(ArrayList.class) instanceof ArrayList);
        assertTrue(answer.returnValueFor(Map.class) instanceof HashMap);
        assertTrue(answer.returnValueFor(HashMap.class) instanceof HashMap);
        assertTrue(answer.returnValueFor(SortedMap.class) instanceof TreeMap);
        assertTrue(answer.returnValueFor(TreeMap.class) instanceof TreeMap);
        assertTrue(answer.returnValueFor(LinkedHashMap.class) instanceof LinkedHashMap);

        // Test other types
        assertNull(answer.returnValueFor(Object.class));
    }
}
