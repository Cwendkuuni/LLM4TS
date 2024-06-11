import org.junit.*;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testConstructor_TwoKeys() {
        String key1 = "key1";
        String key2 = "key2";
        
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        
        String[] expectedKeys = {key1, key2};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }
    
    @Test
    public void testConstructor_ThreeKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";

        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);

        String[] expectedKeys = {key1, key2, key3};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }
    
    @Test
    public void testConstructor_FourKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";

        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4);
        
        String[] expectedKeys = {key1, key2, key3, key4};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }
    
    @Test
    public void testConstructor_FiveKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        String key5 = "key5";

        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        
        String[] expectedKeys = {key1, key2, key3, key4, key5};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }
    
    @Test
    public void testConstructor_Array() {
        String[] keys = {"key1", "key2", "key3"};
        
        MultiKey<String> multiKey = new MultiKey<>(keys);
        
        assertArrayEquals(keys, multiKey.getKeys());
    }
    
    @Test
    public void testConstructor_Array_Clone() {
        String[] keys = {"key1", "key2", "key3"};
        
        MultiKey<String> multiKey = new MultiKey<>(keys, true);
        
        assertArrayEquals(keys, multiKey.getKeys());
        assertNotSame(keys, multiKey.getKeys());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_NullArray() {
        String[] keys = null;
        
        MultiKey<String> multiKey = new MultiKey<>(keys);
    }
    
    @Test
    public void testGetKey() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);
        
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void testGetKey_IndexOutOfBounds() {
        String key1 = "key1";
        String key2 = "key2";
        
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        
        multiKey.getKey(2);
    }
    
    @Test
    public void testSize() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);
        
        assertEquals(3, multiKey.size());
    }
    
    @Test
    public void testEquals_SameInstance() {
        String key1 = "key1";
        String key2 = "key2";
        
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        
        assertTrue(multiKey.equals(multiKey));
    }
    
    @Test
    public void testEquals_SameKeys() {
        String key1 = "key1";
        String key2 = "key2";
        
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);
        
        assertTrue(multiKey1.equals(multiKey2));
    }
    
    @Test
    public void testEquals_DifferentKeys() {
        String key1 = "key1";
        String key2 = "key2";
        
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key2, key1);
        
        assertFalse(multiKey1.equals(multiKey2));
    }
    
    @Test
    public void testEquals_DifferentTypes() {
        String key1 = "key1";
        
        MultiKey<String> multiKey = new MultiKey<>(key1);
        
        assertFalse(multiKey.equals(key1));
    }
    
    @Test
    public void testHashCode_SameKeys() {
        String key1 = "key1";
        String key2 = "key2";
        
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);
        
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }
    
    @Test
    public void testHashCode_DifferentKeys() {
        String key1 = "key1";
        String key2 = "key2";
        
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key2, key1);
        
        assertNotEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }
    
    @Test
    public void testToString() {
        String key1 = "key1";
        String key2 = "key2";
        
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        
        String expectedToString = "MultiKey[key1, key2]";
        assertEquals(expectedToString, multiKey.toString());
    }
    
    @Test
    public void testReadResolve() {
        String key1 = "key1";
        String key2 = "key2";
        
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        
        MultiKey<String> deserializedMultiKey = (MultiKey<String>) SerializationUtils.deserialize(SerializationUtils.serialize(multiKey));
        
        assertEquals(multiKey, deserializedMultiKey);
        assertEquals(multiKey.hashCode(), deserializedMultiKey.hashCode());
    }
}