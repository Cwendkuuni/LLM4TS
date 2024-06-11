package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ReturnsEmptyValuesTest {
    private ReturnsEmptyValues returnsEmptyValues;

    @Before
    public void setUp() {
        returnsEmptyValues = new ReturnsEmptyValues();
    }

    @Test
    public void testAnswer() {
        // Test for toString() method
        Object mock1 = new Object();
        Object mock2 = new Object();
        assertEquals("Mock for Object, hashCode: " + mock1.hashCode(), returnsEmptyValues.answer(null));
        assertEquals("Mock for Object, hashCode: " + mock2.hashCode(), returnsEmptyValues.answer(null));

        // Test for compareTo() method
        assertEquals(0, returnsEmptyValues.answer(null));
        assertEquals(1, returnsEmptyValues.answer(null));

        // Test for other methods
        assertNull(returnsEmptyValues.answer(null));
    }

    @Test
    public void testReturnValueFor() {
        // Test for primitive and wrapper classes
        assertEquals(0, returnsEmptyValues.returnValueFor(int.class));
        assertEquals(0, returnsEmptyValues.returnValueFor(Integer.class));

        // Test for Collection class and its subclasses
        assertTrue(returnsEmptyValues.returnValueFor(Collection.class) instanceof LinkedList);
        assertTrue(returnsEmptyValues.returnValueFor(Set.class) instanceof HashSet);
        assertTrue(returnsEmptyValues.returnValueFor(HashSet.class) instanceof HashSet);
        assertTrue(returnsEmptyValues.returnValueFor(SortedSet.class) instanceof TreeSet);
        assertTrue(returnsEmptyValues.returnValueFor(TreeSet.class) instanceof TreeSet);
        assertTrue(returnsEmptyValues.returnValueFor(LinkedHashSet.class) instanceof LinkedHashSet);
        assertTrue(returnsEmptyValues.returnValueFor(List.class) instanceof LinkedList);
        assertTrue(returnsEmptyValues.returnValueFor(LinkedList.class) instanceof LinkedList);
        assertTrue(returnsEmptyValues.returnValueFor(ArrayList.class) instanceof ArrayList);

        // Test for Map class and its subclasses
        assertTrue(returnsEmptyValues.returnValueFor(Map.class) instanceof HashMap);
        assertTrue(returnsEmptyValues.returnValueFor(HashMap.class) instanceof HashMap);
        assertTrue(returnsEmptyValues.returnValueFor(SortedMap.class) instanceof TreeMap);
        assertTrue(returnsEmptyValues.returnValueFor(TreeMap.class) instanceof TreeMap);
        assertTrue(returnsEmptyValues.returnValueFor(LinkedHashMap.class) instanceof LinkedHashMap);
    }
}