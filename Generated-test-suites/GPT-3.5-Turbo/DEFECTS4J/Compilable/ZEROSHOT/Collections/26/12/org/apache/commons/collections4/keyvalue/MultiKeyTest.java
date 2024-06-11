package org.apache.commons.collections4.keyvalue;
import org.junit.*;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testConstructorTwoKeys() {
        String key1 = "key1";
        String key2 = "key2";
        
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        
        String[] expectedKeys = {key1, key2};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }
    
    @Test
    public void testConstructorThreeKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);
        
        String[] expectedKeys = {key1, key2, key3};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }
    
    @Test
    public void testConstructorFourKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4);
        
        String[] expectedKeys = {key1, key2, key3, key4};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }
    
    @Test
    public void testConstructorFiveKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        String key5 = "key5";
        
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        
        String[] expectedKeys = {key1, key2, key3, key4, key5};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }
    
    @Test
    public void testConstructorArrayKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        
        String[] keys = {key1, key2, key3};
        
        MultiKey<String> multiKey = new MultiKey<>(keys);
        
        assertArrayEquals(keys, multiKey.getKeys());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullKeys() {
        MultiKey<String> multiKey = new MultiKey<>(null);
    }
    
    @Test
    public void testGetKey() {
        String key1 = "key1";
        String key2 = "key2";
        
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyInvalidIndex() {
        String key1 = "key1";
        String key2 = "key2";
        
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        
        multiKey.getKey(2);
    }
    
    @Test
    public void testSize() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);
        
        assertEquals(3, multiKey.size());
    }
    
    @Test
    public void testEquals() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2, key3);
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key2, key3);
        MultiKey<String> multiKey3 = new MultiKey<>(key2, key3, key1);
        
        assertTrue(multiKey1.equals(multiKey2));
        assertTrue(multiKey2.equals(multiKey1));
        assertFalse(multiKey1.equals(multiKey3));
    }
    
    @Test
    public void testHashCode() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2, key3);
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key2, key3);
        
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }
    
    @Test
    public void testToString() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);
        
        assertEquals("MultiKey[key1, key2, key3]", multiKey.toString());
    }
}