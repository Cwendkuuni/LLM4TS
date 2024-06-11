package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        double small = 1e-10;

        // test with identity matrix
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 1}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            RealMatrix expectedRoot = MatrixUtils.createRealMatrix(new double[][]{{1, 0}, {0, 1}});
            assertEquals(expectedRoot, root);
            assertEquals(2, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected NonPositiveDefiniteMatrixException");
        }

        // test with singular matrix
        matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 0}});
        try {
            new RectangularCholeskyDecomposition(matrix, small);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }

        // test with positive semidefinite matrix
        matrix = new Array2DRowRealMatrix(new double[][]{{1, 0.5}, {0.5, 2}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            RealMatrix expectedRoot = MatrixUtils.createRealMatrix(new double[][]{{1, 0.5}, {0, 1}});
            assertEquals(expectedRoot, root);
            assertEquals(2, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected NonPositiveDefiniteMatrixException");
        }

        // test with non positive definite matrix
        matrix = new Array2DRowRealMatrix(new double[][]{{1, 0.5}, {0.5, -1}});
        try {
            new RectangularCholeskyDecomposition(matrix, small);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }
}