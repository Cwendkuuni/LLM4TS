package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
    
    @Test
    public void testRectangularCholeskyDecomposition() {
        double[][] matrixData = { { 4, 1 }, { 1, 2 } };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        assertEquals(2, decomposition.getRank());
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecompositionNonPositiveMatrix() {
        double[][] matrixData = { { 1, -2 }, { -2, 4 } };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
    }
    
    @Test
    public void testRectangularCholeskyDecompositionSmall() {
        double[][] matrixData = { { 4, 1 }, { 1, 2 } };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.5);
        assertEquals(2, decomposition.getRank());
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecompositionNonPositiveMatrixSmall() {
        double[][] matrixData = { { 1, -2 }, { -2, 4 } };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.5);
    }
    
    @Test
    public void testGetRootMatrix() {
        double[][] matrixData = { { 4, 1 }, { 1, 2 } };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        double[][] expectedData = { { 2, 0 }, { 0.5, 1.5 } };
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedData);
        assertEquals(expectedMatrix, rootMatrix);
    }
    
    @Test
    public void testGetRank() {
        double[][] matrixData = { { 4, 1 }, { 1, 2 } };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        assertEquals(2, decomposition.getRank());
    }
}