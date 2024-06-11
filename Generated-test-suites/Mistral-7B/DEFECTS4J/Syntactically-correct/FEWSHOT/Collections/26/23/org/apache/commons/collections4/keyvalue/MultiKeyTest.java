package org.apache.commons.collections4.keyvalue;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testMultiKeyConstruction() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        K key5 = "key5";

        // Test construction with two keys
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        assertEquals(2, multiKey1.size());
        assertArrayEquals(new K[] { key1, key2 }, multiKey1.getKeys());

        // Test construction with three keys
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2, key3);
        assertEquals(3, multiKey2.size());
        assertArrayEquals(new K[] { key1, key2, key3 }, multiKey2.getKeys());

        // Test construction with four keys
        MultiKey<K> multiKey3 = new MultiKey<>(key1, key2, key3, key4);
        assertEquals(4, multiKey3.size());
        assertArrayEquals(new K[] { key1, key2, key3, key4 }, multiKey3.getKeys());

        // Test construction with five keys
        MultiKey<K> multiKey4 = new MultiKey<>(key1, key2, key3, key4, key5);
        assertEquals(5, multiKey4.size());
        assertArrayEquals(new K[] { key1, key2, key3, key4, key5 }, multiKey4.getKeys());

        // Test construction with an array of keys
        K[] keys = new K[] { key1, key2, key3, key4, key5 };
        MultiKey<K> multiKey5 = new MultiKey<>(keys);
        assertEquals(5, multiKey5.size());
        assertArrayEquals(keys, multiKey5.getKeys());
    }

    @Test
    public void testMultiKeyGetKeyAndSize() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(2, multiKey.size());
    }

    @Test
    public void testMultiKeyEquals() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3 = new MultiKey<>(key1, "key3");
        MultiKey<String> multiKey4 = new MultiKey<>("key1", "key2");

        assertTrue(multiKey1.equals(multiKey1));
        assertTrue(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(multiKey3));
        assertFalse(multiKey1.equals(multiKey4));

        MultiKey<K> multiKey5 = new MultiKey<>(multiKey1.getKeys());
        assertTrue(multiKey1.equals(multiKey5));
    }

    @Test
    public void testMultiKeyHashCode() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        int hashCode = multiKey.hashCode();
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        int hashCode2 = multiKey2.hashCode();
        assertEquals(hashCode, hashCode2);

        K key3 = "key3";
        MultiKey<K> multiKey3 = new MultiKey<>(key1, key3);
        assertNotEquals(hashCode, multiKey3.hashCode());

        MultiKey<String> multiKey4 = new MultiKey<>("key1", "key2");
        assertNotEquals(hashCode, multiKey4.hashCode());
    }

    @Test
    public void testMultiKeyToString() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }

    @Test
    public void testMultiKeyClone() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        MultiKey<K> clonedMultiKey = new MultiKey<>(multiKey.getKeys());
        assertEquals(multiKey, clonedMultiKey);
        assertNotSame(multiKey, clonedMultiKey);
    }
}