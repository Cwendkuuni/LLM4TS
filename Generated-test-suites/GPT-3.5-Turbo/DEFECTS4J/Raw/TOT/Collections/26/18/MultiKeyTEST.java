import org.junit.*;

public class MultiKeyTest {
  
    // Test case for constructor taking two keys
    @Test
    public void testConstructorTwoKeys() {
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
        Assert.assertEquals(2, multiKey.size());
        Assert.assertEquals("MultiKey[key1, key2]", multiKey.toString());      
    }
  
    // Test case for constructor taking three keys
    @Test
    public void testConstructorThreeKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);
        
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
        Assert.assertEquals(key3, multiKey.getKey(2));
        Assert.assertEquals(3, multiKey.size());
        Assert.assertEquals("MultiKey[key1, key2, key3]", multiKey.toString());
    }
  
    // Test case for constructor taking four keys
    @Test
    public void testConstructorFourKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4);
        
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
        Assert.assertEquals(key3, multiKey.getKey(2));
        Assert.assertEquals(key4, multiKey.getKey(3));
        Assert.assertEquals(4, multiKey.size());
        Assert.assertEquals("MultiKey[key1, key2, key3, key4]", multiKey.toString());
    }
  
    // Test case for constructor taking five keys
    @Test
    public void testConstructorFiveKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        String key5 = "key5";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
        Assert.assertEquals(key3, multiKey.getKey(2));
        Assert.assertEquals(key4, multiKey.getKey(3));
        Assert.assertEquals(key5, multiKey.getKey(4));
        Assert.assertEquals(5, multiKey.size());
        Assert.assertEquals("MultiKey[key1, key2, key3, key4, key5]", multiKey.toString());
    }
  
    // Test case for constructor taking an array of keys
    @Test
    public void testConstructorArrayKeys() {
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        
        Assert.assertEquals(keys[0], multiKey.getKey(0));
        Assert.assertEquals(keys[1], multiKey.getKey(1));
        Assert.assertEquals(keys[2], multiKey.getKey(2));
        Assert.assertEquals(3, multiKey.size());
        Assert.assertEquals("MultiKey[key1, key2, key3]", multiKey.toString());
    }
  
    // Test case for constructor taking an array of keys without clone
    @Test
    public void testConstructorArrayKeysNoClone() {
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys, false);
        
        Assert.assertEquals(keys[0], multiKey.getKey(0));
        Assert.assertEquals(keys[1], multiKey.getKey(1));
        Assert.assertEquals(keys[2], multiKey.getKey(2));
        Assert.assertEquals(3, multiKey.size());
        Assert.assertEquals("MultiKey[key1, key2, key3]", multiKey.toString());
    }
  
    // Test case for getKeys() method
    @Test
    public void testGetKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);
        
        String[] keys = multiKey.getKeys();
        Assert.assertEquals(key1, keys[0]);
        Assert.assertEquals(key2, keys[1]);
        Assert.assertEquals(key3, keys[2]);
    }
  
    // Test case for getKey() method
    @Test
    public void testGetKey() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);
        
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
        Assert.assertEquals(key3, multiKey.getKey(2));
    }
  
    // Test case for size() method
    @Test
    public void testSize() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);
        
        Assert.assertEquals(3, multiKey.size());
    }
  
    // Test case for equals() method
    @Test
    public void testEquals() {
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey3 = new MultiKey<>(key2, key1);
        
        Assert.assertTrue(multiKey1.equals(multiKey2));
        Assert.assertFalse(multiKey1.equals(multiKey3));
    }
  
    // Test case for hashCode() method
    @Test
    public void testHashCode() {
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);
        
        Assert.assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }
  
    // Test case for toString() method
    @Test
    public void testToString() {
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        
        Assert.assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }
  
}