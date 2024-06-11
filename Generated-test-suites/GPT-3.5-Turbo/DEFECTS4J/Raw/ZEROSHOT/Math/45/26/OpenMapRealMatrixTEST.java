package org.apache.commons.math.linear;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    private OpenMapRealMatrix matrix;

    @Before
    public void setUp() {
        matrix = new OpenMapRealMatrix(3, 3);
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
        m.setEntry(0, 0, 1.0);
        m.setEntry(1, 1, 2.0);
        OpenMapRealMatrix result = matrix.add(m);
        Assert.assertEquals(1.0, result.getEntry(0, 0), 0.0);
        Assert.assertEquals(2.0, result.getEntry(1, 1), 0.0);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
        m.setEntry(0, 0, 1.0);
        m.setEntry(1, 1, 2.0);
        OpenMapRealMatrix result = matrix.subtract(m);
        Assert.assertEquals(-1.0, result.getEntry(0, 0), 0.0);
        Assert.assertEquals(-2.0, result.getEntry(1, 1), 0.0);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
        m.setEntry(0, 0, 1.0);
        m.setEntry(0, 1, 2.0);
        OpenMapRealMatrix result = matrix.multiply(m);
        Assert.assertEquals(0.0, result.getEntry(0, 0), 0.0);
        Assert.assertEquals(0.0, result.getEntry(0, 1), 0.0);
    }

    @Test
    public void testGetEntry() {
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        Assert.assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
        Assert.assertEquals(2.0, matrix.getEntry(1, 1), 0.0);
    }

    @Test
    public void testSetEntry() {
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        Assert.assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
        Assert.assertEquals(2.0, matrix.getEntry(1, 1), 0.0);
    }

    @Test
    public void testAddToEntry() {
        matrix.setEntry(0, 0, 1.0);
        matrix.addToEntry(0, 0, 2.0);
        Assert.assertEquals(3.0, matrix.getEntry(0, 0), 0.0);
    }

    @Test
    public void testMultiplyEntry() {
        matrix.setEntry(0, 0, 2.0);
        matrix.multiplyEntry(0, 0, 3.0);
        Assert.assertEquals(6.0, matrix.getEntry(0, 0), 0.0);
    }

}