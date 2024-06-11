package org.apache.commons.collections4.keyvalue;
import static org.junit.Assert.*;
import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testConstructorWithTwoKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        assertEquals("key1", multiKey.getKeys()[0]);
        assertEquals("key2", multiKey.getKeys()[1]);
    }

    @Test
    public void testConstructorWithThreeKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        assertEquals("key1", multiKey.getKeys()[0]);
        assertEquals("key2", multiKey.getKeys()[1]);
        assertEquals("key3", multiKey.getKeys()[2]);
    }

    @Test
    public void testConstructorWithFourKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4");
        assertEquals("key1", multiKey.getKeys()[0]);
        assertEquals("key2", multiKey.getKeys()[1]);
        assertEquals("key3", multiKey.getKeys()[2]);
        assertEquals("key4", multiKey.getKeys()[3]);
    }

    @Test
    public void testConstructorWithFiveKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4", "key5");
        assertEquals("key1", multiKey.getKeys()[0]);
        assertEquals("key2", multiKey.getKeys()[1]);
        assertEquals("key3", multiKey.getKeys()[2]);
        assertEquals("key4", multiKey.getKeys()[3]);
        assertEquals("key5", multiKey.getKeys()[4]);
    }

    @Test
    public void testConstructorWithArray() {
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        assertArrayEquals(keys, multiKey.getKeys());
    }

    @Test
    public void testGetKeys() {
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        assertArrayEquals(keys, multiKey.getKeys());
    }

    @Test
    public void testGetKey() {
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
    }

    @Test
    public void testSize() {
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        assertEquals(3, multiKey.size());
    }

    @Test
    public void testEquals() {
        String[] keys1 = {"key1", "key2"};
        String[] keys2 = {"key1", "key2"};
        MultiKey<String> multiKey1 = new MultiKey<>(keys1);
        MultiKey<String> multiKey2 = new MultiKey<>(keys2);
        assertTrue(multiKey1.equals(multiKey2));
        assertTrue(multiKey2.equals(multiKey1));
    }

    @Test
    public void testHashCode() {
        String[] keys1 = {"key1", "key2"};
        String[] keys2 = {"key1", "key2"};
        MultiKey<String> multiKey1 = new MultiKey<>(keys1);
        MultiKey<String> multiKey2 = new MultiKey<>(keys2);
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    @Test
    public void testToString() {
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        assertEquals("MultiKey[key1, key2, key3]", multiKey.toString());
    }
}