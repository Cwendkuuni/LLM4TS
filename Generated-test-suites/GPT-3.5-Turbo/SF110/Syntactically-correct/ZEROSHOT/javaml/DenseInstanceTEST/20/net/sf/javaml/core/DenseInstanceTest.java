package net.sf.javaml.core;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DenseInstanceTest {

    private DenseInstance instance;

    @Before
    public void setUp() {
        double[] attributes = {1.0, 2.0, 3.0};
        instance = new DenseInstance(attributes);
    }

    @Test
    public void testValue() {
        assertEquals(1.0, instance.value(0), 0.0001);
        assertEquals(2.0, instance.value(1), 0.0001);
        assertEquals(3.0, instance.value(2), 0.0001);
    }

    @Test
    public void testClear() {
        instance.clear();
        double[] expected = {0.0, 0.0, 0.0};
        assertArrayEquals(expected, instance.attributes, 0.0001);
    }

    @Test
    public void testContainsKey() {
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(4.0));
    }

    // Add tests for the other methods in DenseInstance class

}

