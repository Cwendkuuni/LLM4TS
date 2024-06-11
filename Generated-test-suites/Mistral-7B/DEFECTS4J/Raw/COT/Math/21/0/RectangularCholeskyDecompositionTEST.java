package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;

public class RectangularCholeskyDecompositionTest {

    @org.junit.Test
    public void testRectangularCholeskyDecomposition() {
        double small = 1e-12;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0, 1.0}, {1.0, 2.0, 1.0}, {1.0, 1.0, 2.0}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{0.44721361, 0.89442754, 0.38268343}, {-0.89442754, 1.44089601, -0.44721361}, {-0.38268343, -0.44721361, 0.73439964}});
            assertEquals(expectedRoot, root);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @org.junit.Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        double small = 1e-12;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0, 1.0}, {1.0, -1.0, 1.0}, {1.0, 1.0, 2.0}});
        new RectangularCholeskyDecomposition(matrix, small);
    }

    @org.junit.Test
    public void testGetRank() {
        double small = 1e-12;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0, 1.0}, {1.0, 2.0, 1.0}, {1.0, 1.0, 2.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        int rank = decomposition.getRank();
        assertEquals(3, rank);
    }

    @org.junit.Test
    public void testGetRootMatrix() {
        double small = 1e-12;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0, 1.0}, {1.0, 2.0, 1.0}, {1.0, 1.0, 2.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix root = decomposition.getRootMatrix();
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{0.44721361, 0.89442754, 0.38268343}, {-0.89442754, 1.44089601, -0.44721361}, {-0.38268343, -0.44721361, 0.73439964}});
        assertEquals(expectedRoot, root);
    }
}