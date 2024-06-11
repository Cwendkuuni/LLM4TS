import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.collections4.keyvalue.MultiKey;

public class MultiKeyTest {
    
    // Test constructor with two keys
    @Test
    public void testConstructor_twoKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals(2, multiKey.size());
    }
    
    // Test constructor with three keys
    @Test
    public void testConstructor_threeKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
        assertEquals(3, multiKey.size());
    }
    
    // Test constructor with four keys
    @Test
    public void testConstructor_fourKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4");
        
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
        assertEquals("key4", multiKey.getKey(3));
        assertEquals(4, multiKey.size());
    }
    
    // Test constructor with five keys
    @Test
    public void testConstructor_fiveKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4", "key5");
        
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
        assertEquals("key4", multiKey.getKey(3));
        assertEquals("key5", multiKey.getKey(4));
        assertEquals(5, multiKey.size());
    }
    
    // Test constructor with array of keys
    @Test
    public void testConstructor_arrayKeys() {
        String[] keysArray = {"key1", "key2"};
        MultiKey<String> multiKey = new MultiKey<>(keysArray);
        
        assertArrayEquals(keysArray, multiKey.getKeys());
    }
    
    // Test constructor with null array
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullArray() {
        MultiKey<String> multiKey = new MultiKey<>(null);
    }
    
    // Test getKey() with valid index
    @Test
    public void testGetKey_validIndex() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
    }
    
    // Test getKey() with invalid index
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKey_invalidIndex() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        
        multiKey.getKey(3);
    }
    
    // Test size() method
    @Test
    public void testSize() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        
        assertEquals(3, multiKey.size());
    }
    
    // Test equals() with equal multi keys
    @Test
    public void testEquals_equalMultiKeys() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
        
        assertTrue(multiKey1.equals(multiKey2));
    }
    
    // Test equals() with unequal multi keys
    @Test
    public void testEquals_unequalMultiKeys() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key2", "key1");
        
        assertFalse(multiKey1.equals(multiKey2));
    }
    
    // Test equals() with different object type
    @Test
    public void testEquals_differentObjectType() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        String str = "key1,key2";
        
        assertFalse(multiKey.equals(str));
    }
    
    // Test hashCode()
    @Test
    public void testHashCode() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
        
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }
    
    // Test toString()
    @Test
    public void testToString() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        
        assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }
    
}