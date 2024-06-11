package org.apache.commons.math3.linear;

import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor() {
        double[][] matrixData = {{1, 2, 3}, {2, 5, 6}, {3, 6, 9}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double small = 0.01;

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);

        Assert.assertNotNull(decomposition);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix() {
        double[][] matrixData = {{1, 2, 3}, {2, 5, 6}, {3, 6, 8}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double small = 0.01;

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
    }

    @Test
    public void testGetRootMatrix() {
        double[][] matrixData = {{1, 2, 3}, {2, 5, 6}, {3, 6, 9}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double small = 0.01;

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);

        RealMatrix root = decomposition.getRootMatrix();

        Assert.assertNotNull(root);
    }

    @Test
    public void testGetRank() {
        double[][] matrixData = {{1, 2, 3}, {2, 5, 6}, {3, 6, 9}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        double small = 0.01;

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);

        int rank = decomposition.getRank();

        Assert.assertEquals(3, rank);
    }

}