package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;

import org.apache.commons.collections4.keyvalue.MultiKey;
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
        MultiKey<K> multiKey2Keys = new MultiKey<>(key1, key2);
        assertEquals(2, multiKey2Keys.size());
        assertArrayEquals(new K[] { key1, key2 }, multiKey2Keys.getKeys());

        // Test construction with three keys
        MultiKey<K> multiKey3Keys = new MultiKey<>(key1, key2, key3);
        assertEquals(3, multiKey3Keys.size());
        assertArrayEquals(new K[] { key1, key2, key3 }, multiKey3Keys.getKeys());

        // Test construction with four keys
        MultiKey<K> multiKey4Keys = new MultiKey<>(key1, key2, key3, key4);
        assertEquals(4, multiKey4Keys.size());
        assertArrayEquals(new K[] { key1, key2, key3, key4 }, multiKey4Keys.getKeys());

        // Test construction with five keys
        MultiKey<K> multiKey5Keys = new MultiKey<>(key1, key2, key3, key4, key5);
        assertEquals(5, multiKey5Keys.size());
        assertArrayEquals(new K[] { key1, key2, key3, key4, key5 }, multiKey5Keys.getKeys());

        // Test construction with an array of keys
        K[] keysArray = new K[] { key1, key2, key3, key4, key5 };
        MultiKey<K> multiKeyArrayKeys = new MultiKey<>(keysArray);
        assertEquals(5, multiKeyArrayKeys.size());
        assertArrayEquals(keysArray, multiKeyArrayKeys.getKeys());
    }

    @Test
    public void testMultiKeyGetters() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        K[] keys = multiKey.getKeys();
        assertEquals(2, keys.length);
        assertArrayEquals(new K[] { key1, key2 }, keys);

        K key = multiKey.getKey(0);
        assertEquals(key1, key);
        key = multiKey.getKey(1);
        assertEquals(key2, key);
    }

    @Test
    public void testMultiKeyEqualsHashCode() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);

        assertEquals(multiKey1, multiKey2);
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());

        MultiKey<String> multiKey3 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey4 = new MultiKey<>("key1", "key3");

        assertNotSame(multiKey1, multiKey3);
        assertNotSame(multiKey1.hashCode(), multiKey3.hashCode());
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

        K[] clonedKeys = multiKey.getKeys().clone();
        MultiKey<K> clonedMultiKey = new MultiKey<>(clonedKeys);

        assertArrayEquals(multiKey.getKeys(), multiKey.getKeys());
        assertNotSame(multiKey.getKeys(), clonedMultiKey.getKeys());
        assertArrayEquals(multiKey.getKeys(), clonedMultiKey.getKeys());
    }

    @Test
    public void testMultiKeySerialization() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        byte[] serialized = MultiKey.class.getResourceAsStream("/MultiKey-serialized.bin").readAllBytes();
        Object deserialized = MultiKey.class.getConstructor(byte[].class).newInstance(serialized);

        MultiKey<K> deserializedMultiKey = (MultiKey<K>) deserialized;

        assertEquals(multiKey.hashCode(), deserializedMultiKey.hashCode());
        assertArrayEquals(multiKey.getKeys(), deserializedMultiKey.getKeys());
    }
}