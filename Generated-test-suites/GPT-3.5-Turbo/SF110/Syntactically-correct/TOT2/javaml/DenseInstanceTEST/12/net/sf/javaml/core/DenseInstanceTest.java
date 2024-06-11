package net.sf.javaml.core;
import static org.junit.Assert.*;
import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals(2.0, instance.value(1), 0.001);
    }
    
    @Test
    public void testClear() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        instance.clear();
        
        assertArrayEquals(new double[3], instance.attributes, 0.001);
    }
    
    // Additional test cases for other methods can be added here
}