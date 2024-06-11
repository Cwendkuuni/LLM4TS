package org.apache.commons.collections4.keyvalue;

import static org.junit.Assert.*;

import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testMultiKeyWithTwoKeys() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        assertArrayEquals(new K[] {key1, key2}, multiKey.getKeys());
    }

    @Test
    public void testMultiKeyWithThreeKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
        assertArrayEquals(new K[] {key1, key2, key3}, multiKey.getKeys());
    }

    @Test
    public void testMultiKeyWithFourKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4);
        assertArrayEquals(new K[] {key1, key2, key3, key4}, multiKey.getKeys());
    }

    @Test
    public void testMultiKeyWithFiveKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        K key5 = "key5";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        assertArrayEquals(new K[] {key1, key2, key3, key4, key5}, multiKey.getKeys());
    }

    @Test
    public void testMultiKeyWithArrayOfKeys() {
        K[] keys = new K[] {"key1", "key2", "key3"};
        MultiKey<K> multiKey = new MultiKey<>(keys);
        assertArrayEquals(keys, multiKey.getKeys());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyWithNullArrayOfKeys() {
        MultiKey<K> multiKey = new MultiKey<>(null);
    }

    @Test
    public void testMultiKeyWithClonedArrayOfKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(new K[] {key1, key2, key3}.clone(), false);
        assertArrayEquals(new K[] {key1, key2, key3}, multiKey.getKeys());
    }

    @Test
    public void testMultiKeyWithSize() {
        K[] keys = new K[] {"key1", "key2", "key3"};
        MultiKey<K> multiKey = new MultiKey<>(keys, false);
        assertEquals(3, multiKey.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiKeyWithInvalidIndex() {
        K[] keys = new K[] {"key1", "key2", "key3"};
        MultiKey<K> multiKey = new MultiKey<>(keys, false);
        multiKey.getKey(-1);
    }

    @Test
    public void testMultiKeyWithEquals() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3 = new MultiKey<>(key1, "key2");
        assertEquals(multiKey1, multiKey2);
        assertNotEquals(multiKey1, multiKey3);
    }

    @Test
    public void testMultiKeyWithHashCode() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        assertEquals(key1.hashCode() ^ key2.hashCode(), multiKey.hashCode());
    }

    @Test
    public void testMultiKeyWithToString() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }
}