package org.apache.commons.collections4.keyvalue;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.collections4.keyvalue.MultiKey;

import static org.junit.Assert.*;

public class MultiKeyTest {

    private MultiKey<String> multiKey;

    @Before
    public void setUp() {
        multiKey = new MultiKey<>("key1", "key2");
    }

    @Test
    public void testConstructor_TwoKeys_Success() {
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
    }

    @Test
    public void testConstructor_ThreeKeys_Success() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
    }

    @Test
    public void testConstructor_FourKeys_Success() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4");
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
        assertEquals("key4", multiKey.getKey(3));
    }

    @Test
    public void testConstructor_FiveKeys_Success() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4", "key5");
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
        assertEquals("key4", multiKey.getKey(3));
        assertEquals("key5", multiKey.getKey(4));
    }

    @Test
    public void testConstructor_ArrayKeys_Success() {
        String[] keys = {"key1", "key2", "key3", "key4", "key5"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
        assertEquals("key4", multiKey.getKey(3));
        assertEquals("key5", multiKey.getKey(4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullArray_Failure() {
        MultiKey<String> multiKey = new MultiKey<>(null);
    }

    @Test
    public void testEquals_SameInstance_Success() {
        assertTrue(multiKey.equals(multiKey));
    }
    
    @Test
    public void testEquals_MultiKeyWithSameKeys_Success() {
        MultiKey<String> other = new MultiKey<>("key1", "key2");
        assertTrue(multiKey.equals(other));
    }
    
    @Test
    public void testEquals_MultiKeyWithDifferentKeys_Failure() {
        MultiKey<String> other = new MultiKey<>("key1", "key3");
        assertFalse(multiKey.equals(other));
    }
    
    @Test
    public void testEquals_NonMultiKeyObject_Failure() {
        Object obj = new Object();
        assertFalse(multiKey.equals(obj));
    }
    
    @Test
    public void testHashCode_SameKeys_Success() {
        MultiKey<String> other = new MultiKey<>("key1", "key2");
        assertEquals(multiKey.hashCode(), other.hashCode());
    }
    
    @Test
    public void testHashCode_DifferentKeys_Failure() {
        MultiKey<String> other = new MultiKey<>("key1", "key3");
        assertNotEquals(multiKey.hashCode(), other.hashCode());
    }
    
    @Test
    public void testToString_Success() {
        String expected = "MultiKey[key1, key2]";
        assertEquals(expected, multiKey.toString());
    }
    
    @Test
    public void testSize_Success() {
        assertEquals(2, multiKey.size());
    }
    
    @Test
    public void testGetKeys_Success() {
        String[] expected = {"key1", "key2"};
        assertArrayEquals(expected, multiKey.getKeys());
    }
}