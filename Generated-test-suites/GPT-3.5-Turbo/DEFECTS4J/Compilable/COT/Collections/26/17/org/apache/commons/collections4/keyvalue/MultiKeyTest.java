package org.apache.commons.collections4.keyvalue;


import org.junit.Test;

import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testConstructor_TwoKeys() {
        String key1 = "Hello";
        String key2 = "World";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
    }

    @Test
    public void testConstructor_ThreeKeys() {
        String key1 = "Hello";
        String key2 = "World";
        String key3 = "!";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
    }

    @Test
    public void testConstructor_FourKeys() {
        String key1 = "Hello";
        String key2 = "World";
        String key3 = "!";
        String key4 = "!";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4);
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
    }

    @Test
    public void testConstructor_FiveKeys() {
        String key1 = "Hello";
        String key2 = "World";
        String key3 = "!";
        String key4 = "!";
        String key5 = "?";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(key5, multiKey.getKey(4));
    }

    @Test
    public void testConstructor_CloneKeys() {
        String key1 = "Hello";
        String key2 = "World";
        String[] keys = {key1, key2};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        assertArrayEquals(keys, multiKey.getKeys());
        keys[0] = "Changed";
        assertNotEquals(keys[0], multiKey.getKey(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullKeys() {
        new MultiKey<>(null);
    }

    @Test
    public void testEquals_SameInstance() {
        MultiKey<String> multiKey = new MultiKey<>("Hello", "World");
        assertEquals(multiKey, multiKey);
    }

    @Test
    public void testEquals_EqualInstances() {
        MultiKey<String> multiKey1 = new MultiKey<>("Hello", "World");
        MultiKey<String> multiKey2 = new MultiKey<>("Hello", "World");
        assertEquals(multiKey1, multiKey2);
    }

    @Test
    public void testEquals_DifferentInstances() {
        MultiKey<String> multiKey1 = new MultiKey<>("Hello", "World");
        MultiKey<String> multiKey2 = new MultiKey<>("World", "Hello");
        assertNotEquals(multiKey1, multiKey2);
    }

    @Test
    public void testHashCode_SameInstance() {
        MultiKey<String> multiKey = new MultiKey<>("Hello", "World");
        assertEquals(multiKey.hashCode(), multiKey.hashCode());
    }

    @Test
    public void testHashCode_EqualInstances() {
        MultiKey<String> multiKey1 = new MultiKey<>("Hello", "World");
        MultiKey<String> multiKey2 = new MultiKey<>("Hello", "World");
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    @Test
    public void testToString() {
        MultiKey<String> multiKey = new MultiKey<>("Hello", "World");
        assertEquals("MultiKey[Hello, World]", multiKey.toString());
    }

}
