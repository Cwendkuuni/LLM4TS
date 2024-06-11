package org.apache.commons.collections4.keyvalue;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    // Test Constructor

    @Test
    public void testConstructor_twoKeys() {
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        String[] expectedKeys = {key1, key2};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testConstructor_threeKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);
        String[] expectedKeys = {key1, key2, key3};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testConstructor_fourKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4);
        String[] expectedKeys = {key1, key2, key3, key4};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testConstructor_fiveKeys() {
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
    public void testConstructor_arrayKeys() {
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        assertArrayEquals(keys, multiKey.getKeys());
    }

    // Test getKeys

    @Test
    public void testGetKeys() {
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        assertArrayEquals(keys, multiKey.getKeys());
    }

    // Test getKey

    @Test
    public void testGetKey() {
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
    }
    
    // Test size

    @Test
    public void testSize() {
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        assertEquals(3, multiKey.size());
    }

    // Test equals

    @Test
    public void testEquals_sameKeys() {
        String[] keys1 = {"key1", "key2", "key3"};
        String[] keys2 = {"key1", "key2", "key3"};
        MultiKey<String> multiKey1 = new MultiKey<>(keys1);
        MultiKey<String> multiKey2 = new MultiKey<>(keys2);
        assertTrue(multiKey1.equals(multiKey2));
    }

    @Test
    public void testEquals_differentKeys() {
        String[] keys1 = {"key1", "key2", "key3"};
        String[] keys2 = {"key4", "key5", "key6"};
        MultiKey<String> multiKey1 = new MultiKey<>(keys1);
        MultiKey<String> multiKey2 = new MultiKey<>(keys2);
        assertFalse(multiKey1.equals(multiKey2));
    }

    @Test
    public void testEquals_differentObjects() {
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        assertFalse(multiKey.equals(keys));
    }

    // Test hashCode

    @Test
    public void testHashCode_sameKeys() {
        String[] keys1 = {"key1", "key2", "key3"};
        String[] keys2 = {"key1", "key2", "key3"};
        MultiKey<String> multiKey1 = new MultiKey<>(keys1);
        MultiKey<String> multiKey2 = new MultiKey<>(keys2);
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    @Test
    public void testHashCode_differentKeys() {
        String[] keys1 = {"key1", "key2", "key3"};
        String[] keys2 = {"key4", "key5", "key6"};
        MultiKey<String> multiKey1 = new MultiKey<>(keys1);
        MultiKey<String> multiKey2 = new MultiKey<>(keys2);
        assertNotEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    // Test toString

    @Test
    public void testToString() {
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        assertEquals("MultiKey[key1, key2, key3]", multiKey.toString());
    }

}