package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class MultiKeyTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testMultiKeyConstruction() {
        K[] keys1 = new Object[] {"key1", "locale1"};
        MultiKey<String> multiKey1 = new MultiKey<>(keys1);

        K[] keys2 = new Object[] {"key2", "locale2"};
        MultiKey<String> multiKey2 = new MultiKey<>(keys2);

        K[] keys3 = new Object[] {"key3", "locale3", "locale3"};
        MultiKey<String> multiKey3 = new MultiKey<>(keys3, false);

        K[] keys4 = new Object[] {"key1", "locale1", "key2", "locale2"};
        MultiKey<String> multiKey4 = new MultiKey<>(keys4);

        K[] keys5 = new Object[] {"key1", "locale1", "key2", "locale2", "key3", "locale3"};
        MultiKey<String> multiKey5 = new MultiKey<>(keys5);

        assertEquals(keys1, multiKey1.getKeys());
        assertEquals(keys2, multiKey2.getKeys());
        assertEquals(keys3, multiKey3.getKeys());
        assertEquals(keys4, multiKey4.getKeys());
        assertEquals(keys5, multiKey5.getKeys());
    }

    @Test
    public void testMultiKeyGetters() {
        K[] keys = new Object[] {"key1", "locale1"};
        MultiKey<String> multiKey = new MultiKey<>(keys);

        assertArrayEquals(keys, multiKey.getKeys());
        assertEquals(keys[0], multiKey.getKey(0));
        assertEquals(keys[1], multiKey.getKey(1));
        assertEquals(2, multiKey.size());
    }

    @Test
    public void testMultiKeyEquals() {
        K[] keys1 = new Object[] {"key1", "locale1"};
        MultiKey<String> multiKey1 = new MultiKey<>(keys1);

        K[] keys2 = new Object[] {"key2", "locale2"};
        MultiKey<String> multiKey2 = new MultiKey<>(keys2);

        MultiKey<String> multiKey3 = new MultiKey<>(keys1);

        assertTrue(multiKey1.equals(multiKey3));
        assertFalse(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(new Object()));
    }

    @Test
    public void testMultiKeyHashCode() {
        K[] keys1 = new Object[] {"key1", "locale1"};
        MultiKey<String> multiKey1 = new MultiKey<>(keys1);

        K[] keys2 = new Object[] {"key2", "locale2"};
        MultiKey<String> multiKey2 = new MultiKey<>(keys2);

        assertEquals(multiKey1.hashCode(), multiKey1.hashCode());
        assertNotEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    @Test
    public void testMultiKeyToString() {
        K[] keys = new Object[] {"key1", "locale1"};
        MultiKey<String> multiKey = new MultiKey<>(keys);

        String expected = "MultiKey[key1, locale1]";
        String actual = multiKey.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testMultiKeyClone() {
        K[] keys = new Object[] {"key1", "locale1"};
        MultiKey<String> multiKey = new MultiKey<>(keys);

        MultiKey<String> clonedMultiKey = new MultiKey<>(multiKey.getKeys().clone());

        assertNotSame(multiKey, clonedMultiKey);
        assertArrayEquals(multiKey.getKeys(), clonedMultiKey.getKeys());
    }

    @Test
    public void testMultiKeyNullKeys() {
        K[] keys = null;

        try {
            new MultiKey<>(keys);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = NullPointerException.class)
    public void testMultiKeyNullKeysCloned() {
        K[] keys = null;
        new MultiKey<>(keys, true);
    }
}