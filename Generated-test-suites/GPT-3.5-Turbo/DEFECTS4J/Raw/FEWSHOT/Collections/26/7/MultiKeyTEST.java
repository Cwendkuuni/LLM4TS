package org.apache.commons.collections4.keyvalue;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultiKeyTest {
    private MultiKey<String> multiKey;

    @Before
    public void setUp() {
        multiKey = new MultiKey<>("key1", "key2");
    }

    @Test
    public void testGetKeys() {
        String[] keys = multiKey.getKeys();
        assertArrayEquals(new String[]{"key1", "key2"}, keys);
    }

    @Test
    public void testGetKey() {
        String key1 = multiKey.getKey(0);
        assertEquals("key1", key1);

        String key2 = multiKey.getKey(1);
        assertEquals("key2", key2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKey_OutOfBounds() {
        multiKey.getKey(2);
    }

    @Test
    public void testSize() {
        int size = multiKey.size();
        assertEquals(2, size);
    }

    @Test
    public void testEquals() {
        MultiKey<String> sameKeys = new MultiKey<>("key1", "key2");
        assertTrue(multiKey.equals(sameKeys));

        MultiKey<String> differentKeys = new MultiKey<>("key1", "key3");
        assertFalse(multiKey.equals(differentKeys));

        assertFalse(multiKey.equals(null));
        assertFalse(multiKey.equals("key1"));
    }

    @Test
    public void testHashCode() {
        MultiKey<String> sameKeys = new MultiKey<>("key1", "key2");
        assertEquals(multiKey.hashCode(), sameKeys.hashCode());
    }

    @Test
    public void testToString() {
        String toString = multiKey.toString();
        assertEquals("MultiKey[key1, key2]", toString);
    }
}