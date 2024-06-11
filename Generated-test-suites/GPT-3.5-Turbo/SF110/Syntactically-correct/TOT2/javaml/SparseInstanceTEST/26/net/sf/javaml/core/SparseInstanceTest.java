package net.sf.javaml.core;
import static org.junit.Assert.*;
import org.junit.Test;

public class SparseInstanceTest {

    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testSparseInstanceInitiate() {
        double[] datavector = {1.0, 0.0, 2.0, 0.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(2, instance.noAttributes());
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testValue() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 1.0);
        assertEquals(1.0, instance.value(1), 0.001);
    }

    // Add more test cases for all methods to cover edge cases and exceptions

}