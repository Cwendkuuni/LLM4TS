
package org.apache.commons.collections4.keyvalue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MultiKeyTest {
    @Test
    public void testConstructor1() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2);
        
        assertEquals(2, multiKey.size());
        assertEquals(Integer.valueOf(1), multiKey.getKey(0));
        assertEquals(Integer.valueOf(2), multiKey.getKey(1));
    }
    
    @Test
    public void testConstructor2() {
        MultiKey<String> multiKey = new MultiKey<>("a", "b", "c");
        
        assertEquals(3, multiKey.size());
        assertEquals("a", multiKey.getKey(0));
        assertEquals("b", multiKey.getKey(1));
        assertEquals("c", multiKey.getKey(2));
    }
    
    @Test
    public void testConstructor3() {
        Integer[] keys = {1, 2, 3};
        MultiKey<Integer> multiKey = new MultiKey<>(keys);
        
        assertEquals(3, multiKey.size());
        assertEquals(Integer.valueOf(1), multiKey.getKey(0));
        assertEquals(Integer.valueOf(2), multiKey.getKey(1));
        assertEquals(Integer.valueOf(3), multiKey.getKey(2));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor4() {
        MultiKey<Integer> multiKey = new MultiKey<>(null);
    }
    
    @Test
    public void testGetKeys() {
        MultiKey<String> multiKey = new MultiKey<>("a", "b", "c");
        String[] keys = multiKey.getKeys();
        
        assertEquals(3, keys.length);
        assertEquals("a", keys[0]);
        assertEquals("b", keys[1]);
        assertEquals("c", keys[2]);
    }
    
    @Test
    public void testEquals() {
        MultiKey<String> multiKey1 = new MultiKey<>("a", "b");
        MultiKey<String> multiKey2 = new MultiKey<>("a", "b");
        MultiKey<String> multiKey3 = new MultiKey<>("a", "c");
        
        assertTrue(multiKey1.equals(multiKey1));
        assertTrue(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(multiKey3));
        assertFalse(multiKey1.equals(null));
        assertFalse(multiKey1.equals("a"));
    }
    
    @Test
    public void testHashCode() {
        MultiKey<String> multiKey1 = new MultiKey<>("a", "b");
        MultiKey<String> multiKey2 = new MultiKey<>("a", "b");
        
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }
    
    @Test
    public void testToString() {
        MultiKey<String> multiKey = new MultiKey<>("a", "b");
        
        assertEquals("MultiKey[a, b]", multiKey.toString());
    }
}
