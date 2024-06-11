package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testMultiKeyCreation() {
        // Test creation with two keys
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);

        // Test creation with three keys
        K key3 = "key3";
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2, key3);

        // Test creation with four keys
        K key4 = "key4";
        MultiKey<K> multiKey3 = new MultiKey<>(key1, key2, key3, key4);

        // Test creation with five keys
        K key5 = "key5";
        MultiKey<K> multiKey4 = new MultiKey<>(key1, key2, key3, key4, key5);

        // Test creation with an array of keys
        K[] keys = new K[]{key1, key2, key3, key4, key5};
        MultiKey<K> multiKey5 = new MultiKey<>(keys);
    }

    @Test
    public void testGetKeys() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        K[] expectedKeys = new K[]{key1, key2};
        K[] actualKeys = multiKey.getKeys();

        assertArrayEquals(expectedKeys, actualKeys);
    }

    @Test
    public void testGetKey() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        K expectedKey = key1;
        K actualKey = multiKey.getKey(0);

        assertEquals(expectedKey, actualKey);

        expectedKey = key2;
        actualKey = multiKey.getKey(1);

        assertEquals(expectedKey, actualKey);

        try {
            multiKey.getKey(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testSize() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        int expectedSize = 2;
        int actualSize = multiKey.size();

        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testEquals() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";

        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3 = new MultiKey<>(key1, key3);

        assertTrue(multiKey1.equals(multiKey1));
        assertTrue(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(multiKey3));
    }

    @Test
    public void testHashCode() {
        K key1 = "key1";
        K key2 = "key2";

        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        int expectedHashCode = 32 * key1.hashCode() ^ key2.hashCode();
        int actualHashCode = multiKey.hashCode();

        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testToString() {
        K key1 = "key1";
        K key2 = "key2";

        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        String expectedString = "MultiKey[key1, key2]";
        String actualString = multiKey.toString();

        assertEquals(expectedString, actualString);
    }
}