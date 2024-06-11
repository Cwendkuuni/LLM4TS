
package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

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

public class ReturnsEmptyValuesTest {
    @Test
    public void testAnswer() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        
        // Test toString method
        String toStringResult = (String) returnsEmptyValues.answer(null);
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("Mock for"));
        
        // Test compareTo method
        int compareToResult = (int) returnsEmptyValues.answer(null);
        assertEquals(1, compareToResult);
        
        // Test primitive and wrapper classes
        assertEquals(0, returnsEmptyValues.returnValueFor(int.class));
        assertEquals(0, returnsEmptyValues.returnValueFor(Integer.class));
        assertEquals(0L, returnsEmptyValues.returnValueFor(long.class));
        assertEquals(0L, returnsEmptyValues.returnValueFor(Long.class));
        assertEquals(0.0f, returnsEmptyValues.returnValueFor(float.class), 0.0f);
        assertEquals(0.0f, returnsEmptyValues.returnValueFor(Float.class), 0.0f);
        assertEquals(0.0, returnsEmptyValues.returnValueFor(double.class), 0.0);
        assertEquals(0.0, returnsEmptyValues.returnValueFor(Double.class), 0.0);
        assertEquals((char) 0, returnsEmptyValues.returnValueFor(char.class));
        assertEquals((char) 0, returnsEmptyValues.returnValueFor(Character.class));
        assertEquals(false, returnsEmptyValues.returnValueFor(boolean.class));
        assertEquals(false, returnsEmptyValues.returnValueFor(Boolean.class));
        assertEquals((byte) 0, returnsEmptyValues.returnValueFor(byte.class));
        assertEquals((byte) 0, returnsEmptyValues.returnValueFor(Byte.class));
        assertEquals((short) 0, returnsEmptyValues.returnValueFor(short.class));
        assertEquals((short) 0, returnsEmptyValues.returnValueFor(Short.class));
        
        // Test collections
        assertTrue(returnsEmptyValues.returnValueFor(Collection.class) instanceof LinkedList);
        assertTrue(returnsEmptyValues.returnValueFor(Set.class) instanceof HashSet);
        assertTrue(returnsEmptyValues.returnValueFor(HashSet.class) instanceof HashSet);
        assertTrue(returnsEmptyValues.returnValueFor(SortedSet.class) instanceof TreeSet);
        assertTrue(returnsEmptyValues.returnValueFor(TreeSet.class) instanceof TreeSet);
        assertTrue(returnsEmptyValues.returnValueFor(LinkedHashSet.class) instanceof LinkedHashSet);
        assertTrue(returnsEmptyValues.returnValueFor(List.class) instanceof LinkedList);
        assertTrue(returnsEmptyValues.returnValueFor(LinkedList.class) instanceof LinkedList);
        assertTrue(returnsEmptyValues.returnValueFor(ArrayList.class) instanceof ArrayList);
        assertTrue(returnsEmptyValues.returnValueFor(Map.class) instanceof HashMap);
        assertTrue(returnsEmptyValues.returnValueFor(HashMap.class) instanceof HashMap);
        assertTrue(returnsEmptyValues.returnValueFor(SortedMap.class) instanceof TreeMap);
        assertTrue(returnsEmptyValues.returnValueFor(TreeMap.class) instanceof TreeMap);
        assertTrue(returnsEmptyValues.returnValueFor(LinkedHashMap.class) instanceof LinkedHashMap);
        
        // Test other types
        assertNull(returnsEmptyValues.returnValueFor(String.class));
        assertNull(returnsEmptyValues.returnValueFor(Object.class));
    }
}
