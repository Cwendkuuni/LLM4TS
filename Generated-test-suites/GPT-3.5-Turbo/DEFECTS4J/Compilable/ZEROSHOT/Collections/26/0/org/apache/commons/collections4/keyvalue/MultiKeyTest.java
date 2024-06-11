package org.apache.commons.collections4.keyvalue;

import org.junit.Assert;
import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testConstructorWithTwoKeys() {
        String key1 = "Key1";
        String key2 = "Key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        Assert.assertNotNull(multiKey);
        Assert.assertEquals(2, multiKey.size());
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
    }

    @Test
    public void testConstructorWithThreeKeys() {
        String key1 = "Key1";
        String key2 = "Key2";
        String key3 = "Key3";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);
        Assert.assertNotNull(multiKey);
        Assert.assertEquals(3, multiKey.size());
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
        Assert.assertEquals(key3, multiKey.getKey(2));
    }

    @Test
    public void testConstructorWithFourKeys() {
        String key1 = "Key1";
        String key2 = "Key2";
        String key3 = "Key3";
        String key4 = "Key4";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4);
        Assert.assertNotNull(multiKey);
        Assert.assertEquals(4, multiKey.size());
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
        Assert.assertEquals(key3, multiKey.getKey(2));
        Assert.assertEquals(key4, multiKey.getKey(3));
    }

    @Test
    public void testConstructorWithFiveKeys() {
        String key1 = "Key1";
        String key2 = "Key2";
        String key3 = "Key3";
        String key4 = "Key4";
        String key5 = "Key5";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        Assert.assertNotNull(multiKey);
        Assert.assertEquals(5, multiKey.size());
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
        Assert.assertEquals(key3, multiKey.getKey(2));
        Assert.assertEquals(key4, multiKey.getKey(3));
        Assert.assertEquals(key5, multiKey.getKey(4));
    }

    @Test
    public void testConstructorWithArray() {
        String key1 = "Key1";
        String key2 = "Key2";
        String key3 = "Key3";
        String key4 = "Key4";
        String key5 = "Key5";
        String[] keys = new String[]{key1, key2, key3, key4, key5};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        Assert.assertNotNull(multiKey);
        Assert.assertEquals(5, multiKey.size());
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
        Assert.assertEquals(key3, multiKey.getKey(2));
        Assert.assertEquals(key4, multiKey.getKey(3));
        Assert.assertEquals(key5, multiKey.getKey(4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullArray() {
        MultiKey<String> multiKey = new MultiKey<>(null);
    }

    @Test
    public void testEquals() {
        String key1a = "Key1";
        String key1b = "Key1";
        String key2 = "Key2";
        MultiKey<String> multiKey1 = new MultiKey<>(key1a, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key1b, key2);
        Assert.assertEquals(multiKey1, multiKey2);
    }

    @Test
    public void testNotEquals() {
        String key1 = "Key1";
        String key2 = "Key2";
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key2, key1);
        Assert.assertNotEquals(multiKey1, multiKey2);
    }

    @Test
    public void testHashCode() {
        String key1a = "Key1";
        String key1b = "Key1";
        String key2 = "Key2";
        MultiKey<String> multiKey1 = new MultiKey<>(key1a, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key1b, key2);
        Assert.assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    @Test
    public void testToString() {
        String key1 = "Key1";
        String key2 = "Key2";
        String key3 = "Key3";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);
        Assert.assertEquals("MultiKey[Key1, Key2, Key3]", multiKey.toString());
    }
}