package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
    @Test
    public void testRectangularCholeskyDecomposition() {
        // Test case 1: matrix with positive definite values
        double[][] data1 = {{4, -1, 1},
                            {-1, 5, -3},
                            {1, -3, 14}};
        RealMatrix matrix1 = MatrixUtils.createRealMatrix(data1);
        RectangularCholeskyDecomposition decomposition1 = new RectangularCholeskyDecomposition(matrix1);

        RealMatrix expected1 = MatrixUtils.createRealMatrix(new double[][]{{2, 0, 0},
                                                                          {-0.5, 2, 0},
                                                                          {0.5, -1, 3}});
        assertEquals(expected1, decomposition1.getRootMatrix());
        assertEquals(3, decomposition1.getRank());

        // Test case 2: matrix with non-positive definite values
        double[][] data2 = {{-1, 2, -1},
                            {2, 5, 9},
                            {-1, 9, 12}};
        RealMatrix matrix2 = MatrixUtils.createRealMatrix(data2);

        // Expecting a NonPositiveDefiniteMatrixException to be thrown
        try {
            RectangularCholeskyDecomposition decomposition2 = new RectangularCholeskyDecomposition(matrix2);
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(-1.0, e.getEntry(), 0.001);
            assertEquals(0, e.getEntryRow());
            assertEquals(0.0, e.getSmall(), 0.001);
        }

        // Test case 3: matrix with small value
        double[][] data3 = {{0.0005, -0.0001, 0.0001},
                            {-0.0001, 0.0005, -0.0003},
                            {0.0001, -0.0003, 0.0014}};
        RealMatrix matrix3 = MatrixUtils.createRealMatrix(data3);
        RectangularCholeskyDecomposition decomposition3 = new RectangularCholeskyDecomposition(matrix3, 0.00001);

        RealMatrix expected3 = MatrixUtils.createRealMatrix(new double[][]{{2.23607, 0, 0},
                                                                          {-0.14142, 2.23607, 0},
                                                                          {0.07071, -0.18306, 2.23607}});
        assertEquals(expected3, decomposition3.getRootMatrix());
        assertEquals(3, decomposition3.getRank());
    }
}