package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    /**
     * Test constructor.
     * @throws Exception
     */
    @Test
    public void testConstructor() throws Exception {
        double small = 1e-10;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3, 2, 1}, {2, 3, 2}, {1, 2, 3}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        assertNotNull(decomposition.getRootMatrix());
        assertEquals(2, decomposition.getRank());
    }

    /**
     * Test with non-positive definite matrix.
     * @throws Exception
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorNonPositiveDefiniteMatrix() throws Exception {
        double small = 1e-10;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3, 2, 1}, {2, 3, -1}, {1, 2, 3}});
        new RectangularCholeskyDecomposition(matrix, small);
    }

    /**
     * Test getRootMatrix() method.
     */
    @Test
    public void testGetRootMatrix() {
        double small = 1e-10;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3, 2, 1}, {2, 3, 2}, {1, 2, 3}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix expected = RealMatrixUtils.createRealMatrix(new double[][]{{1, 1/3, 1/3}, {1/2, 1/2, 1}, {1/3, 1, 1}});
        assertEquals(expected, decomposition.getRootMatrix());
    }

    /**
     * Test getRank() method.
     */
    @Test
    public void testGetRank() {
        double small = 1e-10;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3, 2, 1}, {2, 3, 2}, {1, 2, 3}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        assertEquals(2, decomposition.getRank());
    }

    /**
     * Test with small diagonal elements.
     * @throws Exception
     */
    @Test
    public void testSmallDiagonalElements() throws Exception {
        double small = 1e-15;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3, 2, 1e-15}, {2, 3, 2}, {1, 2, 3}});
        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, small);
            fail("Exception expected");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
        assertNull(decomposition);
    }

    /**
     * Test with small off-diagonal elements.
     * @throws Exception
     */
    @Test
    public void testSmallOffDiagonalElements() throws Exception {
        double small = 1e-15;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3, 2, 1}, {2, 3, 1e-15}, {1, 2, 3}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        assertEquals(2, decomposition.getRank());
        RealMatrix expected = RealMatrixUtils.createRealMatrix(new double[][]{{1, 1/3, 1/3}, {1/2, 1/2, 1}, {1/3, 1, 1}});
        assertEquals(expected, decomposition.getRootMatrix());
    }

    /**
     * Test with identity matrix.
     */
    @Test
    public void testIdentityMatrix() {
        double small = 1e-15;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3, 0, 0}, {0, 3, 0}, {0, 0, 3}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix expected = RealMatrixUtils.createIdentityMatrix(3);
        assertEquals(expected, decomposition.getRootMatrix());
        assertEquals(3, decomposition.getRank());
    }

}