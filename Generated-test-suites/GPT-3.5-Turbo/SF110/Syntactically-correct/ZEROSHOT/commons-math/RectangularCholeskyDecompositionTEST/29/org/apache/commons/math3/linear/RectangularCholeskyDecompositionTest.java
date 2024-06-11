
package org.apache.commons.math3.linear;

import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRootMatrix() throws NonPositiveDefiniteMatrixException {
        double[][] testData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(testMatrix);
        RealMatrix result = decomposition.getRootMatrix();

        double[][] expectedData = {{2, 6, -8}, {0, 1, -5}, {0, 0, 3}};
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedData);

        assertTrue(result.equals(expectedMatrix));
    }

    @Test
    public void testRank() throws NonPositiveDefiniteMatrixException {
        double[][] testData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(testMatrix);
        int result = decomposition.getRank();

        int expectedRank = 3;

        assertEquals(expectedRank, result);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrix() throws NonPositiveDefiniteMatrixException {
        double[][] testData = {{-1, 1}, {1, -1}};
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);

        new RectangularCholeskyDecomposition(testMatrix);
    }

}
