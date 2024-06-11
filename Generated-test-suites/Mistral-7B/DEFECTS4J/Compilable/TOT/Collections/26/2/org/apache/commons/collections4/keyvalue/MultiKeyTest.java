package org.apache.commons.collections4.keyvalue;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for MultiKey class.
 */
public class MultiKeyTest {

    /**
     * Test constructor with two keys.
     */
    @Test
    public void testTwoKeys() {
        final String key1 = "key1";
        final String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        String[] expectedKeys = new String[]{"key1", "key2"};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    /**
     * Test constructor with three keys.
     */
    @Test
    public void testThreeKeys() {
        final String key1 = "key1";
        final String key2 = "key2";
        final String key3 = "key3";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);
        String[] expectedKeys = new String[]{"key1", "key2", "key3"};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    /**
     * Test constructor with four keys.
     */
    @Test
    public void testFourKeys() {
        final String key1 = "key1";
        final String key2 = "key2";
        final String key3 = "key3";
        final String key4 = "key4";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4);
        String[] expectedKeys = new String[]{"key1", "key2", "key3", "key4"};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    /**
     * Test constructor with an array of keys.
     */
    @Test
    public void testArrayOfKeys() {
        final String key1 = "key1";
        final String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(new String[]{key1, key2});
        String[] expectedKeys = new String[]{"key1", "key2"};
        assertArrayEquals(expectedKeys, multiKey.getKeys());
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals() {
        final String key1 = "key1";
        final String key2 = "key2";
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey3 = new MultiKey<>(key1, "key2"); // different String object
        MultiKey<String> multiKey4 = new MultiKey<>(key2, key1); // different order
        MultiKey<String> multiKey5 = new MultiKey<>(new String[]{"key1", "key2"});
        assertTrue(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(multiKey3));
        assertFalse(multiKey1.equals(multiKey4));
        assertFalse(multiKey1.equals(multiKey5));
    }

    /**
     * Test hashCode method.
     */
    @Test
    public void testHashCode() {
        final String key1 = "key1";
        final String key2 = "key2";
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    /**
     * Test getKey method.
     */
    @Test
    public void testGetKey() {
        final String key1 = "key1";
        final String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
    }

    /**
     * Test size method.
     */
    @Test
    public void testSize() {
        final String key1 = "key1";
        final String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        assertEquals(2, multiKey.size());
    }
}