package org.apache.commons.collections4.keyvalue;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testMultiKeyConstruction() {
        K key1 = new Object() {}.getClass();
        K key2 = new Object() {}.getClass();
        K key3 = new Object() {}.getClass();

        MultiKey<K> multiKey12 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey123 = new MultiKey<>(key1, key2, key3);
        MultiKey<K> multiKey1234 = new MultiKey<>(key1, key2, key3, key3);
        MultiKey<K> multiKeyArray = new MultiKey<>(new K[]{key1, key2, key3});
        MultiKey<K> multiKeyArrayCloned = new MultiKey<>(new K[]{key1, key2, key3}, true);

        assertEquals(2, multiKey12.size());
        assertEquals(3, multiKey123.size());
        assertEquals(4, multiKey1234.size());
        assertEquals(3, multiKeyArray.size());
        assertEquals(3, multiKeyArrayCloned.size());
    }

    @Test
    public void testMultiKeyGetKeys() {
        K key1 = new Object() {}.getClass();
        K key2 = new Object() {}.getClass();
        K key3 = new Object() {}.getClass();

        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
        K[] keys = multiKey.getKeys();

        assertArrayEquals(new K[]{key1, key2, key3}, keys);
    }

    @Test
    public void testMultiKeyGetKey() {
        K key1 = new Object() {}.getClass();
        K key2 = new Object() {}.getClass();
        K key3 = new Object() {}.getClass();

        MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);

        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));

        assertNull(multiKey.getKey(-1));
        assertNull(multiKey.getKey(3));
    }

    @Test
    public void testMultiKeyEquals() {
        K key1 = new Object() {}.getClass();
        K key2 = new Object() {}.getClass();
        K key3 = new Object() {}.getClass();

        MultiKey<K> multiKey12 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey12Clone = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey12Different = new MultiKey<>(key1, new Object() {}.getClass());
        MultiKey<K> multiKey123 = new MultiKey<>(key1, key2, key3);
        MultiKey<K> multiKey123Different = new MultiKey<>(key1, key2, new Object() {}.getClass());

        assertTrue(multiKey12.equals(multiKey12Clone));
        assertFalse(multiKey12.equals(multiKey12Different));
        assertFalse(multiKey12.equals(multiKey123));
    }

    @Test
    public void testMultiKeyHashCode() {
        K key1 = new Object() {}.getClass();
        K key2 = new Object() {}.getClass();
        K key3 = new Object() {}.getClass();

        MultiKey<K> multiKey12 = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey12Clone = new MultiKey<>(key1, key2);
        MultiKey<K> multiKey12Different = new MultiKey<>(key1, new Object() {}.getClass());
        MultiKey<K> multiKey123 = new MultiKey<>(key1, key2, key3);
        MultiKey<K> multiKey123Different = new MultiKey<>(key1, key2, new Object() {}.getClass());

        assertEquals(multiKey12.hashCode(), multiKey12Clone.hashCode());
        assertNotEquals(multiKey12.hashCode(), multiKey12Different.hashCode());
        assertNotEquals(multiKey12.hashCode(), multiKey123.hashCode());
    }
}