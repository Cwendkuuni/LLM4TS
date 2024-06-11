package org.apache.commons.collections4.keyvalue;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    // Test constructor taking two keys
    @Test
    public void testConstructorTwoKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        assertEquals(2, multiKey.size());
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }

    // Test constructor taking three keys
    @Test
    public void testConstructorThreeKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        assertEquals(3, multiKey.size());
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
        assertEquals("MultiKey[key1, key2, key3]", multiKey.toString());
    }

    // Test constructor taking four keys
    @Test
    public void testConstructorFourKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4");
        assertEquals(4, multiKey.size());
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
        assertEquals("key4", multiKey.getKey(3));
        assertEquals("MultiKey[key1, key2, key3, key4]", multiKey.toString());
    }

    // Test constructor taking five keys
    @Test
    public void testConstructorFiveKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4", "key5");
        assertEquals(5, multiKey.size());
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
        assertEquals("key4", multiKey.getKey(3));
        assertEquals("key5", multiKey.getKey(4));
        assertEquals("MultiKey[key1, key2, key3, key4, key5]", multiKey.toString());
    }

    // Test constructor taking an array of keys
    @Test
    public void testConstructorWithArray() {
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        assertEquals(3, multiKey.size());
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
        assertEquals("MultiKey[key1, key2, key3]", multiKey.toString());
    }

    // Test constructor taking an array of keys (without cloning)
    @Test
    public void testConstructorWithArrayNoClone() {
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys, false);
        assertEquals(3, multiKey.size());
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
        assertEquals("MultiKey[key1, key2, key3]", multiKey.toString());
    }

    // Test getKeys method
    @Test
    public void testGetKeys() {
        String[] keys = {"key1", "key2"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        assertArrayEquals(keys, multiKey.getKeys());
    }

    // Test getKey method
    @Test
    public void testGetKey() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
    }

    // Test size method
    @Test
    public void testSize() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        assertEquals(3, multiKey.size());
    }

    // Test equals method
    @Test
    public void testEquals() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey3 = new MultiKey<>("key2", "key1");
        assertTrue(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(multiKey3));
    }

    // Test hashCode method
    @Test
    public void testHashCode() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    // Test toString method
    @Test
    public void testToString() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }
}

