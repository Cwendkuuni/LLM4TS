package org.apache.commons.collections4.keyvalue;
import org.junit.*;

public class MultiKeyTest {
    
    @Test
    public void testConstructorTwoKeys() {
        MultiKey<String> multiKey = new MultiKey<String>("key1", "key2");
        
        // Check that the keys are stored correctly
        Assert.assertEquals("key1", multiKey.getKey(0));
        Assert.assertEquals("key2", multiKey.getKey(1));
        
        // Check that the size of the MultiKey is correct
        Assert.assertEquals(2, multiKey.size());
    }
    
    @Test
    public void testConstructorThreeKeys() {
        MultiKey<String> multiKey = new MultiKey<String>("key1", "key2", "key3");
        
        // Check that the keys are stored correctly
        Assert.assertEquals("key1", multiKey.getKey(0));
        Assert.assertEquals("key2", multiKey.getKey(1));
        Assert.assertEquals("key3", multiKey.getKey(2));
        
        // Check that the size of the MultiKey is correct
        Assert.assertEquals(3, multiKey.size());
    }
    
    @Test
    public void testConstructorFourKeys() {
        MultiKey<String> multiKey = new MultiKey<String>("key1", "key2", "key3", "key4");
        
        // Check that the keys are stored correctly
        Assert.assertEquals("key1", multiKey.getKey(0));
        Assert.assertEquals("key2", multiKey.getKey(1));
        Assert.assertEquals("key3", multiKey.getKey(2));
        Assert.assertEquals("key4", multiKey.getKey(3));
        
        // Check that the size of the MultiKey is correct
        Assert.assertEquals(4, multiKey.size());
    }
    
    @Test
    public void testConstructorFiveKeys() {
        MultiKey<String> multiKey = new MultiKey<String>("key1", "key2", "key3", "key4", "key5");
        
        // Check that the keys are stored correctly
        Assert.assertEquals("key1", multiKey.getKey(0));
        Assert.assertEquals("key2", multiKey.getKey(1));
        Assert.assertEquals("key3", multiKey.getKey(2));
        Assert.assertEquals("key4", multiKey.getKey(3));
        Assert.assertEquals("key5", multiKey.getKey(4));
        
        // Check that the size of the MultiKey is correct
        Assert.assertEquals(5, multiKey.size());
    }
    
    @Test
    public void testConstructorArrayKeys() {
        String[] keys = new String[] {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<String>(keys);
        
        // Check that the keys are stored correctly
        Assert.assertEquals("key1", multiKey.getKey(0));
        Assert.assertEquals("key2", multiKey.getKey(1));
        Assert.assertEquals("key3", multiKey.getKey(2));
        
        // Check that the size of the MultiKey is correct
        Assert.assertEquals(3, multiKey.size());
        
        // Check that modifying the original array does not affect the MultiKey
        keys[0] = "modified";
        Assert.assertEquals("key1", multiKey.getKey(0));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullKeys() {
        MultiKey<String> multiKey = new MultiKey<String>(null);
    }
    
    @Test
    public void testEquals() {
        MultiKey<String> multiKey1 = new MultiKey<String>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<String>("key1", "key2");
        MultiKey<String> multiKey3 = new MultiKey<String>("key1", "key3");
        
        // Check that equal MultiKey objects are recognized as equal
        Assert.assertEquals(multiKey1, multiKey2);
        Assert.assertEquals(multiKey2, multiKey1);
        Assert.assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
        
        // Check that unequal MultiKey objects are recognized as unequal
        Assert.assertNotEquals(multiKey1, multiKey3);
        Assert.assertNotEquals(multiKey3, multiKey1);
    }
    
    @Test
    public void testHashCode() {
        MultiKey<String> multiKey1 = new MultiKey<String>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<String>("key1", "key2");
        MultiKey<String> multiKey3 = new MultiKey<String>("key1", "key3");
        
        // Check that equal MultiKey objects have the same hash code
        Assert.assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
        
        // Check that unequal MultiKey objects have different hash codes
        Assert.assertNotEquals(multiKey1.hashCode(), multiKey3.hashCode());
    }
    
    @Test
    public void testToString() {
        MultiKey<String> multiKey = new MultiKey<String>("key1", "key2");
        
        // Check that the toString method returns the expected string
        Assert.assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }
    
    // Test additional public methods
    
    @Test
    public void testGetKeys() {
        MultiKey<String> multiKey = new MultiKey<String>("key1", "key2");
        String[] keys = multiKey.getKeys();
        
        // Check that the returned array is equal to the original keys
        Assert.assertArrayEquals(new String[] {"key1", "key2"}, keys);
        
        // Check that modifying the returned array does not affect the MultiKey
        keys[0] = "modified";
        Assert.assertEquals("key1", multiKey.getKey(0));
    }

    @Test
    public void testGetKeyAtIndex() {
        MultiKey<String> multiKey = new MultiKey<String>("key1", "key2", "key3");
        
        // Check that the correct key is returned for each index
        Assert.assertEquals("key1", multiKey.getKey(0));
        Assert.assertEquals("key2", multiKey.getKey(1));
        Assert.assertEquals("key3", multiKey.getKey(2));
        
        // Check that accessing an invalid index throws an exception
        try {
            multiKey.getKey(3);
            Assert.fail("IndexOutOfBoundsException not thrown");
        } catch (IndexOutOfBoundsException ex) {
            // Expected exception
        }
    }

    @Test
    public void testSize() {
        MultiKey<String> multiKey1 = new MultiKey<String>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<String>("key1", "key2", "key3");
        
        // Check that the size of all MultiKey objects is correct
        Assert.assertEquals(2, multiKey1.size());
        Assert.assertEquals(3, multiKey2.size());
    }
    
}