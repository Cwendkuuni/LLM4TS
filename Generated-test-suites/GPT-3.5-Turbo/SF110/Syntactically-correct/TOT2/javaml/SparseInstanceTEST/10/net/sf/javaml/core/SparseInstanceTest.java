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
    public void testSparseInstanceConstructorNoAttributes() {
      SparseInstance instance = new SparseInstance(3);
      assertEquals(3, instance.noAttributes());
    }

    // Add more test cases for other methods as per the requirement

    @Test
    public void testNoAttributesEdgeCases() {
        SparseInstance instance = new SparseInstance();
        assertEquals(0, instance.noAttributes());
        
        // Test for negative noAttributes value
        instance.setNoAttributes(-5);
        assertEquals(0, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 10.0);
        instance.put(2, 20.0);
        instance.put(3, 30.0);
        instance.removeAttribute(2);
        
        assertFalse(instance.containsKey(2));
        assertEquals(10.0, instance.get(1), 0.001);
        assertEquals(30.0, instance.get(2), 0.001);
    }

    @Test
    public void testExceptionHandling() {
        SparseInstance instance = new SparseInstance();
        // Testing for IndexOutOfBoundsException when removing non-existing attribute
        try {
            instance.removeAttribute(5);
            fail("Should throw IndexOutOfBoundsException");
        } catch(IndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }
}