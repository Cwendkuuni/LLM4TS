package org.apache.commons.collections4.keyvalue;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {
    
    private MultiKey<String> multiKey;
    
    @Before
    public void setup() {
        multiKey = new MultiKey<>("key1", "key2", "key3");
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(multiKey);
    }
    
    @Test
    public void testGetKeys() {
        String[] expectedKeys = {"key1", "key2", "key3"};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }
    
    @Test
    public void testGetKey() {
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyWithInvalidIndex() {
        multiKey.getKey(3);
    }
    
    @Test
    public void testSize() {
        assertEquals(3, multiKey.size());
    }
    
    @Test
    public void testEquals() {
        MultiKey<String> sameKey = new MultiKey<>("key1", "key2", "key3");
        MultiKey<String> differentKey = new MultiKey<>("key1", "key2", "key4");
        
        assertTrue(multiKey.equals(sameKey));
        assertFalse(multiKey.equals(differentKey));
    }
    
    @Test
    public void testHashCode() {
        MultiKey<String> sameKey = new MultiKey<>("key1", "key2", "key3");
        
        assertEquals(multiKey.hashCode(), sameKey.hashCode());
    }
    
    @Test
    public void testToString() {
        String expectedString = "MultiKey[key1, key2, key3]";
        
        assertEquals(expectedString, multiKey.toString());
    }
}