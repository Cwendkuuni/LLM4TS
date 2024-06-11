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
        OpenMapRealMatrix matrixToAdd = new OpenMapRealMatrix(3, 3);
        // add values to the matrix to be added
        matrixToAdd.setEntry(0, 0, 1.0);
        matrixToAdd.setEntry(1, 1, 2.0);
        matrixToAdd.setEntry(2, 2, 3.0);
        
        OpenMapRealMatrix result = matrix.add(matrixToAdd);
        
        // check if the addition is correct
        Assert.assertEquals(1.0, result.getEntry(0, 0), 0.0);
        Assert.assertEquals(2.0, result.getEntry(1, 1), 0.0);
        Assert.assertEquals(3.0, result.getEntry(2, 2), 0.0);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrixToSubtract = new OpenMapRealMatrix(3, 3);
        // add values to the matrix to be subtracted
        matrixToSubtract.setEntry(0, 0, 1.0);
        matrixToSubtract.setEntry(1, 1, 2.0);
        matrixToSubtract.setEntry(2, 2, 3.0);
        
        OpenMapRealMatrix result = matrix.subtract(matrixToSubtract);
        
        // check if the subtraction is correct
        Assert.assertEquals(-1.0, result.getEntry(0, 0), 0.0);
        Assert.assertEquals(-2.0, result.getEntry(1, 1), 0.0);
        Assert.assertEquals(-3.0, result.getEntry(2, 2), 0.0);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrixToMultiply = new OpenMapRealMatrix(3, 3);
        // add values to the matrix to be multiplied
        matrixToMultiply.setEntry(0, 0, 2.0);
        matrixToMultiply.setEntry(1, 1, 3.0);
        matrixToMultiply.setEntry(2, 2, 4.0);
        
        OpenMapRealMatrix result = matrix.multiply(matrixToMultiply);
        
        // check if the multiplication is correct
        Assert.assertEquals(0.0, result.getEntry(0, 0), 0.0);
        Assert.assertEquals(0.0, result.getEntry(1, 1), 0.0);
        Assert.assertEquals(0.0, result.getEntry(2, 2), 0.0);
    }

    @Test
    public void testGetEntry() {
        // test getting a valid entry
        matrix.setEntry(1, 2, 5.0);
        Assert.assertEquals(5.0, matrix.getEntry(1, 2), 0.0);
        
        // test getting an invalid entry
        Assert.assertEquals(0.0, matrix.getEntry(0, 0), 0.0);
    }

    @Test
    public void testSetEntry() {
        matrix.setEntry(2, 1, 7.0);
        Assert.assertEquals(7.0, matrix.getEntry(2, 1), 0.0);
    }

    @Test
    public void testAddToEntry() {
        // test adding to a valid entry
        matrix.setEntry(2, 2, 2.0);
        matrix.addToEntry(2, 2, 3.0);
        Assert.assertEquals(5.0, matrix.getEntry(2, 2), 0.0);
        
        // test adding to an invalid entry
        matrix.addToEntry(0, 0, 1.0);
        Assert.assertEquals(0.0, matrix.getEntry(0, 0), 0.0);
    }

    @Test
    public void testMultiplyEntry() {
        // test multiplying a valid entry
        matrix.setEntry(1, 1, 2.0);
        matrix.multiplyEntry(1, 1, 3.0);
        Assert.assertEquals(6.0, matrix.getEntry(1, 1), 0.0);
        
        // test multiplying an invalid entry
        matrix.multiplyEntry(0, 0, 5.0);
        Assert.assertEquals(0.0, matrix.getEntry(0, 0), 0.0);
    }

    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix createdMatrix = matrix.createMatrix(2, 2);
        Assert.assertEquals(2, createdMatrix.getRowDimension());
        Assert.assertEquals(2, createdMatrix.getColumnDimension());
    }
}