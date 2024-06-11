package net.sf.javaml.core;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;

public class SparseInstanceTest {

    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testSparseInstanceDefaultConstructor() {
        SparseInstance instance = new SparseInstance();
        assertEquals(-1, instance.noAttributes());
    }

    @Test
    public void testSparseInstanceConstructorWithDataVector() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(3, instance.size());
        assertEquals(1.0, instance.value(0), 0.001);
    }

    // Add more test cases for other methods, edge cases, and exceptions

}