package org.apache.commons.math3.linear;

import org.apache.commons.math3.MatrixUtils;
import org.apache.commons.math3.SpecialFunctions;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testDecompositionWithDefaultSmall() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 1.0, 0.0},
                {1.0, 2.0, 1.0},
                {0.0, 1.0, 2.0}
        });

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();

        assertEquals(3, rank);
        assertEquals(3, root.getRowDimension());
        assertEquals(rank, root.getColumnDimension());
        assertNotNull(root);

        assertEquals(1.0, root.getEntry(0, 0), 1e-14);
        assertEquals(0.408248290463863, root.getEntry(1, 0), 1e-14);
        assertEquals(1.0, root.getEntry(1, 1), 1e-14);
        assertEquals(-0.816496580927726, root.getEntry(2, 0), 1e-14);
        assertEquals(0.408248290463863, root.getEntry(2, 1), 1e-14);
        assertEquals(1.224744871391555, root.getEntry(2, 2), 1e-14);
    }

    @Test
    public void testDecompositionWithSpecifiedSmall() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 1.0, 0.0},
                {1.0, 2.0, 1.0},
                {0.0, 1.0, 2.0},
                {0.0, 0.0, 1.0}
        });

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.5);
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();

        assertEquals(3, rank);
        assertEquals(3, root.getRowDimension());
        assertEquals(rank, root.getColumnDimension());
        assertNotNull(root);

        assertEquals(1.0, root.getEntry(0, 0), 1e-14);
        assertEquals(0.408248290463863, root.getEntry(1, 0), 1e-14);
        assertEquals(1.0, root.getEntry(1, 1), 1e-14);
        assertEquals(-0.816496580927726, root.getEntry(2, 0), 1e-14);
        assertEquals(0.408248290463863, root.getEntry(2, 1), 1e-14);
        assertEquals(1.224744871391555, root.getEntry(2, 2), 1e-14);
        assertEquals(0.0, root.getEntry(2, 2), 0.5);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrix() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 1.0, 0.0},
                {1.0, 2.0, 1.0},
                {0.0, 1.0, -2.0}
        });

        new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 1.0, 0.0},
                {1.0, 2.0, 1.0},
                {0.0, 1.0, 2.0}
        });

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        int rank = decomposition.getRank();

        assertEquals(3, rank);
    }

    @Test
    public void testGetRankZeroMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {}
        });

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        int rank = decomposition.getRank();

        assertEquals(0, rank);
    }

    @Test
    public void testGetRankIdentityMatrix() {
        RealMatrix matrix = MatrixUtils.identityMatrix(3);

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        int rank = decomposition.getRank();

        assertEquals(3, rank);
    }
}