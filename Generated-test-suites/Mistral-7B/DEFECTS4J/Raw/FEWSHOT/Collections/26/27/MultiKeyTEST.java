package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;

import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testMultiKeyWithTwoKeys() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        K[] actualKeys = multiKey.getKeys();
        assertEquals(2, actualKeys.length);
        assertEquals(key1, actualKeys[0]);
        assertEquals(key2, actualKeys[1]);

        assertEquals(multiKey, new MultiKey<>(key1, key2));
        assertEquals(multiKey.hashCode(), new MultiKey<>(key1, key2).hashCode());
        assertEquals(multiKey.toString(), "MultiKey[key1, key2]");
    }

    @Test
    public void testMultiKeyWithThreeKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);

        K[] actualKeys = multiKey.getKeys();
        assertEquals(3, actualKeys.length);
        assertEquals(key1, actualKeys[0]);
        assertEquals(key2, actualKeys[1]);
        assertEquals(key3, actualKeys[2]);

        assertEquals(multiKey, new MultiKey<>(key1, key2, key3));
        assertEquals(multiKey.hashCode(), new MultiKey<>(key1, key2, key3).hashCode());
        assertEquals(multiKey.toString(), "MultiKey[key1, key2, key3]");
    }

    @Test
    public void testMultiKeyWithFourKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4);

        K[] actualKeys = multiKey.getKeys();
        assertEquals(4, actualKeys.length);
        assertEquals(key1, actualKeys[0]);
        assertEquals(key2, actualKeys[1]);
        assertEquals(key3, actualKeys[2]);
        assertEquals(key4, actualKeys[3]);

        assertEquals(multiKey, new MultiKey<>(key1, key2, key3, key4));
        assertEquals(multiKey.hashCode(), new MultiKey<>(key1, key2, key3, key4).hashCode());
        assertEquals(multiKey.toString(), "MultiKey[key1, key2, key3, key4]");
    }

    @Test
    public void testMultiKeyWithClone() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(new Object[] {key1, key2});

        K[] actualKeys = multiKey.getKeys();
        assertEquals(2, actualKeys.length);
        assertEquals(key1, actualKeys[0]);
        assertEquals(key2, actualKeys[1]);

        K[] newKeys = Arrays.copyOf(actualKeys, 3);
        newKeys[2] = "newKey";
        assertNotSame(multiKey, new MultiKey<>(newKeys));
    }

    @Test
    public void testMultiKeyWithNull() {
        assertThrows(IllegalArgumentException.class, () -> new MultiKey<>(null));
        assertThrows(IllegalArgumentException.class, () -> new MultiKey<>(null, "key2"));
        assertThrows(IllegalArgumentException.class, () -> new MultiKey<>(new Object[] {null}));
        assertThrows(IllegalArgumentException.class, () -> new MultiKey<>(new Object[] {null, "key2"}));
        assertThrows(IllegalArgumentException.class, () -> new MultiKey<>(new Object[] {"key1", null}));
        assertThrows(IllegalArgumentException.class, () -> new MultiKey<>(new Object[] {"key1", null, "key3"}));
    }
}