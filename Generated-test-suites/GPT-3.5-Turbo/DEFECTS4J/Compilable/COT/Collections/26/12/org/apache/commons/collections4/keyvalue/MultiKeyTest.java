package org.apache.commons.collections4.keyvalue;

import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    // Method: MultiKey(K, K)
    // Test case: Create a MultiKey with two keys, test getKeys and getKey methods
    @Test
    public void testMultiKeyTwoKeys() {
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        String[] keys = multiKey.getKeys();
        assertEquals(key1, keys[0]);
        assertEquals(key2, keys[1]);
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
    }

    // Method: MultiKey(K, K, K)
    // Test case: Create a MultiKey with three keys, test getKeys and getKey methods
    @Test
    public void testMultiKeyThreeKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);
        String[] keys = multiKey.getKeys();
        assertEquals(key1, keys[0]);
        assertEquals(key2, keys[1]);
        assertEquals(key3, keys[2]);
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
    }

    // Method: MultiKey(K, K, K, K)
    // Test case: Create a MultiKey with four keys, test getKeys and getKey methods
    @Test
    public void testMultiKeyFourKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4);
        String[] keys = multiKey.getKeys();
        assertEquals(key1, keys[0]);
        assertEquals(key2, keys[1]);
        assertEquals(key3, keys[2]);
        assertEquals(key4, keys[3]);
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
    }

    // Method: MultiKey(K, K, K, K, K)
    // Test case: Create a MultiKey with five keys, test getKeys and getKey methods
    @Test
    public void testMultiKeyFiveKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        String key5 = "key5";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        String[] keys = multiKey.getKeys();
        assertEquals(key1, keys[0]);
        assertEquals(key2, keys[1]);
        assertEquals(key3, keys[2]);
        assertEquals(key4, keys[3]);
        assertEquals(key5, keys[4]);
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(key5, multiKey.getKey(4));
    }
}
