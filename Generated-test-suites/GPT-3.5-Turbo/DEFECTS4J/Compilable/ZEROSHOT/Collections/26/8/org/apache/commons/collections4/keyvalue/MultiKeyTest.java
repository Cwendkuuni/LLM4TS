package org.apache.commons.collections4.keyvalue;
import org.junit.Test;
import org.apache.commons.collections4.keyvalue.MultiKey;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testGetKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        String[] keys = multiKey.getKeys();
        assertEquals(3, keys.length);
        assertArrayEquals(new String[]{"key1", "key2", "key3"}, keys);
    }

    @Test
    public void testGetKey() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        assertEquals("key1", multiKey.getKey(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKey_IndexOutOfBounds() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        multiKey.getKey(3);
    }

    @Test
    public void testSize() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        assertEquals(3, multiKey.size());
    }

    @Test
    public void testEquals_SameObject() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        assertTrue(multiKey.equals(multiKey));
    }

    @Test
    public void testEquals_NullObject() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        assertFalse(multiKey.equals(null));
    }

    @Test
    public void testEquals_DifferentClass() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        assertFalse(multiKey.equals("key1"));
    }

    @Test
    public void testEquals_SameKeys() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2", "key3");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2", "key3");
        assertTrue(multiKey1.equals(multiKey2));
    }

    @Test
    public void testEquals_DifferentKeys() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2", "key3");
        MultiKey<String> multiKey2 = new MultiKey<>("key4", "key5", "key6");
        assertFalse(multiKey1.equals(multiKey2));
    }

    @Test
    public void testHashCode() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2", "key3");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2", "key3");
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    @Test
    public void testToString() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        assertEquals("MultiKey[key1, key2, key3]", multiKey.toString());
    }
}