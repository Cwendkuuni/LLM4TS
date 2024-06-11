package org.apache.commons.math3.linear.test;

import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecompositionConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 3.0, 1.0}, {1.0, 1.0, 2.0}});
        double small = 0.01;
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);
        // Add assertions to check the constructor's functionality
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 3.0, 1.0}, {1.0, 1.0, 2.0}});
        double small = 0.01;
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.73205080756888}, {-0.42261892332672}, {0.42261892332672}});
        RealMatrix actualRoot = rcd.getRootMatrix();
        assertEquals(expectedRoot, actualRoot);
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 3.0, 1.0}, {1.0, 1.0, 2.0}});
        double small = 0.01;
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);
        int expectedRank = 3;
        int actualRank = rcd.getRank();
        assertEquals(expectedRank, actualRank);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonSymmetricMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0}, {2.0, 3.0}});
        double small = 0.01;
        // Add assertions to check the NonPositiveDefiniteMatrixException is thrown
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, -1.0}, {2.0, 3.0, 1.0}, {1.0, 1.0, 2.0}});
        double small = 0.01;
        // Add assertions to check the NonPositiveDefiniteMatrixException is thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeSmallThreshold() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 3.0, 1.0}, {1.0, 1.0, 2.0}});
        double small = -0.01;
        // Add assertions to check the IllegalArgumentException is thrown
    }

    @Test
    public void testDifferentRowsAndColumns() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 3.0, 1.0}, {1.0, 1.0, 3.0}});
        double small = 0.01;
        // Add assertions to check the constructor's functionality with a different number of rows and columns
    }

}