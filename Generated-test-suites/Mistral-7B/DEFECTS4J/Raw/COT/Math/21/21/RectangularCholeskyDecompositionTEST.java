package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;

/**
 * Test class for {@link RectangularCholeskyDecomposition}.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        double small = 1e-5;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 0.1, 0.2},
            {0.1, 1.0, 0.3},
            {0.2, 0.3, 1.0}
        });
        RectangularCholeskyDecomposition decomposableMatrix = new RectangularCholeskyDecomposition(matrix, small);
        assertNotNull(decomposableMatrix.getRootMatrix());
        assertNotNull(decomposableMatrix.getRank());
    }

    /**
     * Test constructor with non positive definite matrix.
     */
    @org.junit.Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix() {
        double small = 1e-5;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 0.1, 0.2},
            {0.1, -1.0, 0.3},
            {0.2, 0.3, 1.0}
        });
        new RectangularCholeskyDecomposition(matrix, small);
    }

    /**
     * Test getRootMatrix method.
     */
    @org.junit.Test
    public void testGetRootMatrix() {
        double small = 1e-5;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 0.1, 0.2},
            {0.1, 1.0, 0.3},
            {0.2, 0.3, 1.0}
        });
        RectangularCholeskyDecomposition decomposableMatrix = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] {
            {1.0, 0.5, 0.2},
            {0.1, 0.7071067811865476, 0.3},
            {0.2, 0.17320508075688772, 1.0}
        });
        assertEquals(expected, decomposableMatrix.getRootMatrix());
    }

    /**
     * Test getRank method.
     */
    @org.junit.Test
    public void testGetRank() {
        double small = 1e-5;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 0.1, 0.2},
            {0.1, 1.0, 0.3},
            {0.2, 0.3, 1.0}
        });
        RectangularCholeskyDecomposition decomposableMatrix = new RectangularCholeskyDecomposition(matrix, small);
        int expected = 3;
        assertEquals(expected, decomposableMatrix.getRank());
    }

    /**
     * Test getRank method with singular value decomposition.
     */
    @org.junit.Test
    public void testGetRankWithSingularValueDecomposition() {
        double small = 1e-5;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 0.1, 0.2},
            {0.1, 1.0, 0.3},
            {0.2, 0.3, 1.0}
        });
        SingularValueDecomposition svd = new SingularValueDecomposition(matrix);
        RealMatrix expected = new Array2DRowRealMatrix(svd.getU().transpose().getData());
        RectangularCholeskyDecomposition decomposableMatrix = new RectangularCholeskyDecomposition(matrix, small);
        assertEquals(expected, decomposableMatrix.getRootMatrix());
        int rank = decomposableMatrix.getRank();
        int expectedRank = svd.getS().getRowDimension();
        assertEquals(expectedRank, rank);
    }

}