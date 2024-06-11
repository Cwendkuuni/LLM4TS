package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testMultiKeyConstruction() {
        K[] key12 = (K[]) new Object[] { "key1", "key2" };
        K[] key123 = (K[]) new Object[] { "key1", "key2", "key3" };
        K[] key1234 = (K[]) new Object[] { "key1", "key2", "key3", "key4" };
        K[] key12345 = (K[]) new Object[] { "key1", "key2", "key3", "key4", "key5" };

        MultiKey<String> multiKey12 = new MultiKey<>(key12, false);
        MultiKey<String> multiKey12Clone = new MultiKey<>(key12.clone(), true);
        MultiKey<String> multiKey123 = new MultiKey<>(key123, false);
        MultiKey<String> multiKey123Clone = new MultiKey<>(key123.clone(), true);
        MultiKey<String> multiKey1234 = new MultiKey<>(key1234, false);
        MultiKey<String> multiKey1234Clone = new MultiKey<>(key1234.clone(), true);
        MultiKey<String> multiKey12345 = new MultiKey<>(key12345, false);

        assertEquals(multiKey12, multiKey12Clone);
        assertArrayEquals(key12, multiKey12.getKeys());
        assertNotSame(multiKey12, multiKey123);
        assertNotSame(multiKey12, multiKey123Clone);
        assertNotSame(multiKey12, multiKey1234);
        assertNotSame(multiKey12, multiKey1234Clone);
        assertNotSame(multiKey12, multiKey12345);
        assertArrayEquals(key123, multiKey123.getKeys());
        assertArrayEquals(key123, multiKey123Clone.getKeys());
        assertArrayEquals(key123, multiKey1234.getKeys());
        assertArrayEquals(key123, multiKey1234Clone.getKeys());
        assertArrayEquals(key123, multiKey12345.getKeys());
    }

    @Test
    public void testMultiKeyGetters() {
        K[] key12 = (K[]) new Object[] { "key1", "key2" };
        MultiKey<String> multiKey12 = new MultiKey<>(key12, false);

        K[] keys = multiKey12.getKeys();
        assertEquals(key12, keys);
        assertEquals("key1", multiKey12.getKey(0));
        assertEquals("key2", multiKey12.getKey(1));
        assertEquals(2, multiKey12.size());
    }

    @Test
    public void testMultiKeyEquals() {
        K[] key12 = (K[]) new Object[] { "key1", "key2" };
        K[] key12Dup = (K[]) key12.clone();
        K[] key123 = (K[]) new Object[] { "key1", "key2", "key3" };
        K[] key12DupWithKey3 = (K[]) Arrays.copyOf(key12, key12.length + 1);
        key12DupWithKey3[key12.length] = "key3";

        MultiKey<String> multiKey12 = new MultiKey<>(key12, false);
        MultiKey<String> multiKey12Clone = new MultiKey<>(key12.clone(), true);
        MultiKey<String> multiKey12Dup = new MultiKey<>(key12Dup, false);
        MultiKey<String> multiKey12DupClone = new MultiKey<>(key12Dup.clone(), true);
        MultiKey<String> multiKey123 = new MultiKey<>(key123, false);
        MultiKey<String> multiKey12DupWithKey3 = new MultiKey<>(key12DupWithKey3, false);

        assertTrue(multiKey12.equals(multiKey12Clone));
        assertTrue(multiKey12.equals(multiKey12Clone));
        assertFalse(multiKey12.equals(multiKey123));
        assertFalse(multiKey12.equals(multiKey12Dup));
        assertFalse(multiKey12.equals(multiKey12DupClone));
        assertFalse(multiKey12.equals(multiKey12DupWithKey3));
    }

    @Test
    public void testMultiKeyHashCode() {
        K[] key12 = (K[]) new Object[] { "key1", "key2" };
        MultiKey<String> multiKey12 = new MultiKey<>(key12, false);

        assertEquals(multiKey12.hashCode(), multiKey12.hashCode());
        assertEquals(multiKey12.hashCode(), multiKey12.hashCode());
        assertNotEquals(multiKey12.hashCode(), new Object().hashCode());
        assertNotEquals(multiKey12.hashCode(), new Object().hashCode());

        K[] key12Dup = (K[]) key12.clone();
        MultiKey<String> multiKey12Dup = new MultiKey<>(key12Dup, false);

        assertEquals(multiKey12.hashCode(), multiKey12Dup.hashCode());
        assertEquals(multiKey12.hashCode(), multiKey12Dup.hashCode());

        K[] key123 = (K[]) new Object[] { "key1", "key2", "key3" };
        MultiKey<String> multiKey123 = new MultiKey<>(key123, false);

        assertNotEquals(multiKey12.hashCode(), multiKey123.hashCode());
        assertNotEquals(multiKey12.hashCode(), multiKey123.hashCode());
    }

    @Test
    public void testMultiKeyToString() {
        K[] key12 = (K[]) new Object[] { "key1", "key2" };
        MultiKey<String> multiKey12 = new MultiKey<>(key12, false);

        assertEquals("MultiKey[key1, key2]", multiKey12.toString());
    }
}