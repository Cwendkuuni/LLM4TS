package org.apache.commons.collections4.keyvalue;

import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.collections4.keyvalue.MultiKey;

public class MultiKeyTest {

    @Test
    public void testConstructor_TwoKeys() {
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);

        Assert.assertEquals(2, multiKey.size());
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
    }

    @Test
    public void testConstructor_ThreeKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);

        Assert.assertEquals(3, multiKey.size());
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
        Assert.assertEquals(key3, multiKey.getKey(2));
    }

    @Test
    public void testConstructor_FourKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4);

        Assert.assertEquals(4, multiKey.size());
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
        Assert.assertEquals(key3, multiKey.getKey(2));
        Assert.assertEquals(key4, multiKey.getKey(3));
    }

    @Test
    public void testConstructor_FiveKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        String key5 = "key5";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);

        Assert.assertEquals(5, multiKey.size());
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
        Assert.assertEquals(key3, multiKey.getKey(2));
        Assert.assertEquals(key4, multiKey.getKey(3));
        Assert.assertEquals(key5, multiKey.getKey(4));
    }

    @Test
    public void testConstructor_ArrayKeys() {
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);

        Assert.assertEquals(keys.length, multiKey.size());
        Assert.assertEquals(keys[0], multiKey.getKey(0));
        Assert.assertEquals(keys[1], multiKey.getKey(1));
        Assert.assertEquals(keys[2], multiKey.getKey(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullArrayKeys() {
        String[] keys = null;
        new MultiKey<>(keys);
    }

    @Test
    public void testEquals() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2", "key3");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2", "key3");
        MultiKey<String> multiKey3 = new MultiKey<>("key1", "key2", "key4");

        Assert.assertTrue(multiKey1.equals(multiKey2));
        Assert.assertFalse(multiKey1.equals(multiKey3));
    }

    @Test
    public void testHashCode() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2", "key3");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2", "key3");

        Assert.assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    @Test
    public void testToString() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");

        Assert.assertEquals("MultiKey[key1, key2, key3]", multiKey.toString());
    }

}
