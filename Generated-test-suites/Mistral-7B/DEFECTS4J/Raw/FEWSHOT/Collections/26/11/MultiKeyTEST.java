package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testMultiKeyConstruction() {
        K[] key12 = new K[2];
        key12[0] = "key1";
        key12[1] = "key2";

        MultiKey<String> multiKey1 = new MultiKey<String>(key12, false);
        MultiKey<String> multiKey2 = new MultiKey<String>(new String[]{"key1", "key2"}, false);

        assertTrue(multiKey1.equals(multiKey2));
        assertTrue(multiKey1.hashCode() == multiKey2.hashCode());
    }

    @Test
    public void testMultiKeyClone() {
        K[] key12 = new K[2];
        key12[0] = "key1";
        key12[1] = "key2";

        MultiKey<String> multiKey1 = new MultiKey<String>(key12, true);
        MultiKey<String> multiKey2 = new MultiKey<String>(key12.clone(), true);

        assertTrue(multiKey1.equals(multiKey2));
        assertTrue(multiKey1.hashCode() == multiKey2.hashCode());

        key12[0] = "newKey1";
        assertTrue(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.hashCode() == multiKey2.hashCode());
    }

    @Test
    public void testMultiKeySize() {
        K[] key34 = new K[4];
        key34[0] = "key1";
        key34[1] = "key2";
        key34[2] = "key3";
        key34[3] = "key4";

        MultiKey<String> multiKey = new MultiKey<String>(key34, false);

        assertEquals(multiKey.size(), 4);
    }

    @Test
    public void testMultiKeyGetKey() {
        K[] key12 = new K[2];
        key12[0] = "key1";
        key12[1] = "key2";

        MultiKey<String> multiKey = new MultiKey<String>(key12, false);

        assertEquals(multiKey.getKey(0), "key1");
        assertEquals(multiKey.getKey(1), "key2");
    }

    @Test
    public void testMultiKeyGetKeys() {
        K[] key12 = new K[2];
        key12[0] = "key1";
        key12[1] = "key2";

        MultiKey<String> multiKey = new MultiKey<String>(key12, false);

        K[] keys = multiKey.getKeys();

        assertArrayEquals(key12, keys);
    }

    @Test
    public void testMultiKeyHashCodeAndEquals() {
        K[] key12 = new K[2];
        key12[0] = "key1";
        key12[1] = "key2";

        MultiKey<String> multiKey1 = new MultiKey<String>(key12, false);
        MultiKey<String> multiKey2 = new MultiKey<String>(key12, false);

        assertTrue(multiKey1.equals(multiKey2));
        assertTrue(multiKey1.hashCode() == multiKey2.hashCode());

        key12[0] = "newKey1";
        assertFalse(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.hashCode() == multiKey2.hashCode());
    }

    @Test
    public void testMultiKeyNull() {
        MultiKey<String> multiKey = new MultiKey<String>(null, false);
        assertNull(multiKey);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyNullKeys() {
        new MultiKey<String>(null, false);
    }
}