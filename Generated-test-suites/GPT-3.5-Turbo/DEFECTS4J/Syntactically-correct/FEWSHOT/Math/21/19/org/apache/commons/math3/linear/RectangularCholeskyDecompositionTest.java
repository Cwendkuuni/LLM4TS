package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    private static final double EPSILON = 1e-10;

    @Test
    public void testConstructor() {
        double[][] a = {{1, 0}, {0, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(a);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, EPSILON);

        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(a);
        assertEquals(expectedRoot, decomposition.getRootMatrix());
        assertEquals(2, decomposition.getRank());
    }

    @Test
    public void testConstructorWithNonPositiveDefiniteMatrix() {
        double[][] a = {{1, 1}, {1, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(a);

        try {
            new RectangularCholeskyDecomposition(matrix, EPSILON);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected exception
        }
    }
}