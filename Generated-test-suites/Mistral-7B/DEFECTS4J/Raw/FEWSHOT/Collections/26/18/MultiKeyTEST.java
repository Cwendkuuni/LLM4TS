package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testMultiKeyConstructorTwoKeys() {
        K key1 = "key1";
        K key2 = "key2";

        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        K[] keys = multiKey.getKeys();
        assertArrayEquals(new K[] { key1, key2 }, keys);
        assertTrue(Arrays.equals(new Object[] { key1, key2 }, multiKey.keys));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyConstructorNullKeys() {
        K[] keys = null;
        new MultiKey<>(keys);
    }

    @Test
    public void testMultiKeyConstructorThreeKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";

        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);

        K[] keys = multiKey.getKeys();
        assertArrayEquals(new K[] { key1, key2, key3 }, keys);
        assertTrue(Arrays.equals(new Object[] { key1, key2, key3 }, multiKey.keys));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyConstructorNullKeysArray() {
        K[] keys = null;
        new MultiKey<>(keys);
    }

    @Test
    public void testMultiKeyConstructorFourKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";

        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4);

        K[] keys = multiKey.getKeys();
        assertArrayEquals(new K[] { key1, key2, key3, key4 }, keys);
        assertTrue(Arrays.equals(new Object[] { key1, key2, key3, key4 }, multiKey.keys));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyConstructorNullKeysArrayFiveElements() {
        K[] keys = null;
        new MultiKey<>(keys);
    }

    @Test
    public void testMultiKeyConstructorArrayOfKeys() {
        K key1 = "key1";
        K key2 = "key2";

        MultiKey<K> multiKey = new MultiKey<>(new K[] { key1, key2 });

        K[] keys = multiKey.getKeys();
        assertArrayEquals(new K[] { key1, key2 }, keys);
        assertTrue(Arrays.equals(new Object[] { key1, key2 }, multiKey.keys));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyConstructorNullKeysArrayCloned() {
        K[] keys = null;
        new MultiKey<>(keys, false);
    }

    @Test
    public void testMultiKeyGetKeys() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        K[] keys = multiKey.getKeys();
        assertArrayEquals(new K[] { key1, key2 }, keys);
    }

    @Test
    public void testMultiKeyGetKey() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        K key1FromMultiKey = multiKey.getKey(0);
        assertEquals(key1, key1FromMultiKey);

        K key2FromMultiKey = multiKey.getKey(1);
        assertEquals(key2, key2FromMultiKey);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiKeyGetKeyIndexOutOfBounds() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        multiKey.getKey(2);
    }

    @Test
    public void testMultiKeySize() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        int size = multiKey.size();
        assertEquals(2, size);
    }

    @Test
    public void testMultiKeyEquals() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);

        MultiKey<K> multiKey3 = new MultiKey<>(key1, "key2");

        MultiKey<K> multiKey4 = new MultiKey<>(new K[] { key1, key2 });

        assertTrue(multiKey.equals(multiKey2));
        assertTrue(multiKey.equals(multiKey4));
        assertFalse(multiKey.equals(multiKey3));
    }

    @Test
    public void testMultiKeyHashCode() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        int hashCode = multiKey.hashCode();
        int hashCodeDifferentOrder = multiKey.hashCode(); // should be same if equals is true
        K key3 = "key3";
        MultiKey<K> multiKey2 = new MultiKey<>(key2, key1);
        int hashCodeDifferentKeys = new MultiKey<>(key1, key3).hashCode();

        assertEquals(hashCode, hashCode); // should be same as long as equals is true
        assertEquals(hashCode, hashCodeDifferentOrder);
        assertNotEquals(hashCode, hashCodeDifferentKeys);
    }

    @Test
    public void testMultiKeyToString() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        String toString = multiKey.toString();
        String expectedToString = "MultiKey[key1, key2]";

        assertEquals(expectedToString, toString);
    }
}