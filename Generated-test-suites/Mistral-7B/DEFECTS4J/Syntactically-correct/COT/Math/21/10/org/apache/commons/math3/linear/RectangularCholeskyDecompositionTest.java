package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;

import java.lang.reflect.InvocationTargetException;

public class RectangularCholeskyDecompositionTest {

    /**
     * Test constructor with valid input.
     * @throws Exception
     */
    @org.junit.Test
    public void testConstructorValidInput() throws Exception {
        double small = 1e-10;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3, 4}, {2, 4, 5, 6}, {3, 5, 6, 7}, {4, 6, 7, 8}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{0.44721361, -0.14112001, 0.67144692, 0.2886248},
                {-0.14112001, 1.41421366, -0.34537715, 0.19812736},
                {0.67144692, -0.34537715, 1.41421366, -0.34537715},
                {0.2886248, 0.19812736, -0.34537715, 1.14142135}});
        assertEquals(expectedRoot, decomposition.getRootMatrix());
        assertEquals(3, decomposition.getRank());
    }

    /**
     * Test constructor with non positive definite matrix.
     */
    @org.junit.Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorNonPositiveDefiniteMatrix() {
        double small = 1e-10;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3, 4}, {2, 3, 4, 6}, {3, 4, 6, 7}, {4, 6, 7, -1}});
        new RectangularCholeskyDecomposition(matrix, small);
    }

    /**
     * Test constructor with small diagonal elements.
     * @throws Exception
     */
    @org.junit.Test
    public void testConstructorSmallDiagonalElements() throws Exception {
        double small = 1e-15;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3, 4}, {2, 4, 5, 6}, {3, 5, 6, 7}, {4, 6, 7, 8e-5}});
        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix, small));
    }

    /**
     * Test getRootMatrix() with valid input.
     * @throws Exception
     */
    @org.junit.Test
    public void testGetRootMatrixValidInput() throws Exception {
        double small = 1e-10;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3, 4}, {2, 4, 5, 6}, {3, 5, 6, 7}, {4, 6, 7, 8}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{0.44721361, -0.14112001, 0.67144692, 0.2886248},
                {-0.14112001, 1.41421366, -0.34537715, 0.19812736},
                {0.67144692, -0.34537715, 1.41421366, -0.34537715},
                {0.2886248, 0.19812736, -0.34537715, 1.14142135}});
        assertEquals(expectedRoot, decomposition.getRootMatrix());
    }

    /**
     * Test getRootMatrix() with non positive definite matrix.
     * @throws Exception
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testGetRootMatrixNonPositiveDefiniteMatrix() throws Exception {
        double small = 1e-10;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3, 4}, {2, 3, 4, 6}, {3, 4, 6, 7}, {4, 6, 7, -1}});
        new RectangularCholeskyDecomposition(matrix, small);
        assertNull(new RectangularCholeskyDecomposition(matrix, small).getRootMatrix());
    }

    /**
     * Test getRank() with valid input.
     * @throws Exception
     */
    @org.junit.Test
    public void testGetRankValidInput() throws Exception {
        double small = 1e-10;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3, 4}, {2, 4, 5, 6}, {3, 5, 6, 7}, {4, 6, 7, 8}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        assertEquals(3, decomposition.getRank());
    }

    /**
     * Test getRank() with non positive definite matrix.
     * @throws Exception
     */
    @org.junit.Test
    public void testGetRankNonPositiveDefiniteMatrix() throws Exception {
        double small = 1e-10;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3, 4}, {2, 3, 4, 6}, {3, 4, 6, 7}, {4, 6, 7, -1}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        assertFalse(decomposition.getRank() > 0);
    }

}