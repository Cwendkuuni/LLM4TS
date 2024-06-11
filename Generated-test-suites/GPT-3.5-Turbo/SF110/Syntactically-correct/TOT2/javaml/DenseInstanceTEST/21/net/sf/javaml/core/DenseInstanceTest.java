package net.sf.javaml.core;
import static org.junit.Assert.*;
import org.junit.Test;

public class DenseInstanceTest {
    
    @Test
    public void testValue() {
        DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(1.0, denseInstance.value(0), 0.0001);
    }
    
    @Test
    public void testClear() {
        DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        denseInstance.clear();
        assertEquals(0.0, denseInstance.value(0), 0.0001);
    }
    
    @Test
    public void testContainsKey() {
        DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertTrue(denseInstance.containsKey(1));
    }
    
    // Add more methods tests for comprehensive testing
    
}