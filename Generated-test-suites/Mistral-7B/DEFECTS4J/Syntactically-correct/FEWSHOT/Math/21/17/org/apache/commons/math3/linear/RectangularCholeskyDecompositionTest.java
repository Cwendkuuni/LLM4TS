package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        double small = 1e-8;

        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.001, 0.5, 0.5},
            {0.5, 2.001, 1.5},
            {0.5, 1.5, 3.001}
        });

        try {
            RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = rcd.getRootMatrix();
            int rank = rcd.getRank();

            assertEquals("Unexpected rank.", 2, rank);
            assertEquals("Unexpected root matrix.", new Array2DRowRealMatrix(new double[][] {
                    {FastMath.sqrt(1.001), 0},
                    {0.5 / FastMath.sqrt(1.001), FastMath.sqrt(2.001)},
                    {0.5 / FastMath.sqrt(1.001), 1.5 / FastMath.sqrt(2.001)}
            }), root);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }

        RealMatrix correctMatrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 0.5, 0.5},
            {0.5, 1.5, 1.5},
            {0.5, 1.5, 3.0}
        });

        try {
            RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(correctMatrix, small);
            RealMatrix root = rcd.getRootMatrix();
            int rank = rcd.getRank();

            assertEquals("Unexpected rank.", 3, rank);
            assertEquals("Unexpected root matrix.", correctMatrix, root);
        } catch (NonPositiveDefiniteMatrixException e) {
            // should not be thrown
            assertNull("Unexpected NonPositiveDefiniteMatrixException.", e);
        }

        RealMatrix smallMatrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 0.0},
            {0.0, 0.0}
        });

        try {
            RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(smallMatrix, small);
            RealMatrix root = rcd.getRootMatrix();
            int rank = rcd.getRank();

            assertEquals("Unexpected rank.", 1, rank);
            assertEquals("Unexpected root matrix.", new Array2DRowRealMatrix(new double[][] {
                    {FastMath.sqrt(1.0)}
            }), root);
        } catch (NonPositiveDefiniteMatrixException e) {
            // should not be thrown
            assertNull("Unexpected NonPositiveDefiniteMatrixException.", e);
        }

        RealMatrix negativeMatrix = new Array2DRowRealMatrix(new double[][] {
            {-1.0, 0.5},
            {0.5, 2.0}
        });

        try {
            RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(negativeMatrix, small);
            RealMatrix root = rcd.getRootMatrix();
            int rank = rcd.getRank();

            assertNull("Unexpected root matrix.", root);
            assertEquals("Unexpected rank.", -1, rank);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }

        RealMatrix singularMatrix = new Array2DRowRealMatrix(new double[][] {
            {0.0, 0.0},
            {0.0, 0.0}
        });

        try {
            RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(singularMatrix, small);
            RealMatrix root = rcd.getRootMatrix();
            int rank = rcd.getRank();

            assertNull("Unexpected root matrix.", root);
            assertEquals("Unexpected rank.", 0, rank);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }

        RealMatrix nonSymmetricMatrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 0.5},
            {0.5, 2.0},
            {0.5, 1.5}
        });

        try {
            RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(nonSymmetricMatrix, small);
            RealMatrix root = rcd.getRootMatrix();
            int rank = rcd.getRank();

            assertNull("Unexpected root matrix.", root);
            assertEquals("Unexpected rank.", -1, rank);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }
}