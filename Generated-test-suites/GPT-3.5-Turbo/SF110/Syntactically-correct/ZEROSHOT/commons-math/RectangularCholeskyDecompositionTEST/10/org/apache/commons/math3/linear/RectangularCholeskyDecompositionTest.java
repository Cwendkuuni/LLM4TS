package org.apache.commons.math3.linear;

import org.apache.commons.math3.util.*;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor_ThrowsNonPositiveDefiniteMatrixException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(3, 3);
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            Assert.fail("Expected NonPositiveDefiniteMatrixException was not thrown");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Pass
        }
    }
  
    @Test
    public void testConstructor_CorrectlyInitializesRootMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(0, 2, 3.0);
        matrix.setEntry(1, 0, 2.0);
        matrix.setEntry(1, 1, 5.0);
        matrix.setEntry(1, 2, 6.0);
        matrix.setEntry(2, 0, 3.0);
        matrix.setEntry(2, 1, 6.0);
        matrix.setEntry(2, 2, 9.0);
      
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
      
        Assert.assertEquals(3.0, rootMatrix.getEntry(0, 0), 0.0001);
        Assert.assertEquals(0.0, rootMatrix.getEntry(0, 1), 0.0001);
        Assert.assertEquals(0.0, rootMatrix.getEntry(0, 2), 0.0001);
        Assert.assertEquals(1.0, rootMatrix.getEntry(1, 0), 0.0001);
        Assert.assertEquals(2.0, rootMatrix.getEntry(1, 1), 0.0001);
        Assert.assertEquals(0.0, rootMatrix.getEntry(1, 2), 0.0001);
        Assert.assertEquals(0.0, rootMatrix.getEntry(2, 0), 0.0001);
        Assert.assertEquals(0.0, rootMatrix.getEntry(2, 1), 0.0001);
        Assert.assertEquals(3.0, rootMatrix.getEntry(2, 2), 0.0001);
    }
  
    @Test
    public void testGetRank_ReturnsCorrectRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(0, 2, 3.0);
        matrix.setEntry(1, 0, 2.0);
        matrix.setEntry(1, 1, 5.0);
        matrix.setEntry(1, 2, 6.0);
        matrix.setEntry(2, 0, 3.0);
        matrix.setEntry(2, 1, 6.0);
        matrix.setEntry(2, 2, 9.0);
      
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        int rank = decomposition.getRank();
      
        Assert.assertEquals(3, rank);
    }

}