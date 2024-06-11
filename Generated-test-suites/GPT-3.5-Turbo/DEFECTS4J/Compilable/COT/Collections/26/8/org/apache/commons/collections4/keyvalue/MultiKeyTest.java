package org.apache.commons.collections4.keyvalue;

import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testConstructorWithTwoKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals(2, multiKey.size());
    }

    @Test
    public void testConstructorWithThreeKeys() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
        assertEquals(Integer.valueOf(1), multiKey.getKey(0));
        assertEquals(Integer.valueOf(2), multiKey.getKey(1));
        assertEquals(Integer.valueOf(3), multiKey.getKey(2));
        assertEquals(3, multiKey.size());
    }

    @Test
    public void testConstructorWithFourKeys() {
        MultiKey<Double> multiKey = new MultiKey<>(1.0, 2.0, 3.0, 4.0);
        assertEquals(Double.valueOf(1.0), multiKey.getKey(0));
        assertEquals(Double.valueOf(2.0), multiKey.getKey(1));
        assertEquals(Double.valueOf(3.0), multiKey.getKey(2));
        assertEquals(Double.valueOf(4.0), multiKey.getKey(3));
        assertEquals(4, multiKey.size());
    }

    @Test
    public void testConstructorWithFiveKeys() {
        MultiKey<Boolean> multiKey = new MultiKey<>(true, false, true, false, true);
        assertEquals(Boolean.valueOf(true), multiKey.getKey(0));
        assertEquals(Boolean.valueOf(false), multiKey.getKey(1));
        assertEquals(Boolean.valueOf(true), multiKey.getKey(2));
        assertEquals(Boolean.valueOf(false), multiKey.getKey(3));
        assertEquals(Boolean.valueOf(true), multiKey.getKey(4));
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
    public void testEqualsAndHashCode() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey3 = new MultiKey<>("key2", "key1");

        assertEquals(multiKey1, multiKey2);
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
        assertNotEquals(multiKey1, multiKey3);
        assertNotEquals(multiKey1.hashCode(), multiKey3.hashCode());
    }

    @Test
    public void testToString() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
        assertEquals("MultiKey[1, 2, 3]", multiKey.toString());
    }

}
