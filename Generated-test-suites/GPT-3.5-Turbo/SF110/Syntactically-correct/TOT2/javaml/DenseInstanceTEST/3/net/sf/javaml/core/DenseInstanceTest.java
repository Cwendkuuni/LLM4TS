package net.sf.javaml.core;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class DenseInstanceTest {

    @Test
    public void testValue() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(2.0, instance.value(1), 0.001);
    }

    @Test
    public void testClear() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        instance.clear();
        assertEquals(0.0, instance.value(1), 0.001);
    }

    @Test
    public void testContainsKey() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertTrue(instance.containsKey(1));
        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertTrue(instance.containsValue(2.0));
        assertFalse(instance.containsValue(4.0));
    }

    // Add more test cases for the remaining methods

}