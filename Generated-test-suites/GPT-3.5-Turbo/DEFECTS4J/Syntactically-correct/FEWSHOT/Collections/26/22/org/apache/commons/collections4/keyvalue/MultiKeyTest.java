package org.apache.commons.collections4.keyvalue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testConstructorWithTwoKeys() {
        String key1 = "key1";
        String key2 = "key2";

        MultiKey<String> multiKey = new MultiKey<>(key1, key2);

        assertEquals(2, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
    }

    @Test
    public void testConstructorWithThreeKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";

        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);

        assertEquals(3, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
    }

    @Test
    public void testConstructorWithFourKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";

        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4);

        assertEquals(4, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
    }

    @Test
    public void testConstructorWithFiveKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        String key5 = "key5";

        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);

        assertEquals(5, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(key5, multiKey.getKey(4));
    }

    @Test
    public void testConstructorWithArray() {
        String[] keys = {"key1", "key2"};

        MultiKey<String> multiKey = new MultiKey<>(keys);

        assertEquals(2, multiKey.size());
        assertEquals(keys[0], multiKey.getKey(0));
        assertEquals(keys[1], multiKey.getKey(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullArray() {
        MultiKey<String> multiKey = new MultiKey<>(null);
    }

    @Test
    public void testEquals() {
        String[] keys1 = {"key1", "key2"};
        String[] keys2 = {"key1", "key2"};

        MultiKey<String> multiKey1 = new MultiKey<>(keys1);
        MultiKey<String> multiKey2 = new MultiKey<>(keys2);

        assertEquals(multiKey1, multiKey2);
    }

    @Test
    public void testNotEquals() {
        String[] keys1 = {"key1", "key2"};
        String[] keys2 = {"key1"};

        MultiKey<String> multiKey1 = new MultiKey<>(keys1);
        MultiKey<String> multiKey2 = new MultiKey<>(keys2);

        assertNotEquals(multiKey1, multiKey2);
    }

    @Test
    public void testHashCode() {
        String[] keys1 = {"key1", "key2"};
        String[] keys2 = {"key1", "key2"};

        MultiKey<String> multiKey1 = new MultiKey<>(keys1);
        MultiKey<String> multiKey2 = new MultiKey<>(keys2);

        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    @Test
    public void testToString() {
        String[] keys = {"key1", "key2"};

        MultiKey<String> multiKey = new MultiKey<>(keys);

        String expected = "MultiKey[key1, key2]";
        assertEquals(expected, multiKey.toString());
    }

    @Test
    public void testGetKeys() {
        String[] keys = {"key1", "key2"};

        MultiKey<String> multiKey = new MultiKey<>(keys);

        String[] returnedKeys = multiKey.getKeys();

        assertNotNull(returnedKeys);
        assertTrue(returnedKeys.length == 2);
        assertEquals(keys[0], returnedKeys[0]);
        assertEquals(keys[1], returnedKeys[1]);
    }

    @Test
    public void testGetKey() {
        String[] keys = {"key1", "key2"};

        MultiKey<String> multiKey = new MultiKey<>(keys);

        String returnedKey = multiKey.getKey(1);

        assertEquals(keys[1], returnedKey);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyWithInvalidIndex() {
        String[] keys = {"key1", "key2"};

        MultiKey<String> multiKey = new MultiKey<>(keys);

        multiKey.getKey(2);
    }

    @Test
    public void testSize() {
        String[] keys = {"key1", "key2"};

        MultiKey<String> multiKey = new MultiKey<>(keys);

        int size = multiKey.size();

        assertEquals(2, size);
    }

}