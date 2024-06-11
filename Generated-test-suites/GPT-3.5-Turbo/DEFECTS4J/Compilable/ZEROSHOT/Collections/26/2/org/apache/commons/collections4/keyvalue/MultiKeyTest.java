package org.apache.commons.collections4.keyvalue;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import java.io.Serializable;
import java.util.Arrays;

public class MultiKeyTest implements Serializable {

    private MultiKey<String> multiKey;
    
    @Before
    public void setup() {
        multiKey = new MultiKey<>("key1", "key2", "key3");
    }

    @Test
    public void testConstructor() {
        MultiKey<String> newMultiKey = new MultiKey<>("key1", "key2", "key3");
        Assert.assertEquals(multiKey, newMultiKey);
    }

    @Test
    public void testGetKeys() {
        String[] keys = multiKey.getKeys();
        Assert.assertEquals("key1", keys[0]);
        Assert.assertEquals("key2", keys[1]);
        Assert.assertEquals("key3", keys[2]);
    }

    @Test
    public void testGetKey() {
        Assert.assertEquals("key1", multiKey.getKey(0));
        Assert.assertEquals("key2", multiKey.getKey(1));
        Assert.assertEquals("key3", multiKey.getKey(2));
    }

    @Test
    public void testSize() {
        Assert.assertEquals(3, multiKey.size());
    }

    @Test
    public void testEquals() {
        MultiKey<String> newMultiKey = new MultiKey<>("key1", "key2", "key3");
        Assert.assertTrue(multiKey.equals(newMultiKey));
    }

    @Test
    public void testHashCode() {
        MultiKey<String> newMultiKey = new MultiKey<>("key1", "key2", "key3");
        Assert.assertEquals(multiKey.hashCode(), newMultiKey.hashCode());
    }

    @Test
    public void testToString() {
        Assert.assertEquals("MultiKey[key1, key2, key3]", multiKey.toString());
    }

}