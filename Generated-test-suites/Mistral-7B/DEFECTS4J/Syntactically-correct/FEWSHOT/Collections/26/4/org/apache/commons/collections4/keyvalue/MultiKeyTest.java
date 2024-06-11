package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import org.apache.commons.collections4.keyvalue.MultiKey;

public class MultiKeyTest {

    @Test
    public void testMultiKeyConstruction() {
        // test two keys
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        K[] keys1 = multiKey1.getKeys();
        assertArrayEquals(new Object[] { key1, key2 }, keys1);

        // test three keys
        K key3 = "key3";
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2, key3);
        K[] keys2 = multiKey2.getKeys();
        assertArrayEquals(new Object[] { key1, key2, key3 }, keys2);

        // test four keys
        K key4 = "key4";
        MultiKey<K> multiKey3 = new MultiKey<>(key1, key2, key3, key4);
        K[] keys3 = multiKey3.getKeys();
        assertArrayEquals(new Object[] { key1, key2, key3, key4 }, keys3);

        // test five keys
        K key5 = "key5";
        MultiKey<K> multiKey4 = new MultiKey<>(key1, key2, key3, key4, key5);
        K[] keys4 = multiKey4.getKeys();
        assertArrayEquals(new Object[] { key1, key2, key3, key4, key5 }, keys4);

        // test array of keys
        K[] keys5 = new K[] { key1, key2, key3, key4, key5 };
        MultiKey<K> multiKey5 = new MultiKey<>(keys5);
        K[] keys5Clone = multiKey5.getKeys();
        assertArrayEquals(keys5, keys5Clone);
    }

    @Test
    public void testGetKey() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
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
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3 = new MultiKey<>(key1, key3); // key3 is different
        assertNotNull(multiKey1);
        assertNotNull(multiKey2);
        assertNotNull(multiKey3);
        assertEquals(multiKey1, multiKey1);
        assertEquals(multiKey1, multiKey2);
        assertNotEquals(multiKey1, multiKey3);
    }

    @Test
    public void testHashCode() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        int hashCode1 = multiKey.hashCode();
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        int hashCode2 = multiKey2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToString() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }
}