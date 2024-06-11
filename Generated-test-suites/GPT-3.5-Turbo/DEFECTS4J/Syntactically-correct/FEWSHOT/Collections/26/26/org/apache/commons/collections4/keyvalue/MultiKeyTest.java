package org.apache.commons.collections4.keyvalue;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MultiKeyTest {
    @Test
    public void testConstructorTwoKeys() {
        String[] keys = { "key1", "key2" };
        MultiKey<String> multiKey = new MultiKey<String>(keys);
        assertArrayEquals(keys, multiKey.getKeys());
        assertEquals(keys.length, multiKey.size());
    }

    @Test
    public void testConstructorThreeKeys() {
        String[] keys = { "key1", "key2", "key3" };
        MultiKey<String> multiKey = new MultiKey<String>(keys);
        assertArrayEquals(keys, multiKey.getKeys());
        assertEquals(keys.length, multiKey.size());
    }

    @Test
    public void testConstructorFourKeys() {
        String[] keys = { "key1", "key2", "key3", "key4" };
        MultiKey<String> multiKey = new MultiKey<String>(keys);
        assertArrayEquals(keys, multiKey.getKeys());
        assertEquals(keys.length, multiKey.size());
    }

    @Test
    public void testConstructorFiveKeys() {
        String[] keys = { "key1", "key2", "key3", "key4", "key5" };
        MultiKey<String> multiKey = new MultiKey<String>(keys);
        assertArrayEquals(keys, multiKey.getKeys());
        assertEquals(keys.length, multiKey.size());
    }

    @Test
    public void testEquals() {
        String[] keys1 = { "key1", "key2" };
        MultiKey<String> multiKey1 = new MultiKey<String>(keys1);

        String[] keys2 = { "key1", "key2" };
        MultiKey<String> multiKey2 = new MultiKey<String>(keys2);

        assertTrue(multiKey1.equals(multiKey2));
    }

    @Test
    public void testNotEqualsDifferentKeys() {
        String[] keys1 = { "key1", "key2" };
        MultiKey<String> multiKey1 = new MultiKey<String>(keys1);

        String[] keys2 = { "key1", "key3" };
        MultiKey<String> multiKey2 = new MultiKey<String>(keys2);

        assertFalse(multiKey1.equals(multiKey2));
    }

    @Test
    public void testNotEqualsDifferentSize() {
        String[] keys1 = { "key1", "key2" };
        MultiKey<String> multiKey1 = new MultiKey<String>(keys1);

        String[] keys2 = { "key1", "key2", "key3" };
        MultiKey<String> multiKey2 = new MultiKey<String>(keys2);

        assertFalse(multiKey1.equals(multiKey2));
    }

    @Test
    public void testHashCode() {
        String[] keys1 = { "key1", "key2" };
        MultiKey<String> multiKey1 = new MultiKey<String>(keys1);

        String[] keys2 = { "key1", "key2" };
        MultiKey<String> multiKey2 = new MultiKey<String>(keys2);

        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }
}