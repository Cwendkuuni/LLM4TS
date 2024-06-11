package org.apache.commons.collections4.keyvalue;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testConstructorWithTwoKeys() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2);
        assertNotNull(multiKey);
        assertEquals(2, multiKey.size());
    }

    @Test
    public void testConstructorWithThreeKeys() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
        assertNotNull(multiKey);
        assertEquals(3, multiKey.size());
    }

    @Test
    public void testGetKeys() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
        Integer[] keys = multiKey.getKeys();
        assertNotNull(keys);
        assertEquals(3, keys.length);
        assertEquals(1, keys[0].intValue());
        assertEquals(2, keys[1].intValue());
        assertEquals(3, keys[2].intValue());
    }

    @Test
    public void testEquals() {
        MultiKey<Integer> multiKey1 = new MultiKey<>(1, 2, 3);
        MultiKey<Integer> multiKey2 = new MultiKey<>(1, 2, 3);
        assertTrue(multiKey1.equals(multiKey2));
    }

    @Test
    public void testHashCode() {
        MultiKey<Integer> multiKey1 = new MultiKey<>(1, 2, 3);
        MultiKey<Integer> multiKey2 = new MultiKey<>(1, 2, 3);
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    @Test
    public void testToString() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
        String expected = "MultiKey[1, 2, 3]";
        assertEquals(expected, multiKey.toString());
    }
}