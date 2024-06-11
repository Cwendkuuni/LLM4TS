
package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
    
    @Test
    public void testConstructor1WithPositiveDefiniteMatrix() {
        double[][] data = {
            {4, -2},
            {-2, 5}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        assertEquals(2, decomposition.getRank());
        RealMatrix expectedRootMatrix = MatrixUtils.createRealMatrix(new double[][] {
            {2, 0},
            {-1, Math.sqrt(3)}
        });
        assertEquals(expectedRootMatrix, decomposition.getRootMatrix());
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor1WithNonPositiveDefiniteMatrix() {
        double[][] data = {
            {1, 2},
            {2, 4}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        new RectangularCholeskyDecomposition(matrix);
    }
    
    @Test
    public void testConstructor2WithPositiveDefiniteMatrix() {
        double[][] data = {
            {4, -2},
            {-2, 5}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        assertEquals(2, decomposition.getRank());
        RealMatrix expectedRootMatrix = MatrixUtils.createRealMatrix(new double[][] {
            {2, 0},
            {-1, Math.sqrt(3)}
        });
        assertEquals(expectedRootMatrix, decomposition.getRootMatrix());
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor2WithNonPositiveDefiniteMatrix() {
        double[][] data = {
            {1, 2},
            {2, 4}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        new RectangularCholeskyDecomposition(matrix, 0.0);
    }
    
    @Test
    public void testGetRank() {
        double[][] data = {
            {4, -2},
            {-2, 5},
            {1, 1}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        assertEquals(2, decomposition.getRank());
    }
}
