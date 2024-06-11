package org.apache.commons.collections4.keyvalue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MultiKeyTest {
    
    @Test
    public void testConstructorWithTwoKeys() {
        Integer key1 = 1;
        String key2 = "two";
        
        MultiKey<Object> multiKey = new MultiKey<>(key1, key2);
        
        assertNotNull(multiKey);
        assertEquals(2, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
    }
    
    @Test
    public void testConstructorWithThreeKeys() {
        Integer key1 = 1;
        String key2 = "two";
        Double key3 = 3.0;
        
        MultiKey<Object> multiKey = new MultiKey<>(key1, key2, key3);
        
        assertNotNull(multiKey);
        assertEquals(3, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
    }
    
    @Test
    public void testConstructorWithFourKeys() {
        Integer key1 = 1;
        String key2 = "two";
        Double key3 = 3.0;
        Boolean key4 = true;
        
        MultiKey<Object> multiKey = new MultiKey<>(key1, key2, key3, key4);
        
        assertNotNull(multiKey);
        assertEquals(4, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
    }
    
    @Test
    public void testConstructorWithFiveKeys() {
        Integer key1 = 1;
        String key2 = "two";
        Double key3 = 3.0;
        Boolean key4 = true;
        Character key5 = '5';
        
        MultiKey<Object> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        
        assertNotNull(multiKey);
        assertEquals(5, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(key5, multiKey.getKey(4));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullKeysArray() {
        new MultiKey<>(null);
    }
    
    @Test
    public void testConstructorWithKeysArray() {
        Integer key1 = 1;
        String key2 = "two";
        
        Object[] keys = {key1, key2};
        
        MultiKey<Object> multiKey = new MultiKey<>(keys);
        
        assertNotNull(multiKey);
        assertEquals(2, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
    }
    
    @Test
    public void testConstructorWithKeysArray_CloneKeysFalse() {
        Integer key1 = 1;
        String key2 = "two";
        
        Object[] keys = {key1, key2};
        
        MultiKey<Object> multiKey = new MultiKey<>(keys, false);
        
        assertNotNull(multiKey);
        assertEquals(2, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        
        keys[0] = 2;
        assertNotEquals(keys[0], multiKey.getKey(0));
    }
    
    @Test
    public void testEquals() {
        Integer key1 = 1;
        String key2 = "two";
        
        MultiKey<Object> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<Object> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<Object> multiKey3 = new MultiKey<>(key2, key1);
        
        assertTrue(multiKey1.equals(multiKey1));
        assertTrue(multiKey1.equals(multiKey2));
        assertTrue(multiKey2.equals(multiKey1));
        assertNotEquals(multiKey1, multiKey3);
        assertNotEquals(multiKey3, multiKey1);
    }
    
    @Test
    public void testHashCode() {
        Integer key1 = 1;
        String key2 = "two";
        
        MultiKey<Object> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<Object> multiKey2 = new MultiKey<>(key1, key2);
        
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }
    
    @Test
    public void testToString() {
        Integer key1 = 1;
        String key2 = "two";
        Double key3 = 3.0;
        
        MultiKey<Object> multiKey = new MultiKey<>(key1, key2, key3);
        
        String result = multiKey.toString();
        
        assertTrue(result.contains("MultiKey"));
        assertTrue(result.contains(key1.toString()));
        assertTrue(result.contains(key2));
        assertTrue(result.contains(key3.toString()));
    }
}