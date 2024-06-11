package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testMultiKeyWithTwoKeys() {
        K key1 = "foo";
        K key2 = "bar";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        K[] keys = multiKey.getKeys();
        assertArrayEquals(new K[] { key1, key2 }, keys);
        assertEquals(2, multiKey.size());
    }

    @Test
    public void testMultiKeyWithThreeKeys() {
        K key1 = "foo";
        K key2 = "bar";
        K key3 = "baz";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
        K[] keys = multiKey.getKeys();
        assertArrayEquals(new K[] { key1, key2, key3 }, keys);
        assertEquals(3, multiKey.size());
    }

    @Test
    public void testMultiKeyWithFourKeys() {
        K key1 = "foo";
        K key2 = "bar";
        K key3 = "baz";
        K key4 = "qux";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4);
        K[] keys = multiKey.getKeys();
        assertArrayEquals(new K[] { key1, key2, key3, key4 }, keys);
        assertEquals(4, multiKey.size());
    }

    @Test
    public void testMultiKeyWithFiveKeys() {
        K key1 = "foo";
        K key2 = "bar";
        K key3 = "baz";
        K key4 = "qux";
        K key5 = "quux";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        K[] keys = multiKey.getKeys();
        assertArrayEquals(new K[] { key1, key2, key3, key4, key5 }, keys);
        assertEquals(5, multiKey.size());
    }

    @Test
    public void testMultiKeyWithClonedArray() {
        K key1 = "foo";
        K key2 = "bar";
        MultiKey<K> multiKey = new MultiKey((K[]) new Object[] { key1, key2 });
        K[] keys = multiKey.getKeys();
        assertArrayEquals(new K[] { key1, key2 }, keys);
        assertEquals(2, multiKey.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyWithNullArray() {
        new MultiKey((K[]) null);
    }

    @Test
    public void testMultiKeyWithNullKey() {
        K[] keys = new K[] { null };
        MultiKey<K> multiKey = new MultiKey<>(keys);
        assertNull(multiKey.getKeys()[0]);
    }

    @Test
    public void testMultiKeyWithEqualKeys() {
        K key = "foo";
        MultiKey<K> multiKey1 = new MultiKey<>(key, key);
        MultiKey<K> multiKey2 = new MultiKey<>(key, key);
        assertEquals(multiKey1, multiKey2);
    }

    @Test
    public void testMultiKeyWithDifferentKeys() {
        K key1 = "foo";
        K key2 = "bar";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key2, key1);
        assertNotSame(multiKey1, multiKey2);
    }

    @Test
    public void testMultiKeyWithHashCode() {
        K key1 = "foo";
        K key2 = "bar";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);
        int expectedHashCode = key1.hashCode() ^ key2.hashCode();
        int actualHashCode = multiKey.hashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }
}