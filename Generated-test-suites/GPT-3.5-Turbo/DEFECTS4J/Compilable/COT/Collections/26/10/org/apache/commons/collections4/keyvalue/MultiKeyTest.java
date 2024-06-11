package org.apache.commons.collections4.keyvalue;
import static org.junit.Assert.*;
import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testConstructorTwoKeys() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2);
        Integer[] expectedKeys = {1, 2};

        assertArrayEquals(expectedKeys, multiKey.getKeys());
        assertEquals(2, multiKey.size());
    }

    @Test
    public void testConstructorThreeKeys() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
        Integer[] expectedKeys = {1, 2, 3};

        assertArrayEquals(expectedKeys, multiKey.getKeys());
        assertEquals(3, multiKey.size());
    }

    @Test
    public void testConstructorFourKeys() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3, 4);
        Integer[] expectedKeys = {1, 2, 3, 4};

        assertArrayEquals(expectedKeys, multiKey.getKeys());
        assertEquals(4, multiKey.size());
    }

    @Test
    public void testConstructorFiveKeys() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3, 4, 5);
        Integer[] expectedKeys = {1, 2, 3, 4, 5};

        assertArrayEquals(expectedKeys, multiKey.getKeys());
        assertEquals(5, multiKey.size());
    }

    @Test
    public void testConstructorArrayKeys() {
        Integer[] keys = {1, 2, 3, 4, 5};
        MultiKey<Integer> multiKey = new MultiKey<>(keys);
        Integer[] expectedKeys = {1, 2, 3, 4, 5};

        assertArrayEquals(expectedKeys, multiKey.getKeys());
        assertEquals(5, multiKey.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullArray() {
        MultiKey<Integer> multiKey = new MultiKey<>(null);
    }

    @Test
    public void testEqualsAndHashCode() {
        MultiKey<Integer> multiKey1 = new MultiKey<>(1, 2);
        MultiKey<Integer> multiKey2 = new MultiKey<>(1, 2);
        MultiKey<Integer> multiKey3 = new MultiKey<>(2, 3);

        assertTrue(multiKey1.equals(multiKey2));
        assertTrue(multiKey2.equals(multiKey1));
        assertFalse(multiKey1.equals(multiKey3));
        assertFalse(multiKey3.equals(multiKey1));

        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
        assertNotEquals(multiKey1.hashCode(), multiKey3.hashCode());
    }

    @Test
    public void testToString() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2);
        String expectedToString = "MultiKey[1,2]";

        assertEquals(expectedToString, multiKey.toString());
    }
}