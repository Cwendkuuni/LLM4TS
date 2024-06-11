package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testMultiKeyConstructorWithTwoKeys() {
        K key1 = (K) "key1";
        K key2 = (K) "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] expectedKeys = (K[]) new Object[]{key1, key2};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testMultiKeyConstructorWithThreeKeys() {
        K key1 = (K) "key1";
        K key2 = (K) "key2";
        K key3 = (K) "key3";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
        K[] expectedKeys = (K[]) new Object[]{key1, key2, key3};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testMultiKeyConstructorWithFourKeys() {
        K key1 = (K) "key1";
        K key2 = (K) "key2";
        K key3 = (K) "key3";
        K key4 = (K) "key4";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4);
        K[] expectedKeys = (K[]) new Object[]{key1, key2, key3, key4};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testMultiKeyConstructorWithFiveKeys() {
        K key1 = (K) "key1";
        K key2 = (K) "key2";
        K key3 = (K) "key3";
        K key4 = (K) "key4";
        K key5 = (K) "key5";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        K[] expectedKeys = (K[]) new Object[]{key1, key2, key3, key4, key5};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testMultiKeyConstructorWithArrayOfKeys() {
        K key1 = (K) "key1";
        K key2 = (K) "key2";
        K key3 = (K) "key3";
        K[] keys = new K[]{key1, key2, key3};
        MultiKey<K> multiKey = new MultiKey<>(keys);
        K[] expectedKeys = keys.clone();
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testMultiKeyEquals() {
        K key1 = (K) "key1";
        K key2 = (K) "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3 = new MultiKey<>(key2, key1);
        assertTrue(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(multiKey3));
        assertFalse(multiKey1.equals(new Object()));
    }

    @Test
    public void testMultiKeyHashCode() {
        K key1 = (K) "key1";
        K key2 = (K) "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        assertEquals(multiKey.hashCode(), multiKey2.hashCode());
    }

    @Test
    public void testMultiKeyToString() {
        K key1 = (K) "key1";
        K key2 = (K) "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        String expectedString = "MultiKey[key1, key2]";
        assertEquals(expectedString, multiKey.toString());
    }
}