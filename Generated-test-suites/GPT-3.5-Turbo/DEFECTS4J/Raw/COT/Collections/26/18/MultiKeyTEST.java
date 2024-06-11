import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.collections4.keyvalue.MultiKey;

public class MultiKeyTest {

    @Test
    public void testConstructorTwoKeys() {
        String key1 = "Key1";
        String key2 = "Key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
        Assert.assertEquals(2, multiKey.size());
    }
    
    @Test
    public void testConstructorThreeKeys() {
        String key1 = "Key1";
        String key2 = "Key2";
        String key3 = "Key3";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);
        
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
        Assert.assertEquals(key3, multiKey.getKey(2));
        Assert.assertEquals(3, multiKey.size());
    }
    
    @Test
    public void testConstructorFourKeys() {
        String key1 = "Key1";
        String key2 = "Key2";
        String key3 = "Key3";
        String key4 = "Key4";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4);
        
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
        Assert.assertEquals(key3, multiKey.getKey(2));
        Assert.assertEquals(key4, multiKey.getKey(3));
        Assert.assertEquals(4, multiKey.size());
    }
    
    @Test
    public void testConstructorFiveKeys() {
        String key1 = "Key1";
        String key2 = "Key2";
        String key3 = "Key3";
        String key4 = "Key4";
        String key5 = "Key5";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
        Assert.assertEquals(key3, multiKey.getKey(2));
        Assert.assertEquals(key4, multiKey.getKey(3));
        Assert.assertEquals(key5, multiKey.getKey(4));
        Assert.assertEquals(5, multiKey.size());
    }
    
    @Test
    public void testConstructorArrayKeys() {
        String[] keys = {"Key1", "Key2", "Key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        
        Assert.assertEquals(keys[0], multiKey.getKey(0));
        Assert.assertEquals(keys[1], multiKey.getKey(1));
        Assert.assertEquals(keys[2], multiKey.getKey(2));
        Assert.assertEquals(3, multiKey.size());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullKeys() {
        String[] keys = null;
        MultiKey<String> multiKey = new MultiKey<>(keys);
    }
    
    @Test
    public void testEquals() {
        MultiKey<String> multiKey1 = new MultiKey<>("Key1", "Key2");
        MultiKey<String> multiKey2 = new MultiKey<>("Key1", "Key2");
        MultiKey<String> multiKey3 = new MultiKey<>("Key2", "Key1");
        
        Assert.assertEquals(multiKey1, multiKey2);
        Assert.assertNotEquals(multiKey1, multiKey3);
    }
    
    @Test
    public void testHashCode() {
        MultiKey<String> multiKey1 = new MultiKey<>("Key1", "Key2");
        MultiKey<String> multiKey2 = new MultiKey<>("Key1", "Key2");
        
        Assert.assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }
    
    @Test
    public void testToString() {
        MultiKey<String> multiKey = new MultiKey<>("Key1", "Key2");
        
        Assert.assertEquals("MultiKey[Key1,Key2]", multiKey.toString());
    }
    
    // Additional test cases for edge cases and exception handling scenarios
    
    @Test
    public void testConstructorZeroKeys() {
        String[] keys = {};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        
        Assert.assertEquals(0, multiKey.size());
    }
    
    @Test
    public void testConstructorNullKey() {
        String key1 = null;
        String key2 = "Key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        
        Assert.assertEquals(key1, multiKey.getKey(0));
        Assert.assertEquals(key2, multiKey.getKey(1));
        Assert.assertEquals(2, multiKey.size());
    }
    
    @Test
    public void testConstructorArrayKeysModification() {
        String[] keys = {"Key1", "Key2", "Key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys, false);
        
        keys[0] = "ModifiedKey1";
        
        Assert.assertEquals("Key1", multiKey.getKey(0));
        Assert.assertEquals("Key2", multiKey.getKey(1));
        Assert.assertEquals("Key3", multiKey.getKey(2));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyInvalidIndex() {
        MultiKey<String> multiKey = new MultiKey<>("Key1", "Key2");
        
        multiKey.getKey(2);
    }
    
    @Test
    public void testReadResolve() {
        String[] keys = {"Key1", "Key2"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        MultiKey<String> deserializedMultiKey = (MultiKey<String>) SerializationUtils.clone(multiKey);
        
        Assert.assertEquals(multiKey, deserializedMultiKey);
        Assert.assertEquals(multiKey.hashCode(), deserializedMultiKey.hashCode());
    }
}