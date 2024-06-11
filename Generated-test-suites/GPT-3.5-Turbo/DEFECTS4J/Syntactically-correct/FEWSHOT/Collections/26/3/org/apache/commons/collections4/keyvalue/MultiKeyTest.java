
package org.apache.commons.collections4.keyvalue;

import static org.junit.Assert.*;

import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testConstructorTwoKeys() {
        Integer key1 = 1;
        String key2 = "two";
        MultiKey<Object> multiKey = new MultiKey<>(key1, key2);
        assertArrayEquals(new Object[]{key1, key2}, multiKey.getKeys());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(2, multiKey.size());
    }

    @Test
    public void testConstructorThreeKeys() {
        Integer key1 = 1;
        String key2 = "two";
        Double key3 = 3.0;
        MultiKey<Object> multiKey = new MultiKey<>(key1, key2, key3);
        assertArrayEquals(new Object[]{key1, key2, key3}, multiKey.getKeys());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(3, multiKey.size());
    }

    @Test
    public void testConstructorFourKeys() {
        Integer key1 = 1;
        String key2 = "two";
        Double key3 = 3.0;
        Boolean key4 = true;
        MultiKey<Object> multiKey = new MultiKey<>(key1, key2, key3, key4);
        assertArrayEquals(new Object[]{key1, key2, key3, key4}, multiKey.getKeys());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(4, multiKey.size());
    }

    @Test
    public void testConstructorFiveKeys() {
        Integer key1 = 1;
        String key2 = "two";
        Double key3 = 3.0;
        Boolean key4 = true;
        Character key5 = '5';
        MultiKey<Object> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
        assertArrayEquals(new Object[]{key1, key2, key3, key4, key5}, multiKey.getKeys());
        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
        assertEquals(key3, multiKey.getKey(2));
        assertEquals(key4, multiKey.getKey(3));
        assertEquals(key5, multiKey.getKey(4));
        assertEquals(5, multiKey.size());
    }

    @Test
    public void testConstructorArrayKeysClone() {
        Integer key1 = 1;
        String key2 = "two";
        Double key3 = 3.0;
        Boolean key4 = true;
        Object[] keys = new Object[]{key1, key2, key3, key4};

        MultiKey<Object> multiKey = new MultiKey<>(keys);

        assertNotSame(keys, multiKey.getKeys());
        assertArrayEquals(keys, multiKey.getKeys());
        assertEquals(keys[0], multiKey.getKey(0));
        assertEquals(keys[1], multiKey.getKey(1));
        assertEquals(keys[2], multiKey.getKey(2));
        assertEquals(keys[3], multiKey.getKey(3));
        assertEquals(4, multiKey.size());
    }

    @Test
    public void testConstructorArrayKeysNoClone() {
        Integer key1 = 1;
        String key2 = "two";
        Double key3 = 3.0;
        Boolean key4 = true;
        Object[] keys = new Object[]{key1, key2, key3, key4};

        MultiKey<Object> multiKey = new MultiKey<>(keys, false);

        assertSame(keys, multiKey.getKeys());
        assertArrayEquals(keys, multiKey.getKeys());
        assertEquals(keys[0], multiKey.getKey(0));
        assertEquals(keys[1], multiKey.getKey(1));
        assertEquals(keys[2], multiKey.getKey(2));
        assertEquals(keys[3], multiKey.getKey(3));
        assertEquals(4, multiKey.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullKeys() {
        MultiKey<Object> multiKey = new MultiKey<>(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyInvalidIndex() {
        Integer key1 = 1;
        String key2 = "two";
        MultiKey<Object> multiKey = new MultiKey<>(key1, key2);
        multiKey.getKey(2);
    }

    @Test
    public void testEquals() {
        Integer key1 = 1;
        String key2 = "two";

        MultiKey<Object> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<Object> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<Object> multiKey3 = new MultiKey<>(key2, key1);
        Object otherObject = new Object();

        assertTrue(multiKey1.equals(multiKey1));
        assertTrue(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(multiKey3));
        assertFalse(multiKey1.equals(otherObject));
        assertFalse(multiKey1.equals(null));
    }

    @Test
    public void testHashCode() {
        Integer key1 = 1;
        String key2 = "two";
        Double key3 = 3.0;

        MultiKey<Object> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<Object> multiKey2 = new MultiKey<>(key1, key3);
        MultiKey<Object> multiKey3 = new MultiKey<>(key1, key2);

        assertNotEquals(multiKey1.hashCode(), multiKey2.hashCode());
        assertEquals(multiKey1.hashCode(), multiKey3.hashCode());
    }

    @Test
    public void testToString() {
        Integer key1 = 1;
        String key2 = "two";
        Double key3 = 3.0;

        MultiKey<Object> multiKey = new MultiKey<>(key1, key2, key3);

        assertEquals("MultiKey[1, two, 3.0]", multiKey.toString());
    }
}
