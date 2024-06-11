package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testEquals() {
        K key1 = (K) ("key1").getClass().newInstance();
        K key2 = (K) ("key2").getClass().newInstance();
        K key3 = (K) ("key3").getClass().newInstance();

        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key2, key1);
        MultiKey<K> multiKey3 = new MultiKey<>(key1, key2, key3);
        MultiKey<K> multiKey4 = new MultiKey<>(key2, key1, key3);
        MultiKey<K> multiKey5 = new MultiKey<>(key1, key2, key3, key3);
        MultiKey<K> multiKey6 = new MultiKey<>(key1, key2, key3, key3, key3);

        MultiKey<K> multiKey7 = new MultiKey<>(key1, key2, key3);

        MultiKey<Object> multiKey8 = new MultiKey<>(key1, key2);
        assertFalse(multiKey1.equals(multiKey2));
        assertTrue(multiKey1.equals(multiKey1));
        assertTrue(multiKey1.equals(multiKey7));
        assertTrue(multiKey1.equals(multiKey3));
        assertTrue(multiKey1.equals(multiKey4));
        assertFalse(multiKey1.equals(multiKey5));
        assertFalse(multiKey1.equals(multiKey6));
        assertFalse(multiKey1.equals(multiKey8));
        assertFalse(multiKey1.equals(null));
        assertFalse(multiKey1.equals(new Object()));
    }

    @Test
    public void testGetKeys() {
        K key1 = (K) ("key1").getClass().newInstance();
        K key2 = (K) ("key2").getClass().newInstance();
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        K[] keys = multiKey.getKeys();
        assertArrayEquals(new K[]{key1, key2}, keys);
    }

    @Test
    public void testGetKey() {
        K key1 = (K) ("key1").getClass().newInstance();
        K key2 = (K) ("key2").getClass().newInstance();
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        K key = multiKey.getKey(0);
        assertSame(key1, key);

        key = multiKey.getKey(1);
        assertSame(key2, key);

        try {
            multiKey.getKey(-1);
            assert False;
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        try {
            multiKey.getKey(2);
            assert False;
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testSize() {
        K key1 = (K) ("key1").getClass().newInstance();
        K key2 = (K) ("key2").getClass().newInstance();
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        int size = multiKey.size();

        assertEquals(2, size);
    }

    @Test
    public void testToString() {
        K key1 = (K) ("key1").getClass().newInstance();
        K key2 = (K) ("key2").getClass().newInstance();
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        String string = multiKey.toString();

        String expected = "MultiKey[key1, key2]";
        assertEquals(expected, string);
    }

    @Test
    public void testHashCode() {
        K key1 = (K) ("key1").getClass().newInstance();
        K key2 = (K) ("key2").getClass().newInstance();
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        int hashCode = multiKey.hashCode();

        int expected = key1.hashCode() ^ key2.hashCode();
        assertEquals(expected, hashCode);
    }
}