package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testMultiKeyConstruction() {
        K[] keyArray1 = (K[]) new Object[] {"key1", "key2"};
        MultiKey<String> multiKey1 = new MultiKey<>(keyArray1);
        K[] keyArray2 = (K[]) new Object[] {"key3", "key4"};
        MultiKey<String> multiKey2 = new MultiKey<>(keyArray2);
        K[] keyArray3 = (K[]) new Object[] {"key1", "key2", "key3", "key4"};
        MultiKey<String> multiKey3 = new MultiKey<>(keyArray3);

        assertEquals(2, multiKey1.size());
        assertEquals(4, multiKey3.size());

        assertArrayEquals(new String[]{"key1", "key2"}, multiKey1.getKeys());
        assertArrayEquals(new String[]{"key3", "key4"}, multiKey2.getKeys());
        assertArrayEquals(new String[]{"key1", "key2", "key3", "key4"}, multiKey3.getKeys());

        MultiKey<String> multiKey4 = new MultiKey<>(multiKey1);
        assertArrayEquals(new String[]{"key1", "key2"}, multiKey4.getKeys());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKeys() {
        new MultiKey<>(null);
    }

    @Test
    public void testMultiKeyClone() {
        K[] keyArray = (K[]) new Object[] {"key1", "key2"};
        MultiKey<String> multiKey = new MultiKey<>(keyArray, false);
        MultiKey<String> clonedMultiKey = multiKey.clone();

        assertNotSame(multiKey, clonedMultiKey);
        assertArrayEquals(multiKey.getKeys(), clonedMultiKey.getKeys());
    }

    @Test
    public void testMultiKeyEquals() {
        K[] keyArray1 = (K[]) new Object[] {"key1", "key2"};
        MultiKey<String> multiKey1 = new MultiKey<>(keyArray1);
        K[] keyArray2 = (K[]) new Object[] {"key1", "key2"};
        MultiKey<String> multiKey2 = new MultiKey<>(keyArray2);

        assertEquals(multiKey1, multiKey2);

        MultiKey<String> multiKey3 = new MultiKey<>(keyArray1);
        MultiKey<String> multiKey4 = new MultiKey<>(Arrays.copyOfRange(keyArray1, 1, keyArray1.length));
        assertNotEquals(multiKey1, multiKey3);
        assertNotEquals(multiKey1, multiKey4);
    }

    @Test
    public void testMultiKeyHashCode() {
        K[] keyArray = (K[]) new Object[] {"key1", "key2"};
        MultiKey<String> multiKey = new MultiKey<>(keyArray);

        assertTrue(multiKey.hashCode() != 0);
        assertTrue(multiKey.hashCode() == multiKey.hashCode());

        MultiKey<String> multiKeyClone = new MultiKey<>(multiKey.getKeys());
        assertTrue(multiKey.hashCode() == multiKeyClone.hashCode());
    }

    @Test
    public void testMultiKeyToString() {
        K[] keyArray = (K[]) new Object[] {"key1", "key2"};
        MultiKey<String> multiKey = new MultiKey<>(keyArray);

        assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }
}