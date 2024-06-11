import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testConstructor_TwoKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        
        String[] expectedKeys = { "key1", "key2" };
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testConstructor_ThreeKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        
        String[] expectedKeys = { "key1", "key2", "key3" };
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testConstructor_FourKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4");
        
        String[] expectedKeys = { "key1", "key2", "key3", "key4" };
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    @Test
    public void testConstructor_FiveKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4", "key5");
        
        String[] expectedKeys = { "key1", "key2", "key3", "key4", "key5" };
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }
    
    @Test
    public void testConstructor_ArrayKeys_Cloned() {
        String[] keys = { "key1", "key2", "key3" };
        MultiKey<String> multiKey = new MultiKey<>(keys);
        
        // Modify the keys array
        keys[0] = "modified_key1";
        
        String[] expectedKeys = { "key1", "key2", "key3" };
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullKeyArray() {
        MultiKey<String> multiKey = new MultiKey<>(null);
    }

    @Test
    public void testEquals_SameInstance() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        
        assertTrue(multiKey.equals(multiKey));
    }
    
    @Test
    public void testEquals_SameKeys() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
        
        assertTrue(multiKey1.equals(multiKey2));
    }
    
    @Test
    public void testEquals_DifferentKeys() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key3", "key4");
        
        assertFalse(multiKey1.equals(multiKey2));
    }
    
    @Test
    public void testEquals_SameClassDifferentTypes() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<Integer> multiKey2 = new MultiKey<>(1, 2);
        
        assertFalse(multiKey1.equals(multiKey2));
    }
    
    @Test
    public void testEquals_Null() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        
        assertFalse(multiKey.equals(null));
    }
    
    @Test
    public void testEquals_DifferentClass() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        
        assertFalse(multiKey.equals("key1,key2"));
    }
    
    @Test
    public void testHashCode_SameKeys() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
        
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }
    
    @Test
    public void testHashCode_DifferentKeys() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key3", "key4");
        
        assertNotEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }
    
    @Test
    public void testToString() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        
        assertEquals("MultiKey[key1, key2, key3]", multiKey.toString());
    }
    
    @Test
    public void testGetSize() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        
        assertEquals(3, multiKey.size());
    }
    
    @Test
    public void testGetKey_ValidIndex() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKey_InvalidIndex() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        
        multiKey.getKey(3);
    }
    
    @Test
    public void testReadWriteResolve() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        
        assertSame(multiKey, multiKey.readResolve());
    }
}