package net.sf.javaml.core;
import net.sf.javaml.core.SparseInstance;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;

public class SparseInstanceTest {

    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testSparseInstanceConstructor1() {
        SparseInstance instance = new SparseInstance();
        assertEquals(-1, instance.noAttributes());
        assertEquals(0.0, instance.get(0), 0);
        // Add more assertions to test other parts of the constructor
    }

    @Test
    public void testSparseInstanceConstructor2() {
        SparseInstance instance = new SparseInstance(3);
        assertEquals(3, instance.noAttributes());
        // Add more assertions to test other parts of the constructor
    }

    // Add test methods for each public method in the SparseInstance class

}