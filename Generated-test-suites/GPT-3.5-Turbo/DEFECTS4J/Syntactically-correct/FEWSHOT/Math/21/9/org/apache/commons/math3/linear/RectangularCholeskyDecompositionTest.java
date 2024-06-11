package org.apache.commons.math3.linear;

import org.apache.commons.math3.util.FastMath;

/**
 * JUnit test cases for RectangularCholeskyDecomposition class.
 */
public class RectangularCholeskyDecompositionTest {

    @Test
    public void testGetRootMatrix() {
        // Test case 1
        RealMatrix matrix1 = new Array2DRowRealMatrix(new double[][] {
            {1, 0},
            {0, 1}
        });
        double small1 = 0.01;
        RectangularCholeskyDecomposition decomposition1 = new RectangularCholeskyDecomposition(matrix1, small1);
        RealMatrix expectedRoot1 = matrix1;
        RealMatrix actualRoot1 = decomposition1.getRootMatrix();
        assertEquals(expectedRoot1, actualRoot1);

        // Test case 2
        RealMatrix matrix2 = new Array2DRowRealMatrix(new double[][] {
            {4, 2},
            {2, 5}
        });
        double small2 = 0.01;
        RectangularCholeskyDecomposition decomposition2 = new RectangularCholeskyDecomposition(matrix2, small2);
        RealMatrix expectedRoot2 = matrix2;
        RealMatrix actualRoot2 = decomposition2.getRootMatrix();
        assertEquals(expectedRoot2, actualRoot2);

        // Test case 3
        RealMatrix matrix3 = new Array2DRowRealMatrix(new double[][] {
            {4, 0},
            {0, 5}
        });
        double small3 = 0.01;
        RectangularCholeskyDecomposition decomposition3 = new RectangularCholeskyDecomposition(matrix3, small3);
        RealMatrix expectedRoot3 = matrix3;
        RealMatrix actualRoot3 = decomposition3.getRootMatrix();
        assertEquals(expectedRoot3, actualRoot3);
    }

    @Test
    public void testGetRank() {
        // Test case 1
        RealMatrix matrix1 = new Array2DRowRealMatrix(new double[][] {
            {1, 0},
            {0, 1}
        });
        double small1 = 0.01;
        RectangularCholeskyDecomposition decomposition1 = new RectangularCholeskyDecomposition(matrix1, small1);
        int expectedRank1 = 2;
        int actualRank1 = decomposition1.getRank();
        assertEquals(expectedRank1, actualRank1);

        // Test case 2
        RealMatrix matrix2 = new Array2DRowRealMatrix(new double[][] {
            {4, 2},
            {2, 5}
        });
        double small2 = 0.01;
        RectangularCholeskyDecomposition decomposition2 = new RectangularCholeskyDecomposition(matrix2, small2);
        int expectedRank2 = 2;
        int actualRank2 = decomposition2.getRank();
        assertEquals(expectedRank2, actualRank2);

        // Test case 3
        RealMatrix matrix3 = new Array2DRowRealMatrix(new double[][] {
            {4, 0},
            {0, 5}
        });
        double small3 = 0.01;
        RectangularCholeskyDecomposition decomposition3 = new RectangularCholeskyDecomposition(matrix3, small3);
        int expectedRank3 = 2;
        int actualRank3 = decomposition3.getRank();
        assertEquals(expectedRank3, actualRank3);
    }
}