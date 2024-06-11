package net.sf.javaml.core;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class SparseInstanceTest {

    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        int noAttributes = 5;
        instance.setNoAttributes(noAttributes);
        assertEquals(noAttributes, instance.noAttributes());
    }
    
    @Test
    public void testSparseInstanceDefaultConstructor() {
        SparseInstance instance = new SparseInstance();
        assertEquals(-1, instance.noAttributes());
    }
    
    @Test
    public void testSparseInstanceWithNoAttributes() {
        int noAttributes = 10;
        SparseInstance instance = new SparseInstance(noAttributes);
        assertEquals(noAttributes, instance.noAttributes());
    }

    // Additional test cases for other methods can be added here
    
}