package org.apache.commons.math.linear;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {
    
    private OpenMapRealMatrix matrix;
    
    @Before
    public void setUp() {
        matrix = new OpenMapRealMatrix(3, 3);
    }

    @Test
    public void testGetRowDimension() {
        assertEquals(3, matrix.getRowDimension());
    }
    
    @Test
    public void testGetColumnDimension() {
        assertEquals(3, matrix.getColumnDimension());
    }
    
    @Test
    public void testGetEntry() {
        // Typical use case
        assertEquals(0.0, matrix.getEntry(0, 0), 0.0001);
        
        // Edge case
        assertEquals(0.0, matrix.getEntry(2, 2), 0.0001);
        
        // Error scenario
        try {
            matrix.getEntry(3, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected exception
        }
    }
    
    @Test
    public void testSetEntry() {
        // Typical use case
        matrix.setEntry(0, 0, 2.5);
        assertEquals(2.5, matrix.getEntry(0, 0), 0.0001);
        
        // Edge case
        matrix.setEntry(2, 2, 1.0);
        assertEquals(1.0, matrix.getEntry(2, 2), 0.0001);
        
        // Error scenario
        try {
            matrix.setEntry(3, 0, 3.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected exception
        }
    }
    
    @Test
    public void testAdd() {
        // Typical use case
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(3, 3);
        m1.setEntry(0, 0, 2.5);
        OpenMapRealMatrix result = matrix.add(m1);
        assertEquals(2.5, result.getEntry(0, 0), 0.0001);
        
        // Edge case
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(3, 3);
        m2.setEntry(2, 2, 1.0);
        result = matrix.add(m2);
        assertEquals(1.0, result.getEntry(2, 2), 0.0001);
        
        // Error scenario
        OpenMapRealMatrix m3 = new OpenMapRealMatrix(2, 2);
        try {
            result = matrix.add(m3);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected exception
        }
    }
    
    @Test
    public void testSubtract() {
        // Typical use case
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(3, 3);
        m1.setEntry(0, 0, 2.5);
        OpenMapRealMatrix result = matrix.subtract(m1);
        assertEquals(-2.5, result.getEntry(0, 0), 0.0001);
        
        // Edge case
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(3, 3);
        m2.setEntry(2, 2, 1.0);
        result = matrix.subtract(m2);
        assertEquals(-1.0, result.getEntry(2, 2), 0.0001);
        
        // Error scenario
        OpenMapRealMatrix m3 = new OpenMapRealMatrix(2, 2);
        try {
            result = matrix.subtract(m3);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected exception
        }
    }
    
    @Test
    public void testMultiply() {
        // Typical use case
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(3, 1);
        m1.setEntry(0, 0, 2.5);
        OpenMapRealMatrix result = matrix.multiply(m1);
        assertEquals(0.0, result.getEntry(0, 0), 0.0001);
        
        // Edge case
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(1, 3);
        m2.setEntry(0, 0, 1.0);
        result = matrix.multiply(m2);
        assertEquals(0.0, result.getEntry(0, 0), 0.0001);
        
        // Error scenario
        OpenMapRealMatrix m3 = new OpenMapRealMatrix(2, 2);
        try {
            result = matrix.multiply(m3);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected exception
        }
    }
    
    @Test
    public void testCopy() {
        // Typical use case
        OpenMapRealMatrix copy = matrix.copy();
        assertEquals(0.0, copy.getEntry(0, 0), 0.0001);
        
        // Modify original and check if it affects the copy
        matrix.setEntry(0, 0, 2.5);
        assertNotEquals(2.5, copy.getEntry(0, 0), 0.0001);
    }
    
}