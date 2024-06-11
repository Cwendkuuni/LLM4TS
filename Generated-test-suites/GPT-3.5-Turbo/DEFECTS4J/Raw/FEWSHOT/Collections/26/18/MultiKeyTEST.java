package org.apache.commons.collections4.keyvalue;

import static org.junit.Assert.*;

import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testConstructorTwoKeys() {
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);

        assertEquals(2, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertArrayEquals(new String[] { key1, key2 }, multiKey.getKeys());
    }

    @Test
    public void testConstructorThreeKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);

        assertEquals(3, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertArrayEquals(new String[] { key1, key2, key3 }, multiKey.getKeys());
    }

    @Test
    public void testConstructorFourKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4);

        assertEquals(4, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertArrayEquals(new String[] { key1, key2, key3, key4 }, multiKey.getKeys());
    }

    @Test
    public void testConstructorFiveKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        String key5 = "key5";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);

        assertEquals(5, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(key5, multiKey.getKey(4));
        assertArrayEquals(new String[] { key1, key2, key3, key4, key5 }, multiKey.getKeys());
    }

    @Test
    public void testConstructorArray() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String[] keys = { key1, key2, key3 };
        MultiKey<String> multiKey = new MultiKey<>(keys);

        assertEquals(3, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertArrayEquals(keys, multiKey.getKeys());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullArray() {
        String[] keys = null;
        new MultiKey<>(keys);
    }

    @Test
    public void testEquals() {
        String key1 = "key1";
        String key2 = "key2";
        String[] keys1 = { key1, key2 };
        String[] keys2 = { key1, key2 };
        MultiKey<String> multiKey1 = new MultiKey<>(keys1);
        MultiKey<String> multiKey2 = new MultiKey<>(keys2);
        MultiKey<String> multiKey3 = new MultiKey<>(key1, key2);

        assertTrue(multiKey1.equals(multiKey2));
        assertTrue(multiKey2.equals(multiKey1));
        assertTrue(multiKey1.equals(multiKey3));
        assertTrue(multiKey2.equals(multiKey3));
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
        assertEquals(multiKey1.hashCode(), multiKey3.hashCode());
    }

    @Test
    public void testHashCode() {
        String key1 = "key1";
        String key2 = "key2";
        String[] keys = { key1, key2 };

        MultiKey<String> multiKey1 = new MultiKey<>(keys);
        MultiKey<String> multiKey2 = new MultiKey<>(keys);

        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    @Test
    public void testToString() {
        String key1 = "key1";
        String key2 = "key2";
        String[] keys = { key1, key2 };
        MultiKey<String> multiKey = new MultiKey<>(keys);

        assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }
}