package org.apache.commons.collections4.keyvalue;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;

public class MultiKeyTest {
    
    @Test
    public void testConstructorWithTwoKeys() {
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        
        assertEquals(2, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
    }
    
    @Test
    public void testConstructorWithThreeKeys() {
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
    public void testConstructorWithFourKeys() {
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
    public void testConstructorWithFiveKeys() {
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
    public void testConstructorWithArray() {
        String[] keysArray = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keysArray);
        
        assertEquals(3, multiKey.size());
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConstructorWithNullArray() {
        String[] keysArray = null;
        MultiKey<String> multiKey = new MultiKey<>(keysArray);
    }
    
    @Test
    public void testGetKeys() {
        String[] keysArray = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keysArray);
        
        String[] result = multiKey.getKeys();
        
        assertArrayEquals(keysArray, result);
    }
    
    @Test
    public void testEquals() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
        
        assertTrue(multiKey1.equals(multiKey2));
    }
    
    @Test
    public void testHashCode() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
        
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }
    
    @Test
    public void testToString() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        
        assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }
    
    @Test
    public void testGetKey() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void testGetKeyOutOfBounds() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        
        multiKey.getKey(4);
    }
}