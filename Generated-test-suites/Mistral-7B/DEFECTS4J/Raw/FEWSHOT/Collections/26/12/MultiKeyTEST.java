package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;

import java.util.Arrays;

import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testMultiKeyConstruction() {
        K key1 = (K) ("key1").getClass().newInstance();
        K key2 = (K) ("key2").getClass().newInstance();
        K key3 = (K) ("key3").getClass().newInstance();

        MultiKey<K> multiKey12 = new MultiKey<K>(key1, key2);
        MultiKey<K> multiKey12Clone = new MultiKey<K>(key1, key2);

        MultiKey<K> multiKey123 = new MultiKey<K>(key1, key2, key3);
        MultiKey<K> multiKey123Clone = new MultiKey<K>(key1, key2, key3);

        MultiKey<K> multiKey1234 = new MultiKey<K>(key1, key2, key3, key3);
        MultiKey<K> multiKey1234Clone = new MultiKey<K>(key1, key2, key3, key3);

        MultiKey<K> multiKeyArray = new MultiKey<K>(new K[] { key1, key2 });

        MultiKey<K> multiKeyClone = new MultiKey<K>(multiKeyArray.getKeys().clone());

        assertEquals(multiKey12.getClass(), MultiKey.class);
        assertEquals(multiKey12.size(), 2);
        assertArrayEquals(multiKey12.getKeys(), new K[] { key1, key2 });
        assertEquals(multiKey12, multiKey12Clone);

        assertEquals(multiKey123.getClass(), MultiKey.class);
        assertEquals(multiKey123.size(), 3);
        assertArrayEquals(multiKey123.getKeys(), new K[] { key1, key2, key3 });
        assertEquals(multiKey123, multiKey123Clone);

        MultiKey<K> multiKey123Different = new MultiKey<K>(key1, key3, key2);
        assertNotSame(multiKey123, multiKey123Different);

        MultiKey<K> multiKeyArrayDifferent = new MultiKey<K>(new K[] { key1, key3 });
        assertNotSame(multiKeyArray, multiKeyArrayDifferent);

        MultiKey<K> multiKeyCloneDifferent = new MultiKey<K>(multiKeyArray.getKeys());
        assertNotSame(multiKeyArray, multiKeyCloneDifferent);
    }

    @Test
    public void testMultiKeyHashCode() {
        K key1 = (K) ("key1").getClass().newInstance();
        K key2 = (K) ("key2").getClass().newInstance();
        K key3 = (K) ("key3").getClass().newInstance();

        MultiKey<K> multiKey12 = new MultiKey<K>(key1, key2);
        MultiKey<K> multiKey12Clone = new MultiKey<K>(key1, key2);

        MultiKey<K> multiKey123 = new MultiKey<K>(key1, key2, key3);
        MultiKey<K> multiKey123Clone = new MultiKey<K>(key1, key2, key3);

        MultiKey<K> multiKey123Different = new MultiKey<K>(key1, key3, key2);

        MultiKey<K> multiKeyArray = new MultiKey<K>(new K[] { key1, key2 });

        MultiKey<K> multiKeyClone = new MultiKey<K>(multiKeyArray.getKeys().clone());

        assertEquals(multiKey12.hashCode(), multiKey12.hashCode());
        assertEquals(multiKey12.hashCode(), multiKey12Clone.hashCode());

        assertEquals(multiKey123.hashCode(), multiKey123.hashCode());
        assertEquals(multiKey123.hashCode(), multiKey123Clone.hashCode());

        assertNotEquals(multiKey12.hashCode(), multiKey123.hashCode());
        assertNotEquals(multiKey12.hashCode(), multiKey123Different.hashCode());

        assertNotEquals(multiKeyArray.hashCode(), multiKeyClone.hashCode());
    }

    @Test
    public void testMultiKeyToString() {
        K key1 = (K) ("key1").getClass().newInstance();
        K key2 = (K) ("key2").getClass().newInstance();
        K key3 = (K) ("key3").getClass().newInstance();

        MultiKey<K> multiKey12 = new MultiKey<K>(key1, key2);
        MultiKey<K> multiKey12Clone = new MultiKey<K>(key1, key2);

        MultiKey<K> multiKey123 = new MultiKey<K>(key1, key2, key3);
        MultiKey<K> multiKey123Clone = new MultiKey<K>(key1, key2, key3);

        MultiKey<K> multiKey123Different = new MultiKey<K>(key1, key3, key2);

        MultiKey<K> multiKeyArray = new MultiKey<K>(new K[] { key1, key2 });

        MultiKey<K> multiKeyClone = new MultiKey<K>(multiKeyArray.getKeys().clone());

        assertEquals(multiKey12.toString(), "MultiKey[key1, key2]");
        assertEquals(multiKey12.toString(), multiKey12Clone.toString());

        assertEquals(multiKey123.toString(), "MultiKey[key1, key2, key3]");
        assertEquals(multiKey123.toString(), multiKey123Clone.toString());

        assertNotEquals(multiKey12.toString(), multiKey123.toString());
        assertNotEquals(multiKey12.toString(), multiKey123Different.toString());

        assertNotEquals(multiKeyArray.toString(), multiKeyClone.toString());
    }
}