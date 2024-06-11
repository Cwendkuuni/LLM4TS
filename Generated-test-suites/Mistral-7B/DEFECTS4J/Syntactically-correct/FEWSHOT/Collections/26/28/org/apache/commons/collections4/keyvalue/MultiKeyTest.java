package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testMultiKeyCreation() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        K key5 = "key5";

        MultiKey<K> multiKey2Keys = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey3Keys = new MultiKey<>(key1, key2, key3);
        MultiKey<K> multiKey4Keys = new MultiKey<>(key1, key2, key3, key4);
        MultiKey<K> multiKey5Keys = new MultiKey<>(key1, key2, key3, key4, key5);
        MultiKey<K> multiKeyArrayKeys = new MultiKey<>(new K[]{key1, key2});

        assertEquals(2, multiKey2Keys.size());
        assertEquals(3, multiKey3Keys.size());
        assertEquals(4, multiKey4Keys.size());
        assertEquals(5, multiKey5Keys.size());

        assertArrayEquals(new K[]{key1, key2}, multiKeyArrayKeys.getKeys());
        assertArrayEquals(new K[]{key1, key2}, multiKey2Keys.getKeys());
        assertArrayEquals(new K[]{key1, key2, key3}, multiKey3Keys.getKeys());
        assertArrayEquals(new K[]{key1, key2, key3, key4}, multiKey4Keys.getKeys());
        assertArrayEquals(new K[]{key1, key2, key3, key4, key5}, multiKey5Keys.getKeys());

        assertSame(key1, multiKey2Keys.getKey(0));
        assertSame(key1, multiKey3Keys.getKey(0));
        assertSame(key1, multiKey4Keys.getKey(0));
        assertSame(key1, multiKey5Keys.getKey(0));
        assertSame(key2, multiKey2Keys.getKey(1));
        assertSame(key2, multiKey3Keys.getKey(1));
        assertSame(key2, multiKey4Keys.getKey(1));
        assertSame(key2, multiKey5Keys.getKey(1));

        assertNull(multiKey2Keys.getKey(-1));
        assertNull(multiKey3Keys.getKey(3));
        assertNull(multiKey4Keys.getKey(5));
        assertNull(multiKey5Keys.getKey(6));
    }

    @Test
    public void testCloneMultiKey() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(new K[]{key1, key2, key3});

        MultiKey<K> clonedMultiKey = (MultiKey<K>) multiKey.clone();

        assertEquals(multiKey.size(), clonedMultiKey.size());
        assertArrayEquals(multiKey.getKeys(), clonedMultiKey.getKeys());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKeysArray() {
        new MultiKey<>(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKey() {
        K[] keys = new K[3];
        keys[0] = "key1";
        keys[1] = null;
        keys[2] = "key3";

        try {
            new MultiKey<>(keys, false);
        } catch (IllegalArgumentException e) {
            assertEquals("The array of keys must not be null", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testHashcode() {
        K key1 = "key1";
        K key2 = "key2";

        MultiKey<K> multiKey1 = new MultiKey<>(key1);
        MultiKey<K> multiKey2 = new MultiKey<>(key2);
        MultiKey<K> multiKey12 = new MultiKey<>(key1, key2);

        int hashcodeMultiKey1 = multiKey1.hashCode();
        int hashcodeMultiKey2 = multiKey2.hashCode();
        int hashcodeMultiKey12 = multiKey12.hashCode();

        assertEquals(hashcodeMultiKey1, multiKey1.hashCode());
        assertEquals(hashcodeMultiKey2, multiKey2.hashCode());
        assertEquals(hashcodeMultiKey12, multiKey12.hashCode());

        K key1Changed = new KeyWithChangedHashcode(key1);
        MultiKey<K> multiKey1Changed = new MultiKey<>(key1Changed);

        int hashcodeMultiKey1Changed = multiKey1Changed.hashCode();

        assertNotSame(hashcodeMultiKey1, hashcodeMultiKey1Changed);
    }

    @Test
    public void testEquals() {
        K key1 = "key1";
        K key2 = "key2";
        K key1Changed = new KeyWithChangedHashcode(key1);

        MultiKey<K> multiKey1 = new MultiKey<>(key1);
        MultiKey<K> multiKey2 = new MultiKey<>(key2);
        MultiKey<K> multiKey12 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey1Changed = new MultiKey<>(key1Changed);

        assertTrue(multiKey1.equals(multiKey1));
        assertTrue(multiKey1.equals(multiKey12));
        assertFalse(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(new Object()));
        assertFalse(multiKey1.equals(null));
        assertFalse(multiKey1.equals(multiKey1Changed));
    }

    private static class KeyWithChangedHashcode extends String implements Serializable {
        private static final long serialVersionUID = 4465448607415788805L;

        public KeyWithChangedHashcode(String original) {
            super(original);
        }

        @Override
        public int hashCode() {
            return 42;
        }
    }
}