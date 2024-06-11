package net.sf.javaml.core;
import static org.junit.Assert.*;
import org.junit.Test;

public class SparseInstanceTest {

    @Test
    public void testSetNoAttributes() {
        // Test setting number of attributes
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testSparseInstanceConstructor() {
        // Test constructor with default values
        SparseInstance instance = new SparseInstance();
        assertEquals(-1, instance.noAttributes());
        assertEquals(0.0, instance.get(0), 0.0001);
    }

    // Additional test cases for other methods can be added here

}