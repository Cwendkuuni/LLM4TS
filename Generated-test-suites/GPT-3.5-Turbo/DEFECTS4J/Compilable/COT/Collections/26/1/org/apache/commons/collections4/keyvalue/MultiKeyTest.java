package org.apache.commons.collections4.keyvalue;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.collections4.keyvalue.MultiKey;

public class MultiKeyTest {

    @Test
    public void testConstructorWithTwoKeys() {
        // Test case for constructor MultiKey(final K key1, final K key2)
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2);
        assertEquals(key1, multiKey.getKeys()[0]);
        assertEquals(key2, multiKey.getKeys()[1]);
    }

    @Test
    public void testConstructorWithThreeKeys() {
        // Test case for constructor MultiKey(final K key1, final K key2, final K key3)
        Integer key1 = 1;
        Integer key2 = 2;
        Integer key3 = 3;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2, key3);
        assertEquals(key1, multiKey.getKeys()[0]);
        assertEquals(key2, multiKey.getKeys()[1]);
        assertEquals(key3, multiKey.getKeys()[2]);
    }

    @Test
    public void testConstructorWithFourKeys() {
        // Test case for constructor MultiKey(final K key1, final K key2, final K key3, final K key4)
        Integer key1 = 1;
        Integer key2 = 2;
        Integer key3 = 3;
        Integer key4 = 4;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2, key3, key4);
        assertEquals(key1, multiKey.getKeys()[0]);
        assertEquals(key2, multiKey.getKeys()[1]);
        assertEquals(key3, multiKey.getKeys()[2]);
        assertEquals(key4, multiKey.getKeys()[3]);
    }

    @Test
    public void testConstructorWithFiveKeys() {
        // Test case for constructor MultiKey(final K key1, final K key2, final K key3, final K key4, final K key5)
        Integer key1 = 1;
        Integer key2 = 2;
        Integer key3 = 3;
        Integer key4 = 4;
        Integer key5 = 5;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        assertEquals(key1, multiKey.getKeys()[0]);
        assertEquals(key2, multiKey.getKeys()[1]);
        assertEquals(key3, multiKey.getKeys()[2]);
        assertEquals(key4, multiKey.getKeys()[3]);
        assertEquals(key5, multiKey.getKeys()[4]);
    }

    @Test
    public void testConstructorWithArrayKeys() {
        // Test case for constructor MultiKey(final K[] keys)
        Integer[] keys = {1, 2, 3};
        MultiKey<Integer> multiKey = new MultiKey<>(keys);
        assertEquals(keys[0], multiKey.getKeys()[0]);
        assertEquals(keys[1], multiKey.getKeys()[1]);
        assertEquals(keys[2], multiKey.getKeys()[2]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullArrayKeys() {
        // Test case for constructor MultiKey(final K[] keys) with null array
        MultiKey<Integer> multiKey = new MultiKey<>(null);
    }

    @Test
    public void testGetKey() {
        // Test case for getKey(int index)
        Integer[] keys = {1, 2, 3};
        MultiKey<Integer> multiKey = new MultiKey<>(keys);
        assertEquals(keys[0], multiKey.getKey(0));
        assertEquals(keys[1], multiKey.getKey(1));
        assertEquals(keys[2], multiKey.getKey(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyWithInvalidIndex() {
        // Test case for getKey(int index) with invalid index
        Integer[] keys = {1, 2, 3};
        MultiKey<Integer> multiKey = new MultiKey<>(keys);
        multiKey.getKey(3);
    }

    @Test
    public void testSize() {
        // Test case for size()
        Integer[] keys = {1, 2, 3};
        MultiKey<Integer> multiKey = new MultiKey<>(keys);
        assertEquals(keys.length, multiKey.size());
    }

    @Test
    public void testEquals() {
        // Test case for equals(Object other)
        Integer[] keys1 = {1, 2, 3};
        Integer[] keys2 = {1, 2, 3};
        MultiKey<Integer> multiKey1 = new MultiKey<>(keys1);
        MultiKey<Integer> multiKey2 = new MultiKey<>(keys2);
        assertTrue(multiKey1.equals(multiKey2));
    }

    @Test
    public void testHashCode() {
        // Test case for hashCode()
        Integer[] keys1 = {1, 2, 3};
        Integer[] keys2 = {1, 2, 3};
        MultiKey<Integer> multiKey1 = new MultiKey<>(keys1);
        MultiKey<Integer> multiKey2 = new MultiKey<>(keys2);
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    @Test
    public void testToString() {
        // Test case for toString()
        Integer[] keys = {1, 2, 3};
        MultiKey<Integer> multiKey = new MultiKey<>(keys);
        assertEquals("MultiKey[1, 2, 3]", multiKey.toString());
    }
}
