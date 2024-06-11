package org.apache.commons.collections4.keyvalue;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections4.keyvalue.MultiKey;

public class MultiKeyTest {

    @Test
    public void testConstructorTwoKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        String[] keys = multiKey.getKeys();

        assertEquals("key1", keys[0]);
        assertEquals("key2", keys[1]);
        assertEquals(2, multiKey.size());
    }

    @Test
    public void testConstructorThreeKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        String[] keys = multiKey.getKeys();

        assertEquals("key1", keys[0]);
        assertEquals("key2", keys[1]);
        assertEquals("key3", keys[2]);
        assertEquals(3, multiKey.size());
    }

    @Test
    public void testConstructorFourKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4");
        String[] keys = multiKey.getKeys();

        assertEquals("key1", keys[0]);
        assertEquals("key2", keys[1]);
        assertEquals("key3", keys[2]);
        assertEquals("key4", keys[3]);
        assertEquals(4, multiKey.size());
    }

    @Test
    public void testConstructorFiveKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3", "key4", "key5");
        String[] keys = multiKey.getKeys();

        assertEquals("key1", keys[0]);
        assertEquals("key2", keys[1]);
        assertEquals("key3", keys[2]);
        assertEquals("key4", keys[3]);
        assertEquals("key5", keys[4]);
        assertEquals(5, multiKey.size());
    }

    @Test
    public void testConstructorArrayKeys() {
        String[] keyArray = new String[] {"key1", "key2", "key3"};
        MultiKey<String> multiKey = new MultiKey<>(keyArray);
        String[] keys = multiKey.getKeys();

        assertEquals("key1", keys[0]);
        assertEquals("key2", keys[1]);
        assertEquals("key3", keys[2]);
        assertEquals(3, multiKey.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullKeys() {
        MultiKey<String> multiKey = new MultiKey<>(null);
    }

    @Test
    public void testGetKey() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        String key = multiKey.getKey(1);
        assertEquals("key2", key);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyInvalidIndex() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        String key = multiKey.getKey(3);
    }

    @Test
    public void testEquals() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2", "key3");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2", "key3");
        MultiKey<String> multiKey3 = new MultiKey<>("key1", "key2", "key4");

        assertTrue(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(multiKey3));
    }

    @Test
    public void testHashCode() {
        MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2", "key3");
        MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2", "key3");
        MultiKey<String> multiKey3 = new MultiKey<>("key1", "key2", "key4");

        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
        assertNotEquals(multiKey1.hashCode(), multiKey3.hashCode());
    }

    @Test
    public void testToString() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        String expected = "MultiKey[key1, key2, key3]";
        assertEquals(expected, multiKey.toString());
    }

    @Test
    public void testMapUsage() {
        Map<MultiKey<String>, String> map = new HashMap<>();
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
        String value = "value1";
        map.put(multiKey, value);

        String retrievedValue = map.get(multiKey);
        assertEquals(value, retrievedValue);
    }
}