package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;

import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testMultiKeyConstructorWithTwoKeys() {
        K key1 = new String("key1");
        K key2 = new String("key2");

        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        String[] expectedKeys = new String[]{"key1", "key2"};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testMultiKeyConstructorWithThreeKeys() {
        K key1 = new String("key1");
        K key2 = new String("key2");
        K key3 = new String("key3");

        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);
        String[] expectedKeys = new String[]{"key1", "key2", "key3"};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testMultiKeyConstructorWithFourKeys() {
        K key1 = new String("key1");
        K key2 = new String("key2");
        K key3 = new String("key3");
        K key4 = new String("key4");

        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4);
        String[] expectedKeys = new String[]{"key1", "key2", "key3", "key4"};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testMultiKeyConstructorWithFiveKeys() {
        K key1 = new String("key1");
        K key2 = new String("key2");
        K key3 = new String("key3");
        K key4 = new String("key4");
        K key5 = new String("key5");

        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        String[] expectedKeys = new String[]{"key1", "key2", "key3", "key4", "key5"};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testMultiKeyConstructorWithArrayOfKeys() {
        K[] keys = new String[]{"key1", "key2"};

        MultiKey<String> multiKey = new MultiKey<>(keys);
        String[] expectedKeys = new String[]{"key1", "key2"};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testMultiKeyConstructorWithArrayOfKeysAndCloneFalse() {
        K[] keys = new String[]{"key1", "key2"};

        MultiKey<String> multiKey = new MultiKey<>(keys, false);
        String[] expectedKeys = new String[]{"key1", "key2"};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testMultiKeySize() {
        K key1 = new String("key1");
        K key2 = new String("key2");
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);

        assertEquals(2, multiKey.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKeysArray() {
        new MultiKey<>(null);
    }

    @Test
    public void testMultiKeyEquals() {
        K key1 = new String("key1");
        K key2 = new String("key2");
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);

        assertEquals(multiKey1, multiKey2);
    }

    @Test
    public void testMultiKeyNotEqualsDifferentObjectType() {
        K key1 = new String("key1");
        K key2 = new String("key2");
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);

        assertNotEquals(multiKey, new Object());
    }

    @Test
    public void testMultiKeyNotEqualsDifferentKeys() {
        K key1 = new String("key1");
        K key2 = new String("key2");
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        K key3 = new String("key3");
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key3);

        assertNotEquals(multiKey1, multiKey2);
    }

    @Test
    public void testMultiKeyHashCode() {
        K key1 = new String("key1");
        K key2 = new String("key2");
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);

        assertEquals(multiKey.hashCode(), multiKey.hashCode());
    }

    @Test
    public void testMultiKeyToString() {
        K key1 = new String("key1");
        K key2 = new String("key2");
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);

        assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }
}