package org.apache.commons.collections4.keyvalue;
import static org.junit.Assert.*;
import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testConstructorTwoKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
    }

    @Test
    public void testConstructorThreeKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
    }

    @Test
    public void testConstructorFourKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4");
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
        assertEquals("key4", multiKey.getKey(3));
    }

    @Test
    public void testConstructorFiveKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4", "key5");
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
        assertEquals("key4", multiKey.getKey(3));
        assertEquals("key5", multiKey.getKey(4));
    }

    @Test
    public void testConstructorCloneKeys() {
        String[] keys = {"key1", "key2"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        keys[0] = "changed";
        assertNotEquals("changed", multiKey.getKey(0));
    }

    @Test
    public void testEquals() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
        assertTrue(multiKey1.equals(multiKey2));
    }

    @Test
    public void testHashCode() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    @Test
    public void testToString() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKeysConstructor() {
        new MultiKey<>(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyInvalidIndex() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        multiKey.getKey(2);
    }

    @Test
    public void testSize() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        assertEquals(2, multiKey.size());
    }

}