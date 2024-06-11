package org.apache.commons.collections4.keyvalue;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {
    
    @Test
    public void testConstructor_TwoKeys() {
        String key1 = "key1";
        String key2 = "key2";
        
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        
        assertEquals(2, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
    }
    
    @Test
    public void testConstructor_ThreeKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);
        
        assertEquals(3, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
    }
    
    @Test
    public void testConstructor_FourKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4);
        
        assertEquals(4, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
    }
    
    @Test
    public void testConstructor_FiveKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        String key5 = "key5";
        
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        
        assertEquals(5, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(key5, multiKey.getKey(4));
    }
    
    @Test
    public void testConstructor_ArrayKeys() {
        String[] keys = {"key1", "key2", "key3"};
        
        MultiKey<String> multiKey = new MultiKey<>(keys);
        
        assertEquals(3, multiKey.size());
        assertEquals(keys[0], multiKey.getKey(0));
        assertEquals(keys[1], multiKey.getKey(1));
        assertEquals(keys[2], multiKey.getKey(2));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullKeys() {
        MultiKey<String> multiKey = new MultiKey<>(null);
    }
    
    @Test
    public void testEquals_SameKeys() {
        String key1 = "key1";
        String key2 = "key2";
        
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);
        
        assertTrue(multiKey1.equals(multiKey2));
    }
    
    @Test
    public void testEquals_DifferentKeys() {
        String key1 = "key1";
        String key2 = "key2";
        
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key2, key1);
        
        assertFalse(multiKey1.equals(multiKey2));
    }
    
    @Test
    public void testHashCode_SameKeys() {
        String key1 = "key1";
        String key2 = "key2";
        
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);
        
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }
    
    @Test
    public void testHashCode_DifferentKeys() {
        String key1 = "key1";
        String key2 = "key2";
        
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key2, key1);
        
        assertNotEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }
    
    @Test
    public void testToString() {
        String[] keys = {"key1", "key2"};
        
        MultiKey<String> multiKey = new MultiKey<>(keys);
        
        assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }
    
    @Test
    public void testGetKeys() {
        String[] keys = {"key1", "key2"};
        
        MultiKey<String> multiKey = new MultiKey<>(keys);
        
        assertArrayEquals(keys, multiKey.getKeys());
    }
    
    @Test
    public void testGetKey_ValidIndex() {
        String key1 = "key1";
        String key2 = "key2";
        
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKey_InvalidIndex() {
        String key1 = "key1";
        String key2 = "key2";
        
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        
        multiKey.getKey(2);
    }
}