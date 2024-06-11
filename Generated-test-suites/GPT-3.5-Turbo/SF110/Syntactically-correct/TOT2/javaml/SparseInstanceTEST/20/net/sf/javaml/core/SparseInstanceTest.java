package net.sf.javaml.core;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

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
        assertNotNull(instance);
    }

    @Test
    public void testSparseInstanceConstructorWithAttributes() {
        SparseInstance instance = new SparseInstance(3);
        assertEquals(3, instance.noAttributes());
    }

    @Test
    public void testSparseInstanceConstructorWithClassValue() {
        Object value = new Object();
        SparseInstance instance = new SparseInstance(3, value);
        assertEquals(value, instance.classValue());
    }

    @Test
    public void testSparseInstanceConstructorWithDataVector() {
        double[] data = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(data);
        assertEquals(3, instance.noAttributes());
    }

    // Additional test cases for other methods can be added here
    
}