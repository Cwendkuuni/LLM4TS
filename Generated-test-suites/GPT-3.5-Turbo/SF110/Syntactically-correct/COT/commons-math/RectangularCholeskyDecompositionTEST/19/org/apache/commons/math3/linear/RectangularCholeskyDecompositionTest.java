package org.apache.commons.math3.linear;

import org.apache.commons.math3.util.*;

public class RectangularCholeskyDecompositionTest {

    private RectangularCholeskyDecomposition decomposition;

    @Test
    public void testConstructor() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4, 12}, {12, 37}});
        decomposition = new RectangularCholeskyDecomposition(matrix);
        Assert.assertNotNull(decomposition);
    }

    @Test
    public void testGetRootMatrix() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4, 12}, {12, 37}});
        decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        Assert.assertEquals(4.0, rootMatrix.getEntry(0, 0), 0.0);
        Assert.assertEquals(12.0, rootMatrix.getEntry(1, 0), 0.0);
        Assert.assertEquals(1.0, rootMatrix.getEntry(1, 1), 0.0);
    }

    @Test
    public void testGetRank() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4, 12}, {12, 37}});
        decomposition = new RectangularCholeskyDecomposition(matrix);
        int rank = decomposition.getRank();
        Assert.assertEquals(2, rank);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1, 1}, {1, 2}});
        decomposition = new RectangularCholeskyDecomposition(matrix);
    }

}