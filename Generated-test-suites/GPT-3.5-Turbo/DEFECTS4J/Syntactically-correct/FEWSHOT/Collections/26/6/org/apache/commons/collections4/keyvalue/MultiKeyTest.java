import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class MultiKeyTest {
    
    // Test constructor taking two keys
    @Test
    public void testConstructorTwoKeys() {
        String key1 = "key1";
        String key2 = "key2";
        
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        
        String[] expectedKeys = {key1, key2};
        
        assertArrayEquals(expectedKeys, multiKey.getKeys());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(2, multiKey.size());
    }
    
    // Test constructor taking three keys
    @Test
    public void testConstructorThreeKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);
        
        String[] expectedKeys = {key1, key2, key3};
        
        assertArrayEquals(expectedKeys, multiKey.getKeys());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(3, multiKey.size());
    }
    
    // Test constructor taking four keys
    @Test
    public void testConstructorFourKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4);
        
        String[] expectedKeys = {key1, key2, key3, key4};
        
        assertArrayEquals(expectedKeys, multiKey.getKeys());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(4, multiKey.size());
    }
    
    // Test constructor taking five keys
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
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(key5, multiKey.getKey(4));
        assertEquals(5, multiKey.size());
    }
    
    // Test constructor taking an array of keys
    @Test
    public void testConstructorKeyArray() {
        String[] keys = {"key1", "key2", "key3"};
        
        MultiKey<String> multiKey = new MultiKey<>(keys);
        
        assertArrayEquals(keys, multiKey.getKeys());
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
        assertEquals(3, multiKey.size());
    }
    
    // Test constructor taking an array of keys with cloning off
    @Test
    public void testConstructorKeyArrayNoClone() {
        String[] keys = {"key1", "key2", "key3"};
        
        MultiKey<String> multiKey = new MultiKey<>(keys, false);
        
        assertArrayEquals(keys, multiKey.getKeys());
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
        assertEquals(3, multiKey.size());
    }
    
    // Test constructor taking null as key array
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullKeyArray() {
        String[] keys = null;
        
        new MultiKey<>(keys);
    }
    
    // Test equals method
    @Test
    public void testEquals() {
        String[] keys1 = {"key1", "key2"};
        String[] keys2 = {"key1", "key2"};
        String[] keys3 = {"key3", "key4"};
        
        MultiKey<String> multiKey1 = new MultiKey<>(keys1);
        MultiKey<String> multiKey2 = new MultiKey<>(keys2);
        MultiKey<String> multiKey3 = new MultiKey<>(keys3);
        
        assertEquals(multiKey1, multiKey2);
        assertNotEquals(multiKey1, multiKey3);
    }
    
    // Test hashCode method
    @Test
    public void testHashCode() {
        String[] keys = {"key1", "key2"};
        
        MultiKey<String> multiKey1 = new MultiKey<>(keys);
        MultiKey<String> multiKey2 = new MultiKey<>(keys);
        
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }
    
    // Test toString method
    @Test
    public void testToString() {
        String[] keys = {"key1", "key2"};
        
        MultiKey<String> multiKey = new MultiKey<>(keys);
        
        assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }
}