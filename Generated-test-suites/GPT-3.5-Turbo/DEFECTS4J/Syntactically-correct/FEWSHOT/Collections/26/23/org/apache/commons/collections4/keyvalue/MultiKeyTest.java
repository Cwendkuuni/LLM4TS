import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;

public class MultiKeyTest {
    
    @Test
    public void testConstructorWithTwoKeys() {
        String key1 = "key1";
        String key2 = "key2";
        
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        
        assertEquals(2, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
    }
    
    @Test
    public void testConstructorWithThreeKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);
        
        assertEquals(3, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
    }
    
    @Test
    public void testConstructorWithFourKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4);
        
        assertEquals(4, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
    }
    
    @Test
    public void testConstructorWithFiveKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        String key5 = "key5";
        
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        
        assertEquals(5, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(key5, multiKey.getKey(4));
    }
    
    @Test
    public void testConstructorWithArray() {
        String[] keysArray = {"key1", "key2", "key3"};
        
        MultiKey<String> multiKey = new MultiKey<>(keysArray);
        
        assertEquals(3, multiKey.size());
        assertEquals(keysArray[0], multiKey.getKey(0));
        assertEquals(keysArray[1], multiKey.getKey(1));
        assertEquals(keysArray[2], multiKey.getKey(2));
        
        // Modify the original array and verify that the multiKey is not affected
        keysArray[0] = "newKey";
        assertNotEquals(keysArray[0], multiKey.getKey(0));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithArrayNull() {
        MultiKey<String> multiKey = new MultiKey<>(null);
    }
    
    @Test
    public void testGetKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);
        
        String[] keysArray = multiKey.getKeys();
        
        assertEquals(3, keysArray.length);
        assertEquals(key1, keysArray[0]);
        assertEquals(key2, keysArray[1]);
        assertEquals(key3, keysArray[2]);
        
        // Modify the original array and verify that the returned array is not affected
        keysArray[0] = "newKey";
        assertNotEquals(keysArray[0], multiKey.getKey(0));
    }
    
    @Test
    public void testEqualsAndHashCode() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2, key3);
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key2, key3);
        MultiKey<String> multiKey3 = new MultiKey<>(key2, key1, key3);
        
        // Test equals
        assertTrue(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(multiKey3));
        
        // Test hashCode
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
        assertNotEquals(multiKey1.hashCode(), multiKey3.hashCode());
    }
    
    @Test
    public void testToString() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);
        
        String expectedString = "MultiKey[key1, key2, key3]";
        
        assertEquals(expectedString, multiKey.toString());
    }
    
}