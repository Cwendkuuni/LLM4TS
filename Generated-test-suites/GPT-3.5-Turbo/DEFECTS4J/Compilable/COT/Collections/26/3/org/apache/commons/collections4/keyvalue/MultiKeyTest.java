package org.apache.commons.collections4.keyvalue;

import org.junit.Assert;
import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testConstructorTwoKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2);
        
        Integer[] keys = multiKey.getKeys();
        
        Assert.assertEquals(2, keys.length);
        Assert.assertEquals(key1, keys[0]);
        Assert.assertEquals(key2, keys[1]);
    }
    
    @Test
    public void testConstructorThreeKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        Integer key3 = 3;
        
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2, key3);
        
        Integer[] keys = multiKey.getKeys();
        
        Assert.assertEquals(3, keys.length);
        Assert.assertEquals(key1, keys[0]);
        Assert.assertEquals(key2, keys[1]);
        Assert.assertEquals(key3, keys[2]);
    }
    
    @Test
    public void testConstructorFourKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        Integer key3 = 3;
        Integer key4 = 4;
        
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2, key3, key4);
        
        Integer[] keys = multiKey.getKeys();
        
        Assert.assertEquals(4, keys.length);
        Assert.assertEquals(key1, keys[0]);
        Assert.assertEquals(key2, keys[1]);
        Assert.assertEquals(key3, keys[2]);
        Assert.assertEquals(key4, keys[3]);
    }
    
    @Test
    public void testConstructorFiveKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        Integer key3 = 3;
        Integer key4 = 4;
        Integer key5 = 5;
        
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        
        Integer[] keys = multiKey.getKeys();
        
        Assert.assertEquals(5, keys.length);
        Assert.assertEquals(key1, keys[0]);
        Assert.assertEquals(key2, keys[1]);
        Assert.assertEquals(key3, keys[2]);
        Assert.assertEquals(key4, keys[3]);
        Assert.assertEquals(key5, keys[4]);
    }
    
    @Test
    public void testConstructorArrayKeys() {
        Integer[] keys = {1, 2, 3, 4, 5};
        
        MultiKey<Integer> multiKey = new MultiKey<>(keys);
        
        Integer[] resultKeys = multiKey.getKeys();
        
        Assert.assertEquals(keys.length, resultKeys.length);
        Assert.assertArrayEquals(keys, resultKeys);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorArrayKeys_NullArray() {
        Integer[] keys = null;
        
        new MultiKey<>(keys);
    }
    
    @Test
    public void testGetSize() {
        Integer[] keys = {1, 2, 3, 4};
        
        MultiKey<Integer> multiKey = new MultiKey<>(keys);
        
        Assert.assertEquals(keys.length, multiKey.size());
    }
    
    @Test
    public void testGetKey() {
        Integer[] keys = {1, 2, 3, 4};
        
        MultiKey<Integer> multiKey = new MultiKey<>(keys);
        
        Assert.assertEquals(keys[0], multiKey.getKey(0));
        Assert.assertEquals(keys[1], multiKey.getKey(1));
        Assert.assertEquals(keys[2], multiKey.getKey(2));
        Assert.assertEquals(keys[3], multiKey.getKey(3));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKey_InvalidIndex() {
        Integer[] keys = {1, 2, 3, 4};
        
        MultiKey<Integer> multiKey = new MultiKey<>(keys);
        
        multiKey.getKey(4);
    }
    
    @Test
    public void testEquals() {
        Integer[] keys1 = {1, 2, 3};
        Integer[] keys2 = {1, 2, 3};
        Integer[] keys3 = {1, 3, 3};
        
        MultiKey<Integer> multiKey1 = new MultiKey<>(keys1);
        MultiKey<Integer> multiKey2 = new MultiKey<>(keys2);
        MultiKey<Integer> multiKey3 = new MultiKey<>(keys3);
        
        Assert.assertTrue(multiKey1.equals(multiKey2));
        Assert.assertTrue(multiKey2.equals(multiKey1));
        
        Assert.assertFalse(multiKey1.equals(multiKey3));
        Assert.assertFalse(multiKey3.equals(multiKey1));
    }
    
    @Test
    public void testHashCode() {
        Integer[] keys1 = {1, 2, 3};
        Integer[] keys2 = {1, 2, 3};
        Integer[] keys3 = {1, 3, 3};
        
        MultiKey<Integer> multiKey1 = new MultiKey<>(keys1);
        MultiKey<Integer> multiKey2 = new MultiKey<>(keys2);
        MultiKey<Integer> multiKey3 = new MultiKey<>(keys3);
        
        Assert.assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
        
        Assert.assertNotEquals(multiKey1.hashCode(), multiKey3.hashCode());
    }
    
    @Test
    public void testToString() {
        Integer[] keys = {1, 2, 3};
        
        MultiKey<Integer> multiKey = new MultiKey<>(keys);
        
        Assert.assertEquals("MultiKey[1, 2, 3]", multiKey.toString());
    }
}
