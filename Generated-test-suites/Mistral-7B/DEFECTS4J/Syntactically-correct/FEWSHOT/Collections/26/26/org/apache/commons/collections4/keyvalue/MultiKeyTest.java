package org.apache.commons.collections4.keyvalue;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testMultiKeyConstructorTwoKeys() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] keys = multiKey.getKeys();
        assertEquals(2, keys.length);
        assertEquals(key1, keys[0]);
        assertEquals(key2, keys[1]);
    }

    @Test
    public void testMultiKeyConstructorThreeKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
        K[] keys = multiKey.getKeys();
        assertEquals(3, keys.length);
        assertEquals(key1, keys[0]);
        assertEquals(key2, keys[1]);
        assertEquals(key3, keys[2]);
    }

    @Test
    public void testMultiKeyConstructorFourKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4);
        K[] keys = multiKey.getKeys();
        assertEquals(4, keys.length);
        assertEquals(key1, keys[0]);
        assertEquals(key2, keys[1]);
        assertEquals(key3, keys[2]);
        assertEquals(key4, keys[3]);
    }

    @Test
    public void testMultiKeyConstructorFiveKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        K key5 = "key5";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        K[] keys = multiKey.getKeys();
        assertEquals(5, keys.length);
        assertEquals(key1, keys[0]);
        assertEquals(key2, keys[1]);
        assertEquals(key3, keys[2]);
        assertEquals(key4, keys[3]);
        assertEquals(key5, keys[4]);
    }

    @Test
    public void testMultiKeyConstructorArrayOfKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K[] keys = new K[] { key1, key2, key3 };
        MultiKey<K> multiKey = new MultiKey<>(keys);
        K[] keysFromMultiKey = multiKey.getKeys();
        assertArrayEquals(keys, keysFromMultiKey);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKeysArray() {
        MultiKey<String> multiKey = new MultiKey<>(null);
    }

    @Test
    public void testGetKey() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyInvalidIndex() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        multiKey.getKey(2);
    }

    @Test
    public void testSize() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        assertEquals(2, multiKey.size());
    }

    @Test
    public void testEquals() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3 = new MultiKey<>(key1, key3);
        assertEquals(multiKey1, multiKey1);
        assertEquals(multiKey1, multiKey2);
        assertNotEquals(multiKey1, multiKey3);
    }

    @Test
    public void testHashCode() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        assertEquals(multiKey.hashCode(), multiKey.hashCode());
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        assertEquals(multiKey.hashCode(), multiKey2.hashCode());
        K key3 = "key3";
        MultiKey<K> multiKey3 = new MultiKey<>(key1, key3);
        assertNotEquals(multiKey.hashCode(), multiKey3.hashCode());
    }

    @Test
    public void testToString() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        assertEquals("MultiKey[keys=[key1, key2]]", multiKey.toString());
    }

    @Test
    public void testClone() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] keys = multiKey.getKeys().clone();
        MultiKey<K> clonedMultiKey = new MultiKey<>(keys);
        assertArrayEquals(multiKey.getKeys(), clonedMultiKey.getKeys());
    }
}