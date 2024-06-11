package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;

import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testMultiKeyConstruction() {
        // Test construction with two keys
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        K[] keys1 = multiKey1.getKeys();
        assertEquals(2, keys1.length);
        assertEquals(key1, keys1[0]);
        assertEquals(key2, keys1[1]);

        // Test construction with three keys
        K key3 = "key3";
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2, key3);
        K[] keys2 = multiKey2.getKeys();
        assertEquals(3, keys2.length);
        assertEquals(key1, keys2[0]);
        assertEquals(key2, keys2[1]);
        assertEquals(key3, keys2[2]);

        // Test construction with four keys
        K key4 = "key4";
        MultiKey<K> multiKey3 = new MultiKey<>(key1, key2, key3, key4);
        K[] keys3 = multiKey3.getKeys();
        assertEquals(4, keys3.length);
        assertEquals(key1, keys3[0]);
        assertEquals(key2, keys3[1]);
        assertEquals(key3, keys3[2]);
        assertEquals(key4, keys3[3]);

        // Test construction with five keys
        K key5 = "key5";
        MultiKey<K> multiKey4 = new MultiKey<>(key1, key2, key3, key4, key5);
        K[] keys4 = multiKey4.getKeys();
        assertEquals(5, keys4.length);
        assertEquals(key1, keys4[0]);
        assertEquals(key2, keys4[1]);
        assertEquals(key3, keys4[2]);
        assertEquals(key4, keys4[3]);
        assertEquals(key5, keys4[4]);

        // Test construction with an array of keys
        K[] keys5 = new K[] {key1, key2, key3, key4, key5};
        MultiKey<K> multiKey5 = new MultiKey<>(keys5, false);
        K[] keys5Cloned = multiKey5.getKeys();
        assertArrayEquals(keys5, keys5Cloned);
    }

    @Test
    public void testMultiKeyClone() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        K[] keys1 = multiKey1.getKeys();

        MultiKey<K> multiKey2 = multiKey1.clone();
        K[] keys2 = multiKey2.getKeys();
        assertEquals(keys1, keys2);
    }

    @Test
    public void testMultiKeyEquals() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3 = new MultiKey<>(key1, "key2");
        MultiKey<K> multiKey4 = new MultiKey<>(key2, key1);

        assertEquals(multiKey1, multiKey1);
        assertEquals(multiKey1, multiKey2);
        assertNotEquals(multiKey1, multiKey3);
        assertNotEquals(multiKey1, multiKey4);
        assertNotEquals(multiKey1, new Object());
    }

    @Test
    public void testMultiKeyHashCode() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);

        int hashCode1 = multiKey1.hashCode();
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        int hashCode2 = multiKey2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testMultiKeyToString() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }

    @Test
    public void testMultiKeySize() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        assertEquals(2, multiKey.size());
    }

    @Test
    public void testMultiKeyGetKey() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiKeyGetKeyOutOfBounds() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        multiKey.getKey(2);
    }

    @Test
    public void testMultiKeyCloneArray() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(new K[] {key1, key2});

        MultiKey<K> multiKeyCloned = new MultiKey<>(multiKey.getKeys(), false);

        K[] keysCloned = multiKeyCloned.getKeys();
        K[] keysOriginal = multiKey.getKeys();

        assertArrayEquals(keysOriginal, keysCloned);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKeys() {
        new MultiKey<>(null);
    }
}