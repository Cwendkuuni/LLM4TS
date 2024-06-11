package org.apache.commons.collections4.keyvalue;

import org.apache.commons.collections4.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testMultiKeyWithTwoKeys() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        K[] expectedKeys = new K[2];
        expectedKeys[0] = key1;
        expectedKeys[1] = key2;

        K[] actualKeys = multiKey.getKeys();
        assertArrayEquals(expectedKeys, actualKeys);

        int expectedSize = 2;
        int actualSize = multiKey.size();
        assertEquals(expectedSize, actualSize);

        K expectedKey = key1;
        K actualKey = multiKey.getKey(0);
        assertEquals(expectedKey, actualKey);

        //Test equals method
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        assertTrue(multiKey.equals(multiKey1));

        Object other = new Object();
        assertFalse(multiKey.equals(other));

        MultiKey<String> multiKeyString = new MultiKey<>("key1", "key2");
        assertTrue(multiKey.equals(multiKeyString));

        //Test hashCode method
        int expectedHashCode = 2 * key1.hashCode() ^ key2.hashCode();
        int actualHashCode = multiKey.hashCode();
        assertEquals(expectedHashCode, actualHashCode);

        //Test toString method
        String expectedToString = "MultiKey[" + key1.toString() + ", " + key2.toString() + "]";
        String actualToString = multiKey.toString();
        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testMultiKeyWithThreeKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);

        K[] expectedKeys = new K[3];
        expectedKeys[0] = key1;
        expectedKeys[1] = key2;
        expectedKeys[2] = key3;

        K[] actualKeys = multiKey.getKeys();
        assertArrayEquals(expectedKeys, actualKeys);

        int expectedSize = 3;
        int actualSize = multiKey.size();
        assertEquals(expectedSize, actualSize);

        K expectedKey = key1;
        K actualKey = multiKey.getKey(0);
        assertEquals(expectedKey, actualKey);

        //Test equals method
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2, key3);
        assertTrue(multiKey.equals(multiKey1));

        //Test hashCode method
        int expectedHashCode = 3 * key1.hashCode() ^ key2.hashCode() ^ key3.hashCode();
        int actualHashCode = multiKey.hashCode();
        assertEquals(expectedHashCode, actualHashCode);

        //Test toString method
        String expectedToString = "MultiKey[" + key1.toString() + ", " + key2.toString() + ", " + key3.toString() + "]";
        String actualToString = multiKey.toString();
        assertEquals(expectedToString, actualToString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyWithNullKeys() {
        K key1 = null;
        K key2 = null;
        //MultiKey<K> multiKey = new MultiKey<>(key1, key2); //Throws IllegalArgumentException

        MultiKey<K> multiKey = new MultiKey<>(new K[] { key1, key2 });
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testMultiKeyWithClonedKeys() {
        K key1 = new K() { /* immutable */ };
        K key2 = new K() { /* immutable */ };
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        MultiKey<K> multiKeyClone = new MultiKey<>(multiKey.getKeys().clone());
        assertNotSame(multiKey, multiKeyClone);
        assertArrayEquals(multiKey.getKeys(), multiKeyClone.getKeys());
    }
}