package org.apache.commons.collections4.keyvalue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testConstructorWithTwoKeys() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2);
        assertEquals(2, multiKey.size());
        assertEquals(1, multiKey.getKey(0).intValue());
        assertEquals(2, multiKey.getKey(1).intValue());
    }

    @Test
    public void testConstructorWithThreeKeys() {
        MultiKey<String> multiKey = new MultiKey<>("a", "b", "c");
        assertEquals(3, multiKey.size());
        assertEquals("a", multiKey.getKey(0));
        assertEquals("b", multiKey.getKey(1));
        assertEquals("c", multiKey.getKey(2));
    }

    @Test
    public void testConstructorWithFourKeys() {
        MultiKey<Boolean> multiKey = new MultiKey<>(true, false, true, false);
        assertEquals(4, multiKey.size());
        assertTrue(multiKey.getKey(0));
        assertFalse(multiKey.getKey(1));
        assertTrue(multiKey.getKey(2));
        assertFalse(multiKey.getKey(3));
    }

    @Test
    public void testConstructorWithFiveKeys() {
        MultiKey<Double> multiKey = new MultiKey<>(1.2, 3.4, 5.6, 7.8, 9.0);
        assertEquals(5, multiKey.size());
        assertEquals(1.2, multiKey.getKey(0), 0);
        assertEquals(3.4, multiKey.getKey(1), 0);
        assertEquals(5.6, multiKey.getKey(2), 0);
        assertEquals(7.8, multiKey.getKey(3), 0);
        assertEquals(9.0, multiKey.getKey(4), 0);
    }
    
    @Test
    public void testConstructorWithArray() {
        Integer[] keys = {1, 2, 3};
        MultiKey<Integer> multiKey = new MultiKey<>(keys);
        
        assertEquals(3, multiKey.size());
        assertEquals(1, multiKey.getKey(0).intValue());
        assertEquals(2, multiKey.getKey(1).intValue());
        assertEquals(3, multiKey.getKey(2).intValue());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullArray() {
        MultiKey<Object> multiKey = new MultiKey<>(null);
    }

    @Test
    public void testEquals() {
        MultiKey<String> multiKey1 = new MultiKey<>("a", "b", "c");
        MultiKey<String> multiKey2 = new MultiKey<>("a", "b", "c");
        MultiKey<String> multiKey3 = new MultiKey<>("x", "y", "z");

        assertTrue(multiKey1.equals(multiKey2));
        assertFalse(multiKey1.equals(multiKey3));
    }

    @Test
    public void testHashCode() {
        MultiKey<Integer> multiKey1 = new MultiKey<>(1, 2, 3);
        MultiKey<Integer> multiKey2 = new MultiKey<>(1, 2, 3);
        MultiKey<Integer> multiKey3 = new MultiKey<>(4, 5, 6);
        
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
        assertFalse(multiKey1.hashCode() == multiKey3.hashCode());
    }
    
    @Test
    public void testToString() {
        MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
        assertEquals("MultiKey[1, 2, 3]", multiKey.toString());
    }
}