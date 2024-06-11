package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
    private static RealMatrix matrix;
    private static double small;

    @BeforeClass
    public static void setUp() {
        double[][] data = {{1.0, 0.5, 0.3},
                           {0.5, 1.0, 0.2},
                           {0.3, 0.2, 1.0}};
        matrix = MatrixUtils.createRealMatrix(data);
        small = 0.1;
    }

    @Test
    public void testRectangularCholeskyDecomposition() {
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);

        assertNotNull(decomposition);
        assertNotNull(decomposition.getRootMatrix());
        assertEquals(3, decomposition.getRootMatrix().getRowDimension());
        assertEquals(3, decomposition.getRootMatrix().getColumnDimension());
        assertEquals(3, decomposition.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecompositionWithNonPositiveDefiniteMatrix() {
        double[][] data = {{1.0, -0.5, 0.3},
                           {-0.5, 1.0, 0.2},
                           {0.3, 0.2, 1.0}};
        RealMatrix nonPositiveDefiniteMatrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(nonPositiveDefiniteMatrix, small);
    }

    @Test
    public void testGetRootMatrix() {
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix rootMatrix = decomposition.getRootMatrix();

        assertNotNull(rootMatrix);
        assertEquals(3, rootMatrix.getRowDimension());
        assertEquals(3, rootMatrix.getColumnDimension());
    }

    @Test
    public void testGetRank() {
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        int rank = decomposition.getRank();

        assertEquals(3, rank);
    }
}