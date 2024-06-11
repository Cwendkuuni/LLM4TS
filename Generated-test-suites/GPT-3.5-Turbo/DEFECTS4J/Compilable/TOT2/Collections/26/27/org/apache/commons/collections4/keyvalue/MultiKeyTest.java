package org.apache.commons.collections4.keyvalue;
import static org.junit.Assert.*;
import org.junit.Test;

public class MultiKeyTest {

    // Test cases for constructor taking two keys
    @Test
    public void testConstructorTwoKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        assertEquals(2, multiKey.size());
    }

    // Test cases for constructor taking three keys
    @Test
    public void testConstructorThreeKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        assertEquals(3, multiKey.size());
    }

    // Test cases for constructor taking four keys
    @Test
    public void testConstructorFourKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4");
        assertEquals(4, multiKey.size());
    }

    // Test cases for constructor taking five keys
    @Test
    public void testConstructorFiveKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4", "key5");
        assertEquals(5, multiKey.size());
    }

    // Test case for constructor taking an array of keys
    @Test
    public void testConstructorArrayKeys() {
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        assertEquals(3, multiKey.size());
    }

    // Test case for getKeys method
    @Test
    public void testGetKeys() {
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        assertArrayEquals(keys, multiKey.getKeys());
    }

    // Test case for getKey method
    @Test
    public void testGetKey() {
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        assertEquals("key2", multiKey.getKey(1));
    }

    // Test case for equals method
    @Test
    public void testEquals() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
        assertTrue(multiKey1.equals(multiKey2));
    }

    // Test case for hashCode method
    @Test
    public void testHashCode() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        int expectedHashCode = "key1".hashCode() ^ "key2".hashCode();
        assertEquals(expectedHashCode, multiKey.hashCode());
    }

    // Test case for toString method
    @Test
    public void testToString() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }

}