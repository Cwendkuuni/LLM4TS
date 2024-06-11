package org.apache.commons.collections4.keyvalue;

import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testConstructorTwoKeys() {
        // create a MultiKey object with two keys
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        
        // check if the MultiKey object is not null
        assertNotNull(multiKey);
        
        // check if the keys are retrieved correctly
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
    }

    @Test
    public void testConstructorThreeKeys() {
        // create a MultiKey object with three keys
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        
        // check if the MultiKey object is not null
        assertNotNull(multiKey);
        
        // check if the keys are retrieved correctly
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
    }

    @Test
    public void testConstructorFourKeys() {
        // create a MultiKey object with four keys
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4");
        
        // check if the MultiKey object is not null
        assertNotNull(multiKey);
        
        // check if the keys are retrieved correctly
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
        assertEquals("key4", multiKey.getKey(3));
    }

    @Test
    public void testConstructorFiveKeys() {
        // create a MultiKey object with five keys
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4", "key5");
        
        // check if the MultiKey object is not null
        assertNotNull(multiKey);
        
        // check if the keys are retrieved correctly
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
        assertEquals("key4", multiKey.getKey(3));
        assertEquals("key5", multiKey.getKey(4));
    }

    @Test
    public void testGetKeys() {
        // create a MultiKey object with two keys
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        
        // check if the keys array is retrieved correctly
        assertArrayEquals(new String[]{"key1", "key2"}, multiKey.getKeys());
    }

    @Test
    public void testSize() {
        // create a MultiKey object with three keys
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        
        // check if the size of keys array is retrieved correctly
        assertEquals(3, multiKey.size());
    }

    @Test
    public void testEquals() {
        // create two MultiKey objects with the same keys
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
        
        // check if the objects are equal
        assertTrue(multiKey1.equals(multiKey2));
    }

    @Test
    public void testHashCode() {
        // create a MultiKey object with two keys
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        
        // check if the hash code is calculated correctly
        assertEquals(multiKey.getKey(0).hashCode() ^ multiKey.getKey(1).hashCode(), multiKey.hashCode());
    }
}