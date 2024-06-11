
package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        // Test primitive-returning methods
        assertEquals(0, answer.answer(null));
        assertEquals(0L, answer.answer(null));
        assertEquals(0F, answer.answer(null));
        assertEquals(0D, answer.answer(null));
        assertEquals((char) 0, answer.answer(null));
        assertEquals(false, answer.answer(null));

        // Test Wrapper class-returning methods
        assertEquals(0, answer.answer(null));
        assertEquals(0L, answer.answer(null));
        assertEquals(0F, answer.answer(null));
        assertEquals(0D, answer.answer(null));
        assertEquals((char) 0, answer.answer(null));
        assertEquals(false, answer.answer(null));

        // Test collection-returning methods
        assertTrue(answer.answer(null) instanceof LinkedList);
        assertTrue(answer.answer(null) instanceof HashSet);
        assertTrue(answer.answer(null) instanceof HashSet);
        assertTrue(answer.answer(null) instanceof SortedSet);
        assertTrue(answer.answer(null) instanceof TreeSet);
        assertTrue(answer.answer(null) instanceof LinkedHashSet);
        assertTrue(answer.answer(null) instanceof LinkedList);
        assertTrue(answer.answer(null) instanceof LinkedList);
        assertTrue(answer.answer(null) instanceof ArrayList);
        assertTrue(answer.answer(null) instanceof HashMap);
        assertTrue(answer.answer(null) instanceof HashMap);
        assertTrue(answer.answer(null) instanceof SortedMap);
        assertTrue(answer.answer(null) instanceof TreeMap);
        assertTrue(answer.answer(null) instanceof LinkedHashMap);

        // Test toString() method
        assertEquals("Mock for Object, hashCode: 0", answer.answer(null));

        // Test compareTo() method
        assertEquals(0, answer.answer(null));
    }
}
