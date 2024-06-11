package org.apache.commons.collections4.keyvalue;

import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {
    // public method: MultiKey.getKeys() 
    @Test
    public void testGetKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        String[] keys = multiKey.getKeys();
        assertEquals("key1", keys[0]);
        assertEquals("key2", keys[1]);
        assertEquals("key3", keys[2]);
    }

    // public method: MultiKey.getKey(int index)
    @Test
    public void testGetKey() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
        assertEquals(Integer.valueOf(1), multiKey.getKey(0));
        assertEquals(Integer.valueOf(2), multiKey.getKey(1));
        assertEquals(Integer.valueOf(3), multiKey.getKey(2));
    }

    // public method: MultiKey.size()
    @Test
    public void testSize() {
        MultiKey<Character> multiKey = new MultiKey<>('a', 'b', 'c');
        assertEquals(3, multiKey.size());
    }

    // public method: MultiKey.equals(Object other)
    @Test
    public void testEquals() {
        MultiKey<Integer> multiKey1 = new MultiKey<>(1, 2);
        MultiKey<Integer> multiKey2 = new MultiKey<>(1, 2);
        MultiKey<Integer> multiKey3 = new MultiKey<>(2, 1);

        assertTrue(multiKey1.equals(multiKey1));
        assertTrue(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(multiKey3));
    }

    // public method: MultiKey.hashCode()
    @Test
    public void testHashCode() {
        MultiKey<Integer> multiKey1 = new MultiKey<>(1, 2);
        MultiKey<Integer> multiKey2 = new MultiKey<>(1, 2);
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    // public method: MultiKey.toString()
    @Test
    public void testToString() {
        MultiKey<String> multiKey = new MultiKey<>("one", "two", "three");
        assertEquals("MultiKey[one, two, three]", multiKey.toString());
    }
}
