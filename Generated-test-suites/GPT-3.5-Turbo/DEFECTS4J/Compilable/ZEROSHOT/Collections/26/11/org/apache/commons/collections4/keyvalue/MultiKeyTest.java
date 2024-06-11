package org.apache.commons.collections4.keyvalue;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testConstructorTwoKeys() {
        Integer key1 = 1;
        Integer key2 = 2;

        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2);

        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(2, multiKey.size());
    }

    @Test
    public void testConstructorThreeKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        Integer key3 = 3;

        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2, key3);

        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(3, multiKey.size());
    }

    @Test
    public void testConstructorFourKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        Integer key3 = 3;
        Integer key4 = 4;

        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2, key3, key4);

        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(4, multiKey.size());
    }

    @Test
    public void testConstructorFiveKeys() {
        Integer key1 = 1;
        Integer key2 = 2;
        Integer key3 = 3;
        Integer key4 = 4;
        Integer key5 = 5;

        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);

        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(key5, multiKey.getKey(4));
        assertEquals(5, multiKey.size());
    }

    @Test
    public void testConstructorArrayKeys() {
        Integer[] keys = {1, 2, 3};

        MultiKey<Integer> multiKey = new MultiKey<>(keys);

        assertArrayEquals(keys, multiKey.getKeys());
        assertEquals(3, multiKey.size());
    }

    @Test
    public void testEquals() {
        Integer key1 = 1;
        Integer key2 = 2;
        Integer key3 = 3;

        MultiKey<Integer> multiKey1 = new MultiKey<>(key1, key2, key3);
        MultiKey<Integer> multiKey2 = new MultiKey<>(key1, key2, key3);
        MultiKey<Integer> multiKey3 = new MultiKey<>(key3, key2, key1);

        assertEquals(multiKey1, multiKey2);
        assertNotEquals(multiKey1, multiKey3);
    }

    @Test
    public void testHashCode() {
        Integer key1 = 1;
        Integer key2 = 2;
        Integer key3 = 3;

        MultiKey<Integer> multiKey1 = new MultiKey<>(key1, key2, key3);
        MultiKey<Integer> multiKey2 = new MultiKey<>(key1, key2, key3);

        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    @Test
    public void testToString() {
        Integer key1 = 1;
        Integer key2 = 2;
        Integer key3 = 3;

        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2, key3);
        String expectedString = "MultiKey[1, 2, 3]";

        assertEquals(expectedString, multiKey.toString());
    }

    @Test
    public void testMapUsage() {
        Integer key1 = 1;
        Integer key2 = 2;
        Integer key3 = 3;
        String value = "testValue";

        Map<MultiKey<Integer>, String> map = new HashMap<>();
        MultiKey<Integer> multiKey = new MultiKey<>(key1, key2, key3);
        map.put(multiKey, value);

        assertEquals(value, map.get(multiKey));
    }
}