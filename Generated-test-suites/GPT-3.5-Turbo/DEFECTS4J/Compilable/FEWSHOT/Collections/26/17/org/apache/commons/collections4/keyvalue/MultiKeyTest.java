package org.apache.commons.collections4.keyvalue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MultiKeyTest {

    @Test
    public void testConstructorTwoKeys() {
        String key1 = "key1";
        String key2 = "key2";

        MultiKey<String> multiKey = new MultiKey<>(key1, key2);

        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(2, multiKey.size());
    }

    @Test
    public void testConstructorThreeKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";

        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);

        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(3, multiKey.size());
    }

    @Test
    public void testConstructorFourKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";

        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4);

        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(4, multiKey.size());
    }

    @Test
    public void testConstructorFiveKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        String key5 = "key5";

        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);

        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(key5, multiKey.getKey(4));
        assertEquals(5, multiKey.size());
    }

    @Test
    public void testConstructorArrayKeys() {
        String[] keys = {"key1", "key2", "key3"};

        MultiKey<String> multiKey = new MultiKey<>(keys);

        assertEquals(keys[0], multiKey.getKey(0));
        assertEquals(keys[1], multiKey.getKey(1));
        assertEquals(keys[2], multiKey.getKey(2));
        assertEquals(3, multiKey.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullArrayKeys() {
        String[] keys = null;

        new MultiKey<>(keys);
    }

    @Test
    public void testEquals() {
        String key1 = "key1";
        String key2 = "key2";

        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);

        assertEquals(multiKey1, multiKey2);
    }

    @Test
    public void testHashCode() {
        String key1 = "key1";
        String key2 = "key2";

        MultiKey<String> multiKey = new MultiKey<>(key1, key2);

        int expectedHashCode = key1.hashCode() ^ key2.hashCode();

        assertEquals(expectedHashCode, multiKey.hashCode());
    }

    @Test
    public void testToString() {
        String key1 = "key1";
        String key2 = "key2";

        MultiKey<String> multiKey = new MultiKey<>(key1, key2);

        String expectedToString = "MultiKey[" + key1 + ", " + key2 + "]";

        assertEquals(expectedToString, multiKey.toString());
    }
}