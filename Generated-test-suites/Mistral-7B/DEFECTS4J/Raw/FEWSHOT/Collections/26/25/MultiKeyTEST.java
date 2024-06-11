package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testMultiKeyConstructorWithTwoKeys() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] expectedKeys = new K[]{key1, key2};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testMultiKeyConstructorWithThreeKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
        K[] expectedKeys = new K[]{key1, key2, key3};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testMultiKeyConstructorWithFourKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4);
        K[] expectedKeys = new K[]{key1, key2, key3, key4};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testMultiKeyConstructorWithFiveKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        K key5 = "key5";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        K[] expectedKeys = new K[]{key1, key2, key3, key4, key5};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testMultiKeyConstructorWithArrayOfKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(new K[]{key1, key2, key3});
        K[] expectedKeys = new K[]{key1, key2, key3};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testMultiKeyCloneArrayOfKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(new K[]{key1, key2, key3});
        MultiKey<K> clonedMultiKey = new MultiKey<>(multiKey.getKeys().clone());
        K[] expectedKeys = new K[]{key1, key2, key3};
        assertArrayEquals(expectedKeys, clonedMultiKey.getKeys());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullArrayOfKeys() {
        MultiKey<K> multiKey = new MultiKey<>(null);
    }

    @Test
    public void testMultiKeyEquals() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key2, key1);
        MultiKey<K> multiKey3 = new MultiKey<>(key1);
        MultiKey<String> multiKey4 = new MultiKey<>("key1", "key2");

        assertTrue(multiKey1.equals(multiKey1));
        assertTrue(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(multiKey3));
        assertFalse(multiKey1.equals(multiKey4));
        assertFalse(multiKey1.equals(null));
        assertFalse(multiKey1.equals(""));
    }

    @Test
    public void testMultiKeyHashCode() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        int firstHashCode = multiKey.hashCode();
        K key3 = "key3";
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key3);
        int secondHashCode = multiKey2.hashCode();
        assertEquals(firstHashCode, multiKey.hashCode());
        assertNotEquals(firstHashCode, secondHashCode);
    }

    @Test
    public void testMultiKeyToString() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        String expectedString = "MultiKey[key1, key2]";
        assertEquals(expectedString, multiKey.toString());
    }
}