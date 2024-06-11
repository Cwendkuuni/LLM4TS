package org.apache.commons.collections4.keyvalue;

import static org.junit.Assert.assertEquals; 
import static org.junit.Assert.assertArrayEquals; 
import static org.junit.Assert.assertFalse; 
import static org.junit.Assert.assertTrue; 

import org.junit.Test;

public class MultiKeyTest {
    @Test
    public void testConstructorWithTwoKeys() {
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        assertEquals(key1, multiKey.getKeys()[0]);
        assertEquals(key2, multiKey.getKeys()[1]);
        assertEquals(2, multiKey.size());
    }

    @Test
    public void testConstructorWithThreeKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);
        assertEquals(key1, multiKey.getKeys()[0]);
        assertEquals(key2, multiKey.getKeys()[1]);
        assertEquals(key3, multiKey.getKeys()[2]);
        assertEquals(3, multiKey.size());
    }

    @Test
    public void testConstructorWithFourKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4);
        assertEquals(key1, multiKey.getKeys()[0]);
        assertEquals(key2, multiKey.getKeys()[1]);
        assertEquals(key3, multiKey.getKeys()[2]);
        assertEquals(key4, multiKey.getKeys()[3]);
        assertEquals(4, multiKey.size());
    }
    
    @Test
    public void testConstructorWithFiveKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        String key5 = "key5";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        assertEquals(key1, multiKey.getKeys()[0]);
        assertEquals(key2, multiKey.getKeys()[1]);
        assertEquals(key3, multiKey.getKeys()[2]);
        assertEquals(key4, multiKey.getKeys()[3]);
        assertEquals(key5, multiKey.getKeys()[4]);
        assertEquals(5, multiKey.size());
    }
    
    @Test
    public void testConstructorWithArrayKeys() {
        String[] keys = new String[] {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        assertArrayEquals(keys, multiKey.getKeys());
        assertEquals(3, multiKey.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullKeys() {
        MultiKey<String> multiKey = new MultiKey<>(null);
    }

    @Test
    public void testEquals() {
        String key1 = "key1";
        String key2 = "key2";
        String[] keys1 = new String[] {key1, key2};
        String[] keys2 = new String[] {key1, key2};
        String[] keys3 = new String[] {key1, "key3"};
        MultiKey<String> multiKey1 = new MultiKey<>(keys1);
        MultiKey<String> multiKey2 = new MultiKey<>(keys1);
        MultiKey<String> multiKey3 = new MultiKey<>(keys2);
        MultiKey<String> multiKey4 = new MultiKey<>(keys3);
        assertTrue(multiKey1.equals(multiKey2));
        assertTrue(multiKey2.equals(multiKey1));
        assertTrue(multiKey1.equals(multiKey3));
        assertFalse(multiKey1.equals(multiKey4));
    }

    @Test
    public void testHashCode() {
        String key1 = "key1";
        String key2 = "key2";
        String[] keys1 = new String[] {key1, key2};
        String[] keys2 = new String[] {key1, key2};
        MultiKey<String> multiKey1 = new MultiKey<>(keys1);
        MultiKey<String> multiKey2 = new MultiKey<>(keys1);
        MultiKey<String> multiKey3 = new MultiKey<>(keys2);
        MultiKey<String> multiKey4 = new MultiKey<>(new String[] {"key1", "key3"});
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
        assertEquals(multiKey1.hashCode(), multiKey3.hashCode());
        assertNotEquals(multiKey1.hashCode(), multiKey4.hashCode());
    }

    @Test
    public void testToString() {
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }
}