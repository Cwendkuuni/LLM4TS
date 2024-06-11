package org.apache.commons.math3.linear; // or the package where your test class is located

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.LUDecomposition;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    /**
     * Test Cholesky decomposition of a real symmetric positive semidefinite matrix with small diagonal elements.
     */
    @Test
    public void testRectangularCholeskyDecomposition() {
        // Given
        double small = 1e-6;
        double[][] matrixData = new double[][] {
                {1.0, 0.5, 0.1},
                {0.5, 1.5, 0.5},
                {0.1, 0.5, 0.1}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        // When
        RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, small);

        // Then
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][] {
                {FastMath.SQRT(1.0), 0.0, 0.0},
                {0.5, FastMath.SQRT(1.5), 0.0},
                {0.1, 0.25, FastMath.SQRT(0.1)}
        }, false);
        RealMatrix expectedResult = expectedRoot.multiply(expectedRoot.transpose());

        RealMatrix root = decomposer.getRootMatrix();
        RealMatrix result = root.multiply(root.transpose());

        // Check result
        int rank = decomposer.getRank();
        assertEquals("Unexpected rank", rank, 3);
        assertArrayEquals("Unexpected result", expectedResult.getData(), result.getData(), 1e-10);

        // Additional checks
        assertNotNull(decomposer);
        assertEquals("Unexpected root matrix dimension", 3, root.getRowDimension());
        assertEquals("Unexpected root matrix columns", 3, root.getColumnDimension());
    }

    /**
     * Test Cholesky decomposition with non-positive definite matrix.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrix() {
        // Given
        double small = 1e-6;
        double[][] matrixData = new double[][] {
                {1.0, 0.5, 0.1},
                {0.5, -1.5, 0.5},
                {0.1, 0.5, 0.1}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        // When
        RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, small);

        // Expect exception
    }

}