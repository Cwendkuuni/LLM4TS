package org.apache.commons.collections4.keyvalue;
import org.junit.Assert;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class MultiKeyTest {

    @Test
    public void testConstructorTwoKeys() {
        Integer key1 = new Integer(1);
        Integer key2 = new Integer(2);
        MultiKey<Integer> multiKey = new MultiKey<Integer>(key1, key2);

        Assert.assertEquals(2, multiKey.size());
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
    }

    @Test
    public void testConstructorThreeKeys() {
        Integer key1 = new Integer(1);
        Integer key2 = new Integer(2);
        Integer key3 = new Integer(3);
        MultiKey<Integer> multiKey = new MultiKey<Integer>(key1, key2, key3);

        Assert.assertEquals(3, multiKey.size());
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
        Assert.assertEquals(key3, multiKey.getKey(2));
    }

    @Test
    public void testConstructorFourKeys() {
        Integer key1 = new Integer(1);
        Integer key2 = new Integer(2);
        Integer key3 = new Integer(3);
        Integer key4 = new Integer(4);
        MultiKey<Integer> multiKey = new MultiKey<Integer>(key1, key2, key3, key4);

        Assert.assertEquals(4, multiKey.size());
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
        Assert.assertEquals(key3, multiKey.getKey(2));
        Assert.assertEquals(key4, multiKey.getKey(3));
    }

    @Test
    public void testConstructorFiveKeys() {
        Integer key1 = new Integer(1);
        Integer key2 = new Integer(2);
        Integer key3 = new Integer(3);
        Integer key4 = new Integer(4);
        Integer key5 = new Integer(5);
        MultiKey<Integer> multiKey = new MultiKey<Integer>(key1, key2, key3, key4, key5);

        Assert.assertEquals(5, multiKey.size());
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
        Assert.assertEquals(key3, multiKey.getKey(2));
        Assert.assertEquals(key4, multiKey.getKey(3));
        Assert.assertEquals(key5, multiKey.getKey(4));
    }

    @Test
    public void testConstructorArray() {
        Integer key1 = new Integer(1);
        Integer key2 = new Integer(2);
        Integer key3 = new Integer(3);
        Integer[] keys = {key1, key2, key3};
        MultiKey<Integer> multiKey = new MultiKey<Integer>(keys);

        Assert.assertEquals(3, multiKey.size());
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
        Assert.assertEquals(key3, multiKey.getKey(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullArray() {
        MultiKey<Integer> multiKey = new MultiKey<Integer>(null);
    }

    @Test
    public void testEquals() {
        Integer key1 = new Integer(1);
        Integer key2 = new Integer(2);
        MultiKey<Integer> multiKey1 = new MultiKey<Integer>(key1, key2);
        MultiKey<Integer> multiKey2 = new MultiKey<Integer>(key1, key2);

        Assert.assertEquals(true, multiKey1.equals(multiKey2));
    }

    @Test
    public void testNotEquals() {
        Integer key1 = new Integer(1);
        Integer key2 = new Integer(2);
        Integer key3 = new Integer(3);
        MultiKey<Integer> multiKey1 = new MultiKey<Integer>(key1, key2);
        MultiKey<Integer> multiKey2 = new MultiKey<Integer>(key1, key3);

        Assert.assertEquals(false, multiKey1.equals(multiKey2));
    }

    @Test
    public void testHashCode() {
        Integer key1 = new Integer(1);
        Integer key2 = new Integer(2);
        MultiKey<Integer> multiKey1 = new MultiKey<Integer>(key1, key2);
        MultiKey<Integer> multiKey2 = new MultiKey<Integer>(key1, key2);

        Assert.assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    @Test
    public void testToString() {
        Integer key1 = new Integer(1);
        Integer key2 = new Integer(2);
        MultiKey<Integer> multiKey = new MultiKey<Integer>(key1, key2);

        Assert.assertEquals("MultiKey[1, 2]", multiKey.toString());
    }
}