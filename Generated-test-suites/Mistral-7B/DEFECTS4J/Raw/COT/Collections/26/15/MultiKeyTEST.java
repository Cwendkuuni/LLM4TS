package org.apache.commons.collections4.keyvalue;

import static org.junit.Assert.*;

import org.apache.commons.collections4.keyvalue.MultiKey;

import java.util.Arrays;

public class MultiKeyTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testMultiKeyTwoKeys() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        K[] keys = multiKey.getKeys();
        assertArrayEquals(new K[] {key1, key2}, keys);
        assertEquals(2, multiKey.size());

        assertEquals(multiKey.hashCode(), multiKey.hashCode()); // check hashcode after construction
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMultiKeyThreeKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);

        K[] keys = multiKey.getKeys();
        assertArrayEquals(new K[] {key1, key2, key3}, keys);
        assertEquals(3, multiKey.size());

        assertEquals(multiKey.hashCode(), multiKey.hashCode()); // check hashcode after construction
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMultiKeyFourKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        K key4 = "key4";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4);

        K[] keys = multiKey.getKeys();
        assertArrayEquals(new K[] {key1, key2, key3, key4}, keys);
        assertEquals(4, multiKey.size());

        assertEquals(multiKey.hashCode(), multiKey.hashCode()); // check hashcode after construction
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMultiKeyArrayOfKeys() {
        K key1 = "key1";
        K key2 = "key2";
        K key3 = "key3";
        MultiKey<K> multiKey = new MultiKey<>(new K[] {key1, key2, key3});

        K[] keys = multiKey.getKeys();
        assertArrayEquals(new K[] {key1, key2, key3}, keys);
        assertEquals(3, multiKey.size());

        assertEquals(multiKey.hashCode(), multiKey.hashCode()); // check hashcode after construction
    }

    @Test
    public void testEquals() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);

        MultiKey<K> multiKey3 = new MultiKey<>(key1, new K() {/*mutable key*/});
        assertNotEquals(multiKey1, multiKey3);

        MultiKey<K> multiKey4 = new MultiKey<>(key1, key2, new K[] {key1, key2});
        assertNotEquals(multiKey1, multiKey4);

        MultiKey<K> multiKey5 = new MultiKey<>(Arrays.copyOfRange(new K[] {key1, key2}, 1, 2));
        assertNotEquals(multiKey1, multiKey5);

        MultiKey<K> multiKey6 = new MultiKey<>(new K[] {key2, key1});
        assertNotEquals(multiKey1, multiKey6);

        MultiKey<String> multiKey7 = new MultiKey<>("key1", "key2");
        assertNotEquals(multiKey1, multiKey7);

        MultiKey<Object> multiKey8 = new MultiKey<>(key1, key2);
        assertEquals(multiKey1, multiKey8); // type erasure

        Object[] keys1 = multiKey1.getKeys();
        Object[] keys2 = multiKey8.getKeys();
        assertArrayEquals(keys1, keys2);
    }

    @Test
    public void testHashCode() {
        K key1 = "key1";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key1);
        MultiKey<K> multiKey2 = new MultiKey<>(key1, new K() {/*mutable key*/});

        assertEquals(multiKey1.hashCode(), multiKey1.hashCode());
        assertEquals(multiKey1.hashCode(), multiKey1.hashCode()); // same keys, same hashcode
        assertNotEquals(multiKey1.hashCode(), multiKey2.hashCode()); // different keys, different hashcode
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKeys() {
        new MultiKey<>(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKeysArray() {
        new MultiKey<>(null);
    }

    @Test
    public void testClone() {
        K key1 = "key1";
        MultiKey<K> multiKey1 = new MultiKey<>(key1, key1);
        MultiKey<K> clonedMultiKey = multiKey1.clone();

        assertEquals(multiKey1, clonedMultiKey); // same keys, same hashcode

        K[] keys1 = multiKey1.getKeys();
        K[] keys2 = clonedMultiKey.getKeys();
        assertArrayEquals(keys1, keys2);
    }

    @Test
    public void testCloneWithClone() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(new K[] {key1, key2});
        MultiKey<K> clonedMultiKey = multiKey.clone();

        assertNotSame(multiKey.getKeys(), clonedMultiKey.getKeys()); // different arrays
        assertArrayEquals(multiKey.getKeys(), clonedMultiKey.getKeys()); // same keys
    }

    @Test
    public void testToString() {
        K key1 = "key1";
        K key2 = "key2";
        MultiKey<K> multiKey = new MultiKey<>(key1, key2);

        assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }
}