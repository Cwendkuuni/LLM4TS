
package org.apache.commons.collections4.keyvalue;

import static org.junit.Assert.*;

import org.junit.Test;

public class MultiKeyTest {
    
    @Test
    public void testConstructorWithTwoKeys() {
        String key1 = "key1";
        Integer key2 = 2;
        
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        
        assertEquals(2, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
    }
    
    @Test
    public void testConstructorWithThreeKeys() {
        String key1 = "key1";
        Integer key2 = 2;
        Double key3 = 3.5;
        
        MultiKey<Number> multiKey = new MultiKey<>(key1, key2, key3);
        
        assertEquals(3, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
    }
    
    @Test
    public void testConstructorWithFourKeys() {
        String key1 = "key1";
        Integer key2 = 2;
        Double key3 = 3.5;
        Boolean key4 = true;
        
        MultiKey<Object> multiKey = new MultiKey<>(key1, key2, key3, key4);
        
        assertEquals(4, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
    }
    
    @Test
    public void testConstructorWithFiveKeys() {
        String key1 = "key1";
        Integer key2 = 2;
        Double key3 = 3.5;
        Boolean key4 = true;
        Object key5 = new Object();
        
        MultiKey<Object> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        
        assertEquals(5, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(key5, multiKey.getKey(4));
    }
    
    @Test
    public void testConstructorWithArray() {
        String[] keys = {"key1", "key2", "key3"};
        
        MultiKey<String> multiKey = new MultiKey<>(keys);
        
        assertEquals(keys.length, multiKey.size());
        for (int i = 0; i < keys.length; i++) {
            assertEquals(keys[i], multiKey.getKey(i));
        }
    }
    
    @Test
    public void testEqualsAndHashCode() {
        Integer key1 = 1;
        String key2 = "key2";
        
        MultiKey<Comparable<?>> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<Comparable<?>> multiKey2 = new MultiKey<>(key1, key2);
        
        assertTrue(multiKey1.equals(multiKey2));
        assertTrue(multiKey2.equals(multiKey1));
        assertTrue(multiKey1.hashCode() == multiKey2.hashCode());
    }
    
    @Test
    public void testEqualsAndHashCodeWithDifferentKeys() {
        Integer key1 = 1;
        String key2 = "key2";
        
        MultiKey<Comparable<?>> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<Comparable<?>> multiKey2 = new MultiKey<>(key2, key1);
        
        assertFalse(multiKey1.equals(multiKey2));
        assertFalse(multiKey2.equals(multiKey1));
        assertFalse(multiKey1.hashCode() == multiKey2.hashCode());
    }
    
    @Test
    public void testToString() {
        Integer key1 = 1;
        String key2 = "key2";
        
        MultiKey<Comparable<?>> multiKey = new MultiKey<>(key1, key2);
        
        String expectedToString = "MultiKey[" + key1.toString() + ", " + key2.toString() + "]";
        assertEquals(expectedToString, multiKey.toString());
    }
}
