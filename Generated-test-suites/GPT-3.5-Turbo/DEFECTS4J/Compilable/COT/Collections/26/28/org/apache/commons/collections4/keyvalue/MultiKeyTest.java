package org.apache.commons.collections4.keyvalue;
import org.junit.Test;
import org.junit.Assert;

public class MultiKeyTest {

    @Test
    public void testConstructorTwoKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2);
        
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
        Assert.assertEquals(2, multiKey.size());
    }
    
    @Test
    public void testConstructorThreeKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        Integer key3 = 3;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2, key3);
        
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
        Assert.assertEquals(key3, multiKey.getKey(2));
        Assert.assertEquals(3, multiKey.size());
    }
    
    @Test
    public void testConstructorFourKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        Integer key3 = 3;
        Integer key4 = 4;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2, key3, key4);
        
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
        Assert.assertEquals(key3, multiKey.getKey(2));
        Assert.assertEquals(key4, multiKey.getKey(3));
        Assert.assertEquals(4, multiKey.size());
    }
    
    @Test
    public void testConstructorFiveKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        Integer key3 = 3;
        Integer key4 = 4;
        Integer key5 = 5;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
        Assert.assertEquals(key3, multiKey.getKey(2));
        Assert.assertEquals(key4, multiKey.getKey(3));
        Assert.assertEquals(key5, multiKey.getKey(4));
        Assert.assertEquals(5, multiKey.size());
    }
    
    @Test
    public void testConstructorArray() {
        Integer[] keys = {1, 2, 3};
        MultiKey<Integer> multiKey = new MultiKey<>(keys);
        
        Assert.assertEquals(keys[0], multiKey.getKey(0));
        Assert.assertEquals(keys[1], multiKey.getKey(1));
        Assert.assertEquals(keys[2], multiKey.getKey(2));
        Assert.assertEquals(3, multiKey.size());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorArrayNull() {
        Integer[] keys = null;
        MultiKey<Integer> multiKey = new MultiKey<>(keys);        
    }
    
    @Test
    public void testEqualsTrue() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<Integer> multiKey2 = new MultiKey<>(key1, key2);        
        Assert.assertTrue(multiKey1.equals(multiKey2));
    }
    
    @Test
    public void testEqualsFalse() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<Integer> multiKey2 = new MultiKey<>(key2, key1);        
        Assert.assertFalse(multiKey1.equals(multiKey2));
    }
    
    @Test
    public void testHashCodeSame() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<Integer> multiKey2 = new MultiKey<>(key2, key1);        
        Assert.assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }
    
    @Test
    public void testHashCodeDifferent() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey1 = new MultiKey<>(key1, key2);
        multiKey1.getKey(0); // Recalculate hashCode
        MultiKey<Integer> multiKey2 = new MultiKey<>(key2, key1);        
        Assert.assertNotEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }
    
    @Test
    public void testToString() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2);
        
        Assert.assertEquals("MultiKey[1, 2]", multiKey.toString());
    }
}