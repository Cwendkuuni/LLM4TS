package org.apache.commons.collections4.keyvalue;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {
    
    private MultiKey<String> multiKey;
    
    @Before
    public void setup() {
        multiKey = new MultiKey<>("key1", "key2", "key3");
    }
    
    @Test
    public void testConstructorWithTwoKeys() {
        MultiKey<String> key = new MultiKey<>("key1", "key2");
        assertEquals(2, key.size());
        assertEquals("key1", key.getKey(0));
        assertEquals("key2", key.getKey(1));
    }
    
    @Test
    public void testConstructorWithThreeKeys() {
        MultiKey<String> key = new MultiKey<>("key1", "key2", "key3");
        assertEquals(3, key.size());
        assertEquals("key1", key.getKey(0));
        assertEquals("key2", key.getKey(1));
        assertEquals("key3", key.getKey(2));
    }
    
    @Test
    public void testConstructorWithFourKeys() {
        MultiKey<String> key = new MultiKey<>("key1", "key2", "key3", "key4");
        assertEquals(4, key.size());
        assertEquals("key1", key.getKey(0));
        assertEquals("key2", key.getKey(1));
        assertEquals("key3", key.getKey(2));
        assertEquals("key4", key.getKey(3));
    }
    
    @Test
    public void testConstructorWithFiveKeys() {
        MultiKey<String> key = new MultiKey<>("key1", "key2", "key3", "key4", "key5");
        assertEquals(5, key.size());
        assertEquals("key1", key.getKey(0));
        assertEquals("key2", key.getKey(1));
        assertEquals("key3", key.getKey(2));
        assertEquals("key4", key.getKey(3));
        assertEquals("key5", key.getKey(4));
    }
    
    @Test
    public void testConstructorWithArray1() {
        String[] keys = {"key1", "key2"};
        MultiKey<String> key = new MultiKey<>(keys);
        assertEquals(2, key.size());
        assertEquals("key1", key.getKey(0));
        assertEquals("key2", key.getKey(1));
    }
    
    @Test
    public void testConstructorWithArray2() {
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> key = new MultiKey<>(keys);
        assertEquals(3, key.size());
        assertEquals("key1", key.getKey(0));
        assertEquals("key2", key.getKey(1));
        assertEquals("key3", key.getKey(2));
    }
    
    @Test
    public void testEqualsSameInstance() {
        assertEquals(multiKey, multiKey);
    }
    
    @Test
    public void testEqualsSameKeys() {
        MultiKey<String> key = new MultiKey<>("key1", "key2", "key3");
        assertEquals(multiKey, key);
    }
    
    @Test
    public void testEqualsDifferentKeys() {
        MultiKey<String> key = new MultiKey<>("key1", "key4", "key3");
        assertNotEquals(multiKey, key);
    }
    
    @Test
    public void testEqualsDifferentObject() {
        Object key = new Object();
        assertNotEquals(multiKey, key);
    }
    
    @Test
    public void testHashCode() {
        MultiKey<String> key = new MultiKey<>("key1", "key2", "key3");
        assertEquals(key.hashCode(), multiKey.hashCode());
    }
    
    @Test
    public void testToString() {
        assertEquals("MultiKey[key1, key2, key3]", multiKey.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullKeys() {
        String[] keys = null;
        MultiKey<String> key = new MultiKey<>(keys);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyInvalidIndex() {
        multiKey.getKey(3);
    }
    
    @Test
    public void testGetKeys() {
        String[] keys = multiKey.getKeys();
        assertEquals(3, keys.length);
        assertEquals("key1", keys[0]);
        assertEquals("key2", keys[1]);
        assertEquals("key3", keys[2]);
    }
}