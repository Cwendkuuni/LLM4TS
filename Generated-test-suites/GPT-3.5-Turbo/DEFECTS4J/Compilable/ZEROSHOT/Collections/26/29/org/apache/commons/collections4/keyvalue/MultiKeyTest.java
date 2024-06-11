package org.apache.commons.collections4.keyvalue;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MultiKeyTest {

    private MultiKey<String> multiKey;

    @Before
    public void setUp() {
        multiKey = new MultiKey<String>("key1", "key2");
    }

    @Test
    public void testGetKeys() {
        String[] expected = {"key1", "key2"};
        assertArrayEquals(expected, multiKey.getKeys());
    }

    @Test
    public void testGetKey() {
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
    }

    @Test
    public void testSize() {
        assertEquals(2, multiKey.size());
    }

    @Test
    public void testEquals() {
        MultiKey<String> otherMultiKey = new MultiKey<String>("key1", "key2");
        assertTrue(multiKey.equals(otherMultiKey));
        
        MultiKey<String> differentMultiKey = new MultiKey<String>("key1", "key3");
        assertFalse(multiKey.equals(differentMultiKey));
        
        assertFalse(multiKey.equals(null));
        assertFalse(multiKey.equals("key1,key2"));
    }

    @Test
    public void testHashCode() {
        MultiKey<String> otherMultiKey = new MultiKey<String>("key1", "key2");
        assertEquals(multiKey.hashCode(), otherMultiKey.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }
}