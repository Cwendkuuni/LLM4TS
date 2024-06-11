package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;

import java.util.Arrays;

public class MultiKeyTest {

    @Test
    public void testMultiKeyCreation() {
        K[] key1_2 = (K[]) new Object[]{"key1", "key2"};
        MultiKey<String> multiKey1_2 = new MultiKey<>(key1_2);
        assertNotNull(multiKey1_2);
        assertEquals(2, multiKey1_2.size());
        assertArrayEquals(key1_2, multiKey1_2.getKeys());

        K[] key1_2_3 = (K[]) new Object[]{"key1", "key2", "key3"};
        MultiKey<String> multiKey1_2_3 = new MultiKey<>(key1_2_3);
        assertNotNull(multiKey1_2_3);
        assertEquals(3, multiKey1_2_3.size());
        assertArrayEquals(key1_2_3, multiKey1_2_3.getKeys());

        MultiKey<String> multiKey1 = new MultiKey<>("key1");
        assertNotNull(multiKey1);
        assertEquals(1, multiKey1.size());
        assertSame("key1", multiKey1.getKey(0));

        MultiKey<String> multiKey1_2 = new MultiKey<>("key1", "key2");
        assertNotNull(multiKey1_2);
        assertEquals(2, multiKey1_2.size());
        assertArrayEquals(new String[]{"key1", "key2"}, multiKey1_2.getKeys());
    }

    @Test
    public void testMultiKeyEquals() {
        K[] key1_2 = (K[]) new Object[]{"key1", "key2"};
        MultiKey<String> multiKey1_2 = new MultiKey<>(key1_2);
        MultiKey<String> multiKey1_2_clone = new MultiKey<>(key1_2.clone());
        MultiKey<String> multiKey1_2_different = new MultiKey<>(new String[]{"key1", "key3"});

        assertEquals(multiKey1_2, multiKey1_2);
        assertEquals(multiKey1_2, multiKey1_2_clone);
        assertNotEquals(multiKey1_2, multiKey1_2_different);
    }

    @Test
    public void testMultiKeyHashCode() {
        K[] key1_2 = (K[]) new Object[]{"key1", "key2"};
        MultiKey<String> multiKey1_2 = new MultiKey<>(key1_2);
        MultiKey<String> multiKey1_2_clone = new MultiKey<>(key1_2.clone());
        MultiKey<String> multiKey1_2_different = new MultiKey<>(new String[]{"key1", "key3"});

        assertEquals(multiKey1_2.hashCode(), multiKey1_2.hashCode());
        assertEquals(multiKey1_2.hashCode(), multiKey1_2_clone.hashCode());
        assertNotEquals(multiKey1_2.hashCode(), multiKey1_2_different.hashCode());
    }

    @Test
    public void testMultiKeyToString() {
        K[] key1_2 = (K[]) new Object[]{"key1", "key2"};
        MultiKey<String> multiKey1_2 = new MultiKey<>(key1_2);

        assertEquals("MultiKey[key1, key2]", multiKey1_2.toString());
    }
}