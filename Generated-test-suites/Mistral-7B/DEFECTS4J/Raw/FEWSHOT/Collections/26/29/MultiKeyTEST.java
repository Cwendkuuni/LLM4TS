package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;

import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testMultiKeyConstruction() {
        // Testing MultiKey construction with different number of keys
        K[] keys1 = new K[] { new Integer(1), new Integer(2) };
        MultiKey<Integer> multiKey1 = new MultiKey<>(keys1, false);

        K[] keys2 = new K[] { new Integer(3), new Integer(4), new Integer(5) };
        MultiKey<Integer> multiKey2 = new MultiKey<>(keys2, false);

        K[] keys3 = new K[] { new Integer(6), new Integer(7), new Integer(8), new Integer(9) };
        MultiKey<Integer> multiKey3 = new MultiKey<>(keys3, false);

        K[] keys4 = new K[] { new Integer(10), new Integer(11), new Integer(12), new Integer(13), new Integer(14) };
        MultiKey<Integer> multiKey4 = new MultiKey<>(keys4, false);

        // Testing MultiKey construction with cloned keys
        K[] keys5 = new K[] { new Integer(15), new Integer(16), new Integer(17) };
        MultiKey<Integer> multiKey5 = new MultiKey<>(keys5, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKeys() {
        K[] keys = null;
        new MultiKey<>(keys, false);
    }

    @Test
    public void testMultiKeyGetKeys() {
        K[] keys = new K[] { new Integer(1), new Integer(2), new Integer(3) };
        MultiKey<Integer> multiKey = new MultiKey<>(keys, false);
        K[] expectedKeys = keys.clone();
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testMultiKeyGetKey() {
        K[] keys = new K[] { new Integer(1), new Integer(2), new Integer(3) };
        MultiKey<Integer> multiKey = new MultiKey<>(keys, false);
        assertEquals(new Integer(1), multiKey.getKey(0));
        assertEquals(new Integer(2), multiKey.getKey(1));
        assertEquals(new Integer(3), multiKey.getKey(2));
    }

    @Test
    public void testMultiKeySize() {
        K[] keys = new K[] { new Integer(1), new Integer(2), new Integer(3) };
        MultiKey<Integer> multiKey = new MultiKey<>(keys, false);
        assertEquals(3, multiKey.size());
    }

    @Test
    public void testMultiKeyEquals() {
        K[] keys1 = new K[] { new Integer(1), new Integer(2) };
        MultiKey<Integer> multiKey1 = new MultiKey<>(keys1, false);

        K[] keys2 = new K[] { new Integer(2), new Integer(1) };
        MultiKey<Integer> multiKey2 = new MultiKey<>(keys2, false);

        K[] keys3 = new K[] { new Integer(3), new Integer(2), new Integer(1) };
        MultiKey<Integer> multiKey3 = new MultiKey<>(keys3, false);

        assertEquals(multiKey1, multiKey1);
        assertEquals(multiKey1, multiKey1);

        assertEquals(multiKey1, multiKey1);
        assertEquals(multiKey1, multiKey1);

        assertEquals(multiKey1, multiKey2);
        assertEquals(multiKey2, multiKey1);

        assertNotEquals(multiKey1, multiKey3);
        assertNotEquals(multiKey3, multiKey1);

        MultiKey<Integer> multiKey4 = new MultiKey<>(keys1.clone(), true);
        assertEquals(multiKey1, multiKey4);

        MultiKey<Integer> multiKey5 = new MultiKey<>(keys2.clone(), true);
        assertNotEquals(multiKey1, multiKey5);

        MultiKey<Integer> multiKey6 = new MultiKey<>(keys3.clone(), true);
        assertNotEquals(multiKey1, multiKey6);

        MultiKey<Integer> multiKey7 = new MultiKey<>(Arrays.asList(1, 2).toArray(new Integer[0]));
        assertEquals(multiKey1, multiKey7);
    }

    @Test
    public void testMultiKeyHashCode() {
        K[] keys = new K[] { new Integer(1), new Integer(2), new Integer(3) };
        MultiKey<Integer> multiKey = new MultiKey<>(keys, false);
        int expectedHashCode = 1 ^ 2 ^ 3;
        assertEquals(expectedHashCode, multiKey.hashCode());
    }

    @Test
    public void testMultiKeyToString() {
        K[] keys = new K[] { new Integer(1), new Integer(2), new Integer(3) };
        MultiKey<Integer> multiKey = new MultiKey<>(keys, false);
        assertEquals("MultiKey[1, 2, 3]", multiKey.toString());
    }
}