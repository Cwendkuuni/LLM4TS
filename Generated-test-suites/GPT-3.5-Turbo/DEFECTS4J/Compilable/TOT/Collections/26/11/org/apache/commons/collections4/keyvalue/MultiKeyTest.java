package org.apache.commons.collections4.keyvalue;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testConstructorWithTwoKeys() {
        String key1 = "key1";
        String key2 = "key2";

        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        assertEquals(2, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
    }

    @Test
    public void testConstructorWithThreeKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";

        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);
        assertEquals(3, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
    }

    @Test
    public void testConstructorWithFourKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";

        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4);
        assertEquals(4, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
    }

    @Test
    public void testConstructorWithFiveKeys() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        String key5 = "key5";

        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        assertEquals(5, multiKey.size());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(key5, multiKey.getKey(4));
    }

    @Test
    public void testConstructorWithArray() {
        String[] keys = {"key1", "key2", "key3"};

        MultiKey<String> multiKey = new MultiKey<>(keys);
        assertEquals(3, multiKey.size());
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals("key3", multiKey.getKey(2));
    }

    @Test
    public void testConstructorWithNullKeys() {
        try {
            MultiKey<String> multiKey = new MultiKey<>(null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testEquals() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey3 = new MultiKey<>("key2", "key1");

        assertTrue(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(multiKey3));
    }

    @Test
    public void testHashCode() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
        MultiKey<String> multiKey3 = new MultiKey<>("key2", "key1");

        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
        assertNotEquals(multiKey1.hashCode(), multiKey3.hashCode());
    }

    @Test
    public void testToString() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");

        assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }

}