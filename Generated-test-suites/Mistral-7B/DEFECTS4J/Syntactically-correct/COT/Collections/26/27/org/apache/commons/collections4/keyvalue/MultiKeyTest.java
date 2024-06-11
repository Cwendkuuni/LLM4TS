package org.apache.commons.collections4.keyvalue;

import org.apache.commons.collections4.MultiKey;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class MultiKeyTest {

    @Test
    public void testMultiKeyConstructionWithTwoKeys() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] expectedKeys = new K[]{key1, key2};
        K[] actualKeys = multiKey.getKeys();
        Assert.assertArrayEquals(expectedKeys, actualKeys);
    }

    @Test
    public void testMultiKeyConstructionWithThreeKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
        K[] expectedKeys = new K[]{key1, key2, key3};
        K[] actualKeys = multiKey.getKeys();
        Assert.assertArrayEquals(expectedKeys, actualKeys);
    }

    @Test
    public void testMultiKeyConstructionWithFourKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4);
        K[] expectedKeys = new K[]{key1, key2, key3, key4};
        K[] actualKeys = multiKey.getKeys();
        Assert.assertArrayEquals(expectedKeys, actualKeys);
    }

    @Test
    public void testMultiKeyConstructionWithFiveKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        K key5 = "key5";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        K[] expectedKeys = new K[]{key1, key2, key3, key4, key5};
        K[] actualKeys = multiKey.getKeys();
        Assert.assertArrayEquals(expectedKeys, actualKeys);
    }

    @Test
    public void testMultiKeyConstructionWithArrayOfKeys() {
        K[] keys = new K[]{"key1", "key2", "key3"};
        MultiKey<K> multiKey = new MultiKey<>(keys);
        K[] expectedKeys = keys.clone();
        K[] actualKeys = multiKey.getKeys();
        Assert.assertArrayEquals(expectedKeys, actualKeys);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyConstructionWithNullArrayOfKeys() {
        MultiKey<K> multiKey = new MultiKey<>(null);
    }

    @Test
    public void testMultiKeyGetKey() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K key1Expected = "key1";
        K key1Actual = multiKey.getKey(0);
        Assert.assertEquals(key1Expected, key1Actual);
        K key2Expected = "key2";
        K key2Actual = multiKey.getKey(1);
        Assert.assertEquals(key2Expected, key2Actual);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiKeyGetKeyWithInvalidIndex() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        multiKey.getKey(2);
    }

    @Test
    public void testMultiKeySize() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        int expectedSize = 2;
        int actualSize = multiKey.size();
        Assert.assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testMultiKeyEquals() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey3 = new MultiKey<>("key1", "key2");
        Object other = new Object();
        Assert.assertTrue(multiKey1.equals(multiKey2));
        Assert.assertFalse(multiKey1.equals(other));
        Assert.assertFalse(multiKey1.equals(null));
    }

    @Test
    public void testMultiKeyHashCode() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        int expectedHashCode = 32 * (1 + 1) ^ key1.hashCode() ^ key2.hashCode();
        int actualHashCode = multiKey.hashCode();
        Assert.assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testMultiKeyToString() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        String expectedToString = "MultiKey[key1, key2]";
        String actualToString = multiKey.toString();
        Assert.assertEquals(expectedToString, actualToString);
    }
}