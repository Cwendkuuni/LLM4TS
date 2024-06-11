
package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
        
        Object value = returnsEmptyValues.answer(null);
        assertNull(value);
    }
    
    @Test
    public void testReturnValueFor_Primitive() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        
        // String
        assertEquals("", returnsEmptyValues.returnValueFor(String.class));
        
        // Integer
        assertEquals(0, returnsEmptyValues.returnValueFor(Integer.TYPE));
        assertEquals(0, returnsEmptyValues.returnValueFor(Integer.class));
        
        // Double
        assertEquals(0.0, returnsEmptyValues.returnValueFor(Double.TYPE));
        assertEquals(0.0, returnsEmptyValues.returnValueFor(Double.class));
        
        // Boolean
        assertEquals(false, returnsEmptyValues.returnValueFor(Boolean.TYPE));
        assertEquals(false, returnsEmptyValues.returnValueFor(Boolean.class));
        
        // Character
        assertEquals('\u0000', returnsEmptyValues.returnValueFor(Character.TYPE));
        assertEquals('\u0000', returnsEmptyValues.returnValueFor(Character.class));
        
        // Float
        assertEquals(0.0f, returnsEmptyValues.returnValueFor(Float.TYPE));
        assertEquals(0.0f, returnsEmptyValues.returnValueFor(Float.class));
        
        // Long
        assertEquals(0L, returnsEmptyValues.returnValueFor(Long.TYPE));
        assertEquals(0L, returnsEmptyValues.returnValueFor(Long.class));
        
        // Short
        assertEquals((short)0, returnsEmptyValues.returnValueFor(Short.TYPE));
        assertEquals((short)0, returnsEmptyValues.returnValueFor(Short.class));
        
        // Byte
        assertEquals((byte)0, returnsEmptyValues.returnValueFor(Byte.TYPE));
        assertEquals((byte)0, returnsEmptyValues.returnValueFor(Byte.class));
        
    }
    
    @Test
    public void testReturnValueFor_Collection() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        
        // Collection
        assertTrue(returnsEmptyValues.returnValueFor(Collection.class) instanceof LinkedList);
        
        // Set
        assertTrue(returnsEmptyValues.returnValueFor(Set.class) instanceof HashSet);
        
        // HashSet
        assertTrue(returnsEmptyValues.returnValueFor(HashSet.class) instanceof HashSet);
        
        // SortedSet
        assertTrue(returnsEmptyValues.returnValueFor(SortedSet.class) instanceof TreeSet);
        
        // TreeSet
        assertTrue(returnsEmptyValues.returnValueFor(TreeSet.class) instanceof TreeSet);
        
        // LinkedHashSet
        assertTrue(returnsEmptyValues.returnValueFor(LinkedHashSet.class) instanceof LinkedHashSet);
        
        // List
        assertTrue(returnsEmptyValues.returnValueFor(List.class) instanceof LinkedList);
        
        // LinkedList
        assertTrue(returnsEmptyValues.returnValueFor(LinkedList.class) instanceof LinkedList);
        
        // ArrayList
        assertTrue(returnsEmptyValues.returnValueFor(ArrayList.class) instanceof ArrayList);
    }
    
    @Test
    public void testReturnValueFor_Map() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        
        // Map
        assertTrue(returnsEmptyValues.returnValueFor(Map.class) instanceof HashMap);
        
        // HashMap
        assertTrue(returnsEmptyValues.returnValueFor(HashMap.class) instanceof HashMap);
        
        // SortedMap
        assertTrue(returnsEmptyValues.returnValueFor(SortedMap.class) instanceof TreeMap);
        
        // TreeMap
        assertTrue(returnsEmptyValues.returnValueFor(TreeMap.class) instanceof TreeMap);
        
        // LinkedHashMap
        assertTrue(returnsEmptyValues.returnValueFor(LinkedHashMap.class) instanceof LinkedHashMap);
    }
}
