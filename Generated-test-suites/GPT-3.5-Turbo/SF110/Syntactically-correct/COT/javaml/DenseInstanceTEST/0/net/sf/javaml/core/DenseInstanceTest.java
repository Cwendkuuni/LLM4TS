
import org.junit.Test;

import static org.junit.Assert.*;

public class DenseInstanceTest {

    @Test
    public void testConstructor1() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertArrayEquals(att, instance.value(), 0.0001);
        assertNull(instance.classValue());
    }

    @Test
    public void testConstructor2() {
        double[] att = {1.0, 2.0, 3.0};
        Object classValue = "class";
        DenseInstance instance = new DenseInstance(att, classValue);
        assertArrayEquals(att, instance.value(), 0.0001);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testConstructor3() {
        int size = 3;
        DenseInstance instance = new DenseInstance(size);
        double[] expected = new double[size];
        assertArrayEquals(expected, instance.value(), 0.0001);
        assertNull(instance.classValue());
    }

    @Test
    public void testValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(att[0], instance.value(0), 0.0001);
        assertEquals(att[1], instance.value(1), 0.0001);
        assertEquals(att[2], instance.value(2), 0.0001);
    }

    @Test
    public void testClear() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.clear();
        double[] expected = new double[att.length];
        assertArrayEquals(expected, instance.value(), 0.0001);
    }

    @Test
    public void testContainsKey() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(-1));
        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertTrue(instance.containsValue(1.00000001));
        assertTrue(instance.containsValue(2.00000001));
        assertTrue(instance.containsValue(3.00000001));
        assertFalse(instance.containsValue(4.0));
        assertFalse(instance.containsValue(0.0));
    }

    // Additional test cases for edge cases and exception handling scenarios can be added here

}
