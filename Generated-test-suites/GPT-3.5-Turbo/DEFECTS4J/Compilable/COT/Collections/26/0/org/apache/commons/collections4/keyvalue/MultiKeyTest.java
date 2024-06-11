package org.apache.commons.collections4.keyvalue;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testConstructorWithTwoKeys() {
        String key1 = "key1";
        String key2 = "key2";

        MultiKey<String> multiKey = new MultiKey<>(key1, key2);

        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(2, multiKey.size());
    }

    @Test
    public void testConstructorWithThreeKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";

        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);

        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(3, multiKey.size());
    }

    @Test
    public void testConstructorWithFourKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";

        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4);

        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(4, multiKey.size());
    }

    @Test
    public void testConstructorWithFiveKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        String key5 = "key5";

        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);

        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(key5, multiKey.getKey(4));
        assertEquals(5, multiKey.size());
    }

    @Test
    public void testConstructorWithArray() {
        String[] keys = {"key1", "key2", "key3"};

        MultiKey<String> multiKey = new MultiKey<>(keys);

        assertArrayEquals(keys, multiKey.getKeys());
        assertEquals(3, multiKey.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullArray() {
        MultiKey<String> multiKey = new MultiKey<>(null);
    }

    @Test
    public void testEquals() {
        MultiKey<String> key1 = new MultiKey<>("key1", "key2");
        MultiKey<String> key2 = new MultiKey<>("key1", "key2");
        MultiKey<String> key3 = new MultiKey<>("key2", "key1");

        assertTrue(key1.equals(key2));
        assertFalse(key1.equals(key3));
    }

    @Test
    public void testHashCode() {
        MultiKey<String> key1 = new MultiKey<>("key1", "key2");
        MultiKey<String> key2 = new MultiKey<>("key1", "key2");
        MultiKey<String> key3 = new MultiKey<>("key2", "key1");

        assertEquals(key1.hashCode(), key2.hashCode());
        assertNotEquals(key1.hashCode(), key3.hashCode());
    }

    @Test
    public void testToString() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");

        assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }
}