package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;

import org.junit.Test;

public class MultiKeyTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testMultiKeyConstruction() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        K key5 = "key5";

        MultiKey<K> multiKey12 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey123 = new MultiKey<>(key1, key2, key3);
        MultiKey<K> multiKey1234 = new MultiKey<>(key1, key2, key3, key4);
        MultiKey<K> multiKey12345 = new MultiKey<>(key1, key2, key3, key4, key5);
        MultiKey<K> multiKeyKeys = new MultiKey<>(new K[] { key1, key2 });
        MultiKey<K> multiKeyKeysCloned = new MultiKey<>(new K[] { key1, key2 }, false);

        assertEquals(2, multiKey12.size());
        assertEquals(3, multiKey123.size());
        assertEquals(4, multiKey1234.size());
        assertEquals(5, multiKey12345.size());

        assertArrayEquals(new Object[] { key1, key2 }, multiKey12.getKeys());
        assertArrayEquals(new Object[] { key1, key2, key3 }, multiKey123.getKeys());
        assertArrayEquals(new Object[] { key1, key2, key3, key4 }, multiKey1234.getKeys());
        assertArrayEquals(new Object[] { key1, key2, key3, key4, key5 }, multiKey12345.getKeys());
        assertArrayEquals(new Object[] { key1, key2 }, multiKeyKeys.getKeys());
        assertSame(multiKeyKeys, multiKeyKeysCloned);

        assertNull(multiKeyKeysCloned.getKey(-1));
        assertEquals(key1, multiKey12.getKey(0));
        assertEquals(key2, multiKey12.getKey(1));
        assertEquals(key3, multiKey123.getKey(2));
    }

    @Test
    public void testMultiKeyEquals() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";

        MultiKey<K> multiKey12 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey12Copy = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey12Other = new MultiKey<>(key1, new K() {});
        MultiKey<K> multiKey3 = new MultiKey<>(key3);
        MultiKey<K> multiKey12Different = new MultiKey<>(key1, new K[] { key2, key4 });

        assertEquals(multiKey12, multiKey12Copy);
        assertNotEquals(multiKey12, multiKey12Other);
        assertNotEquals(multiKey12, multiKey3);
        assertNotEquals(multiKey12, multiKey12Different);

        MultiKey<K> multiKeyCloned = new MultiKey<>(multiKey12.getKeys());
        assertEquals(multiKey12, multiKeyCloned);
    }

    @Test
    public void testMultiKeyHashcode() {
        K key1 = "key1";
        K key2 = "key2";

        MultiKey<K> multiKey12 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey12Copy = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey12Other = new MultiKey<>(key1, new K() {});
        MultiKey<K> multiKey3 = new MultiKey<>(key3);
        MultiKey<K> multiKey12Different = new MultiKey<>(key1, new K[] { key2, key4 });

        assertEquals(multiKey12.hashCode(), multiKey12Copy.hashCode());
        assertNotEquals(multiKey12.hashCode(), multiKey12Other.hashCode());
        assertNotEquals(multiKey12.hashCode(), multiKey3.hashCode());
        assertNotEquals(multiKey12.hashCode(), multiKey12Different.hashCode());

        MultiKey<K> multiKeyCloned = new MultiKey<>(multiKey12.getKeys());
        assertEquals(multiKey12.hashCode(), multiKeyCloned.hashCode());
    }
}