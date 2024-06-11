package org.apache.commons.collections4.keyvalue;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.junit.Test;

public class MultiKeyTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testMultiKeyConstruction() {
        K[] key12 = (K[]) new Object[] { "key1", "key2" };
        MultiKey<String> multiKey12 = new MultiKey<>(key12);
        K[] key12Clone = key12.clone();
        MultiKey<String> multiKey12Clone = new MultiKey<>(key12Clone, false);

        assertEquals("key1, key2", multiKey12.toString());
        assertEquals("key1, key2", multiKey12Clone.toString());
        assertArrayEquals(new Object[] { "key1", "key2" }, multiKey12.getKeys());
        assertArrayEquals(new Object[] { "key1", "key2" }, multiKey12Clone.getKeys());

        K[] key123 = (K[]) new Object[] { "key1", "key2", "key3" };
        MultiKey<String> multiKey123 = new MultiKey<>(key123);
        MultiKey<String> multiKey123Clone = new MultiKey<>(key123.clone(), false);

        assertEquals("key1, key2, key3", multiKey123.toString());
        assertEquals("key1, key2, key3", multiKey123Clone.toString());
        assertArrayEquals(new Object[] { "key1", "key2", "key3" }, multiKey123.getKeys());
        assertArrayEquals(new Object[] { "key1", "key2", "key3" }, multiKey123Clone.getKeys());

        K[] key1234 = (K[]) new Object[] { "key1", "key2", "key3", "key4" };
        MultiKey<String> multiKey1234 = new MultiKey<>(key1234);
        MultiKey<String> multiKey1234Clone = new MultiKey<>(key1234.clone(), false);

        assertEquals("key1, key2, key3, key4", multiKey1234.toString());
        assertEquals("key1, key2, key3, key4", multiKey1234Clone.toString());
        assertArrayEquals(new Object[] { "key1", "key2", "key3", "key4" }, multiKey1234.getKeys());
        assertArrayEquals(new Object[] { "key1", "key2", "key3", "key4" }, multiKey1234Clone.getKeys());

        K[] key12345 = (K[]) new Object[] { "key1", "key2", "key3", "key4", "key5" };
        MultiKey<String> multiKey12345 = new MultiKey<>(key12345);
        MultiKey<String> multiKey12345Clone = new MultiKey<>(key12345.clone(), false);

        assertEquals("key1, key2, key3, key4, key5", multiKey12345.toString());
        assertEquals("key1, key2, key3, key4, key5", multiKey12345Clone.toString());
        assertArrayEquals(new Object[] { "key1", "key2", "key3", "key4", "key5" }, multiKey12345.getKeys());
        assertArrayEquals(new Object[] { "key1", "key2", "key3", "key4", "key5" }, multiKey12345Clone.getKeys());
    }

    @Test
    public void testMultiKeyGetKeyAndSize() {
        K[] key12 = (K[]) new Object[] { "key1", "key2" };
        MultiKey<String> multiKey12 = new MultiKey<>(key12);

        assertEquals("key1", multiKey12.getKey(0));
        assertEquals("key2", multiKey12.getKey(1));
        assertEquals(2, multiKey12.size());

        K[] key123 = (K[]) new Object[] { "key1", "key2", "key3" };
        MultiKey<String> multiKey123 = new MultiKey<>(key123);

        assertEquals("key1", multiKey123.getKey(0));
        assertEquals("key2", multiKey123.getKey(1));
        assertEquals("key3", multiKey123.getKey(2));
        assertEquals(3, multiKey123.size());
    }

    @Test
    public void testMultiKeyEqualsHashCodeToString() {
        K[] key12 = (K[]) new Object[] { "key1", "key2" };
        MultiKey<String> multiKey12 = new MultiKey<>(key12);

        MultiKey<String> multiKey12Equals = new MultiKey<>(key12.clone());
        MultiKey<String> multiKey12Different = new MultiKey<>(new Object[] { "key1", "key3" });

        assertEquals(multiKey12, multiKey12Equals);
        assertEquals(multiKey12.hashCode(), multiKey12Equals.hashCode());
        assertEquals(multiKey12.toString(), multiKey12Equals.toString());

        assertNotEquals(multiKey12, multiKey12Different);
        assertNotEquals(multiKey12.hashCode(), multiKey12Different.hashCode());
        assertNotEquals(multiKey12.toString(), multiKey12Different.toString());
    }
}