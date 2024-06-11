package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Before;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
    private RectangularCholeskyDecomposition decomposition;
    
    @Before
    public void setUp() {
        double[][] matrixData = {
            {4, 12, -16},
            {12, 37, -43},
            {-16, -43, 98}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Failed to initialize decomposition.");
        }
    }
    
    @Test
    public void testGetRootMatrix() {
        RealMatrix root = decomposition.getRootMatrix();
        double[][] expectedData = {
            {2, 6, -8},
            {0, 1, 5},
            {0, 0, 3}
        };
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedData);
        assertTrue(root.equals(expectedMatrix));
    }
    
    @Test
    public void testGetRank() {
        int rank = decomposition.getRank();
        assertEquals(3, rank);
    }
    
    @Test
    public void testNonPositiveDefiniteMatrix() {
        double[][] matrixData = {
            {-4, 12, -16},
            {12, 37, -43},
            {-16, -43, 98}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix);
            fail("NonPositiveDefiniteMatrixException was not thrown.");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Exception successfully thrown
        }
    }
}