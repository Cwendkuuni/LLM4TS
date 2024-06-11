package org.apache.commons.collections4.keyvalue;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MultiKeyTest {
    private MultiKey<String> multiKey;
    
    @Before
    public void setUp() {
        multiKey = new MultiKey<>("key1", "key2", "key3");
    }
    
    @Test
    public void testConstructor_withTwoKeys_shouldCreateMultiKeyWithTwoKeys() {
        MultiKey<String> twoKeyMultiKey = new MultiKey<>("key1", "key2");
        
        assertEquals(2, twoKeyMultiKey.size());
        assertEquals("key1", twoKeyMultiKey.getKey(0));
        assertEquals("key2", twoKeyMultiKey.getKey(1));
    }
    
    @Test
    public void testConstructor_withThreeKeys_shouldCreateMultiKeyWithThreeKeys() {
        MultiKey<String> threeKeyMultiKey = new MultiKey<>("key1", "key2", "key3");
        
        assertEquals(3, threeKeyMultiKey.size());
        assertEquals("key1", threeKeyMultiKey.getKey(0));
        assertEquals("key2", threeKeyMultiKey.getKey(1));
        assertEquals("key3", threeKeyMultiKey.getKey(2));
    }
    
    @Test
    public void testConstructor_withFourKeys_shouldCreateMultiKeyWithFourKeys() {
        MultiKey<String> fourKeyMultiKey = new MultiKey<>("key1", "key2", "key3", "key4");
        
        assertEquals(4, fourKeyMultiKey.size());
        assertEquals("key1", fourKeyMultiKey.getKey(0));
        assertEquals("key2", fourKeyMultiKey.getKey(1));
        assertEquals("key3", fourKeyMultiKey.getKey(2));
        assertEquals("key4", fourKeyMultiKey.getKey(3));
    }
    
    @Test
    public void testConstructor_withFiveKeys_shouldCreateMultiKeyWithFiveKeys() {
        MultiKey<String> fiveKeyMultiKey = new MultiKey<>("key1", "key2", "key3", "key4", "key5");
        
        assertEquals(5, fiveKeyMultiKey.size());
        assertEquals("key1", fiveKeyMultiKey.getKey(0));
        assertEquals("key2", fiveKeyMultiKey.getKey(1));
        assertEquals("key3", fiveKeyMultiKey.getKey(2));
        assertEquals("key4", fiveKeyMultiKey.getKey(3));
        assertEquals("key5", fiveKeyMultiKey.getKey(4));
    }
    
    @Test
    public void testConstructor_withArray_shouldCreateMultiKeyWithArray() {
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> arrayMultiKey = new MultiKey<>(keys);
        
        assertEquals(3, arrayMultiKey.size());
        assertEquals("key1", arrayMultiKey.getKey(0));
        assertEquals("key2", arrayMultiKey.getKey(1));
        assertEquals("key3", arrayMultiKey.getKey(2));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_withNullArray_shouldThrowIllegalArgumentException() {
        String[] keys = null;
        new MultiKey<>(keys);
    }
    
    @Test
    public void testGetKeys_shouldReturnArrayOfKeys() {
        String[] expectedKeys = {"key1", "key2", "key3"};
        
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }
    
    @Test
    public void testGetKey_withValidIndex_shouldReturnKey() {
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKey_withInvalidIndex_shouldThrowIndexOutOfBoundsException() {
        multiKey.getKey(3);
    }
    
    @Test
    public void testSize_shouldReturnSizeOfListOfKeys() {
        assertEquals(3, multiKey.size());
    }
    
    @Test
    public void testEquals_withSameObject_shouldReturnTrue() {
        assertTrue(multiKey.equals(multiKey));
    }
    
    @Test
    public void testEquals_withNullObject_shouldReturnFalse() {
        assertFalse(multiKey.equals(null));
    }
    
    @Test
    public void testEquals_withDifferentClassObject_shouldReturnFalse() {
        assertFalse(multiKey.equals("key1"));
    }
    
    @Test
    public void testEquals_withEqualMultiKey_shouldReturnTrue() {
        MultiKey<String> equalMultiKey = new MultiKey<>("key1", "key2", "key3");
        
        assertTrue(multiKey.equals(equalMultiKey));
    }
    
    @Test
    public void testEquals_withNotEqualMultiKey_shouldReturnFalse() {
        MultiKey<String> notEqualMultiKey = new MultiKey<>("key4", "key5", "key6");
        
        assertFalse(multiKey.equals(notEqualMultiKey));
    }
    
    @Test
    public void testHashCode_withEqualMultiKey_shouldReturnSameHashCode() {
        MultiKey<String> equalMultiKey = new MultiKey<>("key1", "key2", "key3");
        
        assertEquals(multiKey.hashCode(), equalMultiKey.hashCode());
    }
    
    @Test
    public void testHashCode_withNotEqualMultiKey_shouldReturnDifferentHashCode() {
        MultiKey<String> notEqualMultiKey = new MultiKey<>("key4", "key5", "key6");
        
        assertNotEquals(multiKey.hashCode(), notEqualMultiKey.hashCode());
    }
    
    @Test
    public void testToString_shouldReturnStringRepresentationOfMultiKey() {
        String expectedString = "MultiKey[key1, key2, key3]";
        
        assertEquals(expectedString, multiKey.toString());
    }
    
}