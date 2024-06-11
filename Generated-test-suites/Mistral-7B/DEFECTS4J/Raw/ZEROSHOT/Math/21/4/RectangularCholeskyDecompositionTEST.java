package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    /**
     * Test the constructor.
     */
    @Test
    public void testConstructor() {
        double small = 1e-6;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 6.0},
            {3.0, 6.0, 13.0}
        });
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        assertNotNull(decomposition.getRootMatrix());
        assertEquals(2, decomposition.getRank());
    }

    /**
     * Test that the constructor throws an exception with a non positive definite matrix.
     */
    @Test
    public void testNonPositiveDefiniteMatrixException() {
        double small = 1e-6;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 3.0},
            {2.0, -1.0, 6.0},
            {3.0, 6.0, 13.0}
        });
        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix, small));
    }

    /**
     * Test the getRootMatrix method.
     */
    @Test
    public void testGetRootMatrix() {
        double small = 1e-6;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 6.0},
            {3.0, 6.0, 13.0}
        });
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix root = decomposition.getRootMatrix();
        assertEquals(3, root.getRowDimension());
        assertEquals(2, root.getColumnDimension());
        assertEquals(3.0, root.getEntry(0, 0), Precision.SAFE_EARTH_EPSILON);
        assertEquals(1.5, root.getEntry(0, 1), Precision.SAFE_EARTH_EPSILON);
        assertEquals(-0.75, root.getEntry(1, 0), Precision.SAFE_EARTH_EPSILON);
        assertEquals(2.2360679774997897d, root.getEntry(1, 1), Precision.SAFE_EARTH_EPSILON);
        assertEquals(1.2164585425223544d, root.getEntry(2, 0), Precision.SAFE_EARTH_EPSILON);
        assertEquals(-0.6082292712611772d, root.getEntry(2, 1), Precision.SAFE_EARTH_EPSILON);
    }

    /**
     * Test the getRank method.
     */
    @Test
    public void testGetRank() {
        double small = 1e-6;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 6.0},
            {3.0, 6.0, 13.0}
        });
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        assertEquals(2, decomposition.getRank());
    }
}