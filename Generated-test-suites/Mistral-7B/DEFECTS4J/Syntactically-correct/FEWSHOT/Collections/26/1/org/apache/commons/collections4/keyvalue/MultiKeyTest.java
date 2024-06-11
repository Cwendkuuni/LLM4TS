package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;

import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testMultiKeyConstruction() {
        K[] key12 = (K[]) new Object[] { "key1", "key2" };
        MultiKey<String> multiKey12 = new MultiKey<>(key12);
        K[] expected = (K[]) new Object[] { "key1", "key2" };
        assertArrayEquals(expected, multiKey12.getKeys());

        K[] key123 = (K[]) new Object[] { "key1", "key2", "key3" };
        MultiKey<String> multiKey123 = new MultiKey<>(key123);
        expected = (K[]) new Object[] { "key1", "key2", "key3" };
        assertArrayEquals(expected, multiKey123.getKeys());

        K[] key1234 = (K[]) new Object[] { "key1", "key2", "key3", "key4" };
        MultiKey<String> multiKey1234 = new MultiKey<>(key1234);
        expected = (K[]) new Object[] { "key1", "key2", "key3", "key4" };
        assertArrayEquals(expected, multiKey1234.getKeys());

        K[] key12345 = (K[]) new Object[] { "key1", "key2", "key3", "key4", "key5" };
        MultiKey<String> multiKey12345 = new MultiKey<>(key12345);
        expected = (K[]) new Object[] { "key1", "key2", "key3", "key4", "key5" };
        assertArrayEquals(expected, multiKey12345.getKeys());
    }

    @Test
    public void testMultiKeyConstructionWithClone() {
        K[] key12 = (K[]) new Object[] { "key1", "key2" };
        MultiKey<String> multiKey12 = new MultiKey<>(key12, true);
        K[] expected = Arrays.copyOf(key12, key12.length);
        assertArrayEquals(expected, multiKey12.getKeys());

        K[] key123 = (K[]) new Object[] { "key1", "key2", "key3" };
        MultiKey<String> multiKey123 = new MultiKey<>(key123, true);
        expected = Arrays.copyOf(key123, key123.length);
        assertArrayEquals(expected, multiKey123.getKeys());

        K[] key1234 = (K[]) new Object[] { "key1", "key2", "key3", "key4" };
        MultiKey<String> multiKey1234 = new MultiKey<>(key1234, true);
        expected = Arrays.copyOf(key1234, key1234.length);
        assertArrayEquals(expected, multiKey1234.getKeys());

        K[] key12345 = (K[]) new Object[] { "key1", "key2", "key3", "key4", "key5" };
        MultiKey<String> multiKey12345 = new MultiKey<>(key12345, true);
        expected = Arrays.copyOf(key12345, key12345.length);
        assertArrayEquals(expected, multiKey12345.getKeys());
    }

    @Test
    public void testMultiKeyGetKey() {
        MultiKey<Integer> multiKey12 = new MultiKey<>(1, 2);
        assertEquals(1, multiKey12.getKey(0));
        assertEquals(2, multiKey12.getKey(1));

        MultiKey<Integer> multiKey123 = new MultiKey<>(1, 2, 3);
        assertEquals(1, multiKey123.getKey(0));
        assertEquals(2, multiKey123.getKey(1));
        assertEquals(3, multiKey123.getKey(2));

        MultiKey<Integer> multiKey1234 = new MultiKey<>(1, 2, 3, 4);
        assertEquals(1, multiKey1234.getKey(0));
        assertEquals(2, multiKey1234.getKey(1));
        assertEquals(3, multiKey1234.getKey(2));
        assertEquals(4, multiKey1234.getKey(3));

        MultiKey<Integer> multiKey12345 = new MultiKey<>(1, 2, 3, 4, 5);
        assertEquals(1, multiKey12345.getKey(0));
        assertEquals(2, multiKey12345.getKey(1));
        assertEquals(3, multiKey12345.getKey(2));
        assertEquals(4, multiKey12345.getKey(3));
        assertEquals(5, multiKey12345.getKey(4));
    }

    @Test
    public void testMultiKeySize() {
        MultiKey<Integer> multiKey1 = new MultiKey<>(1);
        assertEquals(1, multiKey1.size());

        MultiKey<Integer> multiKey2 = new MultiKey<>(1, 2);
        assertEquals(2, multiKey2.size());

        MultiKey<Integer> multiKey3 = new MultiKey<>(1, 2, 3);
        assertEquals(3, multiKey3.size());

        MultiKey<Integer> multiKey4 = new MultiKey<>(1, 2, 3, 4);
        assertEquals(4, multiKey4.size());

        MultiKey<Integer> multiKey5 = new MultiKey<>(1, 2, 3, 4, 5);
        assertEquals(5, multiKey5.size());
    }

    @Test
    public void testMultiKeyEquals() {
        K[] key12 = (K[]) new Object[] { "key1", "key2" };
        MultiKey<String> multiKey12 = new MultiKey<>(key12);
        MultiKey<String> multiKey12Copy = new MultiKey<>(key12.clone());
        MultiKey<String> multiKey12Different = new MultiKey<>(new Object[] { "key1", "key3" });

        MultiKey<String> multiKey123 = new MultiKey<>(new Object[] { "key1", "key2", "key3" });
        MultiKey<String> multiKey123Copy = new MultiKey<>(Arrays.copyOf(multiKey123.getKeys(), multiKey123.size()));
        MultiKey<String> multiKey123Different = new MultiKey<>(new Object[] { "key1", "key2", "key4" });

        MultiKey<String> multiKey1234 = new MultiKey<>(new Object[] { "key1", "key2", "key3", "key4" });
        MultiKey<String> multiKey1234Copy = new MultiKey<>(Arrays.copyOf(multiKey1234.getKeys(), multiKey1234.size()));
        MultiKey<String> multiKey1234Different = new MultiKey<>(new Object[] { "key1", "key2", "key4", "key5" });

        MultiKey<String> multiKey12345 = new MultiKey<>(new Object[] { "key1", "key2", "key3", "key4", "key5" });
        MultiKey<String> multiKey12345Copy = new MultiKey<>(Arrays.copyOf(multiKey12345.getKeys(), multiKey12345.size()));
        MultiKey<String> multiKey12345Different = new MultiKey<>(new Object[] { "key1", "key2", "key4", "key5", "key6" });

        assertEquals(multiKey12, multiKey12Copy);
        assertNotEquals(multiKey12, multiKey12Different);

        assertEquals(multiKey123, multiKey123Copy);
        assertNotEquals(multiKey123, multiKey123Different);

        assertEquals(multiKey1234, multiKey1234Copy);
        assertNotEquals(multiKey1234, multiKey1234Different);

        assertEquals(multiKey12345, multiKey12345Copy);
        assertNotEquals(multiKey12345, multiKey12345Different);
    }

    @Test
    public void testMultiKeyHashCode() {
        MultiKey<Integer> multiKey1 = new MultiKey<>(1);
        assertEquals(1, multiKey1.hashCode());

        MultiKey<Integer> multiKey2 = new MultiKey<>(1, 2);
        int expectedHashCode = 1 ^ 2;
        assertEquals(expectedHashCode, multiKey2.hashCode());

        MultiKey<Integer> multiKey3 = new MultiKey<>(1, 2, 3);
        expectedHashCode = 1 ^ 2 ^ 3;
        assertEquals(expectedHashCode, multiKey3.hashCode());

        MultiKey<Integer> multiKey4 = new MultiKey<>(1, 2, 3, 4);
        expectedHashCode = 1 ^ 2 ^ 3 ^ 4;
        assertEquals(expectedHashCode, multiKey4.hashCode());

        MultiKey<Integer> multiKey5 = new MultiKey<>(1, 2, 3, 4, 5);
        expectedHashCode = 1 ^ 2 ^ 3 ^ 4 ^ 5;
        assertEquals(expectedHashCode, multiKey5.hashCode());
    }

    @Test
    public void testMultiKeyToString() {
        MultiKey<Integer> multiKey1 = new MultiKey<>(1);
        assertEquals("MultiKey[1]", multiKey1.toString());

        MultiKey<Integer> multiKey2 = new MultiKey<>(1, 2);
        assertEquals("MultiKey[1, 2]", multiKey2.toString());

        MultiKey<Integer> multiKey3 = new MultiKey<>(1, 2, 3);
        assertEquals("MultiKey[1, 2, 3]", multiKey3.toString());

        MultiKey<Integer> multiKey4 = new MultiKey<>(1, 2, 3, 4);
        assertEquals("MultiKey[1, 2, 3, 4]", multiKey4.toString());

        MultiKey<Integer> multiKey5 = new MultiKey<>(1, 2, 3, 4, 5);
        assertEquals("MultiKey[1, 2, 3, 4, 5]", multiKey5.toString());
    }

    @Test
    public void testMultiKeyNullKey() {
        assertThrows(IllegalArgumentException.class, () -> new MultiKey<>(null));
    }

    @Test
    public void testMultiKeyNullKeysArray() {
        assertThrows(IllegalArgumentException.class, () -> new MultiKey<>(null, false));
    }
}