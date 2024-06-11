package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.util.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;

public class RectangularCholeskyDecompositionTest {
  
    @Test
    public void testConstructor1() {
        // matrix with all positive elements
        double[][] data = {{1.0, 2.0}, {2.0, 5.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
        assertEquals(2, rcd.getRank());
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor1_NonPositiveDefiniteMatrixException() {
        // matrix with a non-positive element at position (1, 1)
        double[][] data = {{1.0, 2.0}, {2.0, 0.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
    }
    
    @Test
    public void testConstructor2() {
        // matrix with all positive elements
        double[][] data = {{1.0, 2.0}, {2.0, 5.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, 1e-10);
        assertEquals(2, rcd.getRank());
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor2_NonPositiveDefiniteMatrixException() {
        // matrix with a non-positive element at position (1, 1)
        double[][] data = {{1.0, 2.0}, {2.0, 0.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, 1e-10);
    }
    
    @Test
    public void testGetRootMatrix() {
        double[][] data = {{1.0, 2.0}, {2.0, 5.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
        
        RealMatrix rootMatrix = rcd.getRootMatrix();
        
        assertEquals(2, rootMatrix.getRowDimension());
        assertEquals(2, rootMatrix.getColumnDimension());
        assertEquals(1.0, rootMatrix.getEntry(0, 0), 0.0001);
        assertEquals(0.0, rootMatrix.getEntry(0, 1), 0.0001);
        assertEquals(2.0, rootMatrix.getEntry(1, 0), 0.0001);
        assertEquals(1.0, rootMatrix.getEntry(1, 1), 0.0001);
    }
    
    @Test
    public void testGetRank() {
        double[][] data = {{1.0, 2.0}, {2.0, 5.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
        
        int rank = rcd.getRank();
        
        assertEquals(2, rank);
    }
}