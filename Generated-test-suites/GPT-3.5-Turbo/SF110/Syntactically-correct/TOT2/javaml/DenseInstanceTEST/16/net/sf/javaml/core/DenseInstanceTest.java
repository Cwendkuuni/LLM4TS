package net.sf.javaml.core;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class DenseInstanceTest {

    @Test
    public void testValue() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(2.0, instance.value(1), 0.0);
        assertEquals(3.0, instance.value(2), 0.0);
    }

    @Test
    public void testClear() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        instance.clear();
        assertEquals(0.0, instance.value(0), 0.0);
        assertEquals(0.0, instance.value(1), 0.0);
        assertEquals(0.0, instance.value(2), 0.0);
    }

    // Add more test cases for other public methods

    @Test
    public void testRemoveException() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        try {
            instance.remove(0);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // test passed
        }
    }
    
}