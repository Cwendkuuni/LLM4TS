package org.apache.commons.collections4.keyvalue;

import org.junit.Test;

import static org.junit.Assert.*;

public class MultiKeyTest {
    // Test the constructor with two keys
    @Test
    public void testConstructorWithTwoKeys() {
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);

        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(2, multiKey.size());
    }

    // Test the constructor with three keys
    @Test
    public void testConstructorWithThreeKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);

        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(3, multiKey.size());
    }

    // Test the constructor with four keys
    @Test
    public void testConstructorWithFourKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4);

        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(4, multiKey.size());
    }

    // Test the constructor with five keys
    @Test
    public void testConstructorWithFiveKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        String key5 = "key5";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);

        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(key5, multiKey.getKey(4));
        assertEquals(5, multiKey.size());
    }

    // Test the constructor with an array of keys
    @Test
    public void testConstructorWithArrayKeys() {
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);

        assertArrayEquals(keys, multiKey.getKeys());
        assertEquals(keys[0], multiKey.getKey(0));
        assertEquals(keys[1], multiKey.getKey(1));
        assertEquals(keys[2], multiKey.getKey(2));
        assertEquals(3, multiKey.size());
    }

    // Test the equals() method
    @Test
    public void testEquals() {
        String[] keys1 = {"key1", "key2"};
        String[] keys2 = {"key1", "key2"};
        String[] keys3 = {"key1"};

        MultiKey<String> multiKey1 = new MultiKey<>(keys1);
        MultiKey<String> multiKey2 = new MultiKey<>(keys2);
        MultiKey<String> multiKey3 = new MultiKey<>(keys3);

        assertTrue(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(multiKey3));
        assertFalse(multiKey1.equals(null));
        assertFalse(multiKey1.equals(new Object()));
    }

    // Test the hashCode() method
    @Test
    public void testHashCode() {
        String[] keys1 = {"key1", "key2"};
        String[] keys2 = {"key1", "key2"};

        MultiKey<String> multiKey1 = new MultiKey<>(keys1);
        MultiKey<String> multiKey2 = new MultiKey<>(keys2);

        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    // Test the toString() method
    @Test
    public void testToString() {
        String[] keys = {"key1", "key2"};

        MultiKey<String> multiKey = new MultiKey<>(keys);

        assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }
}
