package org.apache.commons.collections4.keyvalue;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    // Test case for constructor with two keys
    @Test
    public void testConstructorTwoKeys() {
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        assertEquals(2, multiKey.size()); // typical case
        assertEquals(key1, multiKey.getKey(0)); // typical case
        assertEquals(key2, multiKey.getKey(1)); // typical case
    }

    // Test case for constructor with three keys
    @Test
    public void testConstructorThreeKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        Integer key3 = 3;
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2, key3);
        assertEquals(3, multiKey.size()); // typical case
        assertEquals(key1, multiKey.getKey(0)); // typical case
        assertEquals(key2, multiKey.getKey(1)); // typical case
        assertEquals(key3, multiKey.getKey(2)); // typical case
    }

    // Test case for constructor with four keys
    @Test
    public void testConstructorFourKeys() {
        Boolean key1 = true;
        Boolean key2 = false;
        Boolean key3 = true;
        Boolean key4 = false;
        MultiKey<Boolean> multiKey = new MultiKey<>(key1, key2, key3, key4);
        assertEquals(4, multiKey.size()); // typical case
        assertEquals(key1, multiKey.getKey(0)); // typical case
        assertEquals(key2, multiKey.getKey(1)); // typical case
        assertEquals(key3, multiKey.getKey(2)); // typical case
        assertEquals(key4, multiKey.getKey(3)); // typical case
    }

    // Test case for constructor with five keys
    @Test
    public void testConstructorFiveKeys() {
        Double key1 = 1.0;
        Double key2 = 2.0;
        Double key3 = 3.0;
        Double key4 = 4.0;
        Double key5 = 5.0;
        MultiKey<Double> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        assertEquals(5, multiKey.size()); // typical case
        assertEquals(key5, multiKey.getKey(4)); // typical case
    }

    // Test case for constructor with array of keys
    @Test
    public void testConstructorWithArray() {
        String[] keys = {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keys);
        assertEquals(3, multiKey.size()); // typical case
        assertEquals(keys[0], multiKey.getKey(0)); // typical case
        assertEquals(keys[2], multiKey.getKey(2)); // typical case
    }

    // Test case for constructor with null key array
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullArray() {
        MultiKey<Object> multiKey = new MultiKey<>(null); // error scenario
    }

    // Test case for equals method
    @Test
    public void testEquals() {
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey3 = new MultiKey<>(key2, key1);
        assertTrue(multiKey1.equals(multiKey1)); // typical case
        assertTrue(multiKey1.equals(multiKey2)); // typical case
        assertFalse(multiKey1.equals(multiKey3)); // typical case
        assertFalse(multiKey1.equals(null)); // typical case
        assertFalse(multiKey1.equals(new Object())); // typical case
    }

    // Test case for hashCode method
    @Test
    public void testHashCode() {
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);
        assertEquals(multiKey1.hashCode(), multiKey1.hashCode()); // typical case
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode()); // typical case
    }

    // Test case for toString method
    @Test
    public void testToString() {
        String key1 = "key1";
        String key2 = "key2";
        MultiKey<String> multiKey = new MultiKey<>(key1, key2);
        assertEquals("MultiKey[key1, key2]", multiKey.toString()); // typical case
    }
}