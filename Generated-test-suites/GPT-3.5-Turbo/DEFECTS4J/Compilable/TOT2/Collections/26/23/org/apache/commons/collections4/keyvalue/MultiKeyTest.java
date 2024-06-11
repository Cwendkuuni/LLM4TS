package org.apache.commons.collections4.keyvalue;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testConstructorTwoKeys() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        assertEquals("key1", multiKey.getKey(0));
        assertEquals("key2", multiKey.getKey(1));
        assertEquals(2, multiKey.size());
    }

    @Test
    public void testConstructorThreeKeys() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
        assertEquals(1, multiKey.getKey(0).intValue());
        assertEquals(2, multiKey.getKey(1).intValue());
        assertEquals(3, multiKey.getKey(2).intValue());
        assertEquals(3, multiKey.size());
    }

    @Test
    public void testEqualsSameInstance() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        assertTrue(multiKey.equals(multiKey));
    }

    @Test
    public void testHashCode() {
        MultiKey<Integer> multiKey1 = new MultiKey<>(1, 2);
        MultiKey<Integer> multiKey2 = new MultiKey<>(1, 2);
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }

    @Test
    public void testToString() {
        MultiKey<Double> multiKey = new MultiKey<>(1.5, 2.3);
        assertEquals("MultiKey[1.5, 2.3]", multiKey.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKeysArrayConstructor() {
        new MultiKey<>(null);
    }

    @Test
    public void testGetKeys() {
        MultiKey<Boolean> multiKey = new MultiKey<>(true, false);
        assertArrayEquals(new Object[]{true, false}, multiKey.getKeys());
    }
}