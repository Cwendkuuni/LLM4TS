
package net.sf.javaml.core;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class DenseInstanceTest {

    @Test
    public void testValue() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        assertEquals(2.0, instance.value(1), 0.0);
    }

    @Test
    public void testClear() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        instance.clear();
        assertArrayEquals(new double[] {0.0, 0.0, 0.0}, instance.attributes, 0.0);
    }

    @Test
    public void testContainsKey() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        assertTrue(instance.containsKey(1));
        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        assertTrue(instance.containsValue(2.0));
        assertFalse(instance.containsValue(4.0));
    }

    // Add more test cases for other methods...

    @Test
    public void testRemove() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        assertThrows(UnsupportedOperationException.class, () -> {
            instance.remove(1);
        });
    }

    // Add more test cases for other methods...

    @Test
    public void testRemoveAttributes() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        instance.removeAttributes(indices);
        assertArrayEquals(new double[] {1.0, 3.0}, instance.attributes, 0.0);
    }
}

