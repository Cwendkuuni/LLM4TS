package org.apache.commons.collections4.keyvalue;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class MultiKeyTest {
    @Test
    public void testConstructorWithTwoKeys() {
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<String>(key1, key2);
        
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(2, multiKey.size());
    }
    
    @Test
    public void testConstructorWithThreeKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        MultiKey<String> multiKey = new MultiKey<String>(key1, key2, key3);
        
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(3, multiKey.size());
    }
    
    @Test
    public void testConstructorWithFourKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        MultiKey<String> multiKey = new MultiKey<String>(key1, key2, key3, key4);
        
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(4, multiKey.size());
    }
    
    @Test
    public void testConstructorWithFiveKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        String key5 = "key5";
        MultiKey<String> multiKey = new MultiKey<String>(key1, key2, key3, key4, key5);
        
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(key5, multiKey.getKey(4));
        assertEquals(5, multiKey.size());
    }
    
    @Test
    public void testConstructorWithArray() {
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<String>(keys);
        
        assertArrayEquals(keys, multiKey.getKeys());
    }
    
    @Test
    public void testEquals() {
        MultiKey<String> multiKey1 = new MultiKey<String>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<String>("key1", "key2");
        MultiKey<String> multiKey3 = new MultiKey<String>("key1", "key3");
        
        assertEquals(multiKey1, multiKey2);
        assertFalse(multiKey1.equals(multiKey3));
    }
    
    @Test
    public void testHashCode() {
        MultiKey<String> multiKey1 = new MultiKey<String>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<String>("key1", "key2");
        MultiKey<String> multiKey3 = new MultiKey<String>("key1", "key3");
        
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
        assertFalse(multiKey1.hashCode() == multiKey3.hashCode());
    }
    
    @Test
    public void testToString() {
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<String>(keys);
        String expected = "MultiKey[key1, key2, key3]";
        
        assertEquals(expected, multiKey.toString());
    }
}