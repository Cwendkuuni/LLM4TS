package org.apache.commons.collections4.keyvalue;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testConstructor_TwoKeys() {
        MultiKey<String> key = new MultiKey<String>("key1", "key2");
        String[] expectedKeys = {"key1", "key2"};
        assertArrayEquals(expectedKeys, key.getKeys());
    }

    @Test
    public void testConstructor_ThreeKeys() {
        MultiKey<String> key = new MultiKey<String>("key1", "key2", "key3");
        String[] expectedKeys = {"key1", "key2", "key3"};
        assertArrayEquals(expectedKeys, key.getKeys());
    }

    @Test
    public void testConstructor_FourKeys() {
        MultiKey<String> key = new MultiKey<String>("key1", "key2", "key3", "key4");
        String[] expectedKeys = {"key1", "key2", "key3", "key4"};
        assertArrayEquals(expectedKeys, key.getKeys());
    }

    @Test
    public void testConstructor_FiveKeys() {
        MultiKey<String> key = new MultiKey<String>("key1", "key2", "key3", "key4", "key5");
        String[] expectedKeys = {"key1", "key2", "key3", "key4", "key5"};
        assertArrayEquals(expectedKeys, key.getKeys());
    }

    @Test
    public void testConstructor_KeyArray() {
        String[] keysArray = {"key1", "key2", "key3"};
        MultiKey<String> key = new MultiKey<String>(keysArray);
        assertArrayEquals(keysArray, key.getKeys());
        keysArray[0] = "newKey";
        assertFalse(keysArray[0].equals(key.getKey(0)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullKeysArray() {
        MultiKey<String> key = new MultiKey<String>(null);
    }

    @Test
    public void testGetSize() {
        MultiKey<String> key = new MultiKey<String>("key1", "key2", "key3");
        assertEquals(3, key.size());
    }

    @Test
    public void testGetKey() {
        MultiKey<String> key = new MultiKey<String>("key1", "key2", "key3");
        assertEquals("key2", key.getKey(1));
    }

    @Test
    public void testEquals_SameObject() {
        MultiKey<String> key = new MultiKey<String>("key1", "key2");
        assertTrue(key.equals(key));
    }

    @Test
    public void testEquals_SameKeys() {
        MultiKey<String> key1 = new MultiKey<String>("key1", "key2");
        MultiKey<String> key2 = new MultiKey<String>("key1", "key2");
        assertTrue(key1.equals(key2));
    }

    @Test
    public void testEquals_DifferentKeys() {
        MultiKey<String> key1 = new MultiKey<String>("key1", "key2");
        MultiKey<String> key2 = new MultiKey<String>("key3", "key4");
        assertFalse(key1.equals(key2));
    }

    @Test
    public void testEquals_DifferentClass() {
        MultiKey<String> key = new MultiKey<String>("key1", "key2");
        assertFalse(key.equals("key1,key2"));
    }

    @Test
    public void testHashCode_SameKeys() {
        MultiKey<String> key1 = new MultiKey<String>("key1", "key2");
        MultiKey<String> key2 = new MultiKey<String>("key1", "key2");
        assertEquals(key1.hashCode(), key2.hashCode());
    }

    @Test
    public void testToString() {
        MultiKey<String> key = new MultiKey<String>("key1", "key2");
        assertEquals("MultiKey[key1, key2]", key.toString());
    }
}