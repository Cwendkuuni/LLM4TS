import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {
    
    // Test MultiKey constructor with two keys
    @Test
    public void testMultiKeyConstructorTwoKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2);
        
        assertEquals(2, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
    }
    
    // Test MultiKey constructor with three keys
    @Test
    public void testMultiKeyConstructorThreeKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        Integer key3 = 3;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2, key3);
        
        assertEquals(3, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
    }
    
    // Test MultiKey constructor with four keys
    @Test
    public void testMultiKeyConstructorFourKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        Integer key3 = 3;
        Integer key4 = 4;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2, key3, key4);
        
        assertEquals(4, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
    }
    
    // Test MultiKey constructor with five keys
    @Test
    public void testMultiKeyConstructorFiveKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        Integer key3 = 3;
        Integer key4 = 4;
        Integer key5 = 5;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        
        assertEquals(5, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(key5, multiKey.getKey(4));
    }
    
    // Test MultiKey constructor with array of keys
    @Test
    public void testMultiKeyConstructorArrayKeys() {
        Integer[] keys = {1, 2, 3, 4, 5};
        MultiKey<Integer> multiKey = new MultiKey<>(keys);
        
        assertEquals(keys.length, multiKey.size());
        for (int i = 0; i < keys.length; i++) {
            assertEquals(keys[i], multiKey.getKey(i));
        }
    }
    
    // Test MultiKey constructor with null array of keys (should throw IllegalArgumentException)
    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyConstructorNullArrayKeys() {
        Integer[] keys = null;
        MultiKey<Integer> multiKey = new MultiKey<>(keys);
    }
    
    // Test MultiKey constructor with null keys (should throw IllegalArgumentException)
    @Test(expected = IllegalArgumentException.class)
    public void testMultiKeyConstructorNullKeys() {
        Integer key1 = null;
        Integer key2 = null;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2);
    }
    
    // Test getKeys method
    @Test
    public void testGetKeys() {
        Integer[] keys = {1, 2, 3, 4, 5};
        MultiKey<Integer> multiKey = new MultiKey<>(keys);
        
        Integer[] retrievedKeys = multiKey.getKeys();
        assertArrayEquals(keys, retrievedKeys);
    }
    
    // Test getKey method
    @Test
    public void testGetKey() {
        Integer[] keys = {1, 2, 3, 4, 5};
        MultiKey<Integer> multiKey = new MultiKey<>(keys);
        
        for (int i = 0; i < keys.length; i++) {
            Integer retrievedKey = multiKey.getKey(i);
            assertEquals(keys[i], retrievedKey);
        }
    }
    
    // Test equals method for equal objects
    @Test
    public void testEqualsForEqualObjects() {
        Integer[] keys1 = {1, 2, 3};
        Integer[] keys2 = {1, 2, 3};
        MultiKey<Integer> multiKey1 = new MultiKey<>(keys1);
        MultiKey<Integer> multiKey2 = new MultiKey<>(keys2);
        
        assertTrue(multiKey1.equals(multiKey2));
    }
    
    // Test equals method for non-equal objects
    @Test
    public void testEqualsForNonEqualObjects() {
        Integer[] keys1 = {1, 2, 3};
        Integer[] keys2 = {4, 5, 6};
        MultiKey<Integer> multiKey1 = new MultiKey<>(keys1);
        MultiKey<Integer> multiKey2 = new MultiKey<>(keys2);
        
        assertFalse(multiKey1.equals(multiKey2));
    }
    
    // Test hashCode method for equal objects
    @Test
    public void testHashCodeForEqualObjects() {
        Integer[] keys1 = {1, 2, 3};
        Integer[] keys2 = {1, 2, 3};
        MultiKey<Integer> multiKey1 = new MultiKey<>(keys1);
        MultiKey<Integer> multiKey2 = new MultiKey<>(keys2);
        
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }
    
    // Test hashCode method for non-equal objects
    @Test
    public void testHashCodeForNonEqualObjects() {
        Integer[] keys1 = {1, 2, 3};
        Integer[] keys2 = {4, 5, 6};
        MultiKey<Integer> multiKey1 = new MultiKey<>(keys1);
        MultiKey<Integer> multiKey2 = new MultiKey<>(keys2);
        
        assertNotEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }
    
    // Test toString method
    @Test
    public void testToString() {
        Integer[] keys = {1, 2, 3};
        MultiKey<Integer> multiKey = new MultiKey<>(keys);
        
        String expected = "MultiKey[1, 2, 3]";
        assertEquals(expected, multiKey.toString());
    }
    
    // Test readResolve method
    @Test
    public void testReadResolve() {
        Integer[] keys = {1, 2, 3};
        MultiKey<Integer> multiKey = new MultiKey<>(keys);
        
        MultiKey<Integer> deserializedMultiKey = (MultiKey<Integer>) SerializeUtils.deserialize(SerializeUtils.serialize(multiKey));
        
        assertEquals(multiKey, deserializedMultiKey);
        assertEquals(multiKey.hashCode(), deserializedMultiKey.hashCode());
    }
}