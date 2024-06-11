package org.apache.commons.collections4.keyvalue;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.collections4.keyvalue.MultiKey;

import static org.junit.Assert.*;

public class MultiKeyTest {

    private MultiKey<String> multiKey;

    @Before
    public void setUp() {
        multiKey = new MultiKey<>("key1", "key2", "key3");
    }

    @Test
    public void testGetKeys() {
        String[] keys = multiKey.getKeys();
        assertArrayEquals(new String[]{"key1", "key2", "key3"}, keys);
    }

    @Test
    public void testGetKey() {
        String key = multiKey.getKey(1);
        assertEquals("key2", key);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyIndexOutOfBounds() {
        multiKey.getKey(3);
    }

    @Test
    public void testSize() {
        int size = multiKey.size();
        assertEquals(3, size);
    }

    @Test
    public void testEquals() {
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2", "key3");
        assertEquals(multiKey, multiKey2);
    }

    @Test
    public void testNotEquals() {
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2", "key4");
        assertNotEquals(multiKey, multiKey2);
    }

    @Test
    public void testHashCode() {
        int hashCode = multiKey.hashCode();
        assertEquals(hashCode, multiKey.hashCode());
    }

    @Test
    public void testToString() {
        String str = multiKey.toString();
        assertEquals("MultiKey[key1, key2, key3]", str);
    }
}