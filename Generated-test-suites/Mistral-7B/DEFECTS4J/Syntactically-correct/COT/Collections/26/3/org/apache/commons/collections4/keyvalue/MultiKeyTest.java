package org.apache.commons.collections4.keyvalue;

import org.apache.commons.collections4.MultiKey;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MultiKeyTest {

    // Basic test cases for each constructor

    @Test
    public void testMultiKeyWithTwoKeys() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] keys = multiKey.getKeys();
        assertEquals(2, keys.length);
        assertEquals(key1, keys[0]);
        assertEquals(key2, keys[1]);
    }

    @Test
    public void testMultiKeyWithThreeKeys() {
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
    public void testMultiKeyWithFourKeys() {
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
    public void testMultiKeyWithArrayOfKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K[] keys = new K[]{key1, key2, key3};
        MultiKey<K> multiKey = new MultiKey<>(keys);
        K[] checkKeys = multiKey.getKeys();
        assertEquals(3, checkKeys.length);
        assertEquals(key1, checkKeys[0]);
        assertEquals(key2, checkKeys[1]);
        assertEquals(key3, checkKeys[2]);
    }

    // Edge cases and exception handling test cases

    @Test(expected = IllegalArgumentException.class)
    public void testNullArrayOfKeys() {
        MultiKey<K> multiKey = new MultiKey<>(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyArrayOfKeys() {
        MultiKey<K> multiKey = new MultiKey<>(new K[0]);
    }

    @Test
    public void testMultiKeyWithDuplicateKeys() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key1);
        K[] keys = multiKey.getKeys();
        assertEquals(3, keys.length);
        assertEquals(key1, keys[0]);
        assertEquals(key1, keys[1]);
        assertEquals(key2, keys[2]);
    }

    @Test
    public void testMultiKeyWithImmutableKeys() {
        String immutableKey1 = new StringBuilder("key1").toString();
        String immutableKey2 = new StringBuilder("key2").toString();
        MultiKey<String> multiKey = new MultiKey<>(immutableKey1, immutableKey2);
        assertNotNull(multiKey);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChangeKeysAfterMultiKeyCreated() {
        K[] keys = new K[]{new StringBuilder("key1").toString(), new StringBuilder("key2").toString()};
        MultiKey<StringBuilder> multiKey = new MultiKey<>(keys);
        StringBuilder key1 = new StringBuilder("modifiedKey1");
        multiKey.getKey(0).append("modified");
    }

    
    // Add more test cases here as needed
    
}